package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.HashMap;

public class PayStateRestfulApiRequester extends BaseDiscuzApiRequester {
    public static String controll(Context context, String forumKey) {
        String url = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_forum_controll_url"))).append("pay/payState.do").toString();
        HashMap<String, String> params = new HashMap();
        params.put("forumKey", forumKey);
        if (!SharedPreferencesDB.getInstance(context).isPayStateExist()) {
            params.put("connection_timeout", "2000");
            params.put("socket_timeout", "5000");
        }
        params.put(BaseApiConstant.IS_REQUEST_NO_TOKEN, "1");
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
