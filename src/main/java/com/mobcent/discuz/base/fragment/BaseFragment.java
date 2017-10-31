package com.mobcent.discuz.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
//import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.mobcent.discuz.activity.BasePopActivity;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.view.MCProgressDialog;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.exception.CrashHandler;
import com.mobcent.discuz.android.model.ConfigComponentModel;
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
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCActivityUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment implements BaseIntentConstant {
    public String TAG = "BaseFragment";
    protected final long TIME_DELAYED = 0;
    protected Activity activity;
    private Bundle bundle;
    protected FragmentOptHelper fragmentHelper;
    private InputMethodManager imm;
    protected LayoutInflater inflater;
    protected boolean isFirstInit = true;
    private MCProgressDialog loadingPro;
    protected Handler mHandler = new Handler();
    protected ConfigComponentModel moduleModel = null;
    protected PermissionModel permissionModel = null;
    protected MCResource resource;
    protected View rootView;
    protected SettingModel settingModel = null;
    protected SharedPreferencesDB sharedPreferencesDB;
    private final List<AsyncTask<?, ?, ?>> taskList = new ArrayList();
    private TopBarOptImpl topBarOptImpl;
    protected UserManageHelper userManageHelper;

    public final class TopClickListener implements OnClickListener {
        private OnClickListener topClickListener;

        public TopClickListener(OnClickListener topClickListener) {
            this.topClickListener = topClickListener;
        }

        public void onClick(View v) {
            if (v.getTag() != null && (v.getTag() instanceof TopBtnModel)) {
                if (((TopBtnModel) v.getTag()).action == -1) {
                    BaseFragment.this.hideSoftKeyboard();
                    BaseFragment.this.getActivity().onBackPressed();
                } else if (this.topClickListener != null) {
                    this.topClickListener.onClick(v);
                }
            }
        }
    }

    protected abstract String getRootLayoutName();

    protected abstract void initActions(View view);

    protected abstract void initViews(View view);

    public void setSettingModel(SettingModel settingModel) {
        this.settingModel = settingModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            this.topBarOptImpl = (TopBarOptImpl) activity;
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            initDatas(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        activity.getTheme().applyStyle(MCActivityUtils.getTheme(activity),true);
        // create ContextThemeWrapper from the original Activity Context with the custom theme
//        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), MCActivityUtils.getTheme(activity));
//        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        try {
            this.rootView = inflater.inflate(this.resource.getLayoutId(getRootLayoutName() == null ? "base_error_fragment" : getRootLayoutName()), null);
            initViews(this.rootView);
            if (getActivity() instanceof PopComponentActivity) {
                componentDealTopbar();
            }
            initActions(this.rootView);
            if (this.isFirstInit) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        BaseFragment.this.firstCreate();
                    }
                }, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
        return this.rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        try {
            if (this.settingModel == null) {
                this.settingModel = getAppApplication().getSettingModel();
            }
            if (this.permissionModel == null) {
                this.permissionModel = getAppApplication().getPermissionModel();
            }
            if (this.settingModel == null || this.permissionModel == null) {
                new Thread() {
                    public void run() {
                        BaseFragment.this.userManageHelper.getSetting(false, new SettingDataDelegate() {
                            public void saveSettingToApplication(SettingModel settingModel) {
                                BaseFragment.this.getAppApplication().setSettingModel(settingModel);
                                BaseFragment.this.setSettingModel(settingModel);
                            }

                            public void savePermissionToApplication(PermissionModel permissionModel) {
                                BaseFragment.this.getAppApplication().setPermissionModel(permissionModel);
                                BaseFragment.this.setPermissionModel(permissionModel);
                            }
                        });
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

    public void onPause() {
        try {
            super.onPause();
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroyView() {
        try {
            super.onDestroyView();
            if (this.loadingPro != null) {
                this.loadingPro.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

    public void onDestroy() {
        try {
            super.onDestroy();
            cancelAllTask();
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            outState.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, this.moduleModel);
            outState.putSerializable(BaseIntentConstant.BUNDLE_SETTING_MODEL, this.settingModel);
            outState.putSerializable(BaseIntentConstant.BUNDLE_PERMISSION_MODEL, this.permissionModel);
            outState.putSerializable(BaseIntentConstant.BUNDLE_IS_FIRST_CREATE, Boolean.valueOf(this.isFirstInit));
            outState.putSerializable(BaseIntentConstant.BUNDLE_PAYSTATE_MODEL, getAppApplication().getPayStateModel());
        } catch (Exception e) {
            e.printStackTrace();
//            CrashHandler.getInstance().handleExceptionBySelf(e);
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        this.activity = getActivity();
        this.resource = MCResource.getInstance(getActivity());
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(this.activity.getApplicationContext());
        this.inflater = LayoutInflater.from(this.activity.getApplicationContext());
        this.userManageHelper = UserManageHelper.getInstance(this.activity.getApplicationContext());
        this.imm = (InputMethodManager) this.activity.getSystemService("input_method");
        this.settingModel = this.userManageHelper.getSettingModel();
        this.permissionModel = this.userManageHelper.getPermissionModel();
        this.moduleModel = (ConfigComponentModel) getBundle().getSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
        if (savedInstanceState != null) {
            initSaveInstanceState(savedInstanceState);
        }
    }

    protected void initSaveInstanceState(Bundle savedInstanceState) {
        this.moduleModel = (ConfigComponentModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
        this.settingModel = (SettingModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_SETTING_MODEL);
        this.permissionModel = (PermissionModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_PERMISSION_MODEL);
        this.isFirstInit = savedInstanceState.getBoolean(BaseIntentConstant.BUNDLE_IS_FIRST_CREATE);
        getAppApplication().setPayStateModel((PayStateModel) savedInstanceState.getSerializable(BaseIntentConstant.BUNDLE_PAYSTATE_MODEL));
        this.userManageHelper.setPermissionModel(this.permissionModel);
        this.userManageHelper.setSettingModel(this.settingModel);
    }

    protected View findViewByName(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }

    protected DiscuzApplication getAppApplication() {
        if (getActivity() == null) {
            return DiscuzApplication._instance;
        }
        return (DiscuzApplication) getActivity().getApplication();
    }

    protected void firstCreate() {
        this.isFirstInit = false;
    }

    protected void componentDealTopbar() {
        dealTopBar(createTopSettingModel());
    }

    public void hideSoftKeyboard() {
        if (this.activity.getCurrentFocus() != null) {
            this.imm.hideSoftInputFromWindow(this.activity.getCurrentFocus().getWindowToken(), 2);
        }
    }

    public void showSoftKeyboard() {
        if (this.activity.getCurrentFocus() != null) {
            this.imm.showSoftInput(this.activity.getCurrentFocus(), 1);
        }
    }

    protected FragmentOptHelper getFragmentHelper() {
        if (this.fragmentHelper == null) {
            this.fragmentHelper = new FragmentOptHelper(getChildFragmentManager());
        }
        return this.fragmentHelper;
    }

    protected TopSettingModel createTopSettingModel() {
        TopSettingModel topSettingModel = new TopSettingModel();
        topSettingModel.style = isPop() ? 1 : 0;
        if (this.moduleModel != null) {
            topSettingModel.title = TextUtils.isEmpty(this.moduleModel.getDefaultTitle()) ? this.moduleModel.getTitle() : this.moduleModel.getDefaultTitle();
        }
        return topSettingModel;
    }

    protected void dealTopBar(TopSettingModel topSettingModel) {
        if (this.topBarOptImpl != null && this.topBarOptImpl.getTopBarHelper() != null) {
            this.topBarOptImpl.getTopBarHelper().dealTopBar(topSettingModel);
            registerTopListener(null);
        }
    }

    protected void registerTopListener(OnClickListener topClickListener) {
        if (this.topBarOptImpl != null && this.topBarOptImpl.getTopBarHelper() != null) {
//            this.topBarOptImpl.getTopBarHelper().registerClickListener(new TopClickListener(topClickListener));
        }
    }

    protected TopBarHelper getTopBarHelper() {
        if (this.topBarOptImpl == null || this.topBarOptImpl.getTopBarHelper() == null) {
            return null;
        }
        return this.topBarOptImpl.getTopBarHelper();
    }

    protected boolean isPop() {
        if (getActivity() instanceof BasePopActivity) {
            return true;
        }
        return false;
    }

    protected int dip2px(int dipValue) {
        return MCPhoneUtil.dip2px(this.activity, (float) dipValue);
    }

    public void loadDataByNet() {
    }

    protected void addTask(AsyncTask<?, ?, ?> task) {
        this.taskList.add(task);
    }

    protected void cancelAllTask() {
        for (AsyncTask<?, ?, ?> task : this.taskList) {
            if (!(task == null || task.isCancelled())) {
                task.cancel(true);
            }
        }
    }

    public boolean isChildInteruptBackPress() {
        return false;
    }

    protected Bundle getBundle() {
        if (this.bundle == null) {
            this.bundle = getArguments();
            if (this.bundle == null) {
                this.bundle = new Bundle();
            }
        }
        return this.bundle;
    }

    public MCProgressDialog getLoadingPro() {
        if (this.loadingPro == null) {
            this.loadingPro = new MCProgressDialog(this.activity);
        }
        return this.loadingPro;
    }
}
