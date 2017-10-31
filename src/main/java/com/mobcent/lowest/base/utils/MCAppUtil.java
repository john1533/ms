package com.mobcent.lowest.base.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import com.mobcent.discuz.android.constant.PostsConstant;

public class MCAppUtil {
    public static String getAppName(Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 16384).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static Drawable getAppIcon(Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    public static int getPid(Context context, String processName) {
        for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(PostsConstant.TOPIC_TYPE_ACTIVITY)).getRunningAppProcesses()) {
            if (processName.equals(appProcess.processName)) {
                return appProcess.pid;
            }
        }
        return 0;
    }
}
