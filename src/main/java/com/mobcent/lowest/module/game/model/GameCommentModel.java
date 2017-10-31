package com.mobcent.lowest.module.game.model;

import com.mobcent.lowest.base.model.BaseModel;

public class GameCommentModel extends BaseModel {
    private static final long serialVersionUID = 8386810150867360433L;
    private String commentContent;
    private String critic;
    private int position;
    private String time;

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCritic() {
        return this.critic;
    }

    public void setCritic(String critic) {
        this.critic = critic;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
