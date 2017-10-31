package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.SettingConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.SettingModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingServiceImplHelper implements SettingConstant {
    public static BaseResultModel<SettingModel> getSettingContent(Context context, String jsonStr) {
        BaseResultModel<SettingModel> baseResultModel = new BaseResultModel();
//        SettingModel settingModel = new SettingModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject bodyObject = new JSONObject(jsonStr).optJSONObject("body");
                SettingModel settingModel2 = new SettingModel();
                try {
                    if (bodyObject.has(SettingConstant.SERVER_TIME)) {
                        settingModel2.setServerTime(bodyObject.optString(SettingConstant.SERVER_TIME));
                    }
                    if (bodyObject.has(SettingConstant.MISC)) {
                        JSONObject weatheroObject = bodyObject.optJSONObject(SettingConstant.MISC).optJSONObject("weather");
                        settingModel2.setAllowUseWeather(weatheroObject.optInt(SettingConstant.ALLOW_USAGE));
                        settingModel2.setAllowCityQueryWeather(weatheroObject.optInt(SettingConstant.ALLOW_CITY_QUERY));
                    }
                    if (bodyObject.has(SettingConstant.PLUGIN)) {
                        JSONObject pluginObject = bodyObject.optJSONObject(SettingConstant.PLUGIN);
                        settingModel2.setPlugCheck(pluginObject.optInt(SettingConstant.PLUG_CHECK));
                        settingModel2.setQqConnect(pluginObject.optInt(SettingConstant.QQ_CONNECT));
                        settingModel2.setWxConnect(pluginObject.optInt(SettingConstant.WX_CONNECT));
                    }
                    if (bodyObject.has("forum")) {
                        JSONObject forumObject = bodyObject.optJSONObject("forum");
                        settingModel2.setIsForumSummaryShow(forumObject.optInt(SettingConstant.IS_SUMMARY_SHOW));
                        settingModel2.setIsTodayPostCount(forumObject.optInt(SettingConstant.IS_TODAY_POSTS_COUNT));
                        settingModel2.setPostlistOrderby(forumObject.optInt(SettingConstant.POST_LIST_ORDER_BY));
                        settingModel2.setPostAudioLimit(forumObject.optInt(SettingConstant.POST_AUDIO_LIMIT));
                        settingModel2.setIsShowLocationPost(forumObject.optInt(SettingConstant.ISSHOWLOCATIONPOST));
                        settingModel2.setIsShowLocationTopic(forumObject.optInt(SettingConstant.ISSHOWLOCATIONTOPIC));
                    }
                    if (bodyObject.has(SettingConstant.PORTAL)) {
                        settingModel2.setIsPortalSummaryShow(bodyObject.optJSONObject(SettingConstant.PORTAL).optInt(SettingConstant.IS_SUMMARY_SHOW));
                    }
                    if (bodyObject.has("user")) {
                        JSONObject Object = bodyObject.optJSONObject("user");
                        settingModel2.setAllowAt(Object.optInt(SettingConstant.ALLOW_AT));
                        settingModel2.setAllowRegister(Object.optInt(SettingConstant.ALLOW_REGISTER));
                        settingModel2.setWapRegisterUrl(Object.optString(SettingConstant.WEP_REGISTER_URL));
                    }
                    if (bodyObject.has("message")) {
                        JSONObject messageObject = bodyObject.optJSONObject("message");
                        settingModel2.setPmAllowPostImage(messageObject.optInt("allowPostImage"));
                        settingModel2.setPmAudioLimit(messageObject.optInt(SettingConstant.PM_AUDIO_LIMIT));
                    }
                    if (bodyObject.has("moduleList")) {
                        JSONArray moduleArray = bodyObject.optJSONArray("moduleList");
                        if (moduleArray != null && moduleArray.length() > 0) {
                            List<ConfigComponentModel> componentList = new ArrayList();
                            int len = moduleArray.length();
                            for (int j = 0; j < len; j++) {
                                JSONObject obj = moduleArray.optJSONObject(j);
                                ConfigComponentModel componentModel = new ConfigComponentModel();
                                componentModel.setTitle(obj.optString(SettingConstant.MODULE_NAME));
                                componentModel.setNewsModuleId(obj.optLong("moduleId"));
                                componentModel.setType("newslist");
                                componentList.add(componentModel);
                            }
                            settingModel2.setComponentList(componentList);
                        }
                    }
                    settingModel2.setPostInfo(bodyObject.optString("postInfo"));
                    baseResultModel.setData(settingModel2);
//                    settingModel = settingModel2;
                } catch (Exception e) {
                    Exception e2 = e;
//                    settingModel = settingModel2;
                    e2.printStackTrace();
                    baseResultModel.setRs(0);
                    return baseResultModel;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            baseResultModel.setRs(0);
            return baseResultModel;
        }
        return baseResultModel;
    }

    public static String getSettingJsonStr(String forumIds) {
        JSONObject jsonRoot = new JSONObject();
        try {
            JSONObject bodyInfoJsonObject = new JSONObject();
            JSONObject postInfoJsonObject = new JSONObject();
            postInfoJsonObject.put(SettingConstant.FORUMIDS, forumIds);
            bodyInfoJsonObject.put("postInfo", postInfoJsonObject);
            jsonRoot.put("body", bodyInfoJsonObject);
            return jsonRoot.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
