package com.mobcent.discuz.module.article.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;

public class PostsAudioAdapterHolder {
    private LinearLayout audioLayout;
    private MCProgressBar downProgressBar;
    private ImageView playStautsImg;
    private ImageView playingImg;
    private TextView timeText;

    public LinearLayout getAudioLayout() {
        return this.audioLayout;
    }

    public void setAudioLayout(LinearLayout audioLayout) {
        this.audioLayout = audioLayout;
    }

    public ImageView getPlayStautsImg() {
        return this.playStautsImg;
    }

    public void setPlayStautsImg(ImageView playStautsImg) {
        this.playStautsImg = playStautsImg;
    }

    public ImageView getPlayingImg() {
        return this.playingImg;
    }

    public void setPlayingImg(ImageView playingImg) {
        this.playingImg = playingImg;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public MCProgressBar getDownProgressBar() {
        return this.downProgressBar;
    }

    public void setDownProgressBar(MCProgressBar downProgressBar) {
        this.downProgressBar = downProgressBar;
    }
}
