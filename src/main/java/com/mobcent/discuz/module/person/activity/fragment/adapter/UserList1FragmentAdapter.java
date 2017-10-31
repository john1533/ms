package com.mobcent.discuz.module.person.activity.fragment.adapter;

import android.content.Context;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import java.util.List;

public class UserList1FragmentAdapter extends BaseUserListFragmentAdapter {
    public UserList1FragmentAdapter(Context context, List<UserInfoModel> list, String userType, ConfigComponentModel componentModel) {
        super(context, list, userType, componentModel);
    }

    protected String getLayoutName() {
        return "user_list_fragment_item";
    }
}
