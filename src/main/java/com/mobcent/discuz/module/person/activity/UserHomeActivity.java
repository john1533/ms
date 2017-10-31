package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.BaseUserHomeFragment;
import com.mobcent.discuz.module.person.activity.fragment.UserHomeOtherFragment1;
import com.mobcent.discuz.module.person.activity.fragment.UserHomeSelfFragment1;

public class UserHomeActivity extends PopComponentActivity implements BaseIntentConstant {
    private long uid = -1;

    protected void initDatas() {
        super.initDatas();
        this.uid = getIntent().getLongExtra("userId", -1);
    }

    protected Fragment initContentFragment() {
        Fragment fragment;
        Bundle bundle = new Bundle();
        long currentUserId = SharedPreferencesDB.getInstance(getApplicationContext()).getUserId();
        if (this.uid == -1 || this.uid == currentUserId) {
            bundle.putLong("userId", SharedPreferencesDB.getInstance(getApplicationContext()).getUserId());
            BaseResultModel<ConfigModel> config = DiscuzApplication._instance.getConfigModel();
            if (!(config == null || config.getData() == null)) {
                bundle.putBoolean(BaseIntentConstant.BUNDLE_IS_SHOW_MESSAGELIST, ((ConfigModel) config.getData()).isShowMessageList());
            }
            fragment = new UserHomeSelfFragment1();
        } else {
            bundle.putLong("userId", this.uid);
            fragment = new UserHomeOtherFragment1();
        }
        bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.componentModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getContentFragment() != null && (getContentFragment() instanceof BaseUserHomeFragment)) {
            ((BaseUserHomeFragment) getContentFragment()).onActivityResult(requestCode, resultCode, data);
        }
    }
}
