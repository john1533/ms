package com.mobcent.discuz.module.person.activity.fragment.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class UserListFragmentAdapterHolder {
    private TextView locationText;
    private TextView signature;
    private TextView timeText;
    private ImageView userGender;
    private MCHeadIcon userIcon;
    private TextView userName;
    private ImageView userStatus;

    public MCHeadIcon getUserIcon() {
        return this.userIcon;
    }

    public void setUserIcon(MCHeadIcon userIcon) {
        this.userIcon = userIcon;
    }

    public TextView getUserName() {
        return this.userName;
    }

    public void setUserName(TextView userName) {
        this.userName = userName;
    }

    public ImageView getUserGender() {
        return this.userGender;
    }

    public void setUserGender(ImageView userGender) {
        this.userGender = userGender;
    }

    public ImageView getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(ImageView userStatus) {
        this.userStatus = userStatus;
    }

    public TextView getLocationText() {
        return this.locationText;
    }

    public void setLocationText(TextView locationText) {
        this.locationText = locationText;
    }

    public TextView getTimeText() {
        return this.timeText;
    }

    public void setTimeText(TextView timeText) {
        this.timeText = timeText;
    }

    public TextView getSignature() {
        return this.signature;
    }

    public void setSignature(TextView signature) {
        this.signature = signature;
    }
}
