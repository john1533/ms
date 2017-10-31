package com.mobcent.lowest.module.place.api;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCHttpClientUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.ad.model.RequestParamsModel;
import com.mobcent.lowest.module.place.api.constant.BasePlaceApiConstant;
import java.util.HashMap;

public class BasePlaceApiRequester implements BasePlaceApiConstant {
    public static String AK = "BkEE7TlvAMxKIxUnilFFg8Fh";
    public static final String EVENT_DETAIL = "http://api.map.baidu.com/place/v2/eventdetail";
    public static final String EVENT_SEARCH = "http://api.map.baidu.com/place/v2/eventsearch";
    public static final String POI_DETAIL = "http://api.map.baidu.com/place/v2/detail";
    public static final String POI_SEARCH = "http://api.map.baidu.com/place/v2/search";
    public static final String SHANG_QUAN_URL = "http://api.map.baidu.com/shangquan/forward/";
    public static String TAG = "BasePlaceApiRequester";
    private static String outPut = "json";

    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        params.put(BasePlaceApiConstant.REQ_OUTPUT, outPut);
        params.put("ak", AK);
        return MCHttpClientUtil.doPostRequest(urlString, params, context);
    }

    public static String doGetRequest(String urlString, HashMap<String, String> params, Context context) {
        params.put(BasePlaceApiConstant.REQ_OUTPUT, outPut);
        params.put("ak", AK);
        RequestParamsModel requestModel = AdManager.getInstance().getRequestParams();
        if (!(requestModel == null || MCStringUtil.isEmpty(requestModel.getAk()))) {
            params.put("forumKey", requestModel.getAk());
        }
        return PlaceHttpClientUtil.executeHttpGet(urlString, params);
    }

    public static void setOutPut(String outPut) {
        outPut = outPut;
    }
}
