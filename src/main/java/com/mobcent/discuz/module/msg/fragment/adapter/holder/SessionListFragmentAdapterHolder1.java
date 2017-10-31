package com.mobcent.discuz.module.msg.fragment.adapter.holder;

import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class SessionListFragmentAdapterHolder1 {
    private MCHeadIcon iconImg;
    private TextView msgSignText;
    private TextView titleText;

    public MCHeadIcon getIconImg() {
        return this.iconImg;
    }

    public void setIconImg(MCHeadIcon iconImg) {
        this.iconImg = iconImg;
    }

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getMsgSignText() {
        return this.msgSignText;
    }

    public void setMsgSignText(TextView msgSignText) {
        this.msgSignText = msgSignText;
    }
}
