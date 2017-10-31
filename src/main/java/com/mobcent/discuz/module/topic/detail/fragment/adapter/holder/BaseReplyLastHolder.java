package com.mobcent.discuz.module.topic.detail.fragment.adapter.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseReplyLastHolder {
    private LinearLayout locationBox;
    private TextView locationText;
    private LinearLayout moreBox;
    private ImageButton moreBtn;
    private LinearLayout praiseBox;
    private ImageButton praiseBtn;
    private TextView praiseText;
    private TextView quoteText;
    private LinearLayout replyBox;
    private ImageButton replyBtn;
    private TextView replyTimeText;
    private TextView signText;
    private View replyLine;

    public TextView getQuoteText() {
        return this.quoteText;
    }

    public void setQuoteText(TextView quoteText) {
        this.quoteText = quoteText;
    }

    public TextView getSignText() {
        return this.signText;
    }

    public void setSignText(TextView signText) {
        this.signText = signText;
    }

    public LinearLayout getLocationBox() {
        return this.locationBox;
    }

    public void setLocationBox(LinearLayout locationBox) {
        this.locationBox = locationBox;
    }

    public TextView getLocationText() {
        return this.locationText;
    }

    public void setLocationText(TextView locationText) {
        this.locationText = locationText;
    }

    public TextView getReplyTimeText() {
        return this.replyTimeText;
    }

    public void setReplyTimeText(TextView replyTimeText) {
        this.replyTimeText = replyTimeText;
    }

    public LinearLayout getPraiseBox() {
        return this.praiseBox;
    }

    public void setPraiseBox(LinearLayout praiseBox) {
        this.praiseBox = praiseBox;
    }

    public ImageButton getPraiseBtn() {
        return this.praiseBtn;
    }

    public void setPraiseBtn(ImageButton praiseBtn) {
        this.praiseBtn = praiseBtn;
    }

    public TextView getPraiseText() {
        return this.praiseText;
    }

    public void setPraiseText(TextView praiseText) {
        this.praiseText = praiseText;
    }

    public LinearLayout getReplyBox() {
        return this.replyBox;
    }

    public void setReplyBox(LinearLayout replyBox) {
        this.replyBox = replyBox;
    }

    public ImageButton getReplyBtn() {
        return this.replyBtn;
    }

    public void setReplyBtn(ImageButton replyBtn) {
        this.replyBtn = replyBtn;
    }

    public LinearLayout getMoreBox() {
        return this.moreBox;
    }

    public void setMoreBox(LinearLayout moreBox) {
        this.moreBox = moreBox;
    }

    public ImageButton getMoreBtn() {
        return this.moreBtn;
    }

    public void setMoreBtn(ImageButton moreBtn) {
        this.moreBtn = moreBtn;
    }

    public View getReplyLine() {
        return replyLine;
    }

    public void setReplyLine(View replyLine) {
        this.replyLine = replyLine;
    }
}
