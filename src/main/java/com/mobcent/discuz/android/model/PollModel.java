package com.mobcent.discuz.android.model;

import java.util.List;

public class PollModel extends BaseModel {
    private static final long serialVersionUID = -5607245334042892223L;
    private long deadLine;
    private int isVisible;
    private List<Long> pollId;
    private List<PollItemModel> pollItemList;
    private int pollStatus;
    private int type;
    private int voters;

    public long getDeadLine() {
        return this.deadLine;
    }

    public void setDeadLine(long deadLine) {
        this.deadLine = deadLine;
    }

    public int getIsVisible() {
        return this.isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getVoters() {
        return this.voters;
    }

    public void setVoters(int voters) {
        this.voters = voters;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPollStatus() {
        return this.pollStatus;
    }

    public void setPollStatus(int pollStatus) {
        this.pollStatus = pollStatus;
    }

    public List<Long> getPollId() {
        return this.pollId;
    }

    public void setPollId(List<Long> pollId) {
        this.pollId = pollId;
    }

    public List<PollItemModel> getPollItemList() {
        return this.pollItemList;
    }

    public void setPollItemList(List<PollItemModel> pollItemList) {
        this.pollItemList = pollItemList;
    }
}
