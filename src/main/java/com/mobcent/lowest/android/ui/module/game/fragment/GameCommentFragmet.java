package com.mobcent.lowest.android.ui.module.game.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import com.mobcent.lowest.android.ui.module.game.cache.GameDataCache;
import com.mobcent.lowest.android.ui.module.game.constant.GameConstance;
import com.mobcent.lowest.android.ui.module.game.fragment.adapter.GameCommentListFragmentAdapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.model.GameCommentModel;
import com.mobcent.lowest.module.game.model.WebGameModel;
import com.mobcent.lowest.module.game.service.GameService;
import com.mobcent.lowest.module.game.service.impl.GameServiceImpl;
import java.util.List;

public class GameCommentFragmet extends BaseGameFragment {
    private GameService GameService;
    public String TAG = "GameCommentFragmet";
    private List<GameCommentModel> gameCommentList;
    private GameCommentListFragmentAdapter gameCommentListFragmentAdapter;
    private PullToRefreshListView mPullRefreshListView;
    private int page = 1;
    private int pageSize = 10;
    private WebGameModel webGameModel;

    private class PostsByDescAsyncTask extends AsyncTask<Void, Void, List<GameCommentModel>> {
        private int state;

        public PostsByDescAsyncTask(int state) {
            this.state = state;
        }

        protected List<GameCommentModel> doInBackground(Void... params) {
            if (this.state == 2) {
                GameCommentFragmet.this.page = 1;
            }
            return GameCommentFragmet.this.GameService.getGameCommentList(0, GameCommentFragmet.this.webGameModel.getGameId(), GameCommentFragmet.this.page, GameCommentFragmet.this.pageSize);
        }

        protected void onPostExecute(List<GameCommentModel> gameCommentList) {
            GameCommentFragmet.this.onGetDataExecute(gameCommentList, this.state);
        }
    }

    protected void initData() {
        this.webGameModel = (WebGameModel) this.bundle.get(GameConstance.INTENT_TO_DETAIL_FRAGMENT_MODEL);
        this.GameService = new GameServiceImpl(this.activity);
        this.gameCommentList = GameDataCache.getInstance().getGameCommentList();
    }

    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.mcResource.getLayoutId("mc_game_detail_comment"), container, false);
        this.mPullRefreshListView = (PullToRefreshListView) view.findViewById(this.mcResource.getViewId("mc_game_detail_comment_content"));
        this.gameCommentListFragmentAdapter = new GameCommentListFragmentAdapter(this.activity, this.gameCommentList);
        this.mPullRefreshListView.setAdapter(this.gameCommentListFragmentAdapter);
        return view;
    }

    protected void initWidgetActions() {
        this.mPullRefreshListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                GameCommentFragmet.this.clearAsyncTask();
                GameCommentFragmet.this.addAsyncTask(new PostsByDescAsyncTask(2).execute(new Void[0]));
            }
        });
        this.mPullRefreshListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                GameCommentFragmet.this.addAsyncTask(new PostsByDescAsyncTask(3).execute(new Void[0]));
            }
        });
        this.mPullRefreshListView.onRefresh();
    }

    public void loadDataByNet() {
    }

    public void onGetDataExecute(List<GameCommentModel> commentList, int state) {
        this.mPullRefreshListView.onRefreshComplete();
        List<GameCommentModel> innerCommentList = commentList;
        if (innerCommentList == null) {
            return;
        }
        if (innerCommentList.isEmpty()) {
            if (state == 2) {
                this.gameCommentList.clear();
                this.gameCommentListFragmentAdapter.notifyDataSetChanged();
            }
            this.mPullRefreshListView.onBottomRefreshComplete(2);
        } else if (MCStringUtil.isEmpty(((GameCommentModel) innerCommentList.get(0)).getErrorCode())) {
            int totalNum = ((GameCommentModel) innerCommentList.get(0)).getTotalNum();
            if (state == 2) {
                this.gameCommentList.clear();
                this.gameCommentList.addAll(innerCommentList);
                innerCommentList.clear();
                innerCommentList = null;
            }
            if (state == 3) {
                this.gameCommentList.addAll(innerCommentList);
                innerCommentList.clear();
            }
            if (this.gameCommentList.size() >= totalNum - 1) {
                this.mPullRefreshListView.onBottomRefreshComplete(3);
            } else {
                this.mPullRefreshListView.onBottomRefreshComplete(0);
            }
            this.page++;
            this.gameCommentListFragmentAdapter.notifyDataSetChanged();
        } else {
            if (state == 2) {
                this.gameCommentList.clear();
                this.gameCommentListFragmentAdapter.notifyDataSetChanged();
            } else {
                this.page--;
            }
            showMessage(this.mcResource.getString("mc_game_comment_get_data_fail"));
            this.mPullRefreshListView.onBottomRefreshComplete(0);
        }
    }
}
