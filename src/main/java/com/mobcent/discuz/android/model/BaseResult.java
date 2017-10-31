package com.mobcent.discuz.android.model;

import java.io.Serializable;

public class BaseResult implements Serializable {
    private static final long serialVersionUID = 1;
    private int alert;//警告标志：0失败；1成功
    private String errorCode;//错误码
    private String errorInfo;//错误信息
    private int hasNext;
    private boolean isDataChange;
    private boolean isVersionTooLow = false;
    private int page;
    private int rs;//结果标志，0失败；1成功
    private int searchId;
    private int totalNum;
    private String version;//协议版本

    public int getRs() {
        return this.rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public int getAlert() {
        return this.alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public int getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getHasNext() {
        return this.hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return this.errorInfo;
    }

    public void setErrorInfo(String errInfo) {
        this.errorInfo = errInfo;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getSearchId() {
        return this.searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public boolean isDataChange() {
        return this.isDataChange;
    }

    public void setDataChange(boolean isDataChange) {
        this.isDataChange = isDataChange;
    }

    public boolean isVersionTooLow() {
        return this.isVersionTooLow;
    }

    public void setVersionTooLow(boolean isVersionTooLow) {
        this.isVersionTooLow = isVersionTooLow;
    }
}
