package com.mobcent.lowest.module.place.service.impl;

import android.content.Context;
import com.mobcent.lowest.module.place.api.AroundApiRequester;
import com.mobcent.lowest.module.place.api.constant.PlaceConstant;
import com.mobcent.lowest.module.place.db.PlaceSharedPreferenceDb;
import com.mobcent.lowest.module.place.model.AreaModel;
import com.mobcent.lowest.module.place.model.PlaceApiFilterModel;
import com.mobcent.lowest.module.place.model.PlacePoiResult;
import com.mobcent.lowest.module.place.model.PlaceTypeModel;
import com.mobcent.lowest.module.place.service.PlaceAroundService;
import com.mobcent.lowest.module.place.service.impl.helper.PlaceAroundServiceImplHelper;
import java.util.Map;

public class PlaceAroundServiceImpl implements PlaceAroundService, PlaceConstant {
    public String TAG = "PlaceAroundServiceImpl";
    private Context context;

    public PlaceAroundServiceImpl(Context context) {
        this.context = context;
    }

    public PlacePoiResult queryPoiList(String query, String tag, int scope, String regoin, String location, int radius, int pageSize, int pageNum, PlaceApiFilterModel filter) {
        return PlaceAroundServiceImplHelper.parsePlacePoiInfoList(AroundApiRequester.poiSearch(this.context, query, tag, scope, regoin, location, radius, pageSize, pageNum, filter));
    }

    public Map<String, PlaceTypeModel> queryPlaceType(String fileName) {
        return PlaceAroundServiceImplHelper.queryPlaceType(this.context, fileName);
    }

    public AreaModel queryAreaByCityCode(String areaCode) {
        String jsonStr = PlaceSharedPreferenceDb.getInstance(this.context).getAreaJsonStr(areaCode);
        if (jsonStr == null) {
            jsonStr = AroundApiRequester.queryAreaByCityCode(this.context, areaCode, 0);
        }
        AreaModel areaModel = PlaceAroundServiceImplHelper.parseAreaList(jsonStr);
        if (areaModel != null && PlaceSharedPreferenceDb.getInstance(this.context).getAreaJsonStr(areaCode) == null) {
            PlaceSharedPreferenceDb.getInstance(this.context).setAreaJsonStr(areaCode, jsonStr);
        }
        return areaModel;
    }

    public AreaModel querySubAreaByCityCode(String areaCode) {
        return PlaceAroundServiceImplHelper.parseAreaList(AroundApiRequester.queryAreaByCityCode(this.context, areaCode, 1));
    }
}
