package com.mobcent.lowest.module.place.service;

import com.mobcent.lowest.module.place.model.AreaModel;
import com.mobcent.lowest.module.place.model.PlaceApiFilterModel;
import com.mobcent.lowest.module.place.model.PlacePoiResult;
import com.mobcent.lowest.module.place.model.PlaceTypeModel;
import java.util.Map;

public interface PlaceAroundService {
    AreaModel queryAreaByCityCode(String str);

    Map<String, PlaceTypeModel> queryPlaceType(String str);

    PlacePoiResult queryPoiList(String str, String str2, int i, String str3, String str4, int i2, int i3, int i4, PlaceApiFilterModel placeApiFilterModel);

    AreaModel querySubAreaByCityCode(String str);
}
