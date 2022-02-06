package edu.tum.dse.deliveryservice.service;

import edu.tum.dse.deliveryservice.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationService {

    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryService deliveryService;

    public boolean isIdFromUser(String id, String username) {
        Account account = this.userService.getAccount(username);
        return  account.getId().equals(id);
    }

    public boolean isDeliveryAssignedToDeliverer(String id, String delivererUsername) {
        return this.deliveryService.getDeliveryById(id).getDeliverer().getUsername().equals(delivererUsername);
    }
}
