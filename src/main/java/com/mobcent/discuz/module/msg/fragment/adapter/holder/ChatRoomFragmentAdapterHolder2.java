package com.mobcent.discuz.module.msg.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;

public class ChatRoomFragmentAdapterHolder2 {
    private LinearLayout audioBox;
    private TextView audioTimeText;
    private ImageView contentAudio;
    private RelativeLayout contentBox;
    private ImageView contentImg;
    private RelativeLayout contentSignBox;
    private TextView contentText;
    private MCHeadIcon iconImg;
    private RelativeLayout layout;
    private ImageView sendFailImg;
    private MCProgressBar sendIngProgressBar;
    private TextView timeText;

    public RelativeLayout getLayout() {
        return this.layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public MCHeadIcon getIconImg() {
        return this.iconImg;
    }

    public void setIconImg(MCHeadIcon iconImg) {
        this.iconImg = iconImg;
    }

    public RelativeLayout getContentSignBox() {
        return this.contentSignBox;
    }

    public void setContentSignBox(RelativeLayout contentSignBox) {
        this.contentSignBox = contentSignBox;
    }

    public RelativeLayout getContentBox() {
        return this.contentBox;
    }

    public void setContentBox(RelativeLayout contentBox) {
        this.contentBox = contentBox;
    }

    public TextView getContentText() {
        return this.contentText;
    }

    public void setContentText(TextView contentText) {
        this.contentText = contentText;
    }

    public ImageView getContentImg() {
        return this.contentImg;
    }

    public void setContentImg(ImageView contentImg) {
        this.contentImg = contentImg;
    }

    public LinearLayout getAudioBox() {
        return this.audioBox;
    }

    public void setAudioBox(LinearLayout audioBox) {
        this.audioBox = audioBox;
    }

    public ImageView getContentAudio() {
        return this.contentAudio;
    }

    public void setContentAudio(ImageView contentAudio) {
        this.contentAudio = contentAudio;
    }

    public TextView getAudioTimeText() {
        return this.audioTimeText;
    }

    public void setAudioTimeText(TextView audioTimeText) {
        this.audioTimeText = audioTimeText;
    }

    public MCProgressBar getSendIngProgressBar() {
        return this.sendIngProgressBar;
    }

    public void setSendIngProgressBar(MCProgressBar sendIngProgressBar) {
        this.sendIngProgressBar = sendIngProgressBar;
    }

    public ImageView getSendFailImg() {
        return this.sendFailImg;
    }

    public void setSendFailImg(ImageView sendFailImg) {
        this.sendFailImg = sendFailImg;
    }
}
