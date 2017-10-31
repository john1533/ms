package com.mobcent.android.api;

import android.content.Context;
import com.mobcent.android.constant.MCShareApiConstant;
import com.mobcent.android.utils.PhoneUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.HashMap;
import java.util.Locale;

public class MCShareRestfulApiRequester implements MCShareApiConstant {
    public static String doPostRequest(String urlString, HashMap<String, String> params, String appKey, Context context) {
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        MCLogUtil.e("MCShareRestfulApiRequester", "PhoneUtil.getIMSI(context)=" + MCPhoneUtil.getIMSI(context));
        params.put("packageName", context.getPackageName());
        params.put("appKey", appKey);
        if (params.get("lan") == null) {
            params.put("lan", Locale.getDefault().getLanguage());
        }
        if (params.get("cty") == null) {
            params.put("cty", context.getResources().getConfiguration().locale.getCountry());
        }
        params.put("version", MCPhoneUtil.getSDKVersion());
        params.put("ua", MCPhoneUtil.getPhoneType());
        params.put("platType", "1");
        return MCShareHttpClientUtil.doPostRequest(urlString, params, context);
    }

    public static String getAllSites(String appKey, String domainUrl, String lan, String cty, Context context) {
        String url = new StringBuilder(String.valueOf(domainUrl)).append("share/weibo/wbList.do").toString();
        HashMap<String, String> params = new HashMap();
        params.put("lan", lan);
        params.put("cty", cty.replaceAll("_", ""));
        params.put(MCShareApiConstant.MAC, PhoneUtil.getMacAddress(context));
        return doPostRequest(url, params, appKey, context);
    }

    public static String unbindSite(long userId, int siteId, String appKey, String domainUrl, Context context) {
        String url = new StringBuilder(String.valueOf(domainUrl)).append("share/wb/ub.do").toString();
        HashMap<String, String> params = new HashMap();
        params.put("userId", new StringBuilder(String.valueOf(userId)).toString());
        params.put(MCShareApiConstant.MARK, new StringBuilder(String.valueOf(siteId)).toString());
        return doPostRequest(url, params, appKey, context);
    }

    public static String uploadShareImage(long userId, String uploadFile, String appKey, String domainUrl, Context context) {
        return MCShareHttpClientUtil.uploadFile(new StringBuilder(String.valueOf(domainUrl)).append("sdk/action/upload.do?").append("userId").append("=").append(userId).append("&").append("packageName").append("=").append(context.getPackageName()).append("&").append("appKey").append("=").append(appKey).append("&").append(MCShareApiConstant.GZIP).append("=false").toString(), uploadFile, context);
    }

    public static String shareInfo(long userId, String content, String picPath, String ids, String shareUrl, String appKey, String domainUrl, Context context) {
        String url = new StringBuilder(String.valueOf(domainUrl)).append("share/weibo/shareTo.do").toString();
        HashMap<String, String> params = new HashMap();
        params.put("content", content);
        params.put("picPath", picPath);
        params.put("userId", new StringBuilder(String.valueOf(userId)).toString());
        params.put("ids", ids);
        String str = "shareUrl";
        if (shareUrl == null) {
            shareUrl = "";
        }
        params.put(str, shareUrl);
        return doPostRequest(url, params, appKey, context);
    }

    public static String shareLog(String appKey, String openPlatType, String domainUrl, Context context) {
        String url = new StringBuilder(String.valueOf(domainUrl)).append("imsdk/share/shareLog.do").toString();
        HashMap<String, String> params = new HashMap();
        params.put("forumKey", appKey);
        params.put(MCShareApiConstant.OPEN_PLAT_TYPE, openPlatType);
        return doPostRequest(url, params, appKey, context);
    }
}
