package edu.tum.dse.deliveryservice.model;

/**
 * Class only for data transfer.
 * Transfer credentials to the authentication service.
 */
public class Credentials {

    private String username;

    private String role;

    private String password;

    public Credentials() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
