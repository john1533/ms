package com.mobcent.discuz.module.msg.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.service.MsgService;
import com.mobcent.discuz.android.service.impl.MsgServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.module.msg.fragment.adapter.BaseCommentAtListFragmentAdapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseCommentAtListFragment extends BaseFragment implements FinalConstant {
    protected BaseCommentAtListFragmentAdapter adapter;
    protected List<CommentAtModel> commentAtList;
    protected ConfigComponentModel componentModel;
    protected int currentPage = 1;
    protected boolean isLocal;
    protected PullToRefreshListView listView;
    private LoadDataAsyncTask loadDataAsyncTask;
    protected MsgService msgService;
    protected int pageSize = 10;

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, BaseResultModel<List<CommentAtModel>>> {
        private int requestId;

        public LoadDataAsyncTask(int requestId) {
            this.requestId = requestId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            switch (this.requestId) {
                case 1:
                    BaseCommentAtListFragment.this.currentPage = 1;
                    return;
                case 2:
                    BaseCommentAtListFragment baseCommentAtListFragment = BaseCommentAtListFragment.this;
                    baseCommentAtListFragment.currentPage++;
                    return;
                default:
                    BaseCommentAtListFragment.this.currentPage = 1;
                    return;
            }
        }

        protected BaseResultModel<List<CommentAtModel>> doInBackground(Void... params) {
            return BaseCommentAtListFragment.this.getCommentAtList();
        }

        protected void onPostExecute(BaseResultModel<List<CommentAtModel>> result) {
            if (this.requestId == 1) {
                BaseCommentAtListFragment.this.listView.onRefreshComplete();
            }
            if (result != null) {
                DZToastAlertUtils.toast(BaseCommentAtListFragment.this.activity, result);
                if (result.getRs() == 0) {
                    BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(3);
                    if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                        MCToastUtils.toastByResName(BaseCommentAtListFragment.this.activity.getApplicationContext(), "mc_forum_get_at_comment_fail");
                        return;
                    } else {
                        MCToastUtils.toast(BaseCommentAtListFragment.this.activity.getApplicationContext(), result.getErrorInfo());
                        return;
                    }
                }
                if (this.requestId == 1) {
                    BaseCommentAtListFragment.this.commentAtList.clear();
                    if (((List) result.getData()).isEmpty()) {
                        BaseCommentAtListFragment.this.adapter.notifyDataSetChanged();
                        BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(3);
                        if (!BaseCommentAtListFragment.this.isLocal) {
                            MCToastUtils.toastByResName(BaseCommentAtListFragment.this.activity.getApplicationContext(), "mc_forum_warn_no_such_data");
                            return;
                        }
                    }
                    BaseCommentAtListFragment.this.commentAtList.addAll((Collection) result.getData());
                } else if (this.requestId == 2) {
                    BaseCommentAtListFragment.this.commentAtList.addAll((Collection) result.getData());
                }
                BaseCommentAtListFragment.this.adapter.notifyDataSetChanged();
                if (result.getHasNext() == 1) {
                    BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(0);
                } else {
                    BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(3);
                }
                if (result.getTotalNum() > ((List) result.getData()).size()) {
                    BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(0);
                } else {
                    BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(3);
                }
                if (BaseCommentAtListFragment.this.isLocal) {
                    BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(3);
                    return;
                }
                return;
            }
            BaseCommentAtListFragment.this.listView.onBottomRefreshComplete(3);
            if (this.requestId == 1) {
                MCToastUtils.toastByResName(BaseCommentAtListFragment.this.activity.getApplicationContext(), "mc_forum_warn_no_such_data");
            }
        }
    }

    public abstract BaseResultModel<List<CommentAtModel>> getCommentAtList();

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "BaseCommentAtListFragment";
        this.componentModel = (ConfigComponentModel) getBundle().getSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
        this.msgService = new MsgServiceImpl(this.activity.getApplicationContext());
        this.isLocal = true;
        this.commentAtList = new ArrayList();
    }

    protected void initActions(View rootView) {
        this.listView.onBottomRefreshComplete(3);
        this.listView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (BaseCommentAtListFragment.this.listView.isHand()) {
                    BaseCommentAtListFragment.this.isLocal = false;
                }
                BaseCommentAtListFragment.this.onRefreshEvent();
            }
        });
        this.listView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                BaseCommentAtListFragment.this.onMoreEvent();
            }
        });
        onRefreshEvent();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                BaseCommentAtListFragment.this.loadDataByNet();
            }
        }, 1000);
    }

    private void onRefreshEvent() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(1);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    private void onMoreEvent() {
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
        this.isLocal = false;
        if (this.listView != null) {
            this.listView.onRefresh();
        }
    }
}
