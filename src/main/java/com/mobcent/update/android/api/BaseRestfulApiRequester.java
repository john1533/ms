package com.mobcent.update.android.api;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.mobcent.update.android.api.util.HttpClientUtil;
import com.mobcent.update.android.constant.MCUpdateConstant;
import java.util.HashMap;

public abstract class BaseRestfulApiRequester implements MCUpdateConstant {
    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        try {
            String forumKey = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("mc_forum_key");
            int version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            if (forumKey != null) {
                params.put("forumKey", forumKey);
            }
            if (version != 0) {
                params.put(MCUpdateConstant.VERSION_CODE, new StringBuilder(String.valueOf(version)).toString());
            }
            params.put("platType", "1");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return HttpClientUtil.doPostRequest(urlString, params, context);
    }
}
