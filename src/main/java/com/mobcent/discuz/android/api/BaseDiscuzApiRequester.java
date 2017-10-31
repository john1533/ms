package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.api.util.DZHttpClientUtil;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.File;
import java.util.HashMap;

public class BaseDiscuzApiRequester implements BaseApiConstant {
    public static String doPostRequest(Context context, String url, HashMap<String, String> params) {
        SharedPreferencesDB shareDB = SharedPreferencesDB.getInstance(context);
        String forumKey = shareDB.getForumKey();
        String accessToken = shareDB.getAccessToken();
        String accessSecret = shareDB.getAccessSecret();
        if (forumKey != null) {
            params.put("forumKey", forumKey);
        }
        boolean isPayed = params.get(BaseApiConstant.IS_REQUEST_NO_TOKEN) != null;
        if (!(accessToken == null || isPayed)) {
            params.put("accessToken", accessToken);
        }
        if (!(accessSecret == null || isPayed)) {
            params.put("accessSecret", accessSecret);
        }
        if (isPayed) {
            params.remove(BaseApiConstant.IS_REQUEST_NO_TOKEN);
        }
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        params.put(BaseApiConstant.APPHASH, MCStringUtil.stringToMD5(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString().substring(0, 5))).append(BaseApiConstant.AUTHKEY).toString()).substring(8, 16));
        String packageName = context.getPackageName();
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put("sdkType", "");
        params.put(BaseApiConstant.FORUM_TYPE, "7");
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        params.put("platType", "1");
        return DZHttpClientUtil.doPostRequest(url, params, context);
    }

    public static String uploadFile(String urlString, String uploadFile, Context ctx, long boardId, String type, String json, String module) {
        SharedPreferencesDB shareDB = SharedPreferencesDB.getInstance(ctx.getApplicationContext());
        return DZHttpClientUtil.uploadFile(urlString, new File(uploadFile), uploadFile, shareDB.getAccessToken(), shareDB.getAccessSecret(), json, type, module, shareDB.getForumKey());
    }
}
