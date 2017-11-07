package com.mobcent.lowest.module.ad.utils;

import android.content.Context;
import android.os.Environment;
import com.mobcent.lowest.base.utils.MCPhoneConnectionUtil;

public class AdNeedInfoUtils {
    public static String getNetworkType(Context context) {
        if (MCPhoneConnectionUtil.getNetworkType(context).equals("wifi")) {
            return "1";
        }
        if (MCPhoneConnectionUtil.isConnectChinaMobile(context)) {
            return "2";
        }
        if (MCPhoneConnectionUtil.isConnectChinaUnicom(context)) {
            return "3";
        }
        if (MCPhoneConnectionUtil.isConnectChinaTelecom(context)) {
            return "4";
        }
        return "";
    }

    public static String getJWD(Context context) {
//        BDLocation location = LowestManager.getInstance().getConfig().getLocation();
//        if (location == null) {
//            return "";
//        }
//        return location.getLongitude() + "_" + location.getLatitude();
        return "";
    }

    public static String getLocation(Context context) {
//        BDLocation location = LowestManager.getInstance().getConfig().getLocation();
//        if (location == null || MCStringUtil.isEmpty(location.getAddrStr())) {
//            return "";
//        }
//        return location.getAddrStr();
        return "";
    }

    public static String getNet(Context context) {
        String netWorkType = MCPhoneConnectionUtil.getNetworkType(context).toLowerCase();
        if (netWorkType.equals("cmwap")) {
            return "1";
        }
        if (netWorkType.equals("cmnet")) {
            return "2";
        }
        if (netWorkType.equals("ctwap")) {
            return "3";
        }
        if (netWorkType.equals("ctnet")) {
            return "4";
        }
        if (netWorkType.equals("uniwap")) {
            return "5";
        }
        if (netWorkType.equals("uninet")) {
            return "6";
        }
        if (netWorkType.equals("3gwap")) {
            return "7";
        }
        if (netWorkType.equals("3gnet")) {
            return "8";
        }
        return "0";
    }

    public static int getSd() {
        String state = Environment.getExternalStorageState();
        if ("mounted".equals(state)) {
            return 1;
        }
        return "mounted_ro".equals(state) ? 0 : 0;
    }
}
