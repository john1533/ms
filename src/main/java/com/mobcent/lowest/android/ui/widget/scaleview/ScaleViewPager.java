package com.mobcent.lowest.android.ui.widget.scaleview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

public class ScaleViewPager extends ViewPager {
    public String TAG;
    private boolean isScroll;
    private boolean isScrollLeftEnable;
    private boolean isScrollRightEnable;
    public float touchSlop;

    public ScaleViewPager(Context context) {
        this(context, null);
    }

    public ScaleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "MyViewPager";
        this.isScrollLeftEnable = true;
        this.isScrollRightEnable = true;
        init(context);
    }

    private void init(Context context) {
        this.touchSlop = (float) ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
