package com.mobcent.discuz.base.task;

import android.content.Context;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.model.PayStateModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.service.ConfigService;
import com.mobcent.discuz.android.service.PayStateService;
import com.mobcent.discuz.android.service.impl.ConfigServiceImpl;
import com.mobcent.discuz.android.service.impl.PayStateServiceImpl;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.helper.LocationHelper;
import com.mobcent.lowest.android.ui.module.weather.helper.WeatherWidgetHelper;
import com.mobcent.lowest.android.ui.module.weather.observer.WeatherObserver;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.module.weather.service.impl.WeatherServiceImpl;
import java.util.ArrayList;
import java.util.List;

public class ConfigTask extends BaseTask<BaseResultModel<ConfigModel>> implements ConfigConstant {
    public String TAG = "ConfigTask";
    private RequestCalback<BaseResultModel<ConfigModel>> _callback;
    private final String assetsPath = "config.json";
    private ConfigService configService = new ConfigServiceImpl(this.context);
    private boolean getAllDataByNet = true;
    private boolean isNeedGetPayStateByNet = false;
    private boolean isPayed = false;
    private PayStateService payService = new PayStateServiceImpl(this.context);

    public ConfigTask(Context context, boolean getAllDataByNet, RequestCalback<BaseResultModel<ConfigModel>> _callback) {
        super(context, _callback);
        this._callback = _callback;
        this.getAllDataByNet = getAllDataByNet;
        LocationHelper.startLocation(this.context);
    }

    protected BaseResultModel<ConfigModel> doInBackground(Void... params) {
        if (this.getAllDataByNet) {
            return getAllDataByNet();
        }
        return getAllDataByLocal();
    }

    protected void onPostExecute(BaseResultModel<ConfigModel> result) {
        if (this.isNeedGetPayStateByNet) {
            queryPayStateByNet();
        }
        if (this._callback != null) {
            this._callback.onPostExecute(result);
            if (UserManageHelper.getInstance(this.context).getSettingModel() != null) {
                this._callback.onPostExecute(UserManageHelper.getInstance(this.context).getSettingModel());
            }
            if (UserManageHelper.getInstance(this.context).getPermissionModel() != null) {
                this._callback.onPostExecute(UserManageHelper.getInstance(this.context).getPermissionModel());
            }
        }
//        SettingModel settingModel = UserManageHelper.getInstance(this.context).getSettingModel();
//        List cacheWeatherList = new WeatherServiceImpl(this.context).getWeatherInfoByLocal();
//        if (!MCListUtils.isEmpty(cacheWeatherList)) {
//            LowestManager.getInstance().getConfig().setWeatherListCache(cacheWeatherList);
//            WeatherObserver.getInstance().notifyObservers(cacheWeatherList.get(0));
//        }
//        if (settingModel != null) {
//            WeatherWidgetHelper.requestWeatherDataAsync(this.context, settingModel.getAllowCityQueryWeather(), true, null);
//        } else {
//            WeatherWidgetHelper.requestWeatherDataAsync(this.context, 1, true, null);
//        }
    }

    private void queryPayStateByNet() {
        new Thread() {
            public void run() {
                ConfigTask.this.payService.queryPayByNet(ConfigTask.this.db.getForumKey());
                ConfigTask.this.isNeedGetPayStateByNet = false;
            }
        }.start();
    }

    private BaseResultModel<ConfigModel> getAllDataByNet() {
        BaseResultModel<ConfigModel> result;
//        BaseResultModel<PayStateModel> payResult = this.payService.queryPayByLocal(this.db.getForumKey());
//        PayStateModel payStateModel = null;
//        if (payResult.getRs() == 1) {
//            payStateModel =  payResult.getData();
//            this.isNeedGetPayStateByNet = true;
//        } else {
//            payResult = this.payService.queryPayByNet(this.db.getForumKey());
//            if (payResult.getRs() == 1 && payResult.getData() != null) {
//                payStateModel =  payResult.getData();
//            }
//        }
//        if (payStateModel != null) {
//            DiscuzApplication._instance.setPayStateModel(payStateModel);
//            this.isPayed = payStateModel.isUserDefined();
//        }
        this.isPayed = true;
        UserManageHelper.getInstance(this.context).getSetting(true);
        if (this.isPayed) {
            result = this.configService.getConfigModel(true);
        } else {
            result = this.configService.getConfigModelAssets("config.json");
        }
        setConfigData(result.getData());
        return result;
    }

