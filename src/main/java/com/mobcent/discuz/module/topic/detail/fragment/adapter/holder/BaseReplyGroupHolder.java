package com.mobcent.discuz.module.topic.detail.fragment.adapter.holder;

import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;

public class BaseReplyGroupHolder {
    private TextView replyLabText;
    private MCHeadIcon userImg;
    private TextView userNameText;
    private TextView userRoleText;
    private TextView userTileText;

    public MCHeadIcon getUserImg() {
        return this.userImg;
    }

    public void setUserImg(MCHeadIcon userImg) {
        this.userImg = userImg;
    }

    public TextView getUserNameText() {
        return this.userNameText;
    }

    public void setUserNameText(TextView userNameText) {
        this.userNameText = userNameText;
    }

    public TextView getUserRoleText() {
        return this.userRoleText;
    }

    public void setUserRoleText(TextView userRoleText) {
        this.userRoleText = userRoleText;
    }

    public TextView getUserTileText() {
        return this.userTileText;
    }

    public void setUserTileText(TextView userTileText) {
        this.userTileText = userTileText;
    }

    public TextView getReplyLabText() {
        return this.replyLabText;
    }

    public void setReplyLabText(TextView replyLabText) {
        this.replyLabText = replyLabText;
    }
}
