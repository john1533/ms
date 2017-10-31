package com.mobcent.discuz.module.publish.delegate;

import com.mobcent.discuz.android.model.UserInfoModel;

public class MentionFriendReturnDelegate {
    private static final MentionFriendReturnDelegate mentionReturnDelegate = new MentionFriendReturnDelegate();
    private OnMentionChannelListener onMentionChannelListener;

    public interface OnMentionChannelListener {
        void changeMentionFriend(UserInfoModel userInfoModel);
    }

    public static MentionFriendReturnDelegate getInstance() {
        return mentionReturnDelegate;
    }

    public OnMentionChannelListener getOnMentionChannelListener() {
        return this.onMentionChannelListener;
    }

    public void setOnLoginChannelListener(OnMentionChannelListener onLoginChannelListener) {
        this.onMentionChannelListener = onLoginChannelListener;
    }
}
