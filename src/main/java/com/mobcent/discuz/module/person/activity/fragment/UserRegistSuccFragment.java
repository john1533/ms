package com.mobcent.discuz.module.person.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.FinishListener;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserRegistSuccFragment extends BaseFragment {
    private LinearLayout femaleBox;
    private Button femaleBtn;
    private TextView femaleText;
    private int gender = 0;
    private HashMap<String, Serializable> goParam;
    private Class<?> goToActivityClass;
    private String iconLocalPath;
    private Button laterBtn;
    private Button localPhotoBtn;
    private LinearLayout maleBox;
    private Button maleBtn;
    private TextView maleText;
    private PhotoManageHelper photoManageHelper;
    private Button saveBtn;
    private SaveAsyncTask saveTask;
    private LinearLayout secrecyBox;
    private Button secrecyBtn;
    private TextView secrecyText;
    private Button takePhotoBtn;
    private ImageView userIconImg;
    private UserInfoModel userInfoModel;
    private TextView userNameText;
    private UserService userService;

    private class SaveAsyncTask extends AsyncTask<Void, Void, BaseResultModel<Object>> {
        private SaveAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(UserRegistSuccFragment.this.activity, "mc_forum_user_perfect_ing", this);
        }

        protected BaseResultModel<Object> doInBackground(Void... arg0) {
            String iconUrl;
            BaseResultModel<Object> baseResultModel = new BaseResultModel();
            baseResultModel.setRs(0);
            boolean isModifyIcon = false;
            if (MCStringUtil.isEmpty(UserRegistSuccFragment.this.iconLocalPath)) {
                iconUrl = "";
            } else {
                isModifyIcon = true;
                BaseResultModel<UploadPictureModel> uploadPicResultModel = UserRegistSuccFragment.this.userService.uploadIcon(UserRegistSuccFragment.this.iconLocalPath.replace(" ", ""), UserRegistSuccFragment.this.sharedPreferencesDB.getUserId());
                if (uploadPicResultModel == null || uploadPicResultModel.getRs() != 1 || uploadPicResultModel.getData() == null) {
                    baseResultModel.setErrorInfo(uploadPicResultModel.getErrorInfo());
                    return baseResultModel;
                }
                iconUrl = ((UploadPictureModel) uploadPicResultModel.getData()).getPicPath();
            }
            return UserRegistSuccFragment.this.userService.updateUser(iconUrl, UserRegistSuccFragment.this.gender, "info", isModifyIcon);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            super.onPostExecute(result);
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserRegistSuccFragment.this.getAppApplication(), result);
            if (result.getRs() != 0) {
                if (result.getAlert() == 0) {
                    MCToastUtils.toastByResName(UserRegistSuccFragment.this.activity.getApplicationContext(), "mc_forum_user_perfect_succ");
                }
                UserRegistSuccFragment.this.goToTargetActivity();
                if (UserRegistSuccFragment.this.activity != null) {
                    UserRegistSuccFragment.this.activity.finish();
                }
            } else if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toastByResName(UserRegistSuccFragment.this.activity.getApplicationContext(), "mc_forum_user_perfect_fail");
            } else {
                MCToastUtils.toast(UserRegistSuccFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "UserRegistSuccFragment";
        this.goToActivityClass = (Class) getBundle().getSerializable(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS);
        this.goParam = (HashMap) getBundle().getSerializable(IntentConstant.INTENT_GO_PARAM);
        this.userInfoModel = (UserInfoModel) getBundle().getSerializable(IntentConstant.INTENT_USER_INFO);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.photoManageHelper = new PhotoManageHelper(this.activity.getApplicationContext());
    }

    protected String getRootLayoutName() {
        return "user_regist_succ_fragment";
    }

    protected void initViews(View rootView) {
        this.userNameText = (TextView) findViewByName(rootView, "username_text");
        if (this.userInfoModel != null) {
            this.userNameText.setText(this.userInfoModel.getNickname());
        }
        this.userIconImg = (ImageView) findViewByName(rootView, "user_icon_img");
        this.takePhotoBtn = (Button) findViewByName(rootView, "take_photo_btn");
        this.localPhotoBtn = (Button) findViewByName(rootView, "local_photo_btn");
        this.secrecyBox = (LinearLayout) findViewByName(rootView, "gender_secrecy_box");
        this.maleBox = (LinearLayout) findViewByName(rootView, "gender_male_box");
        this.femaleBox = (LinearLayout) findViewByName(rootView, "gender_female_box");
        this.secrecyBtn = (Button) findViewByName(rootView, "gender_secrecy_btn");
        this.maleBtn = (Button) findViewByName(rootView, "gender_male_btn");
        this.femaleBtn = (Button) findViewByName(rootView, "gender_female_btn");
        this.secrecyText = (TextView) findViewByName(rootView, "gender_secrecy_text");
        this.maleText = (TextView) findViewByName(rootView, "gender_male_text");
        this.femaleText = (TextView) findViewByName(rootView, "gender_female_text");
        this.saveBtn = (Button) findViewByName(rootView, "save_info_submit_btn");
        this.laterBtn = (Button) findViewByName(rootView, "later_submit_btn");
    }

    @SuppressLint({"NewApi"})
    protected void initActions(View rootView) {
        this.secrecyBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.gender = 0;
                UserRegistSuccFragment.this.secrecyBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
                UserRegistSuccFragment.this.maleBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserRegistSuccFragment.this.femaleBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            }
        });
        this.secrecyText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.secrecyBtn.performClick();
            }
        });
        this.secrecyBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistSuccFragment.this.secrecyBtn.performClick();
            }
        });
        this.maleBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.gender = 1;
                UserRegistSuccFragment.this.secrecyBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserRegistSuccFragment.this.maleBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
                UserRegistSuccFragment.this.femaleBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            }
        });
        this.maleText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.maleBtn.performClick();
            }
        });
        this.maleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistSuccFragment.this.maleBtn.performClick();
            }
        });
        this.femaleBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.gender = 2;
                UserRegistSuccFragment.this.secrecyBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserRegistSuccFragment.this.maleBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserRegistSuccFragment.this.femaleBtn.setBackgroundResource(UserRegistSuccFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
            }
        });
        this.femaleText.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.femaleBtn.performClick();
            }
        });
        this.femaleBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistSuccFragment.this.femaleBtn.performClick();
            }
        });
        this.saveBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (UserRegistSuccFragment.this.saveTask != null) {
                    UserRegistSuccFragment.this.saveTask.cancel(true);
                }
                UserRegistSuccFragment.this.saveTask = new SaveAsyncTask();
                UserRegistSuccFragment.this.saveTask.execute(new Void[0]);
            }
        });
        this.laterBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                UserRegistSuccFragment.this.goToTargetActivity();
                if (UserRegistSuccFragment.this.activity != null) {
                    UserRegistSuccFragment.this.activity.finish();
                }
            }
        });
        this.photoManageHelper.registListener(new FinishListener() {
            public void photoFinish(int type, Map<String, PictureModel> map) {
            }

            public void cameraFinish(int type, Map<String, PictureModel> map, String outputPath, Bitmap bitmap) {
                UserRegistSuccFragment.this.userIconImg.setImageBitmap(bitmap);
                UserRegistSuccFragment.this.iconLocalPath = outputPath;
            }
        });
        this.takePhotoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistSuccFragment.this.photoManageHelper.openPhotoGraph(UserRegistSuccFragment.this.activity, 1);
            }
        });
        this.localPhotoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserRegistSuccFragment.this.photoManageHelper.openPhotoSelector(UserRegistSuccFragment.this.activity, 1);
            }
        });
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.photoManageHelper.onActivityResult(this.activity, requestCode, resultCode, data);
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.saveTask != null) {
            this.saveTask.cancel(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.photoManageHelper.onDestroy();
        if (this.userInfoModel != null) {
            new Thread() {
                public void run() {
                    UserRegistSuccFragment.this.userService.getUserInfo(UserRegistSuccFragment.this.userInfoModel.getUserId());
                }
            }.start();
        }
    }

    public boolean isChildInteruptBackPress() {
        this.laterBtn.performClick();
        return false;
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_user_register");
        dealTopBar(topSettingModel);
    }
}
