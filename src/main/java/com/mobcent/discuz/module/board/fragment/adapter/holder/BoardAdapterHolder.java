package com.mobcent.discuz.module.board.fragment.adapter.holder;

import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardAdapterHolder {
    private LinearLayout containerBox;
    private TextView titleText;

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public LinearLayout getContainerBox() {
        return this.containerBox;
    }

    public void setContainerBox(LinearLayout containerBox) {
        this.containerBox = containerBox;
    }
}
