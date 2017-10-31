package com.mobcent.lowest.module.weather.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.mobcent.lowest.module.weather.db.constant.WeatherShareDbConstant;
import com.mobcent.lowest.module.weather.model.CityModel;

public class WeatherSharedPreferencesDB implements WeatherShareDbConstant {
    private static WeatherSharedPreferencesDB sharedPreferencesDB = null;
    private SharedPreferences prefs = null;

    private WeatherSharedPreferencesDB(Context context) {
        this.prefs = context.getSharedPreferences(WeatherShareDbConstant.PREFS_FILE, 3);
    }

    public static synchronized WeatherSharedPreferencesDB getInstance(Context context) {
        WeatherSharedPreferencesDB weatherSharedPreferencesDB;
        synchronized (WeatherSharedPreferencesDB.class) {
            if (sharedPreferencesDB == null) {
                sharedPreferencesDB = new WeatherSharedPreferencesDB(context.getApplicationContext());
            }
            weatherSharedPreferencesDB = sharedPreferencesDB;
        }
        return weatherSharedPreferencesDB;
    }

    public void setMusicPlayerModel(String key, int value) {
        Editor editor = this.prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void setWeatherJson(String jsonStr) {
        Editor editor = this.prefs.edit();
        editor.putString(WeatherShareDbConstant.WEATHER_JSON, jsonStr);
        editor.commit();
    }

    public String getWeatherJson() {
        return this.prefs.getString(WeatherShareDbConstant.WEATHER_JSON, null);
    }

    public CityModel getCityModel() {
        String cityId = this.prefs.getString(WeatherShareDbConstant.CITY_MODEL_CITY_ID, "");
        double lng = Double.parseDouble(this.prefs.getString(WeatherShareDbConstant.CITY_MODEL_CITY_LNG, "0"));
        double lat = Double.parseDouble(this.prefs.getString(WeatherShareDbConstant.CITY_MODEL_CITY_LAT, "0"));
        String cityName = this.prefs.getString(WeatherShareDbConstant.CITY_MODEL_CITY_NAME, null);
        if (cityName == null) {
            return null;
        }
        CityModel cityModel = new CityModel();
        cityModel.setCityId(cityId);
        cityModel.setCityName(cityName);
        cityModel.setLongitude(lng);
        cityModel.setLatitude(lat);
        return cityModel;
    }

    public void setCityModel(CityModel cityModel) {
        Editor editor = this.prefs.edit();
        editor.putString(WeatherShareDbConstant.CITY_MODEL_CITY_ID, cityModel.getCityId());
        editor.putString(WeatherShareDbConstant.CITY_MODEL_CITY_NAME, cityModel.getCityName());
        editor.putString(WeatherShareDbConstant.CITY_MODEL_CITY_LNG, new StringBuilder(String.valueOf(cityModel.getLongitude())).toString());
        editor.putString(WeatherShareDbConstant.CITY_MODEL_CITY_LAT, new StringBuilder(String.valueOf(cityModel.getLatitude())).toString());
        editor.commit();
    }
}
