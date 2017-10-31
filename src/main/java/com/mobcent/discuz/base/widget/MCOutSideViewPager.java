package com.mobcent.discuz.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MCOutSideViewPager extends ViewPager {
    private boolean mScrollEnable = true;

    public boolean ismScrollEnable() {
        return this.mScrollEnable;
    }

    public void setmScrollEnable(boolean mScrollEnable) {
        this.mScrollEnable = mScrollEnable;
    }

    public MCOutSideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MCOutSideViewPager(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z = true;
        try {
            if (ismScrollEnable()) {
                z = super.onTouchEvent(ev);
            }
        } catch (Exception e) {
        }
        return z;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean z = false;
        try {
            if (ismScrollEnable()) {
                z = super.onInterceptTouchEvent(event);
            }
        } catch (Exception e) {
        }
        return z;
    }
}
