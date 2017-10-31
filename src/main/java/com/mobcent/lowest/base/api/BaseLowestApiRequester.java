package com.mobcent.lowest.base.api;

import android.content.Context;
import com.mobcent.lowest.base.constant.BaseRestfulApiConstant;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCHttpClientUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.HashMap;

public class BaseLowestApiRequester implements BaseRestfulApiConstant {
    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        String accessToken = LowestManager.getInstance().getConfig().getAccessToken();
        String accessSecret = LowestManager.getInstance().getConfig().getAccessSecret();
        String sdkVersion = LowestManager.getInstance().getConfig().getSDKVersion();
        long userId = LowestManager.getInstance().getConfig().getUserId();
        String forumKey = LowestManager.getInstance().getConfig().getForumKey();
        params.put("forumId", LowestManager.getInstance().getConfig().getForumId());
        params.put("forumKey", forumKey);
        if (!MCStringUtil.isEmpty(accessToken)) {
            params.put("accessToken", accessToken);
        }
        if (!MCStringUtil.isEmpty(accessSecret)) {
            params.put("accessSecret", accessSecret);
        }
        if (userId != 0) {
            params.put("userId", new StringBuilder(String.valueOf(userId)).toString());
        }
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        String packageName = context.getPackageName();
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put("sdkType", "");
        params.put("sdkVersion", sdkVersion);
        params.put("platType", "1");
        return MCHttpClientUtil.doPostRequest(urlString, params, context);
    }

    public static String doPostRequestNoUserInfo(String urlString, HashMap<String, String> params, Context context) {
        String sdkVersion = LowestManager.getInstance().getConfig().getSDKVersion();
        String forumKey = LowestManager.getInstance().getConfig().getForumKey();
        params.put("forumId", LowestManager.getInstance().getConfig().getForumId());
        params.put("forumKey", forumKey);
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        String packageName = context.getPackageName();
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put("sdkType", "");
        params.put("sdkVersion", sdkVersion);
        params.put("platType", "1");
        return MCHttpClientUtil.doPostRequest(urlString, params, context);
    }
}
