package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import com.mobcent.discuz.android.db.constant.UserDBConstant;

public class MCPhoneConnectionUtil {
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        return true;
    }

    public static String getNetworkType(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return "wifi not available";
        }
        if (info.getTypeName().toLowerCase().equals("wifi")) {
            return "wifi";
        }
        return info.getExtraInfo().toLowerCase();
    }

    public static WifiInfo getWifiStatus(Context context) {
        return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
    }

    public static String getAccessPointType(Context context) {
        NetworkInfo netWorkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (netWorkInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
            return netWorkInfo.getExtraInfo();
        }
        return null;
    }

    public static boolean isMobileType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService("connectivity");
        if (manager == null) {
            return false;
        }
        NetworkInfo netWorkInfo = manager.getActiveNetworkInfo();
        if (netWorkInfo == null || !netWorkInfo.getTypeName().equalsIgnoreCase("mobile")) {
            return false;
        }
        return true;
    }

    public static boolean isCmwap(Context context) {
        if (!isConnectChinaMobile(context) || !isMobileType(context)) {
            return false;
        }
        String currentApnProxy = getCurrentApnProxy(context);
        if (currentApnProxy == null) {
            return false;
        }
        if (currentApnProxy.equals("10.0.0.172") || currentApnProxy.equals("010.000.000.172")) {
            return true;
        }
        return false;
    }

    public static boolean isUniwap(Context context) {
        if (!isConnectChinaUnicom(context) || !isMobileType(context)) {
            return false;
        }
        String currentApnProxy = getCurrentApnProxy(context);
        if (currentApnProxy == null) {
            return false;
        }
        if (currentApnProxy.equals("10.0.0.172") || currentApnProxy.equals("010.000.000.172")) {
            return true;
        }
        return false;
    }

    public static String getCurrentApnProxy(Context context) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
            if (c == null || !c.moveToFirst()) {
                if (c != null) {
                    c.close();
                }
                return null;
            }
            String string = c.getString(c.getColumnIndex("proxy"));
            return string;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static String getProxyIp(String apnId, Context context) {
        if (apnId == null) {
            return null;
        }
        Cursor c = null;
        try {
            c = context.getContentResolver().query(Uri.parse("content://telephony/carriers"), null, null, null, null);
            while (c != null && c.moveToNext()) {
                if (apnId.trim().equals(c.getString(c.getColumnIndex(UserDBConstant.COLUMN_ID)))) {
                    String string = c.getString(c.getColumnIndex("proxy"));
                    if (c == null) {
                        return string;
                    }
                    c.close();
                    return string;
                }
            }
            if (c != null) {
                c.close();
            }
            return null;
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
        }
        return "";
    }

    public static boolean isConnectChinaMobile(Context context) {
        String operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (operator == null) {
            return false;
        }
        if (operator.startsWith("46000") || operator.startsWith("46002")) {
            return true;
        }
        return false;
    }

    public static boolean isConnectChinaUnicom(Context context) {
        String operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (operator == null || !operator.startsWith("46001")) {
            return false;
        }
        return true;
    }

    public static boolean isConnectChinaTelecom(Context context) {
        String operator = ((TelephonyManager) context.getSystemService("phone")).getSimOperator();
        if (operator == null || !operator.startsWith("46003")) {
            return false;
        }
        return true;
    }
}
