package com.mobcent.discuz.module.person.activity.fragment;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.model.BaseResult;
import com.mobcent.discuz.android.model.PersonalSettingModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.about.fragment.activity.AboutActivity;
import com.mobcent.discuz.module.person.activity.UserMyPwdActivity;
import com.mobcent.discuz.module.person.activity.delegate.TaskExecuteDelegate;
import com.mobcent.discuz.module.person.activity.task.UserLogoutAsyncTaskLoader;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.update.android.constant.MCUpdateConstant;
import com.mobcent.update.android.os.service.helper.UpdateCheckHelper;
import com.mobcent.update.android.task.UpdateCallback;

public class SettingFragment extends BaseFragment {
    private static Button locationInfoBtn;
    private int LOCATION_HIDE = 1;
    private int LOCATION_OPEN = 0;
    private Button aboutBtn;
    private RelativeLayout atBox;
    private Button atBtn;
    private View atLine;
    private Button clearCacheBth;
    private long currentUserId;
    private LinearLayout locationInfoBox;
    private View locationInfoLine;
    private RelativeLayout locationServiceBox;
    private Button locationServiceBtn;
    private View locationServiceLine;
    private LocationSettingTask locationSettingTask;
    private View logoutLine;
    private TextView logoutText;
    private RelativeLayout notifyBox;
    private Button notifyFlagBtn;
    private View notifyLine;
    private Button passwordBtn;
    private Button picFlagBtn;
    private RelativeLayout replyBox;
    private Button replyFlagBtn;
    private View replyLine;
    private View rootView;
    private PersonalSettingModel settingModel;
    private Button shareAppBtn;
    private SettingSharePreference sp = null;
    private Button updateBtn;
    private UserService userService;

    private class LocationSettingTask extends AsyncTask<Integer, Void, BaseResult> {
        int locationState;

        public LocationSettingTask(int locationState) {
            this.locationState = locationState;
        }

        protected BaseResult doInBackground(Integer... params) {
            return SettingFragment.this.userService.locationSetting(this.locationState);
        }

        protected void onPostExecute(BaseResult result) {
            super.onPostExecute(result);
            SettingFragment.this.locationServiceBtn.setClickable(true);
            SettingFragment.locationInfoBtn.setClickable(true);
            if (result.getAlert() == 1) {
                MCToastUtils.toast(SettingFragment.this.getAppApplication(), result.getErrorInfo());
            }
            if (result.getRs() != 1) {
                if (this.locationState == SettingFragment.this.LOCATION_OPEN) {
                    SettingFragment.this.settingModel.setLocationAvailable(false);
                } else {
                    SettingFragment.this.settingModel.setLocationAvailable(true);
                }
            }
            if (SettingFragment.this.settingModel.isLocationOpen()) {
                SettingFragment.this.setBtnSelect(SettingFragment.locationInfoBtn, SettingFragment.this.settingModel.isLocationAvailable());
            } else {
                SettingFragment.this.setBtnSelect(SettingFragment.locationInfoBtn, false);
            }
            SettingFragment.this.sp.updateSetting(SettingFragment.this.settingModel, SettingFragment.this.currentUserId);
        }
    }

    protected String getRootLayoutName() {
        return "settting_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userService = new UserServiceImpl(this.activity);
        this.sp = new SettingSharePreference(this.activity);
    }

    protected void initViews(View view) {
        this.rootView = view;
        this.logoutText = (TextView) view.findViewById(this.resource.getViewId("logout_text"));
        this.logoutLine = view.findViewById(this.resource.getViewId("logout_line"));
        this.passwordBtn = (Button) view.findViewById(this.resource.getViewId("password_btn"));
        this.replyBox = (RelativeLayout) view.findViewById(this.resource.getViewId("reply_box"));
        this.replyFlagBtn = (Button) view.findViewById(this.resource.getViewId("reply_flag_btn"));
        this.replyLine = view.findViewById(this.resource.getViewId("reply_line"));
        this.atBox = (RelativeLayout) view.findViewById(this.resource.getViewId("at_box"));
        this.atBtn = (Button) view.findViewById(this.resource.getViewId("at_flag_btn"));
        this.atLine = view.findViewById(this.resource.getViewId("at_line"));
        this.notifyBox = (RelativeLayout) view.findViewById(this.resource.getViewId("notify_box"));
        this.notifyFlagBtn = (Button) view.findViewById(this.resource.getViewId("notify_flag_btn"));
        this.notifyLine = view.findViewById(this.resource.getViewId("notify_line"));
        this.locationServiceBox = (RelativeLayout) view.findViewById(this.resource.getViewId("location_service_box"));
        this.locationServiceBtn = (Button) view.findViewById(this.resource.getViewId("location_service_flag_btn"));
        this.locationServiceLine = view.findViewById(this.resource.getViewId("location_service_line"));
        this.locationInfoBox = (LinearLayout) view.findViewById(this.resource.getViewId("location_info_box"));
        locationInfoBtn = (Button) view.findViewById(this.resource.getViewId("location_info_btn"));
        this.locationInfoLine = view.findViewById(this.resource.getViewId("location_info_line"));
        this.picFlagBtn = (Button) view.findViewById(this.resource.getViewId("pic_flag_btn"));
        this.shareAppBtn = (Button) view.findViewById(this.resource.getViewId("share_app_btn"));
        this.clearCacheBth = (Button) view.findViewById(this.resource.getViewId("clear_cache_btn"));
        this.updateBtn = (Button) view.findViewById(this.resource.getViewId("update_btn"));
        this.aboutBtn = (Button) view.findViewById(this.resource.getViewId("about_btn"));
    }

