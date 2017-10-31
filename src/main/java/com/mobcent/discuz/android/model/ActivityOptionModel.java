package com.mobcent.discuz.android.model;

import java.util.List;

public class ActivityOptionModel extends BaseModel {
    private static final long serialVersionUID = 6928254960762070229L;
    private List<ActivityOptionModel> elements;
    private String label;
    private String name;
    private int required;
    private String type;
    private String value;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getRequired() {
        return this.required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public List<ActivityOptionModel> getElements() {
        return this.elements;
    }

    public void setElements(List<ActivityOptionModel> elements) {
        this.elements = elements;
    }
}
