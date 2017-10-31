package com.mobcent.lowest.android.ui.module.plaza.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import com.mobcent.lowest.android.ui.module.ad.delegate.AdDelegate;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaSearchActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.PlazaSearchKeyModel;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaAdPosition;
import com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.PlazaSearchListFragmentAdapter;
import com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.holder.SearchListFragmentHolder;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnScrollListener;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.plaza.model.SearchModel;
import com.mobcent.lowest.module.plaza.service.SearchService;
import com.mobcent.lowest.module.plaza.service.impl.SearchServiceImpl;
import java.util.ArrayList;
import java.util.List;

public class PlazaSearchListFragment extends BaseFragment {
    protected AdView adView1 = null;
    protected AdView adView2 = null;
    private ImageView backTopBtn;
    private Handler handler;
    private boolean isImgRight = true;
    private boolean isSearchBtnClick = false;
    private View line1;
    private View line2;
    private PlazaSearchListFragmentAdapter listAdapter;
    private PullToRefreshListView mPullRefreshListView;
    private int page = 1;
    private PlazaSearchKeyModel searchKeyModel;
    private List<SearchModel> searchList;
    OnScrollListener searchScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == 0) {
                PlazaSearchListFragment.this.loadVisibleItem();
            } else if (scrollState == 1) {
                PlazaSearchListFragment.this.listAdapter.setBusy(false);
            } else if (scrollState == 2) {
                PlazaSearchListFragment.this.listAdapter.setBusy(true);
            }
        }

        public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int totalItemCount) {
        }

        public void onScrollDirection(boolean isUp, int distance) {
        }
    };
    private SearchService searchService;

    class GetData extends AsyncTask<Void, Void, List<SearchModel>> {
        GetData() {
        }

        protected void onPreExecute() {
            PlazaSearchListFragment.this.page = 1;
            if (PlazaSearchListFragment.this.adView1 != null) {
                PlazaSearchListFragment.this.adView1.free();
                PlazaSearchListFragment.this.adView1.showAd(PlazaAdPosition.SEARCH_LIST_TOP);
            }
            if (PlazaSearchListFragment.this.adView2 != null) {
                PlazaSearchListFragment.this.adView2.free();
                PlazaSearchListFragment.this.adView2.showAd(PlazaAdPosition.SEARCH_LIST_TOP);
            }
        }

        protected List<SearchModel> doInBackground(Void... params) {
            if (PlazaSearchListFragment.this.searchKeyModel == null) {
                return null;
            }
            return PlazaSearchListFragment.this.searchService.getSearchList(PlazaSearchListFragment.this.searchKeyModel.getForumId(), PlazaSearchListFragment.this.searchKeyModel.getForumKey(), PlazaSearchListFragment.this.searchKeyModel.getUserId(), PlazaSearchListFragment.this.searchKeyModel.getBaikeType(), PlazaSearchListFragment.this.searchKeyModel.getSearchMode(), PlazaSearchListFragment.this.searchKeyModel.getKeyWord(), PlazaSearchListFragment.this.page, 20);
        }

        protected void onPostExecute(List<SearchModel> result) {
            PlazaSearchListFragment.this.mPullRefreshListView.onRefreshComplete();
            if (result != null) {
                if (result.isEmpty()) {
                    PlazaSearchListFragment.this.searchList.clear();
                    PlazaSearchListFragment.this.notifyListView();
                    PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(3, PlazaSearchListFragment.this.getString(PlazaSearchListFragment.this.mcResource.getStringId("mc_plaza_no_data")));
                } else if (MCStringUtil.isEmpty(((SearchModel) result.get(0)).getErrorCode())) {
                    PlazaSearchListFragment.this.searchList.clear();
                    PlazaSearchListFragment.this.searchList.addAll(result);
                    PlazaSearchListFragment.this.notifyListView();
                    if (((SearchModel) result.get(0)).isHasNext()) {
                        PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(0);
                    } else {
                        PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(3);
                    }
                } else {
                    PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(3, PlazaSearchListFragment.this.getString(PlazaSearchListFragment.this.mcResource.getStringId("mc_plaza_load_error")));
                }
                result.clear();
            } else {
                PlazaSearchListFragment.this.searchList.clear();
                PlazaSearchListFragment.this.notifyListView();
                PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(3, PlazaSearchListFragment.this.getString(PlazaSearchListFragment.this.mcResource.getStringId("mc_plaza_search_no_search_data")));
            }
            PlazaSearchListFragment.this.isSearchBtnClick = false;
        }
    }

    class GetMore extends AsyncTask<Void, Void, List<SearchModel>> {
        GetMore() {
        }

        protected void onPreExecute() {
            PlazaSearchListFragment.this.page = PlazaSearchListFragment.this.page + 1;
        }

        protected List<SearchModel> doInBackground(Void... params) {
            if (PlazaSearchListFragment.this.searchKeyModel == null) {
                return null;
            }
            return PlazaSearchListFragment.this.searchService.getSearchList(PlazaSearchListFragment.this.searchKeyModel.getForumId(), PlazaSearchListFragment.this.searchKeyModel.getForumKey(), PlazaSearchListFragment.this.searchKeyModel.getUserId(), PlazaSearchListFragment.this.searchKeyModel.getBaikeType(), PlazaSearchListFragment.this.searchKeyModel.getSearchMode(), PlazaSearchListFragment.this.searchKeyModel.getKeyWord(), PlazaSearchListFragment.this.page, 20);
        }

        protected void onPostExecute(List<SearchModel> result) {
            if (result != null) {
                if (result.isEmpty()) {
                    PlazaSearchListFragment.this.page = PlazaSearchListFragment.this.page - 1;
                    PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(2);
                } else if (MCStringUtil.isEmpty(((SearchModel) result.get(0)).getErrorCode())) {
                    PlazaSearchListFragment.this.searchList.addAll(result);
                    if (((SearchModel) result.get(0)).isHasNext()) {
                        PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(0);
                    } else {
                        PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(3);
                    }
                } else {
                    PlazaSearchListFragment.this.page = PlazaSearchListFragment.this.page - 1;
                    PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(0);
                }
                result.clear();
                return;
            }
            PlazaSearchListFragment.this.page = PlazaSearchListFragment.this.page - 1;
            PlazaSearchListFragment.this.mPullRefreshListView.onBottomRefreshComplete(0);
        }
    }

    public PlazaSearchListFragment(Handler handler) {
        this.handler = handler;
    }

    protected void initData() {
        this.searchService = new SearchServiceImpl(getActivity());
        this.searchList = new ArrayList();
        this.TAG = "PlazaSearchActivity";
    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.mcResource.getLayoutId("mc_plaza_search_list_fragment"), null);
        this.backTopBtn = (ImageView) view.findViewById(this.mcResource.getViewId("lv_backtotop"));
        this.listAdapter = new PlazaSearchListFragmentAdapter(getActivity(), this.TAG, this.searchList, this.handler);
        this.mPullRefreshListView = (PullToRefreshListView) view.findViewById(this.mcResource.getViewId("pull_refresh_list"));
        this.mPullRefreshListView.setBackToTopView(this.backTopBtn);
        this.adView1 = new AdView(getActivity(), null);
        this.adView1.setSearchViewStyle(1);
        this.adView1.setAdDelegate(new AdDelegate() {
            public void onAdShow(boolean isShow, AdContainerModel adContainerModel) {
                if (isShow && PlazaSearchListFragment.this.line1 != null) {
                    PlazaSearchListFragment.this.line1.setVisibility(0);
                }
            }
        });
        this.mPullRefreshListView.addHeaderView(this.adView1, null, false);
        this.line1 = new View(this.activity);
        this.line1.setBackgroundResource(this.mcResource.getDrawableId("mc_forum_wire"));
        this.line1.setLayoutParams(new LayoutParams(-1, 1));
        this.mPullRefreshListView.addHeaderView(this.line1, null, false);
        this.adView2 = new AdView(getActivity(), null);
        this.adView2.setSearchViewStyle(1);
        this.adView1.setAdDelegate(new AdDelegate() {
            public void onAdShow(boolean isShow, AdContainerModel adContainerModel) {
                if (isShow && PlazaSearchListFragment.this.line2 != null) {
                    PlazaSearchListFragment.this.line2.setVisibility(0);
                }
            }
        });
        this.mPullRefreshListView.addHeaderView(this.adView2, null, false);
        this.line2 = new View(this.activity);
        this.line2.setBackgroundResource(this.mcResource.getDrawableId("mc_forum_wire"));
        this.line2.setLayoutParams(new LayoutParams(-1, 1));
        this.mPullRefreshListView.addHeaderView(this.line2, null, false);
        this.mPullRefreshListView.setAdapter(this.listAdapter);
        this.mPullRefreshListView.setScrollListener(this.searchScrollListener);
        this.mPullRefreshListView.onBottomRefreshComplete(3, getString(this.mcResource.getStringId("mc_plaza_search_no_search_data")));
        return view;
    }

    protected void initWidgetActions() {
        this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (PlazaSearchListFragment.this.isSearchBtnClick) {
                    PlazaSearchListFragment.this.addAsyncTask(new GetData().execute(new Void[0]));
                } else {
                    ((PlazaSearchActivity) PlazaSearchListFragment.this.getActivity()).onSearchBtnClick();
                }
            }
        });
        this.mPullRefreshListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                PlazaSearchListFragment.this.addAsyncTask(new GetMore().execute(new Void[0]));
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.adView1 != null) {
            this.adView1.free();
        }
        if (this.adView2 != null) {
            this.adView2.free();
        }
        if (this.TAG != null) {
            AdManager.getInstance().recyclAdByTag(this.TAG);
        }
    }

    public void requestData(PlazaSearchKeyModel searchKeyModel) {
        this.searchKeyModel = searchKeyModel;
        this.isSearchBtnClick = true;
        if (searchKeyModel == null || searchKeyModel.getKeyWord() == null || "".equals(searchKeyModel.getKeyWord())) {
            this.isSearchBtnClick = false;
            this.mPullRefreshListView.onRefreshComplete();
            this.mPullRefreshListView.onBottomRefreshComplete(3, getString(this.mcResource.getStringId("mc_plaza_search_no_search_data")));
            return;
        }
        this.mPullRefreshListView.onRefresh();
    }

    public void loadVisibleItem() {
        if (this.mPullRefreshListView != null) {
            this.listAdapter.setBusy(false);
            int count = this.mPullRefreshListView.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = this.mPullRefreshListView.getChildAt(i);
                if (view.getTag() != null && (view.getTag() instanceof SearchListFragmentHolder)) {
                    ImageView thumbImg;
                    SearchListFragmentHolder holder = (SearchListFragmentHolder) view.getTag();
                    if (this.isImgRight) {
                        thumbImg = holder.getThumbImgRight();
                    } else {
                        thumbImg = holder.getThumbImg();
                    }
                    if (thumbImg.getDrawable() == null && thumbImg.getTag() != null) {
                        this.listAdapter.loadImageByUrl(thumbImg, thumbImg.getTag().toString());
                    }
                }
            }
        }
    }

    private void notifyListView() {
        this.listAdapter.notifyDataSetChanged();
    }
}
