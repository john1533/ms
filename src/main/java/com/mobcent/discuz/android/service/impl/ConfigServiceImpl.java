package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import android.text.TextUtils;
import com.mobcent.discuz.android.api.ConfigRestfulApiRequester;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.service.ConfigService;
import com.mobcent.discuz.android.service.impl.helper.ConfigServiceImplHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {
    private String MODULE_KEY = "module_";

    public ConfigServiceImpl(Context context) {
        super(context);
    }

    public BaseResultModel<ConfigModel> getConfigModel(boolean isTimeOut) {
        String jsonStr = ConfigRestfulApiRequester.getConfig(this.context, isTimeOut);
        BaseResultModel<ConfigModel> result = ConfigServiceImplHelper.getConfigModel(this.context, jsonStr);
        if (result.getRs() == 0) {
            BaseResultModel<ConfigModel> resultTemp = ConfigServiceImplHelper.getConfigModel(this.context, this.db.getConfig());
            if (resultTemp.getRs() == 1) {
                return resultTemp;
            }
            return result;
        }
        this.db.setConfig(jsonStr);
        return result;
    }

    public BaseResultModel<ConfigModel> getConfigModelByLocal() {
        String jsonStr;
        if (MCStringUtil.isEmpty(this.db.getConfig())) {
            jsonStr = ConfigRestfulApiRequester.getConfig(this.context, false);
        } else {
            jsonStr = this.db.getConfig();
        }
        return ConfigServiceImplHelper.getConfigModel(this.context, jsonStr);
    }

    public BaseResultModel<ConfigModel> getConfigModelAssets(String assetsPath) {
        IOException e;
        Throwable th;
        BaseResultModel<ConfigModel> result = new BaseResultModel();
        StringBuilder json = new StringBuilder();
        InputStream is = null;
        BufferedReader br = null;
        try {
            is = this.context.getAssets().open(assetsPath);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    String data = br2.readLine();
                    if (data == null) {
                        break;
                    }
                    json.append(data);
                } catch (IOException e2) {
                    e = e2;
                    br = br2;
                } catch (Throwable th2) {
                    th = th2;
                    br = br2;
                }
            }
            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                    br = br2;
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
                if (TextUtils.isEmpty(json.toString())) {
                    return ConfigServiceImplHelper.getConfigModel(this.context, json.toString());
                }
                result.setRs(0);
                return result;
            }
            br = br2;
        } catch (IOException e4) {
            try {
                e4.printStackTrace();
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e3222) {
                        e3222.printStackTrace();
                    }
                }
                if (TextUtils.isEmpty(json.toString())) {
                    return ConfigServiceImplHelper.getConfigModel(this.context, json.toString());
                }
                result.setRs(0);
                return result;
            } catch (Throwable th3) {
                th = th3;
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e32222) {
                        e32222.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e322222) {
                        e322222.printStackTrace();
                    }
                }
            }
        }
        if (TextUtils.isEmpty(json.toString())) {
            return ConfigServiceImplHelper.getConfigModel(this.context, json.toString());
        }
        result.setRs(0);
        return result;
    }

    public BaseResultModel<ConfigModuleModel> queryConfigModel(long moduleId, boolean isLocal) {
        String key = this.MODULE_KEY + moduleId;
        if (isLocal) {
            return ConfigServiceImplHelper.getModuleModel(this.context, this.db.queryJsonByKey(key));
        }
        String json = ConfigRestfulApiRequester.getModule(this.context, moduleId);
        BaseResultModel<ConfigModuleModel> result = ConfigServiceImplHelper.getModuleModel(this.context, json);
        try {
            if (this.db.queryJsonByKey(key).equals(json)) {
                return result;
            }
            result.setDataChange(true);
            if (result.getRs() != 1 || result.getData() == null) {
                return result;
            }
            this.db.saveJsonByKey(key, json);
            return result;
        } catch (Exception e) {
            return result;
        }
    }
}
