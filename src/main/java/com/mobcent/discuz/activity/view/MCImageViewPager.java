package com.mobcent.discuz.activity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MCImageViewPager extends ViewPager {
    private boolean isScroll = true;

    public boolean isScroll() {
        return this.isScroll;
    }

    public void setScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    public MCImageViewPager(Context context) {
        super(context);
    }

    public MCImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z = true;
        try {
            if (isScroll()) {
                z = super.onTouchEvent(ev);
            }
        } catch (Exception e) {
        }
        return z;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean z = false;
        try {
            if (isScroll()) {
                z = super.onInterceptTouchEvent(event);
            }
        } catch (Exception e) {
        }
        return z;
    }
}
