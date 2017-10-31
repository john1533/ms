package com.mobcent.discuz.base.task;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResult;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class BaseTask<T extends BaseResult> extends AsyncTask<Void, Void, T> {
    protected BaseRequestCallback<T> _callback;
    protected Context context;
    protected SharedPreferencesDB db = SharedPreferencesDB.getInstance(this.context);
    protected MCResource resource = MCResource.getInstance(this.context);

    public BaseTask(Context context, BaseRequestCallback<T> _callback) {
        this.context = context.getApplicationContext();
        this._callback = _callback;
    }

    protected void onPreExecute() {
        if (this._callback != null) {
            this._callback.onPreExecute();
        }
    }

    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        DZToastAlertUtils.toast(this.context, result);
        if (this._callback != null) {
            this._callback.onPostExecute(result);
        }
    }
}
