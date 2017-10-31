package com.mobcent.discuz.base.task;

import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.SettingModel;

public interface RequestCalback<Result> extends BaseRequestCallback<Result> {
    void onPostExecute(PermissionModel permissionModel);

    void onPostExecute(SettingModel settingModel);
}
