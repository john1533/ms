package com.mobcent.discuz.module.topic.list.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.android.constant.BaseErrorCodeConstant;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.delegate.SubChangeListener;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.widget.MCHeaderPagerHelper;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.listener.PausePullListOnScrollListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.open.SocialConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressLint({"NewApi"})
public abstract class BaseTopicListFragment extends BaseFragment implements FinalConstant, PostsConstant, SubChangeListener {
    private final String TOPIC_LIST = "topicList";
    private AdView adView;
    protected long boardId;
    protected int filterId;
    protected String filterType;
    protected LinearLayout headerRoot;
    protected int imageMore = 0;
    protected boolean isLocal = true;
    protected boolean isRefresh = true;
    protected boolean isShowSilderView = false;
    private LoadDateTask loadDateTask;
    protected String moudleId;
    protected String orderby = "all";
    protected int page = 1;
    protected int pageSize = 20;
    private int positionInParent;
    protected PostsService postsService;
    protected PullToRefreshListView pullToRefreshListView;
    protected int searchId;
    protected View sliderView;
    protected MCHeaderPagerHelper sliderViewHelper = null;
    protected String style;
    protected List<TopicModel> tempList;
    protected int topOrder;
    protected List<TopicModel> topicList;
    protected String topicType = PostsConstant.TOPIC_TYPE;
    protected long userId;
    private ViewPager viewPager;

    private class LoadDateTask extends AsyncTask<Void, Void, BaseResultModel<List<TopicModel>>> {
        private LoadDateTask() {
        }

        protected BaseResultModel<List<TopicModel>> doInBackground(Void... arg0) {
            return BaseTopicListFragment.this.getLoadDate();
        }

        protected void onPostExecute(BaseResultModel<List<TopicModel>> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(BaseTopicListFragment.this.activity, result);
            if (BaseTopicListFragment.this.isRefresh && (BaseTopicListFragment.this.topicType.equals("search") || BaseTopicListFragment.this.topicType.equals("filter"))) {
                BaseTopicListFragment.this.topicList.clear();
            }
            if (result != null) {
                if (result.getRs() == 0 && !MCStringUtil.isEmpty(result.getErrorCode()) && result.getErrorCode().equals(BaseErrorCodeConstant.ERROR_SEARCH_NET_TIME_OUT)) {
                    MCToastUtils.toastByResName(BaseTopicListFragment.this.activity, "mc_forum_search_net_time_out", 0);
                }
                if (BaseTopicListFragment.this.isRefresh) {
                    BaseTopicListFragment.this.pullToRefreshListView.onRefreshComplete();
                }
                if (!MCListUtils.isEmpty((List) result.getData())) {
                    AdManager.getInstance().recyclAdByTag(BaseTopicListFragment.this.TAG);
                    if (BaseTopicListFragment.this.isRefresh) {
                        BaseTopicListFragment.this.topicList.clear();
                        BaseTopicListFragment.this.topicList.addAll((Collection) result.getData());
                        BaseTopicListFragment.this.updateHeader(false);
                    } else {
                        BaseTopicListFragment.this.topicList.addAll(BaseTopicListFragment.this.getTopicList((List) result.getData()));
                    }
                    BaseTopicListFragment.this.notifyList(result);
                    BaseTopicListFragment.this.searchId = result.getSearchId();
                    if (result.getHasNext() == 1) {
                        BaseTopicListFragment.this.pullToRefreshListView.onBottomRefreshComplete(0);
                    } else {
                        BaseTopicListFragment.this.pullToRefreshListView.onBottomRefreshComplete(3);
                    }
                    BaseTopicListFragment.this.isLocal = false;
                    BaseTopicListFragment.this.tempList = (List) result.getData();
                } else if (!BaseTopicListFragment.this.isLocal) {
                    BaseTopicListFragment.this.pullToRefreshListView.onBottomRefreshComplete(3);
                }
            }
            if (BaseTopicListFragment.this.topicList.size() == 0 && !BaseTopicListFragment.this.isLocal) {
                BaseTopicListFragment.this.pullToRefreshListView.onBottomRefreshComplete(2);
            }
        }
    }

    protected abstract BaseResultModel<List<TopicModel>> getLoadDate();

