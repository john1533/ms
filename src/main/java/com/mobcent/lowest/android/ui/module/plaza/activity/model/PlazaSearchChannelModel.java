package com.mobcent.lowest.android.ui.module.plaza.activity.model;

public class PlazaSearchChannelModel {
    int baikeType;
    boolean isShow;
    String typeName;

    public boolean isShow() {
        return this.isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public int getBaikeType() {
        return this.baikeType;
    }

    public void setBaikeType(int baikeType) {
        this.baikeType = baikeType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
