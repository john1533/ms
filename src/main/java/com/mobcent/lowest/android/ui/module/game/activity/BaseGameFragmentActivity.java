package com.mobcent.lowest.android.ui.module.game.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.game.constant.GameConstance;
import com.mobcent.lowest.android.ui.module.game.fragment.BaseGameFragment;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGameFragmentActivity extends FragmentActivity implements GameConstance {
    protected Bundle bundle;
    protected Context context;
    protected int currentPosition = 0;
    protected FragmentStatePagerAdapter gameCenterPagerAdapter;
    protected InputMethodManager imm;
    protected LayoutInflater inflater;
    protected Handler mHandler = new Handler();
    protected MCResource mcResource;
    private List<Integer> positionList;
    protected List<AsyncTask<?, ?, ?>> taskList;
    protected Toast toast;

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initWidgetActions();

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.context = getApplicationContext();
        this.mcResource = MCResource.getInstance(this.context);
        this.positionList = new ArrayList();
        this.inflater = LayoutInflater.from(this);
        this.imm = (InputMethodManager) getSystemService("input_method");
        this.toast = Toast.makeText(this.context, "", 0);
        initData();
        initViews();
        initWidgetActions();
    }

    protected void warById(String name) {
        Toast.makeText(getApplicationContext(), this.mcResource.getStringId(name), 0).show();
    }

    protected void showSoftKeyboard(View view) {
        view.requestFocus();
        this.imm.showSoftInput(view, 1);
    }

    public void showSoftKeyboard() {
        this.imm.showSoftInput(getCurrentFocus(), 1);
    }

    public void hideSoftKeyboard() {
        this.imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
    }

    protected void loadCurrentFragmentData() {
        BaseGameFragment baseFragment = (BaseGameFragment) this.gameCenterPagerAdapter.getItem(this.currentPosition);
        if (!this.positionList.contains(Integer.valueOf(this.currentPosition))) {
            this.positionList.add(Integer.valueOf(this.currentPosition));
            baseFragment.loadDataByNet();
        }
    }

    protected void addAsyncTask(AsyncTask<?, ?, ?> asyncTask) {
        if (this.taskList == null) {
            this.taskList = new ArrayList();
        }
        this.taskList.add(asyncTask);
    }

    protected void clearAsyncTask() {
        if (this.taskList != null) {
            int size = this.taskList.size();
            for (int i = 0; i < size; i++) {
                ((AsyncTask) this.taskList.get(i)).cancel(true);
            }
            this.taskList.clear();
        }
    }

    protected void showToast(String msg) {
        this.toast.setText(msg);
        this.toast.show();
    }
}
