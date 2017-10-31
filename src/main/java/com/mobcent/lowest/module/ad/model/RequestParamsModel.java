package com.mobcent.lowest.module.ad.model;

public class RequestParamsModel extends AdBaseModel {
    private static final long serialVersionUID = -6812674013976006162L;
    private String ak;
    private String ch;
    private String im;
    private String mc;
    private String pn;
    private String pt;
    private String ss;
    private String ua;
    private long uid;
    private String userAgent;
    private String zo;

    public String getIm() {
        return this.im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getPt() {
        return this.pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getSs() {
        return this.ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getUa() {
        return this.ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getZo() {
        return this.zo;
    }

    public void setZo(String zo) {
        this.zo = zo;
    }

    public String getAk() {
        return this.ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getPn() {
        return this.pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getMc() {
        return this.mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getCh() {
        return this.ch;
    }

    public void setCh(String ch) {
        this.ch = ch;
    }

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
