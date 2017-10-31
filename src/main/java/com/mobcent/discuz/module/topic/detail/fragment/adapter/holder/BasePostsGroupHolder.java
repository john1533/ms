package com.mobcent.discuz.module.topic.detail.fragment.adapter.holder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class BasePostsGroupHolder {
    private LinearLayout activityApplyBox;
    private TextView activityDescText;
    private ImageView activityImage;
    private Button activitySubmitBtn;
    private TextView activitySummaryText;
    private LinearLayout activityTopicBox;
    private ImageView essenceImg;
    private ImageView pollImg;
    private TextView replyCountText;
    private TextView scanCountText;
    private TextView timeText;
    private TextView titleText;
    private ImageView topImg;
    private MCHeadIcon userImg;
    private TextView userNameText;
    private TextView userRoleText;
    private TextView userTileText;
    private TextView postsLabText;
    public TextView getPostsLabText() {
        return postsLabText;
    }

    public void setPostsLabText(TextView postsLabText) {
        this.postsLabText = postsLabText;
    }



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

    public ImageView getPollImg() {
        return this.pollImg;
    }

    public void setPollImg(ImageView pollImg) {
        this.pollImg = pollImg;
    }

    public TextView getTitleText() {
        return this.titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
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

    public MCHeadIcon getUserImg() {
        return this.userImg;
    }

    public void setUserImg(MCHeadIcon userImg) {
        this.userImg = userImg;
    }

    public TextView getUserNameText() {
        return this.userNameText;
    }

    public void setUserNameText(TextView userNameText) {
        this.userNameText = userNameText;
    }

    public TextView getUserRoleText() {
        return this.userRoleText;
    }

    public void setUserRoleText(TextView userRoleText) {
        this.userRoleText = userRoleText;
    }

    public TextView getUserTileText() {
        return this.userTileText;
    }

    public void setUserTileText(TextView userTileText) {
        this.userTileText = userTileText;
    }

    public LinearLayout getActivityTopicBox() {
        return this.activityTopicBox;
    }

    public void setActivityTopicBox(LinearLayout activityTopicBox) {
        this.activityTopicBox = activityTopicBox;
    }

    public ImageView getActivityImage() {
        return this.activityImage;
    }

    public void setActivityImage(ImageView activityImage) {
        this.activityImage = activityImage;
    }

    public TextView getActivityDescText() {
        return this.activityDescText;
    }

    public void setActivityDescText(TextView activityDescText) {
        this.activityDescText = activityDescText;
    }

    public TextView getActivitySummaryText() {
        return this.activitySummaryText;
    }

    public void setActivitySummaryText(TextView activitySummaryText) {
        this.activitySummaryText = activitySummaryText;
    }

    public Button getActivitySubmitBtn() {
        return this.activitySubmitBtn;
    }

    public void setActivitySubmitBtn(Button activitySubmitBtn) {
        this.activitySubmitBtn = activitySubmitBtn;
    }

    public LinearLayout getActivityApplyBox() {
        return this.activityApplyBox;
    }

    public void setActivityApplyBox(LinearLayout activityApplyBox) {
        this.activityApplyBox = activityApplyBox;
    }
}
