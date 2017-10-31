package com.mobcent.lowest.android.ui.module.weather.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.weather.constant.WeatherIntentConstant;
import com.mobcent.lowest.module.weather.model.CityModel;
import com.mobcent.lowest.module.weather.service.WeatherService;
import com.mobcent.lowest.module.weather.service.impl.WeatherServiceImpl;
import java.util.ArrayList;
import java.util.List;

public class WeatherCitySearchActivity extends BaseWeatherFragmentActivity {
    public String TAG = "WeatherCityActivity";
    private Button backBtn;
    private List<CityModel> cityList = null;
    private CityAdapter cityListAdapter = null;
    private ListView cityListView;
    private EditText cityNameEidt;
    private CityModel currentCityModel;
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v == WeatherCitySearchActivity.this.searchBtn) {
                WeatherCitySearchActivity.this.clearAsyncTask();
                WeatherCitySearchActivity.this.addAsyncTask(new LoadWeatherTask().execute(new Void[0]));
            } else if (v == WeatherCitySearchActivity.this.backBtn) {
                WeatherCitySearchActivity.this.onBackPressed();
            }
        }
    };
    private Button searchBtn;
    private WeatherService weatherService = null;

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
            return WeatherCitySearchActivity.this.cityList.size();
        }

        public CityModel getItem(int position) {
            return (CityModel) WeatherCitySearchActivity.this.cityList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            final CityModel cityModel = getItem(position);
            if (convertView == null) {
                convertView = WeatherCitySearchActivity.this.inflater.inflate(WeatherCitySearchActivity.this.mcResource.getLayoutId("mc_weather_city_activity_item"), null);
                holder = new Holder();
                holder.setCityText((TextView) convertView.findViewById(WeatherCitySearchActivity.this.mcResource.getViewId("city_text")));
                holder.getCityText().setGravity(19);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.getCityText().setText(cityModel.getCityName());
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WeatherCitySearchActivity.this.currentCityModel = cityModel;
                    WeatherCitySearchActivity.this.onCityClickDo(WeatherCitySearchActivity.this.currentCityModel);
                }
            });
            return convertView;
        }
    }

    class LoadWeatherTask extends AsyncTask<Void, Void, List<CityModel>> {
        LoadWeatherTask() {
        }

        protected void onPreExecute() {
            WeatherCitySearchActivity.this.hideSoftKeyboard();
        }

        protected List<CityModel> doInBackground(Void... params) {
            return WeatherCitySearchActivity.this.weatherService.searchCityList(WeatherCitySearchActivity.this.cityNameEidt.getText().toString());
        }

        protected void onPostExecute(List<CityModel> result) {
            if (result == null || result.isEmpty()) {
                WeatherCitySearchActivity.this.showMessage(WeatherCitySearchActivity.this.mcResource.getString("mc_forum_weather_search_city_not_found"));
            } else {
                WeatherCitySearchActivity.this.cityList.clear();
                WeatherCitySearchActivity.this.cityList.addAll(result);
            }
            WeatherCitySearchActivity.this.cityListAdapter.notifyDataSetChanged();
        }
    }

    protected void initData() {
        this.weatherService = new WeatherServiceImpl(this);
        this.cityList = new ArrayList();
        this.currentCityModel = new CityModel();
    }

    protected void initViews() {
        setContentView(this.mcResource.getLayoutId("mc_weather_city_search_activity"));
        this.cityListView = (ListView) findViewById(this.mcResource.getViewId("city_list"));
        this.cityNameEidt = (EditText) findViewById(this.mcResource.getViewId("city_name_edit"));
        this.searchBtn = (Button) findViewById(this.mcResource.getViewId("search_btn"));
        this.backBtn = (Button) findViewById(this.mcResource.getViewId("weather_detail_back_btn"));
        this.cityListAdapter = new CityAdapter();
        this.cityListView.setAdapter(this.cityListAdapter);
    }

    protected void initActions() {
        this.searchBtn.setOnClickListener(this.onClickListener);
        this.backBtn.setOnClickListener(this.onClickListener);
        this.cityNameEidt.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 84 || keyCode == 66) {
                    WeatherCitySearchActivity.this.cityNameEidt.setFocusable(false);
                    WeatherCitySearchActivity.this.cityNameEidt.setFocusableInTouchMode(true);
                    WeatherCitySearchActivity.this.searchBtn.performClick();
                }
                WeatherCitySearchActivity.this.cityNameEidt.setFocusable(true);
                return false;
            }
        });
        showSoftKeyboard(this.cityNameEidt);
    }

    private void onCityClickDo(CityModel cityModel) {
        if (cityModel != null) {
            Intent intent = new Intent();
            intent.putExtra(WeatherIntentConstant.INTENT_CITY_MODEL, cityModel);
            setResult(-1, intent);
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
    }
}
