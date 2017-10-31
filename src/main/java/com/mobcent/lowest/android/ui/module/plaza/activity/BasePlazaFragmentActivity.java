package com.mobcent.lowest.android.ui.module.plaza.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.utils.MCTouchSlidHelper;
import com.mobcent.lowest.android.ui.utils.MCTouchSlidHelper.TouchSlideDelegate;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class BasePlazaFragmentActivity extends FragmentActivity implements TouchSlideDelegate {
    protected MCResource adResource;
    protected InputMethodManager imm;
    protected Handler mHandler = new Handler();
    private MCTouchSlidHelper slideHelper;

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initWidgetActions();

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.adResource = MCResource.getInstance(getApplicationContext());
        this.imm = (InputMethodManager) getSystemService("input_method");
        this.slideHelper = new MCTouchSlidHelper(getApplicationContext());
        this.slideHelper.setDelegate(this);
        initData();
        initViews();
        initWidgetActions();
    }

    protected void warById(String name) {
        Toast.makeText(getApplicationContext(), this.adResource.getStringId(name), 0).show();
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

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean z = true;
        try {
            if (!this.slideHelper.dispatchTouchEvent(ev)) {
                z = super.dispatchTouchEvent(ev);
            }
        } catch (Exception e) {
        }
        return z;
    }

    public boolean isSlideAble() {
        return true;
    }

    public boolean isSlideFullScreen() {
        return false;
    }

    public void slideExit() {
        finish();
        try {
            overridePendingTransition(0, 17432579);
        } catch (Exception e) {
        }
    }
}
