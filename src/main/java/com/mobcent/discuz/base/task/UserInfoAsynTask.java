package com.mobcent.discuz.base.task;

import android.content.Context;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;

public class UserInfoAsynTask extends BaseTask<BaseResultModel<UserInfoModel>> {
    private long userInfoId;
    private UserService userService;

    public UserInfoAsynTask(Context context, BaseRequestCallback<BaseResultModel<UserInfoModel>> _callback, long userInfoId) {
        super(context, _callback);
        this.userInfoId = userInfoId;
        this.userService = new UserServiceImpl(context);
    }

    protected BaseResultModel<UserInfoModel> doInBackground(Void... params) {
        return this.userService.getUserInfo(this.userInfoId);
    }
}
