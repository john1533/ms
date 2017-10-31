package com.mobcent.update.android.util;

import android.util.Log;

public class MCLogUtil {
    public static int DEBUG = 2;
    public static int ERROR = 5;
    public static int INFO = 3;
    public static int VERBOSE = 1;
    public static int WARN = 4;
    private static boolean isLog = false;
    private static int level = DEBUG;

    public static void i(String tag, String msg) {
        if (isPrintLog() && level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isPrintLog() && level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isPrintLog() && level <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isPrintLog() && level <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (isPrintLog() && level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static boolean isPrintLog() {
        return isLog;
    }
}
