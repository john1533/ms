package com.mobcent.discuz.module.person.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.fragment.adapter.RegLoginListAdapter;
import com.mobcent.discuz.module.person.activity.helper.EmailConstantHelper;
import com.mobcent.login.android.helper.MCPlatformLoginHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;

public class UserRegisterBoundFragment extends BaseFragment implements FinalConstant {
    private LinearLayout femaleBox;
    private Button femaleRadio;
    private LinearLayout maleBox;
    private Button maleRadio;
    private String nickName;
    private EditText nickNameEdit;
    private EditText pwdEdit;
    private RelativeLayout regEmailBox;
    private ListView regEmailList;
    private RegLoginListAdapter regLoginListAdapter;
    private Button saveInfoSubmitBtn;
    private LinearLayout secrecyBox;
    private Button secrecyRadio;
    private UpdateAsyncTask updateAsyncTask;
    private String userEmail;
    private EditText userEmailText;
    private int userGender;

    class UpdateAsyncTask extends AsyncTask<UserInfoModel, Void, BaseResultModel<UserInfoModel>> {
        UpdateAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(UserRegisterBoundFragment.this.activity.getApplicationContext(), "mc_forum_warn_register", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(UserInfoModel... params) {
            return new UserServiceImpl(UserRegisterBoundFragment.this.activity.getApplicationContext()).saveUserPlatforminfo(params[0], UserConstant.REGISTER, MCPlatformLoginHelper.getInstance().loginInfoModel.getPlatformType());
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            DZProgressDialogUtils.hideProgressDialog();
            if (result != null && result.getRs() != 0) {
                MCToastUtils.toastByResName(UserRegisterBoundFragment.this.activity, "mc_forum_user_register_succ");
//                LocationHelper.startLocation(UserRegisterBoundFragment.this.activity);
                ObserverHelper.getInstance().getActivityObservable().loginSuccess();
                UserRegisterBoundFragment.this.activity.finish();
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(UserRegisterBoundFragment.this.activity, "mc_forum_user_register_fail");
            } else {
                MCToastUtils.toast(UserRegisterBoundFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }

    protected String getRootLayoutName() {
        return "user_register_bound_fragment";
    }

    protected void initViews(View rootView) {
        this.saveInfoSubmitBtn = (Button) findViewByName(rootView, "mc_forum_user_save_info_submit_btn");
        this.userEmailText = (EditText) findViewByName(rootView, "mc_forum_user_input_user_email_edit");
        this.nickNameEdit = (EditText) findViewByName(rootView, "mc_forum_user_input_username_btn");
        this.pwdEdit = (EditText) findViewByName(rootView, "mc_forum_user_input_pwd_btn");
        this.maleRadio = (Button) findViewByName(rootView, "mc_forum_gender_male_btn");
        this.femaleRadio = (Button) findViewByName(rootView, "mc_forum_gender_female_btn");
        this.secrecyRadio = (Button) findViewByName(rootView, "mc_forum_gender_secrecy_btn");
        this.maleBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_male_box");
        this.femaleBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_female_box");
        this.secrecyBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_secrecy_box");
        this.regEmailBox = (RelativeLayout) findViewByName(rootView, "mc_forum_user_register_email_box");
        this.regEmailList = (ListView) findViewByName(rootView, "mc_forum_user_register_email_list");
        this.regLoginListAdapter = new RegLoginListAdapter(this.activity, new ArrayList());
        this.regEmailList.setAdapter(this.regLoginListAdapter);
        if (!MCStringUtil.isEmpty(this.userEmail)) {
            this.userEmailText.setText(this.userEmail);
            Editable emailEdit = this.userEmailText.getText();
            Selection.setSelection(emailEdit, emailEdit.length());
        }
        if (!MCStringUtil.isEmpty(this.nickName)) {
            this.nickNameEdit.setText(this.nickName);
            Editable nickEdit = this.nickNameEdit.getText();
            Selection.setSelection(nickEdit, nickEdit.length());
        }
        if (this.userGender == 0) {
            this.secrecyRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
            this.maleRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            this.femaleRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
        } else if (this.userGender == 1) {
            this.maleRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
            this.femaleRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            this.secrecyRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
        } else {
            this.femaleRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
            this.maleRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            this.secrecyRadio.setBackgroundDrawable(getResources().getDrawable(this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
        }
    }

    protected void initActions(View rootView) {
        this.userEmailText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = UserRegisterBoundFragment.this.userEmailText.getText();
                if (editable.length() > 0) {
                    UserRegisterBoundFragment.this.regEmailBox.setVisibility(0);
                    UserRegisterBoundFragment.this.notifyData(editable.toString());
                    return;
                }
                UserRegisterBoundFragment.this.regEmailBox.setVisibility(8);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.regEmailList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserRegisterBoundFragment.this.userEmailText.setText((CharSequence) UserRegisterBoundFragment.this.regLoginListAdapter.getList().get(position));
                Editable regEmail = UserRegisterBoundFragment.this.userEmailText.getText();
                Selection.setSelection(regEmail, regEmail.length());
                UserRegisterBoundFragment.this.regEmailBox.setVisibility(8);
            }
        });
        this.saveInfoSubmitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String userName = UserRegisterBoundFragment.this.nickNameEdit.getText().toString();
                String email = UserRegisterBoundFragment.this.userEmailText.getText().toString();
                String password = UserRegisterBoundFragment.this.pwdEdit.getText().toString();
                if (MCStringUtil.isEmpty(userName)) {
                    MCToastUtils.toastByResName(UserRegisterBoundFragment.this.activity.getApplicationContext(), "mc_forum_user_nickname_not_null");
                } else if (MCStringUtil.isEmpty(password)) {
                    MCToastUtils.toastByResName(UserRegisterBoundFragment.this.activity.getApplicationContext(), "mc_forum_user_password_not_null");
                } else if (MCStringUtil.isEmpty(email)) {
                    MCToastUtils.toastByResName(UserRegisterBoundFragment.this.activity.getApplicationContext(), "mc_forum_user_email_not_null");
                } else {
                    UserInfoModel userInfoModel = new UserInfoModel();
                    userInfoModel.setNickname(userName);
                    userInfoModel.setGender(UserRegisterBoundFragment.this.userGender);
                    userInfoModel.setEmail(email);
                    userInfoModel.setPwd(password);
                    userInfoModel.setOpenId(MCPlatformLoginHelper.getInstance().loginInfoModel.getOpenid());
                    userInfoModel.setToken(MCPlatformLoginHelper.getInstance().loginInfoModel.getAccessToken());
                    userInfoModel.setPlatformUserId(MCPlatformLoginHelper.getInstance().loginInfoModel.getPlatformType());
                    if (UserRegisterBoundFragment.this.updateAsyncTask != null) {
                        UserRegisterBoundFragment.this.updateAsyncTask.cancel(true);
                    }
                    UserRegisterBoundFragment.this.updateAsyncTask = new UpdateAsyncTask();
                    UserRegisterBoundFragment.this.updateAsyncTask.execute(new UserInfoModel[]{userInfoModel});
                }
            }
        });
        this.maleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegisterBoundFragment.this.userGender = 1;
                UserRegisterBoundFragment.this.maleRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
                UserRegisterBoundFragment.this.femaleRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
                UserRegisterBoundFragment.this.secrecyRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            }
        });
        this.femaleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegisterBoundFragment.this.userGender = 2;
                UserRegisterBoundFragment.this.femaleRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
                UserRegisterBoundFragment.this.maleRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
                UserRegisterBoundFragment.this.secrecyRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            }
        });
        this.secrecyBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegisterBoundFragment.this.userGender = 0;
                UserRegisterBoundFragment.this.secrecyRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
                UserRegisterBoundFragment.this.maleRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
                UserRegisterBoundFragment.this.femaleRadio.setBackgroundDrawable(UserRegisterBoundFragment.this.getResources().getDrawable(UserRegisterBoundFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            }
        });
        this.nickNameEdit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    ((InputMethodManager) UserRegisterBoundFragment.this.activity.getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(UserRegisterBoundFragment.this.nickNameEdit.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void notifyData(String str) {
        ArrayList<String> emailSelections = new ArrayList();
        this.regLoginListAdapter.setList(EmailConstantHelper.getInstance().convertEmail(str));
        this.regLoginListAdapter.notifyDataSetChanged();
        this.regEmailList.setSelection(0);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.updateAsyncTask != null) {
            this.updateAsyncTask.cancel(true);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_user_register");
        dealTopBar(topSettingModel);
    }
}
