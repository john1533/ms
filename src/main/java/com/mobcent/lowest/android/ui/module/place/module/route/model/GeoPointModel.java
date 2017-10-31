package com.mobcent.lowest.android.ui.module.place.module.route.model;

import java.io.Serializable;

public class GeoPointModel implements Serializable {
    private static final long serialVersionUID = 3508117934195928332L;
    private int latitudeE6;
    private int longitudeE6;

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

    public String toString() {
        return "GeoPointModel [latitudeE6=" + this.latitudeE6 + ", longitudeE6=" + this.longitudeE6 + "]";
    }
}
