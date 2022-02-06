package edu.tum.dse.deliveryservice.client;

import edu.tum.dse.deliveryservice.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class AuthClient {

    @Autowired
    private RestTemplate restTemplate;


    private final static String AUTH_LOC = "lb://authentication-service/api/auth";

    private final static String AUTH_LOC_CREDENTIALS = AUTH_LOC + "/credentials";


    public Credentials createCredentials(Credentials credentials) throws RestClientException {
        HttpEntity<Credentials> request = new HttpEntity<>(credentials);
        ResponseEntity<Credentials> credentialsResponseEntity = restTemplate.exchange(AUTH_LOC_CREDENTIALS, HttpMethod.POST, request, Credentials.class);
        return credentialsResponseEntity.getBody();
    }

    public Credentials updateCredentials(Credentials credentials) {
        HttpEntity<Credentials> request = new HttpEntity<>(credentials);
        ResponseEntity<Credentials> credentialsResponseEntity = restTemplate.exchange(AUTH_LOC_CREDENTIALS, HttpMethod.PUT, request, Credentials.class);
        return credentialsResponseEntity.getBody();
    }


    public void deleteCredentials(String username) {
        restTemplate.delete(AUTH_LOC_CREDENTIALS + "/" + username);
    }

    public String getUserRole(String username) {
        ResponseEntity<String> credentialsResponseEntity = restTemplate.getForEntity(AUTH_LOC_CREDENTIALS + "/" + username + "/role", String.class);
        return credentialsResponseEntity.getBody();
    }

    public List<String> getUsernamesByRole(String role) {
        ResponseEntity<String[]> usernameResponseEntity = restTemplate.getForEntity(AUTH_LOC_CREDENTIALS +"?role={roleName}", String[].class, role);
        if(usernameResponseEntity.hasBody()) {
        return Arrays.asList(usernameResponseEntity.getBody());
        } else {
            return new ArrayList<>();
        }
    }

    public byte[] getPublicKey() {
        ResponseEntity<byte[]> publicKey = restTemplate.getForEntity(AUTH_LOC + "/public-key", byte[].class);
        return publicKey.getBody();
    }


}
