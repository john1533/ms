package com.mobcent.discuz.module.person.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.person.activity.fragment.UserList1Fragment;

public class UserListActivity extends PopComponentActivity {
    private String orderBy;
    private long userId;
    private String userType;

    protected void initDatas() {
        super.initDatas();
        Intent intent = getIntent();
        if (intent != null) {
            this.userType = intent.getStringExtra("userType");
            this.userId = intent.getLongExtra("userId", 0);
            this.orderBy = intent.getStringExtra("orderBy");
        }
    }

    protected Fragment initContentFragment() {
        Fragment fragment = new UserList1Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.componentModel);
        bundle.putSerializable("userType", this.userType);
        bundle.putSerializable("userId", Long.valueOf(this.userId));
        bundle.putSerializable("orderBy", this.orderBy);
        fragment.setArguments(bundle);
        return fragment;
    }
}
