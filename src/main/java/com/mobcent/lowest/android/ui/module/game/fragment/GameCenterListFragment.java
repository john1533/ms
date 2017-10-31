package com.mobcent.lowest.android.ui.module.game.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import com.mobcent.lowest.android.ui.module.game.cache.GameDataCache;
import com.mobcent.lowest.android.ui.module.game.delegate.FallWallDelegate;
import com.mobcent.lowest.android.ui.module.game.fragment.adapter.GameCenterListFragmentAdapter;
import com.mobcent.lowest.android.ui.module.game.fragment.adapter.GameCenterListFragmentAdapter.GameListListener;
import com.mobcent.lowest.android.ui.module.game.fragment.adapter.holder.GameCenterListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnScrollListener;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.model.WebGameModel;
import com.mobcent.lowest.module.game.service.GameService;
import com.mobcent.lowest.module.game.service.impl.GameServiceImpl;
import java.util.ArrayList;
import java.util.List;

public class GameCenterListFragment extends BaseGameFragment implements FallWallDelegate {
    private String TAG = "GameCenterListFragment";
    private String WHICH_TAG;
    private int currentState;
    private int currentVisiablePosition = 0;
    private GameCenterListFragmentAdapter gameCenterListFragmentAdapter;
    private GameService gameService;
    private boolean hasNext = true;
    private PullToRefreshListView mPullRefreshListView;
    private List<WebGameModel> webGameList;

    private class LoadDataAsyncTask extends AsyncTask<Integer, Void, List<WebGameModel>> {
        private int state;

        private LoadDataAsyncTask() {
        }

        protected List<WebGameModel> doInBackground(Integer... params) {
            this.state = params[0].intValue();
            if (GameCenterListFragment.this.WHICH_TAG.equals(GameApiConstant.RECOMMEND_TAG)) {
                return GameCenterListFragment.this.gameService.getRecommendGameList(20, this.state);
            }
            if (GameCenterListFragment.this.WHICH_TAG.equals(GameApiConstant.LATEST_TAG)) {
                return GameCenterListFragment.this.gameService.getLatestGameList(20, this.state);
            }
            return GameCenterListFragment.this.gameService.getMyGameList(20, this.state);
        }

        protected void onPostExecute(List<WebGameModel> result) {
            switch (this.state) {
                case 1:
                    GameCenterListFragment.this.onRefreshpostExecute(result);
                    return;
                case 2:
                    GameCenterListFragment.this.onRefreshpostExecute(result);
                    return;
                case 3:
                    GameCenterListFragment.this.onMorePostExecute(result);
                    return;
                default:
                    return;
            }
        }
    }

    class GameCenterListListener implements GameListListener {
        GameCenterListListener() {
        }

        public void deleteGameFromLocal(long gameId) {
            GameCenterListFragment.this.gameService.deleteWebGame(gameId);
        }
    }

