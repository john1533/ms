package com.mobcent.discuz.activity.utils;

import android.content.Context;
import android.widget.Toast;
import com.mobcent.discuz.android.model.BaseResult;

public class DZToastAlertUtils {
    public static void toast(Context context, BaseResult result) {
        if (result != null && result.getAlert() == 1) {
            Toast.makeText(context, result.getErrorInfo(), 0).show();
        }
    }
}
