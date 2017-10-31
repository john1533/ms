package com.mobcent.discuz.module.msg.helper;

import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;

public class ChatRoomHelper {
    private static ChatRoomHelper helper;
    private ChatRoomDelegate chatRoomDelegate;

    public interface ChatRoomDelegate {
        void resetChatRoom(MsgUserListModel msgUserListModel);

        void sendMsgAgain(MsgContentModel msgContentModel, long j, long j2, String str, boolean z);
    }

    private ChatRoomHelper() {
    }

    public static ChatRoomHelper getInstance() {
        if (helper == null) {
            helper = new ChatRoomHelper();
        }
        return helper;
    }

    public void setChatRoomDelegate(ChatRoomDelegate chatRoomDelegate) {
        this.chatRoomDelegate = chatRoomDelegate;
    }

    public ChatRoomDelegate getChatRoomDelegate() {
        return this.chatRoomDelegate;
    }
}
