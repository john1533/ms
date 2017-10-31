package com.mobcent.lowest.module.ad.api;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCHttpClientUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.ad.model.RequestParamsModel;
import com.mobcent.lowest.module.ad.utils.AdNeedInfoUtils;
import java.util.HashMap;

public class BaseAdApiRequester implements AdApiConstant, AdConstant {
    public static String BASE_URL = "http://adapi.mobcent.com/";
    public static String TAG = "BaseAdApiRequester";

    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        RequestParamsModel requestParams = AdManager.getInstance().getRequestParams();
        if (requestParams == null) {
            return "{}";
        }
        params.put(AdApiConstant.IM, requestParams.getIm());
        params.put(AdApiConstant.PT, requestParams.getPt());
        params.put(AdApiConstant.SS, requestParams.getSs());
        params.put("ua", requestParams.getUa());
        params.put(AdApiConstant.ZO, requestParams.getZo());
        params.put(AdApiConstant.MC, requestParams.getMc());
        params.put(AdApiConstant.PN, requestParams.getPn());
        params.put("ak", requestParams.getAk());
        params.put("uid", new StringBuilder(String.valueOf(requestParams.getUid())).toString());
        params.put(AdApiConstant.CHANNEL, requestParams.getCh());
        params.put(AdApiConstant.NW, AdNeedInfoUtils.getNetworkType(context));
        params.put(AdApiConstant.JWD, AdNeedInfoUtils.getJWD(context));
        params.put(AdApiConstant.LO, AdNeedInfoUtils.getLocation(context));
        params.put(AdApiConstant.NET, AdNeedInfoUtils.getNet(context));
        params.put(AdApiConstant.SD, new StringBuilder(String.valueOf(AdNeedInfoUtils.getSd())).toString());
        params.put(AdApiConstant.VER, "8");
        params.put(AdApiConstant.USA, requestParams.getUserAgent());
        return MCHttpClientUtil.doPostRequest(urlString, params, context);
    }

    public static String createGetUrl(String urlString, long aid, long po, String du, Context context) {
        RequestParamsModel requestParams = AdManager.getInstance().getRequestParams();
        if (requestParams == null) {
            return urlString;
        }
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(urlString)).append(createParamsStr(AdApiConstant.IM, requestParams.getIm())).toString())).append(createParamsStr(AdApiConstant.PT, requestParams.getPt())).toString())).append(createParamsStr(AdApiConstant.SS, requestParams.getSs())).toString())).append(createParamsStr("ua", requestParams.getUa())).toString())).append(createParamsStr(AdApiConstant.ZO, requestParams.getZo())).toString())).append(createParamsStr(AdApiConstant.MC, requestParams.getMc())).toString())).append(createParamsStr(AdApiConstant.PN, requestParams.getPn())).toString())).append(createParamsStr("ak", requestParams.getAk())).toString())).append(createParamsStr("uid", new StringBuilder(String.valueOf(requestParams.getUid())).toString())).toString())).append(createParamsStr(AdApiConstant.CHANNEL, requestParams.getCh())).toString())).append(createParamsStr(AdApiConstant.NW, AdNeedInfoUtils.getNetworkType(context))).toString())).append(createParamsStr(AdApiConstant.JWD, AdNeedInfoUtils.getJWD(context))).toString())).append(createParamsStr(AdApiConstant.LO, AdNeedInfoUtils.getLocation(context))).toString())).append(createParamsStr(AdApiConstant.NET, AdNeedInfoUtils.getNet(context))).toString())).append(createParamsStr(AdApiConstant.SD, new StringBuilder(String.valueOf(AdNeedInfoUtils.getSd())).toString())).toString())).append(createParamsStr(AdApiConstant.PO, new StringBuilder(String.valueOf(po)).toString())).toString())).append(createParamsStr("aid", new StringBuilder(String.valueOf(aid)).toString())).toString())).append(createParamsStr("du", du)).toString())).append(createParamsStr(AdApiConstant.VER, "8")).toString();
    }

    public static String createParamsStr(String key, String value) {
        return new StringBuilder(String.valueOf(key)).append("=").append(value).append("&").toString();
    }
}
