package com.mobcent.discuz.android.observer;

import com.mobcent.discuz.android.model.MsgContentModel;

public abstract class ActivityObserver {
    public void updateHomeTabNum(int msgNum) {
    }

    public void updateAtNum(int atNum) {
    }

    public void updateReplyNum(int replyNum) {
    }

    public void updateFriendNum(int friendNum) {
    }

    public void updateChatNum(long userId, int chatNum) {
    }

    public void onRefreshMessage() {
    }

    public void showChatRoomData(long chatUserId) {
    }

    public void modifyChatRoomUI(long tempTime, MsgContentModel msgContentModel) {
    }

    public long getCurrentChatUserId() {
        return 0;
    }

    public void loginSuccess() {
    }
}
