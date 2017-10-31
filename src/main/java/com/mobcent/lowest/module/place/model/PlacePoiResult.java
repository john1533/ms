package com.mobcent.lowest.module.place.model;

import java.util.List;

public class PlacePoiResult extends BasePlaceModel {
    private static final long serialVersionUID = 8423253321099481538L;
    private List<PlacePoiInfoModel> poiInfoList;

    public List<PlacePoiInfoModel> getPoiInfoList() {
        return this.poiInfoList;
    }

    public void setPoiInfoList(List<PlacePoiInfoModel> poiInfoList) {
        this.poiInfoList = poiInfoList;
    }

    public String toString() {
        return "PlacePoiResult [poiInfoList=" + this.poiInfoList + "]";
    }
}
