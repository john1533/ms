package com.mobcent.lowest.module.plaza.model;

import java.io.Serializable;

public class PlazaAppModel implements Serializable {
    private static final long serialVersionUID = -2326276971887911801L;
    private String activityName;
    private Object data;
    private int modelAction = -1;
    private String modelDrawable;
    private long modelId;
    private String modelName;
    private int nativeCat;

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getModelDrawable() {
        return this.modelDrawable;
    }

    public void setModelDrawable(String modelDrawable) {
        this.modelDrawable = modelDrawable;
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public long getModelId() {
        return this.modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public int getModelAction() {
        return this.modelAction;
    }

    public void setModelAction(int modelAction) {
        this.modelAction = modelAction;
    }

    public int getNativeCat() {
        return this.nativeCat;
    }

    public void setNativeCat(int nativeCat) {
        this.nativeCat = nativeCat;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
