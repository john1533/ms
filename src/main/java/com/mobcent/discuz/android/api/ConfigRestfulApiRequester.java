package com.mobcent.discuz.android.api;

import android.content.Context;
import android.text.TextUtils;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.HashMap;

public class ConfigRestfulApiRequester extends BaseDiscuzApiRequester {
    public static String getConfig(Context context, boolean isTimeOut) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_request_config_url");
        if (TextUtils.isEmpty(SharedPreferencesDB.getInstance(context.getApplicationContext()).getConfig())) {
            params.put("connection_timeout", "5000");
            params.put("socket_timeout", "10000");
        } else if (isTimeOut) {
            params.put("connection_timeout", "2000");
            params.put("socket_timeout", "5000");
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getModule(Context context, long moduleId) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_request_config_query_module_url");
        params.put("moduleId", new StringBuilder(String.valueOf(moduleId)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
