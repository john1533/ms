package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.UserRegistSuccActivity;
import com.mobcent.discuz.module.person.activity.delegate.RegistSuccDelegate;
import com.mobcent.discuz.module.person.activity.delegate.RegistSuccDelegate.OnRegistSuccListener;
import com.mobcent.discuz.module.person.activity.fragment.adapter.RegLoginListAdapter;
import com.mobcent.discuz.module.person.activity.helper.EmailConstantHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UserRegistFragment extends BaseFragment implements IntentConstant {
    private EditText emailEdit;
    private HashMap<String, Serializable> goParam;
    private Class<?> goToActivityClass;
    private boolean isShowPwd = false;
    private ListView list;
    private RegLoginListAdapter listAdapter;
    private EditText passwordEdit;
    private RegAsyncTask regAsyncTask;
    private Button regSubmitBtn;
    private RelativeLayout registerPanle;
    private ImageButton showPwdBtn;
    private EditText userNameEdit;
    private UserService userService;

    private class RegAsyncTask extends AsyncTask<Void, Void, BaseResultModel<UserInfoModel>> {
        private String email;
        private String password;
        private String userName;

        public RegAsyncTask(String userName, String password, String email) {
            this.userName = userName;
            this.password = password;
            this.email = email;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(UserRegistFragment.this.activity, "mc_forum_warn_login", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(Void... arg0) {
            return UserRegistFragment.this.userService.registUser(this.userName, this.password, this.email);
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserRegistFragment.this.activity.getApplicationContext(), result);
            if (result.getRs() != 0) {
                MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_register_succ");
                OnRegistSuccListener listener = RegistSuccDelegate.getInstance().getOnRegistSuccListener();
                if (listener != null) {
                    listener.registSucc();
                }
                Intent intent = new Intent(UserRegistFragment.this.activity, UserRegistSuccActivity.class);
                intent.putExtra(IntentConstant.INTENT_USER_INFO, (Serializable) result.getData());
                if (UserRegistFragment.this.goToActivityClass != null) {
                    intent.putExtra(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS, UserRegistFragment.this.goToActivityClass);
                    intent.putExtra(IntentConstant.INTENT_GO_PARAM, UserRegistFragment.this.goParam);
                }
                UserRegistFragment.this.activity.startActivity(intent);
//                LocationHelper.startLocation(UserRegistFragment.this.activity);
                if (UserRegistFragment.this.activity != null) {
                    UserRegistFragment.this.activity.finish();
                }
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_regist_fail");
            } else {
                MCToastUtils.toast(UserRegistFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected String getRootLayoutName() {
        return "user_regist_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "UserRegistFragment";
        this.goToActivityClass = (Class) getBundle().getSerializable(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS);
        this.goParam = (HashMap) getBundle().getSerializable(IntentConstant.INTENT_GO_PARAM);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
    }

    protected void initViews(View rootView) {
        this.userNameEdit = (EditText) findViewByName(rootView, "user_register_name_edit");
        this.passwordEdit = (EditText) findViewByName(rootView, "user_register_password_edit");
        this.emailEdit = (EditText) findViewByName(rootView, "user_register_email_edit");
        this.showPwdBtn = (ImageButton) findViewByName(rootView, "user_show_password_btn");
        this.regSubmitBtn = (Button) findViewByName(rootView, "user_register_submit_btn");
        this.list = (ListView) findViewByName(rootView, "user_register_email_list");
        this.registerPanle = (RelativeLayout) findViewByName(rootView, "user_register_panle");
        if (this.listAdapter == null) {
            this.listAdapter = new RegLoginListAdapter(this.activity, new ArrayList());
        }
        this.list.setAdapter(this.listAdapter);
    }

    protected void initActions(View rootView) {
        this.showPwdBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (UserRegistFragment.this.isShowPwd) {
                    UserRegistFragment.this.showPwdBtn.setBackgroundResource(UserRegistFragment.this.resource.getDrawableId("mc_forum_password_invisible"));
                    UserRegistFragment.this.passwordEdit.setInputType(129);
                } else {
                    UserRegistFragment.this.showPwdBtn.setBackgroundResource(UserRegistFragment.this.resource.getDrawableId("mc_forum_password_visible"));
                    UserRegistFragment.this.passwordEdit.setInputType(144);
                }
                UserRegistFragment.this.isShowPwd = !UserRegistFragment.this.isShowPwd;
                Editable etable = UserRegistFragment.this.passwordEdit.getText();
                Selection.setSelection(etable, etable.length());
            }
        });
        this.passwordEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistFragment.this.list.setVisibility(8);
            }
        });
        this.userNameEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistFragment.this.list.setVisibility(8);
            }
        });
        this.emailEdit.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String loginEmail = UserRegistFragment.this.emailEdit.getText().toString();
                int len = loginEmail.length();
                if (len > 0) {
                    UserRegistFragment.this.list.setVisibility(0);
                    UserRegistFragment.this.notifyEmailData(loginEmail);
                } else if (len == 0) {
                    UserRegistFragment.this.list.setVisibility(8);
                }
                ((InputMethodManager) UserRegistFragment.this.activity.getSystemService("input_method")).showSoftInput(UserRegistFragment.this.emailEdit, 2);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserRegistFragment.this.emailEdit.setText((CharSequence) UserRegistFragment.this.listAdapter.getList().get(position));
                Editable regEmail = UserRegistFragment.this.emailEdit.getText();
                Selection.setSelection(regEmail, regEmail.length());
                UserRegistFragment.this.list.setVisibility(8);
            }
        });
        this.registerPanle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistFragment.this.list.setVisibility(8);
            }
        });
        this.regSubmitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String userName = UserRegistFragment.this.userNameEdit.getText().toString();
                String email = UserRegistFragment.this.emailEdit.getText().toString();
                String password = UserRegistFragment.this.passwordEdit.getText().toString();
                if (MCStringUtil.isEmpty(userName)) {
                    MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_nickname_not_null");
                } else if (MCStringUtil.isEmpty(email)) {
                    MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_email_not_null");
                } else if (MCStringUtil.isEmpty(password)) {
                    MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_password_not_null");
                } else if (userName.length() < 3 || userName.length() > 15) {
                    MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_nickname_length_error");
                } else if (email.length() > 64) {
                    MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_email_length_error_warn");
                } else if (MCStringUtil.isEmail(email)) {
                    if (UserRegistFragment.this.regAsyncTask != null) {
                        UserRegistFragment.this.regAsyncTask.cancel(true);
                    }
                    UserRegistFragment.this.regAsyncTask = new RegAsyncTask(userName, password, email);
                    UserRegistFragment.this.regAsyncTask.execute(new Void[0]);
                } else {
                    MCToastUtils.toastByResName(UserRegistFragment.this.activity.getApplicationContext(), "mc_forum_user_email_format_error_warn");
                }
            }
        });
    }

    private void notifyEmailData(String email) {
        ArrayList<String> emailSelections = new ArrayList();
        this.listAdapter.setList(EmailConstantHelper.getInstance().convertEmail(email));
        this.listAdapter.notifyDataSetChanged();
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.regAsyncTask != null) {
            this.regAsyncTask.cancel(true);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_user_register_succ_title");
        dealTopBar(topSettingModel);
    }
}
