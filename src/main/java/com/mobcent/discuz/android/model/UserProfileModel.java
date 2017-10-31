package com.mobcent.discuz.android.model;

import java.io.Serializable;
import java.util.List;

public class UserProfileModel implements Serializable {
    private static final long serialVersionUID = 7925949598817747668L;
    private String data;
    private String title;
    private String type;
    private List<UserProfileModel> userProfileModels;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String date) {
        this.data = date;
    }

    public List<UserProfileModel> getUserProfileModels() {
        return this.userProfileModels;
    }

    public void setUserProfileModels(List<UserProfileModel> userProfileModels) {
        this.userProfileModels = userProfileModels;
    }
}
