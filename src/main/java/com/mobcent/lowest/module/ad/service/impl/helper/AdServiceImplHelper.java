package com.mobcent.lowest.module.ad.service.impl.helper;

import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.cache.AdDatasCache;
import com.mobcent.lowest.module.ad.cache.AdPositionsCache;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.model.AdOtherModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AdServiceImplHelper implements AdApiConstant {
    private static String TAG = "AdServiceImplHelper";

    public static AdPositionsCache parseAdPositions(String jsonStr) {
        AdPositionsCache adPositionsCache;
        Exception e;
        try {
            AdPositionsCache positionsModel = new AdPositionsCache();
            try {
                JSONObject jSONObject = new JSONObject(jsonStr);
                if (jSONObject.optInt("rs") == 0) {
                    adPositionsCache = positionsModel;
                    return positionsModel;
                }
                String[] dt1s = null;
                String[] dt2s = null;
                String[] dt3s = null;
                String[] dt4s = null;
                String[] dt5s = null;
                String[] dt6s = null;
                String[] dt7s = null;
                String dt1 = jSONObject.optString(AdApiConstant.RES_DT1, "").trim();
                if (!MCStringUtil.isEmpty(dt1)) {
                    dt1s = dt1.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                String dt2 = jSONObject.optString(AdApiConstant.RES_DT2, "").trim();
                if (!MCStringUtil.isEmpty(dt2)) {
                    dt2s = dt2.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                String dt3 = jSONObject.optString(AdApiConstant.RES_DT3, "").trim();
                if (!MCStringUtil.isEmpty(dt3)) {
                    dt3s = dt3.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                String dt4 = jSONObject.optString(AdApiConstant.RES_DT4, "").trim();
                if (!MCStringUtil.isEmpty(dt4)) {
                    dt4s = dt4.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                String dt5 = jSONObject.optString(AdApiConstant.RES_DT5, "").trim();
                if (!MCStringUtil.isEmpty(dt5)) {
                    dt5s = dt5.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                String dt6 = jSONObject.optString(AdApiConstant.RES_DT6, "").trim();
                if (!MCStringUtil.isEmpty(dt6)) {
                    dt6s = dt6.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                String dt7 = jSONObject.optString(AdApiConstant.RES_DT7, "").trim();
                if (!MCStringUtil.isEmpty(dt7)) {
                    dt7s = dt7.split(AdApiConstant.RES_SPLIT_COMMA);
                }
                positionsModel.setDt1(ssToInts(dt1s));
                positionsModel.setDt2(ssToInts(dt2s));
                positionsModel.setDt3(ssToInts(dt3s));
                positionsModel.setDt4(ssToInts(dt4s));
                positionsModel.setDt5(ssToInts(dt5s));
                positionsModel.setDt6(ssToInts(dt6s));
                positionsModel.setDt7(ssToInts(dt7s));
                positionsModel.setCycle(jSONObject.optInt(AdApiConstant.RES_CYCLE));
                adPositionsCache = positionsModel;
                return positionsModel;
            } catch (Exception e2) {
                e = e2;
                adPositionsCache = positionsModel;
                MCLogUtil.e(TAG, "[parseAdPositions]" + e.toString());
                return null;
            }
        } catch (Exception e3) {
            e = e3;
            MCLogUtil.e(TAG, "[parseAdPositions]" + e.toString());
            return null;
        }
    }

    public static AdDatasCache parseAdDatas(String jsonStr) {
        AdDatasCache adDatasCache = new AdDatasCache();
        List<AdOtherModel> others = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int rs = jsonObject.optInt("rs");
            adDatasCache.setRequestSucc(true);
            if (rs == 0) {
                return adDatasCache;
            }
            JSONArray listArray = jsonObject.optJSONArray(AdApiConstant.RES_POLIST).optJSONObject(0).optJSONArray("list");
            int len = listArray.length();
            for (int i = 0; i < len; i++) {
                JSONObject adObject = listArray.getJSONObject(i);
                int dt = adObject.optInt(AdApiConstant.RES_DT);
                AdModel adModel = new AdModel();
                adModel.setAid(adObject.optLong("aid"));
                adModel.setAt(adObject.optInt("at"));
                adModel.setDt(dt);
                adModel.setDu(adObject.optString("du"));
                adModel.setGid(adObject.optLong(AdApiConstant.RES_GID));
                adModel.setGt(adObject.optInt(AdApiConstant.RES_GT));
                adModel.setPu(adObject.optString(AdApiConstant.RES_PU));
                adModel.setTel(adObject.optString(AdApiConstant.RES_TEL));
                adModel.setTx(adObject.optString(AdApiConstant.RES_TX));
                adModel.setDx(adObject.optString(AdApiConstant.RES_DE));
                adModel.setAdu(adObject.optString(AdApiConstant.RES_ADU));
                JSONArray jsonArray = adObject.optJSONArray(AdApiConstant.RES_TRACKS);
                if (jsonArray != null) {
                    others.clear();
                    int size = jsonArray.length();
                    for (int j = 0; j < size; j++) {
                        JSONObject json = jsonArray.getJSONObject(j);
                        AdOtherModel other = new AdOtherModel();
                        other.setType(json.optInt("type"));
                        other.setUrl(json.optString("url"));
                        other.setDelay(json.optLong(AdApiConstant.RES_DELAY));
                        others.add(other);
                    }
                }
                if (others != null && others.size() > 0) {
                    adModel.setOther(true);
                    adModel.setOthers(others);
                }
                if (adDatasCache.getAdDataMap().get(Integer.valueOf(dt)) == null) {
                    adDatasCache.getAdDataMap().put(Integer.valueOf(dt), new HashMap());
                }
                ((HashMap) adDatasCache.getAdDataMap().get(Integer.valueOf(dt))).put(Long.valueOf(adModel.getAid()), adModel);
            }
            return adDatasCache;
        } catch (Exception e) {
            MCLogUtil.e(TAG, "[parseAdDatas]" + e.toString());
            if (-1 != 1) {
                return null;
            }
            adDatasCache.setRequestSucc(true);
            return adDatasCache;
        }
    }

    public static AdModel parseSplashAdModel(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.optInt("rs") == 0) {
                return null;
            }
            JSONObject adObject = jsonObject.optJSONArray(AdApiConstant.RES_POLIST).optJSONObject(0).optJSONArray("list").optJSONObject(0);
            int dt = adObject.optInt(AdApiConstant.RES_DT);
            AdModel adModel = new AdModel();
            adModel.setAid(adObject.optLong("aid"));
            adModel.setAt(adObject.optInt("at"));
            adModel.setDt(dt);
            adModel.setDu(adObject.optString("du"));
            adModel.setGid(adObject.optLong(AdApiConstant.RES_GID));
            adModel.setGt(adObject.optInt(AdApiConstant.RES_GT));
            adModel.setPu(adObject.optString(AdApiConstant.RES_PU));
            adModel.setTel(adObject.optString(AdApiConstant.RES_TEL));
            adModel.setTx(adObject.optString(AdApiConstant.RES_TX));
            return adModel;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean parseSucc(String jsonStr) {
        try {
            if (new JSONObject(jsonStr).optInt("rs") == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            MCLogUtil.e(TAG, "[parseSucc]" + e.toString());
            return false;
        }
    }

    public static int[] ssToInts(String[] strs) {
        if (strs == null || strs.length == 0) {
            return null;
        }
        int count = strs.length;
        int[] inters = new int[count];
        for (int i = 0; i < count; i++) {
            inters[i] = Integer.parseInt(strs[i]);
        }
        return inters;
    }
}
