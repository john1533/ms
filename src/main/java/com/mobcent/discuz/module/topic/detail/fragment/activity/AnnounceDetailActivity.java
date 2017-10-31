package com.mobcent.discuz.module.topic.detail.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.module.topic.detail.fragment.AnnounceDetailFragment;

public class AnnounceDetailActivity extends PopComponentActivity {
    private long announceId;

    protected void initDatas() {
        super.initDatas();
        this.announceId = getIntent().getLongExtra(IntentConstant.INTENT_ANNO_ID, 0);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new AnnounceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(IntentConstant.INTENT_ANNO_ID, this.announceId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
