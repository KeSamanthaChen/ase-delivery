package edu.tum.dse.deliveryservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Delivery {

    @Id
    private String trackingCode;
    @DBRef
    private Account customer;
    @DBRef
    private Account deliverer;
    @DBRef
    private Box targetBox;
    private List<DeliveryStatus> statuses;

    public Delivery(Account customer, Account deliverer, Box targetBox) {

        this.customer = customer;
        this.deliverer = deliverer;
        this.targetBox = targetBox;
        DeliveryStatus status = new DeliveryStatus(DeliveryStatus.DeliveryStatusEnum.ORDERED);
        this.statuses = new ArrayList<>();
        this.statuses.add(status);
    }

    public Delivery() {
        this.statuses = new ArrayList<>();
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Account getCustomer() {
        return customer;
    }

    public void setCustomer(Account customer) {
        this.customer = customer;
    }

    public Account getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(Account deliverer) {
        this.deliverer = deliverer;
    }

    public Box getTargetBox() {
        return targetBox;
    }

    public void setTargetBox(Box targetBox) {
        this.targetBox = targetBox;
    }

    public List<DeliveryStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<DeliveryStatus> statuses) {
        this.statuses = statuses;
    }
}
