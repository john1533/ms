package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import java.io.File;
import java.util.List;

public class MCApkUtil {
    public static void installApk(Context context, String apkSDPath) {
        File file = new File(apkSDPath);
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void launchApk(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    public static String getPackageName(Context context, String path) {
        PackageInfo info = context.getPackageManager().getPackageArchiveInfo(path, 1);
        if (info != null) {
            return info.applicationInfo.packageName;
        }
        return null;
    }

    public static boolean isInstallApk(String packageName, Context context) {
        List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
        int size = packageInfoList.size();
        for (int i = 0; i < size; i++) {
            if (packageName.equals(((PackageInfo) packageInfoList.get(i)).packageName)) {
                return true;
            }
        }
        return false;
    }
}
