package com.mobcent.discuz.module.person.activity.fragment;

import android.view.View;
import com.mobcent.discuz.module.person.activity.fragment.adapter.UserList1FragmentAdapter;

public class UserList1Fragment extends BaseUserListFragment {
    protected String getRootLayoutName() {
        return "user_list1_fragment";
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        if (this.adapter == null) {
            this.adapter = new UserList1FragmentAdapter(this.activity, this.userList, this.orderBy, this.componentModel);
        }
        this.userListView.setAdapter(this.adapter);
    }
}
