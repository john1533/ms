package com.mobcent.discuz.android.model;

public class TopicContentModel extends BaseModel {
    private static final long serialVersionUID = 2684661769391352324L;
    private String desc;
    private String infor;
    private String originalInfo;
    private int type;
    private String url;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfor() {
        return this.infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOriginalInfo() {
        return this.originalInfo;
    }

    public void setOriginalInfo(String originalInfo) {
        this.originalInfo = originalInfo;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
