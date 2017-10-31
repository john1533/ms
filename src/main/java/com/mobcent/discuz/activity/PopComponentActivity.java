package com.mobcent.discuz.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.dispatch.FragmentDispatchHelper;

public class PopComponentActivity extends BasePopActivity implements BaseIntentConstant {
    protected ConfigComponentModel componentModel;

    protected void initDatas() {
        super.initDatas();
        this.componentModel = (ConfigComponentModel) getIntent().getSerializableExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
    }

    protected Fragment initContentFragment() {
        return FragmentDispatchHelper.disPatchFragment(this.componentModel);
    }
}
