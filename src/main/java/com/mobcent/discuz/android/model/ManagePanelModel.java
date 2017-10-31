package com.mobcent.discuz.android.model;

public class ManagePanelModel extends BaseModel {
    private static final long serialVersionUID = -8168687402741759109L;
    private String action;
    private String title;
    private String type;

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