    private BaseResultModel<ConfigModel> getAllDataByLocal() {
        BaseResultModel<ConfigModel> result;
        BaseResultModel<PayStateModel> payResult = this.payService.queryPayByLocal(this.db.getForumKey());
        PayStateModel payStateModel = null;
        if (payResult.getRs() == 1) {
            payStateModel =  payResult.getData();
            this.isNeedGetPayStateByNet = true;
        } else {
            payResult = this.payService.queryPayByNet(this.db.getForumKey());
            if (payResult.getRs() == 1 && payResult.getData() != null) {
                payStateModel = (PayStateModel) payResult.getData();
            }
        }
        if (payStateModel != null) {
            DiscuzApplication._instance.setPayStateModel(payStateModel);
            this.isPayed = payStateModel.isUserDefined();
        }
        this.isPayed = true;
        UserManageHelper.getInstance(this.context).getSettingByLocal();
        if (this.isPayed) {
            result = this.configService.getConfigModelByLocal();
        } else {
            result = this.configService.getConfigModelAssets("config.json");
        }
        setConfigData((ConfigModel) result.getData());
        return result;
    }

    private void setConfigData(ConfigModel configModel) {
        SettingModel settingModel = UserManageHelper.getInstance(this.context).getSettingModel();
        if (configModel != null) {
            configModel.setPayed(this.isPayed);
            if (!this.isPayed && settingModel != null) {
                ConfigModuleModel moduleModel = (ConfigModuleModel) configModel.getModuleMap().get(Long.valueOf(1));
                if (moduleModel != null) {
                    if (settingModel.getAllowUseWeather() == 1) {
                        if (MCListUtils.isEmpty(moduleModel.getLeftTopbarList())) {
                            moduleModel.setLeftTopbarList(new ArrayList());
                        }
                        ConfigComponentModel componentModel = new ConfigComponentModel();
                        componentModel.setType("weather");
                        moduleModel.getLeftTopbarList().add(0, componentModel);
                    }
                    if (MCListUtils.isEmpty(moduleModel.getComponentList()) && !MCListUtils.isEmpty(settingModel.getComponentList())) {
                        moduleModel.setComponentList(settingModel.getComponentList());
                        int count = moduleModel.getComponentList().size();
                        for (int i = 0; i < count; i++) {
                            ((ConfigComponentModel) moduleModel.getComponentList().get(i)).setStyle(moduleModel.getStyle());
                        }
                    }
                    List<ConfigComponentModel> fastPostList = createComponentList();
                    if (settingModel.getPlugCheck() == 1) {
                        fastPostList.add(createConfigComponentModel(this.resource.getString("mc_forum_sign"), "mc_forum_ico30", ConfigConstant.COMPONENT_SIGN));
                    }
                    ((ConfigModuleModel) configModel.getModuleMap().get(Long.valueOf(3))).setComponentList(fastPostList);
                }
            }
        }
    }

    private List<ConfigComponentModel> createComponentList() {
        List<ConfigComponentModel> componentModels = new ArrayList();
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_publish_text"), "mc_forum_ico27", ConfigConstant.COMPONENT_FASTPOST));
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_pic_topic_list"), "mc_forum_ico28", ConfigConstant.COMPONENT_FASTIMAGE));
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_take_photo"), "mc_forum_ico29", ConfigConstant.COMPONENT_FASTCAMERA));
        componentModels.add(createConfigComponentModel(this.resource.getString("mc_forum_posts_voice"), "mc_forum_ico45", ConfigConstant.COMPONENT_FASTAUDIO));
        return componentModels;
    }

    private ConfigComponentModel createConfigComponentModel(String title, String icon, String type) {
        ConfigComponentModel componentModel = new ConfigComponentModel();
        componentModel.setTitle(title);
        componentModel.setIcon(icon);
        componentModel.setType(type);
        componentModel.setShowTopicTitle(true);
        return componentModel;
    }
}
