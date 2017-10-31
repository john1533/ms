package com.mobcent.discuz.android.model;

public class OtherPanelModel extends BaseModel {
    private static final long serialVersionUID = -1940255071136065456L;
    private String action;
    private String beforeAction;
    private boolean isHasRecommendAdd;
    private int recommendAdd;
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

    public int getRecommendAdd() {
        return this.recommendAdd;
    }

    public void setRecommendAdd(int recommendAdd) {
        this.recommendAdd = recommendAdd;
    }

    public boolean isHasRecommendAdd() {
        return this.isHasRecommendAdd;
    }

    public void setHasRecommendAdd(boolean isHasRecommendAdd) {
        this.isHasRecommendAdd = isHasRecommendAdd;
    }

    public String getBeforeAction() {
        return this.beforeAction;
    }

    public void setBeforeAction(String beforeAction) {
        this.beforeAction = beforeAction;
    }
}
