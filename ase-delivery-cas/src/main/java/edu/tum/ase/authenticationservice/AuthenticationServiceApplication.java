package edu.tum.ase.authenticationservice;

import edu.tum.ase.authenticationservice.model.AseDeliveryUser;
import edu.tum.ase.authenticationservice.model.SystemRole;
import edu.tum.ase.authenticationservice.model.UserRole;
import edu.tum.ase.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
@EnableEurekaClient
public class AuthenticationServiceApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {

        // TODO clear on production
        // Dummy user accounts to start/test the system
        if (this.userService.getUsers().size() == 0) {
            AseDeliveryUser dummyCustomer = new AseDeliveryUser(UserRole.CUSTOMER, UserRole.CUSTOMER, UserRole.CUSTOMER);
            AseDeliveryUser dummyCustomer2 = new AseDeliveryUser(UserRole.CUSTOMER + "2", UserRole.CUSTOMER, UserRole.CUSTOMER);
            AseDeliveryUser dummyDispatcher = new AseDeliveryUser(UserRole.DISPATCHER, UserRole.DISPATCHER, UserRole.DISPATCHER);
            AseDeliveryUser dummyDeliverer = new AseDeliveryUser(UserRole.DELIVERER, UserRole.DELIVERER, UserRole.DELIVERER);
            userService.saveUser(dummyDeliverer);
            userService.saveUser(dummyDispatcher);
            userService.saveUser(dummyCustomer);
            userService.saveUser(dummyCustomer2);
        }

        this.initSystemAccounts();
    }

    private void initSystemAccounts() {
        try {
            this.userService.loadUserByUsername("Box");
        } catch (UsernameNotFoundException e) {
            this.userService.saveUser(new AseDeliveryUser(SystemRole.BOX, "someStrongPassword", SystemRole.BOX));
        }
    }
}