    public void onResume() {
        super.onResume();
        this.currentUserId = this.sharedPreferencesDB.getUserId();
        this.settingModel = this.sp.querySetting(this.currentUserId);
        resumeInitView(this.rootView);
    }

    private void setBtnSelect(Button btn, boolean isSelect) {
        btn.setSelected(isSelect);
    }

    public void resumeInitView(View view) {
        if (this.userService.isLogin()) {
            this.logoutLine.setVisibility(8);
            this.replyLine.setVisibility(0);
            this.atLine.setVisibility(0);
            this.notifyLine.setVisibility(0);
            this.logoutText.setVisibility(8);
            this.replyBox.setVisibility(0);
            this.atBox.setVisibility(0);
            this.notifyBox.setVisibility(0);
            this.passwordBtn.setText(this.resource.getString("mc_forum_password_setting"));
        } else {
            this.logoutLine.setVisibility(8);
            this.replyLine.setVisibility(8);
            this.atLine.setVisibility(8);
            this.notifyLine.setVisibility(8);
            this.logoutText.setVisibility(8);
            this.replyBox.setVisibility(8);
            this.atBox.setVisibility(8);
            this.notifyBox.setVisibility(8);
            this.passwordBtn.setText(this.resource.getString("mc_forum_user_loginbtn"));
            this.passwordBtn.setVisibility(8);
        }
        dealLocationBox(view, this.userService.isLogin());
        setBtnSelect(this.replyFlagBtn, this.settingModel.isReplyNotify());
        setBtnSelect(this.atBtn, this.settingModel.isAtNotify());
        setBtnSelect(this.notifyFlagBtn, this.settingModel.isSoundOpen());
        setBtnSelect(this.picFlagBtn, this.settingModel.isPicAvailable());
        setBtnSelect(this.locationServiceBtn, this.settingModel.isLocationOpen());
        if (this.settingModel.isLocationOpen()) {
            setBtnSelect(locationInfoBtn, this.settingModel.isLocationAvailable());
        } else {
            setBtnSelect(locationInfoBtn, false);
        }
    }

