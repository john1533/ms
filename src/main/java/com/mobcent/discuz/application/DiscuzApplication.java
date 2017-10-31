package com.mobcent.discuz.application;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;

import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.exception.CrashHandler;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.model.PayStateModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.os.helper.HeartBeatServiceHelper;
import com.mobcent.discuz.application.config.LowestConfigImpl;
import com.mobcent.discuz.module.msg.helper.MsgNotificationHelper;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.ArrayList;
import java.util.List;

public class DiscuzApplication extends Application {
    public static DiscuzApplication _instance;
    private List<Activity> activityList;
    private BaseResultModel<ConfigModel> configModel;
    private boolean isActivityTopic;
    private boolean isRateSucc;
    private PayStateModel payStateModel;
    private PermissionModel permissionModel;
    private SettingModel settingModel;

    public void onCreate() {
        super.onCreate();
        this.activityList = new ArrayList();
//        CrashHandler.getInstance().init(getApplicationContext());
        _instance = this;
        try {
            LowestManager.getInstance().init(this, new LowestConfigImpl(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        HeartBeatServiceHelper.startService(getApplicationContext());
        registNotificationListener();
        initImageLoader();
    }

    public void addActivity(Activity activity) {
        this.activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        this.activityList.remove(activity);
    }

    public Activity getTopActivity() {
        if (MCListUtils.isEmpty(this.activityList)) {
            return null;
        }
        return (Activity) this.activityList.get(this.activityList.size() - 1);
    }

    private void initImageLoader() {
        ImageLoader.getInstance().init(new Builder(getApplicationContext())
                .threadPriority(3)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(52428800)
                .threadPoolSize(4)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .considerExifParams(true)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                        .bitmapConfig(Config.RGB_565).build()).build());
    }

    public BaseResultModel<ConfigModel> getConfigModel() {
        return this.configModel;
    }

    public void setConfigModel(BaseResultModel<ConfigModel> configModel) {
        if (!(configModel == null || TextUtils.isEmpty(configModel.getVersion()))) {
            CrashHandler.SERVER_VERSION = configModel.getVersion();
        }
        this.configModel = configModel;
    }

    public ConfigModuleModel getModuleModel(long moduleId) {
        if (this.configModel == null || this.configModel.getData() == null) {
            return null;
        }
        return (ConfigModuleModel) ((ConfigModel) this.configModel.getData()).getModuleMap().get(Long.valueOf(moduleId));
    }

    public boolean isActivityTopic() {
        return this.isActivityTopic;
    }

    public void setActivityTopic(boolean isActivityTopic) {
        this.isActivityTopic = isActivityTopic;
    }

    public boolean isRateSucc() {
        return this.isRateSucc;
    }

    public void setRateSucc(boolean isRateSucc) {
        this.isRateSucc = isRateSucc;
    }

    public void registNotificationListener() {
        MsgNotificationHelper.getInstance(getApplicationContext()).setIntentToNotification();
    }

    public void setSettingModel(SettingModel settingModel) {
        this.settingModel = settingModel;
    }

    public SettingModel getSettingModel() {
        return this.settingModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public PermissionModel getPermissionModel() {
        return this.permissionModel;
    }

    public PayStateModel getPayStateModel() {
        return this.payStateModel;
    }

    public void setPayStateModel(PayStateModel payStateModel) {
        this.payStateModel = payStateModel;
    }

    public boolean isPayed() {
        if (this.payStateModel == null || !this.payStateModel.isUserDefined()) {
            return false;
        }
        return true;
    }
}
