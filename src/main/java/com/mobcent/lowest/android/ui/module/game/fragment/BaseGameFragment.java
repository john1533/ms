package com.mobcent.lowest.android.ui.module.game.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.game.cache.GameDataCache;
import com.mobcent.lowest.android.ui.module.game.constant.GameConstance;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGameFragment extends Fragment implements GameConstance, GameApiConstant {
    protected Activity activity;
    protected Bundle bundle;
    protected Context context;
    protected GameDataCache gameDataCache;
    protected Handler mHandler;
    protected MCResource mcResource;
    protected List<AsyncTask<?, ?, ?>> taskList;
    protected Toast toast;

    protected abstract void initData();

    protected abstract View initView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    protected abstract void initWidgetActions();

    public abstract void loadDataByNet();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        this.context = this.activity.getApplicationContext();
        this.mcResource = MCResource.getInstance(this.activity);
        this.mHandler = new Handler();
        this.toast = new Toast(this.activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivityArguments();
        initData();
        View view = initView(inflater, container, savedInstanceState);
        initWidgetActions();
        return view;
    }

    private void getActivityArguments() {
        this.bundle = getArguments();
        this.gameDataCache = GameDataCache.getInstance();
    }

    protected void addAsyncTask(AsyncTask<?, ?, ?> asyncTask) {
        if (this.taskList == null) {
            this.taskList = new ArrayList();
        }
        this.taskList.add(asyncTask);
    }

    protected void clearAsyncTask() {
        if (this.taskList != null) {
            int size = this.taskList.size();
            for (int i = 0; i < size; i++) {
                ((AsyncTask) this.taskList.get(i)).cancel(true);
            }
            this.taskList.clear();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        clearAsyncTask();
    }

    public void showMessage(String msg) {
        Toast toast = this.toast;
        Toast.makeText(this.activity, msg, 0).show();
    }
}
