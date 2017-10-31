package com.mobcent.discuz.base.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;

public class MCHeaderViewPager extends ViewPager {
    private ViewPager oSideViewPager;
    private PullToRefreshListView pullToRefreshListView;

    public ViewPager getoSideViewPager() {
        return this.oSideViewPager;
    }

    public void setoSideViewPager(MCOutSideViewPager oSideViewPager) {
        this.oSideViewPager = oSideViewPager;
    }

    public void setPullToRefreshListView(PullToRefreshListView pullToRefreshListView) {
        this.pullToRefreshListView = pullToRefreshListView;
    }

    public PullToRefreshListView getPullToRefreshListView() {
        return this.pullToRefreshListView;
    }

    public MCHeaderViewPager(Context context) {
        super(context);
    }

    public MCHeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return super.onInterceptTouchEvent(arg0);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
