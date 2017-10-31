package com.mobcent.discuz.activity.utils;

import android.view.View.OnClickListener;

public class DZClickSpan {
    private OnClickListener clickListener;
    private String colorName;
    private int endPostion;
    private int startPostion;

    public int getStartPostion() {
        return this.startPostion;
    }

    public void setStartPostion(int startPostion) {
        this.startPostion = startPostion;
    }

    public int getEndPostion() {
        return this.endPostion;
    }

    public void setEndPostion(int endPostion) {
        this.endPostion = endPostion;
    }

    public String getColorName() {
        return this.colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public OnClickListener getClickListener() {
        return this.clickListener;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
