package com.mobcent.lowest.module.place.service.impl.helper;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCAssetsUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.api.constant.PlaceApiConstant;
import com.mobcent.lowest.module.place.model.AreaModel;
import com.mobcent.lowest.module.place.model.PlaceHotNavModel;
import com.mobcent.lowest.module.place.model.PlaceHotSubModel;
import com.mobcent.lowest.module.place.model.PlaceKeyNameModel;
import com.mobcent.lowest.module.place.model.PlacePoiDetailModel;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import com.mobcent.lowest.module.place.model.PlacePoiLocationModel;
import com.mobcent.lowest.module.place.model.PlacePoiResult;
import com.mobcent.lowest.module.place.model.PlaceTypeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlaceAroundServiceImplHelper implements PlaceApiConstant {
    public static PlacePoiResult parsePlacePoiInfoList(String jsonStr) {
        PlacePoiResult placePoiResult = new PlacePoiResult();
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.optInt("status");
            String message = json.optString("message");
            if (status != 0) {
                placePoiResult.setStatus(status);
                placePoiResult.setMessage(message);
            } else {
                placePoiResult.setStatus(status);
                placePoiResult.setTotal(json.optInt("total"));
                List<PlacePoiInfoModel> poiInfoList = new ArrayList();
                JSONArray results = json.optJSONArray(PlaceApiConstant.RESULTS);
                if (!(results == null || results.length() == 0)) {
                    int len = results.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject result = results.optJSONObject(i);
                        PlacePoiInfoModel poiInfo = new PlacePoiInfoModel();
                        poiInfo.name = result.optString("name");
                        poiInfo.address = result.optString(PlaceApiConstant.ADDRESS);
                        poiInfo.telephone = result.optString(PlaceApiConstant.TELEPHONE);
                        poiInfo.uid = result.optString("uid");
                        JSONObject locationObject = result.optJSONObject("location");
                        if (locationObject != null) {
                            PlacePoiLocationModel locationModel = new PlacePoiLocationModel();
                            locationModel.lat = locationObject.optDouble("lat");
                            locationModel.lng = locationObject.optDouble("lng");
                            poiInfo.locationModel = locationModel;
                        }
                        JSONObject detailObject = result.optJSONObject(PlaceApiConstant.DETAIL_INFO);
                        if (detailObject != null) {
                            PlacePoiDetailModel detailModel = new PlacePoiDetailModel();
                            detailModel.distance = detailObject.optInt("distance");
                            detailModel.type = detailObject.optString("type");
                            detailModel.tag = detailObject.optString("tag");
                            detailModel.detailUrl = detailObject.optString(PlaceApiConstant.DETAIL_URL);
                            detailModel.price = detailObject.optString(PlaceApiConstant.PRICE);
                            detailModel.shopHours = detailObject.optString(PlaceApiConstant.SHOP_HOURS);
                            detailModel.overallRating = detailObject.optString(PlaceApiConstant.OVERALL_RATING);
                            detailModel.tasteRating = detailObject.optString(PlaceApiConstant.TASTE_RATING);
                            detailModel.serviceRating = detailObject.optString(PlaceApiConstant.SERVICE_RATING);
                            detailModel.environmentRating = detailObject.optString(PlaceApiConstant.ENVIRONMENT_RATING);
                            detailModel.facilityRating = detailObject.optString(PlaceApiConstant.FACILITY_RATING);
                            detailModel.hygieneRating = detailObject.optString(PlaceApiConstant.HYGIENE_RATING);
                            detailModel.technologyRating = detailObject.optString(PlaceApiConstant.TECHNOLOGY_RATING);
                            detailModel.imageNum = detailObject.optString(PlaceApiConstant.IMAGE_NUM);
                            detailModel.grouponNum = detailObject.optInt(PlaceApiConstant.GROUPON_NUM);
                            detailModel.discountNum = detailObject.optInt(PlaceApiConstant.DISCOUNT_NUM);
                            detailModel.commentNum = detailObject.optString(PlaceApiConstant.COMMENT_NUM);
                            detailModel.favoriteNum = detailObject.optString(PlaceApiConstant.FAVORITE_NUM);
                            detailModel.checkinNum = detailObject.optString(PlaceApiConstant.CHECKIN_NUM);
                            poiInfo.detailInfoModel = detailModel;
                        }
                        poiInfoList.add(poiInfo);
                        placePoiResult.setPoiInfoList(poiInfoList);
                    }
                }
            }
        } catch (Exception e) {
            placePoiResult.setStatus(-1);
            placePoiResult.setMessage("data exception");
            MCLogUtil.e("tag", e.toString());
        }
        return placePoiResult;
    }

    public static Map<String, PlaceTypeModel> queryPlaceType(Context context, String fileName) {
        String jsonStr = getAssetsFile(context, fileName);
        Map<String, PlaceTypeModel> placeTypeMap = new HashMap();
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            if (jsonArray != null && jsonArray.length() > 0) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    int j;
                    PlaceTypeModel placeTypeModel = new PlaceTypeModel();
                    JSONObject typeObject = jsonArray.optJSONObject(i);
                    String type = typeObject.optString("type");
                    String name = typeObject.optString("name");
                    String priceTag = typeObject.optString(PlaceApiConstant.PRICE_TAG);
                    JSONArray distanceArray = typeObject.optJSONArray("distance");
                    List<PlaceKeyNameModel> distanceList = null;
                    if (distanceArray != null && distanceArray.length() > 0) {
                        distanceList = new ArrayList();
                        int dLen = distanceArray.length();
                        for (j = 0; j < dLen; j++) {
                            PlaceKeyNameModel distanceModel = new PlaceKeyNameModel();
                            JSONObject distanceObject = distanceArray.optJSONObject(j);
                            distanceModel.setKey(distanceObject.optString(PlaceApiConstant.KEY));
                            distanceModel.setName(distanceObject.optString("name"));
                            distanceModel.setType(1);
                            distanceList.add(distanceModel);
                        }
                    }
                    JSONArray sortArray = typeObject.optJSONArray("sort");
                    List<PlaceKeyNameModel> sortList = null;
                    if (sortArray != null && sortArray.length() > 0) {
                        sortList = new ArrayList();
                        int sLen = sortArray.length();
                        for (j = 0; j < sLen; j++) {
                            PlaceKeyNameModel sortModel = new PlaceKeyNameModel();
                            JSONObject sortObject = sortArray.optJSONObject(j);
                            sortModel.setKey(sortObject.optString(PlaceApiConstant.KEY));
                            sortModel.setName(sortObject.optString("name"));
                            sortModel.setRule(sortObject.optInt(PlaceApiConstant.RULE, -1));
                            sortModel.setType(2);
                            sortList.add(sortModel);
                        }
                    }
                    List<PlaceKeyNameModel> keywordsList = null;
                    JSONArray keywordsArray = typeObject.optJSONArray(PlaceApiConstant.KEYWORDS);
                    if (keywordsArray != null && keywordsArray.length() > 0) {
                        keywordsList = new ArrayList();
                        int kLen = keywordsArray.length();
                        for (j = 0; j < kLen; j++) {
                            PlaceKeyNameModel keywordsModel = new PlaceKeyNameModel();
                            keywordsModel.setType(3);
                            Object keywordsObject = keywordsArray.opt(j);
                            if (keywordsObject instanceof String) {
                                keywordsModel.setName((String) keywordsObject);
                            } else if (keywordsObject instanceof JSONObject) {
                                JSONObject keywordsObjectTemp = (JSONObject) keywordsObject;
                                keywordsModel.setName(keywordsObjectTemp.optString(PlaceApiConstant.N));
                                JSONArray subArray = keywordsObjectTemp.optJSONArray(PlaceApiConstant.SUB);
                                List<String> sub = null;
                                if (subArray != null && subArray.length() > 0) {
                                    sub = new ArrayList();
                                    int subLen = subArray.length();
                                    for (int k = 0; k < subLen; k++) {
                                        sub.add(subArray.opt(k).toString());
                                    }
                                }
                                keywordsModel.setSub(sub);
                            } else {
                                continue;
                            }
                            keywordsList.add(keywordsModel);
                        }
                        continue;
                    }
                    placeTypeModel.setType(type);
                    placeTypeModel.setName(name);
                    placeTypeModel.setPriceTag(priceTag);
                    placeTypeModel.setDistanceList(distanceList);
                    placeTypeModel.setSortList(sortList);
                    placeTypeModel.setKeywordsList(keywordsList);
                    placeTypeMap.put(type, placeTypeModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return placeTypeMap;
    }

    public static AreaModel parseAreaList(String jsonStr) {
        Exception e;
        AreaModel cityAreaModel = null;
        try {
            JSONObject jsonRoot = new JSONObject(jsonStr);
            JSONObject contentObj = jsonRoot.optJSONObject("content");
            JSONObject resultObj = jsonRoot.optJSONObject(PlaceApiConstant.RESULT);
            if (resultObj == null || resultObj.optInt("error") != 0) {
                return null;
            }
            int error = resultObj.optInt("error");
            AreaModel cityAreaModel2 = new AreaModel();
            try {
                cityAreaModel2.setError(error);
                cityAreaModel2.setAreaName(contentObj.optString(PlaceApiConstant.AREA_NAME));
                cityAreaModel2.setAreaType(contentObj.optInt(PlaceApiConstant.AREA_TYPE));
                cityAreaModel2.setAreaCode(contentObj.optInt(PlaceApiConstant.AREA_CODE));
                cityAreaModel2.setGeo(contentObj.optString(PlaceApiConstant.GEO));
                JSONArray subArray = contentObj.optJSONArray(PlaceApiConstant.SUB);
                if (subArray == null || subArray.length() <= 0) {
                    cityAreaModel = cityAreaModel2;
                    return cityAreaModel;
                }
                List<AreaModel> subList = new ArrayList();
                int len = subArray.length();
                for (int i = 0; i < len; i++) {
                    AreaModel areaModel = new AreaModel();
                    JSONObject areaObj = subArray.optJSONObject(i);
                    areaModel.setAreaCode(areaObj.optInt(PlaceApiConstant.AREA_CODE));
                    areaModel.setAreaName(areaObj.optString(PlaceApiConstant.AREA_NAME));
                    areaModel.setAreaType(areaObj.optInt(PlaceApiConstant.AREA_TYPE));
                    areaModel.setGeo(areaObj.optString(PlaceApiConstant.GEO));
                    subList.add(areaModel);
                }
                cityAreaModel2.setSubAreaList(subList);
                cityAreaModel = cityAreaModel2;
                return cityAreaModel;
            } catch (Exception e2) {
                e = e2;
                cityAreaModel = cityAreaModel2;
                e.printStackTrace();
                return cityAreaModel;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return cityAreaModel;
        }
    }

    public static List<PlaceHotNavModel> queryHotWordList(Context context, String fileName) {
        Exception e;
        List<PlaceHotNavModel> navList = null;
        try {
            JSONArray jsonArray = new JSONArray(getAssetsFile(context, fileName));
            if (jsonArray == null || jsonArray.length() == 0) {
                return null;
            }
            List<PlaceHotNavModel> navList2 = new ArrayList();
            try {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject navObj = jsonArray.optJSONObject(i);
                    PlaceHotNavModel navModel = new PlaceHotNavModel();
                    navModel.setName(navObj.optString("name"));
                    navModel.setColor(MCStringUtil.isEmpty(navObj.optString(PlaceApiConstant.COLOR)) ? "#ff4800" : navObj.optString(PlaceApiConstant.COLOR));
                    navModel.setDrawableName(navObj.optString(PlaceApiConstant.DRAWABLE));
                    String type = navObj.optString("type");
                    navModel.setType(type);
                    JSONArray subArray = navObj.optJSONArray(PlaceApiConstant.CHILD);
                    int l = subArray.length();
                    for (int j = 0; j < l; j++) {
                        PlaceHotSubModel subModel = new PlaceHotSubModel();
                        JSONObject subObj = subArray.getJSONObject(j);
                        subModel.setName(subObj.optString("name"));
                        subModel.setType(type);
                        subModel.setColor(MCStringUtil.isEmpty(subObj.optString(PlaceApiConstant.COLOR)) ? "#383838" : subObj.optString(PlaceApiConstant.COLOR));
                        navModel.getSubList().add(subModel);
                    }
                    navList2.add(navModel);
                }
                return navList2;
            } catch (Exception e2) {
                e = e2;
                navList = navList2;
                e.printStackTrace();
                return navList;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return navList;
        }
    }

    public static String getAssetsFile(Context context, String fileName) {
        return MCAssetsUtils.getFromAssets(context, fileName);
    }
}
