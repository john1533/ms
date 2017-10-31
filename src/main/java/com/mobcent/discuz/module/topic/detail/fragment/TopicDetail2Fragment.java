package com.mobcent.discuz.module.topic.detail.fragment;

import android.os.Bundle;
import android.view.View;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.TopicDetail2FragmentAdapter;

public class TopicDetail2Fragment extends TopicDetailBaseFragment {
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "TopicDetail2Fragment";
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        if (this.adapter == null) {
            this.adapter = new TopicDetail2FragmentAdapter(this.activity, this.detailList, this.TAG);
        }
        this.listView.setAdapter(this.adapter);
    }
}
