package com.mobcent.lowest.base.utils;

import android.os.Build.VERSION;
import android.util.Log;

public class MCLogUtil {
    public static int DEBUG = 2;
    public static int ERROR = 5;
    public static int INFO = 3;
    public static int VERBOSE = 1;
    public static int WARN = 4;
    private static boolean isLog = true;
    private static int level = VERBOSE;

    public static void i(String tag, String msg) {
        if (isPrintLog() && level <= INFO) {
            Log.i(tag, getMsgTag(tag) + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isPrintLog() && level <= DEBUG) {
            Log.d(tag, getMsgTag(tag) + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrintLog() && level <= WARN) {
            Log.w(tag, getMsgTag(tag) + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isPrintLog() && level <= ERROR) {
            Log.e(tag, getMsgTag(tag) + msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isPrintLog() && level <= VERBOSE) {
            Log.v(tag, getMsgTag(tag) + msg);
        }
    }

    public static boolean isPrintLog() {
        return isLog;
    }

    private static String getMsgTag(String tag) {
        if (VERSION.SDK_INT > 15) {
            return "mclog >> " + tag + " >> ";
        }
        return "";
    }
}
