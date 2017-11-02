package com.mobcent.discuz.base.helper;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.service.impl.LocationServiceImpl;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;

public class LocationHelper {
    private static MCLocationUtil locationUtil = null;

//    public static void startLocation(Context context) {
//        initLocationUtil(context);
//    }

    public static void stopLocation(Context context) {
        if (locationUtil != null) {
            locationUtil.stopLocation();
        }
    }

//    public static void initLocationUtil(final Context context) {
//        locationUtil = MCLocationUtil.getInstance(context);
//        locationUtil.requestLocation(new LocationDelegate() {
//            public void onReceiveLocation(final BDLocation location) {
//                if (location != null) {
//                    SharedPreferencesDB.getInstance(context.getApplicationContext()).saveLocation(location);
//                    new Thread() {
//                        public void run() {
//                            new LocationServiceImpl(context).saveLocation(location.getLongitude(), location.getLatitude(), location.getAddrStr());
//                        }
//                    }.start();
//                }
//            }
//        });
//    }
}
