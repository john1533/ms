package com.mobcent.lowest.android.ui.module.place.module.around.model;

public class PlaceFiledSubModel {
    private boolean isAll;
    private boolean isSelected;
    private String key;
    private String name;
    private int rule;
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

    public int getRule() {
        return this.rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isAll() {
        return this.isAll;
    }

    public void setAll(boolean isAll) {
        this.isAll = isAll;
    }
}
