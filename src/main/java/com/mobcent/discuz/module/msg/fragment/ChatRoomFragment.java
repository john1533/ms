package com.mobcent.discuz.module.msg.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCMultiBottomView;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnPicDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnRecordDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.OnSendDelegate;
import com.mobcent.discuz.activity.view.MCMultiBottomView.PicModel;
import com.mobcent.discuz.activity.view.MCMultiBottomView.RecordModel;
import com.mobcent.discuz.activity.view.MCMultiBottomView.SendModel;
import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.observer.ActivityObserver;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.os.helper.HeartBeatServiceHelper;
import com.mobcent.discuz.android.service.MsgService;
import com.mobcent.discuz.android.service.impl.MsgServiceImpl;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.msg.fragment.adapter.ChatRoomFragmentAdapter;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.ChatRoomFragmentAdapterHolder1;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.ChatRoomFragmentAdapterHolder2;
import com.mobcent.discuz.module.msg.helper.ChatRoomHelper;
import com.mobcent.discuz.module.msg.helper.ChatRoomHelper.ChatRoomDelegate;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils.AudioPlayListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomFragment extends BaseFragment implements AudioPlayListener, SlideDelegate {
    private Object _lock = new Object();
    private ChatRoomFragmentAdapter adapter;
    private MCAudioUtils audioUtils;
    private MCMultiBottomView bottomView;
    private List<MsgDBModel> chatList;
    private ChatRoomHelper chatRoomHelper;
    private PullToRefreshListView chatRoomListView;
    private LoadDataAsyncTask loadDataAsyncTask;
    private MsgService msgService;
    private MsgUserListModel msgUserListModel;
    private ActivityObserver observer;
    private ObserverHelper observerHelper;
    private long userId;

    private class LoadDataAsyncTask extends AsyncTask<Integer, Void, List<MsgDBModel>> {
        private int reqState;

        private LoadDataAsyncTask() {
        }

        protected List<MsgDBModel> doInBackground(Integer... params) {
            List<MsgDBModel> mChatList = null;
            this.reqState = params[0].intValue();
            long time = 0;
            int i;
            if (this.reqState == 2) {
                if (!ChatRoomFragment.this.chatList.isEmpty()) {
                    int size = ChatRoomFragment.this.chatList.size();
                    for (i = 0; i < size; i++) {
                        time = ((MsgDBModel) ChatRoomFragment.this.chatList.get(i)).getTime();
                        if (time > 0) {
                            break;
                        }
                    }
                }
                if (time > 0) {
                    mChatList = ChatRoomFragment.this.msgService.getChatListFromDB(ChatRoomFragment.this.userId, ChatRoomFragment.this.msgUserListModel.getToUserId(), ChatRoomFragment.this.msgUserListModel.getPlid(), ChatRoomFragment.this.msgUserListModel.getPmid(), time, 10, 1);
                }
            } else if (this.reqState == 1) {
                if (!ChatRoomFragment.this.chatList.isEmpty()) {
                    MsgDBModel lastMsgModel = (MsgDBModel) ChatRoomFragment.this.chatList.get(ChatRoomFragment.this.chatList.size() - 1);
                    if (lastMsgModel.getSource() == 1) {
                        time = lastMsgModel.getTime();
                    } else {
                        for (i = ChatRoomFragment.this.chatList.size() - 1; i >= 0; i--) {
                            if (((MsgDBModel) ChatRoomFragment.this.chatList.get(i)).getSource() == 1) {
                                time = ((MsgDBModel) ChatRoomFragment.this.chatList.get(i)).getTime();
                                break;
                            }
                        }
                    }
                    if (time == 0) {
                        time = lastMsgModel.getTime();
                    }
                }
                if (time > 0) {
                    mChatList = ChatRoomFragment.this.msgService.getChatListFromDB(ChatRoomFragment.this.userId, ChatRoomFragment.this.msgUserListModel.getToUserId(), ChatRoomFragment.this.msgUserListModel.getPlid(), ChatRoomFragment.this.msgUserListModel.getPmid(), time, 10, 2);
                }
            }
            if (time == 0) {
                return ChatRoomFragment.this.msgService.getChatListFromDB(ChatRoomFragment.this.userId, ChatRoomFragment.this.msgUserListModel.getToUserId(), ChatRoomFragment.this.msgUserListModel.getPlid(), ChatRoomFragment.this.msgUserListModel.getPmid(), time, 10, 1);
            }
            return mChatList;
        }

        protected void onPostExecute(List<MsgDBModel> result) {
            List result2 = result;
            ChatRoomFragment.this.chatRoomListView.onRefreshComplete();
            if (result == null) {
                result2 = new ArrayList();
            }
            switch (this.reqState) {
                case 1:
                    Map<Long, MsgDBModel> tempTimeMap = new HashMap();
                    Map<Long, MsgDBModel> tempPmidMap = new HashMap();
                    for (MsgDBModel msgDBModel : ChatRoomFragment.this.chatList) {
                        tempTimeMap.put(Long.valueOf(msgDBModel.getTime()), msgDBModel);
                        tempPmidMap.put(Long.valueOf(msgDBModel.getPmid()), msgDBModel);
                    }
                    List<MsgDBModel> removeList = new ArrayList();
                    for (int i = 0; i < result2.size(); i++) {
                        MsgDBModel msDbModel = (MsgDBModel) result2.get(i);
                        if ((tempPmidMap.containsKey(Long.valueOf(msDbModel.getPmid())) && tempTimeMap.containsKey(Long.valueOf(msDbModel.getTime()))) || (msDbModel.getStatus() == 0 && tempPmidMap.containsKey(Long.valueOf(msDbModel.getPmid())))) {
                            removeList.add(msDbModel);
                        }
                    }
                    result2.removeAll(removeList);
                    ChatRoomFragment.this.chatList.addAll(result2);
                    ChatRoomFragment.this.notifyData(ChatRoomFragment.this.chatList);
                    if (!ChatRoomFragment.this.isBottom()) {
                        ChatRoomFragment.this.chatRoomListView.setSelection(ChatRoomFragment.this.chatList.size() - 1);
                        break;
                    }
                    break;
                case 2:
                    List<MsgDBModel> list = new ArrayList();
                    list.addAll(result2);
                    list.addAll(ChatRoomFragment.this.chatList);
                    ChatRoomFragment.this.chatList.clear();
                    ChatRoomFragment.this.chatList.addAll(list);
                    list.clear();
                    ChatRoomFragment.this.notifyData(ChatRoomFragment.this.chatList);
                    int currentPostion = 0;
                    if (!MCListUtils.isEmpty(result2)) {
                        currentPostion = result2.size() - 1;
                    }
                    ChatRoomFragment.this.chatRoomListView.setSelection(currentPostion);
                    break;
            }
            result2.clear();
        }
    }

    private class SendTask extends AsyncTask<Void, Void, BaseResultModel<Object>> {
        private String fromName;
        private long fromUid;
        private boolean isAgain;
        private MsgContentModel msgContentModel;
        private long userId;

        protected void onPreExecute() {
            super.onPreExecute();
            ChatRoomFragment.this.bottomView.hidePanel();
        }

        public SendTask(MsgContentModel msgContentModel) {
            this.msgContentModel = msgContentModel;
            this.userId = ChatRoomFragment.this.userId;
            this.fromUid = ChatRoomFragment.this.msgUserListModel.getToUserId();
            this.fromName = ChatRoomFragment.this.msgUserListModel.getToUserName();
            this.isAgain = false;
        }

        public SendTask(MsgContentModel msgContentModel, long userId, long fromUid, String fromName, boolean isAgain) {
            this.msgContentModel = msgContentModel;
            this.userId = userId;
            this.fromUid = fromUid;
            this.fromName = fromName;
            this.isAgain = isAgain;
        }

        protected BaseResultModel<Object> doInBackground(Void... arg0) {
            return ChatRoomFragment.this.msgService.sendMsg(this.msgContentModel, this.userId, this.fromUid, this.fromName, this.isAgain);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(ChatRoomFragment.this.activity.getApplicationContext(), result);
            if (result.getRs() != 0) {
                return;
            }
            if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                MCToastUtils.toast(ChatRoomFragment.this.activity.getApplicationContext(), ChatRoomFragment.this.resource.getString("mc_forum_send_fail"));
            } else {
                MCToastUtils.toast(ChatRoomFragment.this.activity.getApplicationContext(), result.getErrorInfo());
            }
        }
    }

    protected String getRootLayoutName() {
        return "chat_room_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "ChatRoomFragment";
        this.observerHelper = ObserverHelper.getInstance();
        this.chatList = new ArrayList();
        this.msgService = new MsgServiceImpl(this.activity.getApplicationContext());
        this.audioUtils = MCAudioUtils.getInstance(this.activity.getApplicationContext());
        this.audioUtils.registerListener(this);
        this.userId = this.sharedPreferencesDB.getUserId();
        this.msgUserListModel = (MsgUserListModel) getBundle().getSerializable(IntentConstant.INTENT_MSG_USER_LIST_MODEL);
        this.observer = new ActivityObserver() {
            public void showChatRoomData(final long chatUserId) {
                ChatRoomFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (ChatRoomFragment.this.msgUserListModel.getToUserId() == chatUserId) {
                            ChatRoomFragment.this.chatRoomListView.onBottomRefreshExt();
                        }
                    }
                });
            }

            public long getCurrentChatUserId() {
                return ChatRoomFragment.this.msgUserListModel.getToUserId();
            }

            public void modifyChatRoomUI(final long tempTime, final MsgContentModel msgContentModel) {
                synchronized (ChatRoomFragment.this._lock) {
                    ChatRoomFragment.this.mHandler.post(new Runnable() {
                        public void run() {
                            MsgDBModel msgDBModel = new MsgDBModel();
                            msgDBModel.setPlid(msgContentModel.getPlid());
                            msgDBModel.setPmid(msgContentModel.getPmid());
                            msgDBModel.setTime(msgContentModel.getTime());
                            msgDBModel.setContent(msgContentModel.getContent());
                            msgDBModel.setStatus(msgContentModel.getStatus());
                            msgDBModel.setSource(msgContentModel.getSource());
                            msgDBModel.setFromUid(msgContentModel.getSender());
                            msgDBModel.setType(msgContentModel.getType());
                            for (int j = 0; j < ChatRoomFragment.this.chatRoomListView.getChildCount(); j++) {
                                if (ChatRoomFragment.this.chatRoomListView.getChildAt(j).getTag() instanceof ChatRoomFragmentAdapterHolder2) {
                                    ChatRoomFragmentAdapterHolder2 holder2 = (ChatRoomFragmentAdapterHolder2) ChatRoomFragment.this.chatRoomListView.getChildAt(j).getTag();
                                    if (tempTime == ((Long) holder2.getTimeText().getTag()).longValue()) {
                                        ChatRoomFragment.this.adapter.dealMyData(holder2, msgDBModel);
                                        break;
                                    }
                                }
                            }
                            for (int i = 0; i < ChatRoomFragment.this.chatList.size(); i++) {
                                if (((MsgDBModel) ChatRoomFragment.this.chatList.get(i)).getTime() == tempTime) {
                                    ChatRoomFragment.this.chatList.set(i, msgDBModel);
                                    return;
                                }
                            }
                        }
                    });
                }
            }
        };
        this.observerHelper.getActivityObservable().registerObserver(this.observer);
        this.chatRoomHelper = ChatRoomHelper.getInstance();
        this.chatRoomHelper.setChatRoomDelegate(new ChatRoomDelegate() {
            public void sendMsgAgain(MsgContentModel msgContentModel, long userId, long fromUid, String fromName, boolean isAgain) {
                new SendTask(msgContentModel, userId, fromUid, fromName, isAgain).execute(new Void[0]);
            }

            public void resetChatRoom(final MsgUserListModel model) {
                ChatRoomFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        ChatRoomFragment.this.chatList.clear();
                        ChatRoomFragment.this.msgUserListModel = model;
                        ChatRoomFragment.this.componentDealTopbar();
                        ChatRoomFragment.this.chatRoomListView.onRefresh(false);
                    }
                });
            }
        });
    }

    protected void initViews(View rootView) {
        this.chatRoomListView = (PullToRefreshListView) findViewByName(rootView, "chat_room_list");
        this.bottomView = (MCMultiBottomView) findViewByName(rootView, "bottom_bar_box");
        this.bottomView.setOuterRightShow(new int[]{3});
        this.bottomView.setOuterLeftShow(new int[]{1, 2});
        this.bottomView.setInnerShow(new int[]{7, 10});
        this.bottomView.setModifyPicUI(false);
        this.bottomView.setModifyRecordUI(false);
        if (this.adapter == null) {
            this.adapter = new ChatRoomFragmentAdapter(this.activity, this.chatList, this.msgUserListModel);
        }
        this.chatRoomListView.setAdapter(this.adapter);
        this.chatRoomListView.removeFooterLayout();
        this.chatRoomListView.updateHeaderLabel(1, this.resource.getString("mc_forum_cache_message"));
        this.chatRoomListView.updateHeaderLabel(2, this.resource.getString("mc_forum_cache_message"));
    }

    public void onResume() {
        boolean z = true;
        super.onResume();
        if (this.settingModel != null) {
            boolean z2;
            MCMultiBottomView mCMultiBottomView = this.bottomView;
            if (this.settingModel.getPmAudioLimit() != -1) {
                z2 = true;
            } else {
                z2 = false;
            }
            mCMultiBottomView.setRecordPerm(z2);
            mCMultiBottomView = this.bottomView;
            if (this.settingModel.getPmAllowPostImage() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            mCMultiBottomView.setPicPerm(z2);
            MCMultiBottomView mCMultiBottomView2 = this.bottomView;
            if (this.settingModel.getAllowAt() <= 0) {
                z = false;
            }
            mCMultiBottomView2.setAtPerm(z);
        }
    }

    protected void initActions(View rootView) {
        HeartBeatServiceHelper.startService(this.activity, 2);
        this.chatRoomListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                ChatRoomFragment.this.loadDataAsyncTask = new LoadDataAsyncTask();
                ChatRoomFragment.this.loadDataAsyncTask.execute(new Integer[]{Integer.valueOf(2)});
            }
        });
        this.chatRoomListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                ChatRoomFragment.this.loadDataAsyncTask = new LoadDataAsyncTask();
                ChatRoomFragment.this.loadDataAsyncTask.execute(new Integer[]{Integer.valueOf(1)});
            }
        });
        this.chatRoomListView.onRefresh(false);
        this.bottomView.setOnSendDelegate(new OnSendDelegate() {
            public void sendReply(int type, SendModel sendModel) {
                if (sendModel != null && sendModel.getWordModel() != null) {
                    ChatRoomFragment.this.createAndSendModel("text", sendModel.getWordModel().getContent(), System.currentTimeMillis() + ChatRoomFragment.this.sharedPreferencesDB.getServerTimeInterval());
                }
            }
        });
        this.bottomView.setOnPicDelegate(new OnPicDelegate() {
            public void sendPic(int type, PicModel picModel) {
                if (picModel != null && picModel.getSelectMap() != null && !picModel.getSelectMap().isEmpty()) {
                    int i = 0;
                    for (String path : picModel.getSelectMap().keySet()) {
                        i++;
                        ChatRoomFragment.this.createAndSendModel("image", path, ((long) i) + (System.currentTimeMillis() + ChatRoomFragment.this.sharedPreferencesDB.getServerTimeInterval()));
                    }
                }
            }
        });
        this.bottomView.setOnRecordDelegate(new OnRecordDelegate() {
            public void sendRecord(int type, RecordModel recordModel) {
                if (recordModel != null && !MCStringUtil.isEmpty(recordModel.getAudioPath())) {
                    ChatRoomFragment.this.createAndSendModel("audio", recordModel.getAudioPath(), System.currentTimeMillis() + ChatRoomFragment.this.sharedPreferencesDB.getServerTimeInterval());
                }
            }
        });
    }

    public void createAndSendModel(String type, String content, long time) {
        MsgContentModel msgContentModel = new MsgContentModel();
        msgContentModel.setType(type);
        msgContentModel.setContent(content);
        msgContentModel.setTime(time);
        msgContentModel.setPlid(this.msgUserListModel.getPlid());
        msgContentModel.setPmid(this.msgUserListModel.getPmid());
        msgContentModel.setStatus(1);
        new SendTask(msgContentModel).execute(new Void[0]);
    }

    private boolean isBottom() {
        if (this.chatList.size() == (this.chatRoomListView.getFirstVisiblePosition() + this.chatRoomListView.getChildCount()) - 1) {
            return true;
        }
        return false;
    }

    private void notifyData(List<MsgDBModel> result) {
        this.chatRoomListView.onBottomRefreshComplete(3);
        if (result != null && !result.isEmpty()) {
            this.adapter.setDatas(result);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void onDestroy() {
        HeartBeatServiceHelper.startService(this.activity, 1);
        super.onDestroy();
        this.bottomView.onDestroy();
        if (this.loadDataAsyncTask != null) {
            this.loadDataAsyncTask.cancel(true);
        }
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).registerListener(null);
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).release();
        this.chatList.clear();
        this.observerHelper.getActivityObservable().unregisterObserver(this.observer);
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.msgUserListModel.getToUserName();
        dealTopBar(topSettingModel);
    }

    public boolean isChildInteruptBackPress() {
        if (this.bottomView == null || this.bottomView.onKeyDown(true)) {
            return false;
        }
        return true;
    }

    public void onAudioStatusChange(String url, String path, int status, int percent) {
        int count = this.chatRoomListView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.chatRoomListView.getChildAt(i);
            if (view.getTag() != null) {
                if (view.getTag() instanceof ChatRoomFragmentAdapterHolder1) {
                    ChatRoomFragmentAdapterHolder1 holder1 = (ChatRoomFragmentAdapterHolder1) view.getTag();
                    if (holder1.getContentBox().getTag() != null && holder1.getContentBox().getTag().equals(url)) {
                        dealAudioImg(holder1.getContentAudio(), "mc_forum_voice_chat_", status, percent);
                    }
                } else {
                    ChatRoomFragmentAdapterHolder2 holder2 = (ChatRoomFragmentAdapterHolder2) view.getTag();
                    if (holder2.getContentBox().getTag() != null && holder2.getContentBox().getTag().equals(url)) {
                        dealAudioImg(holder2.getContentAudio(), "mc_forum_voice_chat2_", status, percent);
                    }
                }
            }
        }
    }

    private void dealAudioImg(ImageView img, String baseImgName, int status, int percent) {
        switch (status) {
            case 1:
                img.setImageResource(this.resource.getDrawableId(baseImgName + "img0"));
                break;
            case 2:
                img.setImageResource(this.resource.getDrawableId(baseImgName + "img1"));
                switch (percent % 3) {
                    case 0:
                        img.setImageResource(this.resource.getDrawableId(baseImgName + "img1"));
                        return;
                    case 1:
                        img.setImageResource(this.resource.getDrawableId(baseImgName + "img2"));
                        return;
                    case 2:
                        img.setImageResource(this.resource.getDrawableId(baseImgName + "img3"));
                        return;
                    default:
                        img.setImageResource(this.resource.getDrawableId(baseImgName + "img0"));
                        return;
                }
            case 3:
                break;
            case 6:
                img.setImageResource(this.resource.getDrawableId(baseImgName + "img0"));
                return;
            case 7:
                img.setImageResource(this.resource.getDrawableId(baseImgName + "img0"));
                MCToastUtils.toastByResName(this.activity.getApplicationContext(), "mc_forum_audio_play_error");
                return;
            default:
                return;
        }
        img.setImageResource(this.resource.getDrawableId(baseImgName + "img0"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.bottomView.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isSlideFullScreen() {
        if (this.bottomView.getFacePager() == null || this.bottomView.getFacePager().getCurrentItem() == 0) {
            return true;
        }
        return false;
    }
}
