package com.mobcent.lowest.module.weather.service.impl;

import android.content.Context;
import com.mobcent.lowest.module.weather.api.WeatherRestfulApiRequester;
import com.mobcent.lowest.module.weather.db.WeatherSharedPreferencesDB;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import com.mobcent.lowest.module.weather.service.WeatherService;
import com.mobcent.lowest.module.weather.service.impl.helper.WeatherServiceImplHelper;
import java.util.ArrayList;
import java.util.List;

public class WeatherServiceImpl implements WeatherService {
    private Context context;

    public WeatherServiceImpl(Context context) {
        this.context = context;
    }

    public List<WeatherModel> getWeatherInfo(int queryType, String cityId, String cityName, double lng, double lat, boolean forceRefresh) {
        String jsonStr = WeatherRestfulApiRequester.getWeatherInfo(this.context, queryType, cityId, cityName, lng, lat, forceRefresh);
        List<WeatherModel> weatherList = WeatherServiceImplHelper.getWeatherInfo(jsonStr);
        if (weatherList == null || weatherList.isEmpty()) {
            return new ArrayList();
        }
        WeatherSharedPreferencesDB.getInstance(this.context).setWeatherJson(jsonStr);
        return weatherList;
    }

    public List<WeatherModel> getWeatherInfoByLocal() {
        String jsonStr = WeatherSharedPreferencesDB.getInstance(this.context).getWeatherJson();
        if (jsonStr == null) {
            return new ArrayList();
        }
        List<WeatherModel> weatherList = WeatherServiceImplHelper.getWeatherInfo(jsonStr);
        if (weatherList == null) {
            weatherList = new ArrayList();
        }
        return weatherList;
    }

    public List<CityModel> searchCityList(String keyWord) {
        return WeatherServiceImplHelper.parseCityList(WeatherRestfulApiRequester.queryCityByName(this.context, keyWord));
    }
}
