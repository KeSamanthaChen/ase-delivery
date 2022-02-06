package edu.tum.ase.authenticationservice.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "credentials")
public class AseDeliveryUser {

    @Id
    private String id;

    @Indexed(unique = true)
    @NonNull
    private String username;

    private String password;

    private String role;

    public AseDeliveryUser() {}

    public AseDeliveryUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