    protected void initData() {
        this.WHICH_TAG = this.bundle.getString("tag_name");
        MCLogUtil.e(this.TAG, "WHICH_TAG = " + this.WHICH_TAG);
        this.gameDataCache = GameDataCache.getInstance();
        this.webGameList = GameDataCache.getInstance().getWebGameList(this.WHICH_TAG);
        if (this.webGameList == null) {
            this.webGameList = new ArrayList();
            this.gameDataCache.setWebGameList(this.WHICH_TAG, this.webGameList);
        }
        this.gameService = new GameServiceImpl(this.context, this.WHICH_TAG);
    }

    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.mcResource.getLayoutId("mc_game_center_fragment"), container, false);
        this.mPullRefreshListView = (PullToRefreshListView) view.findViewById(this.mcResource.getViewId("mc_game_center_list_content"));
        this.gameCenterListFragmentAdapter = new GameCenterListFragmentAdapter(this.activity, this.webGameList, this.WHICH_TAG, new GameCenterListListener());
        this.mPullRefreshListView.setAdapter(this.gameCenterListFragmentAdapter);
        this.mPullRefreshListView.setScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                switch (scrollState) {
                    case 0:
                        GameCenterListFragment.this.onImageLoaded();
                        return;
                    case 1:
                        GameCenterListFragment.this.gameCenterListFragmentAdapter.setScrolling(false);
                        return;
                    case 2:
                        GameCenterListFragment.this.gameCenterListFragmentAdapter.setScrolling(true);
                        return;
                    default:
                        return;
                }
            }

            public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
            }

            public void onScrollDirection(boolean isUp, int distance) {
            }
        });
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
                switch (GameCenterListFragment.this.currentState) {
                    case 1:
                        GameCenterListFragment.this.getDataTask(1);
                        return;
                    case 2:
                        GameCenterListFragment.this.getDataTask(2);
                        return;
                    case 3:
                        GameCenterListFragment.this.getDataTask(3);
                        return;
                    default:
                        return;
                }
            }
        });
        this.mPullRefreshListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                GameCenterListFragment.this.getDataTask(3);
            }
        });
        if (this.webGameList.size() <= 0) {
            this.currentState = 1;
            this.mPullRefreshListView.onRefresh(false);
        }
    }

    public void getDataTask(int state) {
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask();
        addAsyncTask(loadDataAsyncTask);
        loadDataAsyncTask.execute(new Integer[]{Integer.valueOf(state)});
    }

    public void onRefreshpostExecute(List<WebGameModel> result) {
        this.mPullRefreshListView.onRefreshComplete();
        if (result == null) {
            this.mPullRefreshListView.onBottomRefreshComplete(0);
        } else if (result.size() == 0) {
            this.webGameList.clear();
            this.mPullRefreshListView.onBottomRefreshComplete(2);
        } else if (MCStringUtil.isEmpty(((WebGameModel) result.get(0)).getErrorCode())) {
            this.hasNext = ((WebGameModel) result.get(0)).isHasNext();
            this.webGameList.clear();
            this.webGameList.addAll(result);
            this.mPullRefreshListView.setSelection(0);
            result.clear();
            if (this.hasNext) {
                this.mPullRefreshListView.onBottomRefreshComplete(0);
            } else if (this.gameService.getLocalList(this.WHICH_TAG)) {
                this.mPullRefreshListView.onBottomRefreshComplete(3, MCStringBundleUtil.resolveString(this.mcResource.getStringId("mc_forum_cache_load_more"), this.gameService.getRefreshData(this.WHICH_TAG), this.activity));
            } else {
                this.mPullRefreshListView.onBottomRefreshComplete(3);
            }
            this.gameCenterListFragmentAdapter.setScrolling(false);
            this.gameCenterListFragmentAdapter.notifyDataSetChanged();
        } else {
            showMessage(this.mcResource.getString("mc_game_comment_get_data_fail"));
            this.mPullRefreshListView.onBottomRefreshComplete(0);
        }
    }

    public void onMorePostExecute(List<WebGameModel> result) {
        if (result == null) {
            this.mPullRefreshListView.onBottomRefreshComplete(0);
        } else if (result.size() == 0) {
            this.webGameList.clear();
            this.mPullRefreshListView.onBottomRefreshComplete(2);
        } else if (MCStringUtil.isEmpty(((WebGameModel) result.get(0)).getErrorCode())) {
            this.hasNext = ((WebGameModel) result.get(0)).isHasNext();
            this.webGameList.addAll(result);
            result.clear();
            if (this.hasNext) {
                this.mPullRefreshListView.onBottomRefreshComplete(0);
            } else if (this.gameService.getLocalList(this.WHICH_TAG)) {
                this.mPullRefreshListView.onBottomRefreshComplete(3, MCStringBundleUtil.resolveString(this.mcResource.getStringId("mc_forum_cache_load_more"), this.gameService.getRefreshData(this.WHICH_TAG), this.activity));
            } else {
                this.mPullRefreshListView.onBottomRefreshComplete(3);
            }
        } else {
            showMessage(this.mcResource.getString("mc_game_comment_get_data_fail"));
            this.mPullRefreshListView.onBottomRefreshComplete(0);
        }
    }

    public void onImageLoaded() {
        if (this.mPullRefreshListView != null) {
            this.gameCenterListFragmentAdapter.setScrolling(false);
            int count = this.mPullRefreshListView.getChildCount();
            for (int i = 0; i <= count; i++) {
                View itemView = this.mPullRefreshListView.getChildAt(i);
                if (!(itemView == null || itemView.getTag() == null)) {
                    ImageView iv = ((GameCenterListFragmentAdapterHolder) itemView.getTag()).getThumbnail();
                    if (iv.getDrawable() == null) {
                        this.gameCenterListFragmentAdapter.loadImageByUrl(iv);
                    }
                }
            }
        }
    }

    public void loadImageFallWall(int position) {
        if (this.mPullRefreshListView != null) {
            int count = this.mPullRefreshListView.getCount();
            for (int i = 0; i < count; i++) {
                View view = this.mPullRefreshListView.getChildAt(i);
                if (view != null && (view.getTag() instanceof GameCenterListFragmentAdapterHolder)) {
                    this.gameCenterListFragmentAdapter.loadImageByUrl(((GameCenterListFragmentAdapterHolder) view.getTag()).getThumbnail());
                }
            }
        }
    }

    public void recycleImageFallWall(int position) {
        if (this.mPullRefreshListView != null) {
            int count = this.mPullRefreshListView.getCount();
            for (int i = 0; i < count; i++) {
                View view = this.mPullRefreshListView.getChildAt(i);
                if (view != null && (view.getTag() instanceof GameCenterListFragmentAdapterHolder)) {
                    GameCenterListFragmentAdapterHolder holder = (GameCenterListFragmentAdapterHolder) view.getTag();
                    holder.getThumbnail().setImageBitmap(null);
                    holder.getThumbnail().setImageDrawable(null);
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        loadImageFallWall(0);
        this.mPullRefreshListView.setSelection(this.currentVisiablePosition);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.gameDataCache.setWebGameList(this.WHICH_TAG, this.webGameList);
        this.gameDataCache.savePostion(this.WHICH_TAG, Integer.valueOf(this.currentVisiablePosition));
    }

    public void onStop() {
        super.onStop();
        this.currentVisiablePosition = this.mPullRefreshListView.getFirstVisiblePosition();
    }

    public void onDestroy() {
        super.onDestroy();
        recycleImageFallWall(0);
    }

    public void loadDataByNet() {
        if (this.mPullRefreshListView != null) {
            this.currentState = 2;
            this.mPullRefreshListView.onRefresh();
        }
    }
}
