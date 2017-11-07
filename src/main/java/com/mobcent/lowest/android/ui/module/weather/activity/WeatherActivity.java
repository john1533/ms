package com.mobcent.lowest.android.ui.module.weather.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.weather.constant.WeatherIntentConstant;
import com.mobcent.lowest.android.ui.module.weather.delegate.WeatherTaskDelegate;
import com.mobcent.lowest.android.ui.module.weather.observer.WeatherObserver;
import com.mobcent.lowest.android.ui.module.weather.task.WeatherDataTask;
import com.mobcent.lowest.android.ui.utils.MCAnimationUtils;
import com.mobcent.lowest.android.ui.utils.MCWeatherUtil;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.weather.db.WeatherSharedPreferencesDB;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import java.util.List;

public class WeatherActivity extends BaseWeatherFragmentActivity {
    public String TAG = "WeatherActivity";
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
    private RelativeLayout mainBox;
    private MCResource mcResource;
    private TextView morningExerciseIndex;
    private int qs = 0;
    private Button refreshButton;
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
    private TextView travelIndex;
    private TextView uvIndex;
    private List<WeatherModel> weatherList;

    protected void initData() {
        this.mcResource = MCResource.getInstance(getApplicationContext());
        this.weatherList = LowestManager.getInstance().getConfig().getWeatherListCache();
        setWeatherModels(this.weatherList);
        this.qs = getIntent().getIntExtra("qs", 0);
    }

    protected void initViews() {
        setContentView(this.mcResource.getLayoutId("mc_weather_activity"));
        this.backBtn = (Button) findViewById(this.mcResource.getViewId("weather_detail_back_btn"));
        this.refreshButton = (Button) findViewById(this.mcResource.getViewId("weather_refresh_btn"));
        this.titleText = (TextView) findViewById(this.mcResource.getViewId("title_text"));
        this.arrowView = findViewById(this.mcResource.getViewId("arrow_view"));
        this.todayDate = (TextView) findViewById(this.mcResource.getViewId("weather_detail_date"));
        this.todayInfo = (TextView) findViewById(this.mcResource.getViewId("weather_detail_info"));
        this.todayTemperature = (TextView) findViewById(this.mcResource.getViewId("weather_detail_temperature"));
        this.todayIcon = (ImageView) findViewById(this.mcResource.getViewId("weather_detail_icon"));
        this.dressingIndex = (TextView) findViewById(this.mcResource.getViewId("dressing_index"));
        this.comfortIndex = (TextView) findViewById(this.mcResource.getViewId("comfort_index"));
        this.morningExerciseIndex = (TextView) findViewById(this.mcResource.getViewId("morning_exercise_index"));
        this.coldIndex = (TextView) findViewById(this.mcResource.getViewId("cold_index"));
        this.airConditioningIndex = (TextView) findViewById(this.mcResource.getViewId("air_conditioning_index"));
        this.carWashIndex = (TextView) findViewById(this.mcResource.getViewId("car_wash_index"));
        this.airPollutionIndex = (TextView) findViewById(this.mcResource.getViewId("air_pollution_index"));
        this.indexOfBeer = (TextView) findViewById(this.mcResource.getViewId("index_of_beer"));
        this.dryingIndex = (TextView) findViewById(this.mcResource.getViewId("drying_index"));
        this.travelIndex = (TextView) findViewById(this.mcResource.getViewId("travel_index"));
        this.uvIndex = (TextView) findViewById(this.mcResource.getViewId("uv_index"));
        this.dressSuggest = (TextView) findViewById(this.mcResource.getViewId("dressing_suggest"));
        this.firstDate = (TextView) findViewById(this.mcResource.getViewId("weather_future_first_day_week"));
        this.firstInfo = (TextView) findViewById(this.mcResource.getViewId("weather_future_first_day_weather"));
        this.firstTemperature = (TextView) findViewById(this.mcResource.getViewId("weather_future_first_day_temperature"));
        this.firstManner = (TextView) findViewById(this.mcResource.getViewId("weather_future_first_day_manner"));
        this.firstIcon = (ImageView) findViewById(this.mcResource.getViewId("weather_future_first_day_icon"));
        this.secondDate = (TextView) findViewById(this.mcResource.getViewId("weather_future_second_day_week"));
        this.secondInfo = (TextView) findViewById(this.mcResource.getViewId("weather_future_second_day_weather"));
        this.secondTemperature = (TextView) findViewById(this.mcResource.getViewId("weather_future_second_day_temperature"));
        this.secondManner = (TextView) findViewById(this.mcResource.getViewId("weather_future_second_day_manner"));
        this.secondIcon = (ImageView) findViewById(this.mcResource.getViewId("weather_future_second_day_icon"));
        this.thirdDate = (TextView) findViewById(this.mcResource.getViewId("weather_future_third_day_week"));
        this.thirdInfo = (TextView) findViewById(this.mcResource.getViewId("weather_future_third_day_weather"));
        this.thirdTemperature = (TextView) findViewById(this.mcResource.getViewId("weather_future_third_day_temperature"));
        this.thirdManner = (TextView) findViewById(this.mcResource.getViewId("weather_future_third_day_manner"));
        this.thirdIcon = (ImageView) findViewById(this.mcResource.getViewId("weather_future_third_day_icon"));
        this.mainBox = (RelativeLayout) findViewById(this.mcResource.getViewId("weather_main_box"));
        if (this.qs == 0) {
            this.arrowView.setVisibility(8);
        } else {
            this.arrowView.setVisibility(0);
        }
        updateAllUI();
    }