    protected void initActions(View rootView) {
        this.logoutText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Builder(SettingFragment.this.activity).setMessage(SettingFragment.this.getResources().getString(SettingFragment.this.resource.getStringId("mc_forum_logout_dialog"))).setNegativeButton(SettingFragment.this.getResources().getString(SettingFragment.this.resource.getStringId("mc_forum_dialog_cancel")), null).setPositiveButton(SettingFragment.this.resource.getStringId("mc_forum_dialog_confirm"), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserLogoutAsyncTaskLoader asyncTaskLoader = new UserLogoutAsyncTaskLoader(SettingFragment.this.activity);
                        asyncTaskLoader.setTaskExecuteDelegate(new TaskExecuteDelegate() {
                            public void executeSuccess(String url) {
                                SettingFragment.this.onResume();
                            }

                            public void executeFail() {
                            }
                        });
                        asyncTaskLoader.execute(new Void[0]);
                    }
                }).show();
            }
        });
        this.passwordBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SettingFragment.this.userService.isLogin()) {
                    SettingFragment.this.startActivity(new Intent(SettingFragment.this.activity, UserMyPwdActivity.class));
                } else if (!LoginHelper.doInterceptor(SettingFragment.this.activity, null, null)) {
                }
            }
        });
        this.replyFlagBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingFragment.this.settingModel.setReplyNotify(!SettingFragment.this.settingModel.isReplyNotify());
                SettingFragment.this.setBtnSelect(SettingFragment.this.replyFlagBtn, SettingFragment.this.settingModel.isReplyNotify());
                SettingFragment.this.sp.updateSetting(SettingFragment.this.settingModel, SettingFragment.this.currentUserId);
            }
        });
        this.clearCacheBth.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCToastUtils.toastByResName(SettingFragment.this.getAppApplication(), "mc_forum_clear_cache_proess");
                new Thread() {
                    public void run() {
                        MCLibIOUtil.cleanCache(SettingFragment.this.getAppApplication());
                        SettingFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                MCToastUtils.toastByResName(SettingFragment.this.getAppApplication(), "mc_forum_clear_cache_success");
                            }
                        });
                    }
                }.start();
            }
        });
        this.shareAppBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingFragment.this.share();
            }
        });
        this.atBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingFragment.this.settingModel.setAtNotify(!SettingFragment.this.settingModel.isAtNotify());
                SettingFragment.this.setBtnSelect(SettingFragment.this.atBtn, SettingFragment.this.settingModel.isAtNotify());
                SettingFragment.this.sp.updateSetting(SettingFragment.this.settingModel, SettingFragment.this.currentUserId);
            }
        });
        this.notifyFlagBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingFragment.this.settingModel.setSoundOpen(!SettingFragment.this.settingModel.isSoundOpen());
                SettingFragment.this.setBtnSelect(SettingFragment.this.notifyFlagBtn, SettingFragment.this.settingModel.isSoundOpen());
                SettingFragment.this.sp.updateSetting(SettingFragment.this.settingModel, SettingFragment.this.currentUserId);
            }
        });
        this.picFlagBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingFragment.this.settingModel.setPicAvailable(!SettingFragment.this.settingModel.isPicAvailable());
                SettingFragment.this.setBtnSelect(SettingFragment.this.picFlagBtn, SettingFragment.this.settingModel.isPicAvailable());
                SettingFragment.this.sp.updateSetting(SettingFragment.this.settingModel, SettingFragment.this.currentUserId);
            }
        });
        this.updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(SettingFragment.this.activity);
                dialog.setMessage(SettingFragment.this.getResources().getString(SettingFragment.this.resource.getStringId("mc_forum_update_loding")));
                dialog.show();
                UpdateCheckHelper.check(SettingFragment.this.activity.getApplicationContext(), new UpdateCallback() {
                    public void onChecked(boolean isHaveNew) {
                        dialog.dismiss();
                        if (!isHaveNew) {
                            MCToastUtils.toastByResName(SettingFragment.this.activity, "mc_forum_update_new");
                        }
                    }
                });
            }
        });
        this.locationServiceBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.setClickable(false);
                SettingFragment.this.settingModel.setLocationOpen(!SettingFragment.this.settingModel.isLocationOpen());
                SettingFragment.this.setBtnSelect(SettingFragment.this.locationServiceBtn, SettingFragment.this.settingModel.isLocationOpen());
                if (!SettingFragment.this.settingModel.isLocationOpen()) {
                    SettingFragment.this.locationSettingTask = new LocationSettingTask(SettingFragment.this.LOCATION_HIDE);
                    SettingFragment.this.locationSettingTask.execute(new Integer[0]);
                } else if (SettingFragment.this.settingModel.isLocationAvailable()) {
                    SettingFragment.this.locationSettingTask = new LocationSettingTask(SettingFragment.this.LOCATION_OPEN);
                    SettingFragment.this.locationSettingTask.execute(new Integer[0]);
                } else {
                    v.setClickable(true);
                    SettingFragment.this.sp.updateSetting(SettingFragment.this.settingModel, SettingFragment.this.currentUserId);
                }
            }
        });
        locationInfoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SettingFragment.this.settingModel.isLocationOpen()) {
                    boolean z;
                    v.setClickable(false);
                    PersonalSettingModel access$800 = SettingFragment.this.settingModel;
                    if (SettingFragment.this.settingModel.isLocationAvailable()) {
                        z = false;
                    } else {
                        z = true;
                    }
                    access$800.setLocationAvailable(z);
                    SettingFragment.this.locationSettingTask = new LocationSettingTask(SettingFragment.this.getLocationState());
                    SettingFragment.this.locationSettingTask.execute(new Integer[0]);
                }
            }
        });
        this.aboutBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingFragment.this.startActivity(new Intent(SettingFragment.this.activity, AboutActivity.class));
            }
        });
    }

    private int getLocationState() {
        return this.settingModel.isLocationAvailable() ? this.LOCATION_OPEN : this.LOCATION_HIDE;
    }

    private void share() {
        MCShareModel shareModel = new MCShareModel();
        shareModel.setDownloadUrl(this.resource.getString("mc_share_download_url"));
        shareModel.setTitle(this.resource.getString(MCUpdateConstant.UPDATE_APP_NAME));
        shareModel.setContent(this.resource.getString("mc_forum_default_share_content"));
        shareModel.setType(6);
        MCForumLaunchShareHelper.share(this.activity, shareModel);
    }

    private void dealLocationBox(View parent, boolean isLogin) {
        if (isLogin) {
            this.locationServiceBox.setVisibility(0);
            this.locationServiceLine.setVisibility(0);
            this.locationInfoBox.setVisibility(0);
            this.locationInfoLine.setVisibility(0);
            return;
        }
        this.locationServiceBox.setVisibility(8);
        this.locationServiceLine.setVisibility(8);
        this.locationInfoBox.setVisibility(8);
        this.locationInfoLine.setVisibility(8);
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        if (this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle())) {
            topSettingModel.title = this.resource.getString("mc_forum_setting");
        } else {
            topSettingModel.title = this.moduleModel.getTitle();
        }
        dealTopBar(topSettingModel);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.locationSettingTask != null) {
            this.locationSettingTask.cancel(true);
        }
    }
}
