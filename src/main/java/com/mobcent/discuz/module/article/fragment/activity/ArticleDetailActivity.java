package com.mobcent.discuz.module.article.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.module.article.fragment.ArticleDetailFragment;

public class ArticleDetailActivity extends PopComponentActivity {
    private long aid;
    private TopicDraftModel topicDraftModel;

    protected void initDatas() {
        super.initDatas();
        this.aid = getIntent().getLongExtra("aid", 0);
        this.topicDraftModel = (TopicDraftModel) getIntent().getSerializableExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new ArticleDetailFragment();
        Bundle bundle = new Bundle();
        if (this.componentModel == null || this.componentModel.getArticleId() == 0) {
            bundle.putLong("aid", this.aid);
        } else {
            bundle.putLong("aid", this.componentModel.getArticleId());
        }
        bundle.putSerializable(IntentConstant.INTENT_TOPIC_DRAFMODEL, this.topicDraftModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
