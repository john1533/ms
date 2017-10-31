package com.mobcent.lowest.module.ad.model;

public class AdDownDbModel extends AdBaseModel {
    private static final long serialVersionUID = 6622772758414690086L;
    private int activeDo;
    private long aid;
    private String appName;
    private String currentPn;
    private String date;
    private int downloadDo;
    private int id;
    private String pn;
    private int po;
    private int status;
    private String url;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAid() {
        return this.aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPn() {
        return this.pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getCurrentPn() {
        return this.currentPn;
    }

    public void setCurrentPn(String currentPn) {
        this.currentPn = currentPn;
    }

    public int getPo() {
        return this.po;
    }

    public void setPo(int po) {
        this.po = po;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getDownloadDo() {
        return this.downloadDo;
    }

    public void setDownloadDo(int downloadDo) {
        this.downloadDo = downloadDo;
    }

    public int getActiveDo() {
        return this.activeDo;
    }

    public void setActiveDo(int activeDo) {
        this.activeDo = activeDo;
    }

    public String toString() {
        return "AdDownDbModel [id=" + this.id + ", aid=" + this.aid + ", url=" + this.url + ", pn=" + this.pn + ", currentPn=" + this.currentPn + ", po=" + this.po + ", date=" + this.date + ", status=" + this.status + ", appName=" + this.appName + ", downloadDo=" + this.downloadDo + ", activeDo=" + this.activeDo + "]";
    }
}
