package com.mobcent.discuz.module.msg.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.msg.fragment.CommentList1Fragment;

public class CommentListActivity extends PopComponentActivity {
    protected Fragment initContentFragment() {
        Fragment fragment = new CommentList1Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.componentModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
