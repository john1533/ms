package com.mobcent.lowest.module.place.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceHotNavModel implements Serializable {
    private static final long serialVersionUID = -4482144865304362078L;
    private String color;
    private String drawableName;
    private String name;
    private List<PlaceHotSubModel> subList = new ArrayList();
    private String type;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PlaceHotSubModel> getSubList() {
        return this.subList;
    }

    public void setSubList(List<PlaceHotSubModel> subList) {
        this.subList = subList;
    }

    public String toString() {
        return "PlaceHotNavModel [name=" + this.name + ", type=" + this.type + ", subList=" + this.subList + "]";
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDrawableName() {
        return this.drawableName;
    }

    public void setDrawableName(String drawableName) {
        this.drawableName = drawableName;
    }
}
