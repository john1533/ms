package com.mobcent.lowest.base.model;

import java.io.Serializable;

public class LocationModel implements Serializable {
    private static final long serialVersionUID = -2824079131788643543L;
    private String addrStr;
    private String city;
    private String district;
    private double latitude;
    private double longitude;
    private String province;
    private String str;
    private String street;
    private long userId;

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddrStr() {
        return this.addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getStr() {
        return this.str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
