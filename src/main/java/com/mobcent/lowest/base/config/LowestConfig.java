package com.mobcent.lowest.base.config;

import com.mobcent.lowest.base.delegate.PlazaDelegate;
import com.mobcent.lowest.module.game.config.GameConfig.GameDelegate;
import com.mobcent.lowest.module.weather.config.WeatherConfig;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.ArrayList;
import java.util.List;

public abstract class LowestConfig implements PlazaDelegate, GameDelegate, WeatherConfig {
    protected List<WeatherModel> weatherListCache;

    public abstract String getAccessSecret();

    public abstract String getAccessToken();

    public abstract String getChannelNum();

    public abstract String getForumId();

    public abstract String getForumKey();


    public abstract String getSDKVersion();

    public abstract long getUserId();

    public abstract String getCtr() ;

    public String getBdMapAk() {
        return "9R8TDPD4PupIx0OQFlx2MQgs";
    }

    public List<WeatherModel> getWeatherListCache() {
        return this.weatherListCache;
    }

    public void setWeatherListCache(List<WeatherModel> weatherList) {
        if (weatherList != null && !weatherList.isEmpty()) {
            if (this.weatherListCache == null) {
                this.weatherListCache = new ArrayList();
            }
            this.weatherListCache.clear();
            this.weatherListCache.addAll(weatherList);
        }
    }
}
