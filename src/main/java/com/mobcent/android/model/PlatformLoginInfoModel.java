package com.mobcent.android.model;

import java.io.Serializable;

public class PlatformLoginInfoModel implements Serializable {
    private static final long serialVersionUID = 5741284433628706075L;
    private String accessToken;
    private int expiresIn;
    private String openid;
    private String platformType;
    private String refreshToken;
    private String scope;

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpiresIn() {
        return this.expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getPlatformType() {
        return this.platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
}
