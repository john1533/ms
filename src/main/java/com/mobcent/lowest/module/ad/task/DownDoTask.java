package com.mobcent.lowest.module.ad.task;

import android.content.Context;
import com.mobcent.lowest.module.ad.db.DownSqliteHelper;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;

public class DownDoTask extends BaseTask<Void, Void, Boolean> {
    public String TAG = "DownDoTask";
    private AdDownDbModel adDbModel;
    private DownSqliteHelper sqliteHelper;

    public DownDoTask(Context context, AdDownDbModel adDbModel) {
        super(context);
        this.adDbModel = adDbModel;
        this.sqliteHelper = DownSqliteHelper.getInstance(context);
    }

    protected Boolean doInBackground(Void... params) {
        this.adDbModel = this.sqliteHelper.queryByPackageName(this.adDbModel.getPn());
        if (this.adDbModel == null || this.adDbModel.getDownloadDo() == 1) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(this.adService.downAdDo(this.adDbModel.getAid(), this.adDbModel.getPo(), this.adDbModel.getPn(), this.adDbModel.getDate()));
    }

    protected void onPostExecute(Boolean result) {
        if (result.booleanValue()) {
            this.adDbModel.setDownloadDo(1);
        } else {
            this.adDbModel.setDownloadDo(2);
        }
        this.sqliteHelper.update(this.adDbModel);
    }
}
