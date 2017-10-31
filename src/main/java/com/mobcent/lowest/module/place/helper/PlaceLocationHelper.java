package com.mobcent.lowest.module.place.helper;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.delegate.QueryAreaDelegate;
import com.mobcent.lowest.module.place.model.AreaModel;
import com.mobcent.lowest.module.place.task.QueryAreaTask;
import java.util.HashMap;
import java.util.Map;

public class PlaceLocationHelper {
    private static PlaceLocationHelper helper;
    public String TAG = "PlaceLocationHelper";
    private long TIME_OUT = 300000;
    private String areaCode = "";
    private Map<String, AreaModel> areaMap = new HashMap();
    private AreaModel areaModel = null;
    private BDLocation bdLocation;
    private long lastTime = 0;
    private MCLocationUtil locationUtil = null;
    private QueryAreaTask queryAreaTask = null;

    public static PlaceLocationHelper getInastance() {
        if (helper == null) {
            helper = new PlaceLocationHelper();
        }
        return helper;
    }

    public void getCurrentLocation(Context context, boolean openGps, final LocationDelegate locationDelegate) {
        if (this.lastTime == 0 || System.currentTimeMillis() - this.lastTime >= this.TIME_OUT || this.bdLocation == null) {
            this.locationUtil = MCLocationUtil.getInstance(context.getApplicationContext());
            this.locationUtil.requestLocation(new LocationDelegate() {
                public void onReceiveLocation(BDLocation locationModel) {
                    BDLocation locationTemp;
                    if (locationModel == null) {
                        locationTemp = PlaceLocationHelper.this.bdLocation;
                    } else {
                        locationTemp = locationModel;
                        PlaceLocationHelper.this.bdLocation = locationModel;
                        PlaceLocationHelper.this.lastTime = System.currentTimeMillis();
                    }
                    if (locationDelegate != null) {
                        locationDelegate.onReceiveLocation(locationTemp);
                    }
                }
            });
        } else if (locationDelegate != null) {
            locationDelegate.onReceiveLocation(this.bdLocation);
        }
    }

    public void queryAreaByAreaCode(Context context, final String areaCode, final QueryAreaDelegate delegate) {
        if (!(this.queryAreaTask == null || this.queryAreaTask.isCancelled())) {
            this.queryAreaTask.cancel(true);
        }
        if (this.areaModel == null || MCStringUtil.isEmpty(areaCode) || !areaCode.equals(this.areaCode) || delegate == null) {
            this.queryAreaTask = new QueryAreaTask(context, areaCode);
            this.queryAreaTask.setDelegate(new QueryAreaDelegate() {
                public void onResult(AreaModel areaModel) {
                    if (delegate != null) {
                        delegate.onResult(areaModel);
                        if (areaModel != null && areaModel.getError() == 0) {
                            PlaceLocationHelper.this.setAreaModel(areaModel);
                            PlaceLocationHelper.this.setAreaCode(areaCode);
                        }
                    }
                }
            });
            this.queryAreaTask.execute(new Void[0]);
            return;
        }
        delegate.onResult(this.areaModel);
    }

    public void querySubAreaByAreaCode(Context context, final String areaCode, final QueryAreaDelegate delegate) {
        if (this.areaMap.get(areaCode) == null || ((AreaModel) this.areaMap.get(areaCode)).getSubAreaList().isEmpty() || delegate == null) {
            if (!(this.queryAreaTask == null || this.queryAreaTask.isCancelled())) {
                this.queryAreaTask.cancel(true);
            }
            this.queryAreaTask = new QueryAreaTask(context, areaCode);
            this.queryAreaTask.setQueryArea(false);
            this.queryAreaTask.setDelegate(new QueryAreaDelegate() {
                public void onResult(AreaModel areaModel) {
                    if (delegate != null) {
                        delegate.onResult(areaModel);
                        if (areaModel != null && areaModel.getError() == 0) {
                            PlaceLocationHelper.this.areaMap.put(areaCode, areaModel);
                        }
                    }
                }
            });
            this.queryAreaTask.execute(new Void[0]);
            return;
        }
        delegate.onResult((AreaModel) this.areaMap.get(areaCode));
    }

    public AreaModel getAreaModel() {
        return this.areaModel;
    }

    public void setAreaModel(AreaModel areaModel) {
        this.areaModel = areaModel;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}
