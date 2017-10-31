package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.SettingConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.service.impl.helper.SettingServiceImplHelper;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class SettingRestfulApiRequester extends BaseDiscuzApiRequester implements SettingConstant {
    public static String getSettingContent(Context context, boolean isTimeOut) {
        String url = MCResource.getInstance(context).getString("mc_forum_user_post_or_topic_getsetting");
        HashMap<String, String> params = new HashMap();
        try {
            params.put(SettingConstant.GETSETTING, URLEncoder.encode(SettingServiceImplHelper.getSettingJsonStr(String.valueOf(0)), "utf-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (MCStringUtil.isEmpty(SharedPreferencesDB.getInstance(context.getApplicationContext()).getSettingStr())) {
            params.put("connection_timeout", "3000");
            params.put("socket_timeout", "7000");
        } else if (isTimeOut) {
            params.put("connection_timeout", "1000");
            params.put("socket_timeout", "3000");
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
