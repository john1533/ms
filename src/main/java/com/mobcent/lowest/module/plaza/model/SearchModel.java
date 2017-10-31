package com.mobcent.lowest.module.plaza.model;

import com.mobcent.lowest.base.model.BaseModel;

public class SearchModel extends BaseModel {
    private static final long serialVersionUID = 4353917048542507681L;
    private int baikeType;
    private long boardId;
    private String clickUrl;
    private long forumId;
    private boolean isEssence;
    private boolean isFavor;
    private boolean isHot;
    private boolean isTop;
    private long numIid;
    private String picPath;
    private float ratio;
    private String singer;
    private int sourceType;
    private int status;
    private String summary;
    private String title;
    private long topicId;
    private long userId;
    private String userNickName;

    public int getSourceType() {
        return this.sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getPicPath() {
        return this.picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSinger() {
        return this.singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getBaikeType() {
        return this.baikeType;
    }

    public void setBaikeType(int baikeType) {
        this.baikeType = baikeType;
    }

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRatio() {
        return this.ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public long getForumId() {
        return this.forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public boolean isTop() {
        return this.isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isHot() {
        return this.isHot;
    }

    public void setHot(boolean isHot) {
        this.isHot = isHot;
    }

    public String getUserNickName() {
        return this.userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public boolean isEssence() {
        return this.isEssence;
    }

    public void setEssence(boolean isEssence) {
        this.isEssence = isEssence;
    }

    public boolean isFavor() {
        return this.isFavor;
    }

    public void setFavor(boolean isFavor) {
        this.isFavor = isFavor;
    }

    public long getNumIid() {
        return this.numIid;
    }

    public void setNumIid(long numIid) {
        this.numIid = numIid;
    }

    public String getClickUrl() {
        return this.clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }
}
