package com.mobcent.discuz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.WebViewFragment;

public class WebViewFragmentActivity extends PopComponentActivity implements IntentConstant {
    private int adPosition;
    private String title;
    private long topicId;
    private String type;
    private String webUrl;

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        this.webUrl = intent.getStringExtra("webViewUrl");
        this.title = intent.getStringExtra(BaseIntentConstant.BUNDLE_WEB_TITLE);
        this.type = intent.getStringExtra(BaseIntentConstant.BUNDLE_WEB_TYPE);
        this.adPosition = intent.getIntExtra("adPosition", 0);
        this.topicId = intent.getLongExtra("topicId", 0);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("webViewUrl", this.webUrl);
        bundle.putString(BaseIntentConstant.BUNDLE_WEB_TITLE, this.title);
        bundle.putString(BaseIntentConstant.BUNDLE_WEB_TYPE, this.type);
        bundle.putInt("adPosition", this.adPosition);
        bundle.putLong("topicId", this.topicId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public boolean isSlideAble() {
        return false;
    }
}