    protected abstract void notifyList(BaseResultModel<List<TopicModel>> baseResultModel);

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        if (getArguments() != null) {
            this.positionInParent = getArguments().getInt("position");
        }
        this.topicList = new ArrayList();
        this.tempList = new ArrayList();
        this.topicType = getArguments().getString(PostsConstant.TOPIC_TYPE);
        if (MCStringUtil.isEmpty(this.topicType)) {
            this.topicType = PostsConstant.TOPIC_TYPE;
        }
        this.userId = getArguments().getLong("userId", 0);
        if (this.moduleModel != null) {
            this.orderby = this.moduleModel.getOrderby();
            this.boardId = this.moduleModel.getForumId();
            this.moudleId = this.moduleModel.getNewsModuleId() + "";
            this.style = this.moduleModel.getStyle();
            this.filterId = this.moduleModel.getFilterId();
            this.filterType = this.moduleModel.getFilter();
            this.topOrder = this.moduleModel.getOrder();
        } else {
            this.moduleModel = createComponentModel();
            this.style = "flat";
        }
        this.postsService = new PostsServiceImpl(this.activity);
        this.sliderViewHelper = new MCHeaderPagerHelper(getActivity());
    }

    private ConfigComponentModel createComponentModel() {
        ConfigComponentModel model = new ConfigComponentModel();
        model.setForumId(this.boardId);
        model.setType(ConfigConstant.COMPONENT_TOPICLIST_SIMPLE);
        model.setStyle("flat");
        model.setSubDetailViewStyle("flat");
        model.setListImagePosition(2);
        model.setListTitleLength(20);
        model.setListSummaryLength(40);
        return model;
    }

    protected void initSaveInstanceState(Bundle savedInstanceState) {
        super.initSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.topicList = (ArrayList) savedInstanceState.getSerializable("topicList");
        }
        if (this.topicList == null) {
            this.topicList = new ArrayList();
        }
    }

    protected void initActions(View rootView) {
        this.pullToRefreshListView.setScrollListener(new PausePullListOnScrollListener(ImageLoader.getInstance(), false, false));
        this.pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                BaseTopicListFragment.this.refresh();
            }
        });
        this.pullToRefreshListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                BaseTopicListFragment.this.loadmore();
            }
        });
    }

    protected void onRefreshListView() {
        if (MCListUtils.isEmpty(this.topicList)) {
            this.pullToRefreshListView.onRefresh(false);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    BaseTopicListFragment.this.isLocal = false;
                    BaseTopicListFragment.this.pullToRefreshListView.onRefresh(true);
                }
            }, 500);
        }
    }

    protected void updateAdView(LayoutInflater inflater) {
        View adLayout = inflater.inflate(this.resource.getLayoutId("ad_view"), null);
        this.adView = (AdView) adLayout.findViewById(this.resource.getViewId("adView"));
        if (this.moduleModel.getStyle().equals("flat")) {
            this.adView.setImgAdWidth(MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()));
        } else if (this.moduleModel.getStyle().equals("card")) {
            this.adView.setImgAdWidth(MCPhoneUtil.getDisplayWidth(this.activity.getApplicationContext()) - MCPhoneUtil.getRawSize(this.activity.getApplicationContext(), 1, 16.0f));
        }
        this.adView.showAd(Integer.valueOf(this.resource.getString("mc_forum_topic_list_top_position")).intValue());
        this.pullToRefreshListView.addHeaderView(adLayout, null, false);
    }

    protected void updateHeader(boolean isInit) {
        if (this.isShowSilderView) {
            if (isInit) {
                this.headerRoot = new LinearLayout(this.activity);
                this.headerRoot.setOrientation(1);
                this.sliderView = this.sliderViewHelper.initView(getActivity(), this.moduleModel.getStyle());
                this.sliderViewHelper.attachListView(this.pullToRefreshListView);
                this.headerRoot.addView(this.sliderView);
                try {
                    this.pullToRefreshListView.addHeaderView(this.headerRoot, null, false);
                } catch (Exception e) {
                }
            }
            if (this.sliderView != null) {
                if (MCListUtils.isEmpty(this.topicList) || MCListUtils.isEmpty(((TopicModel) this.topicList.get(0)).getPortalRecommList())) {
                    this.sliderView.setVisibility(8);
                } else {
                    this.sliderViewHelper.setData(((TopicModel) this.topicList.get(0)).getPortalRecommList(), this.moduleModel.getStyle());
                    this.sliderView.setVisibility(0);
                }
                startHeaderScroll();
            }
        }
    }

    private void refresh() {
        this.page = 1;
        if (this.pullToRefreshListView.isHand()) {
            this.isLocal = false;
        }
        this.isRefresh = true;
        this.loadDateTask = new LoadDateTask();
        this.loadDateTask.execute(new Void[0]);
    }

    private void loadmore() {
        this.page++;
        this.isRefresh = false;
        this.isLocal = false;
        this.loadDateTask = new LoadDateTask();
        this.loadDateTask.execute(new Void[0]);
    }

    public void onRefreshListView(PopupModel popupModel) {
        if (popupModel != null) {
            this.topicType = "filter";
            this.filterId = popupModel.getId();
            if (popupModel.getType() == -1 && this.filterId > 0) {
                this.filterType = SocialConstants.PARAM_TYPE_ID;
            } else if (popupModel.getType() != -2 || this.filterId <= 0) {
                this.topicType = PostsConstant.TOPIC_TYPE;
            } else {
                this.filterType = PostsConstant.CLASSIFICATIONTOP_ID;
            }
        }
        refresh();
    }

    private List<TopicModel> getTopicList(List<TopicModel> data) {
        if (MCListUtils.isEmpty((List) data)) {
            return new ArrayList();
        }
        try {
            for (int i = data.size() - 1; i >= 0; i--) {
                for (int j = 0; j < this.tempList.size(); j++) {
                    if (((TopicModel) data.get(i)).getTopicId() == ((TopicModel) this.tempList.get(j)).getTopicId()) {
                        data.remove(i);
                    }
                }
            }
            return data;
        } catch (Exception e) {
            return data;
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        if (this.topicType.equals("reply")) {
            if (this.userId == this.sharedPreferencesDB.getUserId()) {
                topSettingModel.title = this.resource.getString("mc_forum_user_reply_posts");
            } else {
                topSettingModel.title = this.resource.getString("mc_forum_he_reply");
            }
        }
        if (this.topicType.equals("favorite")) {
            topSettingModel.title = this.resource.getString("mc_forum_my_favorite");
        }
        if (this.topicType.equals("topic")) {
            if (this.userId == this.sharedPreferencesDB.getUserId()) {
                topSettingModel.title = this.resource.getString("mc_forum_user_topic");
            } else {
                topSettingModel.title = this.resource.getString("mc_forum_he_publish");
            }
        }
        if (this.topicType.equals("search")) {
            topSettingModel.title = this.resource.getString("mc_forum_search_title");
        }
        if (this.topicType.equals(PostsConstant.TOPIC_TYPE_SURROUND)) {
            topSettingModel.title = this.resource.getString("mc_forum_surround_topic");
        }
        if (!(this.moduleModel == null || MCStringUtil.isEmpty(this.moduleModel.getTitle()))) {
            topSettingModel.title = this.moduleModel.getTitle();
        }
        dealTopBar(topSettingModel);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putSerializable("topicList", (Serializable) this.topicList);
        }
    }

    public void onResume() {
        super.onResume();
        startHeaderScroll();
    }

    public void onPause() {
        super.onPause();
        stopHeaderScroll();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.adView != null) {
            this.adView.free();
        }
        if (this.loadDateTask != null) {
            this.loadDateTask.cancel(true);
        }
    }

    public void onSelected(boolean isSelected) {
        if (isSelected) {
            startHeaderScroll();
        } else {
            stopHeaderScroll();
        }
    }

    public void startHeaderScroll() {
        if (this.sliderViewHelper != null && this.sliderView != null && getViewPager() != null && getViewPager().getCurrentItem() == this.positionInParent) {
            this.sliderViewHelper.startAutoScroll();
        }
    }

    public void stopHeaderScroll() {
        if (this.sliderViewHelper != null) {
            this.sliderViewHelper.stopAutoScroll();
        }
    }
}
