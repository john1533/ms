package com.mobcent.lowest.android.ui.widget.scaleview;

import java.io.Serializable;

public class RichImageModel implements Serializable {
    private static final long serialVersionUID = -156085826382659818L;
    private int downloadLen;
    private String imageDesc;
    private String imageUrl;
    private int maxLen;

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDesc() {
        return this.imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public int getMaxLen() {
        return this.maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

    public int getDownloadLen() {
        return this.downloadLen;
    }

    public void setDownloadLen(int downloadLen) {
        this.downloadLen = downloadLen;
    }
}
