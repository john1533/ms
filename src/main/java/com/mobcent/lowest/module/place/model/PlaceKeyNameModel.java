package com.mobcent.lowest.module.place.model;

import java.io.Serializable;
import java.util.List;

public class PlaceKeyNameModel implements Serializable {
    private static final long serialVersionUID = 1248543471925993926L;
    private String key;
    private String name;
    private int rule = 1;
    private List<String> sub;
    private int type;

    public List<String> getSub() {
        return this.sub;
    }

    public void setSub(List<String> sub) {
        this.sub = sub;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRule() {
        return this.rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }
}
