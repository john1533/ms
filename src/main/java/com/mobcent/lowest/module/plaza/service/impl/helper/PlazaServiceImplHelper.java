package com.mobcent.lowest.module.plaza.service.impl.helper;

import com.mobcent.lowest.base.constant.BaseRestfulApiConstant;
import com.mobcent.lowest.base.model.LowestResultModel;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.plaza.api.constant.PlazaApiConstant;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlazaServiceImplHelper implements BaseRestfulApiConstant, AdConstant, PlazaApiConstant {
    public static String TAG = "PlazaServiceImplHelper";

    public static LowestResultModel<List<PlazaAppModel>> parsePlazaAppModelList(String jsonStr) {
        LowestResultModel<List<PlazaAppModel>> result = new LowestResultModel();
        List<PlazaAppModel> plazaAppModelList = new ArrayList();
        try {
            JSONObject jsonRoot = new JSONObject(jsonStr);
            int rs = jsonRoot.optInt("rs");
            result.setRs(rs);
            if (!(rs == 0 || rs == 2)) {
                long ut = jsonRoot.optLong(PlazaApiConstant.UT, -1);
                JSONArray jsonArray = jsonRoot.optJSONArray(PlazaApiConstant.SDLIST);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    PlazaAppModel plazaAppModel = new PlazaAppModel();
                    plazaAppModel.setModelName(json.optString(PlazaApiConstant.MN));
                    plazaAppModel.setModelDrawable(json.optString(PlazaApiConstant.MPIC));
                    plazaAppModel.setModelId(json.optLong("mid"));
                    plazaAppModel.setModelAction(json.optInt("act"));
                    plazaAppModel.setNativeCat(json.optInt(PlazaApiConstant.NATIVE_CAT));
                    plazaAppModelList.add(plazaAppModel);
                }
                result.setData(plazaAppModelList);
                if (ut != -1) {
                    result.setUt(ut);
                }
            }
        } catch (Exception e) {
            result.setRs(0);
        }
        return result;
    }
}
