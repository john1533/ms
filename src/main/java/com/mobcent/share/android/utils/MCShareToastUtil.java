package com.mobcent.share.android.utils;

import android.app.Activity;
import android.widget.Toast;

public class MCShareToastUtil {
    private static Toast mToast;

    public static void toast(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (MCShareToastUtil.mToast != null) {
                    MCShareToastUtil.mToast.cancel();
                    MCShareToastUtil.mToast = null;
                }
                MCShareToastUtil.mToast = Toast.makeText(activity, message, 0);
                MCShareToastUtil.mToast.show();
            }
        });
    }
}
