package com.mobcent.android.service.impl.helper;

import com.mobcent.android.constant.MCShareConstant;
import com.mobcent.android.constant.MCShareReturnConstant;
import com.mobcent.android.model.MCShareSiteModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MCShareServiceImplHelper implements MCShareConstant, MCShareReturnConstant {
    public static String formJsonRS(String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.optInt("rs") == 1) {
                return MCShareReturnConstant.RS_SUCC;
            }
            return jsonObj.optString(MCShareReturnConstant.RS_REASON);
        } catch (JSONException e) {
            return MCShareReturnConstant.RS_FAIL;
        }
    }

    public static List<MCShareSiteModel> formSiteModels(String jsonStr) {
        List<MCShareSiteModel> siteList = new ArrayList();
        List<MCShareSiteModel> bindSiteList = new ArrayList();
        List<MCShareSiteModel> unBindSiteList = new ArrayList();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String baseUrl = jsonObj.optString("baseUrl");
            String domainUrl = jsonObj.optString(MCShareConstant.DOMAIN_URL);
            long userId = jsonObj.optLong("userId");
            JSONArray array = jsonObj.optJSONArray("list");
            if (array == null) {
                return null;
            }
            MCShareSiteModel model;
            for (int i = 0; i < array.length(); i++) {
                model = new MCShareSiteModel();
                JSONObject siteObj = array.getJSONObject(i);
                int siteId = siteObj.optInt("id");
                String name = siteObj.optString("name");
                String icon = siteObj.optString("picPath");
                boolean isBind = siteObj.optBoolean(MCShareConstant.IS_BIND);
                String bindUrl = siteObj.optString(MCShareConstant.BIND_URL);
                model.setSiteId(siteId);
                model.setUserId(userId);
                model.setSiteName(name);
                model.setSiteImage(new StringBuilder(String.valueOf(baseUrl)).append(icon).toString());
                model.setBindUrl(new StringBuilder(String.valueOf(domainUrl)).append(bindUrl).toString());
                if (isBind) {
                    model.setBindState(1);
                    bindSiteList.add(model);
                } else {
                    model.setBindState(3);
                    unBindSiteList.add(model);
                }
            }
            siteList.addAll(bindSiteList);
            siteList.addAll(unBindSiteList);
            if (siteList.size() <= 0) {
                return siteList;
            }
            model = (MCShareSiteModel) siteList.get(0);
            model.setShareContent(jsonObj.optString("content"));
            model.setShareUrl(jsonObj.optString("shareUrl"));
            siteList.set(0, model);
            return siteList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isUnbindSucc(String jsonStr) {
        try {
            if (new JSONObject(jsonStr).optInt("rs") == 1) {
                return true;
            }
            return false;
        } catch (JSONException e) {
            return false;
        }
    }

    public static String formUploadImageJson(String jsonStr) {
        String str = null;
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.optInt("rs") != 0) {
                String baseUrl = jsonObj.optString(MCShareConstant.PHOTO_BASE_PATH);
                str = new StringBuilder(String.valueOf(baseUrl)).append(jsonObj.optString(MCShareConstant.PHOTO_PATH)).toString();
            }
        } catch (JSONException e) {
        }
        return str;
    }
}
