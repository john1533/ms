package com.mobcent.lowest.module.ad.model;

import java.util.HashSet;

public class AdContainerModel extends AdBaseModel {
    private static final long serialVersionUID = 3591526408525297395L;
    private HashSet<AdModel> adSet = new HashSet();
    private int dtType;
    private int imgWidth;
    private int position;
    private int searchStyle;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashSet<AdModel> getAdSet() {
        return this.adSet;
    }

    public void setAdSet(HashSet<AdModel> adSet) {
        this.adSet = adSet;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDtType() {
        return this.dtType;
    }

    public void setDtType(int dtType) {
        this.dtType = dtType;
    }

    public int getImgWidth() {
        return this.imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getSearchStyle() {
        return this.searchStyle;
    }

    public void setSearchStyle(int searchStyle) {
        this.searchStyle = searchStyle;
    }
}
