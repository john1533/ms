package com.mobcent.discuz.android.model;

public class ClassifyTopModel extends BaseModel {
    private static final long serialVersionUID = -8653788999241385038L;
    private String action;
    private int actionId;
    private String title;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getActionId() {
        return this.actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
}
