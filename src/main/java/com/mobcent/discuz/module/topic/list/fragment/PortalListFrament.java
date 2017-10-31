package com.mobcent.discuz.module.topic.list.fragment;

import android.os.Bundle;
import android.view.View;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.topic.list.fragment.adapter.BaseTopicListFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListBigPicFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListCardFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListFlatFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListNeteaseNewsFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListTiebaFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.TopicListWechatFragmentAdapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import java.util.List;

public class PortalListFrament extends BaseTopicListFragment {
    private BaseTopicListFragmentAdapter adapter;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.isShowSilderView = true;
    }

    protected String getRootLayoutName() {
        return "topic_list_fragment";
    }

    protected void initViews(View rootView) {
        this.pullToRefreshListView = (PullToRefreshListView) findViewByName(rootView, "mc_forum_lv");
        if (this.adapter == null) {
            if (this.style.equals("card")) {
                this.pullToRefreshListView.setDividerHeight(0);
                this.adapter = new TopicListCardFragmentAdapter(this.activity, this.topicList, this.moduleModel, false);
            } else if (this.style.equals(StyleConstant.STYLE_TIE_BA)) {
                this.imageMore = 1;
                this.adapter = new TopicListTiebaFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else if (this.style.equals(StyleConstant.STYLE_WE_CHAT)) {
                this.imageMore = 1;
                this.isShowSilderView = false;
                this.adapter = new TopicListWechatFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else if (this.style.equals(StyleConstant.STYLE_IMG_BIG)) {
                this.isShowSilderView = false;
                this.adapter = new TopicListBigPicFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else if (this.style.equals(StyleConstant.STYLE_NET_EASENEWS)) {
                this.pullToRefreshListView.setDividerHeight(0);
                this.adapter = new TopicListNeteaseNewsFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            } else {
                this.adapter = new TopicListFlatFragmentAdapter(this.activity, this.topicList, this.moduleModel);
            }
        }
        updateAdView(this.inflater);
        updateHeader(true);
        this.pullToRefreshListView.setAdapter(this.adapter);
    }

    protected void initActions(View rootView) {
        super.initActions(rootView);
        onRefreshListView();
    }

    public BaseResultModel<List<TopicModel>> getLoadDate() {
        return this.postsService.getPortalList(this.page, this.pageSize, this.moudleId, this.isLocal, this.imageMore);
    }

    protected void notifyList(BaseResultModel<List<TopicModel>> baseResultModel) {
        if (this.isRefresh) {
            this.adapter.notifyDataSetChanged();
        }
    }
}
