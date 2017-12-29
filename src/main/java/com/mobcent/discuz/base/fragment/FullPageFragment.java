package com.mobcent.discuz.base.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.mark6.fragment.OpenFragment;
import com.mobcent.discuz.activity.HomeActivity;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.dispatch.FragmentDispatchHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.ArrayList;
import java.util.List;

public class FullPageFragment extends BaseModuleFragment {
    private int ID_WEB_BACK = 1;
    private int ID_WEB_MORE = 2;
    private FrameLayout containerBox;
    private Fragment currentFrgment = null;
    private OnClickListener topClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag() != null && (v.getTag() instanceof TopBtnModel)) {
                TopBtnModel topBtnModel = (TopBtnModel) v.getTag();
                if (FullPageFragment.this.currentFrgment != null && (FullPageFragment.this.currentFrgment instanceof WebViewFragment)) {
                    WebViewFragment webFragment = (WebViewFragment) FullPageFragment.this.currentFrgment;
                    if (topBtnModel.action == FullPageFragment.this.ID_WEB_BACK) {
                        webFragment.isChildInteruptBackPress();
                    } else if (topBtnModel.action == FullPageFragment.this.ID_WEB_MORE && DiscuzApplication._instance.isPayed()) {
                        webFragment.topMoreClick();
                    }
                }
            }
        }
    };

    protected String getRootLayoutName() {
        return "base_full_fragment";
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        this.containerBox = (FrameLayout) findViewByName(rootView, "container_layout");
        if (this.currentFrgment != null) {
            getFragmentHelper().showFragment(this.currentFrgment);
        }
    }

    protected void initActions(View rootView) {
    }

    protected void firstCreate() {
        super.firstCreate();
        if (this.moduleModel != null) {

            if(moduleModel.getId()==-2){//kj
                this.currentFrgment = new OpenFragment();
                getFragmentHelper().addFragment(this.containerBox.getId(), this.currentFrgment);
            }else if(moduleModel.getId()==-3){//static
                this.currentFrgment = new OpenFragment();
                getFragmentHelper().addFragment(this.containerBox.getId(), this.currentFrgment);
            }else if(!MCListUtils.isEmpty(this.moduleModel.getComponentList())){
                List<ConfigComponentModel> componentModels = this.moduleModel.getComponentList();
                if (componentModels != null && !componentModels.isEmpty()) {
                    this.currentFrgment = FragmentDispatchHelper.disPatchFragment((ConfigComponentModel) componentModels.get(0));
                    getFragmentHelper().addFragment(this.containerBox.getId(), this.currentFrgment);
                }
            }

        }
    }

    public void dealTopBar() {
        if (!(this.moduleModel == null || MCListUtils.isEmpty(this.moduleModel.getComponentList()))) {
            ConfigComponentModel componentModel = (ConfigComponentModel) this.moduleModel.getComponentList().get(0);
            if (componentModel != null) {
                TopSettingModel topSettingModel;
                if (ConfigConstant.COMPONENT_USERINFO.equals(componentModel.getType())) {
                    topSettingModel = createTopSettingModel();
                    topSettingModel.isVisibile = false;
                    dealTopBar(topSettingModel);
                    return;
                } else if (ConfigConstant.COMPONENT_WEBAPP.equals(componentModel.getType())) {
                    TopBtnModel topBtnModel;
                    topSettingModel = createTopSettingModel();
                    topSettingModel.title = this.moduleModel.getTitle();
                    List<TopBtnModel> leftModels = new ArrayList();
                    if (this.activity instanceof HomeActivity) {
                        topBtnModel = new TopBtnModel();
                        topBtnModel.icon = "mc_forum_top_bar_button1";
                        topBtnModel.action = this.ID_WEB_BACK;
                        leftModels.add(topBtnModel);
                    }
                    topSettingModel.leftModels = leftModels;
                    List<TopBtnModel> rightModels = new ArrayList();
                    topBtnModel = new TopBtnModel();
                    topBtnModel.icon = "mc_forum_top_bar_button5";
                    topBtnModel.action = this.ID_WEB_MORE;
                    rightModels.add(topBtnModel);
                    topSettingModel.rightModels = rightModels;
                    dealTopBar(topSettingModel);
                    registerTopListener(this.topClickListener);
                    return;
                }
            }
        }
        super.dealTopBar();
    }

    public boolean isSlideFullScreen() {
        if (this.currentFrgment == null || !(this.currentFrgment instanceof SlideDelegate)) {
            return false;
        }
        return ((SlideDelegate) this.currentFrgment).isSlideFullScreen();
    }
}
