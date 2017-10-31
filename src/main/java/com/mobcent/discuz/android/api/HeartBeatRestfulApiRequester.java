package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.HashMap;

public class HeartBeatRestfulApiRequester extends BaseDiscuzApiRequester {
    public static final String HEART_FORUM_URL = "imsdk/message/heart.do";

    public static String getHeartModel(Context context) {
        String url = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_heart")).toString();
        HashMap<String, String> params = new HashMap();
        params.put("connection_timeout", "1000");
        params.put("socket_timeout", "2000");
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getIMSDKHeartBeat(Context context, long userId) {
        String url = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_forum_imsdk_url"))).append(HEART_FORUM_URL).toString();
        HashMap<String, String> params = new HashMap();
        params.put("userId", new StringBuilder(String.valueOf(userId)).toString());
        String version = new StringBuilder(String.valueOf(Double.parseDouble(SharedPreferencesDB.getInstance(context).getdiscusVersion()) * 1000.0d)).toString();
        params.put(BaseApiConstant.DISCUZ_VERSION, version.substring(0, version.length() - 2));
        params.put("connection_timeout", "1000");
        params.put("socket_timeout", "2000");
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
