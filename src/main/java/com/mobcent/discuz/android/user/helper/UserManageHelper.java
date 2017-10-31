package com.mobcent.discuz.android.user.helper;

import android.app.NotificationManager;
import android.content.Context;
import com.mobcent.discuz.android.api.util.DZHttpClientUtil;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.db.UserDBUtil;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.SettingService;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.SettingServiceImpl;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.List;

public class UserManageHelper {
    private static UserManageHelper userManageHelper;
    private Context context;
    private List<ChangeUserInfoListener> list;
    private ObserverHelper observerHelper;
    private PermissionModel permissionModel = null;
    private SettingModel settingModel = null;
    private SettingService settingService;
    private SharedPreferencesDB sharedPreferencesDB;
    private UserDBUtil userDBUtil;
    private UserService userService;

    public interface ChangeUserInfoListener {
        void change(boolean z, UserInfoModel userInfoModel);
    }

    public interface SettingDataDelegate {
        void savePermissionToApplication(PermissionModel permissionModel);

        void saveSettingToApplication(SettingModel settingModel);
    }

    private UserManageHelper(Context context) {
        this.context = context.getApplicationContext();
        this.settingService = new SettingServiceImpl(context.getApplicationContext());
        this.userService = new UserServiceImpl(this.context);
        this.userDBUtil = UserDBUtil.getInstance(context.getApplicationContext());
        this.list = new ArrayList();
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(context.getApplicationContext());
        this.observerHelper = ObserverHelper.getInstance();
    }

    public static synchronized UserManageHelper getInstance(Context context) {
        UserManageHelper userManageHelper1;
        synchronized (UserManageHelper.class) {
            if (userManageHelper == null) {
                userManageHelper = new UserManageHelper(context.getApplicationContext());
            }
            userManageHelper1 = userManageHelper;
        }
        return userManageHelper1;
    }

    public SettingModel getSettingModel() {
        return this.settingModel;
    }

    public PermissionModel getPermissionModel() {
        return this.permissionModel;
    }

