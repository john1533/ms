package com.mobcent.lowest.android.ui.module.weather.helper;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.weather.activity.WeatherActivity;
import com.mobcent.lowest.android.ui.module.weather.delegate.WeatherTaskDelegate;
import com.mobcent.lowest.android.ui.module.weather.observer.WeatherObserver;
import com.mobcent.lowest.android.ui.module.weather.task.WeatherDataTask;
import com.mobcent.lowest.android.ui.utils.MCWeatherUtil;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.weather.db.WeatherSharedPreferencesDB;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.Observable;
import java.util.Observer;

public class WeatherWidgetHelper {
    public static View createWeatherWidget(Context context, WeatherModel weatherModel, final int qs) {
        MCResource resource = MCResource.getInstance(context);
        if (weatherModel == null) {
            weatherModel = new WeatherModel();
            weatherModel.setCity(resource.getString("mc_weather_querying"));
        }
        final View weatherBox = LayoutInflater.from(context).inflate(resource.getLayoutId("mc_weather_widget"), null);
        changeWeatherView(weatherBox, weatherModel);
        weatherBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WeatherActivity.class);
                intent.putExtra("qs", qs);
                v.getContext().startActivity(intent);
            }
        });
        WeatherObserver.getInstance().addObserver(new Observer() {
            public void update(Observable observable, Object data) {
                if (data instanceof WeatherModel) {
                    WeatherWidgetHelper.changeWeatherView(weatherBox, (WeatherModel) data);
                }
            }
        });
        return weatherBox;
    }

    private static void changeWeatherView(final View weatherBox, final WeatherModel weatherModel) {
        if (weatherModel != null) {
            weatherBox.post(new Runnable() {
                public void run() {
                    MCResource resource = MCResource.getInstance(weatherBox.getContext());
                    TextView dateText = (TextView) weatherBox.findViewById(resource.getViewId("weather_date_text"));
                    TextView tempText = (TextView) weatherBox.findViewById(resource.getViewId("weather_temp_text"));
                    ImageView weatherImg = (ImageView) weatherBox.findViewById(resource.getViewId("weather_img"));
                    if (TextUtils.isEmpty(weatherModel.getCity())) {
                        dateText.setVisibility(8);
                    } else {
                        dateText.setVisibility(0);
                        dateText.setText(weatherModel.getCity());
                    }
                    if (TextUtils.isEmpty(weatherModel.getTemperature())) {
                        tempText.setVisibility(8);
                    } else {
                        tempText.setVisibility(0);
                        tempText.setText(weatherModel.getTemperature());
                    }
                    weatherImg.setBackgroundResource(MCWeatherUtil.getInstance(weatherBox.getContext()).getWeatherIcon(weatherModel.getPicInfo()));
                }
            });
        }
    }

    public static void requestWeatherDataAsync(final Context context, final int queryType, final boolean forceRefresh, final WeatherTaskDelegate delegate) {
        CityModel cityModel = null;
        if (queryType == 1) {
            cityModel = WeatherSharedPreferencesDB.getInstance(context.getApplicationContext()).getCityModel();
        }
        if (cityModel == null) {

            return;
        }
        new WeatherDataTask(context, queryType, cityModel, delegate).execute(new Boolean[]{Boolean.valueOf(forceRefresh)});
    }
}
