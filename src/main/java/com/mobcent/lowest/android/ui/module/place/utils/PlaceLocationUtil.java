package com.mobcent.lowest.android.ui.module.place.utils;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.base.utils.MCLibIOUtil;

public class PlaceLocationUtil {
    private static RouteLocationCallBack callback;
    private static LocationClient mLocClient;
    private static PlaceLocationUtil placeLocationUtil;

    public interface RouteLocationCallBack {
        void onGetMyPosition(BDLocation bDLocation);

        void onGetPoi(BDLocation bDLocation);
    }

    public class MyLocationListenner implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
            if (location != null) {
                PlaceLocationUtil.callback.onGetMyPosition(location);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation != null) {
                PlaceLocationUtil.callback.onGetPoi(poiLocation);
            }
        }
    }

    private PlaceLocationUtil() {
    }

    public static PlaceLocationUtil getInstance() {
        if (placeLocationUtil == null) {
            placeLocationUtil = new PlaceLocationUtil();
        }
        return placeLocationUtil;
    }

    public void requestLocation(Context context, RouteLocationCallBack callback) {
        callback = callback;
        if (mLocClient == null) {
            mLocClient = new LocationClient(context);
            MyLocationListenner myListener = new MyLocationListenner();
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            option.setCoorType("bd09ll");
            option.setScanSpan(5000);
            option.setProdName(MCLibIOUtil.MOBCENT);
            mLocClient.setLocOption(option);
            mLocClient.registerLocationListener(myListener);
        }
        mLocClient.start();
        mLocClient.requestLocation();
    }

    public void stopLocation() {
        mLocClient.stop();
    }
}
