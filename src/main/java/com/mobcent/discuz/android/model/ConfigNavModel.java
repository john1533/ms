package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class ConfigNavModel implements Serializable {
    private static final long serialVersionUID = -789601330264604816L;
    private String icon;
    private long moduleId;
    private String title;

    public long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
