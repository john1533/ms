package com.mobcent.discuz.module.person.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.person.activity.fragment.AuthorizationSuccFragment;

public class AuthorizationSuccActivity extends PopComponentActivity {
    private UserInfoModel infoModel;
    private long platformId;

    protected void initDatas() {
        super.initDatas();
        this.infoModel = (UserInfoModel) getIntent().getSerializableExtra(IntentConstant.INTENT_USER_INFO);
        this.platformId = getIntent().getLongExtra("platformId", 0);
    }

    protected Fragment initContentFragment() {
        AuthorizationSuccFragment fragment = new AuthorizationSuccFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_USER_INFO, this.infoModel);
        bundle.putString("platformId", this.platformId + "");
        fragment.setArguments(bundle);
        return fragment;
    }
}
