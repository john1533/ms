package com.mobcent.lowest.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.lowest.base.delegate.MCLocationListener;
import java.util.Locale;

public class MCPhoneUtil {

    static class AnonymousClass1 implements LocationListener {
        private final /* synthetic */ MCLocationListener val$listener;
        private final /* synthetic */ LocationManager val$locationManager;

        AnonymousClass1(LocationManager locationManager, MCLocationListener mCLocationListener) {
            this.val$locationManager = locationManager;
            this.val$listener = mCLocationListener;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            MCLogUtil.e("PhoneUtil", "onStatusChanged");
        }

        public void onProviderEnabled(String provider) {
            MCLogUtil.e("PhoneUtil", "onProviderEnabled");
            this.val$listener.onLocationChanged(this.val$locationManager.getLastKnownLocation(provider));
            this.val$locationManager.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {
            MCLogUtil.e("PhoneUtil", "onProviderDisabled");
            this.val$locationManager.removeUpdates(this);
        }

        public void onLocationChanged(Location location) {
            MCLogUtil.e("PhoneUtil", "onLocationChanged");
            this.val$listener.onLocationChanged(location);
            this.val$locationManager.removeUpdates(this);
        }
    }

    public static String getUserAgent() {
        return getPhoneType();
    }

    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (imei == null) {
            return "";
        }
        return imei;
    }

    public static String getIMSI(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        if (imsi == null) {
            return "";
        }
        return imsi;
    }

    public static String getNetworkOperatorName(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
    }

    public static String getNetworkOperatorName(Activity activity) {
        return ((TelephonyManager) activity.getSystemService("phone")).getNetworkOperatorName();
    }

    public static String getPhoneType() {
        String type = Build.MODEL;
        if (type != null) {
            type = type.replace(" ", "");
        }
        return type.trim();
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static String getType() {
        return Build.TYPE;
    }

    public static String getSDKVersionName() {
        return VERSION.RELEASE;
    }

    public static String getSDKVersion() {
        return VERSION.SDK;
    }

    public static int getAndroidSDKVersion() {
        return VERSION.SDK_INT;
    }

    public static String getResolution(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    public static int getDisplayWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static float getDisplayDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    public static float getDisplayDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String getPhoneLanguage() {
        String language = Locale.getDefault().getLanguage();
        if (language == null) {
            return "";
        }
        return language;
    }

    public static String getLocalMacAddress(Context context) {
        WifiInfo info = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        if (info == null) {
            return "";
        }
        return info.getMacAddress();
    }

    public static int getCacheSize(Context context) {
        return (((ActivityManager) context.getSystemService(PostsConstant.TOPIC_TYPE_ACTIVITY)).getMemoryClass() * AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) / 4;
    }

    public static int getRawSize(Context context, int unit, float size) {
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return (int) TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
    }

    public static String getNetWorkName(Context context) {
        return MCPhoneConnectionUtil.getNetworkType(context).toLowerCase();
    }

    public static String getServiceName(Context context) {
        if (MCPhoneConnectionUtil.getNetworkType(context).equals("wifi")) {
            return "wifi";
        }
        if (MCPhoneConnectionUtil.isConnectChinaMobile(context)) {
            return "mobile";
        }
        if (MCPhoneConnectionUtil.isConnectChinaUnicom(context)) {
            return "unicom";
        }
        if (MCPhoneConnectionUtil.isConnectChinaTelecom(context)) {
            return "telecom";
        }
        return "";
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) TypedValue.applyDimension(1, dipValue, getDisplayMetrics(context));
    }

    public static float dip2pxFloat(Context context, float dipValue) {
        return TypedValue.applyDimension(1, dipValue, getDisplayMetrics(context));
    }

    public static int dip2px(float dipValue) {
        return (int) TypedValue.applyDimension(1, dipValue, Resources.getSystem().getDisplayMetrics());
    }

    public static void getLocation(Context context, MCLocationListener listener) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(2);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(1);
        criteria.setSpeedRequired(false);
        try {
            String currentProvider = locationManager.getBestProvider(criteria, true);
            Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
            if (currentLocation == null) {
                locationManager.requestLocationUpdates(currentProvider, 0, 0.0f, new AnonymousClass1(locationManager, listener));
            } else {
                listener.onLocationChanged(currentLocation);
            }
        } catch (Exception e) {
            listener.onLocationChanged(null);
        }
    }
}
