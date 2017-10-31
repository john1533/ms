package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCResource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class LocationRestfulApiRequester extends BaseDiscuzApiRequester {
    public static String saveLocation(Context context, double longitude, double latitude, String location, long userId) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_common_location");
        HashMap<String, String> params = new HashMap();
        params.put("longitude", new StringBuilder(String.valueOf(longitude)).toString());
        params.put("latitude", new StringBuilder(String.valueOf(latitude)).toString());
        if (location != null) {
            try {
                params.put("location", URLEncoder.encode(location, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            params.put("location", location);
        }
        params.put("userId", new StringBuilder(String.valueOf(userId)).toString());
        params.put("connection_timeout", "1000");
        params.put("socket_timeout", "2000");
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
