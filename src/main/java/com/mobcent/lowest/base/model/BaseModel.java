package com.mobcent.lowest.base.model;

import java.io.Serializable;

public class BaseModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String baseUrl;
    private String errorCode;
    private boolean hasNext;
    private String iconUrl;
    private long lastUpdateTime;
    private int page;
    private String pathOrContent;
    private int rs = 1;
    private int totalNum;

    public String getPathOrContent() {
        return this.pathOrContent;
    }

    public void setPathOrContent(String pathOrContent) {
        this.pathOrContent = pathOrContent;
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

    public int getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getRs() {
        return this.rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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
