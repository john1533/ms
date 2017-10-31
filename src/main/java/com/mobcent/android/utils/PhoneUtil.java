package com.mobcent.android.utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;

public class PhoneUtil {
    public static String getMacAddress(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    public static int getDisplayDpi(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }
}
