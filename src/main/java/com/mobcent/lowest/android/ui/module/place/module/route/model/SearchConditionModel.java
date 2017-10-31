package com.mobcent.lowest.android.ui.module.place.module.route.model;

import java.io.Serializable;

public class SearchConditionModel implements Serializable {
    private static final long serialVersionUID = 6449314604552091621L;
    private String cityName;
    private String hint;
    private int latitudeE6;
    private int longitudeE6;
    private String pointLocation;
    private String pointName;

    public int getLatitudeE6() {
        return this.latitudeE6;
    }

    public void setLatitudeE6(int latitudeE6) {
        this.latitudeE6 = latitudeE6;
    }

    public int getLongitudeE6() {
        return this.longitudeE6;
    }

    public void setLongitudeE6(int longitudeE6) {
        this.longitudeE6 = longitudeE6;
    }

    public String getPointName() {
        return this.pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getPointLocation() {
        return this.pointLocation;
    }

    public void setPointLocation(String pointLocation) {
        this.pointLocation = pointLocation;
    }

    public void clearAll() {
        this.latitudeE6 = 0;
        this.longitudeE6 = 0;
        this.pointName = null;
        this.cityName = null;
        this.hint = null;
        this.pointLocation = null;
    }

    public String toString() {
        return "SearchConditionModel [pointName=" + this.pointName + ", cityName=" + this.cityName + ", hint=" + this.hint + ", pointLocation=" + this.pointLocation + ", latitudeE6=" + this.latitudeE6 + ", longitudeE6=" + this.longitudeE6 + "]";
    }
}
