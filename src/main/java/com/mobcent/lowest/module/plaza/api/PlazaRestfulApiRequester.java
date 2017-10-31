package com.mobcent.lowest.module.plaza.api;

import android.content.Context;
import com.mobcent.lowest.module.ad.api.BaseAdApiRequester;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.lowest.module.plaza.api.constant.PlazaApiConstant;
import java.util.HashMap;

public class PlazaRestfulApiRequester extends BaseAdApiRequester implements PlazaApiConstant, AdApiConstant {
    static String urlString = null;

    public static String getPlazaAppModelList(Context context, String appKey, long userId, long ut) {
        urlString = BASE_URL + "clientapi/m/getSquare.do";
        HashMap<String, String> params = new HashMap();
        params.put("ak", appKey);
        params.put(AdApiConstant.PO, "7001");
        params.put("uid", new StringBuilder(String.valueOf(userId)).toString());
        params.put(PlazaApiConstant.UT, new StringBuilder(String.valueOf(ut)).toString());
        return BaseAdApiRequester.doPostRequest(urlString, params, context);
    }

    public static String getPlazaLinkUrl(Context context, String appKey, long mid, long userId) {
        return new StringBuilder(String.valueOf(BaseAdApiRequester.createGetUrl(BASE_URL + "clientapi/m/linkModule.do?", 0, 7001, null, context))).append(BaseAdApiRequester.createParamsStr("mid", new StringBuilder(String.valueOf(mid)).toString())).toString();
    }
}
