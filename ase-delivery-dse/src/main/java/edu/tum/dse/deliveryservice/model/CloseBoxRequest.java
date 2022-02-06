package edu.tum.dse.deliveryservice.model;

public class CloseBoxRequest {
    private String box_id;
    private String box_state;

    public CloseBoxRequest() {
    }

    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public String getBox_state() {
        return box_state;
    }

    public void setBox_state(String box_state) {
        this.box_state = box_state;
    }
}
