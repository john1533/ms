package com.mobcent.discuz.base.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.text.TextUtils;
import com.mobcent.discuz.activity.HomeActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.db.UserDBUtil;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.task.ConfigTask;
import com.mobcent.discuz.base.task.RequestCalback;
import com.mobcent.lowest.base.utils.MCAppUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.update.android.os.service.helper.UpdateCheckHelper;

public class InitHelper {
    private static InitHelper initHelper;
    public String TAG = "InitHelper";
    private Context context;
    private SharedPreferencesDB db;
    private MCResource resource;

    private InitHelper(Context context) {
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.db = SharedPreferencesDB.getInstance(context.getApplicationContext());
        this.context = context;
    }

    public static synchronized InitHelper getInstance(Context context) {
        InitHelper initHelper;
        synchronized (InitHelper.class) {
            if (InitHelper.initHelper == null) {
                initHelper = new InitHelper(context.getApplicationContext());
                InitHelper.initHelper = initHelper;

            }else {
                initHelper = InitHelper.initHelper;
            }
        }
        return initHelper;
    }

    public void init(Context context, boolean getAllDataByNet, RequestCalback<BaseResultModel<ConfigModel>> _callback) {
        new ConfigTask(context, getAllDataByNet, _callback).execute(new Void[0]);
    }

    public void dispatchActivity(Activity activity, String skipToWhere) {
        if (FinalConstant.Home_SKIP.equals(skipToWhere)) {
            defaultSkip(activity);
        } else if (FinalConstant.HOME_SKIP_TO_SESSION.equals(skipToWhere)) {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.putExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE, FinalConstant.HOME_SKIP_TO_SESSION);
            intent.putExtra(IntentConstant.INTENT_SKIP_TO_HOME_MSG, true);
            activity.startActivity(intent);
            activity.finish();
        } else if (!FinalConstant.HOME_SKIP_TO_CHAT.equals(skipToWhere)) {
            defaultSkip(activity);
        }
    }

    private void defaultSkip(Activity activity) {
        if (!this.db.getShortCutFlag()) {
            this.db.setShortCutFlag(true);
            addShortcut(activity);
        }
        UpdateCheckHelper.check(activity.getApplicationContext(), null);//更新检查
        doLoginAsync();
        activity.startActivity(new Intent(activity, HomeActivity.class));
        activity.finish();
    }

    private void addShortcut(Activity activity) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("android.intent.extra.shortcut.NAME", MCAppUtil.getAppName(activity.getApplicationContext()));
        shortcut.putExtra("duplicate", false);
        Intent shortcutIntent = new Intent("android.intent.action.MAIN");
        shortcutIntent.setClassName(activity, activity.getClass().getName());
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
        try {
            shortcut.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(activity, this.resource.getDrawableId("app_icon128")));
            activity.sendBroadcast(shortcut);
        } catch (Exception e) {
        }
    }

    private void doLoginAsync() {
        new Thread() {
            public void run() {
                InitHelper.this.doLoginSync();
            }
        }.start();
    }

    private void doLoginSync() {
        UserService userService = new UserServiceImpl(this.context);
        if (userService.isLogin()) {
            UserInfoModel userInfoModel = UserDBUtil.getInstance(this.context).getCurrUser(this.db.getUserId());
            if (userInfoModel != null) {
                String userName = userInfoModel.getNickname();
                String passWord = userInfoModel.getPwd();
                if (!TextUtils.isEmpty(userName) && TextUtils.isEmpty(passWord)) {
                    userName = UserConstant.SYSTEM_USER_PASSWORD;
                    passWord = UserConstant.SYSTEM_USER_PASSWORD;
                }
                userService.loginUser(userName, passWord, "login");
            }
        }
    }
}
