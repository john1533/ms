package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.UserLoginFragment;
import java.io.Serializable;
import java.util.HashMap;

public class UserLoginActivity extends PopComponentActivity implements IntentConstant, FinalConstant {
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
        Fragment fragment = new UserLoginFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS, this.goToActivityClass);
        bundle.putSerializable(IntentConstant.INTENT_GO_PARAM, this.goParam);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            finish();
        }
    }
}
