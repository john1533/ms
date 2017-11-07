package com.mobcent.lowest.android.ui.module.place.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.place.constant.PlaceIntentConstant;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class BasePlaceFragmentActivity extends FragmentActivity implements RouteConstant, PlaceIntentConstant {
    protected OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            BasePlaceFragmentActivity.this.onViewClick(v);
        }
    };
    protected Context context;
    protected MCResource resource;
    private FragmentTransaction transaction = null;

    protected abstract void initActions();

    protected abstract void initView();

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initData();
        initView();
        initActions();
    }

    protected void initData() {
        this.context = getApplicationContext();
        this.resource = MCResource.getInstance(this);
    }

    protected void onViewClick(View v) {
    }

    protected void addFragment(int containerId, Fragment fragemnt) {
        getFragmentTransaction().add(containerId, fragemnt);
        commitFragment();
    }

    protected void showFragment(Fragment fragemnt) {
        getFragmentTransaction().setCustomAnimations(17432576, 17432577);
        getFragmentTransaction().show(fragemnt);
        commitFragment();
    }

    protected void replaceFragment(int containerId, Fragment fragemnt) {
        getFragmentTransaction().replace(containerId, fragemnt);
        commitFragment();
    }

    protected void hideFragment(Fragment fragment) {
        getFragmentTransaction().hide(fragment);
        commitFragment();
    }

    protected void deleteFragment(Fragment fragment) {
        getFragmentTransaction().remove(fragment);
        commitFragment();
    }

    protected FragmentTransaction getFragmentTransaction() {
        if (this.transaction == null) {
            this.transaction = getSupportFragmentManager().beginTransaction();
        }
        return this.transaction;
    }

    protected void commitFragment() {
        getFragmentTransaction().addToBackStack(null);
        getFragmentTransaction().commit();
        this.transaction = null;
    }

    protected View findViewById(String name) {
        View view = findViewById(this.resource.getViewId(name));
        if (view != null) {
            return view;
        }
        view = new View(this);
        Toast.makeText(this, name + " can't find", 1000).show();
        return view;
    }

    protected void warn(String name) {
        Toast.makeText(this, this.resource.getStringId(name), 1000).show();
    }

    protected void warnStr(String msg) {
        Toast.makeText(this, msg, 1000).show();
    }
}
