package com.mobcent.discuz.android.model;

import java.util.List;

public class BoardModel extends BaseModel {
    private static final long serialVersionUID = -5055264636829393501L;
    private int onlineUserNum;
    private List<BoardParent> parentList;
    private int tdVisitors;

    public int getOnlineUserNum() {
        return this.onlineUserNum;
    }

    public void setOnlineUserNum(int onlineUserNum) {
        this.onlineUserNum = onlineUserNum;
    }

    public int getTdVisitors() {
        return this.tdVisitors;
    }

    public void setTdVisitors(int tdVisitors) {
        this.tdVisitors = tdVisitors;
    }

    public List<BoardParent> getParentList() {
        return this.parentList;
    }

    public void setParentList(List<BoardParent> parentList) {
        this.parentList = parentList;
    }
}
