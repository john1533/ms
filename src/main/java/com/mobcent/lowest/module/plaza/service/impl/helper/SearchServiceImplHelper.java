package com.mobcent.lowest.module.plaza.service.impl.helper;

import com.mobcent.lowest.module.plaza.api.constant.SearchApiConstant;
import com.mobcent.lowest.module.plaza.model.SearchModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchServiceImplHelper implements SearchApiConstant {
    public static List<SearchModel> parseSearchList(String jsonStr) {
        List<SearchModel> searchList = new ArrayList();
        try {
            JSONObject jsonRoot = new JSONObject(jsonStr);
            if (jsonRoot.optInt("rs") == 0) {
                return null;
            }
            int hasNext = jsonRoot.optInt("has_next", 0);
            int totalNum = jsonRoot.optInt("total_num", 0);
            String baseUrl = jsonRoot.optString("img_url");
            JSONArray listArray = jsonRoot.optJSONArray("list");
            for (int i = 0; i < listArray.length(); i++) {
                SearchModel searchModel = new SearchModel();
                JSONObject json = listArray.getJSONObject(i);
                searchModel.setBaseUrl(baseUrl);
                searchModel.setSummary(json.optString("summary"));
                searchModel.setLastUpdateTime(json.optLong("last_reply_date"));
                searchModel.setStatus(json.optInt("status", 0));
                searchModel.setPicPath(json.optString("pic_path"));
                searchModel.setSinger(json.optString(SearchApiConstant.SINGER));
                searchModel.setBaikeType(json.optInt(SearchApiConstant.BAIKE_TYPE));
                searchModel.setBoardId(json.optLong("board_id"));
                searchModel.setTitle(json.optString("title"));
                searchModel.setRatio((float) json.optDouble("ratio"));
                searchModel.setForumId(json.optLong(SearchApiConstant.FORUM_ID));
                searchModel.setTop(json.optInt(SearchApiConstant.IS_TOP) != 0);
                searchModel.setHot(json.optInt(SearchApiConstant.IS_HOT) != 0);
                searchModel.setUserNickName(json.optString("user_nick_name"));
                searchModel.setUserId(json.optLong("user_id"));
                searchModel.setTopicId(json.optLong("topic_id"));
                searchModel.setEssence(json.optInt(SearchApiConstant.IS_ESSENCE) != 0);
                searchModel.setSourceType(json.optInt("source_type"));
                searchModel.setClickUrl(json.optString(SearchApiConstant.CLICK_URL));
                searchModel.setFavor(json.optInt("is_favor") != 0);
                searchModel.setNumIid(json.optLong(SearchApiConstant.NUM_IID));
                searchList.add(searchModel);
            }
            if (searchList.size() == 0) {
                return searchList;
            }
            ((SearchModel) searchList.get(0)).setTotalNum(totalNum);
            ((SearchModel) searchList.get(0)).setHasNext(hasNext != 0);
            return searchList;
        } catch (Exception e) {
            return null;
        }
    }
}
