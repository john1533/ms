package com.mobcent.lowest.module.weather.model;

import java.io.Serializable;

public class CityModel implements Serializable {
    private static final long serialVersionUID = -2666820818328103729L;
    private String cityCode;
    private String cityName;
    private double latitude;
    private double longitude;

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCityId() {
        return this.cityCode;
    }

    public void setCityId(String cityId) {
        this.cityCode = cityId;
    }

    public String toString() {
        return "CityModel [cityId=" + this.cityCode + ", cityName=" + this.cityName + ", longitude=" + this.longitude + ", latitude=" + this.latitude + "]";
    }
}
