package com.mobcent.discuz.module.msg.helper;

import android.content.Context;
import android.content.Intent;
import com.mobcent.discuz.activity.HomeActivity;
import com.mobcent.discuz.activity.SplashActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.HeartPushInfoModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.os.helper.HeartNotificationHelper;
import com.mobcent.discuz.android.os.helper.HeartNotificationHelper.NotifyInfoModel;
import com.mobcent.discuz.android.os.helper.HeartNotificationHelper.NotifyListener;
import com.mobcent.discuz.module.msg.fragment.activity.ChatRoomActivity;
import com.mobcent.discuz.module.msg.fragment.activity.SessionListActivity;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.lowest.base.utils.MCActivityUtils;
import java.util.List;

public class MsgNotificationHelper {
    private static MsgNotificationHelper _instance;
    private static Object _lock = new Object();
    private final String TAG = "MsgNotificationHelper";
    private Context context;
    private boolean isAtSessionList;
    private boolean isContainMsg;
    private boolean isHomeActivity;
    private boolean isNotExistApp;
    private ObserverHelper observerHelper;

    public void setHomeActivity(boolean isHomeActivity) {
        this.isHomeActivity = isHomeActivity;
    }

    public void setContainMsg(boolean isContainMsg) {
        this.isContainMsg = isContainMsg;
    }

    public boolean isContainMsg() {
        return this.isContainMsg;
    }

    public void setAtSessionList(boolean isAtSessionList) {
        this.isAtSessionList = isAtSessionList;
    }

    public void setNotExistApp(boolean isNotExistApp) {
        this.isNotExistApp = isNotExistApp;
    }

    private MsgNotificationHelper(Context context) {
        this.context = context.getApplicationContext();
        this.observerHelper = ObserverHelper.getInstance();
    }

    public static MsgNotificationHelper getInstance(Context context) {
        synchronized (_lock) {
            if (_instance == null) {
                _instance = new MsgNotificationHelper(context);
            }
        }
        return _instance;
    }

    public void setIntentToNotification() {
        HeartNotificationHelper.getInstance(this.context).setNotifyListener(new NotifyListener() {
            public Intent getNotifyIntent(NotifyInfoModel notifyInfo) {
                Intent intent = new Intent();
                if (notifyInfo != null && notifyInfo.id == 4) {
                    return MsgNotificationHelper.this.getPushIntent(notifyInfo);
                }
                MsgUserListModel msgUserListModel;
                if (MCActivityUtils.isAction(MsgNotificationHelper.this.context)) {
                    if (MsgNotificationHelper.this.isHomeActivity) {
                        if (!MsgNotificationHelper.this.isContainMsg) {
                            intent = MsgNotificationHelper.this.skipToSession();
                        } else if (notifyInfo.id == 3) {
                            msgUserListModel = MsgNotificationHelper.this.getMsgUserListModel(notifyInfo.data);
                            intent = MsgNotificationHelper.this.skipToHomeMsg();
                            intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                            intent.putExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE, FinalConstant.HOME_SKIP_TO_CHAT);
                        } else {
                            intent = MsgNotificationHelper.this.skipToHomeMsg();
                        }
                    } else if (MsgNotificationHelper.this.isContainMsg) {
                        if (notifyInfo.id == 3) {
                            msgUserListModel = MsgNotificationHelper.this.getMsgUserListModel(notifyInfo.data);
                            if (MsgNotificationHelper.this.observerHelper.getActivityObservable().getCurrentChatUserId() != 0) {
                                intent = new Intent(MsgNotificationHelper.this.context, ChatRoomActivity.class);
                                intent.putExtra(IntentConstant.INTENT_RESET_CHAT_DATA, true);
                                intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                            } else {
                                intent = MsgNotificationHelper.this.skipToHomeMsg();
                                intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                                intent.putExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE, FinalConstant.HOME_SKIP_TO_CHAT);
                            }
                        } else {
                            intent = MsgNotificationHelper.this.skipToHomeMsg();
                        }
                    } else if (MsgNotificationHelper.this.isAtSessionList) {
                        return intent;
                    } else {
                        if (notifyInfo.id != 3 || MsgNotificationHelper.this.observerHelper.getActivityObservable().getCurrentChatUserId() == 0) {
                            intent = MsgNotificationHelper.this.skipToSession();
                        } else {
                            msgUserListModel = MsgNotificationHelper.this.getMsgUserListModel(notifyInfo.data);
                            intent = new Intent(MsgNotificationHelper.this.context, ChatRoomActivity.class);
                            intent.putExtra(IntentConstant.INTENT_RESET_CHAT_DATA, true);
                            intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                        }
                    }
                } else if (!MsgNotificationHelper.this.isNotExistApp) {
                    intent = new Intent(MsgNotificationHelper.this.context, SplashActivity.class);
                    intent.putExtra(IntentConstant.INTENT_GET_ALL_DATA_BY_NET, false);
                    intent.putExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE, FinalConstant.HOME_SKIP_TO_SESSION);
                } else if (!MsgNotificationHelper.this.isContainMsg) {
                    intent = MsgNotificationHelper.this.skipToSession();
                } else if (notifyInfo.id == 3) {
                    msgUserListModel = MsgNotificationHelper.this.getMsgUserListModel(notifyInfo.data);
                    intent = MsgNotificationHelper.this.skipToHomeMsg();
                    intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                    intent.putExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE, FinalConstant.HOME_SKIP_TO_CHAT);
                } else {
                    intent = MsgNotificationHelper.this.skipToHomeMsg();
                }
                intent.setFlags(131072);
                return intent;
            }
        });
    }

    private Intent getPushIntent(NotifyInfoModel notifyInfo) {
        Intent intent = new Intent();
        if (notifyInfo.data instanceof HeartPushInfoModel) {
            HeartPushInfoModel pushModel = (HeartPushInfoModel)notifyInfo.data;
            if (pushModel.getType() == 2) {
                intent = new Intent(this.context, TopicDetailActivity.class);
                intent.putExtra("topicId", pushModel.getTopicId());
            } else {
                intent = new Intent(this.context, HomeActivity.class);
                intent.putExtra("push", true);
            }
        }
        intent.setFlags(131072);
        return intent;
    }

    private MsgUserListModel getMsgUserListModel(Object data) {
        List<MsgUserListModel> pmList = (List) data;
        return (MsgUserListModel) pmList.get(pmList.size() - 1);
    }

    private Intent skipToSession() {
        Intent intent = new Intent();
        return new Intent(this.context, SessionListActivity.class);
    }

    private Intent skipToHomeMsg() {
        Intent intent = new Intent();
        intent = new Intent(this.context, HomeActivity.class);
        intent.putExtra(IntentConstant.INTENT_SKIP_TO_HOME_MSG, true);
        return intent;
    }

    private Intent skipToChat() {
        Intent intent = new Intent();
        return new Intent(this.context, ChatRoomActivity.class);
    }
}
