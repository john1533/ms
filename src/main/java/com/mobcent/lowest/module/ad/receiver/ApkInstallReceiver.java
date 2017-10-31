package com.mobcent.lowest.module.ad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mobcent.lowest.base.utils.MCApkUtil;
import com.mobcent.lowest.module.ad.db.DownSqliteHelper;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import com.mobcent.lowest.module.ad.task.ActiveDoTask;

public class ApkInstallReceiver extends BroadcastReceiver {
    private String TAG = "ApkInstallReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getData().getSchemeSpecificPart();
        if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
            AdDownDbModel adDownModel = DownSqliteHelper.getInstance(context).queryByPackageName(packageName);
            if (adDownModel != null) {
                if (adDownModel.getActiveDo() != 1) {
                    new ActiveDoTask(context, adDownModel).execute(new Void[0]);
                }
                MCApkUtil.launchApk(context, packageName);
            }
        }
    }
}
