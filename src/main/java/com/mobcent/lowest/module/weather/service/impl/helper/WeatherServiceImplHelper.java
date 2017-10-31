package com.mobcent.lowest.module.weather.service.impl.helper;

import com.mobcent.lowest.base.constant.BaseRestfulApiConstant;
import com.mobcent.lowest.module.weather.api.constant.WeatherApiConstant;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherServiceImplHelper implements BaseRestfulApiConstant, WeatherApiConstant {
    public static List<WeatherModel> getWeatherInfo(String jsonStr) {
        List<WeatherModel> weatherList = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.optInt("rs") == 0) {
                return null;
            }
            JSONArray jsonArrayTopic = jsonObject.optJSONArray("list");
            int j = jsonArrayTopic.length();
            for (int i = 0; i < j; i++) {
                JSONObject obj = jsonArrayTopic.optJSONObject(i);
                WeatherModel weatherModel = new WeatherModel();
                weatherModel.setCity(obj.optString(WeatherApiConstant.CITY));
                weatherModel.setDate(obj.optString("date"));
                weatherModel.setWeek(obj.optString(WeatherApiConstant.WEEK));
                weatherModel.setTemperature(obj.optString(WeatherApiConstant.TEMPERRATURE));
                weatherModel.setWeather(obj.optString("weather"));
                weatherModel.setPicInfo(obj.optInt(WeatherApiConstant.PICTURE_INFO));
                weatherModel.setWind(obj.optString(WeatherApiConstant.WIND));
                weatherModel.setDressIndex(obj.optString(WeatherApiConstant.DRESS_INDEX));
                weatherModel.setDressSuggest(obj.optString(WeatherApiConstant.DRESS_SUGGEST));
                weatherModel.setuVIndex(obj.optString(WeatherApiConstant.UV_INDEX));
                weatherModel.setWashCarIndex(obj.optString(WeatherApiConstant.WASHCAR_INDEX));
                weatherList.add(weatherModel);
            }
            return weatherList;
        } catch (Exception e) {
            e.printStackTrace();
            weatherList = null;
        }
        return weatherList;
    }

    public static List<CityModel> parseCityList(String jsonStr) {
        List<CityModel> cityList = new ArrayList();
        try {
            JSONObject jsonRoot = new JSONObject(jsonStr);
            if (jsonRoot.optInt("rs") == 0) {
                return null;
            }
            JSONArray jsonArray = jsonRoot.optJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                CityModel cityModel = new CityModel();
                JSONObject obj = jsonArray.getJSONObject(i);
                cityModel.setCityId(new StringBuilder(String.valueOf(obj.optInt(WeatherApiConstant.PLACE))).toString());
                cityModel.setCityName(obj.optString(WeatherApiConstant.CITY));
                cityList.add(cityModel);
            }
            return cityList;
        } catch (Exception e) {
            e.printStackTrace();
            return cityList;
        }
    }
}
