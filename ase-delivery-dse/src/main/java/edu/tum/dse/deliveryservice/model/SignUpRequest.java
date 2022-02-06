package edu.tum.dse.deliveryservice.model;

/**
 * Class for data transfer from the client to create a new user entity
 */
public class SignUpRequest extends UpdateRequest {

    private String password;

    public String getPassword() {
        return password;
    }
}
