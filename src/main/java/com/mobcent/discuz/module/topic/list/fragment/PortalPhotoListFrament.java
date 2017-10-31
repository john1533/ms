package com.mobcent.discuz.module.topic.list.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.module.topic.list.fragment.adapter.BasePhotoListFragmentAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.PhotoList1FragementAdapter;
import com.mobcent.discuz.module.topic.list.fragment.adapter.PhotoList2FragementAdapter;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.ArrayList;
import java.util.List;

public class PortalPhotoListFrament extends BasePhotoListFragment {
    private BasePhotoListFragmentAdapter adapter;
    private String moudleId;
    private String style;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        if (this.moduleModel != null) {
            this.moudleId = this.moduleModel.getNewsModuleId() + "";
            this.style = this.moduleModel.getStyle();
        }
        if (this.adapter != null) {
            return;
        }
        if (this.style.equals(StyleConstant.STYLE_IMAGE2)) {
            this.adapter = new PhotoList2FragementAdapter(this.activity, this.moduleModel);
        } else {
            this.adapter = new PhotoList1FragementAdapter(this.activity, this.moduleModel);
        }
    }

    protected void initActions(View rootView) {
        super.initActions(rootView);
        if (!this.fallList.isEmpty()) {
            this.pullRefreshView.onDrawWaterFall(this.fallList, 0);
            if (this.scrollY != 0) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        ((ScrollView) PortalPhotoListFrament.this.pullRefreshView.getRefreshableView()).scrollTo(0, PortalPhotoListFrament.this.scrollY);
                    }
                }, 200);
            }
        }
    }

    protected BaseResultModel<List<TopicModel>> getTopicList() {
        BaseResultModel<List<TopicModel>> baseResultModel = this.postsService.getPortalList(this.page, this.pageSize, this.moudleId, this.isLocal, 0);
        if (this.style.equals(StyleConstant.STYLE_IMAGE2)) {
            List list = (List) baseResultModel.getData();
            List<TopicModel> topicList = new ArrayList();
            if (!MCListUtils.isEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    TopicModel model = (TopicModel) list.get(i);
                    model.setRatio(1.56f);
                    topicList.add(model);
                }
            }
            baseResultModel.setData(topicList);
        }
        return baseResultModel;
    }

    protected View createView(BaseFallWallModel tag, TopicModel model) {
        return this.adapter.createView(tag, model);
    }

    protected void setData(BaseFallWallModel flowTag, View view, boolean isVisibile, TopicModel model) {
        this.adapter.setData(flowTag, view, isVisibile, model);
    }
}
