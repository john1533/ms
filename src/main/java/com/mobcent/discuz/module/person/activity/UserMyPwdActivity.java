package com.mobcent.discuz.module.person.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.person.activity.fragment.UserMyPwdFragment;

public class UserMyPwdActivity extends PopComponentActivity {
    protected Fragment initContentFragment() {
        return new UserMyPwdFragment();
    }
}
