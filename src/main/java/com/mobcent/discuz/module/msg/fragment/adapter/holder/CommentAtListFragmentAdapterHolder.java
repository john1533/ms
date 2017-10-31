package com.mobcent.discuz.module.msg.fragment.adapter.holder;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class CommentAtListFragmentAdapterHolder {
    private Button checkBtn;
    private MCHeadIcon iconImg;
    private TextView nameText;
    private Button replyBtn;
    private TextView replyCotentText;
    private LinearLayout replyLayout;
    private ImageView replyUrlImg;
    private TextView timeText;
    private TextView topicCotentText;
    private LinearLayout topicLayout;
    private ImageView topicUrlImg;

    public MCHeadIcon getIconImg() {
        return this.iconImg;
    }

    public void setIconImg(MCHeadIcon iconImg) {
        this.iconImg = iconImg;
    }

    public TextView getNameText() {
        return this.nameText;
    }

    public void setNameText(TextView nameText) {
        this.nameText = nameText;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public LinearLayout getReplyLayout() {
        return this.replyLayout;
    }

    public void setReplyLayout(LinearLayout replyLayout) {
        this.replyLayout = replyLayout;
    }

    public TextView getReplyCotentText() {
        return this.replyCotentText;
    }

    public void setReplyCotentText(TextView replyCotentText) {
        this.replyCotentText = replyCotentText;
    }

    public ImageView getReplyUrlImg() {
        return this.replyUrlImg;
    }

    public void setReplyUrlImg(ImageView replyUrlImg) {
        this.replyUrlImg = replyUrlImg;
    }

    public LinearLayout getTopicLayout() {
        return this.topicLayout;
    }

    public void setTopicLayout(LinearLayout topicLayout) {
        this.topicLayout = topicLayout;
    }

    public TextView getTopicCotentText() {
        return this.topicCotentText;
    }

    public void setTopicCotentText(TextView topicCotentText) {
        this.topicCotentText = topicCotentText;
    }

    public ImageView getTopicUrlImg() {
        return this.topicUrlImg;
    }

    public void setTopicUrlImg(ImageView topicUrlImg) {
        this.topicUrlImg = topicUrlImg;
    }

    public Button getCheckBtn() {
        return this.checkBtn;
    }

    public void setCheckBtn(Button checkBtn) {
        this.checkBtn = checkBtn;
    }

    public Button getReplyBtn() {
        return this.replyBtn;
    }

    public void setReplyBtn(Button replyBtn) {
        this.replyBtn = replyBtn;
    }
}
