package com.mobcent.discuz.module.publish.adapter.holder;

import android.widget.LinearLayout;
import android.widget.TextView;

public class PublishBoardListAdapterHolder {
    private LinearLayout box1;
    private LinearLayout box2;
    private TextView titleText;

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public LinearLayout getBox1() {
        return this.box1;
    }

    public void setBox1(LinearLayout box1) {
        this.box1 = box1;
    }

    public LinearLayout getBox2() {
        return this.box2;
    }

    public void setBox2(LinearLayout box2) {
        this.box2 = box2;
    }
}
