package com.mobcent.discuz.android.model;

public class HeartMsgModel extends BaseModel {
    private static final long serialVersionUID = 1;
    private long fromUid;
    private int plid;
    private int pmid;
    private long time;

    public long getFromUid() {
        return this.fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public int getPlid() {
        return this.plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }

    public int getPmid() {
        return this.pmid;
    }

    public void setPmid(int pmid) {
        this.pmid = pmid;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
