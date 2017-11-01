package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.ConfigApiConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentHeaderModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigFastPostModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.model.ConfigNavModel;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigServiceImplHelper implements ConfigApiConstant {
    private static boolean isShowMessageList = false;

    public static BaseResultModel<ConfigModel> getConfigModel(Context context, String json) {
        BaseResultModel<ConfigModel> result = new BaseResultModel();
        try {
            ConfigModel configModel = new ConfigModel();
            BaseJsonHelper.formJsonRs(context, json, result);
            if (result.getRs() == 1) {
                JSONObject bodyObject = new JSONObject(json).optJSONObject("body");
//                JSONObject navigationObject = bodyObject.optJSONObject(ConfigApiConstant.NAVIGATION);//下面的tab页数据
//                configModel.setType(navigationObject.optString("type"));//tab类型："bottom"
//                parseConfigNavList(configModel, navigationObject.optJSONArray(ConfigApiConstant.NAV_ITEM_LIST));//
                parseConfigModelList(configModel, bodyObject.optJSONArray("moduleList"));
                configModel.setShowMessageList(isShowMessageList);
                result.setData(configModel);
            }
        } catch (Exception e) {
            result.setRs(0);
            result.setErrorInfo(MCResource.getInstance(context).getString("mc_forum_json_error"));
            e.printStackTrace();
        }
        return result;
    }

    public static BaseResultModel<ConfigModuleModel> getModuleModel(Context context, String json) {
        BaseResultModel<ConfigModuleModel> result = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, json, result);
            if (result.getRs() == 1) {
                result.setData(parseConfigModel(new JSONObject(json).optJSONObject("body").optJSONObject("module")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setRs(0);
            result.setData(null);
        }
        return result;
    }

    private static List<ConfigComponentModel> parseComponentList(JSONArray jsonArray) {
        List<ConfigComponentModel> moduleModels = null;
        if (jsonArray != null) {
            moduleModels = new ArrayList();
            for (int j = 0; j < jsonArray.length(); j++) {
                ConfigComponentModel model = parseComponentModel(jsonArray.optJSONObject(j));
                if (model != null) {
                    moduleModels.add(model);
                }
            }
        }
        return moduleModels;
    }

    private static ConfigComponentModel parseComponentModel(JSONObject object) {
        if (object == null) {
            return null;
        }
        ConfigComponentModel model = new ConfigComponentModel();
        model.setId(object.optString("id"));
        model.setType(object.optString("type"));
        model.setTitle(object.optString("title"));
        model.setDesc(object.optString("desc"));
        model.setIcon(object.optString("icon"));
        model.setStyle(object.optString("style"));
        model.setIconStyle(object.optString(ConfigApiConstant.ICON_STYLE));
        if (object.has("extParams")) {
            JSONObject paramObj = object.optJSONObject("extParams");
            model.setPadding(paramObj.optString(ConfigApiConstant.PADDING));
            model.setNewsModuleId(paramObj.optLong(ConfigApiConstant.NEWS_MODULE_ID));
            model.setModuleId(paramObj.optLong("moduleId"));
            model.setTopicId(paramObj.optLong("topicId"));
            model.setArticleId(paramObj.optLong(ConfigApiConstant.ARTICLE_ID));
            model.setShowForumIcon(paramObj.optInt(ConfigApiConstant.IS_SHOW_FORUM_ICON) != 0);
            model.setShowForumTwoCols(paramObj.optInt(ConfigApiConstant.IS_SHOW_FORUM_TWO_COLS) != 0);
            model.setForumId((long) paramObj.optInt("forumId"));
            model.setRedirect(paramObj.optString("redirect"));
            model.setShowTopicTitle(paramObj.optInt(ConfigApiConstant.IS_SHOW_TOPIC_TITLE) != 0);
            model.setShowTopicSort(paramObj.optInt(ConfigApiConstant.IS_SHOW_TOPIC_SORT) != 0);
            model.setDefaultTitle(paramObj.optString(ConfigApiConstant.DEFAULT_TITLE));
            model.setFilter(paramObj.optString("filter"));
            model.setOrderby(paramObj.optString(ConfigApiConstant.ORDERBY));
            if (paramObj.has("filterId")) {
                model.setFilterId(paramObj.optInt("filterId"));
            } else {
                model.setFilterId(0);
            }
            if (paramObj.has(ConfigApiConstant.LIST_TITLE_LENGTH)) {
                model.setListTitleLength(paramObj.optInt(ConfigApiConstant.LIST_TITLE_LENGTH));
            } else {
                model.setListTitleLength(10);
            }
            if (paramObj.has(ConfigApiConstant.LIST_SUMMARY_LENGTH)) {
                model.setListSummaryLength(paramObj.optInt(ConfigApiConstant.LIST_SUMMARY_LENGTH));
            } else {
                model.setListSummaryLength(40);
            }
            if (paramObj.has(ConfigApiConstant.LIST_IMAGE_POSITION)) {
                model.setListImagePosition(paramObj.optInt(ConfigApiConstant.LIST_IMAGE_POSITION));
            } else {
                model.setListImagePosition(2);
            }
            if (paramObj.has(ConfigApiConstant.SUB_LIST_STYLE)) {
                model.setSubListStyle(paramObj.optString(ConfigApiConstant.SUB_LIST_STYLE));
            } else {
                model.setSubListStyle(model.getStyle());
            }
            if (paramObj.has(ConfigApiConstant.SUB_DETAIL_VIEW_STYLE)) {
                model.setSubDetailViewStyle(paramObj.optString(ConfigApiConstant.SUB_DETAIL_VIEW_STYLE));
            } else {
                model.setSubDetailViewStyle(model.getStyle());
            }
            boolean isShowMessageList = paramObj.optInt(ConfigApiConstant.IS_SHOW_MESSAGELIST) == 1;
            if (isShowMessageList) {
                isShowMessageList = true;
            }
            model.setShowMessageList(isShowMessageList);
            if (paramObj.has(ConfigApiConstant.FAST_POST_FORUMIDS)) {
                JSONArray fastPostArray = paramObj.optJSONArray(ConfigApiConstant.FAST_POST_FORUMIDS);
                if (fastPostArray != null && fastPostArray.length() > 0) {
                    List<ConfigFastPostModel> fastPostList = new ArrayList();
                    for (int i = 0; i < fastPostArray.length(); i++) {
                        ConfigFastPostModel fastPostModel = new ConfigFastPostModel();
                        JSONObject fastObj = fastPostArray.optJSONObject(i);
                        fastPostModel.setFid(fastObj.optLong("fid"));
                        fastPostModel.setName(fastObj.optString("title"));
                        fastPostList.add(fastPostModel);
                    }
                    model.setFastPostList(fastPostList);
                }
            }
            if (paramObj.has(ConfigApiConstant.STYLE_HEADER)) {
                JSONObject headerObj = paramObj.optJSONObject(ConfigApiConstant.STYLE_HEADER);
                if (headerObj != null) {
                    ConfigComponentHeaderModel header = new ConfigComponentHeaderModel();
                    header.setShowTitle(headerObj.optInt(ConfigApiConstant.IS_SHOW) != 0);
                    header.setTitle(headerObj.optString("title"));
                    header.setPosition(headerObj.optInt("position"));
                    header.setShowMore(headerObj.optInt(ConfigApiConstant.IS_SHOW_MORE) != 0);
                    JSONObject moreComponentObj = headerObj.optJSONObject(ConfigApiConstant.MORE_COMPONENT);
                    if (moreComponentObj != null) {
                        header.setMoreComponent(parseComponentModel(moreComponentObj));
                    }
                    model.setHeaderModel(header);
                }
            }
        }
        model.setComponentList(parseComponentList(object.optJSONArray(ConfigApiConstant.COMPONENT_LIST)));
        return model;
    }

    private static ConfigModuleModel parseConfigModel(JSONObject moduleObj) {
        ConfigModuleModel moduleModel = new ConfigModuleModel();
        if (moduleObj != null) {
            moduleModel.setId((long) moduleObj.optInt("id"));
            moduleModel.setType(moduleObj.optString("type"));
            moduleModel.setTitle(moduleObj.optString("title"));
            moduleModel.setIcon(moduleObj.optString("icon"));
            moduleModel.setStyle(moduleObj.optString("style", "card"));
            moduleModel.setLeftTopbarList(parseComponentList(moduleObj.optJSONArray(ConfigApiConstant.LEFT_TOP_BARS)));
            moduleModel.setRightTopbarList(parseComponentList(moduleObj.optJSONArray(ConfigApiConstant.RIGHT_TOP_BARS)));
            moduleModel.setComponentList(parseComponentList(moduleObj.optJSONArray(ConfigApiConstant.COMPONENT_LIST)));
        }
        return moduleModel;
    }

    private static void parseConfigModelList(ConfigModel configModel, JSONArray moduleArray) {
        if (moduleArray != null && moduleArray.length() > 0) {
            int length = moduleArray.length();
            for (int i = 0; i < length; i++) {
                ConfigModuleModel moduleModel = parseConfigModel(moduleArray.optJSONObject(i));
                configModel.getModuleMap().put(Long.valueOf(moduleModel.getId()), moduleModel);
            }
        }
    }

    private static void parseConfigNavList(ConfigModel configModel, JSONArray navArray) {
        List<ConfigNavModel> navList = null;
        if (navArray != null) {
            navList = new ArrayList();
            for (int i = 0; i < navArray.length(); i++) {
                ConfigNavModel configNavModel = new ConfigNavModel();
                JSONObject navObject = navArray.optJSONObject(i);
                configNavModel.setModuleId(navObject.optLong("moduleId"));
                configNavModel.setTitle(navObject.optString("title"));
                configNavModel.setIcon(navObject.optString("icon"));
                navList.add(configNavModel);
            }
        }
        configModel.setNavList(navList);
    }
}
