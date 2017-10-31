package com.mobcent.discuz.module.plaza.fragment;

import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.android.user.helper.UserManageHelper.ChangeUserInfoListener;
import com.mobcent.lowest.android.ui.module.plaza.fragment.PlazaFragmentNew;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.List;

public class PlazaFragmentComponent extends PlazaFragmentNew implements ChangeUserInfoListener {
    private SharedPreferencesDB sharedPreferencesDB;
    private UserService userService;

    protected void initData() {
        super.initData();
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(this.activity);
        UserManageHelper.getInstance(this.activity).registListener(this);
    }

    public void change(boolean isLogin, UserInfoModel userInfo) {
        changeUser(isLogin);
    }

    private void changeUser(boolean isLogin) {
        if (!MCListUtils.isEmpty(getModuleData())) {
            for (List<PlazaAppModel> appModels : getModuleData()) {
                if (dealUserHomeModule(appModels, isLogin)) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            PlazaFragmentComponent.this.notifyDataChange();
                        }
                    });
                    return;
                }
            }
        }
    }

    private boolean dealUserHomeModule(List<PlazaAppModel> appModels, boolean isLogin) {
        for (PlazaAppModel appModel : appModels) {
            if (-1 == appModel.getNativeCat()) {
                if (isLogin) {
                    appModel.setModelDrawable(this.sharedPreferencesDB.getIcon());
                    appModel.setModelName(this.sharedPreferencesDB.getNickName());
                } else {
                    appModel.setModelDrawable("mc_forum_squre_icon9");
                    appModel.setModelName(this.mcResource.getString("mc_forum_more_info"));
                }
                return true;
            }
        }
        return false;
    }

    public List<PlazaAppModel> getDbModuleData() {
        List<PlazaAppModel> appModels = super.getDbModuleData();
        if (this.userService.isLogin()) {
            dealUserHomeModule(appModels, true);
        }
        return appModels;
    }
}
