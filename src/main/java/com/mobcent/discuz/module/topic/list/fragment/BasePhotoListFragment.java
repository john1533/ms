package com.mobcent.discuz.module.topic.list.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.helper.DZAdPositionHelper;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshBase.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFallNew;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFallNew.OnLoadItemListener;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BasePhotoListFragment extends BaseFragment implements FinalConstant, PostsConstant {
    private AdView adView = null;
    private GetDataTask dataTask;
    protected ArrayList<TopicModel> fallList;
    protected boolean isLocal = false;
    protected boolean isRefresh = true;
    protected int page = 1;
    protected int pageSize = 30;
    protected PostsService postsService;
    protected PullToRefreshWaterFallNew pullRefreshView;
    protected int scrollY = 0;

    private class GetDataTask extends AsyncTask<Void, Void, BaseResultModel<List<TopicModel>>> {
        private GetDataTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (BasePhotoListFragment.this.adView != null) {
                BasePhotoListFragment.this.adView.showAd(DZAdPositionHelper.getImgListTopPosition(BasePhotoListFragment.this.activity));
            }
        }

        protected BaseResultModel<List<TopicModel>> doInBackground(Void... params) {
            return BasePhotoListFragment.this.getTopicList();
        }

        protected void onPostExecute(BaseResultModel<List<TopicModel>> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(BasePhotoListFragment.this.activity, result);
            if (result.getData() != null) {
                if (BasePhotoListFragment.this.isRefresh) {
                    BasePhotoListFragment.this.pullRefreshView.onRefreshComplete();
                    BasePhotoListFragment.this.fallList.clear();
                }
                BasePhotoListFragment.this.fallList.addAll((Collection) result.getData());
                if (BasePhotoListFragment.this.isRefresh) {
                    BasePhotoListFragment.this.pullRefreshView.onDrawWaterFall((List) result.getData(), 0);
                } else {
                    BasePhotoListFragment.this.pullRefreshView.onDrawWaterFall((List) result.getData(), 1);
                }
                if (result.getHasNext() == 1) {
                    BasePhotoListFragment.this.pullRefreshView.onBottomRefreshComplete(0);
                } else {
                    BasePhotoListFragment.this.pullRefreshView.onBottomRefreshComplete(3);
                }
            }
        }
    }

    protected abstract View createView(BaseFallWallModel baseFallWallModel, TopicModel topicModel);

    protected abstract BaseResultModel<List<TopicModel>> getTopicList();

    protected abstract void setData(BaseFallWallModel baseFallWallModel, View view, boolean z, TopicModel topicModel);

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.postsService = new PostsServiceImpl(this.activity);
        this.fallList = new ArrayList();
    }

    protected String getRootLayoutName() {
        return "portal_list_photo_frament";
    }

    protected void initSaveInstanceState(Bundle savedInstanceState) {
        super.initSaveInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            this.fallList = (ArrayList) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_TEMP_LIST);
            this.scrollY = savedInstanceState.getInt(BaseIntentConstant.BUNDLE_TEMP_PARAM);
        }
        if (this.fallList == null) {
            this.fallList = new ArrayList();
        }
    }

    protected void initViews(View rootView) {
        this.pullRefreshView = (PullToRefreshWaterFallNew) findViewByName(rootView, "pull_refresh_view");
        this.pullRefreshView.setColumnCount(2);
        this.pullRefreshView.setColumnSpace(2);
        if (this.moduleModel != null && StyleConstant.STYLE_IMAGE2.equals(this.moduleModel.getStyle())) {
            this.pullRefreshView.setColumnSpace(4);
            this.pullRefreshView.setHasBorder(false);
        }
        this.adView = new AdView(this.activity, null);
        this.adView.setImgAdWidth(MCPhoneUtil.getDisplayWidth(this.activity));
        this.pullRefreshView.initView(this.activity, this.adView);
        this.adView.showAd(DZAdPositionHelper.getImgListTopPosition(this.activity));
        this.pullRefreshView.onBottomRefreshComplete(3);
    }

    protected void initActions(View rootView) {
        this.pullRefreshView.setOnLoadItemListener(new OnLoadItemListener() {
            public View createItemView(BaseFallWallModel tag) {
                return BasePhotoListFragment.this.createView(tag, (TopicModel) BasePhotoListFragment.this.fallList.get(tag.getPosition()));
            }

            public void onItemClick(BaseFallWallModel flowTag, View view) {
                TopicModel topicModel = (TopicModel) BasePhotoListFragment.this.fallList.get(flowTag.getPosition());
                Intent intent = new Intent(BasePhotoListFragment.this.activity.getApplicationContext(), TopicDetailActivity.class);
                intent.putExtra("boardId", topicModel.getBoardId());
                intent.putExtra("boardName", topicModel.getBoardName());
                intent.putExtra("topicId", topicModel.getTopicId());
                intent.putExtra("style", BasePhotoListFragment.this.moduleModel.getStyle());
                BasePhotoListFragment.this.startActivity(intent);
            }

            public void setItemData(BaseFallWallModel flowTag, View view, boolean isVisibile) {
                BasePhotoListFragment.this.setData(flowTag, view, isVisibile, (TopicModel) BasePhotoListFragment.this.fallList.get(flowTag.getPosition()));
            }
        });
        this.pullRefreshView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                BasePhotoListFragment.this.isLocal = false;
                BasePhotoListFragment.this.page = 1;
                BasePhotoListFragment.this.isRefresh = true;
                BasePhotoListFragment.this.loadDate();
            }
        });
        this.pullRefreshView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                BasePhotoListFragment basePhotoListFragment = BasePhotoListFragment.this;
                basePhotoListFragment.page++;
                BasePhotoListFragment.this.isRefresh = false;
                BasePhotoListFragment.this.loadDate();
            }
        });
    }

    private void loadDate() {
        if (this.dataTask != null) {
            this.dataTask.cancel(true);
        }
        this.dataTask = new GetDataTask();
        this.dataTask.execute(new Void[0]);
    }

    protected void firstCreate() {
        super.firstCreate();
        this.isLocal = true;
        this.pullRefreshView.onRefresh();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BaseIntentConstant.BUNDLE_TEMP_LIST, this.fallList);
        if (this.pullRefreshView != null) {
            outState.putInt(BaseIntentConstant.BUNDLE_TEMP_PARAM, ((ScrollView) this.pullRefreshView.getRefreshableView()).getScrollY());
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.pullRefreshView.getRefreshableView() != null) {
            this.scrollY = ((ScrollView) this.pullRefreshView.getRefreshableView()).getScrollY();
        }
        this.pullRefreshView.onDestroyView();
        if (this.adView != null) {
            this.adView.free();
        }
    }
}
