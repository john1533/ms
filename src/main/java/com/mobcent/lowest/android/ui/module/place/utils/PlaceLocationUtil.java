package com.mobcent.lowest.android.ui.module.place.utils;


public class PlaceLocationUtil {
    private static PlaceLocationUtil placeLocationUtil;




    private PlaceLocationUtil() {
    }

    public static PlaceLocationUtil getInstance() {
        if (placeLocationUtil == null) {
            placeLocationUtil = new PlaceLocationUtil();
        }
        return placeLocationUtil;
    }


}
