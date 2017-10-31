package com.mobcent.discuz.module.person.activity.fragment;

import android.view.View;
import android.widget.TextView;
import com.mobcent.discuz.base.fragment.BaseFragment;

public class UserInfoFragment extends BaseFragment {
    protected String getRootLayoutName() {
        return "base_error_fragment";
    }

    protected void initViews(View rootView) {
        ((TextView) findViewByName(rootView, "error_text")).setText("user info center");
    }

    protected void initActions(View rootView) {
    }

    protected void componentDealTopbar() {
        dealTopBar(createTopSettingModel());
        registerTopListener(null);
    }
}
