package com.mobcent.discuz.module.person.activity.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import com.mobcent.discuz.activity.HomeActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.activity.view.PopMenuDialog.PopModel;
import com.mobcent.discuz.activity.view.PopMenuDialog.PopupListDialogListener;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.UserInfoAsynTask;
import com.mobcent.discuz.module.person.activity.UserLoginActivity;
import com.mobcent.discuz.module.person.activity.delegate.TaskExecuteDelegate;
import com.mobcent.discuz.module.person.activity.task.UserLogoutAsyncTaskLoader;
import com.mobcent.discuz.module.person.dialog.SwitchUserDialog;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseUserHomeFragment extends BaseFragment implements FinalConstant, UserConstant {
    public static int FLAG_LOGOUT = 1;
    public static final int SKIP_REQUEST_CODE = 100;
    public static final int SKIP_RESULT_CODE = 100;
    protected long currentUserId;
    protected UserInfoModel currentUserInfo;
    protected MCAsyncTaskLoaderImage imgLoader;
    protected List<PopupModel> popupList;
    private SwitchUserAsyncTask switchUserTask = null;
    private UserInfoAsynTask userInfoTask = null;
    protected UserService userService;

    private class SwitchUserAsyncTask extends AsyncTask<String, Void, BaseResultModel<UserInfoModel>> {
        private SwitchUserAsyncTask() {
        }

        protected BaseResultModel<UserInfoModel> doInBackground(String... params) {
            return BaseUserHomeFragment.this.userService.switchUser(params[0]);
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(BaseUserHomeFragment.this.activity.getApplicationContext(), result);
            if (result.getRs() == 1) {
//                LocationHelper.startLocation(BaseUserHomeFragment.this.activity);
                BaseUserHomeFragment.this.currentUserId = ((UserInfoModel) result.getData()).getUserId();
                BaseUserHomeFragment.this.refreshUserInfo();
            }
        }
    }

    protected abstract void onUserInfoResult(UserInfoModel userInfoModel);

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userService = new UserServiceImpl(getActivity().getApplicationContext());
        this.currentUserId = getBundle().getLong("userId", 0);
        if (this.currentUserId == 0) {
            this.currentUserId = this.sharedPreferencesDB.getUserId();
        }
        this.imgLoader = MCAsyncTaskLoaderImage.getInstance(this.activity);
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = new TopSettingModel();
        topSettingModel.isVisibile = false;
        dealTopBar(topSettingModel);
    }

    protected void showSwitchUserDialog() {
        SwitchUserDialog switchDialog = new SwitchUserDialog(this.activity);
        switchDialog.setOnItemClickListener(new PopupListDialogListener() {
            public void onItemClick(PopModel popModel, int position) {
                if (popModel.action == BaseUserHomeFragment.FLAG_LOGOUT) {
                    BaseUserHomeFragment.this.logoutUser();
                } else if (popModel.data instanceof UserInfoModel) {
                    BaseUserHomeFragment.this.switchUser(((UserInfoModel)popModel.data).getNickname());
                }
            }
        });
        switchDialog.setPopList(getSwitchUserList());
        switchDialog.showCenter();
    }

    private List<PopModel> getSwitchUserList() {
        List<PopModel> popList = new ArrayList();
        if (!(this.currentUserInfo == null || MCListUtils.isEmpty(this.currentUserInfo.getRepeatList()))) {
            List<UserInfoModel> userList = this.currentUserInfo.getRepeatList();
            int count = userList.size();
            for (int i = 0; i < count; i++) {
                UserInfoModel userModel = (UserInfoModel) userList.get(i);
                PopModel popModel = new PopModel();
                popModel.itemName = userModel.getNickname();
                popModel.drawableName = "mc_forum_personal_change_account";
                popModel.data = userModel;
                popList.add(popModel);
            }
        }
        return popList;
    }

    protected void logoutUser() {
        new Builder(this.activity).setMessage(getResources().getString(this.resource.getStringId("mc_forum_logout_dialog"))).setNegativeButton(getResources().getString(this.resource.getStringId("mc_forum_dialog_cancel")), null).setPositiveButton(this.resource.getStringId("mc_forum_dialog_confirm"), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UserLogoutAsyncTaskLoader asyncTaskLoader = new UserLogoutAsyncTaskLoader(BaseUserHomeFragment.this.activity);
                asyncTaskLoader.setTaskExecuteDelegate(new TaskExecuteDelegate() {
                    public void executeSuccess(String url) {
                        if (!(BaseUserHomeFragment.this.activity instanceof HomeActivity)) {
                            BaseUserHomeFragment.this.activity.finish();
                        }
                        BaseUserHomeFragment.this.activity.startActivity(new Intent(BaseUserHomeFragment.this.activity, UserLoginActivity.class));
                    }

                    public void executeFail() {
                        executeSuccess("");
                    }
                });
                asyncTaskLoader.execute(new Void[0]);
            }
        }).show();
    }

    protected void refreshUserInfo() {
        if (!(this.userInfoTask == null || this.userInfoTask.isCancelled())) {
            this.userInfoTask.cancel(true);
        }
        this.userInfoTask = new UserInfoAsynTask(this.activity, new BaseRequestCallback<BaseResultModel<UserInfoModel>>() {
            public void onPreExecute() {
                BaseUserHomeFragment.this.getLoadingPro().show();
            }

            public void onPostExecute(BaseResultModel<UserInfoModel> result) {
                BaseUserHomeFragment.this.getLoadingPro().dismiss();
                DZToastAlertUtils.toast(BaseUserHomeFragment.this.activity.getApplicationContext(), result);
                UserInfoModel resultRserInfoModel = (UserInfoModel) result.getData();
                if (resultRserInfoModel != null) {
                    BaseUserHomeFragment.this.currentUserInfo = resultRserInfoModel;
                    BaseUserHomeFragment.this.currentUserInfo.setUserId(BaseUserHomeFragment.this.currentUserId);
                }
                BaseUserHomeFragment.this.onUserInfoResult(resultRserInfoModel);
            }
        }, this.currentUserId);
        this.userInfoTask.execute(new Void[0]);
    }

    protected void switchUser(String userName) {
        if (!(this.switchUserTask == null || this.switchUserTask.isCancelled())) {
            this.switchUserTask.cancel(true);
        }
        this.switchUserTask = new SwitchUserAsyncTask();
        this.switchUserTask.execute(new String[]{userName});
    }

    protected void loadIcon(MCHeadIcon headIcon, UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            headIcon.setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.activity));
            ImageLoader.getInstance().displayImage(MCAsyncTaskLoaderImage.formatUrl(userInfoModel.getIcon(), FinalConstant.RESOLUTION_SMALL), (ImageView) headIcon, new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(20)).build());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    BaseUserHomeFragment.this.refreshUserInfo();
                }
            });
        }
    }
}
