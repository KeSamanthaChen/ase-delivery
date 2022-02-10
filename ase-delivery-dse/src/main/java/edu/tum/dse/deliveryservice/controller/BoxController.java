package edu.tum.dse.deliveryservice.controller;

//import com.netflix.discovery.converters.Auto;

import edu.tum.dse.deliveryservice.exceptions.BoxNotAvailableException;
import edu.tum.dse.deliveryservice.model.*;
import edu.tum.dse.deliveryservice.service.BoxService;
import edu.tum.dse.deliveryservice.service.DeliveryService;
import edu.tum.dse.deliveryservice.service.EmailService;
import edu.tum.dse.deliveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery/boxes")
public class BoxController {

    @Autowired
    BoxService boxService;
    @Autowired
    DeliveryService deliveryService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @GetMapping("")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public List<Box> getAllBoxes() {
        return boxService.getAllBoxes();
    }

    @GetMapping("/{id}")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public Box getBoxById(@PathVariable String id) {
        return boxService.getBoxById(id);
    }

    @PostMapping("")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public Box createBox(@RequestBody Box box) {
        box.setState(Box.BoxStateEnum.AVAILABLE);
        return boxService.createBox(box);
    }

    @PutMapping("")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public Box updateBox(@RequestBody Box box) {
        return this.boxService.updateBox(box);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public ResponseEntity<String> deleteBox(@PathVariable String id) {
        try {
            this.boxService.deleteBox(id);
            return new ResponseEntity<>("Box with " + id + "deleted", HttpStatus.OK);
        } catch (BoxNotAvailableException e) {
            return new ResponseEntity<>("Box  " + id + " is not available!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/deliveries")
    @PreAuthorize(("hasAuthority('" + UserRole.DISPATCHER + "')"))
    public List<Delivery> getDeliveriesFromBox(@PathVariable String id) {
        return this.deliveryService.getDeliveryByBox(id);
    }

    @PostMapping("/box/auth")
    @PreAuthorize(("hasAuthority('" + SystemRole.BOX + "')"))
    public ResponseEntity authBox(@RequestBody BoxAuthRequest authRequest) {
        Boolean auth = boxService.authBoxWithAccount(authRequest.getBox_id(), authRequest.getRfid());
        if (auth) return new ResponseEntity<>("pass", HttpStatus.OK);
        else return new ResponseEntity<>("no pass", HttpStatus.OK);
    }

    @PutMapping("/close")
    @PreAuthorize(("hasAuthority('" + SystemRole.BOX + "')"))
    public ResponseEntity<String> closeBox(@RequestBody CloseBoxRequest closeBoxRequest) {
        Box box = boxService.getBoxById(closeBoxRequest.getBox_id());
        if (box != null) {
            List<Delivery> deliveries = deliveryService.getDeliveryByBox(box.getId());
            Account customer = this.userService.getAccount(box.getCustomerName());
            if (box.getState() == Box.BoxStateEnum.ASSIGNED) {
                box.setState(Box.BoxStateEnum.FILLED);
                boxService.updateBox(box);
                for (Delivery delivery : deliveries) {
                    if (delivery.getStatuses().size() == 2) {
                        delivery.getStatuses().add(new DeliveryStatus(DeliveryStatus.DeliveryStatusEnum.DELIVERED));
                        deliveryService.updateDelivery(delivery);
                        this.emailService.sentDeliveryInsideBoxMail(customer, delivery);
                    }
                }
            } else if (box.getState() == Box.BoxStateEnum.FILLED) {
                int stillInDepositCount = 0;
                boxService.updateBox(box);
                for (Delivery delivery : deliveries) {
                    if (delivery.getStatuses().size() == 1) {
                        stillInDepositCount += 1;
                    }
                    if (delivery.getStatuses().size() == 2) {
                        delivery.getStatuses().add(new DeliveryStatus(DeliveryStatus.DeliveryStatusEnum.COMPLETE));
                        deliveryService.updateDelivery(delivery);
                        this.emailService.sentDeliveriesCollected(customer, null);
                    }
                }
                if (stillInDepositCount == 0) {
                    box.setState(Box.BoxStateEnum.AVAILABLE);
                    box.setCustomerName(null);
                    box.setDelivererName(null);
                } else {
                    box.setState(Box.BoxStateEnum.ASSIGNED);
                }
                boxService.updateBox(box);
            }
            return new ResponseEntity<>("box & deliveries updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("no such box", HttpStatus.BAD_REQUEST);
    }
}
