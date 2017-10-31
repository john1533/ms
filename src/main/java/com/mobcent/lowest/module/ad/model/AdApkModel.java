package com.mobcent.lowest.module.ad.model;

public class AdApkModel extends AdBaseModel {
    private static final long serialVersionUID = 1945721580361147895L;
    private String appName;
    private int appSize;
    private String packageName;

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppSize() {
        return this.appSize;
    }

    public void setAppSize(int appSize) {
        this.appSize = appSize;
    }
}
