package com.mobcent.discuz.base.helper;

import android.content.Context;
import android.content.Intent;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;
import com.mobcent.discuz.module.person.activity.UserLoginActivity;
import java.io.Serializable;
import java.util.HashMap;

public class LoginHelper {
    /**
    * 判断是否已经登录，未登录跳转到登录界面并返回false，已登录返回true
    */
    public static boolean doInterceptor(Context context, Class<?> goToActicityClass, HashMap<String, Serializable> param) {
        boolean isLogin = new UserServiceImpl(context.getApplicationContext()).isLogin();
        if (!isLogin) {
            Intent intent = new Intent(context, UserLoginActivity.class);
            intent.putExtra(IntentConstant.INTENT_GO_TO_ACTIVITY_CLASS, goToActicityClass);
            intent.putExtra(IntentConstant.INTENT_GO_PARAM, param);
            ActivityDispatchHelper.startActivity(context, intent);
        }
        return isLogin;
    }
}
