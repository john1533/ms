package com.mobcent.lowest.module.plaza.api;

import android.content.Context;
import com.mobcent.lowest.base.api.BaseLowestApiRequester;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.plaza.api.constant.SearchApiConstant;
import com.mobcent.lowest.module.plaza.config.PlazaConfig;
import java.util.HashMap;

public class SearchRestfulApiRequester extends BaseLowestApiRequester implements SearchApiConstant {
    private static String urlString = null;

    public static String getSearchList(Context context, int forumId, String forumKey, long userId, int baikeType, int searchMode, String keyWord, int page, int pageSize) {
        if (PlazaConfig.BASE_SEARCH_REQUEST_URL == null) {
            PlazaConfig.BASE_SEARCH_REQUEST_URL = context.getString(MCResource.getInstance(context).getStringId("mc_plaza_search_base_request_url"));
        }
        urlString = PlazaConfig.BASE_SEARCH_REQUEST_URL + "search/searchKeyword.do";
        HashMap<String, String> params = new HashMap();
        params.put("forumId", new StringBuilder(String.valueOf(forumId)).toString());
        params.put("forumKey", new StringBuilder(String.valueOf(forumKey)).toString());
        params.put("baikeType", new StringBuilder(String.valueOf(baikeType)).toString());
        params.put(SearchApiConstant.PARAM_SEARCH_MODE, new StringBuilder(String.valueOf(searchMode)).toString());
        params.put("keyword", new StringBuilder(String.valueOf(keyWord)).toString());
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseLowestApiRequester.doPostRequest(urlString, params, context);
    }
}
