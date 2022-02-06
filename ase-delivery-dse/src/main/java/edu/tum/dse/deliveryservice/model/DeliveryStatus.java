package edu.tum.dse.deliveryservice.model;

import java.time.LocalDate;

public class DeliveryStatus {

    LocalDate date;
    DeliveryStatusEnum status;

    public DeliveryStatus() {

    }

    public DeliveryStatus(DeliveryStatusEnum status) {
        this.date = LocalDate.now();
        this.status = status;
    }

    public enum DeliveryStatusEnum {
        ORDERED(0), DELIVERING(1), DELIVERED(2), COMPLETE(3);

        private int value;

        DeliveryStatusEnum(int value) {
            this.value = value;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DeliveryStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatusEnum status) {
        this.status = status;
    }
}
