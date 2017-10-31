package com.mobcent.android.model;

import java.io.Serializable;

public class MCShareSiteModel implements Serializable {
    public static final int BINDED = 1;
    public static final int UNBIND = 3;
    public static final int UNSELECT = 2;
    private static final long serialVersionUID = -6954032620289110870L;
    private int bindState = 3;
    private String bindUrl;
    private String rsReason;
    private String shareContent;
    private String shareUrl;
    private int siteId;
    private String siteImage;
    private String siteName;
    private String siteNickName;
    private long userId;

    public int getSiteId() {
        return this.siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteNickName() {
        return this.siteNickName;
    }

    public void setSiteNickName(String siteNickName) {
        this.siteNickName = siteNickName;
    }

    public String getSiteImage() {
        return this.siteImage;
    }

    public void setSiteImage(String siteImage) {
        this.siteImage = siteImage;
    }

    public String getBindUrl() {
        return this.bindUrl;
    }

    public void setBindUrl(String bindUrl) {
        this.bindUrl = bindUrl;
    }

    public int getBindState() {
        return this.bindState;
    }

    public void setBindState(int bindState) {
        this.bindState = bindState;
    }

    public String getShareContent() {
        return this.shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareUrl() {
        return this.shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRsReason() {
        return this.rsReason;
    }

    public void setRsReason(String rsReason) {
        this.rsReason = rsReason;
    }
}
