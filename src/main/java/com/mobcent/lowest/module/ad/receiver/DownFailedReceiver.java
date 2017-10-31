package com.mobcent.lowest.module.ad.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.mobcent.lowest.base.utils.MCAppUtil;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import com.mobcent.lowest.module.ad.task.manager.DownTaskManager;

public class DownFailedReceiver extends BroadcastReceiver {
    public static final String AD_DOWNLOAD_MODEL = "adDownloadModel";
    public String TAG = "DownFailedReceiver";
    private AdDownDbModel adDownDbModel = null;

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && action.equals(getAction(context))) {
                this.adDownDbModel = null;
                this.adDownDbModel = (AdDownDbModel) intent.getSerializableExtra(AD_DOWNLOAD_MODEL);
                DownTaskManager.getInastance().downloadApk(context, this.adDownDbModel);
            }
        }
    }

    public static String getAction(Context context) {
        return MCAppUtil.getPackageName(context) + "." + DownFailedReceiver.class.getName();
    }
}
