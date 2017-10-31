package com.mobcent.lowest.module.ad.task;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.mobcent.lowest.base.utils.MCApkUtil;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.db.DownSqliteHelper;
import com.mobcent.lowest.module.ad.helper.AdNotificationHelper;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import com.mobcent.lowest.module.ad.receiver.DownFailedReceiver;
import com.mobcent.lowest.module.ad.utils.DownloadUtils;
import com.mobcent.lowest.module.ad.utils.DownloadUtils.DownProgressDelegate;
import java.io.File;

public class DownTask extends BaseTask<Void, Integer, Boolean> {
    public String TAG = "DownloadTask";
    private AdDownDbModel adDownDbModel;
    private int currentProgress;
    private File file;
    private String filePath = null;
    private NotificationManager manager;
    private Notification notification;
    private int progressMax;
    private DownSqliteHelper sqliteHelper;

    public DownTask(Context context, AdDownDbModel adDownDbModel) {
        super(context);
        this.manager = AdNotificationHelper.getManager(context);
        this.adDownDbModel = adDownDbModel;
        String dir = DownloadUtils.getDownloadDirPath(context);
        this.filePath = new StringBuilder(String.valueOf(dir)).append(File.separator).append(MCStringUtil.getMD5Str(adDownDbModel.getUrl())).toString();
        this.file = new File(this.filePath);
        this.sqliteHelper = DownSqliteHelper.getInstance(context);
        adDownDbModel.setStatus(1);
    }

    protected void onPreExecute() {
        this.notification = AdNotificationHelper.createNotification(this.context);
        this.manager.notify(this.adDownDbModel.getId(), this.notification);
    }

    protected Boolean doInBackground(Void... params) {
        return Boolean.valueOf(DownloadUtils.downloadFile(this.context, this.adDownDbModel.getUrl(), this.file, new DownProgressDelegate() {
            public void setProgress(int progress) {
                DownTask.this.currentProgress = progress;
                DownTask.this.updateNotification(false, DownTask.this.adDownDbModel);
            }

            public void setMax(int max) {
                DownTask.this.progressMax = max;
                setProgress(0);
            }
        }));
    }

    protected void onPostExecute(Boolean result) {
        if (result.booleanValue()) {
            this.adDownDbModel.setStatus(2);
            this.adDownDbModel.setDate(MCDateUtil.getFormatTimeToSecond(System.currentTimeMillis()));
            String pn = MCApkUtil.getPackageName(this.context, this.filePath);
            this.adDownDbModel.setPn(pn);
            this.sqliteHelper.update(this.adDownDbModel);
            this.manager.cancel(this.adDownDbModel.getId());
            if (!MCStringUtil.isEmpty(pn)) {
                MCApkUtil.installApk(this.context, this.filePath);
                if (this.adDownDbModel.getDownloadDo() != 1) {
                    new DownDoTask(this.context, this.adDownDbModel).execute(new Void[0]);
                    return;
                }
                return;
            }
            return;
        }
        updateNotification(true, this.adDownDbModel);
        this.adDownDbModel.setStatus(3);
        this.sqliteHelper.update(this.adDownDbModel);
    }

    private void updateNotification(boolean isBtnVisible, AdDownDbModel adDownDbModel) {
        RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), MCResource.getInstance(this.context).getLayoutId("mc_ad_widget_notification"));
        remoteViews.setProgressBar(this.resource.getViewId("download_progress_bar"), this.progressMax, this.currentProgress, false);
        if (adDownDbModel.getAppName() != null) {
            remoteViews.setTextViewText(this.resource.getViewId("download_title_text"), adDownDbModel.getAppName());
        } else {
            remoteViews.setTextViewText(this.resource.getViewId("download_title_text"), getFileName(adDownDbModel.getUrl()));
        }
        Intent intent;
        PendingIntent pendingIntent;
        if (isBtnVisible) {
            this.notification.flags = 16;
            intent = new Intent(DownFailedReceiver.getAction(this.context));
            intent.putExtra(DownFailedReceiver.AD_DOWNLOAD_MODEL, adDownDbModel);
            pendingIntent = PendingIntent.getBroadcast(this.context, adDownDbModel.getId(), intent, 134217728);
            remoteViews.setViewVisibility(this.resource.getViewId("continue_btn"), 0);
            remoteViews.setOnClickPendingIntent(this.resource.getViewId("continue_btn"), pendingIntent);
            this.notification.contentIntent = pendingIntent;
        } else {
            this.notification.flags = 32;
            remoteViews.setViewVisibility(this.resource.getViewId("continue_btn"), 8);
            intent = new Intent(DownFailedReceiver.getAction(this.context));
            intent.putExtra(DownFailedReceiver.AD_DOWNLOAD_MODEL, adDownDbModel);
            pendingIntent = PendingIntent.getBroadcast(this.context, adDownDbModel.getId(), intent, 134217728);
            remoteViews.setOnClickPendingIntent(this.resource.getViewId("continue_btn"), pendingIntent);
            this.notification.contentIntent = pendingIntent;
        }
        this.notification.contentView = remoteViews;
        this.manager.notify(adDownDbModel.getId(), this.notification);
    }

    private String getFileName(String url) {
        if (MCStringUtil.isEmpty(url)) {
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }
}
