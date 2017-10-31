package com.mobcent.discuz.module.person.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.person.activity.fragment.UserPhotoSubFragment;

public class UserPhotosSubActivity extends PopComponentActivity {
    private int aid;
    private long userId = 0;

    protected void initDatas() {
        super.initDatas();
        this.userId = getIntent().getExtras().getLong("userId");
        this.aid = getIntent().getExtras().getInt("aid");
    }

    protected Fragment initContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong("userId", this.userId);
        bundle.putInt("aid", this.aid);
        Fragment fragment = new UserPhotoSubFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
