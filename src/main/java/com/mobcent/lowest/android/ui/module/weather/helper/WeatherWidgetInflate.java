package com.mobcent.lowest.android.ui.module.weather.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.mobcent.lowest.android.ui.module.weather.activity.WeatherCityActivity;
import com.mobcent.lowest.android.ui.module.weather.constant.WeatherIntentConstant;
import com.mobcent.lowest.android.ui.module.weather.delegate.WeatherTaskDelegate;
import com.mobcent.lowest.android.ui.module.weather.observer.WeatherObserver;
import com.mobcent.lowest.android.ui.module.weather.task.WeatherDataTask;
import com.mobcent.lowest.android.ui.utils.MCAnimationUtils;
import com.mobcent.lowest.android.ui.utils.MCWeatherUtil;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.weather.db.WeatherSharedPreferencesDB;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.List;

public class WeatherWidgetInflate {
    public String TAG = "WeatherActivity";
    private Activity activity;
    private TextView airConditioningIndex;
    private TextView airPollutionIndex;
    private View arrowView;
    private Button backBtn;
    private TextView carWashIndex;
    private TextView coldIndex;
    private TextView comfortIndex;
    private TextView dressSuggest;
    private TextView dressingIndex;
    private TextView dryingIndex;
    private TextView firstDate;
    private ImageView firstIcon;
    private TextView firstInfo;
    private TextView firstManner;
    private TextView firstTemperature;
    private WeatherModel firstWeatherModel = null;
    private Handler handler = new Handler();
    private TextView indexOfBeer;
    private LayoutInflater inflater;
    private RelativeLayout mainBox;
    private TextView morningExerciseIndex;
    private int qs = 0;
    private Button refreshButton;
    private MCResource resource;
    private View rootView = null;
    private TextView secondDate;
    private ImageView secondIcon;
    private TextView secondInfo;
    private TextView secondManner;
    private TextView secondTemperature;
    private WeatherModel secondWeatherModel = null;
    private TextView thirdDate;
    private ImageView thirdIcon;
    private TextView thirdInfo;
    private TextView thirdManner;
    private TextView thirdTemperature;
    private WeatherModel thirdWeatherModel = null;
    private TextView titleText;
    private TextView todayDate;
    private ImageView todayIcon;
    private TextView todayInfo;
    private TextView todayTemperature;
    private WeatherModel todayWeatherModel = null;
    private View topBar = null;
    private TextView travelIndex;
    private TextView uvIndex;
    private List<WeatherModel> weatherList;

    public WeatherWidgetInflate(Activity activity) {
        this.activity = activity;
        this.resource = MCResource.getInstance(activity.getApplicationContext());
        this.inflater = LayoutInflater.from(activity);
        this.weatherList = LowestManager.getInstance().getConfig().getWeatherListCache();
    }

    public String getLayoutName() {
        return "mc_weather_activity";
    }

    public void setUpView(View rootView, int qs) {
        this.rootView = rootView;
        this.qs = qs;
        initViews();
        initViewAction();
    }

    public void hideTopBar(boolean isHide) {
        if (this.topBar == null) {
            return;
        }
        if (isHide) {
            this.topBar.setVisibility(8);
        } else {
            this.topBar.setVisibility(0);
        }
    }

