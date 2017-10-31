package com.mobcent.update.android.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import java.io.File;

public class ApkUtil {
    public static void installApk(Context context, String path) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        File file = new File(path);
        if (file.exists()) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    public static void launchApk(Context context, String packageName) {
        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
    }

    public static int sdkVersion() {
        return VERSION.SDK_INT;
    }
}
