package com.mobcent.discuz.module.topic.list.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.topic.list.fragment.TopicListFragment;

public class UserTopicListActivity extends PopComponentActivity {
    private String TopicType;
    private long userId;

    protected void initDatas() {
        super.initDatas();
        this.TopicType = getIntent().getStringExtra(PostsConstant.TOPIC_TYPE);
        this.userId = getIntent().getLongExtra("userId", 0);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new TopicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PostsConstant.TOPIC_TYPE, this.TopicType);
        bundle.putLong("userId", this.userId);
        bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.componentModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
