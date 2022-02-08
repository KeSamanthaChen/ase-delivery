package edu.tum.dse.deliveryservice.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Box {

    @Id
    private String id;
    @Indexed(unique = true)
    @NonNull
    private String name;
    private String address;
    private BoxStateEnum state;

    private String customerName;
    private String delivererName;

    public Box(String name, String address) {
        this.name = name;
        this.address = address;
        this.state = BoxStateEnum.ASSIGNED;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    public Box(){};

    public enum BoxStateEnum {
        ASSIGNED(0), FILLED(1), AVAILABLE(2);
        private int value;

        BoxStateEnum(int value) {
            this.value = value;
        }
    }

    public BoxStateEnum getState() {
        return state;
    }

    public void setState(BoxStateEnum state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

