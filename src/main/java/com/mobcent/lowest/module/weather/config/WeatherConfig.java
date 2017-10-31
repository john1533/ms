package com.mobcent.lowest.module.weather.config;

import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.List;

public interface WeatherConfig {
    List<WeatherModel> getWeatherListCache();

    void setWeatherListCache(List<WeatherModel> list);
}
