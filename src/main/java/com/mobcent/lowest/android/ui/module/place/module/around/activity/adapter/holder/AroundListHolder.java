package com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AroundListHolder {
    private TextView addressText;
    private RelativeLayout arriveBox;
    private Button arriveBtn;
    private ImageButton arriveImgBtn;
    private ImageView arrowImg;
    private TextView distanceText;
    private ImageView groupImg;
    private LinearLayout itemBox;
    private RelativeLayout phoneBox;
    private Button phoneBtn;
    private ImageButton phoneImgBtn;
    private TextView priceLeft;
    private TextView priceRight;
    private RatingBar ratingBar;
    private View slidLine;
    private TextView tagText;
    private TextView titleText;

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getDistanceText() {
        return this.distanceText;
    }

    public void setDistanceText(TextView distanceText) {
        this.distanceText = distanceText;
    }

    public TextView getAddressText() {
        return this.addressText;
    }

    public void setAddressText(TextView addressText) {
        this.addressText = addressText;
    }

    public TextView getPriceLeft() {
        return this.priceLeft;
    }

    public void setPriceLeft(TextView priceLeft) {
        this.priceLeft = priceLeft;
    }

    public TextView getPriceRight() {
        return this.priceRight;
    }

    public void setPriceRight(TextView priceRight) {
        this.priceRight = priceRight;
    }

    public TextView getTagText() {
        return this.tagText;
    }

    public void setTagText(TextView tagText) {
        this.tagText = tagText;
    }

    public Button getArriveBtn() {
        return this.arriveBtn;
    }

    public void setArriveBtn(Button arriveBtn) {
        this.arriveBtn = arriveBtn;
    }

    public Button getPhoneBtn() {
        return this.phoneBtn;
    }

    public void setPhoneBtn(Button phoneBtn) {
        this.phoneBtn = phoneBtn;
    }

    public RatingBar getRatingBar() {
        return this.ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public ImageView getGroupImg() {
        return this.groupImg;
    }

    public void setGroupImg(ImageView groupImg) {
        this.groupImg = groupImg;
    }

    public View getSlidLine() {
        return this.slidLine;
    }

    public void setSlidLine(View slidLine) {
        this.slidLine = slidLine;
    }

    public RelativeLayout getPhoneBox() {
        return this.phoneBox;
    }

    public void setPhoneBox(RelativeLayout phoneBox) {
        this.phoneBox = phoneBox;
    }

    public RelativeLayout getArriveBox() {
        return this.arriveBox;
    }

    public void setArriveBox(RelativeLayout arriveBox) {
        this.arriveBox = arriveBox;
    }

    public ImageButton getArriveImgBtn() {
        return this.arriveImgBtn;
    }

    public void setArriveImgBtn(ImageButton arriveImgBtn) {
        this.arriveImgBtn = arriveImgBtn;
    }

    public ImageButton getPhoneImgBtn() {
        return this.phoneImgBtn;
    }

    public void setPhoneImgBtn(ImageButton phoneImgBtn) {
        this.phoneImgBtn = phoneImgBtn;
    }

    public ImageView getArrowImg() {
        return this.arrowImg;
    }

    public void setArrowImg(ImageView arrowImg) {
        this.arrowImg = arrowImg;
    }

    public LinearLayout getItemBox() {
        return this.itemBox;
    }

    public void setItemBox(LinearLayout itemBox) {
        this.itemBox = itemBox;
    }
}
