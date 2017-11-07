package com.mobcent.discuz.base.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import java.util.ArrayList;
import java.util.List;

public class DemoDetailFragment extends BaseFragment {
    private int COMMENT = 1;
    private int SHARE = 2;

    protected String getRootLayoutName() {
        return "base_demo_detail";
    }

    protected void initViews(View rootView) {
    }

    protected void initActions(View rootView) {
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = true;
        List<TopBtnModel> rightModels = new ArrayList();
        topSettingModel.title = "帖子详情";
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button2";
        topBtnModel.title = "评论";
        topBtnModel.action = this.COMMENT;
        rightModels.add(topBtnModel);
        TopBtnModel topBtnModel2 = new TopBtnModel();
        topBtnModel2.icon = "mc_forum_top_bar_button2";
        topBtnModel2.title = "分享";
        topBtnModel2.action = this.SHARE;
        rightModels.add(topBtnModel2);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                TopBtnModel t = (TopBtnModel) v.getTag();
                if (t.action == DemoDetailFragment.this.COMMENT) {
                    Toast.makeText(DemoDetailFragment.this.getActivity(), "评论", 1000).show();
                } else if (t.action == DemoDetailFragment.this.SHARE) {
                    Toast.makeText(DemoDetailFragment.this.getActivity(), "分享", 1000).show();
                } else if (t.action == -2) {
                    Toast.makeText(DemoDetailFragment.this.getActivity(), "标题点击", 1000).show();
                }
            }
        });
    }
}
