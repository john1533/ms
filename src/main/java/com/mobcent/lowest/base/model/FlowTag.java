package com.mobcent.lowest.base.model;

import java.io.Serializable;

public class FlowTag implements Serializable {
    private static final long serialVersionUID = 7732693584051165961L;
    private String baseUrl;
    private long boardId;
    private String errorCode;
    private boolean hasNext;
    private String iconUrl;
    private long lastUpdateTime;
    private int marginTop;
    private String middleUrl;
    private String originalUrl;
    private int page;
    private float ratio;
    private int rs = 1;
    private String thumbnailUrl;
    private String title;
    private long topicId;
    private int totalNum;

    public long getBoardId() {
        return this.boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMiddleUrl() {
        return this.middleUrl;
    }

    public void setMiddleUrl(String middleUrl) {
        this.middleUrl = middleUrl;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
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

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getRs() {
        return this.rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isHasNext() {
        return this.hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
