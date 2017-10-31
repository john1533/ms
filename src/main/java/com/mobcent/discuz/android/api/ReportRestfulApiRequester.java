package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.ReportConstant;
import com.mobcent.lowest.base.utils.MCResource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ReportRestfulApiRequester extends BaseDiscuzApiRequester implements ReportConstant {
    public static String report(Context context, long id, String reason, String idType) {
        String url = MCResource.getInstance(context).getString("mc_forum_post_report_user");
        HashMap<String, String> params = new HashMap();
        params.put("id", new StringBuilder(String.valueOf(id)).toString());
        try {
            params.put("message", URLEncoder.encode(reason, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("idType", idType);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
