package com.mobcent.update.android.ui.activity.receiver;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.update.android.util.ApkUtil;

public class DownReceiver extends BroadcastReceiver {
    public String TAG = "DownReceiver";

    @SuppressLint({"NewApi"})
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                long downId = intent.getLongExtra("extra_download_id", -1);
                DownloadManager dm = (DownloadManager) context.getSystemService(MCLibIOUtil.DOWNLOAD);
                Query query = new Query();
                query.setFilterById(new long[]{downId});
                Cursor cursor = dm.query(query);
                if (cursor != null) {
                    try {
                        cursor.moveToFirst();
                        String path = cursor.getString(cursor.getColumnIndex("local_filename"));
                        if (!TextUtils.isEmpty(path)) {
                            ApkUtil.installApk(context, path);
                        }
                        cursor.close();
                    } catch (Exception e) {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                }
            }
        } catch (Exception e2) {
        }
    }
}
