package com.mobcent.discuz.android.os.helper;

import android.content.Context;
import android.content.Intent;
import com.mobcent.discuz.android.os.HeartBeatOSService;

public class HeartBeatServiceHelper {
    public static void startService(Context context) {
        startService(context, 0);
    }

    public static void startService(Context context, int status) {
        Intent intent = new Intent(context, HeartBeatOSService.class);
        intent.putExtra("status", status);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        context.stopService(new Intent(context, HeartBeatOSService.class));
    }
}
