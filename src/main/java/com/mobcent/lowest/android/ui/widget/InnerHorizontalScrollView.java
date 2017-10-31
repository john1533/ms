package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

public class InnerHorizontalScrollView extends HorizontalScrollView {
    private ScrollView parentScrollView;
    private int startY;

    public InnerHorizontalScrollView(Context context) {
        super(context);
    }

    public InnerHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollView getParentScrollView() {
        return this.parentScrollView;
    }

    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.parentScrollView == null) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case 0:
                this.startY = (int) ev.getY();
                this.parentScrollView.requestDisallowInterceptTouchEvent(true);
                break;
            case 2:
                if (Math.abs(((int) ev.getY()) - this.startY) > 0) {
                    this.parentScrollView.requestDisallowInterceptTouchEvent(false);
                    break;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
