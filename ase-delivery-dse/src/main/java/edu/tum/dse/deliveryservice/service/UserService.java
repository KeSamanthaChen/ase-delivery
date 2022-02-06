package edu.tum.dse.deliveryservice.service;

import edu.tum.dse.deliveryservice.client.AuthClient;
import edu.tum.dse.deliveryservice.model.*;
import edu.tum.dse.deliveryservice.repository.AccountRepository;
import edu.tum.dse.deliveryservice.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private DeliveryService deliveryService;

    public List<Account> getUsers() {
        return this.accountRepository.findAll();
    }

    public List<Account> getUsersByRole(String role) {
        List<String> usernamesOfThisRole = this.authClient.getUsernamesByRole(role);
        return this.accountRepository.findAllByUsernameIn(usernamesOfThisRole);
    }

    public Account getAccountByRFIDToken(String rfid) {
        return this.accountRepository.findAccountByRfidToken(rfid);
    }

    public Account getAccount(String username) {
        return this.accountRepository.findAccountByUsername(username);
    }

    public Account getById(String id) {
        return this.accountRepository.findAccountById(id);
    }

    public ResponseEntity<Account> createUser(SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();

        if (accountRepository.findAccountByUsername(username) == null) {

            Account account = createAccount(signUpRequest);
            account.setRfidToken(tokenGenerator.generateToken());
            this.accountRepository.save(account);
            Credentials credentials = createCredentials(signUpRequest);
            try {
                this.authClient.createCredentials(credentials);
            } catch (RestClientException e) {
                this.accountRepository.deleteByUsername(username);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public Account updateUser(UpdateRequest request) {
        String currentRole = null;
        try {
            currentRole = this.authClient.getUserRole(request.getUsername());
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // if there is a change in role -> check if that is allowed
        if(!currentRole.equals(request.getRole())) {
            if(currentRole.equals(UserRole.DELIVERER) || currentRole.equals(UserRole.CUSTOMER)) {
                List<Delivery> deliveries = this.getDeliveriesByUsername(request.getUsername());
                if(!deliveries.isEmpty()) {
                    // There exist some deliveries => Don't allow change user role!
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            }
        }

        // Everything is okay, so apply update
        Account account = createAccount(request);
        this.accountRepository.updateAccount(account);

        Credentials credentials = new Credentials();
        credentials.setUsername(request.getUsername());
        credentials.setRole(request.getRole());

        try {
            this.authClient.updateCredentials(credentials);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        return this.getAccount(account.getUsername());
    }

    public void deleteUser(String username) {
        List<Delivery> deliveries = this.getDeliveriesByUsername(username);
        // allow deleting only users if there are no deliveries associated with him
        if (deliveries.isEmpty()) {
            try {
                this.authClient.deleteCredentials(username);
            } catch (RestClientException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            this.accountRepository.deleteByUsername(username);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Account createAccount(UpdateRequest signUpRequest) {
        Account account = new Account();
        account.setUsername(signUpRequest.getUsername());
        account.setEmail(signUpRequest.getEmail());
        return account;
    }

    private Credentials createCredentials(SignUpRequest signUpRequest) {
        Credentials credentials = new Credentials();
        credentials.setUsername(signUpRequest.getUsername());
        credentials.setPassword(signUpRequest.getPassword());
        credentials.setRole(signUpRequest.getRole());
        return credentials;
    }

    public Account createUserWithoutSignupRequest(Account user) {
        return this.accountRepository.save(user);
    }

    private List<Delivery> getDeliveriesByUsername(String username) {
        Account account = this.getAccount(username);
        return this.deliveryService.getDeliveriesWithUserId(account.getId());
    }
}
