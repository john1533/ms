package com.mobcent.lowest.module.ad.model;

public class AdOtherModel extends AdBaseModel {
    private static final long serialVersionUID = 1846790527484842875L;
    private long delay;
    private int type;
    private String url;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
