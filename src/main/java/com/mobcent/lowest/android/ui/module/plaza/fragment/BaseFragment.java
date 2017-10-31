package com.mobcent.lowest.android.ui.module.plaza.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.base.model.BaseModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragment extends Fragment implements PlazaConstant {
    protected String TAG;
    protected Activity activity;
    protected String appKey;
    protected LayoutInflater inflater;
    private boolean isCache;
    private boolean isCreateView;
    private boolean isLocal;
    protected List<AsyncTask<?, ?, ?>> loadDataAsyncTasks;
    protected Handler mHandler;
    protected MCResource mcResource;
    protected ProgressDialog myDialog;
    protected int page;
    protected int pageSize;
    protected View view;

    protected abstract void initData();

    protected abstract View initViews(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    protected abstract void initWidgetActions();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.TAG = toString();
        this.loadDataAsyncTasks = new ArrayList();
        this.mcResource = MCResource.getInstance(getActivity());
        this.mHandler = new Handler();
        this.activity = getActivity();
        this.inflater = LayoutInflater.from(getActivity());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        this.view = initViews(inflater, container, savedInstanceState);
        initWidgetActions();
        return this.view;
    }

    protected View findViewByName(View parent, String name) {
        return parent.findViewById(this.mcResource.getViewId(name));
    }

    public void warnMessageById(String warnMessId) {
        Toast.makeText(getActivity(), this.mcResource.getStringId(warnMessId), 0).show();
    }

    public void warnMessageByStr(String str) {
        Toast.makeText(getActivity(), str, 0).show();
    }

    protected static int checkGetData(boolean isRefresh, Object object, boolean isCreateView) {
        List<Object> objects = null;
        if (object != null) {
            objects = new ArrayList();
            objects.add(object);
        }
        return checkGetData(isRefresh, objects, isCreateView);
    }

    protected boolean isServerMode(List<BaseModel> objects) {
        if (objects == null || objects.isEmpty() || !MCStringUtil.isEmpty(((BaseModel) objects.get(0)).getErrorCode())) {
            return false;
        }
        return true;
    }

    protected boolean isServerMode(BaseModel object) {
        if (object == null || !MCStringUtil.isEmpty(object.getErrorCode())) {
            return false;
        }
        return true;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        clearAsyncTask();
    }

    private void clearAsyncTask() {
        int size = this.loadDataAsyncTasks.size();
        for (int i = 0; i < size; i++) {
            if (this.loadDataAsyncTasks.get(i) != null) {
                ((AsyncTask) this.loadDataAsyncTasks.get(i)).cancel(true);
            }
        }
        this.loadDataAsyncTasks.clear();
    }

    public void addAsyncTask(AsyncTask<?, ?, ?> asyncTask) {
        this.loadDataAsyncTasks.add(asyncTask);
    }

    protected void setDBMode(boolean isLocal) {
        this.isLocal = isLocal;
    }

    protected boolean getDBMode() {
        return this.isLocal;
    }

    protected void setMemoryMode(boolean isCache) {
        this.isCache = isCache;
    }

    protected boolean getMemory() {
        return this.isCache;
    }

    protected void setCreateView(boolean isCreateView) {
        this.isCreateView = isCreateView;
    }

    protected boolean getCreateView() {
        return this.isCreateView;
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