    protected void initActions() {
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WeatherActivity.this.finish();
            }
        });
        this.titleText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (WeatherActivity.this.qs == 1) {
                    WeatherActivity.this.startActivityForResult(new Intent(WeatherActivity.this, WeatherCityActivity.class), 1);
                }
            }
        });
        this.refreshButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                CityModel cityModel = new CityModel();
                if (WeatherActivity.this.qs != 0) {
                    CityModel localCityModel = WeatherSharedPreferencesDB.getInstance(WeatherActivity.this.getApplicationContext()).getCityModel();
                    if (localCityModel != null) {
                        WeatherActivity.this.queryWeatherInfo(localCityModel);
                        return;
                    }
//                    BDLocation location = LowestManager.getInstance().getConfig().getLocation();
//                    if (location == null) {
//
//                        return;
//                    }
//                    cityModel.setCityName(location.getCity());
//                    cityModel.setLatitude(location.getLatitude());
//                    cityModel.setLongitude(location.getLongitude());
                    WeatherActivity.this.queryWeatherInfo(cityModel);
                    return;
                }
                WeatherActivity.this.queryWeatherInfo(cityModel);
            }
        });
        if (this.weatherList == null || this.weatherList.isEmpty()) {
            this.refreshButton.performClick();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mainBox.setBackgroundDrawable(null);
    }

    private void updateAllUI() {
        changeTitle();
        if (this.todayWeatherModel != null) {
            this.todayDate.setText(this.todayWeatherModel.getCity() + " " + this.todayWeatherModel.getDate() + " " + this.todayWeatherModel.getWeek());
            this.todayInfo.setText(this.todayWeatherModel.getWeather());
            this.todayTemperature.setText(this.todayWeatherModel.getTemperature());
            this.todayIcon.setBackgroundResource(MCWeatherUtil.getInstance(this).getWeatherIcon(this.todayWeatherModel.getPicInfo()));
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
            this.firstIcon.setBackgroundResource(MCWeatherUtil.getInstance(this).getWeatherIcon(this.firstWeatherModel.getPicInfo()));
        }
        if (this.secondWeatherModel != null) {
            this.secondDate.setText(this.secondWeatherModel.getWeek());
            this.secondInfo.setText(this.secondWeatherModel.getWeather());
            this.secondTemperature.setText(this.secondWeatherModel.getTemperature());
            this.secondManner.setText(this.secondWeatherModel.getWind());
            this.secondIcon.setBackgroundResource(MCWeatherUtil.getInstance(this).getWeatherIcon(this.secondWeatherModel.getPicInfo()));
        }
        if (this.thirdWeatherModel != null) {
            this.thirdDate.setText(this.thirdWeatherModel.getWeek());
            this.thirdInfo.setText(this.thirdWeatherModel.getWeather());
            this.thirdTemperature.setText(this.thirdWeatherModel.getTemperature());
            this.thirdManner.setText(this.thirdWeatherModel.getWind());
            this.thirdIcon.setBackgroundResource(MCWeatherUtil.getInstance(this).getWeatherIcon(this.thirdWeatherModel.getPicInfo()));
        }
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

    private void queryWeatherInfo(CityModel cityModel) {
        if (cityModel != null) {
            clearAsyncTask();
            new WeatherDataTask(getApplicationContext(), this.qs, cityModel, new WeatherTaskDelegate() {
                public void onPreExecute() {
                    MCAnimationUtils.rotateView(WeatherActivity.this.refreshButton);
                }

                public void onPostExecute(List<WeatherModel> result) {
                    WeatherActivity.this.refreshButton.clearAnimation();
                    if (result == null || result.isEmpty()) {
                        WeatherActivity.this.showMessage(WeatherActivity.this.mcResource.getString("mc_weather_query_error"));
                        return;
                    }
                    WeatherActivity.this.setWeatherModels(result);
                    if (WeatherActivity.this.todayWeatherModel != null) {
                        WeatherObserver.getInstance().notifyObservers(WeatherActivity.this.todayWeatherModel);
                        LowestManager.getInstance().getConfig().setWeatherListCache(result);
                    }
                    WeatherActivity.this.updateAllUI();
                }
            }).execute(new Boolean[]{Boolean.valueOf(true)});
        }
    }

    private void changeTitle() {
        this.handler.post(new Runnable() {
            public void run() {
                if (WeatherActivity.this.todayWeatherModel != null) {
                    WeatherActivity.this.titleText.setText(WeatherActivity.this.todayWeatherModel.getCity());
                } else {
                    WeatherActivity.this.titleText.setText(WeatherActivity.this.mcResource.getString("mc_weather_title_text"));
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            queryWeatherInfo((CityModel) intent.getSerializableExtra(WeatherIntentConstant.INTENT_CITY_MODEL));
        }
    }
}
