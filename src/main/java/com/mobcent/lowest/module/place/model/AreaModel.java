package com.mobcent.lowest.module.place.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AreaModel implements Serializable {
    private static final long serialVersionUID = -3980186060394500005L;
    private int areaCode;
    private String areaName;
    private int areaType;
    private int error = -1;
    private String geo;
    private List<AreaModel> subAreaList = new ArrayList();

    public int getError() {
        return this.error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return this.areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAreaType() {
        return this.areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public String getGeo() {
        return this.geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public List<AreaModel> getSubAreaList() {
        return this.subAreaList;
    }

    public void setSubAreaList(List<AreaModel> subAreaList) {
        this.subAreaList = subAreaList;
    }

    public String toString() {
        return "AreaModel [error=" + this.error + ", areaCode=" + this.areaCode + ", areaName=" + this.areaName + ", areaType=" + this.areaType + ", geo=" + this.geo + ", subAreaList=" + this.subAreaList + "]";
    }
}
