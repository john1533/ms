package com.mobcent.discuz.android.model;

import java.io.Serializable;
/**
 * 与sdk.mobcent.com 交互，获取消费信息
 *
 */

public class PayStateModel implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean adv;
    private boolean clientManager;
    private String imgUrl;
    private boolean isUserDefined;
    private boolean loadingPage;
    private String loadingPageImage;
    private boolean msgPush;
    private boolean powerBy;
    private boolean shareKey;
    private boolean square;
    private boolean waterMark;
    private String waterMarkImage;
    private String weiXinAppKey;
    private String weixinAppSecret;

    public String getLoadingPageImage() {
        return this.loadingPageImage;
    }

    public void setLoadingPageImage(String loadingPageImage) {
        this.loadingPageImage = loadingPageImage;
    }

    public boolean getAdv() {
        return this.adv;
    }

    public void setAdv(boolean adv) {
        this.adv = adv;
    }

    public boolean getClientManager() {
        return this.clientManager;
    }

    public void setClientManager(boolean clientManager) {
        this.clientManager = clientManager;
    }

    public boolean getLoadingPage() {
        return this.loadingPage;
    }

    public void setLoadingPage(boolean loadingPage) {
        this.loadingPage = loadingPage;
    }

    public boolean getMsgPush() {
        return this.msgPush;
    }

    public void setMsgPush(boolean msgPush) {
        this.msgPush = msgPush;
    }

    public boolean getPowerBy() {
        return this.powerBy;
    }

    public void setPowerBy(boolean powerBy) {
        this.powerBy = powerBy;
    }

    public boolean getShareKey() {
        return this.shareKey;
    }

    public void setShareKey(boolean shareKey) {
        this.shareKey = shareKey;
    }

    public boolean getSquare() {
        return this.square;
    }

    public void setSquare(boolean square) {
        this.square = square;
    }

    public boolean getWaterMark() {
        return this.waterMark;
    }

    public void setWaterMark(boolean waterMark) {
        this.waterMark = waterMark;
    }

    public String getWaterMarkImage() {
        return this.waterMarkImage;
    }

    public void setWaterMarkImage(String waterMarkImage) {
        this.waterMarkImage = waterMarkImage;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getWeiXinAppKey() {
        return this.weiXinAppKey;
    }

    public void setWeiXinAppKey(String weiXinAppKey) {
        this.weiXinAppKey = weiXinAppKey;
    }

    public String getWeixinAppSecret() {
        return this.weixinAppSecret;
    }

    public void setWeixinAppSecret(String weixinAppSecret) {
        this.weixinAppSecret = weixinAppSecret;
    }

    public boolean isUserDefined() {
        return this.isUserDefined;
    }

    public void setUserDefined(boolean isUserDefined) {
        this.isUserDefined = isUserDefined;
    }
}
