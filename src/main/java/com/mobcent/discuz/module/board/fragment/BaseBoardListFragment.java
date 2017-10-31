package com.mobcent.discuz.module.board.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.android.service.BoardService;
import com.mobcent.discuz.android.service.impl.BoardServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.listener.PausePullListOnScrollListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;

public abstract class BaseBoardListFragment extends BaseFragment {
    private BaseAdapter boardAdapter;
    protected BaseResultModel<BoardModel> boardData;
    protected BoardService boardService;
    protected long fid = -1;
    protected PullToRefreshListView listView = null;
    protected ArrayList<BoardParent> parentList = null;
    protected GetDataTask task;

    class GetDataTask extends AsyncTask<Void, Void, BaseResultModel<BoardModel>> {
        GetDataTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected BaseResultModel<BoardModel> doInBackground(Void... params) {
            if (BaseBoardListFragment.this.fid == -1) {
                return BaseBoardListFragment.this.boardService.getBoardModelByNet();
            }
            return BaseBoardListFragment.this.boardService.getBoardChildList(BaseBoardListFragment.this.fid);
        }

        protected void onPostExecute(BaseResultModel<BoardModel> result) {
            super.onPostExecute(result);
            BaseBoardListFragment.this.listView.onRefreshComplete();
            if (result.getRs() == 0 || result.getData() == null) {
                BaseBoardListFragment.this.listView.onBottomRefreshComplete(4, result.getErrorInfo());
            } else if (((BoardModel) result.getData()).getParentList() == null || ((BoardModel) result.getData()).getParentList().isEmpty()) {
                BaseBoardListFragment.this.listView.onBottomRefreshComplete(2);
            } else {
                BaseBoardListFragment.this.boardData = result;
                BaseBoardListFragment.this.onBoardDataLoaded(result);
                BaseBoardListFragment.this.listView.onBottomRefreshComplete(6);
            }
        }
    }

    protected abstract BaseAdapter getListAdapter();

    protected abstract void onBoardDataLoaded(BaseResultModel<BoardModel> baseResultModel);

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.boardService = new BoardServiceImpl(this.activity);
        this.parentList = new ArrayList();
        if (this.moduleModel != null) {
            this.fid = this.moduleModel.getForumId();
            if (this.fid == 0) {
                this.fid = -1;
            }
        }
        if (savedInstanceState != null) {
            this.boardData = (BaseResultModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_TEMP_DATA);
            this.parentList = (ArrayList) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_TEMP_LIST);
        }
    }

    protected String getRootLayoutName() {
        return "board_list_fragment1";
    }

    protected void initViews(View rootView) {
        this.listView = (PullToRefreshListView) findViewByName(rootView, "pull_refresh_list");
        if (this.boardAdapter == null) {
            this.boardAdapter = getListAdapter();
        }
        this.listView.setAdapter(this.boardAdapter);
        this.listView.setScrollListener(new PausePullListOnScrollListener(ImageLoader.getInstance(), false, true));
    }

    protected void initActions(View rootView) {
        this.listView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (!(BaseBoardListFragment.this.task == null || BaseBoardListFragment.this.task.isCancelled())) {
                    BaseBoardListFragment.this.task.cancel(true);
                }
                BaseBoardListFragment.this.task = new GetDataTask();
                BaseBoardListFragment.this.task.execute(new Void[0]);
            }
        });
        this.listView.onBottomRefreshComplete(6);
    }

    protected void firstCreate() {
        super.firstCreate();
        new Thread() {
            public void run() {
                final BaseResultModel<BoardModel> result = BaseBoardListFragment.this.fid != -1 ? null : BaseBoardListFragment.this.boardService.getBoardModelByLocal();
                BaseBoardListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (result != null && result.getRs() != 0 && result.getData() != null) {
                            BaseBoardListFragment.this.onBoardDataLoaded(result);
                        }
                    }
                });
                BaseBoardListFragment.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        BaseBoardListFragment.this.listView.onRefresh(false);
                    }
                }, 200);
            }
        }.start();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BaseIntentConstant.BUNDLE_TEMP_DATA, this.boardData);
        outState.putSerializable(BaseIntentConstant.BUNDLE_TEMP_LIST, this.parentList);
    }
}
