package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.MsgConstant;
import com.mobcent.lowest.base.utils.MCResource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class MsgRestfulApiRequester extends BaseDiscuzApiRequester implements MsgConstant {
    public static String getMsgUserList(Context context, String paramJson) {
        String url = MCResource.getInstance(context).getString("mc_forum_msg_list");
        HashMap<String, String> params = new HashMap();
        params.put("json", paramJson);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getCommentAtList(Context context, String type, int page, int pageSize) {
        String url = MCResource.getInstance(context).getString("mc_forum_posts_notices");
        HashMap<String, String> params = new HashMap();
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("type", type);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getPmList(Context context, String pmListStr) {
        String url = MCResource.getInstance(context).getString("mc_forum_heart_msg");
        HashMap<String, String> params = new HashMap();
        params.put(MsgConstant.PARAM_PMLIST, pmListStr);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String sendMsg(Context context, String paramJson) {
        String url = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_msg_reply")).toString();
        HashMap<String, String> params = new HashMap();
        try {
            params.put("json", URLEncoder.encode(paramJson, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
