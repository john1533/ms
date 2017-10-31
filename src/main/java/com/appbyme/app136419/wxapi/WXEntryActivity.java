package com.appbyme.app136419.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import com.mobcent.android.model.PlatformLoginInfoModel;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.module.person.activity.UserLoginBoundActivity;
import com.mobcent.login.android.helper.MCPlatformLoginHelper;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;

public class WXEntryActivity extends Activity {
    private Activity activity;

    private class WeChatAsyncTask extends AsyncTask<Void, Void, BaseResultModel<UserInfoModel>> {
        private String code;
        private Context context;
        private MCResource resource = MCResource.getInstance(this.context);

        public WeChatAsyncTask(Context context2, String code2) {
            this.context = context2;
            this.code = code2;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(this.context, "mc_forum_user_wechat_authorization", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(Void... arg0) {
            MCPlatformLoginHelper.getInstance().requestWeChatInfo(this.context, this.code, this.resource.getString("mc_wechat_appid"), this.resource.getString("mc_wechat_appsecret"));
            UserService userService = new UserServiceImpl(this.context);
            PlatformLoginInfoModel loginInfoModel = MCPlatformLoginHelper.getInstance().loginInfoModel;
            return userService.getUserPlatforminfo(loginInfoModel.getOpenid(), loginInfoModel.getAccessToken(), loginInfoModel.getPlatformType());
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> baseResultModel) {
            super.onPostExecute(baseResultModel);
            if (baseResultModel == null || baseResultModel.getRs() == 0) {
                DZProgressDialogUtils.hideProgressDialog();
                MCToastUtils.toastByResName(this.context, "mc_forum_user_wechat_authorization_error");
                WXEntryActivity.this.finish();
                return;
            }
            final UserInfoModel userInfoModel = (UserInfoModel) baseResultModel.getData();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    DZProgressDialogUtils.hideProgressDialog();
                    if (userInfoModel.isRegister()) {
                        Intent intent = new Intent(WeChatAsyncTask.this.context, UserLoginBoundActivity.class);
                        intent.putExtra(IntentConstant.INTENT_USER_LOGIN_BOUND_FLAG, true);
                        WXEntryActivity.this.activity.startActivity(intent);
                    } else {
                        ObserverHelper.getInstance().getActivityObservable().loginSuccess();
                    }
                    WXEntryActivity.this.finish();
                }
            }, 1000);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        requestWindowFeature(1);
        handleIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @SuppressLint({"ShowToast"})
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            finish();
            return;
        }
        Resp resp = new Resp(intent.getExtras());
        if (resp == null) {
            finish();
        } else if (!"wechat_login".equals(resp.state)) {
            finish();
        } else if (resp.errCode == 0) {
            new WeChatAsyncTask(getApplicationContext(), resp.code).execute(new Void[0]);
        } else {
            MCToastUtils.toastByResName(getApplicationContext(), "mc_forum_user_wechat_authorization_error");
            finish();
        }
    }
}
