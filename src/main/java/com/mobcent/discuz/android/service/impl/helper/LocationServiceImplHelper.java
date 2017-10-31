package com.mobcent.discuz.android.service.impl.helper;

import com.mobcent.discuz.android.constant.LocationConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocationServiceImplHelper implements LocationConstant {
    public static BaseResultModel<List<String>> parsePoi(String json) {
        BaseResultModel<List<String>> baseResultModel = new BaseResultModel();
        try {
            JSONArray jsonArray = new JSONObject(json).optJSONArray(LocationConstant.POI);
            List<String> list = new ArrayList();
            if (jsonArray != null) {
                int j = jsonArray.length();
                for (int i = 0; i < j; i++) {
                    list.add(((JSONObject) jsonArray.opt(i)).optString("name"));
                }
            }
            baseResultModel.setData(list);
            return baseResultModel;
        } catch (Exception e) {
            return null;
        }
    }
}
