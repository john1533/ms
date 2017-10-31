package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.widget.Toast;

public class MCToastUtils {
    public static void toast(Context context, String msg) {
        toast(context, msg, 1);
    }

    public static void toast(Context context, String msg, int time) {
        Toast.makeText(context, msg, time).show();
    }

    public static void toastByResName(Context context, String resName) {
        toastByResName(context, resName, 1);
    }

    public static void toastByResName(Context context, String resName, int time) {
        Toast.makeText(context, MCResource.getInstance(context.getApplicationContext()).getString(resName), time).show();
    }
}
