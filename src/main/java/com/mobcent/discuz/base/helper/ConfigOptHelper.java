package com.mobcent.discuz.base.helper;

import android.content.Context;
import android.text.TextUtils;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.model.ConfigNavModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigOptHelper implements ConfigConstant {
    public static boolean navContainOnly(ConfigModuleModel moduleModel, String matchingType) {
        List componentList = moduleModel.getComponentList();
        if (!MCListUtils.isEmpty(componentList)
                && componentList.size() == 1
                && matchingType.equals(((ConfigComponentModel) componentList.get(0)).getType())) {
            return true;
        }
        return false;
    }

    public static boolean navContainMore(ConfigModuleModel moduleModel, String matchingType) {
        List componentList = moduleModel.getComponentList();
        if (!MCListUtils.isEmpty(componentList)) {
            int count = componentList.size();
            for (int i = 0; i < count; i++) {
                if (matchingType.equals(((ConfigComponentModel) componentList.get(i)).getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNavContainDiscover(Map<Long, ConfigModuleModel> moduleMap, List<ConfigNavModel> navList) {
        if (!(MCListUtils.isEmpty((List) navList) || moduleMap == null)) {
            for (ConfigNavModel navModel : navList) {
                ConfigModuleModel moduleModel = (ConfigModuleModel) moduleMap.get(Long.valueOf(navModel.getModuleId()));
                if (moduleModel != null && navContainMore(moduleModel, ConfigConstant.COMPONENT_DISCOVER)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ConfigNavModel createPlazaNavModel(Context context) {
        ConfigNavModel navModel = new ConfigNavModel();
        navModel.setIcon("mc_forum_main_bar_button5");
        navModel.setTitle(MCResource.getInstance(context).getString("mc_forum_home_discover"));
        navModel.setModuleId(-1);
        return navModel;
    }

    public static ConfigModuleModel createPlazaModuleModel(Context context) {
        ConfigModuleModel configModuleModel = new ConfigModuleModel();
        configModuleModel.setId(-1);
        configModuleModel.setType(ConfigConstant.MODULE_TYPE_FULL);
        configModuleModel.setIcon("mc_forum_main_bar_button5");
        configModuleModel.setTitle(MCResource.getInstance(context).getString("mc_forum_home_discover"));
        List<ConfigComponentModel> componentModels = new ArrayList();
        ConfigComponentModel componentModel = new ConfigComponentModel();
        componentModel.setType(ConfigConstant.COMPONENT_DISCOVER);
        componentModels.add(componentModel);
        configModuleModel.setComponentList(componentModels);
        return configModuleModel;
    }

    /**
     *module中有需要登录的component
     */
    public static boolean isNeedLogin(ConfigModuleModel moduleModel) {
        if (moduleModel == null) {
            return false;
        }
        if (MCListUtils.isEmpty(moduleModel.getComponentList())) {
            return false;
        }
        if (!ConfigConstant.MODULE_TYPE_FULL.equals(moduleModel.getType())) {
            return false;
        }
        int len = moduleModel.getComponentList().size();
        for (int i = 0; i < len; i++) {
            List componentList = moduleModel.getComponentList();
            if (!MCListUtils.isEmpty(componentList)) {
                int size = componentList.size();
                for (int j = 0; j < size; j++) {
                    boolean isNeedLogin = isNeedLogin(((ConfigComponentModel) componentList.get(i)).getType());
                    if (isNeedLogin) {
                        return isNeedLogin;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static boolean isNeedLogin(ConfigComponentModel componentModel) {
        if (componentModel != null) {
            return isNeedLogin(componentModel.getType());
        }
        return false;
    }

    public static boolean isNeedLogin(String type) {
        if (TextUtils.isEmpty(type)) {
            return false;
        }
        if (ConfigConstant.COMPONENT_MESSAGELIST.equals(type) || ConfigConstant.COMPONENT_USERINFO.equals(type) || ConfigConstant.COMPONENT_USER_LIST.equals(type) || ConfigConstant.COMPONENT_FASTAUDIO.equals(type) || ConfigConstant.COMPONENT_FASTCAMERA.equals(type) || ConfigConstant.COMPONENT_FASTIMAGE.equals(type) || ConfigConstant.COMPONENT_FASTPOST.equals(type) || ConfigConstant.COMPONENT_SIGN.equals(type)) {
            return true;
        }
        return false;
    }
}
