package com.mobcent.discuz.module.person.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.person.activity.fragment.UserProfileFragment;

public class UserProfileActivity extends PopComponentActivity {
    private UserInfoModel infoModel;

    protected void initDatas() {
        super.initDatas();
        this.infoModel = (UserInfoModel) getIntent().getSerializableExtra(IntentConstant.INTENT_USER_INFO);
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_USER_INFO, this.infoModel);
        fragment.setArguments(bundle);
        return fragment;
    }
}
