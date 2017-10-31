package com.mobcent.lowest.module.ad.model;

public class AdStateModel {
    private int adPosition;
    private int dtType;
    private boolean haveAd;

    public boolean isHaveAd() {
        return this.haveAd;
    }

    public void setHaveAd(boolean haveAd) {
        this.haveAd = haveAd;
    }

    public int getDtType() {
        return this.dtType;
    }

    public void setDtType(int dtType) {
        this.dtType = dtType;
    }

    public int getAdPosition() {
        return this.adPosition;
    }

    public void setAdPosition(int adPosition) {
        this.adPosition = adPosition;
    }
}
