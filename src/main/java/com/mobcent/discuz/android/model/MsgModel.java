package com.mobcent.discuz.android.model;

import java.util.List;

public class MsgModel extends BaseModel {
    private static final long serialVersionUID = -5423523581449998306L;
    private List<MsgUserListModel> pmList;
    private UserInfoModel userInfoModel;

    public UserInfoModel getUserInfoModel() {
        return this.userInfoModel;
    }

    public void setUserInfoModel(UserInfoModel userInfoModel) {
        this.userInfoModel = userInfoModel;
    }

    public List<MsgUserListModel> getPmList() {
        return this.pmList;
    }

    public void setPmList(List<MsgUserListModel> pmList) {
        this.pmList = pmList;
    }
}
