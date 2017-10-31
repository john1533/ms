package com.mobcent.discuz.module.person.activity.fragment;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;

public class UserMyPwdFragment extends BaseFragment {
    private Button backBtn;
    private ChangePwdAsynTask changePwdAsynTask;
    private boolean isShowNewPwd = true;
    private boolean isShowOldPwd = true;
    private EditText newPasswordEdit;
    private EditText oldPasswordEdit;
    private Button savePasswordBtn;
    private ImageButton showNewPwdImgBtn;
    private ImageButton showOldPwdImgBtn;
    private long userId;
    private UserService userService;

    private class ChangePwdAsynTask extends AsyncTask<String, Void, BaseResultModel<UserInfoModel>> {
        private String newPwd;

        private ChangePwdAsynTask() {
        }

        protected void onPreExecute() {
            DZProgressDialogUtils.showProgressDialog(UserMyPwdFragment.this.activity.getApplicationContext(), "mc_forum_warn_change_pwd", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(String... params) {
            if (params[0] == null || params[1] == null) {
                return null;
            }
            String oldPwd = params[0];
            this.newPwd = params[1];
            return UserMyPwdFragment.this.userService.changeUserPwd("password", oldPwd, this.newPwd, UserMyPwdFragment.this.userId);
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserMyPwdFragment.this.activity.getApplicationContext(), result);
            if (result.getRs() != 0) {
                MCToastUtils.toastByResName(UserMyPwdFragment.this.activity.getApplicationContext(), "mc_forum_change_password_succ");
                UserMyPwdFragment.this.activity.finish();
            } else if (!MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toast(UserMyPwdFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected String getRootLayoutName() {
        return "password_setting_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userId = this.sharedPreferencesDB.getUserId();
        this.userService = new UserServiceImpl(this.activity);
    }

    protected void initViews(View rootView) {
        this.newPasswordEdit = (EditText) findViewByName(rootView, "mc_forum_new_password_edit");
        this.oldPasswordEdit = (EditText) findViewByName(rootView, "mc_forum_old_password_edit");
        this.savePasswordBtn = (Button) findViewByName(rootView, "mc_forum_save_btn");
        this.showOldPwdImgBtn = (ImageButton) findViewByName(rootView, "mc_forum_old_password_imgBtn");
        this.showNewPwdImgBtn = (ImageButton) findViewByName(rootView, "mc_forum_new_password_imgBtn");
    }

    protected void initActions(View rootView) {
        this.showOldPwdImgBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (UserMyPwdFragment.this.isShowOldPwd) {
                    UserMyPwdFragment.this.showOldPwdImgBtn.setBackgroundResource(UserMyPwdFragment.this.resource.getDrawableId("mc_forum_password_visible"));
                    UserMyPwdFragment.this.oldPasswordEdit.setInputType(144);
                    Editable etable = UserMyPwdFragment.this.oldPasswordEdit.getText();
                    Selection.setSelection(etable, etable.length());
                    UserMyPwdFragment.this.isShowOldPwd = false;
                    return;
                }
                UserMyPwdFragment.this.showOldPwdImgBtn.setBackgroundResource(UserMyPwdFragment.this.resource.getDrawableId("mc_forum_password_invisible"));
                UserMyPwdFragment.this.oldPasswordEdit.setInputType(129);
                Editable etable = UserMyPwdFragment.this.oldPasswordEdit.getText();
                Selection.setSelection(etable, etable.length());
                UserMyPwdFragment.this.isShowOldPwd = true;
            }
        });
        this.showNewPwdImgBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (UserMyPwdFragment.this.isShowNewPwd) {
                    UserMyPwdFragment.this.showNewPwdImgBtn.setBackgroundDrawable(UserMyPwdFragment.this.getResources().getDrawable(UserMyPwdFragment.this.resource.getDrawableId("mc_forum_password_visible")));
                    UserMyPwdFragment.this.newPasswordEdit.setInputType(144);
                    Editable etable = UserMyPwdFragment.this.newPasswordEdit.getText();
                    Selection.setSelection(etable, etable.length());
                    UserMyPwdFragment.this.isShowNewPwd = false;
                    return;
                }
                UserMyPwdFragment.this.showNewPwdImgBtn.setBackgroundDrawable(UserMyPwdFragment.this.getResources().getDrawable(UserMyPwdFragment.this.resource.getDrawableId("mc_forum_password_invisible")));
                UserMyPwdFragment.this.newPasswordEdit.setInputType(129);
                Editable etable = UserMyPwdFragment.this.newPasswordEdit.getText();
                Selection.setSelection(etable, etable.length());
                UserMyPwdFragment.this.isShowNewPwd = true;
            }
        });
        this.savePasswordBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String oldPassStr = UserMyPwdFragment.this.oldPasswordEdit.getText().toString();
                String newPassStr = UserMyPwdFragment.this.newPasswordEdit.getText().toString();
                if (MCStringUtil.isEmpty(oldPassStr) || MCStringUtil.isEmpty(newPassStr)) {
                    MCToastUtils.toastByResName(UserMyPwdFragment.this.activity.getApplicationContext(), "mc_forum_parssword_not_null");
                    return;
                }
                if (UserMyPwdFragment.this.changePwdAsynTask != null) {
                    UserMyPwdFragment.this.changePwdAsynTask.cancel(true);
                }
                UserMyPwdFragment.this.changePwdAsynTask = new ChangePwdAsynTask();
                UserMyPwdFragment.this.changePwdAsynTask.execute(new String[]{oldPassStr, newPassStr});
            }
        });
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        topSettingModel.title = this.resource.getString("mc_forum_password_setting");
        dealTopBar(topSettingModel);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.changePwdAsynTask != null && this.changePwdAsynTask.getStatus() != Status.FINISHED) {
            this.changePwdAsynTask.cancel(true);
        }
    }
}
