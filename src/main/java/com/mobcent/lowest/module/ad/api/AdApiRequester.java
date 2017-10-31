package com.mobcent.lowest.module.ad.api;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCHttpClientUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import java.util.HashMap;
import org.apache.http.client.methods.HttpGet;

public class AdApiRequester extends BaseAdApiRequester {
    private static String TAG = "AdApiRequester";
    private static String urlString;

    public static String haveAd(Context context) {
        urlString = BASE_URL + "clientapi/m/haveAd.do";
        return BaseAdApiRequester.doPostRequest(urlString, new HashMap(), context);
    }

    public static String getAd(Context context) {
        urlString = BASE_URL + "clientapi/m/getAd.do";
        HashMap<String, String> params = new HashMap();
        params.put(AdApiConstant.PO, "99");
        return BaseAdApiRequester.doPostRequest(urlString, params, context);
    }

    public static String getSplashAd(Context context) {
        urlString = BASE_URL + "clientapi/m/getAd.do";
        HashMap<String, String> params = new HashMap();
        params.put(AdApiConstant.PO, "100");
        return BaseAdApiRequester.doPostRequest(urlString, params, context);
    }

    public static String getLinkDoUrl(Context context, long aid, long po, String du) {
        urlString = BASE_URL + "clientapi/m/linkAd.do?";
        urlString = BaseAdApiRequester.createGetUrl(urlString, aid, po, du, context);
        return urlString;
    }

    public static String downAd(Context context, long aid, int po, String app, String date) {
        urlString = BASE_URL + "clientapi/m/downAd.do";
        HashMap<String, String> params = new HashMap();
        params.put("aid", new StringBuilder(String.valueOf(aid)).toString());
        params.put(AdApiConstant.PO, new StringBuilder(String.valueOf(po)).toString());
        params.put(AdApiConstant.APP_PN, app);
        params.put("date", date);
        return BaseAdApiRequester.doPostRequest(urlString, params, context);
    }

    public static String activeAd(Context context, long aid, int po, String app, String date) {
        urlString = BASE_URL + "clientapi/m/activeAd.do";
        HashMap<String, String> params = new HashMap();
        params.put("aid", new StringBuilder(String.valueOf(aid)).toString());
        params.put(AdApiConstant.PO, new StringBuilder(String.valueOf(po)).toString());
        params.put(AdApiConstant.APP_PN, app);
        params.put("date", date);
        return BaseAdApiRequester.doPostRequest(urlString, params, context);
    }

    public static void doGetRequest(Context context, String url) {
        try {
            MCLogUtil.i(TAG, "[" + MCHttpClientUtil.getNewHttpClient(context, -1, -1).execute(new HttpGet(url)).getStatusLine().getStatusCode() + "]" + url);
        } catch (Exception e) {
            e.printStackTrace();
            MCLogUtil.e(TAG, e.toString());
        }
    }
}
