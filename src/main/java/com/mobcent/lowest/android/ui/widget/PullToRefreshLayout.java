package com.mobcent.lowest.android.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.widget.AdProgress;
import com.mobcent.lowest.base.utils.MCResource;

public class PullToRefreshLayout extends LinearLayout {
    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private ImageView headerImage;
    private TextView headerLabel;
    private AdProgress headerProgress;
    private MCResource mcResource;
    private TextView noteLabel;
    private String pullLabel;
    private String refreshingLabel;
    private String releaseLabel;
    private Animation resetRotateAnimation;
    private Animation rotateAnimation = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);

    public PullToRefreshLayout(Context context, int mode, String releaseLabel, String pullLabel, String refreshingLabel) {
        super(context);
        this.mcResource = MCResource.getInstance(context);
        LinearLayout header = (LinearLayout) LayoutInflater.from(context).inflate(this.mcResource.getLayoutId("mc_plaza_pull_to_refresh_header"), this);
        this.headerLabel = (TextView) header.findViewById(this.mcResource.getViewId("mc_forum_pull_to_refresh_text"));
        this.headerImage = (ImageView) header.findViewById(this.mcResource.getViewId("mc_forum_pull_to_refresh_image"));
        this.headerProgress = (AdProgress) header.findViewById(this.mcResource.getViewId("mc_forum_pull_to_refresh_progress"));
        this.headerProgress.stop();
        this.noteLabel = (TextView) header.findViewById(this.mcResource.getViewId("mc_forum_pull_to_refresh_note"));
        onTheme();
        Interpolator interpolator = new LinearInterpolator();
        this.rotateAnimation.setInterpolator(interpolator);
        this.rotateAnimation.setDuration(200);
        this.rotateAnimation.setFillAfter(true);
        this.resetRotateAnimation = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.resetRotateAnimation.setInterpolator(interpolator);
        this.resetRotateAnimation.setDuration(200);
        this.resetRotateAnimation.setFillAfter(true);
        this.releaseLabel = releaseLabel;
        this.pullLabel = pullLabel;
        this.refreshingLabel = refreshingLabel;
        switch (mode) {
            case 2:
                this.headerImage.setImageDrawable(this.mcResource.getDrawable("mc_forum_refresh"));
                return;
            default:
                this.headerImage.setImageDrawable(this.mcResource.getDrawable("mc_forum_refresh"));
                return;
        }
    }

    public void onTheme() {
        this.headerLabel.setTextColor(this.mcResource.getColor("mc_forum_text4_normal_color"));
        this.noteLabel.setTextColor(this.mcResource.getColor("mc_forum_text4_normal_color"));
        this.headerProgress.setImageDrawable(this.mcResource.getDrawable("mc_forum_loading1"));
    }

    public void reset(String note) {
        this.headerLabel.setText(this.pullLabel);
        if (note != null) {
            this.noteLabel.setVisibility(0);
            this.noteLabel.setText(note);
        }
        this.headerImage.setVisibility(0);
        this.headerProgress.hide();
    }

    public void pullToRefresh(boolean isBack) {
        if (isBack) {
            this.headerLabel.setText(this.pullLabel);
            this.headerImage.clearAnimation();
            this.headerImage.startAnimation(this.resetRotateAnimation);
            return;
        }
        this.headerLabel.setText(this.pullLabel);
    }

    public void releaseToRefresh() {
        this.headerLabel.setText(this.releaseLabel);
        this.headerImage.clearAnimation();
        this.headerImage.startAnimation(this.rotateAnimation);
    }

    public void refreshing() {
        this.headerLabel.setText(this.refreshingLabel);
        this.headerImage.clearAnimation();
        this.headerImage.setVisibility(4);
        this.headerProgress.setVisibility(0);
        this.headerProgress.show();
    }

    public void resetBottom() {
        this.headerLabel.setText(this.pullLabel);
        this.headerImage.clearAnimation();
        this.headerImage.setVisibility(4);
        this.headerProgress.setVisibility(8);
        this.headerProgress.stop();
    }

    public void hideBottom() {
        this.headerLabel.setVisibility(8);
        this.headerImage.setVisibility(8);
        this.headerProgress.setVisibility(8);
        this.headerProgress.stop();
    }

    public void showBottom() {
        this.headerLabel.setVisibility(0);
        resetBottom();
    }

    public void bottomRefreshing() {
        this.headerLabel.setText(this.refreshingLabel);
        this.headerImage.clearAnimation();
        this.headerImage.setVisibility(4);
        this.headerProgress.setVisibility(0);
        this.headerProgress.show();
    }

    public void changeBottomRefreshLabel(String label) {
        this.headerLabel.setText(label);
        this.headerImage.clearAnimation();
        this.headerImage.setVisibility(4);
        this.headerProgress.setVisibility(8);
        this.headerProgress.stop();
    }

    public void setTextColor(int color) {
        this.headerLabel.setTextColor(color);
    }

    public void changeHeaderLabel(int type, String label) {
        switch (type) {
            case 1:
                this.releaseLabel = label;
                return;
            case 2:
                this.pullLabel = label;
                return;
            case 3:
                this.refreshingLabel = label;
                return;
            default:
                return;
        }
    }
}
