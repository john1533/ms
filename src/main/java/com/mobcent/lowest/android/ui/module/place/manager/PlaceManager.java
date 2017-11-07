package com.mobcent.lowest.android.ui.module.place.manager;

import android.content.Context;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.BasePlaceApiRequester;
import com.mobcent.lowest.module.place.helper.PlaceFiledHelper;

public class PlaceManager {
    public static String TAG = "PlaceManager";
    private static PlaceManager manager = null;
//    private BMapManager bManager = null;
//    private RouteModel routeModel;



    public static PlaceManager getInstance() {
        if (manager == null) {
            manager = new PlaceManager();
        }
        return manager;
    }

//    public BMapManager getBMapManager(Context context) {
//        if (this.bManager == null) {
//            this.bManager = new BMapManager(context.getApplicationContext());
//            this.bManager.init(LowestManager.getInstance().getConfig().getBdMapAk(), new MyGeneralListener(context));
//        }
//        return this.bManager;
//    }

    public void init(final Context context) {
        if (!MCStringUtil.isEmpty(LowestManager.getInstance().getConfig().getBdMapAk())) {
            PlaceFiledHelper.getInstance().queryFiledType(context.getApplicationContext(), null, null);
//            PlaceLocationHelper.getInastance().getCurrentLocation(context.getApplicationContext(), false, new LocationDelegate() {
//                public void onReceiveLocation(BDLocation location) {
//                    if (location != null) {
//                        PlaceLocationHelper.getInastance().queryAreaByAreaCode(context.getApplicationContext(), location.getCityCode(), null);
//                    }
//                }
//            });
        }
    }

    public void setAk(String ak) {
        BasePlaceApiRequester.AK = ak;
    }

//    public RouteModel getRouteModel() {
//        return this.routeModel;
//    }
//
//    public void setRouteModel(RouteModel routeModel) {
//        this.routeModel = routeModel;
//    }


}
