package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class BasePicView extends ImageView {
    protected Handler handler = new Handler();

    protected abstract void loadPic(String str, AdProgress adProgress);

    protected abstract void recyclePic();

    protected abstract void setShowWidthReal(int i);

    public BasePicView(Context context) {
        super(context);
    }

    public BasePicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
