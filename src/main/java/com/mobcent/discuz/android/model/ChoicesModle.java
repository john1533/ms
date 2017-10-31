package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class ChoicesModle implements Serializable {
    private static final long serialVersionUID = 7168954632245706336L;
    private String choicesName;
    private String choicesValue;

    public String getChoicesName() {
        return this.choicesName;
    }

    public void setChoicesName(String choicesName) {
        this.choicesName = choicesName;
    }

    public String getChoicesValue() {
        return this.choicesValue;
    }

    public void setChoicesValue(String choicesValue) {
        this.choicesValue = choicesValue;
    }

    public String toString() {
        return "ChoicesModle [choicesName=" + this.choicesName + ", choicesValue=" + this.choicesValue + "]";
    }
}
