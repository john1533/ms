package com.mobcent.discuz.android.observer;

import android.content.Context;
import com.mobcent.discuz.android.db.MsgDBUtil;
import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.MsgContentModel;
import java.util.Map;

public class ActivityObservable extends Observable<ActivityObserver> {
    public void updateHomeTabNum(int msgNum) {
        synchronized (this.mObservers) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((ActivityObserver) this.mObservers.get(i)).updateHomeTabNum(msgNum);
            }
        }
    }

    public void updateMsgNum(Context context, int msgType, int msgNum, long userId, long fromUid) {
        SharedPreferencesDB sharedPreferencesDB = SharedPreferencesDB.getInstance(context.getApplicationContext());
        MsgDBUtil msgDBUtil = MsgDBUtil.getInstance(context.getApplicationContext());
        synchronized (this.mObservers) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                switch (msgType) {
                    case 1:
                        sharedPreferencesDB.setAtMeModel(sharedPreferencesDB.getAtMeTime(userId), msgNum, userId);
                        ((ActivityObserver) this.mObservers.get(i)).updateAtNum(msgNum);
                        break;
                    case 2:
                        sharedPreferencesDB.setReplyModel(sharedPreferencesDB.getReplyTime(userId), msgNum, userId);
                        ((ActivityObserver) this.mObservers.get(i)).updateReplyNum(msgNum);
                        break;
                    case 3:
                        if (msgNum == 0) {
                            msgDBUtil.modifyMsgStatus(userId, fromUid);
                        }
                        ((ActivityObserver) this.mObservers.get(i)).updateChatNum(fromUid, msgNum);
                        break;
                    case 4:
                        sharedPreferencesDB.setFriendModel(sharedPreferencesDB.getFriendTime(userId), msgNum, userId);
                        ((ActivityObserver) this.mObservers.get(i)).updateFriendNum(msgNum);
                        break;
                    default:
                        break;
                }
                ((ActivityObserver) this.mObservers.get(i)).updateHomeTabNum(getTabNum(context, userId));
            }
        }
    }

    public int getTabNum(Context context, long userId) {
        if (userId == 0) {
            return 0;
        }
        SharedPreferencesDB sharedPreferencesDB = SharedPreferencesDB.getInstance(context.getApplicationContext());
        int tabNum = (sharedPreferencesDB.getAtMeCount(userId) + sharedPreferencesDB.getReplyCount(userId)) + sharedPreferencesDB.getFriendCount(userId);
        Map<Long, MsgDBModel> cacheMap = MsgDBUtil.getInstance(context.getApplicationContext()).getCacheSessionList(userId);
        if (!(cacheMap == null || cacheMap.isEmpty())) {
            for (Long longValue : cacheMap.keySet()) {
                tabNum += ((MsgDBModel) cacheMap.get(Long.valueOf(longValue.longValue()))).getUnReadCount();
            }
        }
        return tabNum;
    }

    public void onRefreshMessageList() {
        synchronized (this.mObservers) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((ActivityObserver) this.mObservers.get(i)).onRefreshMessage();
            }
        }
    }

    public void showChatRoomData(long chatUserId) {
        synchronized (this.mObservers) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((ActivityObserver) this.mObservers.get(i)).showChatRoomData(chatUserId);
            }
        }
    }

    public long getCurrentChatUserId() {
        synchronized (this.mObservers) {
            if (this.mObservers.isEmpty()) {
                return 0;
            }
            long currentChatUserId = ((ActivityObserver) this.mObservers.get(this.mObservers.size() - 1)).getCurrentChatUserId();
            return currentChatUserId;
        }
    }

    public void modifyChatRoomUI(long tempTime, MsgContentModel msgContentModel) {
        synchronized (this.mObservers) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((ActivityObserver) this.mObservers.get(i)).modifyChatRoomUI(tempTime, msgContentModel);
            }
        }
    }

    public void loginSuccess() {
        synchronized (this.mObservers) {
            for (int i = this.mObservers.size() - 1; i >= 0; i--) {
                ((ActivityObserver) this.mObservers.get(i)).loginSuccess();
            }
        }
    }
}
