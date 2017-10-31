package com.mobcent.lowest.module.place.api;

import android.content.Context;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.constant.BasePlaceApiConstant;
import com.mobcent.lowest.module.place.model.PlaceApiFilterModel;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class AroundApiRequester extends BasePlaceApiRequester {
    private static String urlString = null;

    public static String poiSearch(Context context, String query, String tag, int scope, String regoin, String location, int radius, int pageSize, int pageNum, PlaceApiFilterModel filter) {
        HashMap<String, String> params = new HashMap();
        String tempUrl = MCResource.getInstance(context).getString("mc_place_poi_request_url");
        if (MCStringUtil.isEmpty(tempUrl)) {
            tempUrl = BasePlaceApiRequester.POI_SEARCH;
        }
        urlString = tempUrl;
        params.put("forumKey", LowestManager.getInstance().getConfig().getForumKey());
        params.put("query", encode(query));
        if (!MCStringUtil.isEmpty(tag)) {
            params.put("tag", encode(tag));
        }
        params.put("scope", new StringBuilder(String.valueOf(scope)).toString());
        params.put("region", encode(regoin));
        params.put(BasePlaceApiConstant.REQ_PAGE_SIZE, new StringBuilder(String.valueOf(pageSize)).toString());
        params.put(BasePlaceApiConstant.REQ_PAGE_NUM, new StringBuilder(String.valueOf(pageNum)).toString());
        if (filter != null) {
            params.put("filter", filter.getFilterString());
        }
        if (!MCStringUtil.isEmpty(location)) {
            params.put("location", location);
        }
        if (radius != 0) {
            params.put("radius", new StringBuilder(String.valueOf(radius)).toString());
        }
        return BasePlaceApiRequester.doGetRequest(urlString, params, context);
    }

    public static String queryAreaByCityCode(Context context, String areaCode, int businessFlag) {
        HashMap<String, String> params = new HashMap();
        urlString = BasePlaceApiRequester.SHANG_QUAN_URL;
        params.put(BasePlaceApiConstant.REQ_QT, "sub_area_list");
        params.put("level", "1");
        params.put(BasePlaceApiConstant.REQ_AREACODE, areaCode);
        params.put(BasePlaceApiConstant.REQ_BUSINESS_FLAG, new StringBuilder(String.valueOf(businessFlag)).toString());
        return BasePlaceApiRequester.doGetRequest(urlString, params, context);
    }

    public static String encode(String param) {
        if (!MCStringUtil.isEmpty(param)) {
            try {
                param = URLEncoder.encode(param, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return param;
    }

    public static String encodeWithSpace(String param) {
        if (!MCStringUtil.isEmpty(param)) {
            try {
                return URLEncoder.encode(param, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
