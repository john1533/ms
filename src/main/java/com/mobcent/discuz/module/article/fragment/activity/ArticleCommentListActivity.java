package com.mobcent.discuz.module.article.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.article.fragment.ArticleCommentListFragment;

public class ArticleCommentListActivity extends PopComponentActivity {
    private long aid;
    private int allowComment;

    protected void initDatas() {
        super.initDatas();
        this.aid = getIntent().getLongExtra("aid", 0);
        this.allowComment = getIntent().getIntExtra("allowComment", 0);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new ArticleCommentListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("aid", this.aid);
        bundle.putInt("allowComment", this.allowComment);
        fragment.setArguments(bundle);
        return fragment;
    }
}
