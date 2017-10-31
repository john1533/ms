package com.mobcent.discuz.module.topic.detail.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.widget.AdCollectionView;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;

public class BaseMiddleHolder {
    private AdCollectionView adView;
    private LinearLayout audioLayout;
    private TextView audioTimeText;
    private ImageView imgView;
    private LinearLayout layout;
    private ImageView playAudioImg;
    private ImageView playIngImg;
    private MCProgressBar progressBar;
    private TextView textView;
    private ImageView videoView;

    public LinearLayout getLayout() {
        return this.layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
    }

    public ImageView getVideoView() {
        return this.videoView;
    }

    public void setVideoView(ImageView videoView) {
        this.videoView = videoView;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getImgView() {
        return this.imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

    public LinearLayout getAudioLayout() {
        return this.audioLayout;
    }

    public void setAudioLayout(LinearLayout audioLayout) {
        this.audioLayout = audioLayout;
    }

    public ImageView getPlayAudioImg() {
        return this.playAudioImg;
    }

    public void setPlayAudioImg(ImageView playAudioImg) {
        this.playAudioImg = playAudioImg;
    }

    public TextView getAudioTimeText() {
        return this.audioTimeText;
    }

    public void setAudioTimeText(TextView audioTimeText) {
        this.audioTimeText = audioTimeText;
    }

    public ImageView getPlayIngImg() {
        return this.playIngImg;
    }

    public void setPlayIngImg(ImageView playIngImg) {
        this.playIngImg = playIngImg;
    }

    public MCProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(MCProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public AdCollectionView getAdView() {
        return this.adView;
    }

    public void setAdView(AdCollectionView adView) {
        this.adView = adView;
    }
}
