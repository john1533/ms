package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.ArticleConstant;
import com.mobcent.lowest.base.utils.MCResource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class ArticleRestfulApiRequester extends BaseDiscuzApiRequester implements ArticleConstant {
    public static String getArticleDetail(Context context, String json) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_request_article_info");
        try {
            params.put("json", URLEncoder.encode(json, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String commentArticle(Context context, String json) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_article_comment");
        try {
            params.put("json", URLEncoder.encode(json, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getArtileCommentList(Context context, String json) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_artile_comment_list");
        try {
            params.put("json", URLEncoder.encode(json, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
