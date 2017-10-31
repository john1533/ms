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
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.fragment.adapter.RegLoginListAdapter;
import com.mobcent.discuz.module.person.activity.helper.EmailConstantHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;

public class AuthorizationSuccFragment extends BaseFragment implements FinalConstant {
    private String autoToken;
    private LinearLayout femaleBox;
    private Button femaleRadio;
    private LinearLayout maleBox;
    private Button maleRadio;
    private String nickName;
    private EditText nickNameEdit;
    private int nickNameMaxLen = 20;
    private int nickNameMinLen = 3;
    private String openId;
    private String platformId;
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
    private UserInfoModel userInfoModel;

    class UpdateAsyncTask extends AsyncTask<UserInfoModel, Void, BaseResultModel<UserInfoModel>> {
        UpdateAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(AuthorizationSuccFragment.this.activity.getApplicationContext(), "mc_forum_warn_register", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(UserInfoModel... params) {
            UserService userService = new UserServiceImpl(AuthorizationSuccFragment.this.activity.getApplicationContext());
            UserInfoModel model = params[0];
            return userService.saveWebRegisteUser(model.getNickname(), model, AuthorizationSuccFragment.this.platformId);
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            DZProgressDialogUtils.hideProgressDialog();
            if (result.getRs() != 0) {
                MCToastUtils.toastByResName(AuthorizationSuccFragment.this.activity, "mc_forum_user_register_succ");
                AuthorizationSuccFragment.this.activity.setResult(200);
                AuthorizationSuccFragment.this.sharedPreferencesDB.setNickName(AuthorizationSuccFragment.this.nickNameEdit.getText().toString());
                AuthorizationSuccFragment.this.activity.finish();
                LocationHelper.startLocation(AuthorizationSuccFragment.this.activity);
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(AuthorizationSuccFragment.this.activity, "mc_forum_user_register_fail");
            } else {
                MCToastUtils.toast(AuthorizationSuccFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userInfoModel = (UserInfoModel) getBundle().getSerializable(IntentConstant.INTENT_USER_INFO);
        this.platformId = getBundle().getString("platformId");
        this.userEmail = this.userInfoModel.getEmail();
        this.userGender = this.userInfoModel.getGender();
        this.nickName = this.userInfoModel.getNickname();
        this.openId = this.userInfoModel.getOpenId();
        this.autoToken = this.userInfoModel.getAutoToken();
        this.regLoginListAdapter = new RegLoginListAdapter(this.activity, new ArrayList());
    }

    protected String getRootLayoutName() {
        return "authorization_succ_fragment";
    }

    protected void initViews(View rootView) {
        this.saveInfoSubmitBtn = (Button) findViewByName(rootView, "mc_forum_user_save_info_submit_btn");
        this.userEmailText = (EditText) findViewByName(rootView, "mc_forum_user_input_user_email_edit");
        this.nickNameEdit = (EditText) findViewByName(rootView, "mc_forum_user_input_username_btn");
        this.maleRadio = (Button) findViewByName(rootView, "mc_forum_gender_male_btn");
        this.femaleRadio = (Button) findViewByName(rootView, "mc_forum_gender_female_btn");
        this.secrecyRadio = (Button) findViewByName(rootView, "mc_forum_gender_secrecy_btn");
        this.maleBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_male_box");
        this.femaleBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_female_box");
        this.secrecyBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_secrecy_box");
        this.regEmailBox = (RelativeLayout) findViewByName(rootView, "mc_forum_user_register_email_box");
        this.regEmailList = (ListView) findViewByName(rootView, "mc_forum_user_register_email_list");
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
                Editable editable = AuthorizationSuccFragment.this.userEmailText.getText();
                if (editable.length() > 0) {
                    AuthorizationSuccFragment.this.regEmailBox.setVisibility(0);
                    AuthorizationSuccFragment.this.notifyData(editable.toString());
                    return;
                }
                AuthorizationSuccFragment.this.regEmailBox.setVisibility(8);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.regEmailList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AuthorizationSuccFragment.this.userEmailText.setText((CharSequence) AuthorizationSuccFragment.this.regLoginListAdapter.getList().get(position));
                Editable regEmail = AuthorizationSuccFragment.this.userEmailText.getText();
                Selection.setSelection(regEmail, regEmail.length());
                AuthorizationSuccFragment.this.regEmailBox.setVisibility(8);
            }
        });
        this.saveInfoSubmitBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AuthorizationSuccFragment.this.nickName = AuthorizationSuccFragment.this.nickNameEdit.getText().toString().trim();
                AuthorizationSuccFragment.this.userEmail = AuthorizationSuccFragment.this.userEmailText.getText().toString().trim();
                if (MCStringUtil.checkNickNameMinLen(AuthorizationSuccFragment.this.nickName, AuthorizationSuccFragment.this.nickNameMinLen) && MCStringUtil.checkNickNameMaxLen(AuthorizationSuccFragment.this.nickName, AuthorizationSuccFragment.this.nickNameMaxLen)) {
                    AuthorizationSuccFragment.this.userInfoModel.setNickname(AuthorizationSuccFragment.this.nickName);
                    AuthorizationSuccFragment.this.userInfoModel.setGender(AuthorizationSuccFragment.this.userGender);
                    AuthorizationSuccFragment.this.userInfoModel.setEmail(AuthorizationSuccFragment.this.userEmail);
                    AuthorizationSuccFragment.this.userInfoModel.setOpenId(AuthorizationSuccFragment.this.openId);
                    AuthorizationSuccFragment.this.userInfoModel.setAutoToken(AuthorizationSuccFragment.this.autoToken);
                    AuthorizationSuccFragment.this.userInfoModel.setPlatformUserId(AuthorizationSuccFragment.this.platformId);
                    if (AuthorizationSuccFragment.this.updateAsyncTask != null) {
                        AuthorizationSuccFragment.this.updateAsyncTask.cancel(true);
                    }
                    AuthorizationSuccFragment.this.updateAsyncTask = new UpdateAsyncTask();
                    AuthorizationSuccFragment.this.updateAsyncTask.execute(new UserInfoModel[]{AuthorizationSuccFragment.this.userInfoModel});
                    return;
                }
                MCToastUtils.toastByResName(AuthorizationSuccFragment.this.activity, "mc_forum_user_nickname_length_error", 1);
            }
        });
        this.maleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AuthorizationSuccFragment.this.userGender = 1;
                AuthorizationSuccFragment.this.maleRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
                AuthorizationSuccFragment.this.femaleRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
                AuthorizationSuccFragment.this.secrecyRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            }
        });
        this.femaleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AuthorizationSuccFragment.this.userGender = 2;
                AuthorizationSuccFragment.this.femaleRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
                AuthorizationSuccFragment.this.maleRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
                AuthorizationSuccFragment.this.secrecyRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            }
        });
        this.secrecyBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AuthorizationSuccFragment.this.userGender = 0;
                AuthorizationSuccFragment.this.secrecyRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h")));
                AuthorizationSuccFragment.this.maleRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
                AuthorizationSuccFragment.this.femaleRadio.setBackgroundDrawable(AuthorizationSuccFragment.this.getResources().getDrawable(AuthorizationSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n")));
            }
        });
        this.nickNameEdit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    ((InputMethodManager) AuthorizationSuccFragment.this.activity.getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(AuthorizationSuccFragment.this.nickNameEdit.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void notifyData(String str) {
        ArrayList<String> emailSelections = new ArrayList();
        this.regLoginListAdapter.setList(EmailConstantHelper.getInstance().convertEmail(str));
        this.regLoginListAdapter.notifyDataSetChanged();
        this.regLoginListAdapter.notifyDataSetInvalidated();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.updateAsyncTask != null) {
            this.updateAsyncTask.cancel(true);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_user_register_succ_warn");
        dealTopBar(topSettingModel);
    }
}
