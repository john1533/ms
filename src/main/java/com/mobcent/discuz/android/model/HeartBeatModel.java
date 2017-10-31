package com.mobcent.discuz.android.model;

import java.util.List;

public class HeartBeatModel extends BaseModel {
    private static final long serialVersionUID = -4873630257756448006L;
    private HeartInfoModel atMeInfo;
    private HeartInfoModel friendInfo;
    private long heartTime;
    private List<HeartMsgModel> pmInfos;
    private long pmPeriod;
    private HeartInfoModel replyInfo;
    private long userId;

    public HeartInfoModel getReplyInfo() {
        return this.replyInfo;
    }

    public void setReplyInfo(HeartInfoModel replyInfo) {
        this.replyInfo = replyInfo;
    }

    public HeartInfoModel getAtMeInfo() {
        return this.atMeInfo;
    }

    public HeartInfoModel getFriendInfo() {
        return this.friendInfo;
    }

    public void setFriendInfo(HeartInfoModel friendInfo) {
        this.friendInfo = friendInfo;
    }

    public void setAtMeInfo(HeartInfoModel atMeInfo) {
        this.atMeInfo = atMeInfo;
    }

    public List<HeartMsgModel> getPmInfos() {
        return this.pmInfos;
    }

    public void setPmInfos(List<HeartMsgModel> pmInfos) {
        this.pmInfos = pmInfos;
    }

    public long getHeartTime() {
        return this.heartTime;
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPmPeriod() {
        return this.pmPeriod;
    }

    public void setPmPeriod(long pmPeriod) {
        this.pmPeriod = pmPeriod;
    }
}
