package com.mobcent.discuz.android.model;

public class HeartPushInfoModel extends BaseModel {
    private static final long serialVersionUID = 1;
    private String desc;
    private int detailType;
    private long pushMsgId;
    private String title;
    private long topicId;
    private int type;

    public long getPushMsgId() {
        return this.pushMsgId;
    }

    public void setPushMsgId(long pushMsgId) {
        this.pushMsgId = pushMsgId;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public int getDetailType() {
        return this.detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }
}
