package com.mobcent.discuz.module.person.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.person.activity.fragment.UserMyInfoFragment;

public class UserMyInfoActivity extends PopComponentActivity {
    protected ConfigComponentModel componentModel;
    private Fragment fragment;

    protected Fragment initContentFragment() {
        this.fragment = new UserMyInfoFragment();
        return this.fragment;
    }
}
