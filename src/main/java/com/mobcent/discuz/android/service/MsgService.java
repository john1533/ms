package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.android.model.FriendModel;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import java.util.List;

public interface MsgService {
    void deleteUserMsg(long j, long j2, long j3);

    List<MsgDBModel> getChatListFromDB(long j, long j2, long j3, long j4, long j5, int i, int i2);

    BaseResultModel<List<CommentAtModel>> getCommentAtList(String str, int i, int i2, boolean z);

    BaseResultModel<List<FriendModel>> getFriendList(String str, int i, int i2);

    BaseResultModel<MsgModel> getPmList(List<MsgDBModel> list);

    BaseResultModel<List<MsgUserListModel>> getSessionList(int i, int i2, boolean z);

    boolean modifyMsgStatus(long j, long j2);

    BaseResultModel<Object> sendMsg(MsgContentModel msgContentModel, long j, long j2, String str, boolean z);
}
