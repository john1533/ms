package com.mobcent.discuz.module.person.activity.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper;
import com.mobcent.discuz.activity.widget.album.PhotoManageHelper.FinishListener;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.service.impl.FileTransferServiceImpl;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class UserMyInfoFragment extends BaseFragment implements FinalConstant {
    private int TOPBAR_BACK = 1;
    private TextView emailText;
    private Builder exitAlertDialog;
    private LinearLayout femaleBox;
    private Button femaleRadio;
    private TextView femaleText;
    private int gender = -1;
    private MCHeadIcon headerImg;
    private String iconLocalPath = "";
    private LoadAsyncTask loadAsyncTask;
    private Button localPhotoBtn;
    private LinearLayout maleBox;
    private Button maleRadio;
    private TextView maleText;
    private TextView nicknameEdit;
    private PhotoManageHelper photoManageHelper;
    private Builder reloadDialog;
    private Button saveBtn;
    private LinearLayout secrecyBox;
    private Button secrecyRadio;
    private TextView secrecyText;
    private Button takePhotoBtn;
    private UpdateAsyncTask updateAsyncTask;
    private UserInfoModel userInfoModel;
    private UserService userService;

    public class LoadAsyncTask extends AsyncTask<String, Void, BaseResultModel<UserInfoModel>> {
        protected void onPreExecute() {
            super.onPreExecute();
            DZProgressDialogUtils.showProgressDialog(UserMyInfoFragment.this.activity.getApplicationContext(), "mc_forum_warn_load", this);
        }

        protected BaseResultModel<UserInfoModel> doInBackground(String... params) {
            return UserMyInfoFragment.this.userService.getUser();
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserMyInfoFragment.this.activity, result);
            if (result.getRs() == 0) {
                UserMyInfoFragment.this.reloadDialog.show();
            } else if (result.getData() == null) {
                UserMyInfoFragment.this.reloadDialog.show();
            } else {
                UserMyInfoFragment.this.userInfoModel = (UserInfoModel) result.getData();
                UserMyInfoFragment.this.updateView(UserMyInfoFragment.this.userInfoModel);
            }
        }
    }

    private class UpdateAsyncTask extends AsyncTask<UserInfoModel, Void, BaseResultModel<Object>> {
        private boolean isModifyIcon;

        private UpdateAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.isModifyIcon = false;
            DZProgressDialogUtils.showProgressDialog(UserMyInfoFragment.this.activity.getApplicationContext(), "mc_forum_warn_update", this);
        }

        protected BaseResultModel<Object> doInBackground(UserInfoModel... params) {
            BaseResultModel<Object> baseResultModel = new BaseResultModel();
            baseResultModel.setRs(0);
            String iconUrl = null;
            if (!MCStringUtil.isEmpty(UserMyInfoFragment.this.iconLocalPath)) {
                this.isModifyIcon = true;
                BaseResultModel<UploadPictureModel> uploadPicResultModel = UserMyInfoFragment.this.userService.uploadIcon(UserMyInfoFragment.this.iconLocalPath.replace(" ", ""), UserMyInfoFragment.this.sharedPreferencesDB.getUserId());
                if (uploadPicResultModel == null || uploadPicResultModel.getRs() != 1 || uploadPicResultModel.getData() == null) {
                    baseResultModel.setErrorInfo(uploadPicResultModel.getErrorInfo());
                    return baseResultModel;
                }
                iconUrl = ((UploadPictureModel) uploadPicResultModel.getData()).getPicPath();
            } else if (UserMyInfoFragment.this.userInfoModel != null) {
                iconUrl = UserMyInfoFragment.this.userInfoModel.getIcon();
            }
            return UserMyInfoFragment.this.userService.updateUser(iconUrl, UserMyInfoFragment.this.gender, "info", this.isModifyIcon);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            DZProgressDialogUtils.hideProgressDialog();
            DZToastAlertUtils.toast(UserMyInfoFragment.this.getAppApplication(), result);
            if (result.getRs() != 0) {
                if (this.isModifyIcon) {
                    UserMyInfoFragment.this.replaceIcon(UserMyInfoFragment.this.userInfoModel.getIcon(), UserMyInfoFragment.this.iconLocalPath);
                }
                UserMyInfoFragment.this.activity.setResult(100);
                UserMyInfoFragment.this.activity.finish();
            }
        }
    }

    protected String getRootLayoutName() {
        return "user_info_setting_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.photoManageHelper = new PhotoManageHelper(this.activity.getApplicationContext());
    }

    private void loadData() {
        if (this.loadAsyncTask != null) {
            this.loadAsyncTask.cancel(true);
        }
        this.loadAsyncTask = new LoadAsyncTask();
        this.loadAsyncTask.execute(new String[0]);
    }

    protected void initViews(View rootView) {
        this.saveBtn = (Button) findViewByName(rootView, "mc_forum_save_btn");
        this.takePhotoBtn = (Button) findViewByName(rootView, "mc_forum_take_photo_btn");
        this.localPhotoBtn = (Button) findViewByName(rootView, "mc_forum_gallery_pic_btn");
        this.emailText = (TextView) findViewByName(rootView, "mc_forum_email_text");
        this.nicknameEdit = (TextView) findViewByName(rootView, "mc_forum_nickname_edit");
        this.headerImg = (MCHeadIcon) findViewByName(rootView, "mc_forum_user_icon_img");
        this.maleRadio = (Button) findViewByName(rootView, "mc_forum_gender_male_btn");
        this.femaleRadio = (Button) findViewByName(rootView, "mc_forum_gender_female_btn");
        this.secrecyRadio = (Button) findViewByName(rootView, "mc_forum_gender_secrecy_btn");
        this.maleText = (TextView) findViewByName(rootView, "mc_forum_gender_male_text");
        this.femaleText = (TextView) findViewByName(rootView, "mc_forum_gender_female_text");
        this.secrecyText = (TextView) findViewByName(rootView, "mc_forum_gender_secrecy_text");
        this.maleBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_male_box");
        this.femaleBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_female_box");
        this.secrecyBox = (LinearLayout) findViewByName(rootView, "mc_forum_gender_secrecy_box");
        initReloadDialog();
        loadData();
    }

    protected void initReloadDialog() {
        this.reloadDialog = new Builder(this.activity).setTitle(this.resource.getStringId("mc_forum_dialog_tip")).setMessage(this.resource.getStringId("mc_forum_warn_error"));
        this.reloadDialog.setPositiveButton(this.resource.getStringId("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                UserMyInfoFragment.this.loadData();
            }
        });
        this.reloadDialog.setNegativeButton(this.resource.getStringId("mc_forum_dialog_cancel"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UserMyInfoFragment.this.activity.finish();
            }
        });
        this.reloadDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4) {
                    UserMyInfoFragment.this.activity.finish();
                }
                return true;
            }
        });
        this.reloadDialog.create();
        this.exitAlertDialog = new Builder(this.activity).setTitle(this.resource.getString("mc_forum_dialog_tip")).setMessage(this.resource.getString("mc_forum_user_back_warn"));
        this.exitAlertDialog.setPositiveButton(this.resource.getString("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                UserMyInfoFragment.this.activity.finish();
            }
        });
        this.exitAlertDialog.setNegativeButton(this.resource.getString("mc_forum_dialog_cancel"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        this.exitAlertDialog.create();
    }

    protected void initActions(View rootView) {
        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (UserMyInfoFragment.this.updateAsyncTask != null) {
                    UserMyInfoFragment.this.updateAsyncTask.cancel(true);
                }
                UserMyInfoFragment.this.updateAsyncTask = new UpdateAsyncTask();
                UserMyInfoFragment.this.updateAsyncTask.execute(new UserInfoModel[0]);
            }
        });
        this.photoManageHelper.registListener(new FinishListener() {
            public void cameraFinish(int type, Map<String, PictureModel> map, String outputPath, Bitmap bitmap) {
                UserMyInfoFragment.this.headerImg.setImageBitmap(bitmap);
                UserMyInfoFragment.this.headerImg.setBackgroundDrawable(null);
                UserMyInfoFragment.this.iconLocalPath = outputPath;
            }

            public void photoFinish(int type, Map<String, PictureModel> map) {
            }
        });
        this.takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.photoManageHelper.openPhotoGraph(UserMyInfoFragment.this.activity, 1);
            }
        });
        this.localPhotoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.photoManageHelper.openPhotoSelector(UserMyInfoFragment.this.activity, 1);
            }
        });
        this.maleBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.gender = 1;
                UserMyInfoFragment.this.maleRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
                UserMyInfoFragment.this.femaleRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserMyInfoFragment.this.secrecyRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            }
        });
        this.femaleBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.gender = 2;
                UserMyInfoFragment.this.maleRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserMyInfoFragment.this.femaleRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
                UserMyInfoFragment.this.secrecyRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            }
        });
        this.secrecyBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.gender = 0;
                UserMyInfoFragment.this.maleRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserMyInfoFragment.this.femaleRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
                UserMyInfoFragment.this.secrecyRadio.setBackgroundResource(UserMyInfoFragment.this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
            }
        });
        this.maleText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.maleBox.performClick();
            }
        });
        this.femaleText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.femaleBox.performClick();
            }
        });
        this.secrecyText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.secrecyBox.performClick();
            }
        });
        this.maleRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.maleBox.performClick();
            }
        });
        this.femaleRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.femaleBox.performClick();
            }
        });
        this.secrecyRadio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserMyInfoFragment.this.secrecyBox.performClick();
            }
        });
        this.nicknameEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    UserMyInfoFragment.this.nicknameEdit.setFocusable(false);
                    UserMyInfoFragment.this.nicknameEdit.setFocusableInTouchMode(true);
                    UserMyInfoFragment.this.hideSoftKeyboard();
                }
                UserMyInfoFragment.this.nicknameEdit.setFocusable(true);
                return false;
            }
        });
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        topSettingModel.title = this.resource.getString("mc_forum_user_info_setting");
        topSettingModel.leftModels = new ArrayList();
        dealTopBar(topSettingModel);
        registerTopListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action == UserMyInfoFragment.this.TOPBAR_BACK) {
                    Toast.makeText(UserMyInfoFragment.this.getActivity(), "评论", 0).show();
                }
            }
        });
    }

    public boolean isChildInteruptBackPress() {
        if (!isModify()) {
            return false;
        }
        this.exitAlertDialog.show();
        return true;
    }

    private boolean isModify() {
        if (this.userInfoModel == null) {
            return false;
        }
        if (MCStringUtil.isEmpty(this.iconLocalPath) && this.gender == this.userInfoModel.getGender()) {
            return false;
        }
        return true;
    }

    private void updateView(UserInfoModel userInfoModel) {
        this.emailText.setText(userInfoModel.getEmail());
        this.nicknameEdit.setText(userInfoModel.getNickname());
        this.gender = userInfoModel.getGender();
        if (this.gender == 0) {
            this.maleRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            this.femaleRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            this.secrecyRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
        } else if (this.gender == 1) {
            this.maleRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
            this.femaleRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            this.secrecyRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
        } else {
            this.maleRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
            this.femaleRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_h"));
            this.secrecyRadio.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_set_icon3_n"));
        }
        if (new SettingSharePreference(this.activity).isPicAvailable()) {
            ImageLoader.getInstance().displayImage(MCAsyncTaskLoaderImage.formatUrl(userInfoModel.getIcon(), FinalConstant.RESOLUTION_SMALL), this.headerImg);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.photoManageHelper.onActivityResult(this.activity, requestCode, resultCode, data);
    }

    public void onDestroy() {
        super.onDestroy();
        this.photoManageHelper.onDestroy();
        this.reloadDialog = null;
        this.exitAlertDialog = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.loadAsyncTask != null) {
            this.loadAsyncTask.cancel(true);
        }
        if (this.updateAsyncTask != null) {
            this.updateAsyncTask.cancel(true);
        }
    }

    private synchronized void replaceIcon(String url, final String sourcePath) {
        ImageLoader.getInstance().getMemoryCache().remove(url);
        File file = ImageLoader.getInstance().getDiskCache().get(url);
        if (file != null && file.exists()) {
            final String destPath = file.getPath();
            new Thread() {
                public void run() {
                    new FileTransferServiceImpl(UserMyInfoFragment.this.getAppApplication()).copyFile(sourcePath, destPath);
                }
            }.start();
        }
    }
}
