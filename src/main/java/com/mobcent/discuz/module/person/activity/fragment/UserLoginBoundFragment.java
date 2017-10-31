package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mobcent.android.constant.MCShareConstant;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.observer.ActivityObserver;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.UserLoginBoundActivity;
import com.mobcent.discuz.module.person.activity.delegate.RegistSuccDelegate;
import com.mobcent.discuz.module.person.activity.delegate.RegistSuccDelegate.OnRegistSuccListener;
import com.mobcent.login.android.helper.MCPlatformLoginHelper;
import com.mobcent.lowest.base.utils.MCAppUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;

public class UserLoginBoundFragment extends BaseFragment implements IntentConstant, FinalConstant {
    private Button boundBtn;
    private LoginBoundAsyncTask loginBoundTask;
    private EditText nameEdit;
    private ActivityObserver observer;
    private ObserverHelper observerHelper;
    private EditText passwordEdit;
    private TextView registBtn;
    private UserService userService;

    private class LoginBoundAsyncTask extends AsyncTask<String, Void, BaseResultModel<UserInfoModel>> {
        private String name;
        private String password;

        public LoginBoundAsyncTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(UserLoginBoundFragment.this.activity.getApplicationContext(), "mc_forum_warn_bound", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(String... params) {
            UserInfoModel userInfoModel = new UserInfoModel();
            userInfoModel.setNickname(this.name);
            userInfoModel.setPwd(this.password);
            userInfoModel.setOpenId(MCPlatformLoginHelper.getInstance().loginInfoModel.getOpenid());
            userInfoModel.setToken(MCPlatformLoginHelper.getInstance().loginInfoModel.getAccessToken());
            return UserLoginBoundFragment.this.userService.saveUserPlatforminfo(userInfoModel, MCShareConstant.IS_BIND, MCPlatformLoginHelper.getInstance().loginInfoModel.getPlatformType());
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            super.onPostExecute(result);
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserLoginBoundFragment.this.activity.getApplicationContext(), result);
            if (result != null && result.getRs() != 0) {
                LocationHelper.startLocation(UserLoginBoundFragment.this.activity);
                ObserverHelper.getInstance().getActivityObservable().loginSuccess();
                UserLoginBoundFragment.this.activity.finish();
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(UserLoginBoundFragment.this.activity.getApplicationContext(), "mc_forum_login_fail");
            } else {
                MCToastUtils.toast(UserLoginBoundFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected String getRootLayoutName() {
        return "user_login_bound_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.observerHelper = ObserverHelper.getInstance();
    }

    protected void initViews(View rootView) {
        this.nameEdit = (EditText) findViewByName(rootView, "user_login_name_edit");
        this.passwordEdit = (EditText) findViewByName(rootView, "user_login_password_edit");
        this.boundBtn = (Button) findViewByName(rootView, "bound_submit_btn");
        this.registBtn = (TextView) findViewByName(rootView, "reg_bound_btn");
        if (this.settingModel == null || this.settingModel.getAllowRegister() != 0) {
            this.registBtn.setVisibility(0);
        } else {
            this.registBtn.setVisibility(8);
        }
    }

    protected void initActions(View rootView) {
        this.boundBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String name = UserLoginBoundFragment.this.nameEdit.getText().toString();
                String password = UserLoginBoundFragment.this.passwordEdit.getText().toString();
                if (MCStringUtil.isEmpty(name)) {
                    MCToastUtils.toastByResName(UserLoginBoundFragment.this.activity.getApplicationContext(), "mc_forum_user_nickname_not_null");
                } else if (MCStringUtil.isEmpty(password)) {
                    MCToastUtils.toastByResName(UserLoginBoundFragment.this.activity.getApplicationContext(), "mc_forum_user_password_not_null");
                } else {
                    if (UserLoginBoundFragment.this.loginBoundTask != null) {
                        UserLoginBoundFragment.this.loginBoundTask.cancel(true);
                    }
                    UserLoginBoundFragment.this.loginBoundTask = new LoginBoundAsyncTask(name, password);
                    UserLoginBoundFragment.this.loginBoundTask.execute(new String[0]);
                }
            }
        });
        this.registBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginBoundFragment.this.activity, UserLoginBoundActivity.class);
                intent.putExtra(IntentConstant.INTENT_USER_LOGIN_BOUND_FLAG, false);
                UserLoginBoundFragment.this.activity.startActivity(intent);
            }
        });
        this.observer = new ActivityObserver() {
            public void loginSuccess() {
                if (UserLoginBoundFragment.this.activity != null) {
                    UserLoginBoundFragment.this.activity.finish();
                }
            }
        };
        this.observerHelper.getActivityObservable().registerObserver(this.observer);
        RegistSuccDelegate.getInstance().setOnRegistSuccListener(new OnRegistSuccListener() {
            public void registSucc() {
                if (UserLoginBoundFragment.this.activity != null) {
                    UserLoginBoundFragment.this.activity.finish();
                }
            }
        });
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_user_welcome_text") + MCAppUtil.getAppName(this.activity.getApplicationContext());
        dealTopBar(topSettingModel);
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.loginBoundTask != null) {
            this.loginBoundTask.cancel(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.observerHelper != null) {
            this.observerHelper.getActivityObservable().unregisterObserver(this.observer);
        }
    }
}
