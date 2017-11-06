package com.mobcent.discuz.application.config;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.baidu.location.BDLocation;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.about.fragment.activity.AboutActivity;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.discuz.module.person.activity.UserListActivity;
import com.mobcent.discuz.module.person.activity.UserSettingActivity;
import com.mobcent.discuz.module.topic.list.fragment.activity.UserTopicListActivity;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.base.config.LowestConfig;
import com.mobcent.lowest.module.game.model.WebGameModel;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.mobcent.lowest.module.plaza.model.SearchModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

public class LowestConfigImpl extends LowestConfig implements PlazaConstant {
    private Context context;
    private SharedPreferencesDB db;
    private String ctr;

    public LowestConfigImpl(Context context) {
        this.context = context.getApplicationContext();
        db = SharedPreferencesDB.getInstance(this.context);
        ctr = Locale.getDefault().getCountry();
    }

    public void onAppItemClick(Context context, PlazaAppModel paAppModel) {
        Intent intent = null;
        if (paAppModel.getData() == null) {
            switch (paAppModel.getNativeCat()) {
                case PLAZA_ID_ABOUT:
                    intent = new Intent(context, AboutActivity.class);
                    break;
                case PLAZA_ID_SET:
                    intent = new Intent(context, UserSettingActivity.class);
                    break;
                case PLAZA_ID_SOUROUND_RECOMMEND_USER:
                    HashMap<String, Serializable> recommendParam = new HashMap();
                    recommendParam.put("userType", "recommend");
                    recommendParam.put("orderBy", "dateline");
                    if (LoginHelper.doInterceptor(context, UserListActivity.class, recommendParam)) {
                        intent = new Intent(context.getApplicationContext(), UserListActivity.class);
                        intent.putExtra("userType", "recommend");
                        intent.putExtra("orderBy", "dateline");
                        break;
                    }
                    return;
                case PLAZA_ID_SOUROUND_USER:
                    HashMap<String, Serializable> souroundParam = new HashMap();
                    souroundParam.put("userType", "all");
                    souroundParam.put("orderBy", "distance");
                    if (LoginHelper.doInterceptor(context, UserListActivity.class, souroundParam)) {
                        intent = new Intent(context.getApplicationContext(), UserListActivity.class);
                        intent.putExtra("userType", "all");
                        intent.putExtra("orderBy", "distance");
                        break;
                    }
                    return;
                case PLAZA_ID_SOUROUND_POST:
                    intent = new Intent(context.getApplicationContext(), UserTopicListActivity.class);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, PostsConstant.TOPIC_TYPE_SURROUND);
                    break;
                case PLAZA_ID_USER_HOME:
                    if (LoginHelper.doInterceptor(context, UserHomeActivity.class, null)) {
                        intent = new Intent(context, UserHomeActivity.class);
                        break;
                    }
                    return;
            }
            if (intent != null) {
                context.startActivity(intent);
            }
        } else if (paAppModel.getData() instanceof ConfigComponentModel) {
            ActivityDispatchHelper.dispatchActivity(context, (ConfigComponentModel) paAppModel.getData());
        }
    }

    public void onSearchItemClick(Context context, SearchModel searchModel) {
    }

    public boolean onPlazaBackPressed(Context context) {
        return false;
    }

    public void onPersonalClick(Context context) {
    }

    public void onSetClick(Context context) {
    }

    public void onAboutClick(Context context) {
    }

    public void shareWebGame(View v, WebGameModel webGameModel) {
    }

    public boolean isLogin(View v) {
        return false;
    }

    public void clickScreenShotImg(View v, String[] urlArr) {
    }

    public String getSDKVersion() {
        return BaseApiConstant.SDK_VERSION_VALUE;
    }

    public String getForumKey() {
        return this.db.getForumKey();
    }

    public String getForumId() {
        return null;
    }

    public String getChannelNum() {
        return null;
    }

    public BDLocation getLocation() {
        return this.db.getLocation();
    }

    public String getAccessToken() {
        return this.db.getAccessToken();
    }

    public String getAccessSecret() {
        return this.db.getAccessSecret();
    }

    public long getUserId() {
        return this.db.getUserId();
    }
//CN,HK,MO 澳门;TW
    public String getCtr() {
        return ctr;
    }
}
