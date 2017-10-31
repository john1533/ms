package com.mobcent.discuz.module.msg.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.msg.fragment.SessionList1Fragment;

public class SessionListActivity extends PopComponentActivity implements FinalConstant {
    protected Fragment initContentFragment() {
        Fragment fragment = new SessionList1Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.componentModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
