package com.mobcent.lowest.module.game.api;

import android.content.Context;
import com.mobcent.lowest.base.api.BaseLowestApiRequester;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import java.util.HashMap;

public class GameApiRequester extends BaseLowestApiRequester implements GameApiConstant {
    public static String TAG = "GameApiRequester";

    public static String getRecommendGames(Context context, int page, int pageSize) {
        String urlString = getBaseUrl(context) + "game/getRecommendGames";
        HashMap<String, String> params = new HashMap();
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseLowestApiRequester.doPostRequest(urlString, params, context);
    }

    public static String getLatestGames(Context context, int page, int pageSize) {
        String urlString = getBaseUrl(context) + "game/getLatestGames";
        HashMap<String, String> params = new HashMap();
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseLowestApiRequester.doPostRequest(urlString, params, context);
    }

    public static String getPostsByDesc(Context context, long boardId, long topicId, int page, int pageSize) {
        String url = getBaseUrl(context) + "forum/reversePosts.do";
        HashMap<String, String> params = new HashMap();
        if (boardId > 0) {
            params.put("boardId", new StringBuilder(String.valueOf(boardId)).toString());
        }
        params.put("topicId", new StringBuilder(String.valueOf(topicId)).toString());
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseLowestApiRequester.doPostRequest(url, params, context);
    }

    public static String replyGame(Context context, String rTitle, String rContent, long topicId, boolean isQuote, long longitude, long latitude, String location, int r, int isAnnounce) {
        String url = getBaseUrl(context) + "forum/replyTo.do";
        HashMap<String, String> params = new HashMap();
        if (!MCStringUtil.isEmpty(rTitle)) {
            params.put(GameApiConstant.R_TITLE, rTitle);
        }
        params.put(GameApiConstant.R_CONTENT, rContent);
        params.put("topicId", new StringBuilder(String.valueOf(topicId)).toString());
        params.put("isQuote", new StringBuilder(String.valueOf(isQuote)).toString());
        params.put("toReplyId", "");
        params.put("longitude", new StringBuilder(String.valueOf(longitude)).toString());
        params.put("latitude", new StringBuilder(String.valueOf(latitude)).toString());
        params.put("location", location);
        if (r == 1) {
            params.put("r", new StringBuilder(String.valueOf(r)).toString());
        }
        params.put(GameApiConstant.PARAM_IS_ANNOUNCE, new StringBuilder(String.valueOf(isAnnounce)).toString());
        return BaseLowestApiRequester.doPostRequest(url, params, context);
    }

    public static String getBaseUrl(Context context) {
        return MCResource.getInstance(context).getString("mc_game_request_url");
    }
}
