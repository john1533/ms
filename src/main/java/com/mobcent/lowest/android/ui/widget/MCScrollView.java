package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MCScrollView extends ScrollView {
    private OnScrollListener onScrollListener;

    public interface OnScrollListener {
        void onScrollChanged(int i, int i2, int i3, int i4);
    }

    public MCScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.onScrollListener != null) {
            this.onScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public OnScrollListener getOnScrollListener() {
        return this.onScrollListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }
}
