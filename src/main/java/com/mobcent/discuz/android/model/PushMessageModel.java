package com.mobcent.discuz.android.model;

public class PushMessageModel extends BaseModel {
    private static final long serialVersionUID = -4130566338947229624L;
    private String pushDesc;
    private int pushDetailType;
    private long pushMsgId;
    private int pushType;
    private String title;
    private long topicId;

    public long getPushMsgId() {
        return this.pushMsgId;
    }

    public void setPushMsgId(long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPushDesc() {
        return this.pushDesc;
    }

    public void setPushDesc(String pushDesc) {
        this.pushDesc = pushDesc;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public int getPushType() {
        return this.pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public int getPushDetailType() {
        return this.pushDetailType;
    }

    public void setPushDetailType(int pushDetailType) {
        this.pushDetailType = pushDetailType;
    }
}
