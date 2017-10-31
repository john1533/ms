package com.mobcent.lowest.android.ui.module.place.module.route.model;

import java.io.Serializable;

public class RouteSearchMessageModel implements Serializable {
    private static final long serialVersionUID = -7057779560910220056L;
    private String distanceCity;
    private String distanceLocation;
    private String distanceName;
    private GeoPointModel endPointModel;
    private int searchNum;
    private int searchPolicy;
    private int searchType;
    private String startCity;
    private GeoPointModel startGeoPointModel;
    private String startLocation;
    private String startName;

    public int getSearchType() {
        return this.searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public int getSearchNum() {
        return this.searchNum;
    }

    public void setSearchNum(int searchNum) {
        this.searchNum = searchNum;
    }

    public int getSearchPolicy() {
        return this.searchPolicy;
    }

    public void setSearchPolicy(int searchPolicy) {
        this.searchPolicy = searchPolicy;
    }

    public String getStartCity() {
        return this.startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartName() {
        return this.startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getDistanceCity() {
        return this.distanceCity;
    }

    public void setDistanceCity(String distanceCity) {
        this.distanceCity = distanceCity;
    }

    public String getDistanceName() {
        return this.distanceName;
    }

    public void setDistanceName(String distanceName) {
        this.distanceName = distanceName;
    }

    public GeoPointModel getStartGeoPointModel() {
        return this.startGeoPointModel;
    }

    public void setStartGeoPointModel(GeoPointModel startGeoPointModel) {
        this.startGeoPointModel = startGeoPointModel;
    }

    public GeoPointModel getEndPointModel() {
        return this.endPointModel;
    }

    public void setEndPointModel(GeoPointModel endPointModel) {
        this.endPointModel = endPointModel;
    }

    public String getStartLocation() {
        return this.startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDistanceLocation() {
        return this.distanceLocation;
    }

    public void setDistanceLocation(String distanceLocation) {
        this.distanceLocation = distanceLocation;
    }

    public String toString() {
        return "RouteSearchMessageModel [searchType=" + this.searchType + ", searchPolicy=" + this.searchPolicy + ", startCity=" + this.startCity + ", startName=" + this.startName + ", distanceCity=" + this.distanceCity + ", distanceName=" + this.distanceName + ", startLocation=" + this.startLocation + ", distanceLocation=" + this.distanceLocation + ", searchNum=" + this.searchNum + ", startGeoPointModel=" + this.startGeoPointModel + ", endPointModel=" + this.endPointModel + "]";
    }
}
