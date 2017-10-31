package com.mobcent.lowest.module.place.model;

public class PlacePoiDetailModel extends BasePlaceModel {
    private static final long serialVersionUID = -7757038812229271520L;
    public String checkinNum;
    public String commentNum;
    public String detailUrl;
    public int discountNum;
    public int distance;
    public String environmentRating;
    public String facilityRating;
    public String favoriteNum;
    public int grouponNum;
    public String hygieneRating;
    public String imageNum;
    public String overallRating;
    public String price;
    public String serviceRating;
    public String shopHours;
    public String tag;
    public String tasteRating;
    public String technologyRating;
    public String type;

    public String toString() {
        return "PlacePoiDetailModel [distance=" + this.distance + ", type=" + this.type + ", tag=" + this.tag + ", detailUrl=" + this.detailUrl + ", price=" + this.price + ", shopHours=" + this.shopHours + ", overallRating=" + this.overallRating + ", tasteRating=" + this.tasteRating + ", serviceRating=" + this.serviceRating + ", environmentRating=" + this.environmentRating + ", facilityRating=" + this.facilityRating + ", hygieneRating=" + this.hygieneRating + ", technologyRating=" + this.technologyRating + ", imageNum=" + this.imageNum + ", grouponNum=" + this.grouponNum + ", discountNum=" + this.discountNum + ", commentNum=" + this.commentNum + ", favoriteNum=" + this.favoriteNum + ", checkinNum=" + this.checkinNum + "]";
    }
}
