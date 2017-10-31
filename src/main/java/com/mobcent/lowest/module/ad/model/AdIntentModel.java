package com.mobcent.lowest.module.ad.model;

public class AdIntentModel extends AdBaseModel {
    private static final long serialVersionUID = -8451573007049048834L;
    private long aid;
    private int po;
    private String url;

    public long getAid() {
        return this.aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public int getPo() {
        return this.po;
    }

    public void setPo(int po) {
        this.po = po;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
