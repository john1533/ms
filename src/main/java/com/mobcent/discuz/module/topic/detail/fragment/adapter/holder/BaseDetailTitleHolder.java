package com.mobcent.discuz.module.topic.detail.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class BaseDetailTitleHolder {
    private ImageView essenceImg;
    private ImageView pollImg;
    private TextView replyCountText;
    private TextView scanCountText;
    private TextView timeText;
    private TextView titleText;
    private ImageView topImg;

    public ImageView getTopImg() {
        return this.topImg;
    }

    public void setTopImg(ImageView topImg) {
        this.topImg = topImg;
    }

    public ImageView getEssenceImg() {
        return this.essenceImg;
    }

    public void setEssenceImg(ImageView essenceImg) {
        this.essenceImg = essenceImg;
    }

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public ImageView getPollImg() {
        return this.pollImg;
    }

    public void setPollImg(ImageView pollImg) {
        this.pollImg = pollImg;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public TextView getReplyCountText() {
        return this.replyCountText;
    }

    public void setReplyCountText(TextView replyCountText) {
        this.replyCountText = replyCountText;
    }

    public TextView getScanCountText() {
        return this.scanCountText;
    }

    public void setScanCountText(TextView scanCountText) {
        this.scanCountText = scanCountText;
    }
}
