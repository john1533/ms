package com.mobcent.update.android.task;

import android.content.Context;
import android.os.Handler;
import com.mobcent.update.android.model.UpdateModel;
import com.mobcent.update.android.service.UpdateService;
import com.mobcent.update.android.service.impl.UpdateServiceImpl;

public class UpdateCheckTask extends Thread {
    private UpdateCallback callBack;
    private Context context;
    private Handler mHandler = null;
    private UpdateService updateService;

    public UpdateCheckTask(Context context) {
        this.context = context.getApplicationContext();
        this.updateService = new UpdateServiceImpl(this.context);
        this.mHandler = new Handler();
    }

    public UpdateCheckTask(Context context, UpdateCallback callBack) {
        this.context = context.getApplicationContext();
        this.updateService = new UpdateServiceImpl(this.context);
        this.mHandler = new Handler();
        this.callBack = callBack;
    }

    public void run() {
        final UpdateModel updateModel = this.updateService.getUpdateInfo(this.context);
        this.mHandler.post(new Runnable() {
            public void run() {
                UpdateCheckTask.this.notifyResult(updateModel);
            }
        });
    }

    public void notifyResult(UpdateModel updateModel) {
        getCallBack().onChecked(updateModel != null);
        UpdateDialogHelper.notifyDialog(this.context, updateModel);
    }

    public UpdateCallback getCallBack() {
        if (this.callBack == null) {
            this.callBack = new UpdateCallback() {
                public void onChecked(boolean isHaveNew) {
                }
            };
        }
        return this.callBack;
    }

    public void setCallBack(UpdateCallback callBack) {
        this.callBack = callBack;
    }
}
