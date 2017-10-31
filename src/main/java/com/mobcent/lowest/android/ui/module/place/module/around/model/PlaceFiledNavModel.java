package com.mobcent.lowest.android.ui.module.place.module.around.model;

import java.util.ArrayList;
import java.util.List;

public class PlaceFiledNavModel {
    private String areaCode;
    private boolean isSelected;
    private String key;
    private String name;
    private int rule;
    private List<PlaceFiledSubModel> subList = new ArrayList();
    private int type;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public List<PlaceFiledSubModel> getSubList() {
        return this.subList;
    }

    public void setSubList(List<PlaceFiledSubModel> subList) {
        this.subList = subList;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
