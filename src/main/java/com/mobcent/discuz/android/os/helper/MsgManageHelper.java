package com.mobcent.discuz.android.os.helper;

import android.content.Context;
import com.mobcent.discuz.android.db.MsgDBUtil;
import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.HeartBeatModel;
import com.mobcent.discuz.android.model.HeartInfoModel;
import com.mobcent.discuz.android.model.HeartMsgModel;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.observer.ActivityObservable;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.os.helper.HeartNotificationHelper.NotifyInfoModel;
import com.mobcent.discuz.android.service.MsgService;
import com.mobcent.discuz.android.service.impl.MsgServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MsgManageHelper {
    private static final int PM_LIMIT_10 = 10;
    private static MsgManageHelper msgManageHelper;
    private final String TAG = "MsgManageHelper";
    private Context context;
    private MsgDBUtil msgDBUtil;
    private MsgService msgService;
    private HeartNotificationHelper notifyHelper;
    private ActivityObservable observer;
    private SharedPreferencesDB sharedPreferencesDB;

    private MsgManageHelper(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(context.getApplicationContext());
        this.notifyHelper = HeartNotificationHelper.getInstance(context.getApplicationContext());
        this.observer = ObserverHelper.getInstance().getActivityObservable();
        this.msgDBUtil = MsgDBUtil.getInstance(context.getApplicationContext());
        this.msgService = new MsgServiceImpl(context.getApplicationContext());
    }

    public static synchronized MsgManageHelper getInstance(Context context) {
        MsgManageHelper msgManageHelper1;
        synchronized (MsgManageHelper.class) {
            if (msgManageHelper == null) {
                msgManageHelper = new MsgManageHelper(context.getApplicationContext());
            }
            msgManageHelper1 = msgManageHelper;
        }
        return msgManageHelper1;
    }

    public void dealMsgFromHeart(HeartBeatModel heartModel) {
        long heartUserId = this.sharedPreferencesDB.getUserId();
        dealAt(heartUserId, heartModel.getAtMeInfo());
        dealReply(heartUserId, heartModel.getReplyInfo());
        dealFriend(heartUserId, heartModel.getFriendInfo());
        dealPmInfos(heartUserId, heartModel.getPmInfos());
    }

    private void dealFriend(long userId, HeartInfoModel friendModel) {
        if (friendModel != null && friendModel.getTime() > this.sharedPreferencesDB.getFriendTime(userId)) {
            this.sharedPreferencesDB.setFriendModel(friendModel.getTime(), friendModel.getCount(), userId);
            NotifyInfoModel notifyInfoModel = new NotifyInfoModel();
            notifyInfoModel.data = friendModel;
            notifyInfoModel.id = 6;
            this.notifyHelper.notifyNotication(notifyInfoModel);
            this.observer.updateMsgNum(this.context.getApplicationContext(), 4, friendModel.getCount(), userId, 0);
        }
    }

    private void dealAt(long userId, HeartInfoModel atModel) {
        if (atModel != null && atModel.getTime() > this.sharedPreferencesDB.getAtMeTime(userId)) {
            this.sharedPreferencesDB.setAtMeModel(atModel.getTime(), atModel.getCount(), userId);
            NotifyInfoModel notifyInfoModel = new NotifyInfoModel();
            notifyInfoModel.data = atModel;
            notifyInfoModel.id = 2;
            this.notifyHelper.notifyNotication(notifyInfoModel);
            this.observer.updateMsgNum(this.context.getApplicationContext(), 1, atModel.getCount(), userId, 0);
        }
    }

    private void dealReply(long userId, HeartInfoModel replyModel) {
        if (replyModel != null && replyModel.getTime() > this.sharedPreferencesDB.getReplyTime(userId)) {
            this.sharedPreferencesDB.setReplyModel(replyModel.getTime(), replyModel.getCount(), userId);
            NotifyInfoModel notifyInfoModel = new NotifyInfoModel();
            notifyInfoModel.data = replyModel;
            notifyInfoModel.id = 1;
            this.notifyHelper.notifyNotication(notifyInfoModel);
            this.observer.updateMsgNum(this.context.getApplicationContext(), 2, replyModel.getCount(), userId, 0);
        }
    }

    private void dealPmInfos(long userId, List<HeartMsgModel> pmInfos) {
        if (this.msgDBUtil.createTable(userId) && pmInfos != null && !pmInfos.isEmpty()) {
            int i;
            List<Long> fromUidList = new ArrayList();
            for (i = 0; i < pmInfos.size(); i++) {
                fromUidList.add(Long.valueOf(((HeartMsgModel) pmInfos.get(i)).getFromUid()));
            }
            Map<Long, MsgDBModel> timeAndCountMap = this.msgDBUtil.getLastTimeAndCacheCount(userId, fromUidList);
            List<MsgDBModel> tempList = new ArrayList();
            for (i = 0; i < pmInfos.size(); i++) {
                HeartMsgModel heartMsgModel = (HeartMsgModel) pmInfos.get(i);
                MsgDBModel msgDBModel = timeAndCountMap == null ? null : (MsgDBModel) timeAndCountMap.get(Long.valueOf(heartMsgModel.getFromUid()));
                long lastTime = msgDBModel == null ? 0 : msgDBModel.getStartTime();
                if (lastTime == 0 || lastTime < heartMsgModel.getTime()) {
                    MsgDBModel tempModel = new MsgDBModel();
                    tempModel.setFromUid(heartMsgModel.getFromUid());
                    tempModel.setStartTime(lastTime);
                    tempModel.setStopTime(0);
                    tempModel.setPlid((long) heartMsgModel.getPlid());
                    tempModel.setPmid((long) heartMsgModel.getPmid());
                    tempModel.setCacheCount(msgDBModel == null ? 0 : msgDBModel.getCacheCount());
                    tempModel.setPmLimit(10);
                    tempList.add(tempModel);
                }
            }
            if (tempList != null && !tempList.isEmpty()) {
                BaseResultModel<MsgModel> baseResultModel = this.msgService.getPmList(tempList);
                if (baseResultModel != null && baseResultModel.getData() != null && ((MsgModel) baseResultModel.getData()).getUserInfoModel() != null && ((MsgModel) baseResultModel.getData()).getPmList() != null && !((MsgModel) baseResultModel.getData()).getPmList().isEmpty()) {
                    i = 0;
                    while (i < ((MsgModel) baseResultModel.getData()).getPmList().size()) {
                        MsgUserListModel msgUserListModel = (MsgUserListModel) ((MsgModel) baseResultModel.getData()).getPmList().get(i);
                        if (msgUserListModel != null && msgUserListModel.getMsgList() != null && !msgUserListModel.getMsgList().isEmpty()) {
                            Map<Long, MsgDBModel> cacheMap = this.msgDBUtil.getCacheMsgList(((MsgModel) baseResultModel.getData()).getUserInfoModel().getUserId(), msgUserListModel.getToUserId(), 0);
                            if (cacheMap != null) {
                                List<MsgContentModel> msgList = msgUserListModel.getMsgList();
                                List<MsgContentModel> removeList = new ArrayList();
                                for (MsgContentModel contentModel : msgList) {
                                    if (cacheMap.containsKey(Long.valueOf(contentModel.getPmid()))) {
                                        removeList.add(contentModel);
                                    }
                                }
                                msgList.removeAll(removeList);
                            }
                            i++;
                        } else {
                            return;
                        }
                    }
                    boolean insertSucc = this.msgDBUtil.saveMsg(((MsgModel) baseResultModel.getData()).getUserInfoModel(), ((MsgModel) baseResultModel.getData()).getPmList());
                    List<MsgUserListModel> pmList = ((MsgModel) baseResultModel.getData()).getPmList();
                    if (insertSucc) {
                        boolean isAtChatRoom = false;
                        this.observer.onRefreshMessageList();
                        for (i = 0; i < pmList.size(); i++) {
                            MsgUserListModel model = (MsgUserListModel) pmList.get(i);
                            this.observer.updateMsgNum(this.context.getApplicationContext(), 3, model.getMsgList().size(), userId, model.getToUserId());
                            if (this.observer.getCurrentChatUserId() == model.getToUserId()) {
                                this.observer.showChatRoomData(model.getToUserId());
                                isAtChatRoom = true;
                            }
                        }
                        if (!isAtChatRoom) {
                            NotifyInfoModel notifyInfoModel = new NotifyInfoModel();
                            notifyInfoModel.data = ((MsgModel) baseResultModel.getData()).getPmList();
                            notifyInfoModel.id = 3;
                            this.notifyHelper.notifyNotication(notifyInfoModel);
                        }
                    }
                }
            }
        }
    }
}
