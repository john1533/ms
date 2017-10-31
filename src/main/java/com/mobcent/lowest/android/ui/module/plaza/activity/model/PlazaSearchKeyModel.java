package com.mobcent.lowest.android.ui.module.plaza.activity.model;

public class PlazaSearchKeyModel {
    private int baikeType;
    private int forumId;
    private String forumKey;
    private String keyWord;
    private int searchMode;
    private long userId;

    public int getForumId() {
        return this.forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getForumKey() {
        return this.forumKey;
    }

    public void setForumKey(String forumKey) {
        this.forumKey = forumKey;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getBaikeType() {
        return this.baikeType;
    }

    public void setBaikeType(int baikeType) {
        this.baikeType = baikeType;
    }

    public int getSearchMode() {
        return this.searchMode;
    }

    public void setSearchMode(int searchMode) {
        this.searchMode = searchMode;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String toString() {
        return "SearchKeyModel [forumId=" + this.forumId + ", forumKey=" + this.forumKey + ", userId=" + this.userId + ", baikeType=" + this.baikeType + ", searchMode=" + this.searchMode + ", keyWord=" + this.keyWord + "]";
    }
}
