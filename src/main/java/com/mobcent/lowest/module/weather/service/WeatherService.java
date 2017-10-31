package com.mobcent.lowest.module.weather.service;

import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.List;

public interface WeatherService {
    List<WeatherModel> getWeatherInfo(int i, String str, String str2, double d, double d2, boolean z);

    List<WeatherModel> getWeatherInfoByLocal();

    List<CityModel> searchCityList(String str);
}
