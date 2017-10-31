package com.mobcent.lowest.module.place.model;

public class PlacePoiLocationModel extends BasePlaceModel {
    private static final long serialVersionUID = -8793862679126099731L;
    private String address;
    private String areaCode;
    public String city;
    public double lat;
    public double lng;

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getLatE6() {
        return (int) (this.lat * 1000000.0d);
    }

    public int getLngE6() {
        return (int) (this.lng * 1000000.0d);
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String toString() {
        return "PlacePoiLocationModel [lat=" + this.lat + ", lng=" + this.lng + ", city=" + this.city + ", areaCode=" + this.areaCode + "]";
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
