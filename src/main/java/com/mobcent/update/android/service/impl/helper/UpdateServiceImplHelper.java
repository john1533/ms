package com.mobcent.update.android.service.impl.helper;

import com.mobcent.update.android.constant.MCUpdateConstant;
import com.mobcent.update.android.model.UpdateModel;
import org.json.JSONObject;

public class UpdateServiceImplHelper implements MCUpdateConstant {
    public static UpdateModel getUpdateInfo(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String url = jsonObject.optString(MCUpdateConstant.UPDATE_APP_URL);
            JSONObject object = jsonObject.optJSONObject("update");
            if (object == null) {
                return null;
            }
            UpdateModel model = new UpdateModel();
            try {
                model.setDesc(object.optString("desc"));
                model.setId(object.optInt("id"));
                model.setLink(object.optString(MCUpdateConstant.UPDATE_LINK));
                model.setSize((float) object.optLong(MCUpdateConstant.UPDATE_SIZE));
                model.setTime(object.optLong("time"));
                model.setVer_name(object.optString(MCUpdateConstant.UPDATE_VER_NAME));
                model.setAppName(object.optString(MCUpdateConstant.UPDATE_APP_NAME));
                model.setIcon(new StringBuilder(String.valueOf(url)).append(object.optString(MCUpdateConstant.UPDATE_APP_ICON)).toString());
                model.setShowDetail(object.optInt(MCUpdateConstant.UPDATE_SHOW_DETAIL));
                model.setType(object.optString("type"));
                return model;
            } catch (Exception e2) {
                UpdateModel updateModel = model;
                e2.printStackTrace();
                return null;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }
}
