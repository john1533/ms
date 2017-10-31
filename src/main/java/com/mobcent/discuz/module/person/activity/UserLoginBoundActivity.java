package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.UserLoginBoundFragment;
import com.mobcent.discuz.module.person.activity.fragment.UserRegisterBoundFragment;

public class UserLoginBoundActivity extends PopComponentActivity implements IntentConstant, FinalConstant {
    private boolean isLoginBound = false;

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        if (intent != null) {
            this.isLoginBound = ((Boolean) intent.getSerializableExtra(IntentConstant.INTENT_USER_LOGIN_BOUND_FLAG)).booleanValue();
        }
    }

    protected Fragment initContentFragment() {
        if (this.isLoginBound) {
            return new UserLoginBoundFragment();
        }
        return new UserRegisterBoundFragment();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            finish();
        }
    }
}
