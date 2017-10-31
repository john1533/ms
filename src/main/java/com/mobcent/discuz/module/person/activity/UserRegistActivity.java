package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.UserRegistFragment;
import java.io.Serializable;
import java.util.HashMap;

public class UserRegistActivity extends PopComponentActivity implements IntentConstant {
    private HashMap<String, Serializable> goParam;
    private Class<?> goToActivityClass;

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        if (intent != null) {
            this.goToActivityClass = (Class) intent.getSerializableExtra(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS);
            this.goParam = (HashMap) intent.getSerializableExtra(IntentConstant.INTENT_GO_PARAM);
        }
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new UserRegistFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS, this.goToActivityClass);
        bundle.putSerializable(IntentConstant.INTENT_GO_PARAM, this.goParam);
        fragment.setArguments(bundle);
        return fragment;
    }
}
