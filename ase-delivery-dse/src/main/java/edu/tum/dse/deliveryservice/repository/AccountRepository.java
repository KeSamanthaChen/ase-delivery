package edu.tum.dse.deliveryservice.repository;

import edu.tum.dse.deliveryservice.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface AccountRepository extends MongoRepository<Account, String>, CustomAccountRepository {

    Account findAccountByUsername(String username);

    Account findAccountById(String id);

    Account findAccountByRfidToken(String rfid);

    List<Account> findAllByUsernameIn(List<String> username);

    void deleteByUsername(String username);

}
