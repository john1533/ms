package com.mobcent.discuz.module.msg.fragment.adapter.holder;

import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class SessionListFragmentAdapterHolder2 {
    private MCHeadIcon iconImg;
    private TextView msgSignText;
    private TextView nameText;
    private TextView summaryText;
    private TextView timeText;

    public MCHeadIcon getIconImg() {
        return this.iconImg;
    }

    public void setIconImg(MCHeadIcon iconImg) {
        this.iconImg = iconImg;
    }

    public TextView getMsgSignText() {
        return this.msgSignText;
    }

    public void setMsgSignText(TextView msgSignText) {
        this.msgSignText = msgSignText;
    }

    public TextView getNameText() {
        return this.nameText;
    }

    public void setNameText(TextView nameText) {
        this.nameText = nameText;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public TextView getSummaryText() {
        return this.summaryText;
    }

    public void setSummaryText(TextView summaryText) {
        this.summaryText = summaryText;
    }
}
