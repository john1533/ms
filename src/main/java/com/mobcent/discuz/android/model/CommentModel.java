package com.mobcent.discuz.android.model;

import java.util.List;

public class CommentModel extends BaseModel {
    private static final long serialVersionUID = -1185050813650789049L;
    private int commentPostsId;
    private int commentUserId;
    private List<ContentModel> contentList;
    private String icon;
    private List<ManagePanelModel> managePanelModelList;
    private String time;
    private String userNickName;

    public String getUserNickName() {
        return this.userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCommentUserId() {
        return this.commentUserId;
    }

    public void setCommentUserId(int commentUserId) {
        this.commentUserId = commentUserId;
    }

    public int getCommentPostsId() {
        return this.commentPostsId;
    }

    public void setCommentPostsId(int commentPostsId) {
        this.commentPostsId = commentPostsId;
    }

    public List<ContentModel> getContentList() {
        return this.contentList;
    }

    public void setContentList(List<ContentModel> contentList) {
        this.contentList = contentList;
    }

    public List<ManagePanelModel> getManagePanelModelList() {
        return this.managePanelModelList;
    }

    public void setManagePanelModelList(List<ManagePanelModel> managePanelModelList) {
        this.managePanelModelList = managePanelModelList;
    }
}
