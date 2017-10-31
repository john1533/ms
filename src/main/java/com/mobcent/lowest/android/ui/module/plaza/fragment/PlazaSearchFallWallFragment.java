package com.mobcent.lowest.android.ui.module.plaza.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaSearchActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.PlazaSearchKeyModel;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaAdPosition;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFall;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFall.OnLoadItemListener;
import com.mobcent.lowest.base.model.FlowTag;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.plaza.config.PlazaConfig;
import com.mobcent.lowest.module.plaza.model.SearchModel;
import com.mobcent.lowest.module.plaza.service.SearchService;
import com.mobcent.lowest.module.plaza.service.impl.SearchServiceImpl;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlazaSearchFallWallFragment extends BaseFragment {
    public String TAG = "SearchFallWallFragment";
    protected AdView adView = null;
    private PullToRefreshWaterFall fallwallBox;
    private Handler handler;
    private Map<String, ImageView> imageMap;
    private boolean isSearchBtnClick = false;
    private OnLoadItemListener loadItemListener = new OnLoadItemListener() {
        public void loadLayout(LinearLayout linearLayout, FlowTag flowTag) {
            if (PlazaSearchFallWallFragment.this.getActivity() != null) {
                ImageView imageView = new ImageView(PlazaSearchFallWallFragment.this.getActivity());
                LayoutParams lp = new LayoutParams(-1, -1);
                imageView.setBackgroundResource(PlazaSearchFallWallFragment.this.mcResource.getDrawableId("mc_forum_list9_li_bg"));
                imageView.setScaleType(ScaleType.CENTER_CROP);
                linearLayout.addView(imageView, lp);
                PlazaSearchFallWallFragment.this.imageMap.put(flowTag.getThumbnailUrl(), imageView);
            }
        }

        public void loadImage(String imageUrl) {
            ImageLoader.getInstance().displayImage(MCAsyncTaskLoaderImage.formatUrl(imageUrl, PlazaConstant.RESOLUTION_200X200), (ImageView) PlazaSearchFallWallFragment.this.imageMap.get(imageUrl));
        }

        public void onItemClick(int currentPosition, FlowTag flowTag) {
            if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                PlazaConfig.getInstance().getPlazaDelegate().onSearchItemClick(PlazaSearchFallWallFragment.this.getActivity(), (SearchModel) PlazaSearchFallWallFragment.this.searchList.get(currentPosition));
            }
        }

        public void recycleImage(String imageUrl) {
            ImageView imageView = (ImageView) PlazaSearchFallWallFragment.this.imageMap.get(imageUrl);
            if (imageView != null) {
                imageView.setImageBitmap(null);
            }
        }
    };
    private int page = 1;
    private RelativeLayout rootView;
    private PlazaSearchKeyModel searchKeyModel;
    private List<SearchModel> searchList;
    private SearchService searchService;

    class GetData extends AsyncTask<Void, Void, List<SearchModel>> {
        GetData() {
        }

        protected void onPreExecute() {
            PlazaSearchFallWallFragment.this.page = 1;
            if (PlazaSearchFallWallFragment.this.adView != null) {
                PlazaSearchFallWallFragment.this.adView.free();
                PlazaSearchFallWallFragment.this.adView.showAd(PlazaAdPosition.SEARCH_FALL_TOP);
            }
        }

        protected List<SearchModel> doInBackground(Void... params) {
            if (PlazaSearchFallWallFragment.this.searchKeyModel == null) {
                return null;
            }
            return PlazaSearchFallWallFragment.this.searchService.getSearchList(PlazaSearchFallWallFragment.this.searchKeyModel.getForumId(), PlazaSearchFallWallFragment.this.searchKeyModel.getForumKey(), PlazaSearchFallWallFragment.this.searchKeyModel.getUserId(), PlazaSearchFallWallFragment.this.searchKeyModel.getBaikeType(), PlazaSearchFallWallFragment.this.searchKeyModel.getSearchMode(), PlazaSearchFallWallFragment.this.searchKeyModel.getKeyWord(), PlazaSearchFallWallFragment.this.page, 20);
        }

        protected void onPostExecute(List<SearchModel> result) {
            PlazaSearchFallWallFragment.this.fallwallBox.onRefreshComplete();
            PlazaSearchFallWallFragment.this.isSearchBtnClick = false;
            if (result != null) {
                if (result.isEmpty()) {
                    PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(3, PlazaSearchFallWallFragment.this.getString(PlazaSearchFallWallFragment.this.mcResource.getStringId("mc_plaza_no_data")));
                    PlazaSearchFallWallFragment.this.clearSearchData();
                } else if (MCStringUtil.isEmpty(((SearchModel) result.get(0)).getErrorCode())) {
                    PlazaSearchFallWallFragment.this.searchList.clear();
                    PlazaSearchFallWallFragment.this.searchList.addAll(result);
                    PlazaSearchFallWallFragment.this.imageMap.clear();
                    PlazaSearchFallWallFragment.this.fallwallBox.onDrawWaterFall(PlazaSearchFallWallFragment.this.galleryModels2FlowTags(result), 0);
                    if (((SearchModel) result.get(0)).isHasNext()) {
                        PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(0);
                    } else {
                        PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(3);
                    }
                } else {
                    PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(3, PlazaSearchFallWallFragment.this.getString(PlazaSearchFallWallFragment.this.mcResource.getStringId("mc_plaza_load_error")));
                    PlazaSearchFallWallFragment.this.clearSearchData();
                }
                result.clear();
                return;
            }
            PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(3, PlazaSearchFallWallFragment.this.getString(PlazaSearchFallWallFragment.this.mcResource.getStringId("mc_plaza_search_no_search_data")));
            PlazaSearchFallWallFragment.this.clearSearchData();
        }
    }

    class GetMore extends AsyncTask<Void, Void, List<SearchModel>> {
        GetMore() {
        }

        protected void onPreExecute() {
            PlazaSearchFallWallFragment.this.page = PlazaSearchFallWallFragment.this.page + 1;
        }

        protected List<SearchModel> doInBackground(Void... params) {
            if (PlazaSearchFallWallFragment.this.searchKeyModel == null) {
                return null;
            }
            return PlazaSearchFallWallFragment.this.searchService.getSearchList(PlazaSearchFallWallFragment.this.searchKeyModel.getForumId(), PlazaSearchFallWallFragment.this.searchKeyModel.getForumKey(), PlazaSearchFallWallFragment.this.searchKeyModel.getUserId(), PlazaSearchFallWallFragment.this.searchKeyModel.getBaikeType(), PlazaSearchFallWallFragment.this.searchKeyModel.getSearchMode(), PlazaSearchFallWallFragment.this.searchKeyModel.getKeyWord(), PlazaSearchFallWallFragment.this.page, 20);
        }

        protected void onPostExecute(List<SearchModel> result) {
            if (result != null) {
                if (result.isEmpty()) {
                    PlazaSearchFallWallFragment.this.page = PlazaSearchFallWallFragment.this.page - 1;
                    PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(2);
                } else if (MCStringUtil.isEmpty(((SearchModel) result.get(0)).getErrorCode())) {
                    PlazaSearchFallWallFragment.this.searchList.addAll(result);
                    PlazaSearchFallWallFragment.this.fallwallBox.onDrawWaterFall(PlazaSearchFallWallFragment.this.galleryModels2FlowTags(result), 1);
                    if (((SearchModel) result.get(0)).isHasNext()) {
                        PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(0);
                    } else {
                        PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(3);
                    }
                } else {
                    PlazaSearchFallWallFragment.this.page = PlazaSearchFallWallFragment.this.page - 1;
                    PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(0);
                }
                result.clear();
                return;
            }
            PlazaSearchFallWallFragment.this.page = PlazaSearchFallWallFragment.this.page - 1;
            PlazaSearchFallWallFragment.this.fallwallBox.onBottomRefreshComplete(0);
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    protected void initData() {
        this.searchService = new SearchServiceImpl(getActivity());
        this.searchList = new ArrayList();
        this.imageMap = new HashMap();
    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = (RelativeLayout) inflater.inflate(this.mcResource.getLayoutId("mc_plaza_search_fallwall_fragment"), null);
        this.fallwallBox = (PullToRefreshWaterFall) this.rootView.findViewById(this.mcResource.getViewId("fallwall_layout"));
        this.adView = new AdView(getActivity(), null);
        this.fallwallBox.initView(getActivity(), this.adView);
        this.fallwallBox.onBottomRefreshComplete(3, getString(this.mcResource.getStringId("mc_plaza_search_no_search_data")));
        return this.rootView;
    }

    protected void initWidgetActions() {
        this.fallwallBox.setOnLoadItemListener(this.loadItemListener);
        this.fallwallBox.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (PlazaSearchFallWallFragment.this.isSearchBtnClick) {
                    PlazaSearchFallWallFragment.this.addAsyncTask(new GetData().execute(new Void[0]));
                } else {
                    ((PlazaSearchActivity) PlazaSearchFallWallFragment.this.getActivity()).onSearchBtnClick();
                }
            }
        });
        this.fallwallBox.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                new GetMore().execute(new Void[0]);
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        for (String key : this.imageMap.keySet()) {
            ImageView img = (ImageView) this.imageMap.get(key);
            if (img != null) {
                img.setImageBitmap(null);
            }
        }
        this.imageMap.clear();
        if (this.adView != null) {
            this.adView.free();
        }
    }

    public void requestData(PlazaSearchKeyModel searchKeyModel) {
        this.searchKeyModel = searchKeyModel;
        this.isSearchBtnClick = true;
        this.searchList.clear();
        this.fallwallBox.onDrawWaterFall(galleryModels2FlowTags(this.searchList), 0);
        if (searchKeyModel == null || searchKeyModel.getKeyWord() == null || "".equals(searchKeyModel.getKeyWord())) {
            this.isSearchBtnClick = false;
            this.fallwallBox.onRefreshComplete();
            this.fallwallBox.onBottomRefreshComplete(3, getString(this.mcResource.getStringId("mc_plaza_search_no_search_data")));
            return;
        }
        this.fallwallBox.onRefresh();
    }

    private void clearSearchData() {
        this.searchList.clear();
        this.fallwallBox.onDrawWaterFall(galleryModels2FlowTags(this.searchList), 0);
    }

    private ArrayList<FlowTag> galleryModels2FlowTags(List<SearchModel> searchModels) {
        ArrayList<FlowTag> flowTags = new ArrayList();
        for (int i = 0; i < searchModels.size(); i++) {
            SearchModel searchModel = (SearchModel) searchModels.get(i);
            FlowTag flowTag = new FlowTag();
            flowTag.setRatio(searchModel.getRatio());
            flowTag.setThumbnailUrl(searchModel.getBaseUrl() + searchModel.getPicPath());
            flowTags.add(flowTag);
        }
        return flowTags;
    }
}
