package com.mobcent.discuz.android.model;

import com.mobcent.lowest.base.model.BaseFallWallModel;

public class PhotoModel extends BaseFallWallModel {
    private static final long serialVersionUID = -4897943030617838971L;
    private int albumId;
    private int arert;
    private long createDate;
    private int has_next;
    private int hitCount;
    private String info;
    private long lastReplyDate;
    private String originalUrl;
    private int picId;
    private int replieCount;
    private String thumbnailUrl;
    private String title;
    private long userId;
    private String userName;

    public int getHas_next() {
        return this.has_next;
    }

    public int getHitCount() {
        return this.hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int getReplieCount() {
        return this.replieCount;
    }

    public void setReplieCount(int replieCount) {
        this.replieCount = replieCount;
    }

    public void setHas_next(int has_next) {
        this.has_next = has_next;
    }

    public int getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getPicId() {
        return this.picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getLastReplyDate() {
        return this.lastReplyDate;
    }

    public void setLastReplyDate(long lastReplyDate) {
        this.lastReplyDate = lastReplyDate;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public int getArert() {
        return this.arert;
    }

    public void setArert(int arert) {
        this.arert = arert;
    }
}
