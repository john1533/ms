package com.mobcent.lowest.android.ui.module.plaza.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobcent.discuz.activity.utils.DZImageLoadUtils;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.lowest.base.config.LowestConfig;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;

public class SettingFragment extends BaseFragment implements UserManageHelper.ChangeUserInfoListener,View.OnClickListener {
    private LowestConfig config;
    private boolean isCreate = true;
    private SharedPreferencesDB sharedPreferencesDB;
    private ImageView userIconImg;
    private TextView userNameText;
    private LinearLayout infomationLayout;
    private LinearLayout clearCacheBox;
    private UserService userService;

    protected void initData() {
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.config = LowestManager.getInstance().getConfig();
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(this.activity);
        UserManageHelper.getInstance(this.activity).registListener(this);
    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.mcResource.getLayoutId("mc_plaza_fragment_setting"), null);
        if (this.isCreate) {
            initFirst();
            this.isCreate = false;
        }
        userIconImg = (ImageView) findViewByName(view,"user_icon_img") ;
        userNameText = (TextView) findViewByName(view,"user_name_text");

        infomationLayout = (LinearLayout)findViewByName(view,"mc_forum_infomation_layout");
        clearCacheBox = (LinearLayout)findViewByName(view,"clear_cache_box");
        return view;
    }

    protected void initWidgetActions() {
        infomationLayout.setOnClickListener(this);
        clearCacheBox.setOnClickListener(this);
    }


    private void initFirst() {
        if (this.userService.isLogin()) {
            changeUser( true);
        }
    }

    @Override
    public void change(boolean isLogin, UserInfoModel userInfo) {
        changeUser(isLogin);
        Log.v("SettingFragment","isLogin:"+isLogin);
    }

    private void changeUser(boolean isLogin) {
        if (isLogin) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    DZImageLoadUtils.loadImage(userIconImg,SettingFragment.this.sharedPreferencesDB.getIcon());
                    userNameText.setText(SettingFragment.this.sharedPreferencesDB.getNickName());
                    Log.v("SettingFragment","getNickName:"+SettingFragment.this.sharedPreferencesDB.getNickName());
                }
            });
        }else{
            this.mHandler.post(new Runnable() {
                public void run() {
//                    DZImageLoadUtils.loadImage(userIconImg,SettingFragment.this.sharedPreferencesDB.getIcon());
                    userIconImg.setImageResource(mcResource.getDrawableId("mc_forum_squre_icon9"));
                    userNameText.setText(mcResource.getString("mc_plaza_top_personal"));
                    Log.v("SettingFragment","getNickName:"+SettingFragment.this.sharedPreferencesDB.getNickName());
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mcResource.getViewId("mc_forum_infomation_layout")){
            Intent intent = null;
            if (LoginHelper.doInterceptor(getContext(), UserHomeActivity.class, null)) {
                intent = new Intent(getContext(), UserHomeActivity.class);
            }
            if (intent != null) {
                getContext().startActivity(intent);
            }
            return;
        }else if(v.getId() == mcResource.getViewId("clear_cache_box")){
            MCToastUtils.toastByResName(getContext(), "mc_forum_clear_cache_proess");
            new Thread() {
                public void run() {
                    MCLibIOUtil.cleanCache(getContext());
                    SettingFragment.this.mHandler.post(new Runnable() {
                        public void run() {
                            MCToastUtils.toastByResName(SettingFragment.this.getContext(), "mc_forum_clear_cache_success");
                        }
                    });
                }
            }.start();
            return;
        }


    }
}
