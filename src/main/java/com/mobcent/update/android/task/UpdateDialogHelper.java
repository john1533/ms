package com.mobcent.update.android.task;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.update.android.db.UpdateStatusDBUtil;
import com.mobcent.update.android.model.UpdateModel;
import com.mobcent.update.android.util.ApkUtil;
import com.mobcent.update.android.util.MCUpdateResource;

public class UpdateDialogHelper {

    static class AnonymousClass1 implements OnClickListener {
        private final /* synthetic */ Context val$context;
        private final /* synthetic */ UpdateStatusDBUtil val$dbUtil;
        private final /* synthetic */ MCUpdateResource val$resource;
        private final /* synthetic */ UpdateModel val$updateModel;

        AnonymousClass1(UpdateStatusDBUtil updateStatusDBUtil, UpdateModel updateModel, Context context, MCUpdateResource mCUpdateResource) {
            this.val$dbUtil = updateStatusDBUtil;
            this.val$updateModel = updateModel;
            this.val$context = context;
            this.val$resource = mCUpdateResource;
        }

        @SuppressLint({"NewApi"})
        public void onClick(DialogInterface dialog, int which) {
            this.val$dbUtil.saveOrUpdate(this.val$updateModel, 1);
            int sdkVersion = ApkUtil.sdkVersion();
            Intent i;
            if (sdkVersion >= 9) {
                DownloadManager dm = (DownloadManager) this.val$context.getSystemService(MCLibIOUtil.DOWNLOAD);
                Request request = new Request(Uri.parse(this.val$updateModel.getLink()));
                request.setTitle(this.val$updateModel.getAppName());
                request.setShowRunningNotification(true);
                request.setDescription(this.val$resource.getString("mc_update_download_update_ing"));
                if (sdkVersion >= 11) {
                    request.setNotificationVisibility(1);
                }
                try {
                    dm.enqueue(request);
                } catch (Exception e) {
                    i = new Intent("android.intent.action.VIEW", Uri.parse(this.val$updateModel.getLink()));
                    i.setFlags(268435456);
                    this.val$context.startActivity(i);
                }
            } else {
                i = new Intent("android.intent.action.VIEW", Uri.parse(this.val$updateModel.getLink()));
                i.setFlags(268435456);
                this.val$context.startActivity(i);
            }
            dialog.dismiss();
        }
    }

    static  class AnonymousClass2 implements OnClickListener {
        private final /* synthetic */ UpdateStatusDBUtil val$dbUtil;
        private final /* synthetic */ UpdateModel val$updateModel;

        AnonymousClass2(UpdateStatusDBUtil updateStatusDBUtil, UpdateModel updateModel) {
            this.val$dbUtil = updateStatusDBUtil;
            this.val$updateModel = updateModel;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.val$dbUtil.saveOrUpdate(this.val$updateModel, 2);
            dialog.dismiss();
        }
    }

    public static void notifyDialog(Context cxt, UpdateModel updateModel) {
        Context context = cxt.getApplicationContext();
        UpdateStatusDBUtil dbUtil = new UpdateStatusDBUtil(context);
        if (updateModel != null && updateModel.getAppName() != null && !updateModel.getAppName().equals("")  && !updateModel.getVer_name().equals("") && updateModel.getVer_name() != null) {
            MCUpdateResource resource = MCUpdateResource.getInstance(context);
            String title = resource.getString("mc_update_notificaion_title");
            String content = resource.getString("mc_update_dialog_new") + updateModel.getAppName() + AdApiConstant.RES_SPLIT_COMMA + resource.getString("mc_update_dialog_version") + updateModel.getVer_name() + AdApiConstant.RES_SPLIT_COMMA + resource.getStringId("mc_update_dialog_install");
            if (!TextUtils.isEmpty(updateModel.getDesc())) {
                content = new StringBuilder(String.valueOf(content)).append("\n").append(resource.getString("mc_update_dialog_desc")).append("\n").append(updateModel.getDesc()).toString();
            }
            AlertDialog dialog = new Builder(context.getApplicationContext())
                    .setIcon(17301659)
                    .setTitle(title)
                    .setMessage(content)
                    .setPositiveButton(resource.getString("mc_update_dialog_confirm")
                    , new AnonymousClass1(dbUtil, updateModel, context, resource)).setNegativeButton(context.getResources().getString(resource.getStringId("mc_update_dialog_cancel"))
                    , new AnonymousClass2(dbUtil, updateModel)).create();
            dialog.getWindow().setType(2003);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
