package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.location.Location;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.base.delegate.MCLocationListener;
import java.util.Iterator;
import java.util.Vector;

public class MCLocationUtil {
    private static MCLocationUtil locationUtil;
    private final long TIMEOUT = 300000;
    private BDLocationListener bdLocationListener = new BDLocationListener() {
        public void onReceivePoi(BDLocation poiLocation) {
        }

        public void onReceiveLocation(BDLocation bdLocation) {
            MCLogUtil.i("LocationUtil", "onReceiveLocation");
            if (bdLocation == null || !(bdLocation.getLocType() == BDLocation.TypeNetWorkLocation || bdLocation.getLocType() == 61)) {
                MCLocationUtil mCLocationUtil = MCLocationUtil.this;
                mCLocationUtil.times = mCLocationUtil.times + 1;
                if (MCLocationUtil.this.times > 3) {
                    MCLocationUtil.this.stopLocation();
                    MCPhoneUtil.getLocation(MCLocationUtil.this.context, new MCLocationListener() {
                        public void onLocationChanged(Location location) {
                            if (location != null) {
                                new Thread() {
                                    public void run() {
                                        MCLocationUtil.this.notifyDelegate(MCLocationUtil.this.cacheLocation);
                                    }
                                }.start();
                            } else {
                                MCLocationUtil.this.notifyDelegate(null);
                            }
                        }
                    });
                    return;
                }
                return;
            }
            MCLocationUtil.this.cacheLocation = bdLocation;
            MCLocationUtil.this.notifyDelegate(bdLocation);
            MCLocationUtil.this.stopLocation();
        }
    };
    private BDLocation cacheLocation = null;
    private Context context;
    private Vector<LocationDelegate> delegates = null;
    private long lastRequestTime = 0;
    private LocationClient mLocationClient = null;
    private int times = 0;

    public interface LocationDelegate {
        void onReceiveLocation(BDLocation bDLocation);
    }

    public LocationClient getLocationClient() {
        return this.mLocationClient;
    }

    public void setLocationClient(LocationClient mLocationClient) {
        this.mLocationClient = mLocationClient;
    }

    public static MCLocationUtil getInstance(Context context) {
        if (locationUtil == null) {
            locationUtil = new MCLocationUtil(context.getApplicationContext());
        }
        return locationUtil;
    }

    private MCLocationUtil(Context context) {
        this.context = context;
        this.delegates = new Vector();
        this.mLocationClient = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);
        option.setCoorType("bd09ll");
        option.setPriority(1);
        option.setProdName(MCLibIOUtil.MOBCENT);
        option.setPoiExtraInfo(true);
        option.setScanSpan(LocationClientOption.MIN_SCAN_SPAN);
        option.setTimeOut(5000);
        option.setAddrType("all");
        this.mLocationClient.setLocOption(option);
        this.mLocationClient.registerLocationListener(this.bdLocationListener);
    }

    private void notifyDelegate(BDLocation location) {
        if (this.delegates != null) {
            Iterator it = this.delegates.iterator();
            while (it.hasNext()) {
                LocationDelegate delegate = (LocationDelegate) it.next();
                if (delegate != null) {
                    delegate.onReceiveLocation(location);
                }
            }
            this.delegates.clear();
        }
    }

    public synchronized void requestLocation(LocationDelegate delegate) {
        MCLogUtil.i("LocationUtil", "requestLocation");
        if (this.cacheLocation == null || isLocationInfoTimeOut()) {
            if (!this.delegates.contains(delegate)) {
                this.delegates.add(delegate);
            }
            if (this.mLocationClient.isStarted()) {
                this.mLocationClient.stop();
            }
            this.times = 0;
            this.lastRequestTime = System.currentTimeMillis();
            this.mLocationClient.start();
            this.mLocationClient.requestLocation();
        } else {
            delegate.onReceiveLocation(this.cacheLocation);
        }
    }

    private boolean isLocationInfoTimeOut() {
        if (System.currentTimeMillis() - this.lastRequestTime > 300000) {
            return true;
        }
        return false;
    }

    public synchronized void stopLocation() {
        if (this.mLocationClient != null && this.mLocationClient.isStarted()) {
            this.mLocationClient.stop();
        }
    }

    public BDLocation getCacheLocation() {
        return this.cacheLocation;
    }
}
