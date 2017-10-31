package com.mobcent.discuz.module.person.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.person.activity.fragment.UserPhotoFragment;

public class UserPhotosActivity extends PopComponentActivity {
    private long userId = 0;

    protected void initDatas() {
        super.initDatas();
        this.userId = getIntent().getExtras().getLong("userId");
    }

    protected Fragment initContentFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong("userId", this.userId);
        Fragment fragment = new UserPhotoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
