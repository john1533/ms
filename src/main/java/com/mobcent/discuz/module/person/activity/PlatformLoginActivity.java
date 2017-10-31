package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.module.person.activity.fragment.PlatformLoginFragment;

public class PlatformLoginActivity extends PopComponentActivity implements FinalConstant {
    private long platformId;

    protected void initDatas() {
        super.initDatas();
        this.platformId = getIntent().getLongExtra("platformId", 0);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new PlatformLoginFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("platformId", this.platformId);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 100 || resultCode == 200) {
            finish();
        } else {
            finish();
        }
    }
}
