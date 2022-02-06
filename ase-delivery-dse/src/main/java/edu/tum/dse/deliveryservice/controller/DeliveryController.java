package edu.tum.dse.deliveryservice.controller;

import edu.tum.dse.deliveryservice.exceptions.BoxNotAvailableException;
import edu.tum.dse.deliveryservice.exceptions.InvalidDeliveryStatusException;
import edu.tum.dse.deliveryservice.model.Delivery;
import edu.tum.dse.deliveryservice.model.DeliveryStatus;
import edu.tum.dse.deliveryservice.model.UserRole;
import edu.tum.dse.deliveryservice.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @GetMapping("/deliveries")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @PostMapping("/deliveries")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        try {
            return new ResponseEntity<>(deliveryService.createDelivery(delivery), HttpStatus.OK);
        } catch (BoxNotAvailableException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/deliveries/{id}")
    @PostAuthorize("hasAuthority('" + UserRole.DISPATCHER + "')" + " " +
            "or returnObject.customer.username == authentication.principal.username" +
            " or returnObject.deliverer.username == authentication.principal.username")
    public Delivery getDeliveryById(@PathVariable String id) {
        return deliveryService.getDeliveryById(id);
    }


    @GetMapping("/deliveries/user-list")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "') or @authorizationService.isIdFromUser(#id, authentication.principal.username)"))
    public List<Delivery> getDeliveriesWithUserId(@RequestParam(value = "user") String id) {
        return deliveryService.getDeliveriesWithUserId(id);
    }


    @PutMapping("/deliveries/{id}/delivering")
    @PreAuthorize(("hasAuthority('" + UserRole.DELIVERER + "')  and @authorizationService.isDeliveryAssignedToDeliverer(#id, authentication.principal.username)"))
    public ResponseEntity<Delivery> setDeliveryDelivering(@PathVariable String id) {
        try {
            Optional<Delivery> optionalDelivery = this.deliveryService.updateDeliveryStatusDelivering(id);
            return optionalDelivery.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        } catch (InvalidDeliveryStatusException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deliveries/{id}")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public void deleteDelivery(@PathVariable String id) {
        this.deliveryService.deleteDelivery(id);
    }

    @PutMapping("/deliveries/{id}")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public Delivery updateDelivery(@RequestBody Delivery newDelivery, @PathVariable String id) {
        Delivery oldDelivery = deliveryService.getDeliveryById(id);
        oldDelivery.setDeliverer(newDelivery.getDeliverer());
        oldDelivery.setCustomer(newDelivery.getCustomer());
        oldDelivery.setStatuses(newDelivery.getStatuses());
        oldDelivery.setTargetBox(newDelivery.getTargetBox());
        oldDelivery.setTrackingCode(newDelivery.getTrackingCode());
        deliveryService.updateDelivery(oldDelivery);
        return deliveryService.getDeliveryById(id);
    }

}
