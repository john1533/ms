package com.mobcent.discuz.activity;

import android.support.v4.app.Fragment;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.dispatch.FragmentDispatchHelper;
import com.mobcent.lowest.base.utils.MCListUtils;

public class PopModuleActivity extends BasePopActivity implements BaseIntentConstant {
    private ConfigModuleModel moduleModel;

    protected void initDatas() {
        super.initDatas();
        this.moduleModel = (ConfigModuleModel) getIntent().getSerializableExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL);
    }

    protected Fragment initContentFragment() {
        return FragmentDispatchHelper.disPatchFragment(this.moduleModel);
    }

    public boolean isSlideFullScreen() {
        if (this.moduleModel == null || !ConfigConstant.MODULE_TYPE_SUBNAV.equals(this.moduleModel.getType())) {
            return true;
        }
        return false;
    }

    public boolean isSlideAble() {
        if (isSlideFullScreen() && ConfigConstant.MODULE_TYPE_FULL.equals(this.moduleModel.getType()) && !MCListUtils.isEmpty(this.moduleModel.getComponentList()) && ((ConfigComponentModel) this.moduleModel.getComponentList().get(0)).getType().equals(ConfigConstant.COMPONENT_WEBAPP)) {
            return false;
        }
        return super.isSlideAble();
    }
}
