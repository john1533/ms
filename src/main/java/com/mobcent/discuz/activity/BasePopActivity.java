package com.mobcent.discuz.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.fragment.BaseFragment;

public abstract class BasePopActivity extends BaseFragmentActivity implements BaseIntentConstant {
    private FrameLayout containerBox;
    private Fragment contentFragment;
    protected Toolbar mToolbar;

    protected abstract Fragment initContentFragment();

    protected String getLayoutName() {
        return "base_pop_activity";
    }

    protected void initViews() {
        this.containerBox = (FrameLayout) findViewByName("container_layout");
        this.contentFragment = initContentFragment();
        getFragmentHelper().addFragment(this.containerBox.getId(), this.contentFragment);

        mToolbar = (Toolbar) findViewByName("my_toolbar");
        mToolbar.setTitle("");
//        mToolbar.setNavigationIcon(resource.getDrawableId("mc_forum_top_bar_button1"));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasePopActivity.this.finish();
            }
        });
        if(getSupportActionBar() != null){
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void initActions() {
    }

    public void onBackPressed() {
        if (this.contentFragment == null || !(this.contentFragment instanceof BaseFragment) || !((BaseFragment) this.contentFragment).isChildInteruptBackPress()) {
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected Fragment getContentFragment() {
        return this.contentFragment;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getContentFragment() != null) {
            getContentFragment().onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isSlideFullScreen() {
        if (this.contentFragment == null || !(this.contentFragment instanceof SlideDelegate)) {
            return true;
        }
        return ((SlideDelegate) this.contentFragment).isSlideFullScreen();
    }
}
