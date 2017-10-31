package com.mobcent.lowest.android.ui.module.weather.task;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.lowest.android.ui.module.weather.delegate.WeatherTaskDelegate;
import com.mobcent.lowest.android.ui.module.weather.observer.WeatherObserver;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import com.mobcent.lowest.module.weather.service.WeatherService;
import com.mobcent.lowest.module.weather.service.impl.WeatherServiceImpl;
import java.util.List;

public class WeatherDataTask extends AsyncTask<Boolean, Void, List<WeatherModel>> {
    private CityModel cityModel;
    private WeatherTaskDelegate delegate;
    private int queryType;
    private WeatherService weatherService = null;

    public WeatherDataTask(Context context, int queryType, CityModel cityModel, WeatherTaskDelegate delegate) {
        this.delegate = delegate;
        this.weatherService = new WeatherServiceImpl(context.getApplicationContext());
        this.queryType = queryType;
        this.cityModel = cityModel;
    }

    protected void onPreExecute() {
        if (this.delegate != null) {
            this.delegate.onPreExecute();
        }
    }

    protected List<WeatherModel> doInBackground(Boolean... params) {
        boolean isRefresh = true;
        try {
            isRefresh = params[0].booleanValue();
        } catch (Exception e) {
        }
        return this.weatherService.getWeatherInfo(this.queryType, this.cityModel.getCityId(), this.cityModel.getCityName(), this.cityModel.getLongitude(), this.cityModel.getLatitude(), isRefresh);
    }

    protected void onPostExecute(List<WeatherModel> result) {
        if (!(result == null || result.isEmpty())) {
            LowestManager.getInstance().getConfig().setWeatherListCache(result);
            WeatherObserver.getInstance().notifyObservers(result.get(0));
        }
        if (this.delegate != null) {
            this.delegate.onPostExecute(result);
        }
    }
}
