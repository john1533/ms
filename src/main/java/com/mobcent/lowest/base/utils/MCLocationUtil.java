package com.mobcent.lowest.base.utils;

import android.content.Context;


public class MCLocationUtil {
    private static MCLocationUtil locationUtil;
    private final long TIMEOUT = 300000;

//    private BDLocation cacheLocation = null;
    private Context context;
//    private Vector<LocationDelegate> delegates = null;
    private long lastRequestTime = 0;
//    private LocationClient mLocationClient = null;
    private int times = 0;



//    public LocationClient getLocationClient() {
//        return this.mLocationClient;
//    }

//    public void setLocationClient(LocationClient mLocationClient) {
//        this.mLocationClient = mLocationClient;
//    }

    public static MCLocationUtil getInstance(Context context) {
        if (locationUtil == null) {
            locationUtil = new MCLocationUtil(context.getApplicationContext());
        }
        return locationUtil;
    }

    private MCLocationUtil(Context context) {
//        this.context = context;
//        this.delegates = new Vector();
//        this.mLocationClient = new LocationClient(context);
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(false);
//        option.setCoorType("bd09ll");
//        option.setPriority(1);
//        option.setProdName(MCLibIOUtil.MOBCENT);
//        option.setPoiExtraInfo(true);
//        option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN);
//        option.setTimeOut(5000);
//        option.setAddrType("all");
//        this.mLocationClient.setLocOption(option);
//        this.mLocationClient.registerLocationListener(this.bdLocationListener);
    }



//    public synchronized void requestLocation(LocationDelegate delegate) {
//        MCLogUtil.i("LocationUtil", "requestLocation");
//        if (this.cacheLocation == null || isLocationInfoTimeOut()) {
//            if (!this.delegates.contains(delegate)) {
//                this.delegates.add(delegate);
//            }
//            if (this.mLocationClient.isStarted()) {
//                this.mLocationClient.stop();
//            }
//            this.times = 0;
//            this.lastRequestTime = System.currentTimeMillis();
//            this.mLocationClient.start();
//            this.mLocationClient.requestLocation();
//        } else {
//            delegate.onReceiveLocation(this.cacheLocation);
//        }
//    }

    private boolean isLocationInfoTimeOut() {
        if (System.currentTimeMillis() - this.lastRequestTime > 300000) {
            return true;
        }
        return false;
    }

    public synchronized void stopLocation() {
//        if (this.mLocationClient != null && this.mLocationClient.isStarted()) {
//            this.mLocationClient.stop();
//        }
    }

//    public BDLocation getCacheLocation() {
//        return this.cacheLocation;
//    }
}
