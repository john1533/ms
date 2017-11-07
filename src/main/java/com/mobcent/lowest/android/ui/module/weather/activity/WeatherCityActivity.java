package com.mobcent.lowest.android.ui.module.weather.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.weather.constant.WeatherIntentConstant;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.module.weather.db.WeatherSharedPreferencesDB;
import com.mobcent.lowest.module.weather.model.CityModel;
import java.util.ArrayList;
import java.util.List;

public class WeatherCityActivity extends BaseWeatherFragmentActivity {
    private static final int WEATHER_CITY_ACTIVITY = 1;
    public String TAG = "WeatherCityActivity";
    private Button backBtn;
    private CityAdapter cityGridAdapter = null;
    private List<CityModel> cityGridList = null;
    private CityModel currentCityModel;
    private GridView hotCityGrid;
    private TextView locationCityText;

    private MCLocationUtil locationUtil = null;
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v == WeatherCityActivity.this.locationCityText) {
                WeatherCityActivity.this.onCityClickDo(WeatherCityActivity.this.currentCityModel);
            } else if (v == WeatherCityActivity.this.searchEdit) {
                WeatherCityActivity.this.startActivityForResult(new Intent(WeatherCityActivity.this, WeatherCitySearchActivity.class), 1);
            } else if (v == WeatherCityActivity.this.backBtn) {
                WeatherCityActivity.this.onBackPressed();
            }
        }
    };
    private EditText searchEdit;

    class CityAdapter extends BaseAdapter {

        class Holder {
            public TextView cityText;

            Holder() {
            }

            public TextView getCityText() {
                return this.cityText;
            }

            public void setCityText(TextView cityText) {
                this.cityText = cityText;
            }
        }

        public int getCount() {
            return WeatherCityActivity.this.cityGridList.size();
        }

        public CityModel getItem(int position) {
            return (CityModel) WeatherCityActivity.this.cityGridList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            final CityModel cityModel = getItem(position);
            if (convertView == null) {
                convertView = WeatherCityActivity.this.inflater.inflate(WeatherCityActivity.this.mcResource.getLayoutId("mc_weather_city_activity_item"), null);
                holder = new Holder();
                holder.setCityText((TextView) convertView.findViewById(WeatherCityActivity.this.mcResource.getViewId("city_text")));
                holder.getCityText().setGravity(17);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.getCityText().setText(cityModel.getCityName());
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WeatherCityActivity.this.currentCityModel = cityModel;
                    WeatherCityActivity.this.onCityClickDo(WeatherCityActivity.this.currentCityModel);
                }
            });
            return convertView;
        }
    }

    protected void initData() {
        this.cityGridList = new ArrayList();
        setLocalData();
        this.currentCityModel = new CityModel();
    }

    protected void initViews() {
        setContentView(this.mcResource.getLayoutId("mc_weather_city_activity"));
        this.searchEdit = (EditText) findViewById(this.mcResource.getViewId("city_name_edit"));
        this.hotCityGrid = (GridView) findViewById(this.mcResource.getViewId("hot_city_grid"));
        this.backBtn = (Button) findViewById(this.mcResource.getViewId("weather_detail_back_btn"));
        this.locationCityText = (TextView) findViewById(this.mcResource.getViewId("location_city_text"));
        this.cityGridAdapter = new CityAdapter();
        this.hotCityGrid.setAdapter(this.cityGridAdapter);
    }

    protected void initActions() {
        this.searchEdit.setOnClickListener(this.onClickListener);
        this.locationCityText.setOnClickListener(this.onClickListener);
        this.backBtn.setOnClickListener(this.onClickListener);
        this.locationCityText.setText("......");
        this.locationUtil = MCLocationUtil.getInstance(getApplicationContext());
    }

    private void onCityClickDo(CityModel cityModel) {
        if (cityModel != null) {
            this.currentCityModel = cityModel;
            saveCityModel(this.currentCityModel);
        }
    }

    private void setLocalData() {
        String[] cityName = getResources().getStringArray(this.mcResource.getArrayId("mc_forum_weather_hot_city_array"));
        this.cityGridList.clear();
        for (String cityName2 : cityName) {
            CityModel cityModel = new CityModel();
            cityModel.setCityName(cityName2);
            this.cityGridList.add(cityModel);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            this.currentCityModel = (CityModel) intent.getSerializableExtra(WeatherIntentConstant.INTENT_CITY_MODEL);
            saveCityModel(this.currentCityModel);
            finish();
        }
    }

    private void saveCityModel(CityModel cityModel) {
        WeatherSharedPreferencesDB.getInstance(this).setCityModel(cityModel);
        Intent intent = new Intent();
        intent.putExtra(WeatherIntentConstant.INTENT_CITY_MODEL, cityModel);
        setResult(-1, intent);
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
    }
}
