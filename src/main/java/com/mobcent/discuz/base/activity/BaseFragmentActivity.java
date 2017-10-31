package com.mobcent.discuz.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.mobcent.discuz.android.exception.CrashHandler;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.PayStateModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.android.user.helper.UserManageHelper.SettingDataDelegate;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.FragmentOptHelper;
import com.mobcent.discuz.base.helper.TopBarHelper;
import com.mobcent.discuz.base.helper.TopBarOptImpl;
import com.mobcent.discuz.base.widget.CenteredToolbar;
import com.mobcent.discuz.base.widget.TopBarWidget;
import com.mobcent.lowest.android.ui.utils.MCTouchSlidHelper;
import com.mobcent.lowest.android.ui.utils.MCTouchSlidHelper.TouchSlideDelegate;
import com.mobcent.lowest.base.utils.MCActivityUtils;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class BaseFragmentActivity extends AppCompatActivity implements TopBarOptImpl, BaseIntentConstant, TouchSlideDelegate {
    public static final int TYPE_COMPONENT = 3;
    public static final int TYPE_HOME = 1;
    public static final int TYPE_MODULE = 2;
    private FragmentOptHelper fragmentHelper;
    protected InputMethodManager imm;
    protected PermissionModel permissionModel = null;
    protected MCResource resource;
    protected SettingModel settingModel = null;
    protected MCTouchSlidHelper slideHelper;
//    protected TopBarWidget topBar;
    protected TopBarHelper topBarHelper;
    protected UserManageHelper userManageHelper;
    protected CenteredToolbar mToolbar;


    protected abstract String getLayoutName();

    protected abstract void initActions();

    protected abstract void initViews();

    public void setSettingModel(SettingModel settingModel) {
        this.settingModel = settingModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initBaseUtils();
        setTheme(MCActivityUtils.getTheme(this));
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            try {
                BaseResultModel<ConfigModel> configModel = (BaseResultModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL);
                if (getAppApplication().getConfigModel() == null && configModel != null) {
                    getAppApplication().setConfigModel(configModel);
                }
            } catch (Exception e) {
//                CrashHandler.getInstance().handleExceptionBySelf(e);
                e.printStackTrace();
                return;
            }
        }

        this.slideHelper = new MCTouchSlidHelper(this);
        this.slideHelper.setDelegate(this);

//        setTheme(resource.getStyleId("light"));
        overridePendingTransition(this.resource.getAnimId("slide_in_right"), 17432577);
        initDatas();

        initViews();
        initActions();
        getAppApplication().addActivity(this);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL, getAppApplication().getConfigModel());
        outState.putSerializable(BaseIntentConstant.BUNDLE_SETTING_MODEL, this.settingModel);
        outState.putSerializable(BaseIntentConstant.BUNDLE_PERMISSION_MODEL, this.permissionModel);
        outState.putSerializable(BaseIntentConstant.BUNDLE_PAYSTATE_MODEL, getAppApplication().getPayStateModel());
    }

    protected void initSaveInstanceState(Bundle savedInstanceState) {
        this.settingModel = (SettingModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_SETTING_MODEL);
        this.permissionModel = (PermissionModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_PERMISSION_MODEL);
        getAppApplication().setPayStateModel((PayStateModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_PAYSTATE_MODEL));
        getAppApplication().setConfigModel((BaseResultModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL));
        this.userManageHelper.setPermissionModel(this.permissionModel);
        this.userManageHelper.setSettingModel(this.settingModel);
    }

    protected void initBaseUtils() {
        this.resource = MCResource.getInstance(getApplicationContext());
    }

    protected void initDatas() {
        setContentViewByName(getLayoutName());
        if (isContainTopBar()) {
            View top = findViewById(this.resource.getViewId("my_toolbar"));//从BaseFragmentActivity派生的activity都有这个view
            if (top == null) {
                throw new RuntimeException(this.resource.getString("top_bar_un_define"));
            }
//            this.topBar = (TopBarWidget) top;
            mToolbar = (CenteredToolbar) top;
            this.topBarHelper = new TopBarHelper();
            this.topBarHelper.setToolbar(mToolbar);
            setSupportActionBar(mToolbar);

        }
        this.userManageHelper = UserManageHelper.getInstance(getApplicationContext());
        this.imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.settingModel = this.userManageHelper.getSettingModel();
        this.permissionModel = this.userManageHelper.getPermissionModel();
    }

    protected boolean isContainTopBar() {
        return true;
//        return false;
    }

    protected View findViewByName(String viewName) {
        return findViewById(this.resource.getViewId(viewName));
    }

    protected View inflateLayout(String name) {
        return LayoutInflater.from(this).inflate(this.resource.getLayoutId(name), null);
    }

    protected void setContentViewByName(String layoutName) {
        setContentView(this.resource.getLayoutId(layoutName));
    }

    protected DiscuzApplication getAppApplication() {
        return (DiscuzApplication) getApplication();
    }

    public TopBarHelper getTopBarHelper() {
        return this.topBarHelper;
    }

    protected FragmentOptHelper getFragmentHelper() {
        if (this.fragmentHelper == null) {
            this.fragmentHelper = new FragmentOptHelper(getSupportFragmentManager());
        }
        return this.fragmentHelper;
    }

    protected void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, 0).show();
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            this.imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
        }
    }

    public void showSoftKeyboard() {
        if (getCurrentFocus() != null) {
            this.imm.showSoftInput(getCurrentFocus(), 1);
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.settingModel == null) {
            this.settingModel = getAppApplication().getSettingModel();
        }
        if (this.permissionModel == null) {
            this.permissionModel = getAppApplication().getPermissionModel();
        }
        if (this.settingModel == null || this.permissionModel == null) {
            new Thread() {
                public void run() {
                    BaseFragmentActivity.this.userManageHelper.getSetting(false, new SettingDataDelegate() {
                        public void saveSettingToApplication(SettingModel settingModel) {
                            ((DiscuzApplication) BaseFragmentActivity.this.getApplication()).setSettingModel(settingModel);
                            BaseFragmentActivity.this.setSettingModel(settingModel);
                        }

                        public void savePermissionToApplication(PermissionModel permissionModel) {
                            ((DiscuzApplication) BaseFragmentActivity.this.getApplication()).setPermissionModel(permissionModel);
                            BaseFragmentActivity.this.setPermissionModel(permissionModel);
                        }
                    });
                }
            }.start();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, this.resource.getAnimId("slide_out_right"));
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean z = true;
        try {
            if (!this.slideHelper.dispatchTouchEvent(ev)) {
                z = super.dispatchTouchEvent(ev);
            }
        } catch (Exception e) {
        }
        return z;
    }

    public boolean isSlideAble() {
        return true;
    }

    public boolean isSlideFullScreen() {
        return false;
    }

    public void slideExit() {
        hideSoftKeyboard();
        finish();
    }

    protected void onDestroy() {
        getAppApplication().removeActivity(this);
        super.onDestroy();
    }
}
