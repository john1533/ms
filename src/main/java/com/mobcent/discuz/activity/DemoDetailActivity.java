package com.mobcent.discuz.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.base.fragment.DemoDetailFragment;

public class DemoDetailActivity extends PopComponentActivity {
    protected void initDatas() {
        super.initDatas();
    }

    protected Fragment initContentFragment() {
        return new DemoDetailFragment();
    }
}
