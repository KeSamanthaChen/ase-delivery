package edu.tum.dse.deliveryservice.model;

public class BoxAuthRequest {
    private String box_id;
    private String rfid;

    public BoxAuthRequest() {}

    public String getBox_id() {
        return this.box_id;
    }

    public String getRfid() {
        return this.rfid;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
}