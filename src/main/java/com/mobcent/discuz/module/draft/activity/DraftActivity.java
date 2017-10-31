package com.mobcent.discuz.module.draft.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.module.draft.fragment.DraftFragment;

public class DraftActivity extends PopComponentActivity {
    protected Fragment initContentFragment() {
        return new DraftFragment();
    }
}
