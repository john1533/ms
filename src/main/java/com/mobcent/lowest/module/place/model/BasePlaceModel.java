package com.mobcent.lowest.module.place.model;

import java.io.Serializable;

public class BasePlaceModel implements Serializable {
    private static final long serialVersionUID = 8991247353172362974L;
    private String message;
    private int status = -1;
    private int total;

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
