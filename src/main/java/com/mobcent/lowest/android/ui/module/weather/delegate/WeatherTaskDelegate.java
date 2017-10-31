package com.mobcent.lowest.android.ui.module.weather.delegate;

import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.List;

public interface WeatherTaskDelegate {
    void onPostExecute(List<WeatherModel> list);

    void onPreExecute();
}
