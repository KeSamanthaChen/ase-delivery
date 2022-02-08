package edu.tum.dse.deliveryservice.controller;

import edu.tum.dse.deliveryservice.client.CookieManager;
import edu.tum.dse.deliveryservice.model.*;
import edu.tum.dse.deliveryservice.service.DeliveryService;
import edu.tum.dse.deliveryservice.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delivery")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryService deliveryService;


    @GetMapping("/users")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public List<Account> getUsers(@RequestParam Optional<String> role) {
        if (role.isEmpty()) {
            return this.userService.getUsers();
        } else {
            return this.userService.getUsersByRole(role.get());
        }
    }

    @GetMapping("/users/{username}")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "') or #username == authentication.principal.username"))
    public Account getUser(@PathVariable String username) {
        return this.userService.getAccount(username);
    }

    @PostMapping("/users")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public ResponseEntity<Account> createUser(@RequestBody SignUpRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/users")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public Account updateUser(@RequestBody UpdateRequest request) {
        return this.userService.updateUser(request);
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public void deleteUser(@PathVariable String username) {
        this.userService.deleteUser(username);
    }

    @GetMapping("/users/{id}/deliveries")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "') or @authorizationService.isIdFromUser(#id, authentication.principal.username)"))
    public List<Delivery> getUserDeliveries(@PathVariable String id) {
        return this.deliveryService.getDeliveriesWithUserId(id);
    }
}
