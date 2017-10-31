package com.mobcent.discuz.android.util;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.impl.helper.LocationServiceImplHelper;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class DZPoiUtil {
    private static DZPoiUtil poiUtil;
    private final long TIMEOUT;
    private BDLocationListener bdLocationListener;
    private Vector<PoiDelegate> delegates;
    private long lastRequestTime;
    private LocationClient mLocationClient;
    private List<String> poi;
    private int times;

    public interface PoiDelegate {
        void onReceivePoi(List<String> list);
    }

    public static synchronized DZPoiUtil getInstance(Context context) {
        DZPoiUtil dZPoiUtil;
        synchronized (DZPoiUtil.class) {
            if (poiUtil == null) {
                poiUtil = new DZPoiUtil(context.getApplicationContext());
            }
            dZPoiUtil = poiUtil;
        }
        return dZPoiUtil;
    }

    private DZPoiUtil(Context context) {
        this.poi = null;
        this.delegates = null;
        this.mLocationClient = null;
        this.times = 0;
        this.lastRequestTime = 0;
        this.TIMEOUT = 60;
        this.bdLocationListener = new BDLocationListener() {
            public void onReceiveLocation(BDLocation bdLocation) {
                MCLogUtil.i("PoiUtil", "onReceiveLocation");
                if (bdLocation != null && DZPoiUtil.this.mLocationClient != null && DZPoiUtil.this.mLocationClient.isStarted()) {
                    DZPoiUtil.this.mLocationClient.requestPoi();
                }
            }

            public void onReceivePoi(BDLocation poiLocation) {
                MCLogUtil.i("PoiUtil", "onReceivePoi");
                if (poiLocation != null && poiLocation.hasPoi()) {
                    BaseResultModel<List<String>> baseResultModel = LocationServiceImplHelper.parsePoi(poiLocation.getPoi());
                    if (baseResultModel != null) {
                        DZPoiUtil.this.poi = (List) baseResultModel.getData();
                        DZPoiUtil.this.notifyDelegate(DZPoiUtil.this.poi);
                        DZPoiUtil.this.stopLocation();
                        return;
                    }
                }
                DZPoiUtil dZPoiUtil = DZPoiUtil.this;
                dZPoiUtil.times = dZPoiUtil.times + 1;
                if (DZPoiUtil.this.times > 3) {
                    DZPoiUtil.this.stopLocation();
                    DZPoiUtil.this.notifyDelegate(DZPoiUtil.this.poi);
                }
            }
        };
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

    private void notifyDelegate(List<String> poi) {
        if (this.delegates != null) {
            Iterator it = this.delegates.iterator();
            while (it.hasNext()) {
                PoiDelegate delegate = (PoiDelegate) it.next();
                if (delegate != null) {
                    delegate.onReceivePoi(poi);
                }
            }
            this.delegates.clear();
        }
    }

    public synchronized void requestPoi(PoiDelegate delegate) {
        MCLogUtil.i("PoiUtil", "requestPoi");
        if (this.poi == null || isLocationInfoTimeOut()) {
            if (!this.delegates.contains(delegate)) {
                this.delegates.add(delegate);
            }
            if (this.mLocationClient.isStarted()) {
                this.mLocationClient.stop();
            }
            this.times = 0;
            this.lastRequestTime = System.currentTimeMillis();
            this.mLocationClient.start();
            this.mLocationClient.requestPoi();
        } else {
            delegate.onReceivePoi(this.poi);
        }
    }

    private boolean isLocationInfoTimeOut() {
        if (System.currentTimeMillis() - this.lastRequestTime > 60) {
            return true;
        }
        return false;
    }

    public synchronized void stopLocation() {
        if (this.mLocationClient != null && this.mLocationClient.isStarted()) {
            this.mLocationClient.stop();
        }
    }
}