    public void setSettingModel(SettingModel settingModel) {
        this.settingModel = settingModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public void registListener(ChangeUserInfoListener listener) {
        if (listener != null) {
            this.list.add(listener);
        }
    }

    public void removeListener(ChangeUserInfoListener listener) {
        if (this.list.contains(listener)) {
            this.list.remove(listener);
        }
    }

    public void getUserPermission(boolean isLocal, long userId) {
        savePermVariable(userId, this.userService.getPermissionStr(isLocal, userId), null);
    }

    private void savePermVariable(long userId, String permissionStr, SettingDataDelegate delegate) {
        if (!MCStringUtil.isEmpty(permissionStr)) {
            this.userDBUtil.savePermission(userId, permissionStr);
            BaseResultModel<PermissionModel> baseResultModel = this.userService.getPermissionModel(permissionStr);
            if (baseResultModel.getRs() == 1) {
                this.permissionModel = baseResultModel.getData();
                if (delegate != null) {
                    delegate.savePermissionToApplication(this.permissionModel);
                }
            }
        }
    }

    public synchronized void getSetting(boolean isTimeOut) {
        getSetting(isTimeOut, null);
    }

    public synchronized void getSetting(boolean isTimeOut, SettingDataDelegate delegate) {
        setDataBySetting(this.settingService.getSettingContent(isTimeOut), delegate);
    }

    public synchronized void getSettingByLocal() {
        getSettingByLocal(null);
    }

    public synchronized void getSettingByLocal(SettingDataDelegate delegate) {
        setDataBySetting(this.settingService.getSettingByLocal(), delegate);
    }

    private void setDataBySetting(BaseResultModel<SettingModel> baseResultModel, SettingDataDelegate delegate) {
        if (baseResultModel.getRs() == 1) {
            this.settingModel = baseResultModel.getData();
            if (delegate != null) {
                delegate.saveSettingToApplication(this.settingModel);
            }
            savePermVariable(0, this.settingModel.getPostInfo(), delegate);
            this.sharedPreferencesDB.setSettingModel(baseResultModel.getData());
        }
    }

    public UserInfoModel getUserInfo(long userId) {
        return this.userDBUtil.getCurrUser(userId);
    }

    public boolean addUserInfo(final UserInfoModel userInfoModel, boolean sourceFormLoginOrRegist) {
        saveUserInfoToSharePre(userInfoModel);
        for (ChangeUserInfoListener listener : this.list) {
            listener.change(true, userInfoModel);
        }
        String passwordTemp = userInfoModel.getPwd();
        if (UserConstant.SYSTEM_USER_PASSWORD.equals(passwordTemp)) {
            userInfoModel.setPwd("");
        }
        if (!this.userDBUtil.addUserInfo(userInfoModel, sourceFormLoginOrRegist)) {
            return false;
        }
        this.observerHelper.getActivityObservable().onRefreshMessageList();
        this.observerHelper.getActivityObservable().updateHomeTabNum(this.observerHelper.getActivityObservable().getTabNum(this.context, this.sharedPreferencesDB.getUserId()));
        if (UserConstant.SYSTEM_USER_PASSWORD.equals(passwordTemp)) {
            return true;
        }
        new Thread() {
            public void run() {
                UserManageHelper.this.getSetting(false);
                UserManageHelper.this.userService.getMentionFriendByNet(userInfoModel.getUserId());
            }
        }.start();
        return true;
    }

    public void logout() {
        deleteIconBitmap(this.sharedPreferencesDB.getIcon());
        this.sharedPreferencesDB.removeUserInfo();
        ((NotificationManager) this.context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        DZHttpClientUtil.clearCookie(this.context, MCResource.getInstance(this.context.getApplicationContext()).getString("mc_discuz_base_request_url"));
        ObserverHelper.getInstance().getActivityObservable().updateHomeTabNum(0);
        for (ChangeUserInfoListener listener : this.list) {
            listener.change(false, null);
        }
        new Thread() {
            public void run() {
                UserManageHelper.this.getSetting(false);
            }
        }.start();
    }

    public boolean updateUserInfo(UserInfoModel userInfoModel) {
        this.sharedPreferencesDB.setUserId(userInfoModel.getUserId());
        this.sharedPreferencesDB.setIcon(userInfoModel.getIcon());
        for (ChangeUserInfoListener listener : this.list) {
            listener.change(true, userInfoModel);
        }
        return this.userDBUtil.updateUserInfo(userInfoModel);
    }

    public boolean changePwd(UserInfoModel userInfoModel) {
        this.sharedPreferencesDB.updateTokenAndSecret(userInfoModel.getToken(), userInfoModel.getSecret());
        return this.userDBUtil.changePassword(userInfoModel.getUserId(), userInfoModel.getPwd(), userInfoModel.getToken(), userInfoModel.getSecret());
    }

    public void notifyUserInfo(UserInfoModel userInfoModel) {
        if (userInfoModel.getUserId() == this.sharedPreferencesDB.getUserId()) {
            this.sharedPreferencesDB.setIcon(userInfoModel.getIcon());
            for (ChangeUserInfoListener listener : this.list) {
                listener.change(true, userInfoModel);
            }
        }
    }

    public void deleteIconBitmap(String icon) {
        if (!MCStringUtil.isEmpty(icon)) {
            MCAsyncTaskLoaderImage.getInstance(this.context.getApplicationContext()).deleteBimap(icon);
        }
    }

    private void saveUserInfoToSharePre(UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            this.sharedPreferencesDB.setUserId(userInfoModel.getUserId());
            this.sharedPreferencesDB.updateTokenAndSecret(userInfoModel.getToken(), userInfoModel.getSecret());
            this.sharedPreferencesDB.setIcon(userInfoModel.getIcon());
            this.sharedPreferencesDB.setNickName(userInfoModel.getNickname());
        }
    }
}
