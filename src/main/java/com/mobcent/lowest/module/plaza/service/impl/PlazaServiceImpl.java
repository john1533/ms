package com.mobcent.lowest.module.plaza.service.impl;

import android.content.Context;
import com.mobcent.lowest.base.model.LowestResultModel;
import com.mobcent.lowest.module.plaza.api.PlazaRestfulApiRequester;
import com.mobcent.lowest.module.plaza.api.constant.PlazaApiConstant;
import com.mobcent.lowest.module.plaza.db.PlazaDbHelper;
import com.mobcent.lowest.module.plaza.db.constant.PlazaDbColumnConstant;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.mobcent.lowest.module.plaza.service.PlazaService;
import com.mobcent.lowest.module.plaza.service.impl.helper.PlazaServiceImplHelper;
import java.util.List;

public class PlazaServiceImpl implements PlazaService, PlazaApiConstant, PlazaDbColumnConstant {
    private String SHARED_PRE_NAME = "mc_plaza.prefs";
    private String TAG = "PlazaServiceImpl";
    private Context context;

    public PlazaServiceImpl(Context context) {
        this.context = context;
    }

    public LowestResultModel<List<PlazaAppModel>> getPlazaAppModelListByLocal() {
        return PlazaServiceImplHelper.parsePlazaAppModelList(new PlazaDbHelper(this.context, PlazaDbColumnConstant.PLAZA_DB_NAME).getJsonStr());
    }

    public LowestResultModel<List<PlazaAppModel>> getPlazaAppModelListByNet(String appKey, long userId) {
        String jsonStr = PlazaRestfulApiRequester.getPlazaAppModelList(this.context, appKey, userId, getLocalUt());
        LowestResultModel<List<PlazaAppModel>> result = PlazaServiceImplHelper.parsePlazaAppModelList(jsonStr);
        PlazaDbHelper dbHelper = new PlazaDbHelper(this.context, PlazaDbColumnConstant.PLAZA_DB_NAME);
        if (!(result.getRs() == 0 || result.getRs() == 2)) {
            setLocalUt(result.getUt());
            dbHelper.setJsonStr(jsonStr);
        }
        return result;
    }

    public String getPlazaLinkUrl(String appKey, long mid, long userId) {
        return PlazaRestfulApiRequester.getPlazaLinkUrl(this.context, appKey, mid, userId);
    }

    public long getLocalUt() {
        return this.context.getSharedPreferences(this.SHARED_PRE_NAME, 3).getLong(PlazaApiConstant.UT, 1);
    }

    public void setLocalUt(long ut) {
        this.context.getSharedPreferences(this.SHARED_PRE_NAME, 3).edit().putLong(PlazaApiConstant.UT, ut).commit();
    }
}
