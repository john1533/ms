package com.mobcent.discuz.module.about.fragment.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.about.fragment.AboutFragment;

public class AboutActivity extends PopComponentActivity {
    protected Fragment initContentFragment() {
        return new AboutFragment();
    }
}
