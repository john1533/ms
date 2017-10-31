package com.mobcent.lowest.android.ui.module.place.module.route.model;

import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;

public class RouteModel {
    private MKWalkingRouteResult WalkingRouteModel;
    private MKDrivingRouteResult drivingRouteModel;
    private MKTransitRouteResult transitRouteModel;

    public MKTransitRouteResult getTransitRouteModel() {
        return this.transitRouteModel;
    }

    public void setTransitRouteModel(MKTransitRouteResult transitRouteModel) {
        this.transitRouteModel = transitRouteModel;
    }

    public MKDrivingRouteResult getDrivingRouteModel() {
        return this.drivingRouteModel;
    }

    public void setDrivingRouteModel(MKDrivingRouteResult drivingRouteModel) {
        this.drivingRouteModel = drivingRouteModel;
    }

    public MKWalkingRouteResult getWalkingRouteModel() {
        return this.WalkingRouteModel;
    }

    public void setWalkingRouteModel(MKWalkingRouteResult walkingRouteModel) {
        this.WalkingRouteModel = walkingRouteModel;
    }
}
