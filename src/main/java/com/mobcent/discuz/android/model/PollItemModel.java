package com.mobcent.discuz.android.model;

public class PollItemModel extends BaseModel {
    private static final long serialVersionUID = -3558478021178790550L;
    private int itemId;
    private String percent;
    private long pollItemId;
    private String pollName;
    private double ratio;
    private int resultVisiable;
    private int totalNum;

    public String getPollName() {
        return this.pollName;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public long getPollItemId() {
        return this.pollItemId;
    }

    public void setPollItemId(long pollItemId) {
        this.pollItemId = pollItemId;
    }

    public int getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getPercent() {
        return this.percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public double getRatio() {
        return this.ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getResultVisiable() {
        return this.resultVisiable;
    }

    public void setResultVisiable(int resultVisiable) {
        this.resultVisiable = resultVisiable;
    }

    public int getItemId() {
        return this.itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
