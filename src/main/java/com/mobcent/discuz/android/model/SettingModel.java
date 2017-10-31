package com.mobcent.discuz.android.model;

import java.util.List;

public class SettingModel extends BaseModel {
    private static final long serialVersionUID = -5691975715376515393L;
    private int allowAt;
    private int allowCityQueryWeather;
    private int allowRegister;
    private int allowUseWeather;
    private List<ConfigComponentModel> componentList;
    private int isForumSummaryShow;
    private int isPortalSummaryShow;
    private int isShowLocationPost;
    private int isShowLocationTopic;
    private int isTodayPostCount;
    private int plugCheck;
    private int pmAllowPostImage;
    private int pmAudioLimit;
    private int postAudioLimit;
    private String postInfo;
    private int postlistOrderby;
    private int qqConnect;
    private String serverTime;
    private String wapRegisterUrl;
    private int wxConnect;

    public String getServerTime() {
        return this.serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public int getAllowUseWeather() {
        return this.allowUseWeather;
    }

    public void setAllowUseWeather(int allowUseWeather) {
        this.allowUseWeather = allowUseWeather;
    }

    public int getAllowCityQueryWeather() {
        return this.allowCityQueryWeather;
    }

    public void setAllowCityQueryWeather(int allowCityQueryWeather) {
        this.allowCityQueryWeather = allowCityQueryWeather;
    }

    public int getQqConnect() {
        return this.qqConnect;
    }

    public void setQqConnect(int qqConnect) {
        this.qqConnect = qqConnect;
    }

    public int getPlugCheck() {
        return this.plugCheck;
    }

    public void setPlugCheck(int plugCheck) {
        this.plugCheck = plugCheck;
    }

    public int getIsForumSummaryShow() {
        return this.isForumSummaryShow;
    }

    public void setIsForumSummaryShow(int isForumSummaryShow) {
        this.isForumSummaryShow = isForumSummaryShow;
    }

    public int getIsTodayPostCount() {
        return this.isTodayPostCount;
    }

    public void setIsTodayPostCount(int isTodayPostCount) {
        this.isTodayPostCount = isTodayPostCount;
    }

    public int getPostlistOrderby() {
        return this.postlistOrderby;
    }

    public void setPostlistOrderby(int postlistOrderby) {
        this.postlistOrderby = postlistOrderby;
    }

    public int getPostAudioLimit() {
        return this.postAudioLimit;
    }

    public void setPostAudioLimit(int postAudioLimit) {
        this.postAudioLimit = postAudioLimit;
    }

    public int getIsPortalSummaryShow() {
        return this.isPortalSummaryShow;
    }

    public void setIsPortalSummaryShow(int isPortalSummaryShow) {
        this.isPortalSummaryShow = isPortalSummaryShow;
    }

    public int getAllowAt() {
        return this.allowAt;
    }

    public void setAllowAt(int allowAt) {
        this.allowAt = allowAt;
    }

    public int getAllowRegister() {
        return this.allowRegister;
    }

    public void setAllowRegister(int allowRegister) {
        this.allowRegister = allowRegister;
    }

    public String getWapRegisterUrl() {
        return this.wapRegisterUrl;
    }

    public void setWapRegisterUrl(String wapRegisterUrl) {
        this.wapRegisterUrl = wapRegisterUrl;
    }

    public int getPmAudioLimit() {
        return this.pmAudioLimit;
    }

    public void setPmAudioLimit(int pmAudioLimit) {
        this.pmAudioLimit = pmAudioLimit;
    }

    public int getPmAllowPostImage() {
        return this.pmAllowPostImage;
    }

    public void setPmAllowPostImage(int pmAllowPostImage) {
        this.pmAllowPostImage = pmAllowPostImage;
    }

    public String getPostInfo() {
        return this.postInfo;
    }

    public void setPostInfo(String postInfo) {
        this.postInfo = postInfo;
    }

    public List<ConfigComponentModel> getComponentList() {
        return this.componentList;
    }

    public void setComponentList(List<ConfigComponentModel> componentList) {
        this.componentList = componentList;
    }

    public int getIsShowLocationTopic() {
        return this.isShowLocationTopic;
    }

    public void setIsShowLocationTopic(int isShowLocationTopic) {
        this.isShowLocationTopic = isShowLocationTopic;
    }

    public int getIsShowLocationPost() {
        return this.isShowLocationPost;
    }

    public void setIsShowLocationPost(int isShowLocationPost) {
        this.isShowLocationPost = isShowLocationPost;
    }

    public int getWxConnect() {
        return this.wxConnect;
    }

    public void setWxConnect(int wxConnect) {
        this.wxConnect = wxConnect;
    }
}
