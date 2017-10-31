package com.mobcent.lowest.android.ui.module.game.fragment.adapter.holder;

import android.widget.TextView;

public class GameCommentAdapterHolder {
    private TextView commentContent;
    private TextView critic;
    private TextView time;

    public TextView getCritic() {
        return this.critic;
    }

    public void setCritic(TextView critic) {
        this.critic = critic;
    }

    public TextView getCommentContent() {
        return this.commentContent;
    }

    public void setCommentContent(TextView commentContent) {
        this.commentContent = commentContent;
    }

    public TextView getTime() {
        return this.time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }
}
