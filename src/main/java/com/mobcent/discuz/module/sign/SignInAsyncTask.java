package com.mobcent.discuz.module.sign;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import com.mobcent.discuz.android.model.BaseResult;
import com.mobcent.discuz.android.service.impl.SignInServiceImpl;
import com.mobcent.lowest.android.ui.utils.MCAnimationUtils;
import com.mobcent.lowest.android.ui.widget.sign.SignInDialog;
import com.mobcent.lowest.base.utils.MCToastUtils;

public class SignInAsyncTask extends AsyncTask<Void, Void, BaseResult> {
    private Context context;
    private View view;

    public SignInAsyncTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    protected BaseResult doInBackground(Void... params) {
        return new SignInServiceImpl(this.context).signIn();
    }

    protected void onPostExecute(BaseResult result) {
        super.onPostExecute(result);
        if (result.getRs() == 1) {
            try {
                SignInDialog registerDialog = new SignInDialog(this.context);
                registerDialog.getWindow().getAttributes().type = 2003;
                registerDialog.showDialog();
            } catch (Exception e) {
            }
        } else if (this.view != null) {
            MCAnimationUtils.shakeView(this.view, null);
        }
        if (result.getAlert() == 1) {
            MCToastUtils.toast(this.context, result.getErrorInfo());
        }
    }
}
