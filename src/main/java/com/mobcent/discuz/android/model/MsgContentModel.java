package com.mobcent.discuz.android.model;

public class MsgContentModel extends BaseModel {
    private static final long serialVersionUID = -6653804624385468207L;
    private String content;
    private long plid;
    private long pmid;
    private long sender;
    private int source;
    private int status;
    private long time;
    private String type;

    public long getSender() {
        return this.sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getPmid() {
        return this.pmid;
    }

    public void setPmid(long pmid) {
        this.pmid = pmid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPlid() {
        return this.plid;
    }

    public void setPlid(long plid) {
        this.plid = plid;
    }

    public int getSource() {
        return this.source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
