package com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;

public class SearchListFragmentHolder {
    private AdView adView;
    private TextView describeText;
    private ImageView thumbImg;
    private ImageView thumbImgRight;
    private TextView titleText;

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getDescribeText() {
        return this.describeText;
    }

    public void setDescribeText(TextView describeText) {
        this.describeText = describeText;
    }

    public ImageView getThumbImg() {
        return this.thumbImg;
    }

    public void setThumbImg(ImageView thumbImg) {
        this.thumbImg = thumbImg;
    }

    public ImageView getThumbImgRight() {
        return this.thumbImgRight;
    }

    public void setThumbImgRight(ImageView thumbImgRight) {
        this.thumbImgRight = thumbImgRight;
    }

    public AdView getAdView() {
        return this.adView;
    }

    public void setAdView(AdView adView) {
        this.adView = adView;
    }
}
