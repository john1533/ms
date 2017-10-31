package com.mobcent.android.model;

import java.io.Serializable;
import java.util.HashMap;

public class MCShareModel implements Serializable {
    private static final long serialVersionUID = -6538180573558128256L;
    private String appKey;
    private String content;
    private String downloadUrl;
    private String imageFilePath;
    private String linkUrl;
    private HashMap<String, String> params;
    private String picUrl;
    private String skipUrl;
    private String title;
    private int type;

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkUrl() {
        return this.linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSkipUrl() {
        return this.skipUrl;
    }

    public void setSkipUrl(String skipUrl) {
        this.skipUrl = skipUrl;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getImageFilePath() {
        return this.imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HashMap<String, String> getParams() {
        return this.params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String toString() {
        return "appKey=" + getAppKey() + " title=" + getTitle() + " content=" + getContent() + " linkUrl=" + getLinkUrl() + " downloadUrl=" + getDownloadUrl() + " skipUrl=" + getSkipUrl() + " picUrl=" + getPicUrl() + " imageFilePath=" + getImageFilePath() + " type=" + getType();
    }
}
