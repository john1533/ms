package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import android.text.TextUtils;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.BaseErrorCodeConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import org.json.JSONObject;

public class BaseJsonHelper implements BaseErrorCodeConstant {
    public static BaseResultModel<Object> formJsonRs(String jsonStr, Context context) {
        BaseResultModel<Object> baseResultModel = new BaseResultModel();
        formJsonRs(context, jsonStr, baseResultModel);
        return baseResultModel;
    }

    public static void formJsonRs(Context context, String jsonStr, BaseResultModel<?> result) {
        MCResource resource = MCResource.getInstance(context);
        result.setRs(0);
        result.setErrorInfo(MCResource.getInstance(context).getString("mc_forum_json_error"));
        if (!MCStringUtil.isEmpty(jsonStr)) {
            if (jsonStr.trim().equals("{}")) {
                result.setErrorInfo(resource.getString("mc_forum_json_error"));
            } else if (jsonStr.equals("connection_fail") || jsonStr.equals("upload_images_fail")) {
                result.setErrorInfo(resource.getString("mc_forum_connect_error"));
                result.setAlert(1);
            } else {
                int rs;
                String errorInfo = "";
                String errorCode = "";
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    rs = jsonObj.optInt("rs");
                    JSONObject headObject = jsonObj.optJSONObject("head");
                    result.setErrorCode(headObject.optString("errcode"));
                    result.setVersion(headObject.optString("version"));
                    errorInfo = headObject.optString("errInfo");
                    result.setAlert(headObject.optInt("alert"));
                } catch (Exception e) {
                    rs = 0;
                    errorInfo = resource.getString("mc_forum_json_error");
                }
                if (!TextUtils.isEmpty(result.getVersion())) {
                    result.setVersionTooLow(isVersionTooLowe(result.getVersion(), BaseApiConstant.SDK_VERSION_VALUE));
                }
                result.setRs(rs);
                result.setErrorInfo(errorInfo);
            }
        }
    }

    public static boolean isVersionTooLowe(String serverVersion, String clientVersion) {
        String[] clientVer = clientVersion.split("\\.");
        String[] serVer = serverVersion.split("\\.");
        if (serVer.length >= 3) {
            for (int i = 0; i < clientVer.length; i++) {
                if (Integer.parseInt(clientVer[i]) > Integer.parseInt(serVer[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isJson(String jsonStr) {
        if (!jsonStr.trim().equals("{}") && jsonStr.substring(0, 1).equals("{") && jsonStr.substring(jsonStr.length() - 1).equals("}")) {
            return true;
        }
        return false;
    }
}
