package com.mobcent.discuz.android.model;

import java.util.List;

public class HeartPushModel extends BaseModel {
    private static final long serialVersionUID = 1;
    private int hbTime;
    List<HeartPushInfoModel> pushList;

    public int getHbTime() {
        return this.hbTime;
    }

    public void setHbTime(int hbTime) {
        this.hbTime = hbTime;
    }

    public List<HeartPushInfoModel> getPushList() {
        return this.pushList;
    }

    public void setPushList(List<HeartPushInfoModel> pushList) {
        this.pushList = pushList;
    }
}
