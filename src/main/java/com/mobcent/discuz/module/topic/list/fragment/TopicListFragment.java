package com.mobcent.discuz.module.topic.list.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.view.MCHeaderSearchView;
import com.mobcent.discuz.activity.view.MCHeaderSearchView.OnSearchClickListener;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BaseResultTopicModel;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.TopModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.discuz.module.topic.list.fragment.adapter.BaseTopicListFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListBigPicFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListCardFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListFlatFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListNeteaseNewsFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListTiebaFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListWechatFragmentAdapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicListFragment extends BaseTopicListFragment implements FinalConstant, PostsConstant {
    private static final String FORUMINFO = "foruminfo";
    private static final String TOPLIST = "toplist";
    private BaseTopicListFragmentAdapter adapter;
    private BoardChild boardChild;
    private FrameLayout forumInfoLayout;
    private View forumInfoLayoutChild;
    private String keywordStr;
    private MCLocationUtil locationUtil = null;
    private Handler mHandler = new Handler();
    private List<TopModel> topTopicList;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.isShowSilderView = false;
    }

    protected String getRootLayoutName() {
        return "topic_list_fragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initViews(View rootView) {
        this.pullToRefreshListView = (PullToRefreshListView) findViewByName(rootView, "mc_forum_lv");
        if (this.topicType.equals(PostsConstant.TOPIC_TYPE_SURROUND)) {
            if (new SettingSharePreference(this.activity.getApplicationContext()).isLocationOpen(this.sharedPreferencesDB.getUserId())) {
                initLocationUtil(this.activity.getApplicationContext());
            } else {
                MCToastUtils.toastByResName(this.activity.getApplicationContext(), "mc_forum_location_setting_flag");
            }
        } else if (this.topicType.equals("search")) {
            this.pullToRefreshListView.removePullToRefreshLayout();
            this.pullToRefreshListView.onBottomRefreshComplete(3, MCResource.getInstance(this.activity.getApplicationContext()).getString("mc_forum_search_keywords"));
            initSearchView();
        } else {
            updateAdView(this.inflater);
        }
        if (this.adapter == null) {
            if (this.style.equals(StyleConstant.STYLE_CARD)) {//帖子类表展示类型
                this.pullToRefreshListView.setDividerHeight(0);
                this.adapter = new TopicListCardFragmentAdapter(this.activity, this.topicList, this.moduleModel, true);
            } else if (this.style.equals(StyleConstant.STYLE_TIE_BA)) {
                this.topOrder = 1;
                this.imageMore = 1;
                this.adapter = new TopicListTiebaFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else if (this.style.equals(StyleConstant.STYLE_WE_CHAT)) {
                this.imageMore = 1;
                this.adapter = new TopicListWechatFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else if (this.style.equals(StyleConstant.STYLE_IMG_BIG)) {
                this.adapter = new TopicListBigPicFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else if (this.style.equals(StyleConstant.STYLE_NET_EASENEWS)) {
                this.pullToRefreshListView.setDividerHeight(0);
                this.adapter = new TopicListNeteaseNewsFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else {
                this.adapter = new TopicListFlatFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            }
        }
        initForumInfoView(null);
        this.pullToRefreshListView.setAdapter(this.adapter);
    }

    private void initSearchView() {
        MCHeaderSearchView mcHeaderSearchView = new MCHeaderSearchView(this.activity, 13, "mc_forum_bg1");
        this.pullToRefreshListView.addHeaderView(mcHeaderSearchView, null, false);
        mcHeaderSearchView.setClickListener(new OnSearchClickListener() {
            public void OnClickListener(String keywordStrs) {
                TopicListFragment.this.pullToRefreshListView.setVisibility(0);
                TopicListFragment.this.keywordStr = keywordStrs;
                TopicListFragment.this.pullToRefreshListView.onRefresh(true);
            }
        });
    }

    private void initForumInfoView(BaseResultModel<List<TopicModel>> baseResultModel) {
        if (this.style.equals(StyleConstant.STYLE_TIE_BA)) {
            if (this.forumInfoLayout == null) {
                this.forumInfoLayout = new FrameLayout(this.activity);
                this.forumInfoLayoutChild = this.inflater.inflate(this.resource.getLayoutId("topic_list_foruminfo"), null);
                this.forumInfoLayout.addView(this.forumInfoLayoutChild);
            }
            this.forumInfoLayoutChild.setVisibility(8);
            if (baseResultModel == null) {
                this.pullToRefreshListView.addHeaderView(this.forumInfoLayout, null, false);
                if (this.boardChild == null) {
                    return;
                }
            }
            if (this.boardChild == null && baseResultModel != null) {
                this.boardChild = ((BaseResultTopicModel) baseResultModel).getForumInfo();
            }
            if ((this.topTopicList == null || (this.topTopicList != null && this.topTopicList.size() == 0)) && baseResultModel != null) {
                this.topTopicList = ((BaseResultTopicModel) baseResultModel).getTopTopicList();
            }
            if (this.boardChild == null || (this.boardChild != null && MCStringUtil.isEmpty(this.boardChild.getBoardName()))) {
                this.pullToRefreshListView.removeHeaderView(this.forumInfoLayout);
                this.forumInfoLayout = null;
                return;
            }
            this.forumInfoLayoutChild.setVisibility(0);
            View marginView = this.forumInfoLayout.findViewById(this.resource.getViewId("topic_list_foruminfo_marginview"));
            if (this.boardChild.isHaveAnnoModel()) {
                marginView.setVisibility(0);
            } else {
                marginView.setVisibility(8);
            }
            ImageLoader.getInstance().displayImage(this.boardChild.getBoardImg(), (ImageView) this.forumInfoLayout.findViewById(this.resource.getViewId("topic_list_foruminfo_boardicon")));
            ((TextView) this.forumInfoLayout.findViewById(this.resource.getViewId("topic_list_foruminfo_boardname"))).setText(this.boardChild.getBoardName());
            ((TextView) this.forumInfoLayout.findViewById(this.resource.getViewId("topic_list_foruminfo_boarddesc"))).setText(this.boardChild.getDescription());
            int i = 1;
            int sizeI = this.topTopicList.size() > 3 ? 3 : this.topTopicList.size();
            while (i <= sizeI) {
                TopModel topModel = (TopModel) this.topTopicList.get(i - 1);
                final long topicId = topModel.getId();
                RelativeLayout topbox = (RelativeLayout) this.forumInfoLayout.findViewById(this.resource.getViewId("topic_list_foruminfo_topbox" + i));
                topbox.setVisibility(0);
                ((TextView) this.forumInfoLayout.findViewById(this.resource.getViewId("topic_list_foruminfo_text" + i))).setText(topModel.getTitle());
                topbox.setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent(TopicListFragment.this.activity, TopicDetailActivity.class);
                        intent.putExtra("topicId", topicId);
                        intent.putExtra("style", TopicListFragment.this.moduleModel.getSubDetailViewStyle());
                        TopicListFragment.this.activity.startActivity(intent);
                    }
                });
                i++;
            }
        }
    }

    protected void initActions(View rootView) {
        super.initActions(rootView);
        if (!this.topicType.equals("search")) {
            onRefreshListView();
        }
    }

    private void initLocationUtil(Context context) {
        this.locationUtil = MCLocationUtil.getInstance(context);
        this.locationUtil.requestLocation(new LocationDelegate() {
            public void onReceiveLocation(final BDLocation locationModel) {
                TopicListFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (locationModel != null) {
                            TopicListFragment.this.sharedPreferencesDB.saveLocation(locationModel);
                        }
                    }
                });
            }
        });
    }

    protected BaseResultModel<List<TopicModel>> getLoadDate() {
        if (this.topicType.equals(PostsConstant.TOPIC_TYPE_SURROUND)) {
            if (!new SettingSharePreference(this.activity.getApplicationContext()).isLocationOpen(this.sharedPreferencesDB.getUserId())) {
                return null;
            }
            BDLocation location = SharedPreferencesDB.getInstance(this.activity.getApplicationContext()).getLocation();
            if (location != null) {
                return this.postsService.getSurroundtopicList(this.page, this.pageSize, Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()), "topic");
            }
            return null;
        } else if (this.topicType.equals("search")) {
            return this.postsService.getSearchTopicList(this.page, this.pageSize, this.keywordStr, this.searchId);
        } else {
            if (this.topicType.equals("topic") || this.topicType.equals("reply") || this.topicType.equals("favorite")) {
                return this.postsService.getUserTopicList(this.userId, this.page, this.pageSize, this.topicType);
            }
            return this.postsService.getTopicListByLocal(this.boardId, this.page, this.pageSize, this.orderby, this.filterType, this.filterId, this.isLocal, this.imageMore, this.topOrder);
        }
    }

    protected void notifyList(BaseResultModel<List<TopicModel>> baseResultModel) {
        if (this.isRefresh) {
            if (baseResultModel instanceof BaseResultTopicModel) {
                initForumInfoView(baseResultModel);
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void initSaveInstanceState(Bundle savedInstanceState) {
        super.initSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.topTopicList = (ArrayList) savedInstanceState.getSerializable(TOPLIST);
            this.boardChild = (BoardChild) savedInstanceState.getSerializable(FORUMINFO);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putSerializable(TOPLIST, (Serializable) this.topTopicList);
            outState.putSerializable(FORUMINFO, this.boardChild);
        }
    }
}
