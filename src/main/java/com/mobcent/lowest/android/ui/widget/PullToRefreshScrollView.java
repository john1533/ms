package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnRefreshListener;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {
    private final OnRefreshListener defaultOnRefreshListener = new OnRefreshListener() {
        public void onRefresh() {
            PullToRefreshScrollView.this.onRefreshComplete();
        }
    };

    public PullToRefreshScrollView(Context context) {
        super(context);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    public PullToRefreshScrollView(Context context, int mode) {
        super(context, mode);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(this.defaultOnRefreshListener);
    }

    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        return new ScrollView(context, attrs);
    }

    protected boolean isReadyForPullDown() {
        return ((ScrollView) this.refreshableView).getScrollY() == 0;
    }

    protected boolean onScroll(MotionEvent event) {
        return false;
    }

    protected void gotoTop() {
    }

    public View getFooterlayout() {
        return this.footerLayout;
    }
}
