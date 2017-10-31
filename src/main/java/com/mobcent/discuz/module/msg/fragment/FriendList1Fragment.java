package com.mobcent.discuz.module.msg.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.FriendModel;
import com.mobcent.discuz.android.service.MsgService;
import com.mobcent.discuz.android.service.impl.MsgServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.msg.fragment.adapter.FriendList1FragmentAdapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendList1Fragment extends BaseFragment implements FinalConstant {
    private FriendList1FragmentAdapter adapter;
    private int currentPage = 1;
    private List<FriendModel> friendList;
    private PullToRefreshListView listView;
    private LoadDataAsyncTask loadDataAsyncTask;
    private MsgService msgService;
    private int pageSize = 10;

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, BaseResultModel<List<FriendModel>>> {
        private int requestId;

        public LoadDataAsyncTask(int requestId) {
            this.requestId = requestId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            switch (this.requestId) {
                case 1:
                    FriendList1Fragment.this.currentPage = 1;
                    return;
                case 2:
                    FriendList1Fragment.this.currentPage = FriendList1Fragment.this.currentPage + 1;
                    return;
                default:
                    FriendList1Fragment.this.currentPage = 1;
                    return;
            }
        }

        protected BaseResultModel<List<FriendModel>> doInBackground(Void... params) {
            return FriendList1Fragment.this.msgService.getFriendList("friend", FriendList1Fragment.this.currentPage, FriendList1Fragment.this.pageSize);
        }

        protected void onPostExecute(BaseResultModel<List<FriendModel>> result) {
            super.onPostExecute(result);
            if (this.requestId == 1) {
                FriendList1Fragment.this.listView.onRefreshComplete();
            }
            if (result != null) {
                DZToastAlertUtils.toast(FriendList1Fragment.this.activity, result);
                if (result.getRs() == 0) {
                    FriendList1Fragment.this.listView.onBottomRefreshComplete(3);
                    if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                        MCToastUtils.toastByResName(FriendList1Fragment.this.activity.getApplicationContext(), "mc_forum_get_at_comment_fail");
                        return;
                    } else {
                        MCToastUtils.toast(FriendList1Fragment.this.activity.getApplicationContext(), result.getErrorInfo());
                        return;
                    }
                }
                if (this.requestId == 1) {
                    FriendList1Fragment.this.friendList.clear();
                    if (((List) result.getData()).isEmpty()) {
                        FriendList1Fragment.this.adapter.notifyDataSetChanged();
                        FriendList1Fragment.this.listView.onBottomRefreshComplete(3);
                    }
                    FriendList1Fragment.this.friendList.addAll((Collection) result.getData());
                } else if (this.requestId == 2) {
                    FriendList1Fragment.this.friendList.addAll((Collection) result.getData());
                }
                FriendList1Fragment.this.adapter.notifyDataSetChanged();
                if (result.getHasNext() == 1) {
                    FriendList1Fragment.this.listView.onBottomRefreshComplete(0);
                    return;
                } else {
                    FriendList1Fragment.this.listView.onBottomRefreshComplete(3);
                    return;
                }
            }
            FriendList1Fragment.this.listView.onBottomRefreshComplete(3);
            if (this.requestId == 1) {
                MCToastUtils.toastByResName(FriendList1Fragment.this.activity.getApplicationContext(), "mc_forum_warn_no_such_data");
            }
        }
    }

    protected String getRootLayoutName() {
        return "friend_list1_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "FriendList1Fragment";
        this.friendList = new ArrayList();
        this.msgService = new MsgServiceImpl(this.activity.getApplicationContext());
    }

    protected void initViews(View rootView) {
        this.listView = (PullToRefreshListView) findViewByName(rootView, "friend_list");
        if (this.adapter == null) {
            this.adapter = new FriendList1FragmentAdapter(this.activity, this.friendList);
        }
        this.listView.setAdapter(this.adapter);
    }

    protected void initActions(View rootView) {
        this.listView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                FriendList1Fragment.this.refresh();
            }
        });
        this.listView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                FriendList1Fragment.this.more();
            }
        });
        this.listView.onRefresh();
    }

    private void refresh() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(1);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    private void more() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(2);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.loadDataAsyncTask != null) {
            this.loadDataAsyncTask.cancel(true);
        }
    }

    public void loadDataByNet() {
        if (this.listView != null) {
            this.listView.onRefresh();
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_new_friend");
        dealTopBar(topSettingModel);
    }
}
