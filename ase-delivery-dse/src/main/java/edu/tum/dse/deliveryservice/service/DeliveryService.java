package edu.tum.dse.deliveryservice.service;

import edu.tum.dse.deliveryservice.exceptions.BoxNotAvailableException;
import edu.tum.dse.deliveryservice.exceptions.InvalidDeliveryStatusException;
import edu.tum.dse.deliveryservice.model.Account;
import edu.tum.dse.deliveryservice.model.Box;
import edu.tum.dse.deliveryservice.model.Delivery;
import edu.tum.dse.deliveryservice.model.DeliveryStatus;
import edu.tum.dse.deliveryservice.repository.AccountRepository;
import edu.tum.dse.deliveryservice.repository.BoxRepository;
import edu.tum.dse.deliveryservice.repository.DeliveryRepository;
import edu.tum.dse.deliveryservice.utils.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BoxService boxService;

    @Autowired
    private UserService userService;

    public Delivery createDelivery(Delivery delivery) throws BoxNotAvailableException {
        String id = delivery.getTargetBox().getId();
        Box box = this.boxService.getBoxById(id);
        Box.BoxStateEnum state = box.getState();
        String delivererName = userService.getById(delivery.getDeliverer().getId()).getUsername();
        String customerName = userService.getById(delivery.getCustomer().getId()).getUsername();
        // Assign only if box is available or delivery is assigned to the same customer
        if (state == Box.BoxStateEnum.AVAILABLE || (state == Box.BoxStateEnum.ASSIGNED && box.getCustomerName().equals(customerName))) {
            box.setState(Box.BoxStateEnum.ASSIGNED);
            box.setDelivererName(delivererName);
            box.setCustomerName(customerName);
            boxService.updateBox(box);
        } else {
            throw new BoxNotAvailableException();
        }
        delivery.getStatuses().add(new DeliveryStatus(DeliveryStatus.DeliveryStatusEnum.ORDERED));
        Delivery res = deliveryRepository.save(delivery);
        Delivery returnedRes = this.getDeliveryById(res.getTrackingCode());
        this.emailService.sentDeliveryCreatedMail(returnedRes.getCustomer(), returnedRes);
        return returnedRes;
    }

    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getDeliveryByBox(String box_id) {
        return deliveryRepository.getAllByTargetBox(box_id);
    }


    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public List<Delivery> getDeliveriesWithUserId(String id) {
        return deliveryRepository.findAllByCustomer_IdOrDeliverer_Id(id, id);
    }


    public Delivery getDeliveryById(String id) {
        return deliveryRepository.findById(id).orElse(null);
    }

    public Optional<Delivery> updateDeliveryStatusDelivering(String deliveryId) throws InvalidDeliveryStatusException {
        DeliveryStatus deliveryStatus = new DeliveryStatus(DeliveryStatus.DeliveryStatusEnum.DELIVERING);
        Optional<Delivery> optionalDelivery = this.deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            List<DeliveryStatus> currentStatuses = delivery.getStatuses();
            // Can only update to Delivering if previous status is ORDERED
            if (currentStatuses.size() == 1 && currentStatuses.get(0).getStatus() == DeliveryStatus.DeliveryStatusEnum.ORDERED) {
                delivery.getStatuses().add(deliveryStatus);
                this.updateDelivery(delivery);
            } else {
                throw new InvalidDeliveryStatusException();
            }
        }
        return optionalDelivery;
    }

    public void deleteDelivery(String id) {
        Optional<Delivery> optionalDelivery = this.deliveryRepository.findById(id);
        if (optionalDelivery.isPresent()) {
            Delivery delivery = optionalDelivery.get();
            this.deliveryRepository.deleteById(id);

            // make box available if it's the only delivery assigned to this box.
            List<Delivery> deliveries = this.getDeliveryByBox(delivery.getTargetBox().getId());
            int dCount = 0;
            for (Delivery cur: deliveries) {
                if (cur.getStatuses().size() == 1) {
                    // only count in current active and ORDERED deliveries
                    dCount += 1;
                }
            }
            if (dCount == 0) {
                Box box = delivery.getTargetBox();
                box.setState(Box.BoxStateEnum.AVAILABLE);
                this.boxService.updateBox(box);
            }
        }
    }
}
