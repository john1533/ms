package com.mobcent.discuz.android.model;

import java.util.List;

public class MsgUserListModel extends BaseModel {
    private static final long serialVersionUID = -727910735281381481L;
    private int hasPrev;
    private String lastDateLine;
    private String lastSummary;
    private String lastSummaryType;
    private long lastUserId;
    private String lastUserName;
    private List<MsgContentModel> msgList;
    private long plid;
    private long pmid;
    private String toUserAvatar;
    private long toUserId;
    private int toUserIsBlack;
    private String toUserName;
    private int unReadCount;

    public long getPlid() {
        return this.plid;
    }

    public void setPlid(long plid) {
        this.plid = plid;
    }

    public long getPmid() {
        return this.pmid;
    }

    public void setPmid(long pmid) {
        this.pmid = pmid;
    }

    public long getLastUserId() {
        return this.lastUserId;
    }

    public void setLastUserId(long lastUserId) {
        this.lastUserId = lastUserId;
    }

    public String getLastUserName() {
        return this.lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String getLastSummary() {
        return this.lastSummary;
    }

    public void setLastSummary(String lastSummary) {
        this.lastSummary = lastSummary;
    }

    public String getLastDateLine() {
        return this.lastDateLine;
    }

    public void setLastDateLine(String lastDateLine) {
        this.lastDateLine = lastDateLine;
    }

    public long getToUserId() {
        return this.toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserAvatar() {
        return this.toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public int getToUserIsBlack() {
        return this.toUserIsBlack;
    }

    public void setToUserIsBlack(int toUserIsBlack) {
        this.toUserIsBlack = toUserIsBlack;
    }

    public int getUnReadCount() {
        return this.unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getLastSummaryType() {
        return this.lastSummaryType;
    }

    public void setLastSummaryType(String lastSummaryType) {
        this.lastSummaryType = lastSummaryType;
    }

    public int getHasPrev() {
        return this.hasPrev;
    }

    public void setHasPrev(int hasPrev) {
        this.hasPrev = hasPrev;
    }

    public List<MsgContentModel> getMsgList() {
        return this.msgList;
    }

    public void setMsgList(List<MsgContentModel> msgList) {
        this.msgList = msgList;
    }
}