    public void initViews() {
        this.topBar = findViewById("weather_detail_top_box");
        this.backBtn = (Button) findViewById("weather_detail_back_btn");
        this.refreshButton = (Button) findViewById("weather_refresh_btn");
        this.titleText = (TextView) findViewById("title_text");
        this.arrowView = findViewById("arrow_view");
        this.todayDate = (TextView) findViewById("weather_detail_date");
        this.todayInfo = (TextView) findViewById("weather_detail_info");
        this.todayTemperature = (TextView) findViewById("weather_detail_temperature");
        this.todayIcon = (ImageView) findViewById("weather_detail_icon");
        this.dressingIndex = (TextView) findViewById("dressing_index");
        this.comfortIndex = (TextView) findViewById("comfort_index");
        this.morningExerciseIndex = (TextView) findViewById("morning_exercise_index");
        this.coldIndex = (TextView) findViewById("cold_index");
        this.airConditioningIndex = (TextView) findViewById("air_conditioning_index");
        this.carWashIndex = (TextView) findViewById("car_wash_index");
        this.airPollutionIndex = (TextView) findViewById("air_pollution_index");
        this.indexOfBeer = (TextView) findViewById("index_of_beer");
        this.dryingIndex = (TextView) findViewById("drying_index");
        this.travelIndex = (TextView) findViewById("travel_index");
        this.uvIndex = (TextView) findViewById("uv_index");
        this.dressSuggest = (TextView) findViewById("dressing_suggest");
        this.firstDate = (TextView) findViewById("weather_future_first_day_week");
        this.firstInfo = (TextView) findViewById("weather_future_first_day_weather");
        this.firstTemperature = (TextView) findViewById("weather_future_first_day_temperature");
        this.firstManner = (TextView) findViewById("weather_future_first_day_manner");
        this.firstIcon = (ImageView) findViewById("weather_future_first_day_icon");
        this.secondDate = (TextView) findViewById("weather_future_second_day_week");
        this.secondInfo = (TextView) findViewById("weather_future_second_day_weather");
        this.secondTemperature = (TextView) findViewById("weather_future_second_day_temperature");
        this.secondManner = (TextView) findViewById("weather_future_second_day_manner");
        this.secondIcon = (ImageView) findViewById("weather_future_second_day_icon");
        this.thirdDate = (TextView) findViewById("weather_future_third_day_week");
        this.thirdInfo = (TextView) findViewById("weather_future_third_day_weather");
        this.thirdTemperature = (TextView) findViewById("weather_future_third_day_temperature");
        this.thirdManner = (TextView) findViewById("weather_future_third_day_manner");
        this.thirdIcon = (ImageView) findViewById("weather_future_third_day_icon");
        this.mainBox = (RelativeLayout) findViewById("weather_main_box");
        if (this.qs == 0) {
            this.arrowView.setVisibility(8);
        } else {
            this.arrowView.setVisibility(0);
        }
    }

