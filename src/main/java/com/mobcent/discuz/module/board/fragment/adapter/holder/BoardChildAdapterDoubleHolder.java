package com.mobcent.discuz.module.board.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BoardChildAdapterDoubleHolder {
    private TextView leftBoardName;
    private TextView leftBoardTime;
    private LinearLayout leftBox;
    private ImageView leftImg;
    private TextView leftTotalNum;
    private TextView rightBoardName;
    private TextView rightBoardTime;
    private LinearLayout rightBox;
    private ImageView rightImg;
    private TextView rightTotalNum;

    public LinearLayout getLeftBox() {
        return this.leftBox;
    }

    public void setLeftBox(LinearLayout leftBox) {
        this.leftBox = leftBox;
    }

    public LinearLayout getRightBox() {
        return this.rightBox;
    }

    public void setRightBox(LinearLayout rightBox) {
        this.rightBox = rightBox;
    }

    public ImageView getLeftImg() {
        return this.leftImg;
    }

    public void setLeftImg(ImageView leftImg) {
        this.leftImg = leftImg;
    }

    public ImageView getRightImg() {
        return this.rightImg;
    }

    public void setRightImg(ImageView rightImg) {
        this.rightImg = rightImg;
    }

    public TextView getLeftBoardName() {
        return this.leftBoardName;
    }

    public void setLeftBoardName(TextView leftBoardName) {
        this.leftBoardName = leftBoardName;
    }

    public TextView getLeftBoardTime() {
        return this.leftBoardTime;
    }

    public void setLeftBoardTime(TextView leftBoardTime) {
        this.leftBoardTime = leftBoardTime;
    }

    public TextView getLeftTotalNum() {
        return this.leftTotalNum;
    }

    public void setLeftTotalNum(TextView leftTotalNum) {
        this.leftTotalNum = leftTotalNum;
    }

    public TextView getRightBoardName() {
        return this.rightBoardName;
    }

    public void setRightBoardName(TextView rightBoardName) {
        this.rightBoardName = rightBoardName;
    }

    public TextView getRightBoardTime() {
        return this.rightBoardTime;
    }

    public void setRightBoardTime(TextView rightBoardTime) {
        this.rightBoardTime = rightBoardTime;
    }

    public TextView getRightTotalNum() {
        return this.rightTotalNum;
    }

    public void setRightTotalNum(TextView rightTotalNum) {
        this.rightTotalNum = rightTotalNum;
    }
}
