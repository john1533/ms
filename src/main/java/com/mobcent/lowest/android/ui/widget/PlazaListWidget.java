package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class PlazaListWidget extends ListView {
    public String TAG = "PlazaListWidget";
    private int mTouchSlop;
    private float startY = 0.0f;

    public PlazaListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mTouchSlop = ViewConfiguration.getTouchSlop();
    }
}
