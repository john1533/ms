package com.mobcent.discuz.android.os.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.HeartInfoModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.Serializable;
import java.util.List;

public class HeartNotificationHelper {
    public static final int AT_ID = 2;
    public static final int FRIEND_ID = 6;
    public static final int MSG_ID = 3;
    public static final int NOTIFICATION_ID = 5;
    public static final int PUSH_ID = 4;
    public static final int REPLY_ID = 1;
    private static HeartNotificationHelper _instance;
    private static Object _lock = new Object();
    private final String TAG = "HeartNotificationHelper";
    private ApplicationInfo appInfo;
    protected Context context;
    private NotificationManager nm;
    private NotifyListener notifyListener;
    private MCResource resource;
    private SharedPreferencesDB sDb;
    private SettingSharePreference sPreference;

    public static class NotifyInfoModel implements Serializable {
        private static final long serialVersionUID = 1;
        public String content;
        public Object data;
        public int iconId;
        public int id;
        public String title;
    }

    public interface NotifyListener {
        Intent getNotifyIntent(NotifyInfoModel notifyInfoModel);
    }

    public HeartNotificationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.appInfo = context.getApplicationInfo();
        this.sPreference = new SettingSharePreference(context.getApplicationContext());
        this.sDb = SharedPreferencesDB.getInstance(context.getApplicationContext());
        this.resource = MCResource.getInstance(context.getApplicationContext());
    }

    public static HeartNotificationHelper getInstance(Context context) {
        synchronized (_lock) {
            if (_instance == null) {
                _instance = new HeartNotificationHelper(context.getApplicationContext());
            }
        }
        return _instance;
    }

    public void notifyNotication(NotifyInfoModel notifyInfo) {
        if (notifyInfo.iconId == 0) {
            notifyInfo.iconId = this.appInfo.icon;
        }
        if (MCStringUtil.isEmpty(notifyInfo.title)) {
            notifyInfo.title = (String) this.context.getPackageManager().getApplicationLabel(this.appInfo);
        }
        Notification notification = null;
        switch (notifyInfo.id) {
            case 1:
                if (getReplyFalg()) {
                    notifyInfo.content = resolveString("mc_forum_heart_beat_reply_content", String.valueOf(((HeartInfoModel)notifyInfo.data).getCount()), this.context);
                    notification = createNotification(notifyInfo);
                    break;
                }
                break;
            case 2:
                if (getAtFalg()) {
                    notifyInfo.content = resolveString("mc_forum_heart_beat_at_content", String.valueOf(((HeartInfoModel)notifyInfo.data).getCount()), this.context);
                    notification = createNotification(notifyInfo);
                    break;
                }
                break;
            case 3:
                if (getMsgFalg()) {
                    List<MsgUserListModel> pmList = (List<MsgUserListModel>)notifyInfo.data;
                    MsgUserListModel msgUserListModel = (MsgUserListModel) pmList.get(pmList.size() - 1);
                    notifyInfo.content = resolveString("mc_forum_heart_beat_msg_content", new String[]{String.valueOf(msgUserListModel.getToUserName()), String.valueOf(msgUserListModel.getMsgList().size())}, this.context.getApplicationContext());
                    notification = createNotification(notifyInfo);
                    break;
                }
                break;
            case 4:
                if (getPushFlag()) {
                    notification = createNotification(notifyInfo);
                    break;
                }
                break;
            case 6:
                if (getFriendFlag()) {
                    notifyInfo.content = resolveString("mc_forum_heart_friend_title", String.valueOf(((HeartInfoModel)notifyInfo.data).getCount()), this.context);
                    notification = createNotification(notifyInfo);
                    break;
                }
                break;
        }
        if (notification != null) {
            this.nm.notify(5, notification);
        }
    }

    private String resolveString(String stringSource, String[] args, Context context) {
        String string = this.resource.getString(stringSource);
        for (int i = 0; i < args.length; i++) {
            string = string.replace("{" + i + "}", args[i]);
        }
        return string;
    }

    private String resolveString(String stringSource, String arg, Context context) {
        return this.resource.getString(stringSource).replace("{0}", arg);
    }

    public void setNotifyListener(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    private Notification createNotification(NotifyInfoModel notifyInfo) {
        Notification notification = new Notification();
        notification.when = System.currentTimeMillis();
        notification.tickerText = notifyInfo.title;
        notification.flags = 16;
        notification.icon = notifyInfo.iconId;
        if (this.sPreference.isSoundOpen(this.sDb.getUserId())) {
            notification.defaults = -1;
        }
        Intent intent = null;
        if (this.notifyListener != null) {
            intent = this.notifyListener.getNotifyIntent(notifyInfo);
        }
        if (intent == null) {
            return null;
        }
        //TODO
//        notification.setLatestEventInfo(this.context, notifyInfo.title, notifyInfo.content, PendingIntent.getActivity(this.context, 134217728, intent, 268435456));
        return notification;
    }

    private boolean getReplyFalg() {
        return this.sPreference.isReplyNotify(this.sDb.getUserId());
    }

    private boolean getAtFalg() {
        return this.sPreference.isAtNotify(this.sDb.getUserId());
    }

    private boolean getMsgFalg() {
        return true;
    }

    private boolean getPushFlag() {
        return true;
    }

    private boolean getFriendFlag() {
        return true;
    }
}
