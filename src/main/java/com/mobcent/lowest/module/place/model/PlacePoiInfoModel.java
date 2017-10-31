package com.mobcent.lowest.module.place.model;

public class PlacePoiInfoModel extends BasePlaceModel {
    private static final long serialVersionUID = 7769859585723166336L;
    public String address;
    public PlacePoiDetailModel detailInfoModel;
    public PlacePoiLocationModel locationModel;
    public String name;
    public String telephone;
    public String uid;

    public String toString() {
        return "PlacePoiInfoModel [locationModel=" + this.locationModel + ", detailInfoModel=" + this.detailInfoModel + ", address=" + this.address + ", telephone=" + this.telephone + ", name=" + this.name + ", uid=" + this.uid + "]";
    }
}
