package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class ConfigComponentHeaderModel implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean isShowMore;
    private boolean isShowTitle;
    private ConfigComponentModel moreComponent;
    private int position;
    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isShowMore() {
        return this.isShowMore;
    }

    public void setShowMore(boolean isShowMore) {
        this.isShowMore = isShowMore;
    }

    public boolean isShowTitle() {
        return this.isShowTitle;
    }

    public void setShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public ConfigComponentModel getMoreComponent() {
        return this.moreComponent;
    }

    public void setMoreComponent(ConfigComponentModel moreComponent) {
        this.moreComponent = moreComponent;
    }
}
