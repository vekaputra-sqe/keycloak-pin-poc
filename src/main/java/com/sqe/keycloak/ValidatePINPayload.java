package com.sqe.keycloak;

import javax.xml.bind.annotation.XmlRootElement; 

@XmlRootElement
public class ValidatePINPayload {
    private String pin;

    public ValidatePINPayload() {
    }

    public ValidatePINPayload(String pin) {
        this.pin = pin;
    }

    public void setPIN(String pin) {
        this.pin = pin;
    }

    public String getPIN() {
        return pin;
    }
}
