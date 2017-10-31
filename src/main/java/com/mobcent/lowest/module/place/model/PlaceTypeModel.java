package com.mobcent.lowest.module.place.model;

import java.io.Serializable;
import java.util.List;

public class PlaceTypeModel implements Serializable {
    private static final long serialVersionUID = 6627564672510175762L;
    List<PlaceKeyNameModel> distanceList;
    List<PlaceKeyNameModel> keywordsList;
    private String name;
    private String priceTag;
    List<PlaceKeyNameModel> sortList;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceTag() {
        return this.priceTag;
    }

    public void setPriceTag(String priceTag) {
        this.priceTag = priceTag;
    }

    public List<PlaceKeyNameModel> getSortList() {
        return this.sortList;
    }

    public void setSortList(List<PlaceKeyNameModel> sortList) {
        this.sortList = sortList;
    }

    public List<PlaceKeyNameModel> getKeywordsList() {
        return this.keywordsList;
    }

    public void setKeywordsList(List<PlaceKeyNameModel> keywordsList) {
        this.keywordsList = keywordsList;
    }

    public List<PlaceKeyNameModel> getDistanceList() {
        return this.distanceList;
    }

    public void setDistanceList(List<PlaceKeyNameModel> distanceList) {
        this.distanceList = distanceList;
    }
}
