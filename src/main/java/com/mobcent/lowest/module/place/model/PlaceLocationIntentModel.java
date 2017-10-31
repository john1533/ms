package com.mobcent.lowest.module.place.model;

import java.io.Serializable;

public class PlaceLocationIntentModel implements Serializable {
    private static final long serialVersionUID = 4663436661450709668L;
    private boolean isSearch;
    private PlacePoiLocationModel locationModel;
    private PlaceQueryModel queryModel;

    public PlacePoiLocationModel getLocationModel() {
        return this.locationModel;
    }

    public void setLocationModel(PlacePoiLocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public PlaceQueryModel getQueryModel() {
        return this.queryModel;
    }

    public void setQueryModel(PlaceQueryModel queryModel) {
        this.queryModel = queryModel;
    }

    public boolean isSearch() {
        return this.isSearch;
    }

    public void setSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }
}
