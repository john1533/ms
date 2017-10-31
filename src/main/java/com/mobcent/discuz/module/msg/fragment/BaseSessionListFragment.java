package com.mobcent.discuz.module.msg.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.observer.ActivityObserver;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.MsgService;
import com.mobcent.discuz.android.service.impl.MsgServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.msg.fragment.activity.AtListActivity;
import com.mobcent.discuz.module.msg.fragment.activity.ChatRoomActivity;
import com.mobcent.discuz.module.msg.fragment.activity.CommentListActivity;
import com.mobcent.discuz.module.msg.fragment.activity.FriendListActivity;
import com.mobcent.discuz.module.msg.fragment.adapter.BaseSessionFragmentAdapter;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.SessionListFragmentAdapterHolder1;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.SessionListFragmentAdapterHolder2;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseSessionListFragment extends BaseFragment implements FinalConstant {
    protected BaseSessionFragmentAdapter adapter;
    protected ConfigComponentModel componentModel;
    private int currentPage = 1;
    private boolean isLocal = true;
    private long lastUserId = -1;
    private LoadDataAsyncTask loadDataAsyncTask;
    protected Handler mHandler;
    protected PullToRefreshListView msgListView;
    private MsgService msgService;
    private ActivityObserver observer;
    private ObserverHelper observerHelper;
    private int pageSize = 50;
    private BaseResultModel<List<MsgUserListModel>> userMsgData;
    protected List<MsgUserListModel> userMsgList;

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, BaseResultModel<List<MsgUserListModel>>> {
        private int requestId;

        public LoadDataAsyncTask(int requestId) {
            this.requestId = requestId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (this.requestId == 1) {
                BaseSessionListFragment.this.currentPage = 1;
            } else if (this.requestId == 2) {
                BaseSessionListFragment.this.currentPage = BaseSessionListFragment.this.currentPage + 1;
            }
        }

        protected BaseResultModel<List<MsgUserListModel>> doInBackground(Void... params) {
            return BaseSessionListFragment.this.msgService.getSessionList(BaseSessionListFragment.this.currentPage, BaseSessionListFragment.this.pageSize, BaseSessionListFragment.this.isLocal);
        }

        protected void onPostExecute(BaseResultModel<List<MsgUserListModel>> result) {
            super.onPostExecute(result);
            if (this.requestId == 1) {
                BaseSessionListFragment.this.msgListView.onRefreshComplete();
            }
            BaseSessionListFragment.this.msgListView.onBottomRefreshComplete(6);
            DZToastAlertUtils.toast(BaseSessionListFragment.this.getAppApplication(), result);
            if (result.getRs() == 0) {
                BaseSessionListFragment.this.msgListView.onBottomRefreshComplete(4, BaseSessionListFragment.this.resource.getString("mc_forum_get_msg_user_fail"));
                return;
            }
            if (result.getData() != null) {
                if (this.requestId == 1) {
                    BaseSessionListFragment.this.refreshExecute(result);
                } else if (this.requestId == 2) {
                    BaseSessionListFragment.this.moreExecute(result);
                }
            }
            if (this.requestId == 1) {
                BaseSessionListFragment.this.adapter.notifyDataSetChanged();
            }
            if (result.getHasNext() == 1) {
                BaseSessionListFragment.this.msgListView.onBottomRefreshComplete(6);
            } else {
                BaseSessionListFragment.this.msgListView.onBottomRefreshComplete(6);
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "BaseSessionListFragment";
        this.componentModel = (ConfigComponentModel) getBundle().getSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
        this.msgService = new MsgServiceImpl(this.activity.getApplicationContext());
        this.mHandler = new Handler();
        this.observerHelper = ObserverHelper.getInstance();
        if (savedInstanceState != null) {
            this.userMsgData = (BaseResultModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_TEMP_DATA);
            if (this.userMsgData != null) {
                this.userMsgList = (List) this.userMsgData.getData();
            }
            this.lastUserId = savedInstanceState.getLong(BaseIntentConstant.BUNDLE_TEMP_PARAM);
        }
        if (this.userMsgList == null) {
            this.userMsgList = new ArrayList();
        }
        this.observer = new ActivityObserver() {
            public void updateReplyNum(final int replyNum) {
                BaseSessionListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (BaseSessionListFragment.this.msgListView != null) {
                            for (int i = 0; i < BaseSessionListFragment.this.msgListView.getChildCount(); i++) {
                                if (BaseSessionListFragment.this.msgListView.getChildAt(i).getTag() instanceof SessionListFragmentAdapterHolder1) {
                                    SessionListFragmentAdapterHolder1 holder1 = (SessionListFragmentAdapterHolder1) BaseSessionListFragment.this.msgListView.getChildAt(i).getTag();
                                    if (holder1.getTitleText().getText().equals(BaseSessionListFragment.this.resource.getString("mc_forum_comment"))) {
                                        BaseSessionListFragment.this.adapter.changeCount(2, replyNum, holder1, null);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            public void updateAtNum(final int atNum) {
                BaseSessionListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (BaseSessionListFragment.this.msgListView != null) {
                            for (int i = 0; i < BaseSessionListFragment.this.msgListView.getChildCount(); i++) {
                                if (BaseSessionListFragment.this.msgListView.getChildAt(i).getTag() instanceof SessionListFragmentAdapterHolder1) {
                                    SessionListFragmentAdapterHolder1 holder1 = (SessionListFragmentAdapterHolder1) BaseSessionListFragment.this.msgListView.getChildAt(i).getTag();
                                    if (holder1.getTitleText().getText().equals(BaseSessionListFragment.this.resource.getString("mc_forum_at_me"))) {
                                        BaseSessionListFragment.this.adapter.changeCount(1, atNum, holder1, null);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            public void updateChatNum(final long userId, final int chatNum) {
                BaseSessionListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        boolean isHasChatUser = false;
                        for (MsgUserListModel model : BaseSessionListFragment.this.userMsgList) {
                            if (model.getToUserId() == userId) {
                                model.setUnReadCount(chatNum);
                                isHasChatUser = true;
                                break;
                            }
                        }
                        if (isHasChatUser) {
                            if (BaseSessionListFragment.this.msgListView != null) {
                                for (int i = 4; i < BaseSessionListFragment.this.msgListView.getChildCount(); i++) {
                                    if (BaseSessionListFragment.this.msgListView.getChildAt(i).getTag() instanceof SessionListFragmentAdapterHolder2) {
                                        SessionListFragmentAdapterHolder2 holder2 = (SessionListFragmentAdapterHolder2) BaseSessionListFragment.this.msgListView.getChildAt(i).getTag();
                                        if (userId == BaseSessionListFragment.this.adapter.getChatUserId(i - 4)) {
                                            BaseSessionListFragment.this.adapter.changeCount(3, chatNum, null, holder2);
                                        }
                                    }
                                }
                            }
                        } else if (BaseSessionListFragment.this.msgListView != null) {
                            BaseSessionListFragment.this.msgListView.onRefresh();
                        }
                    }
                });
            }

            public void updateFriendNum(final int friendNum) {
                super.updateFriendNum(friendNum);
                BaseSessionListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (BaseSessionListFragment.this.msgListView != null) {
                            for (int i = 0; i < BaseSessionListFragment.this.msgListView.getChildCount(); i++) {
                                if (BaseSessionListFragment.this.msgListView.getChildAt(i).getTag() instanceof SessionListFragmentAdapterHolder1) {
                                    SessionListFragmentAdapterHolder1 holder1 = (SessionListFragmentAdapterHolder1) BaseSessionListFragment.this.msgListView.getChildAt(i).getTag();
                                    if (holder1.getTitleText().getText().equals(BaseSessionListFragment.this.resource.getString("mc_forum_new_friend"))) {
                                        BaseSessionListFragment.this.adapter.changeCount(4, friendNum, holder1, null);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            public void onRefreshMessage() {
                BaseSessionListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        BaseSessionListFragment.this.isLocal = false;
                        if (BaseSessionListFragment.this.msgListView != null) {
                            BaseSessionListFragment.this.msgListView.onRefresh(false);
                        }
                    }
                });
            }
        };
        this.observerHelper.getActivityObservable().registerObserver(this.observer);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BaseIntentConstant.BUNDLE_TEMP_DATA, this.userMsgData);
        outState.putSerializable(BaseIntentConstant.BUNDLE_TEMP_PARAM, Long.valueOf(this.lastUserId));
    }

    protected void initViews(View rootView) {
        this.msgListView = (PullToRefreshListView) findViewByName(rootView, "msg_list");
        if (!(this.isFirstInit || this.lastUserId == this.sharedPreferencesDB.getUserId())) {
            this.isFirstInit = true;
        }
        this.msgListView.onBottomRefreshComplete(3);
    }

    protected void initActions(View rootView) {
        this.msgListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (BaseSessionListFragment.this.msgListView.isHand()) {
                    BaseSessionListFragment.this.isLocal = false;
                }
                BaseSessionListFragment.this.onRefreshEvent();
            }
        });
        this.msgListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                BaseSessionListFragment.this.onMoreEvent();
            }
        });
        this.msgListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent;
                if (position == 1) {
                    intent = new Intent(BaseSessionListFragment.this.activity.getApplicationContext(), AtListActivity.class);
                    intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, BaseSessionListFragment.this.componentModel);
                    BaseSessionListFragment.this.activity.startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(BaseSessionListFragment.this.activity.getApplicationContext(), CommentListActivity.class);
                    intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, BaseSessionListFragment.this.componentModel);
                    BaseSessionListFragment.this.activity.startActivity(intent);
                } else if (position == 3) {
                    intent = new Intent(BaseSessionListFragment.this.activity.getApplicationContext(), FriendListActivity.class);
                    intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, BaseSessionListFragment.this.componentModel);
                    BaseSessionListFragment.this.activity.startActivity(intent);
                } else {
                    MsgUserListModel msgUserModel = (MsgUserListModel) BaseSessionListFragment.this.userMsgList.get(position - 4);
                    intent = new Intent(BaseSessionListFragment.this.activity.getApplicationContext(), ChatRoomActivity.class);
                    intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserModel);
                    BaseSessionListFragment.this.activity.startActivity(intent);
                }
            }
        });
        this.msgListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                if (position > 3) {
                    final String delStr = BaseSessionListFragment.this.resource.getString("mc_forum_message_del_user");
                    final String[] items = new String[]{delStr};
                    new Builder(BaseSessionListFragment.this.activity).setItems(items, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (items[which].equals(delStr)) {
                                new Thread() {
                                    public void run() {
                                        MsgUserListModel msgUserModel = (MsgUserListModel) BaseSessionListFragment.this.userMsgList.get(position - 4);
                                        new MsgServiceImpl(BaseSessionListFragment.this.activity.getApplicationContext()).deleteUserMsg(SharedPreferencesDB.getInstance(BaseSessionListFragment.this.activity.getApplicationContext()).getUserId(), msgUserModel.getPlid(), msgUserModel.getToUserId());
                                        BaseSessionListFragment.this.mHandler.post(new Runnable() {
                                            public void run() {
                                                BaseSessionListFragment.this.loadDataByNet();
                                            }
                                        });
                                    }
                                }.start();
                            }
                        }
                    }).show();
                }
                return true;
            }
        });
    }

    protected void firstCreate() {
        super.firstCreate();
        new Thread() {
            public void run() {
                BaseSessionListFragment.this.isLocal = true;
                final BaseResultModel<List<MsgUserListModel>> baseResultModel = BaseSessionListFragment.this.msgService.getSessionList(BaseSessionListFragment.this.currentPage, BaseSessionListFragment.this.pageSize, BaseSessionListFragment.this.isLocal);
                BaseSessionListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (!(baseResultModel.getRs() == 0 || baseResultModel.getData() == null)) {
                            BaseSessionListFragment.this.refreshExecute(baseResultModel);
                        }
                        BaseSessionListFragment.this.loadDataByNet();
                    }
                });
            }
        }.start();
    }

    private void onRefreshEvent() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(1);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    private void onMoreEvent() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(2);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    private void refreshExecute(BaseResultModel<List<MsgUserListModel>> result) {
        this.userMsgList.clear();
        this.userMsgList.addAll((Collection) result.getData());
    }

    private void moreExecute(BaseResultModel<List<MsgUserListModel>> result) {
        this.userMsgList.addAll((Collection) result.getData());
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.lastUserId = this.sharedPreferencesDB.getUserId();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.loadDataAsyncTask != null) {
            this.loadDataAsyncTask.cancel(true);
        }
        this.observerHelper.getActivityObservable().unregisterObserver(this.observer);
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        if (this.moduleModel != null) {
            topSettingModel.title = TextUtils.isEmpty(this.moduleModel.getTitle()) ? this.resource.getString("mc_forum_msg") : this.moduleModel.getTitle();
        } else {
            topSettingModel.title = this.resource.getString("mc_forum_msg");
        }
        dealTopBar(topSettingModel);
    }

    public void loadDataByNet() {
        this.isLocal = false;
        if (this.msgListView != null) {
            this.msgListView.onRefresh();
        }
    }
}
