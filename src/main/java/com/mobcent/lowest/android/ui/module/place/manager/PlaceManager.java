package com.mobcent.lowest.android.ui.module.place.manager;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteModel;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.BasePlaceApiRequester;
import com.mobcent.lowest.module.place.helper.PlaceFiledHelper;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;

public class PlaceManager {
    public static String TAG = "PlaceManager";
    private static PlaceManager manager = null;
    private BMapManager bManager = null;
    private RouteModel routeModel;

    static class MyGeneralListener implements MKGeneralListener {
        private Context context;
        public boolean m_bKeyRight = true;

        public MyGeneralListener(Context context) {
            this.context = context;
        }

        public void onGetNetworkState(int iError) {
            if (iError == 2) {
                MCLogUtil.e(PlaceManager.TAG, MCResource.getInstance(this.context).getString("mc_place_bad_net"));
            } else if (iError == 3) {
                MCLogUtil.e(PlaceManager.TAG, MCResource.getInstance(this.context).getString("mc_place_bad_search_keywords"));
            }
        }

        public void onGetPermissionState(int iError) {
            if (iError != 0) {
                MCLogUtil.e(PlaceManager.TAG, MCResource.getInstance(this.context).getString("mc_place_key_disable"));
                this.m_bKeyRight = false;
                return;
            }
            this.m_bKeyRight = true;
            MCLogUtil.e(PlaceManager.TAG, MCResource.getInstance(this.context).getString("mc_place_key_able"));
        }
    }

    public static PlaceManager getInstance() {
        if (manager == null) {
            manager = new PlaceManager();
        }
        return manager;
    }

    public BMapManager getBMapManager(Context context) {
        if (this.bManager == null) {
            this.bManager = new BMapManager(context.getApplicationContext());
            this.bManager.init(LowestManager.getInstance().getConfig().getBdMapAk(), new MyGeneralListener(context));
        }
        return this.bManager;
    }

    public void init(final Context context) {
        if (!MCStringUtil.isEmpty(LowestManager.getInstance().getConfig().getBdMapAk())) {
            PlaceFiledHelper.getInstance().queryFiledType(context.getApplicationContext(), null, null);
            PlaceLocationHelper.getInastance().getCurrentLocation(context.getApplicationContext(), false, new LocationDelegate() {
                public void onReceiveLocation(BDLocation location) {
                    if (location != null) {
                        PlaceLocationHelper.getInastance().queryAreaByAreaCode(context.getApplicationContext(), location.getCityCode(), null);
                    }
                }
            });
        }
    }

    public void setAk(String ak) {
        BasePlaceApiRequester.AK = ak;
    }

    public RouteModel getRouteModel() {
        return this.routeModel;
    }

    public void setRouteModel(RouteModel routeModel) {
        this.routeModel = routeModel;
    }

    public void clearRouteModel() {
        if (this.routeModel != null) {
            this.routeModel = null;
        }
    }
}
