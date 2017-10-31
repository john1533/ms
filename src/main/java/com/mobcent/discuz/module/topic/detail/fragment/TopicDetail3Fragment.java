package com.mobcent.discuz.module.topic.detail.fragment;

import android.os.Bundle;
import android.view.View;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.TopicDetail3FragmentAdapter;
import java.util.List;

public class TopicDetail3Fragment extends TopicDetail3BaseFragment {
    protected String getRootLayoutName() {
        return "topic_detail3_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "TopicDetail3Fragment";
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        if (this.adapter == null) {
            this.adapter = new TopicDetail3FragmentAdapter(this.activity, this.detailList, this.TAG);
        }
        this.listView.setAdapter(this.adapter);
    }

    protected void nofityAdapter() {
        this.adapter.notifyDataSetChanged();
    }

    protected void expandAllView() {
        int groupCount = this.adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            this.listView.expandGroup(i);
        }
    }

    protected void setAtUserList(List<UserInfoModel> list) {
        this.adapter.setAtUserList(this.atUserList);
    }
}
