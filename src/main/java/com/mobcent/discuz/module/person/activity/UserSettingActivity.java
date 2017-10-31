package com.mobcent.discuz.module.person.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.person.activity.fragment.SettingFragment;

public class UserSettingActivity extends PopComponentActivity {
    protected Fragment initContentFragment() {
        return new SettingFragment();
    }
}
