package com.mobcent.lowest.module.ad.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.api.AdApiRequester;
import com.mobcent.lowest.module.ad.cache.AdDatasCache;
import com.mobcent.lowest.module.ad.cache.AdPositionsCache;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.service.AdService;
import com.mobcent.lowest.module.ad.service.impl.helper.AdServiceImplHelper;
import java.util.Calendar;

public class AdServiceImpl implements AdService {
    private String SHARED_PRE_NAME = "mc_ad.prefs";
    public String TAG = "AdServiceImpl";
    private Context context;

    public AdServiceImpl(Context context) {
        this.context = context;
    }

    public AdPositionsCache haveAd() {
        String jsonStr = AdApiRequester.haveAd(this.context);
        if (!jsonStr.equals("connection_fail")) {
            return AdServiceImplHelper.parseAdPositions(jsonStr);
        }
        AdPositionsCache ac = new AdPositionsCache();
        ac.setConectFailed(true);
        return ac;
    }

    public AdDatasCache getAd() {
        return AdServiceImplHelper.parseAdDatas(AdApiRequester.getAd(this.context));
    }

    public String getLinkUrl(long aid, long po, String du) {
        return AdApiRequester.getLinkDoUrl(this.context, aid, po, du);
    }

    public boolean downAdDo(long aid, int po, String app, String date) {
        return AdServiceImplHelper.parseSucc(AdApiRequester.downAd(this.context, aid, po, app, date));
    }

    public boolean activeAdDo(long aid, int po, String app, String date) {
        return AdServiceImplHelper.parseSucc(AdApiRequester.activeAd(this.context, aid, po, app, date));
    }

    public void exposureAd(String url) {
        AdApiRequester.doGetRequest(this.context, url);
    }

    public AdModel getSplashAd() {
        if (getTodaySplashAdModel() != null) {
            return getTodaySplashAdModel();
        }
        AdModel adModel = AdServiceImplHelper.parseSplashAdModel(AdApiRequester.getSplashAd(this.context));
        if (adModel == null) {
            return adModel;
        }
        setTodaySplashAdModel(adModel);
        return adModel;
    }

    public AdModel getTodaySplashAdModel() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(this.SHARED_PRE_NAME, 3);
        String dateCache = sharedPreferences.getString(AdConstant.TODAY_DATE, "");
        String imgUrl = sharedPreferences.getString(AdConstant.SPLASH_IMGURL, "");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        int month = calendar.get(2);
        if (!dateCache.equals(new StringBuilder(String.valueOf(year)).append("-").append(month).append("-").append(calendar.get(5)).toString()) || imgUrl.equals("")) {
            return null;
        }
        AdModel adModel = new AdModel();
        adModel.setPu(imgUrl);
        return adModel;
    }

    public void setTodaySplashAdModel(AdModel adModel) {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(this.SHARED_PRE_NAME, 3);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(1);
        int month = calendar.get(2);
        String date = new StringBuilder(String.valueOf(year)).append("-").append(month).append("-").append(calendar.get(5)).toString();
        if (adModel != null && !MCStringUtil.isEmpty(adModel.getPu())) {
            sharedPreferences.edit().putString(AdConstant.TODAY_DATE, date).commit();
            sharedPreferences.edit().putString(AdConstant.SPLASH_IMGURL, adModel.getPu()).commit();
        }
    }
}
