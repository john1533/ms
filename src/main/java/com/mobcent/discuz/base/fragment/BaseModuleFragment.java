package com.mobcent.discuz.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModuleFragment extends BaseFragment implements SlideDelegate {
    public String TAG = "BaseModuleFragment";
    protected ConfigModuleModel moduleModel;
    private OnClickListener topClickListener = new OnClickListener() {
        public void onClick(View v) {
            TopBtnModel topBtnModel = (TopBtnModel) v.getTag();
            if (topBtnModel.tag != null && (topBtnModel.tag instanceof ConfigComponentModel)) {
                ActivityDispatchHelper.dispatchActivity(BaseModuleFragment.this.activity, (ConfigComponentModel)topBtnModel.tag);
            }
        }
    };

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        setHasOptionsMenu(true);
        this.moduleModel = (ConfigModuleModel) getBundle().getSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL);
        if (savedInstanceState != null) {
            ConfigModuleModel saveModuleModel = (ConfigModuleModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL);
            if (saveModuleModel != null) {
                this.moduleModel = saveModuleModel;
            }
        }
    }

    protected void initViews(View rootView) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (!BaseModuleFragment.this.moduleModel.isComponent()) {
                    BaseModuleFragment.this.dealTopBar();
                }
            }
        }, 100);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL, this.moduleModel);
    }

    public void onResume() {
        super.onResume();
    }

    public void dealTopBar() {
        if (this.moduleModel != null) {
            int i;
            ConfigComponentModel moduleModel;
            TopBtnModel topBtnModel;
            TopSettingModel topSettingModel = createTopSettingModel();
            topSettingModel.title = this.moduleModel.getTitle();
            List<TopBtnModel> leftList = new ArrayList();
            List<TopBtnModel> rightList = new ArrayList();
            List<ConfigComponentModel> leftModels = this.moduleModel.getLeftTopbarList();
            if (!(leftModels == null || leftModels.isEmpty())) {
                for (i = 0; i < leftModels.size(); i++) {
                    moduleModel = (ConfigComponentModel) leftModels.get(i);
                    topBtnModel = new TopBtnModel();
                    topBtnModel.tag = moduleModel;
                    topBtnModel.icon = moduleModel.getIcon();
                    leftList.add(topBtnModel);
                }
            }
            List<ConfigComponentModel> rightModels = this.moduleModel.getRightTopbarList();
            if (!(rightModels == null || rightModels.isEmpty())) {
                for (i = 0; i < rightModels.size(); i++) {
                    moduleModel = (ConfigComponentModel) rightModels.get(i);
                    topBtnModel = new TopBtnModel();
                    topBtnModel.tag = moduleModel;
                    topBtnModel.icon = moduleModel.getIcon();
                    rightList.add(topBtnModel);
                }
            }
            topSettingModel.leftModels = leftList;
            topSettingModel.rightModels = rightList;
            dealTopBar(topSettingModel);
            registerTopListener(this.topClickListener);
        }
    }

    public boolean isSlideFullScreen() {
        return true;
    }
}
