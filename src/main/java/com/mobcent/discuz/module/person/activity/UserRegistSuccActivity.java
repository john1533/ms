package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.person.activity.fragment.UserRegistSuccFragment;
import java.io.Serializable;
import java.util.HashMap;

public class UserRegistSuccActivity extends PopComponentActivity implements IntentConstant {
    private Fragment fragment;
    private HashMap<String, Serializable> goParam;
    private Class<?> goToActivityClass;
    private UserInfoModel userInfoModel;

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        if (intent != null) {
            this.goToActivityClass = (Class) intent.getSerializableExtra(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS);
            this.goParam = (HashMap) intent.getSerializableExtra(IntentConstant.INTENT_GO_PARAM);
            this.userInfoModel = (UserInfoModel) intent.getSerializableExtra(IntentConstant.INTENT_USER_INFO);
        }
    }

    protected Fragment initContentFragment() {
        this.fragment = new UserRegistSuccFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS, this.goToActivityClass);
        bundle.putSerializable(IntentConstant.INTENT_GO_PARAM, this.goParam);
        bundle.putSerializable(IntentConstant.INTENT_USER_INFO, this.userInfoModel);
        this.fragment.setArguments(bundle);
        return this.fragment;
    }
}
