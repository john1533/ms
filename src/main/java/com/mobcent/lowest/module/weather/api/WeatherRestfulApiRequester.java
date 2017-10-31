package com.mobcent.lowest.module.weather.api;

import android.content.Context;
import com.mobcent.lowest.base.api.BaseLowestApiRequester;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.weather.api.constant.WeatherApiConstant;
import java.util.HashMap;

public class WeatherRestfulApiRequester extends BaseLowestApiRequester implements WeatherApiConstant {
    public static String getWeatherInfo(Context context, int queryType, String cityId, String cityName, double lng, double lat, boolean forceRefresh) {
        String urlString = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_weather_request_base_url"))).append("weather/getWeatherMsg.do").toString();
        HashMap<String, String> params = new HashMap();
        if (!forceRefresh) {
            params.put("connection_timeout", "2000");
            params.put("socket_timeout", "2000");
        }
        params.put("qs", new StringBuilder(String.valueOf(queryType)).toString());
        if (!MCStringUtil.isEmpty(cityId)) {
            params.put(WeatherApiConstant.PLACE, new StringBuilder(String.valueOf(cityId)).toString());
        }
        if (!MCStringUtil.isEmpty(cityName)) {
            params.put(WeatherApiConstant.CITY, cityName);
        }
        if (!(lng == 0.0d && lat == 0.0d)) {
            params.put("lng", new StringBuilder(String.valueOf(lng)).toString());
            params.put("lat", new StringBuilder(String.valueOf(lat)).toString());
        }
        return BaseLowestApiRequester.doPostRequestNoUserInfo(urlString, params, context);
    }

    public static String queryCityByName(Context context, String cityName) {
        String urlString = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_weather_request_base_url"))).append("weather/citySuggestion.do").toString();
        HashMap<String, String> params = new HashMap();
        params.put(WeatherApiConstant.CITYNAME, cityName);
        return BaseLowestApiRequester.doPostRequestNoUserInfo(urlString, params, context);
    }
}
