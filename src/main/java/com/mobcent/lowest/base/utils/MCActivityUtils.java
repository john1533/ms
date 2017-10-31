package com.mobcent.lowest.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;

import java.util.List;

public class MCActivityUtils {
    public static boolean isAction(Context context) {
        List<RunningTaskInfo> tasks = ((ActivityManager) context.getApplicationContext().getSystemService(PostsConstant.TOPIC_TYPE_ACTIVITY)).getRunningTasks(1);
        if (tasks == null || tasks.isEmpty() || !((RunningTaskInfo) tasks.get(0)).topActivity.getPackageName().equals(context.getApplicationContext().getPackageName())) {
            return false;
        }
        return true;
    }

    public static void getTopActivity(Context context) {
        for (RunningTaskInfo info : ((ActivityManager) context.getSystemService(PostsConstant.TOPIC_TYPE_ACTIVITY)).getRunningTasks(100)) {
            MCLogUtil.e("ActivityUtils", "info.topActivity============" + info.topActivity + "============info.id===========" + info.id + "=============info.baseActivity===" + info.baseActivity);
        }
    }

    public static int getTheme(Context context,boolean isLight){
        return  isLight ? MCResource.getInstance(context).getStyleId("light") : MCResource.getInstance(context).getStyleId("dark");
    }

    public static int getTheme(Context context){
        SharedPreferencesDB sharedPreferencesDB = SharedPreferencesDB.getInstance(context);
        boolean isLight = sharedPreferencesDB.isLight();
        return  getTheme(context,isLight);
    }
}
