package com.mobcent.discuz.module.msg.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.module.msg.fragment.ChatRoomFragment;
import com.mobcent.discuz.module.msg.helper.ChatRoomHelper;

public class ChatRoomActivity extends PopComponentActivity {
    private Fragment fragment;
    private MsgUserListModel msgUserListModel;

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        if (intent != null) {
            this.msgUserListModel = (MsgUserListModel) intent.getSerializableExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL);
        }
    }

    protected Fragment initContentFragment() {
        this.fragment = new ChatRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_MSG_USER_LIST_MODEL, this.msgUserListModel);
        this.fragment.setArguments(bundle);
        return this.fragment;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MsgUserListModel msgUserListModel = (MsgUserListModel) intent.getSerializableExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL);
        if (intent.getBooleanExtra(IntentConstant.INTENT_RESET_CHAT_DATA, false) && msgUserListModel != null && ChatRoomHelper.getInstance().getChatRoomDelegate() != null) {
            ChatRoomHelper.getInstance().getChatRoomDelegate().resetChatRoom(msgUserListModel);
        }
    }
}