    private void initViewAction() {
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WeatherWidgetInflate.this.activity.finish();
            }
        });
        this.titleText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (WeatherWidgetInflate.this.qs == 1) {
                    WeatherWidgetInflate.this.activity.startActivityForResult(new Intent(WeatherWidgetInflate.this.activity, WeatherCityActivity.class), 1);
                }
            }
        });
        this.refreshButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CityModel cityModel = new CityModel();
                if (WeatherWidgetInflate.this.qs != 0) {
                    CityModel localCityModel = WeatherSharedPreferencesDB.getInstance(WeatherWidgetInflate.this.activity.getApplicationContext()).getCityModel();
                    if (localCityModel != null) {
                        WeatherWidgetInflate.this.queryWeatherInfo(localCityModel);
                        return;
                    }
                    BDLocation location = LowestManager.getInstance().getConfig().getLocation();
                    if (location == null) {
                        MCLocationUtil.getInstance(WeatherWidgetInflate.this.activity.getApplicationContext()).requestLocation(new LocationDelegate() {
                            public void onReceiveLocation(BDLocation location) {
                                if (location != null) {
                                    CityModel cityModel = new CityModel();
                                    cityModel.setCityName(location.getCity());
                                    cityModel.setLatitude(location.getLatitude());
                                    cityModel.setLongitude(location.getLongitude());
                                    WeatherWidgetInflate.this.queryWeatherInfo(cityModel);
                                }
                            }
                        });
                        return;
                    }
                    cityModel.setCityName(location.getCity());
                    cityModel.setCityId(location.getCityCode());
                    cityModel.setLatitude(location.getLatitude());
                    cityModel.setLongitude(location.getLongitude());
                    WeatherWidgetInflate.this.queryWeatherInfo(cityModel);
                    return;
                }
                WeatherWidgetInflate.this.queryWeatherInfo(cityModel);
            }
        });
        if (this.weatherList == null || this.weatherList.isEmpty()) {
            this.refreshButton.performClick();
            return;
        }
        if (this.todayWeatherModel == null) {
            setWeatherModels(this.weatherList);
        }
        updateAllUI();
    }

    private void queryWeatherInfo(CityModel cityModel) {
        if (cityModel != null) {
            new WeatherDataTask(this.activity, this.qs, cityModel, new WeatherTaskDelegate() {
                public void onPreExecute() {
                    MCAnimationUtils.rotateView(WeatherWidgetInflate.this.refreshButton);
                }

                public void onPostExecute(List<WeatherModel> result) {
                    WeatherWidgetInflate.this.refreshButton.clearAnimation();
                    if (result == null || result.isEmpty()) {
                        WeatherWidgetInflate.this.showMessage(WeatherWidgetInflate.this.resource.getString("mc_weather_query_error"));
                        return;
                    }
                    WeatherWidgetInflate.this.setWeatherModels(result);
                    if (WeatherWidgetInflate.this.todayWeatherModel != null) {
                        WeatherObserver.getInstance().notifyObservers(WeatherWidgetInflate.this.todayWeatherModel);
                        LowestManager.getInstance().getConfig().setWeatherListCache(result);
                    }
                    WeatherWidgetInflate.this.updateAllUI();
                }
            }).execute(new Boolean[]{Boolean.valueOf(true)});
        }
    }

    private void showMessage(String msg) {
        Toast.makeText(this.activity, msg, 0).show();
    }

    private void setWeatherModels(List<WeatherModel> weatherList) {
        if (weatherList != null) {
            if (!weatherList.isEmpty()) {
                this.todayWeatherModel = (WeatherModel) weatherList.get(0);
            }
            if (weatherList.size() >= 2) {
                this.firstWeatherModel = (WeatherModel) weatherList.get(1);
            }
            if (weatherList.size() >= 3) {
                this.secondWeatherModel = (WeatherModel) weatherList.get(2);
            }
            if (weatherList.size() >= 4) {
                this.thirdWeatherModel = (WeatherModel) weatherList.get(3);
            }
        }
    }

    private void updateAllUI() {
        changeTitle();
        if (this.todayWeatherModel != null) {
            this.todayDate.setText(this.todayWeatherModel.getCity() + " " + this.todayWeatherModel.getDate() + " " + this.todayWeatherModel.getWeek());
            this.todayInfo.setText(this.todayWeatherModel.getWeather());
            this.todayTemperature.setText(this.todayWeatherModel.getTemperature());
            this.todayIcon.setBackgroundResource(MCWeatherUtil.getInstance(this.activity).getWeatherIcon(this.todayWeatherModel.getPicInfo()));
            this.uvIndex.setText(this.todayWeatherModel.getuVIndex());
            this.dressSuggest.setText(this.todayWeatherModel.getDressSuggest());
            this.dressingIndex.setText(this.todayWeatherModel.getDressIndex());
            this.carWashIndex.setText(this.todayWeatherModel.getWashCarIndex());
        }
        if (this.firstWeatherModel != null) {
            this.firstDate.setText(this.firstWeatherModel.getWeek());
            this.firstInfo.setText(this.firstWeatherModel.getWeather());
            this.firstTemperature.setText(this.firstWeatherModel.getTemperature());
            this.firstManner.setText(this.firstWeatherModel.getWind());
            this.firstIcon.setBackgroundResource(MCWeatherUtil.getInstance(this.activity).getWeatherIcon(this.firstWeatherModel.getPicInfo()));
        }
        if (this.secondWeatherModel != null) {
            this.secondDate.setText(this.secondWeatherModel.getWeek());
            this.secondInfo.setText(this.secondWeatherModel.getWeather());
            this.secondTemperature.setText(this.secondWeatherModel.getTemperature());
            this.secondManner.setText(this.secondWeatherModel.getWind());
            this.secondIcon.setBackgroundResource(MCWeatherUtil.getInstance(this.activity).getWeatherIcon(this.secondWeatherModel.getPicInfo()));
        }
        if (this.thirdWeatherModel != null) {
            this.thirdDate.setText(this.thirdWeatherModel.getWeek());
            this.thirdInfo.setText(this.thirdWeatherModel.getWeather());
            this.thirdTemperature.setText(this.thirdWeatherModel.getTemperature());
            this.thirdManner.setText(this.thirdWeatherModel.getWind());
            this.thirdIcon.setBackgroundResource(MCWeatherUtil.getInstance(this.activity).getWeatherIcon(this.thirdWeatherModel.getPicInfo()));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == -1) {
            queryWeatherInfo((CityModel) intent.getSerializableExtra(WeatherIntentConstant.INTENT_CITY_MODEL));
        }
    }

    private void changeTitle() {
        this.handler.post(new Runnable() {
            public void run() {
                if (WeatherWidgetInflate.this.todayWeatherModel != null) {
                    WeatherWidgetInflate.this.titleText.setText(WeatherWidgetInflate.this.todayWeatherModel.getCity());
                } else {
                    WeatherWidgetInflate.this.titleText.setText(WeatherWidgetInflate.this.resource.getString("mc_weather_title_text"));
                }
            }
        });
    }

    private View findViewById(String name) {
        if (this.rootView != null) {
            return this.rootView.findViewById(this.resource.getViewId(name));
        }
        return null;
    }
}
