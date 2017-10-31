package com.mobcent.lowest.android.ui.module.weather.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.weather.constant.WeatherIntentConstant;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseWeatherFragmentActivity extends FragmentActivity implements WeatherIntentConstant {
    private InputMethodManager imm;
    protected LayoutInflater inflater;
    protected List<AsyncTask<?, ?, ?>> loadDataAsyncTasks;
    protected MCResource mcResource;

    protected abstract void initActions();

    protected abstract void initData();

    protected abstract void initViews();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.loadDataAsyncTasks = new ArrayList();
        this.imm = (InputMethodManager) getSystemService("input_method");
        this.mcResource = MCResource.getInstance(getApplicationContext());
        this.inflater = LayoutInflater.from(this);
        initData();
        initViews();
        initActions();
    }

    protected void clearAsyncTask() {
        int size = this.loadDataAsyncTasks.size();
        for (int i = 0; i < size; i++) {
            ((AsyncTask) this.loadDataAsyncTasks.get(i)).cancel(true);
        }
        this.loadDataAsyncTasks.clear();
    }

    protected void addAsyncTask(AsyncTask<?, ?, ?> asyncTask) {
        this.loadDataAsyncTasks.add(asyncTask);
    }

    protected void showSoftKeyboard(View view) {
        view.requestFocus();
        this.imm.showSoftInput(view, 1);
    }

    protected void showSoftKeyboard() {
        this.imm.showSoftInput(getCurrentFocus(), 1);
    }

    protected void hideSoftKeyboard() {
        this.imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
    }

    protected void showMessage(String str) {
        Toast.makeText(this, str, 0).show();
    }
}
