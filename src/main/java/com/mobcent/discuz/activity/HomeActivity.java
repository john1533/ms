package com.mobcent.discuz.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.model.ConfigNavModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.observer.ActivityObserver;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.ConfigService;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.ConfigServiceImpl;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.dispatch.FragmentDispatchHelper;
import com.mobcent.discuz.base.fragment.BaseModuleFragment;
import com.mobcent.discuz.base.fragment.SubPageFragment;
import com.mobcent.discuz.base.helper.ConfigOptHelper;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.base.widget.MCPublishMenuDialog;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.msg.fragment.activity.ChatRoomActivity;
import com.mobcent.discuz.module.msg.fragment.activity.SessionListActivity;
import com.mobcent.discuz.module.msg.helper.MsgNotificationHelper;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseFragmentActivity implements BaseIntentConstant, PlazaConstant, ConfigConstant {
    private LinearLayout bottomBox;
    private ConfigService configService;
    private FrameLayout container;
    private int count;
    private MCPublishMenuDialog dialog;
    private Fragment[] fragments = null;
    private boolean isContainMsgOnly;
    private boolean isHaveNav = false;
    private boolean isNeedLoginInterupt = false;
    private int loginInteruptPosition = 0;
    private ConfigModuleModel msgConfigModuleModel;
    private String msgModuleTag = null;
    private Button[] navBtns;
    private List<ConfigModuleModel> navModuleList = new ArrayList();
    private MsgNotificationHelper notificationHelper;
    private ActivityObserver observer;
    private ObserverHelper observerHelper;
    private Fragment curFragment;

    private OnClickListener tabClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag() != null && (v.getTag() instanceof ConfigModuleModel)) {
                ConfigModuleModel moduleModel = (ConfigModuleModel) v.getTag();
                if (moduleModel.getType().equals(ConfigConstant.MODULE_TYPE_ONELINK)) {
                    if (HomeActivity.this.dialog == null) {
                        HomeActivity.this.dialog = new MCPublishMenuDialog(HomeActivity.this, HomeActivity.this.resource.getStyleId("mc_forum_home_publish_dialog"));
                    }
                    HomeActivity.this.dialog.setConfigComponenList(moduleModel.getComponentList());
                    HomeActivity.this.dialog.setPermissionModel(HomeActivity.this.permissionModel);
                    HomeActivity.this.dialog.setSettingModel(HomeActivity.this.settingModel);
                    if (HomeActivity.this.dialog.isShowing()) {
                        HomeActivity.this.dialog.dismiss();
                    } else {
                        HomeActivity.this.dialog.show(v);
                    }
                } else if (ConfigConstant.MODULE_TYPE_FULL.equals(moduleModel.getType())
                        && ConfigOptHelper.navContainOnly(moduleModel, ConfigConstant.COMPONENT_MESSAGELIST)
                        && !LoginHelper.doInterceptor(HomeActivity.this, null, null)) {
                    HomeActivity.this.isNeedLoginInterupt = true;
                    HomeActivity.this.loginInteruptPosition = moduleModel.getPosition();
                } else if (!ConfigOptHelper.isNeedLogin(moduleModel)
                        || LoginHelper.doInterceptor(HomeActivity.this, null, null)) {
                    HomeActivity.this.selectCurrentTab(moduleModel);
                } else {
                    HomeActivity.this.isNeedLoginInterupt = true;
                    HomeActivity.this.loginInteruptPosition = moduleModel.getPosition();
                }
            }
        }
    };
    private UserService userService = null;

    private void selectCurrentTab(ConfigModuleModel moduleModel) {
        if (moduleModel != null) {
            int position = moduleModel.getPosition();
            for (int i = 0; i < this.fragments.length; i++) {
                curFragment = this.fragments[i];
                Button btn = this.navBtns[i];

                if (position == i) {
                    if (btn != null) {
                        btn.setSelected(true);
                    }
                    if (curFragment == null) {
                        curFragment = FragmentDispatchHelper.disPatchFragment(moduleModel);
                        this.fragments[i] = curFragment;
                        getFragmentHelper().addFragment(this.container.getId(), curFragment);
                    } else {
                        getFragmentHelper().showFragment(curFragment);
                        curFragment.onResume();
                    }
                    if (curFragment instanceof BaseModuleFragment) {
                        ((BaseModuleFragment) curFragment).dealTopBar();
                    }
                } else {
                    if (btn != null) {
                        btn.setSelected(false);
                    }
                    if (curFragment != null) {
                        getFragmentHelper().hideFragment(curFragment);
                        curFragment.onPause();
                    }
                }
            }
        }
    }

    private void selectFirst() {
        if (!MCListUtils.isEmpty(this.navModuleList)) {
            selectCurrentTab((ConfigModuleModel) this.navModuleList.get(0));
        }
    }

    protected String getLayoutName() {
        return "home_page_activity";
    }

    protected void initDatas() {
        super.initDatas();
        this.configService = new ConfigServiceImpl(getApplicationContext());
        this.userService = new UserServiceImpl(getApplicationContext());
        this.observerHelper = ObserverHelper.getInstance();
        this.notificationHelper = MsgNotificationHelper.getInstance(getApplicationContext());
        if (getIntent() != null && getIntent().getBooleanExtra("push", false)) {
            getAppApplication().setConfigModel(new ConfigServiceImpl(getApplicationContext()).getConfigModelByLocal());
        }
    }

    protected void initViews() {
        this.container = (FrameLayout) findViewByName("container");
        this.bottomBox = (LinearLayout) findViewByName("bottomBox");
        setupTabHost();
        setupMsgTag(getIntent());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        if(curFragment instanceof SubPageFragment){
//            final String [] testStrings = getResources().getStringArray(resource.getArrayId("mc_place_home_around_names"));
//
//            MenuInflater inflater = getMenuInflater();
//            inflater.inflate(resource.getMenuId("menu"), menu);
//
//
//            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//            MenuItem menuItem = menu.findItem(resource.getViewId("searchView"));
//
//            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//            int completeTextId = searchView.getResources().getIdentifier("android:id/search_src_text", null, null);
//
//            AutoCompleteTextView completeText = (AutoCompleteTextView) searchView
//                    .findViewById(android.support.v7.appcompat.R.id.search_src_text) ;
//            completeText.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,testStrings));
//            completeText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    searchView.setQuery(testStrings[position], true);
//
//                }
//            });
//
//            completeText.setThreshold(0);
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Toast.makeText(HomeActivity.this, query, Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        }
//
//        return super.onCreateOptionsMenu(menu);
//    }

    protected void initActions() {
    }

    protected void onResume() {
        super.onResume();
        this.notificationHelper.setHomeActivity(true);
        this.notificationHelper.setNotExistApp(true);
        this.observerHelper.getActivityObservable().updateHomeTabNum(this.observerHelper.getActivityObservable().getTabNum(getApplicationContext(), SharedPreferencesDB.getInstance(getApplicationContext()).getUserId()));
        if (this.isNeedLoginInterupt && this.userService.isLogin()) {
            this.navBtns[this.loginInteruptPosition].performClick();
            this.isNeedLoginInterupt = false;
            this.loginInteruptPosition = 0;
            return;
        }
        this.isNeedLoginInterupt = false;
        this.loginInteruptPosition = 0;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setupMsgTag(intent);
    }

    private void setupMsgTag(Intent intent) {
        if (intent != null) {
            if (intent.getBooleanExtra(IntentConstant.INTENT_SKIP_TO_HOME_MSG, false) && this.isContainMsgOnly && this.msgConfigModuleModel != null) {
                selectCurrentTab(this.msgConfigModuleModel);
            }
            String skipToWhere = intent.getStringExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE);
            if (!TextUtils.isEmpty(skipToWhere)) {
                if (FinalConstant.HOME_SKIP_TO_SESSION.equals(skipToWhere)) {
                    Intent sessionIntent = new Intent(getApplicationContext(), SessionListActivity.class);
                    sessionIntent.setFlags(67108864);
                    startActivity(sessionIntent);
                } else if (FinalConstant.HOME_SKIP_TO_CHAT.equals(skipToWhere)) {
                    MsgUserListModel msgUserListModel = (MsgUserListModel) intent.getSerializableExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL);
                    if (msgUserListModel != null) {
                        Intent chatRoomIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                        chatRoomIntent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                        chatRoomIntent.setFlags(67108864);
                        startActivity(chatRoomIntent);
                    }
                }
            }
        }
    }

    private void setupTabHost() {
        BaseResultModel<ConfigModel> configModel = getAppApplication().getConfigModel();
        if (configModel != null && configModel.isVersionTooLow()) {
            showAlertDialog("mc_forum_config_server_version_lower");
        }
        if (configModel == null || configModel.getRs() == 0) {
            DZToastAlertUtils.toast(getApplicationContext(), configModel);
            configModel = this.configService.getConfigModelAssets("config.json");
        } else if (configModel.getData() == null || MCListUtils.isEmpty(((ConfigModel) configModel.getData()).getNavList())) {
            showAlertDialog("mc_forum_config_list_empty");
            configModel = this.configService.getConfigModelAssets("config.json");
        }
        List<ConfigNavModel> navList = ((ConfigModel) configModel.getData()).getNavList();
        if (!ConfigOptHelper.isNavContainDiscover(((ConfigModel) configModel.getData()).getModuleMap(), navList)) {
            navList.add(ConfigOptHelper.createPlazaNavModel(getApplicationContext()));
            ((ConfigModel) configModel.getData()).getModuleMap().put(Long.valueOf(-1), ConfigOptHelper.createPlazaModuleModel(getApplicationContext()));
        }
        int count = navList.size();
        this.fragments = new Fragment[count];
        this.navBtns = new Button[count];
        for (int i = 0; i < count; i++) {
            ConfigNavModel navModel = (ConfigNavModel) navList.get(i);
            ConfigModuleModel moduleModel = (ConfigModuleModel) ((ConfigModel) configModel.getData()).getModuleMap().get(Long.valueOf(navModel.getModuleId()));
            this.navModuleList.add(moduleModel);
            if (moduleModel != null) {
                View navBox;
                moduleModel.setPosition(i);
                if (!this.isHaveNav) {
                    this.isHaveNav = true;
                }
                if (i == 0 || !this.isContainMsgOnly) {
                    this.isContainMsgOnly = ConfigOptHelper.navContainOnly(moduleModel, ConfigConstant.COMPONENT_MESSAGELIST);
                    this.notificationHelper.setContainMsg(this.isContainMsgOnly);
                }
                if (ConfigOptHelper.navContainOnly(moduleModel, ConfigConstant.COMPONENT_MESSAGELIST)) {
                    this.msgConfigModuleModel = moduleModel;
                    this.msgModuleTag = moduleModel.getId() + "";
                    navBox = inflateLayout("home_page_nav_item_msg");
                    View pointView = navBox.findViewById(this.resource.getViewId("point_view"));
                    if (this.observerHelper.getActivityObservable().getTabNum(getApplicationContext(), SharedPreferencesDB.getInstance(getApplicationContext()).getUserId()) > 0) {
                        pointView.setVisibility(View.VISIBLE);
                    } else {
                        pointView.setVisibility(View.GONE);
                    }
                    final View view = pointView;
                    this.observer = new ActivityObserver() {
                        public void updateHomeTabNum(final int msgNum) {
                            view.post(new Runnable() {
                                public void run() {
                                    if (msgNum > 0) {
                                        view.setVisibility(View.VISIBLE);
                                    } else {
                                        view.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    };
                    this.observerHelper.getActivityObservable().registerObserver(this.observer);
                } else {
                    navBox = inflateLayout("home_page_nav_item");
                }
                Button navBtn = (Button) navBox.findViewById(this.resource.getViewId("nav_btn"));
                this.navBtns[i] = navBtn;
                navBtn.setText(navModel.getTitle());
                Bundle bundle = new Bundle();
                bundle.putSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL, moduleModel);
                bundle.putInt(BaseIntentConstant.BUNDLE_ACTIVITY_TYPE, 1);
                navBtn.setTag(moduleModel);
                navBtn.setOnClickListener(this.tabClickListener);
                if (ConfigConstant.MODULE_TYPE_ONELINK.equals(moduleModel.getType())) {
                    navBtn.setText("");
                    navBtn.setBackgroundResource(this.resource.getDrawableId("mc_forum_main_bar_button17"));
                    LayoutParams lps = (LayoutParams) navBtn.getLayoutParams();
                    lps.width = MCPhoneUtil.dip2px(getApplicationContext(), 38.0f);
                    lps.height = MCPhoneUtil.dip2px(getApplicationContext(), 34.0f);
                    navBtn.setLayoutParams(lps);
                } else {
                    navBtn.setBackgroundResource(this.resource.getDrawableId(navModel.getIcon()));
                }
                this.bottomBox.addView(navBox, new LinearLayout.LayoutParams(-2, -2, CustomConstant.RATIO_ONE_HEIGHT));
            }
        }
        selectFirst();
    }

    public void onBackPressed() {
        if (this.count == 1) {
            System.exit(0);
            super.onBackPressed();
            return;
        }
        this.count++;
        Toast.makeText(this, this.resource.getStringId("mc_forum_home_back_exit"), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                HomeActivity.this.count = 0;
            }
        }, 5000);
    }

    protected void onPause() {
        super.onPause();
        this.notificationHelper.setHomeActivity(false);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.observer != null) {
            this.observerHelper.getActivityObservable().unregisterObserver(this.observer);
        }
    }

    public boolean isSlideAble() {
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.fragments != null && this.fragments.length != 0) {
            for (int i = 0; i < this.fragments.length; i++) {
                if (this.fragments[i] != null) {
                    this.fragments[i].onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private void showAlertDialog(String name) {
        AlertDialog dialog = new Builder(this).setMessage(
                this.resource.getString(name).replace("{0}", this.resource.getString("mc_discuz_base_request_url")))
                .setPositiveButton(this.resource.getString("mc_share_cancel"), null).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
