package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.SettingRestfulApiRequester;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.service.SettingService;
import com.mobcent.discuz.android.service.impl.helper.SettingServiceImplHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;

public class SettingServiceImpl implements SettingService {
    private Context context;

    public SettingServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel<SettingModel> getSettingContent(boolean isTimeOut) {
        String jsonStr = SettingRestfulApiRequester.getSettingContent(this.context, isTimeOut);
        BaseResultModel<SettingModel> baseResultModel = SettingServiceImplHelper.getSettingContent(this.context, jsonStr);
        SharedPreferencesDB sDb = SharedPreferencesDB.getInstance(this.context.getApplicationContext());
        if (baseResultModel.getRs() == 1) {
            sDb.saveSettingStr(jsonStr);
            return baseResultModel;
        } else if (MCStringUtil.isEmpty(sDb.getSettingStr())) {
            return baseResultModel;
        } else {
            baseResultModel.setRs(1);
            return SettingServiceImplHelper.getSettingContent(this.context, sDb.getSettingStr());
        }
    }

    public BaseResultModel<SettingModel> getSettingByLocal() {
        SharedPreferencesDB sDb = SharedPreferencesDB.getInstance(this.context.getApplicationContext());
//        BaseResultModel<SettingModel> baseResultModel = new BaseResultModel();
        if (MCStringUtil.isEmpty(sDb.getSettingStr())) {
            return getSettingContent(false);
        }
        return SettingServiceImplHelper.getSettingContent(this.context, sDb.getSettingStr());
    }
}
