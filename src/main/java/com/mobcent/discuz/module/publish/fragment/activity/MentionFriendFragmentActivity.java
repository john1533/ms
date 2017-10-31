package com.mobcent.discuz.module.publish.fragment.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.publish.fragment.MentionFriendFragment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MentionFriendFragmentActivity extends PopComponentActivity {
    private List<UserInfoModel> infoModels;

    protected void initDatas() {
        super.initDatas();
        this.infoModels = (List) getIntent().getSerializableExtra(FinalConstant.POSTS_USER_LIST);
        if (this.infoModels == null) {
            this.infoModels = new ArrayList();
        }
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new MentionFriendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FinalConstant.POSTS_USER_LIST, (Serializable) this.infoModels);
        fragment.setArguments(bundle);
        return fragment;
    }
}
