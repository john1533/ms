package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.android.model.PlatformLoginInfoModel;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCPopupListView;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupClickListener;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.observer.ActivityObserver;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.PlatformLoginActivity;
import com.mobcent.discuz.module.person.activity.UserLoginBoundActivity;
import com.mobcent.discuz.module.person.activity.UserRegistActivity;
import com.mobcent.discuz.module.person.activity.UserSettingActivity;
import com.mobcent.discuz.module.person.activity.delegate.RegistSuccDelegate;
import com.mobcent.discuz.module.person.activity.delegate.RegistSuccDelegate.OnRegistSuccListener;
import com.mobcent.discuz.module.person.activity.fragment.adapter.PlatformLoginAdapter;
import com.mobcent.discuz.module.person.activity.fragment.adapter.RegLoginListAdapter;
import com.mobcent.login.android.helper.MCPlatformLoginHelper;
import com.mobcent.login.android.helper.MCPlatformLoginHelper.LoginCallbackListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserLoginFragment extends BaseFragment implements IntentConstant, FinalConstant {
    private int MORE = 1;
    private GetDataTask getDataTask;
    private HashMap<String, Serializable> goParam;
    private Class<?> goToActivityClass;
    private GridView grid;
    private PlatformLoginAdapter gridAdapter;
    private RelativeLayout layoutBox;
    private ListView list;
    private RegLoginListAdapter listAdapter;
    private Button loginBtn;
    private List<String> loginNameList;
    private LoginAsyncTask loginTask;
    private EditText nameEdit;
    private ActivityObserver observer;
    private ObserverHelper observerHelper;
    private EditText passwordEdit;
    private MCPopupListView popupListView;
    private LinearLayout qqLoginBtn;
    private Button registBtn;
    private List<UserInfoModel> userInfoList;
    private UserInfoModel userInfoModel;
    private UserService userService;
    private LinearLayout warnBox;
    private LinearLayout wechatLoginBtn;

    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        private GetDataTask() {
        }

        protected Void doInBackground(Void... arg0) {
            UserLoginFragment.this.userInfoModel = UserLoginFragment.this.userService.getLastUserInfo();
            UserLoginFragment.this.userInfoList = UserLoginFragment.this.userService.getAllUserInfo();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (UserLoginFragment.this.userInfoModel != null) {
                if (!MCStringUtil.isEmpty(UserLoginFragment.this.userInfoModel.getNickname())) {
                    UserLoginFragment.this.nameEdit.setText(UserLoginFragment.this.userInfoModel.getNickname());
                }
                if (!MCStringUtil.isEmpty(UserLoginFragment.this.userInfoModel.getPwd())) {
                    UserLoginFragment.this.passwordEdit.setText(UserLoginFragment.this.userInfoModel.getPwd());
                }
            }
        }
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, BaseResultModel<UserInfoModel>> {
        private String name;
        private String password;

        public LoginAsyncTask(String name, String password) {
            this.name = name;
            this.password = password;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(UserLoginFragment.this.activity.getApplicationContext(), "mc_forum_warn_login", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(String... params) {
            return UserLoginFragment.this.userService.loginUser(this.name, this.password, "login");
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            super.onPostExecute(result);
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserLoginFragment.this.activity.getApplicationContext(), result);
            if (result.getRs() != 0) {
                UserLoginFragment.this.goToTargetActivity();
//                LocationHelper.startLocation(UserLoginFragment.this.activity);
                UserLoginFragment.this.activity.finish();
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(UserLoginFragment.this.activity.getApplicationContext(), "mc_forum_login_fail");
            } else {
                MCToastUtils.toast(UserLoginFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    private class QQLoginAsyncTask extends AsyncTask<Void, Void, BaseResultModel<UserInfoModel>> {
        private Context context;

        public QQLoginAsyncTask(Context context2) {
            this.context = context2;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(this.context, "mc_forum_user_wechat_authorization", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(Void... arg0) {
            UserService userService = new UserServiceImpl(this.context);
            PlatformLoginInfoModel loginInfoModel = MCPlatformLoginHelper.getInstance().loginInfoModel;
            return userService.getUserPlatforminfo(loginInfoModel.getOpenid(), loginInfoModel.getAccessToken(), loginInfoModel.getPlatformType());
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> baseResultModel) {
            super.onPostExecute(baseResultModel);
            if (baseResultModel == null || baseResultModel.getRs() == 0) {
                DZProgressDialogUtils.hideProgressDialog();
                MCToastUtils.toastByResName(this.context, "mc_forum_user_wechat_authorization_error");
                UserLoginFragment.this.activity.finish();
                return;
            }
            final UserInfoModel userInfoModel = (UserInfoModel) baseResultModel.getData();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    DZProgressDialogUtils.hideProgressDialog();
                    if (userInfoModel.isRegister()) {
                        Intent intent = new Intent(QQLoginAsyncTask.this.context, UserLoginBoundActivity.class);
                        intent.putExtra(IntentConstant.INTENT_USER_LOGIN_BOUND_FLAG, true);
                        UserLoginFragment.this.activity.startActivity(intent);
                        return;
                    }
                    ObserverHelper.getInstance().getActivityObservable().loginSuccess();
                }
            }, 1000);
        }
    }

    protected String getRootLayoutName() {
        return "user_login_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.goToActivityClass = (Class) getBundle().getSerializable(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS);
        this.goParam = (HashMap) getBundle().getSerializable(IntentConstant.INTENT_GO_PARAM);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.loginNameList = new ArrayList();
        this.observerHelper = ObserverHelper.getInstance();
    }

    protected void initViews(View rootView) {
        this.nameEdit = (EditText) findViewByName(rootView, "user_login_name_edit");
        this.passwordEdit = (EditText) findViewByName(rootView, "user_login_password_edit");
        this.loginBtn = (Button) findViewByName(rootView, "login_submit_btn");
        this.registBtn = (Button) findViewByName(rootView, "reg_submit_btn");
        this.grid = (GridView) findViewByName(rootView, "platform_grid");
        this.list = (ListView) findViewByName(rootView, "user_login_name_list");
        this.popupListView = (MCPopupListView) findViewByName(rootView, "more_popup_listview");
        if (this.listAdapter == null) {
            this.listAdapter = new RegLoginListAdapter(this.activity.getApplicationContext(), this.loginNameList);
        }
        this.list.setAdapter(this.listAdapter);
        this.warnBox = (LinearLayout) findViewByName(rootView, "user_warn_box");
        this.wechatLoginBtn = (LinearLayout) findViewByName(rootView, "user_wechat_login_btn");
        this.qqLoginBtn = (LinearLayout) findViewByName(rootView, "user_qq_login_btn");
        SharedPreferencesDB sharedPreferencesDB = SharedPreferencesDB.getInstance(this.activity.getApplicationContext());
        if (sharedPreferencesDB.getQQConnect() == 0) {
            this.qqLoginBtn.setVisibility(View.GONE);
        }
        if (sharedPreferencesDB.getWXConnect() == 0) {
            this.wechatLoginBtn.setVisibility(8);
        }
        if (sharedPreferencesDB.getQQConnect() == 0 && sharedPreferencesDB.getWXConnect() == 0) {
            this.warnBox.setVisibility(8);
        }
        if (this.settingModel == null || this.settingModel.getAllowRegister() != 0) {
            this.registBtn.setVisibility(0);
        } else {
            this.registBtn.setVisibility(8);
        }
        this.layoutBox = (RelativeLayout) findViewByName(rootView, "user_login_box");
    }

    protected void initActions(View rootView) {
        this.getDataTask = new GetDataTask();
        this.getDataTask.execute(new Void[0]);
        this.layoutBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (UserLoginFragment.this.list.getVisibility() == 0) {
                    UserLoginFragment.this.list.setVisibility(8);
                }
                return false;
            }
        });
        this.nameEdit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = UserLoginFragment.this.nameEdit.getText().toString();
                if (name.length() == 0) {
                    UserLoginFragment.this.getAllUser();
                } else {
                    UserLoginFragment.this.getUser(name);
                }
            }

            public void afterTextChanged(Editable s) {
                if (UserLoginFragment.this.nameEdit.getText().toString().length() > 0) {
                    UserLoginFragment.this.getPwdByName(UserLoginFragment.this.nameEdit.getText().toString());
                }
            }
        });
        this.passwordEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!MCStringUtil.isEmpty(UserLoginFragment.this.passwordEdit.getText().toString())) {
                    UserLoginFragment.this.passwordEdit.setText("");
                }
                UserLoginFragment.this.list.setVisibility(8);
            }
        });
        this.loginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String name = UserLoginFragment.this.nameEdit.getText().toString();
                String password = UserLoginFragment.this.passwordEdit.getText().toString();
                if (MCStringUtil.isEmpty(name)) {
                    MCToastUtils.toastByResName(UserLoginFragment.this.activity.getApplicationContext(), "mc_forum_user_nickname_not_null");
                } else if (MCStringUtil.isEmpty(password)) {
                    MCToastUtils.toastByResName(UserLoginFragment.this.activity.getApplicationContext(), "mc_forum_user_password_not_null");
                } else {
                    if (UserLoginFragment.this.loginTask != null) {
                        UserLoginFragment.this.loginTask.cancel(true);
                    }
                    UserLoginFragment.this.loginTask = new LoginAsyncTask(name, password);
                    UserLoginFragment.this.loginTask.execute(new String[0]);
                }
            }
        });
        this.registBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean localRegist = true;
                if (!(UserLoginFragment.this.settingModel == null || MCStringUtil.isEmpty(UserLoginFragment.this.settingModel.getWapRegisterUrl()))) {
                    localRegist = false;
                }
                if (localRegist) {
                    Intent intent = new Intent(UserLoginFragment.this.activity, UserRegistActivity.class);
                    intent.putExtra(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS, UserLoginFragment.this.goToActivityClass);
                    intent.putExtra(IntentConstant.INTENT_GO_PARAM, UserLoginFragment.this.goParam);
                    UserLoginFragment.this.activity.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(UserLoginFragment.this.activity, WebViewFragmentActivity.class);
                intent.putExtra("webViewUrl", UserLoginFragment.this.settingModel.getWapRegisterUrl());
                UserLoginFragment.this.activity.startActivity(intent);
            }
        });
        this.wechatLoginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                MCPlatformLoginHelper.getInstance().loginFromWeChat(UserLoginFragment.this.getActivity());
            }
        });
        this.qqLoginBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (!MCStringUtil.isEmpty(UserLoginFragment.this.resource.getString("mc_tencent_appid")) && SharedPreferencesDB.getInstance(UserLoginFragment.this.activity.getApplicationContext()).getForumType().equals(FinalConstant.PHPWIND)) {
                    MCPlatformLoginHelper.getInstance().loginFromQQ(UserLoginFragment.this.activity, new LoginCallbackListener() {
                        public void onError() {
                            MCToastUtils.toastByResName(UserLoginFragment.this.activity.getApplicationContext(), "mc_forum_user_wechat_authorization_error");
                        }

                        public void onComplete(String openId) {
                            if (MCStringUtil.isEmpty(openId)) {
                                MCToastUtils.toastByResName(UserLoginFragment.this.activity.getApplicationContext(), "mc_forum_user_wechat_authorization_error");
                            } else {
                                new QQLoginAsyncTask(UserLoginFragment.this.activity.getApplicationContext()).execute(new Void[0]);
                            }
                        }
                    });
                } else if (!SharedPreferencesDB.getInstance(UserLoginFragment.this.activity.getApplicationContext()).getForumType().equals(FinalConstant.PHPWIND)) {
                    Intent intent = new Intent(UserLoginFragment.this.activity, PlatformLoginActivity.class);
                    intent.putExtra("platformId", Long.valueOf(20));
                    UserLoginFragment.this.activity.startActivityForResult(intent, 100);
                }
            }
        });
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                UserLoginFragment.this.nameEdit.setText((CharSequence) UserLoginFragment.this.listAdapter.getList().get(position));
                UserLoginFragment.this.getPwdByName(UserLoginFragment.this.nameEdit.getText().toString());
                UserLoginFragment.this.list.setVisibility(8);
            }
        });
        this.observer = new ActivityObserver() {
            public void loginSuccess() {
                if (UserLoginFragment.this.activity != null) {
                    UserLoginFragment.this.activity.finish();
                }
            }
        };
        this.observerHelper.getActivityObservable().registerObserver(this.observer);
        RegistSuccDelegate.getInstance().setOnRegistSuccListener(new OnRegistSuccListener() {
            public void registSucc() {
                if (UserLoginFragment.this.activity != null) {
                    UserLoginFragment.this.activity.finish();
                }
            }
        });
        this.popupListView.setResource("mc_forum_personal_publish_bg", 40);
        LayoutParams layoutParams = new LayoutParams(MCPhoneUtil.getRawSize(this.activity.getApplicationContext(), 1, 100.0f), -2);
        layoutParams.addRule(11);
        layoutParams.rightMargin = MCPhoneUtil.getRawSize(this.activity.getApplicationContext(), 1, 5.0f);
        this.popupListView.setPopupListViewLayoutParams(layoutParams);
        this.popupListView.init(createMoreList(), new PopupClickListener() {
            public void initTextView(TextView textView) {
                textView.setTextColor(UserLoginFragment.this.getResources().getColorStateList(UserLoginFragment.this.resource.getColorId("mc_forum_bubble_color")));
                textView.setTextSize(14.0f);
            }

            public void click(TextView textView, ImageView imageView, PopupModel popupModel, int position) {
                UserLoginFragment.this.popupListView.setVisibility(8);
                if (popupModel.getId() == -1) {
                    UserLoginFragment.this.activity.startActivity(new Intent(UserLoginFragment.this.activity, UserSettingActivity.class));
                }
            }

            public void hideView() {
            }
        });
    }

    private List<PopupModel> createMoreList() {
        List<PopupModel> list = new ArrayList();
        PopupModel model = new PopupModel();
        model.setName(this.resource.getString("mc_forum_setting"));
        model.setId(-1);
        list.add(model);
        return list;
    }

    private void getAllUser() {
        if (!MCListUtils.isEmpty(this.userInfoList)) {
            this.loginNameList.clear();
            for (UserInfoModel userInfoModel : this.userInfoList) {
                this.loginNameList.add(userInfoModel.getNickname());
            }
            if (this.loginNameList.isEmpty()) {
                this.list.setVisibility(8);
                return;
            }
            this.listAdapter.setList(this.loginNameList);
            this.listAdapter.notifyDataSetChanged();
            this.list.setVisibility(0);
        }
    }

    private void getUser(String name) {
        if (!MCListUtils.isEmpty(this.userInfoList)) {
            List<String> names = new ArrayList();
            for (UserInfoModel userInfoModel : this.userInfoList) {
                if (userInfoModel.getNickname() != null && userInfoModel.getNickname().contains(name)) {
                    names.add(userInfoModel.getNickname());
                }
            }
            if (names.isEmpty()) {
                this.list.setVisibility(8);
            } else {
                this.list.setVisibility(0);
                this.listAdapter.setList(names);
                this.listAdapter.notifyDataSetChanged();
            }
            if (names.size() == 1 && this.userInfoModel != null && ((String) names.get(0)).equals(this.userInfoModel.getNickname())) {
                this.list.setVisibility(8);
            }
        }
    }

    public void getPwdByName(String name) {
        if (this.userInfoList != null) {
            for (UserInfoModel model : this.userInfoList) {
                if (name.equals(model.getNickname())) {
                    this.passwordEdit.setText(model.getPwd());
                    return;
                }
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.getDataTask != null) {
            this.getDataTask.cancel(true);
        }
        if (this.loginTask != null) {
            this.loginTask.cancel(true);
        }
    }

    private void goToTargetActivity() {
        if (this.goToActivityClass != null) {
            Intent intent = new Intent(this.activity, this.goToActivityClass);
            if (this.goParam != null) {
                for (String key : this.goParam.keySet()) {
                    intent.putExtra(key, (Serializable) this.goParam.get(key));
                }
            }
            startActivity(intent);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        List<TopBtnModel> rightModels = new ArrayList();
        topSettingModel.title = this.resource.getString("mc_forum_user_loginbtn");
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button12";
        topBtnModel.action = this.MORE;
        rightModels.add(topBtnModel);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action != UserLoginFragment.this.MORE) {
                    return;
                }
                if (UserLoginFragment.this.popupListView.getVisibility() == 0) {
                    UserLoginFragment.this.popupListView.setVisibility(8);
                } else {
                    UserLoginFragment.this.popupListView.setVisibility(0);
                }
            }
        });
    }

    public boolean isChildInteruptBackPress() {
        if (this.popupListView == null || this.popupListView.onKeyDown()) {
            return super.isChildInteruptBackPress();
        }
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.observerHelper != null) {
            this.observerHelper.getActivityObservable().unregisterObserver(this.observer);
        }
    }
}
