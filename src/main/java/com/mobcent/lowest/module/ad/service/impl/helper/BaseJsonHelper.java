package com.mobcent.lowest.module.ad.service.impl.helper;

import com.mobcent.lowest.base.constant.BaseReturnCodeConstant;
import org.json.JSONObject;

public class BaseJsonHelper implements BaseReturnCodeConstant {
    public static String formJsonRS(String jsonStr) {
        if (jsonStr.trim().equals("{}")) {
            return "-1";
        }
        String errorCode = "";
        if (jsonStr.equals("connection_fail") || jsonStr.equals("upload_images_fail")) {
            return jsonStr;
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            int rs = jsonObj.optInt("rs");
            if (rs == 1) {
                return null;
            }
            if (rs == 0) {
                return jsonObj.optString("errcode");
            }
            return "-2";
        } catch (Exception e) {
            return "-1";
        }
    }
}
