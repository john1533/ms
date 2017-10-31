package com.mobcent.lowest.module.place.model;

import java.io.Serializable;

public class PlaceHotSubModel implements Serializable {
    private static final long serialVersionUID = 4762327904330285040L;
    private String color;
    private String name;
    private String type;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "PlaceHotSubModel [name=" + this.name + ", color=" + this.color + ", type=" + this.type + "]";
    }
}
