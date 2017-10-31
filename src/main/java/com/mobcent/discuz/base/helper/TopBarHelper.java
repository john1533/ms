package com.mobcent.discuz.base.helper;

import android.text.TextUtils;
import android.view.View.OnClickListener;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.widget.CenteredToolbar;

public class TopBarHelper {
//    private TopBarWidget topBox;
//
//    public void dealTopBar(TopSettingModel topSettingModel) {
//        if (this.topBox != null) {
//            this.topBox.resetTopSetting(topSettingModel);
//        }
//    }
//
//    public void registerClickListener(OnClickListener onClickListener) {
//        if (this.topBox != null) {
//            this.topBox.registerClickListener(onClickListener);
//        }
//    }
//
//    public TopBarWidget getTopBox() {
//        return this.topBox;
//    }
//
//    public void setTopBox(TopBarWidget topBox) {
//        this.topBox = topBox;
//    }


    private CenteredToolbar toolbar;


    public void dealTopBar(TopSettingModel topSettingModel) {
        if (this.toolbar != null) {
//            this.toolbar.setTitle(topSettingModel.title);
            if(!TextUtils.isEmpty(topSettingModel.title)){
                this.toolbar.setCenteredTitle(topSettingModel.title);
            }else{
                this.toolbar.setTitle(topSettingModel.leftTitle);
            }

        }
    }


    public void setToolbar(CenteredToolbar toolbar){
        this.toolbar = toolbar;
    }

    public CenteredToolbar getToolbar(){
        return this.toolbar;
    }
}
