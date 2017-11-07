package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.discuz.module.person.activity.fragment.adapter.BaseUserListFragmentAdapter;
import com.mobcent.discuz.module.person.activity.helper.InTimeHelper;
import com.mobcent.discuz.module.person.activity.helper.InTimeHelper.InTimeDelegate;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnBottomRefreshListener;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.android.ui.widget.listener.PausePullListOnScrollListener;
import com.mobcent.lowest.base.utils.MCLocationUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseUserListFragment extends BaseFragment implements FinalConstant, UserConstant {
    protected BaseUserListFragmentAdapter adapter;
    protected ConfigComponentModel componentModel;
    private int currentPage = 1;
    private InTimeDelegate delegate;
    private InTimeHelper inTimeHelper;
    private boolean isLocal = true;
    private double latitude;
    private LoadDataAsyncTask loadDataAsyncTask;
    private MCLocationUtil locationUtil = null;
    private double longitude;
    protected String orderBy;
    private int pageSize = 20;
    private long userId;
    protected List<UserInfoModel> userList;
    protected PullToRefreshListView userListView;
    private UserService userService;
    protected String userType;

    private class LoadDataAsyncTask extends AsyncTask<Void, Void, BaseResultModel<List<UserInfoModel>>> {
        private int requestId;

        public LoadDataAsyncTask(int requestId) {
            this.requestId = requestId;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            switch (this.requestId) {
                case 1:
                    BaseUserListFragment.this.currentPage = 1;
                    return;
                case 2:
                    BaseUserListFragment.this.currentPage = BaseUserListFragment.this.currentPage + 1;
                    return;
                default:
                    BaseUserListFragment.this.currentPage = 1;
                    return;
            }
        }

        protected BaseResultModel<List<UserInfoModel>> doInBackground(Void... arg0) {
            return BaseUserListFragment.this.loadData();
        }

        protected void onPostExecute(BaseResultModel<List<UserInfoModel>> result) {
            super.onPostExecute(result);
            if (this.requestId == 1) {
                BaseUserListFragment.this.userListView.onRefreshComplete();
            }
            DZToastAlertUtils.toast(BaseUserListFragment.this.activity.getApplicationContext(), result);
            if (result != null) {
                if (result.getRs() != 0) {
                    if (result.getData() != null) {
                        switch (this.requestId) {
                            case 1:
                                BaseUserListFragment.this.refreshExecute(result);
                                break;
                            case 2:
                                BaseUserListFragment.this.moreExecute(result);
                                break;
                            default:
                                BaseUserListFragment.this.refreshExecute(result);
                                break;
                        }
                    }
                    BaseUserListFragment.this.adapter.setDatas(BaseUserListFragment.this.userList);
                    if (this.requestId == 1) {
                        BaseUserListFragment.this.adapter.notifyDataSetChanged();
                    }
                    if (result.getHasNext() == 1) {
                        BaseUserListFragment.this.userListView.onBottomRefreshComplete(0);
                    } else {
                        BaseUserListFragment.this.userListView.onBottomRefreshComplete(3);
                    }
                } else if (!MCStringUtil.isEmpty(result.getErrorInfo())) {
                    MCToastUtils.toast(BaseUserListFragment.this.activity.getApplicationContext(), result.getErrorInfo());
                }
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.TAG = "BaseUserFragment";
        this.componentModel = (ConfigComponentModel) getBundle().getSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL);
        this.userType = getBundle().getString("userType");
        this.orderBy = getBundle().getString("orderBy");
        this.userId = getBundle().getLong("userId", 0);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.userList = new ArrayList();
        if (this.userId == 0) {
            this.userId = this.sharedPreferencesDB.getUserId();
        }
        this.inTimeHelper = InTimeHelper.getInstance();
        this.delegate = new InTimeDelegate() {
            public void refresh() {
                if (BaseUserListFragment.this.userId == BaseUserListFragment.this.sharedPreferencesDB.getUserId()) {
                    BaseUserListFragment.this.userListView.onRefresh(false);
                }
            }
        };
        this.inTimeHelper.add(this.delegate);
        if (this.componentModel != null) {
            this.orderBy = this.componentModel.getOrderby();
        }
    }

    protected void initViews(View rootView) {
        this.userListView = (PullToRefreshListView) findViewByName(rootView, "user_list");
        this.userListView.setScrollListener(new PausePullListOnScrollListener(ImageLoader.getInstance(), false, true));
    }

    protected void initActions(View rootView) {
        this.userListView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (BaseUserListFragment.this.userListView.isHand()) {
                    BaseUserListFragment.this.isLocal = false;
                }
                BaseUserListFragment.this.onRefreshEvent();
            }
        });
        this.userListView.setOnBottomRefreshListener(new OnBottomRefreshListener() {
            public void onRefresh() {
                BaseUserListFragment.this.onMoreEvent();
            }
        });
        this.userListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(BaseUserListFragment.this.activity.getApplicationContext(), UserHomeActivity.class);
                intent.putExtra("userId", ((UserInfoModel) BaseUserListFragment.this.userList.get(position - 1)).getUserId());
                BaseUserListFragment.this.activity.startActivity(intent);
            }
        });
        this.userListView.onRefresh(false);
        if (!"distance".equals(this.orderBy)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    BaseUserListFragment.this.loadDataByNet();
                }
            }, 1000);
        }
    }


    private void updateLocationFail() {
        this.userList.clear();
        this.adapter.setDatas(this.userList);
        this.userListView.setAdapter(this.adapter);
        this.userListView.onRefreshComplete();
        this.userListView.onBottomRefreshComplete(3);
        MCToastUtils.toastByResName(this.activity.getApplicationContext(), "mc_forum_un_obtain_location_info_warn");
    }

    private void onRefreshEvent() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(1);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    private void onMoreEvent() {
        this.loadDataAsyncTask = new LoadDataAsyncTask(2);
        this.loadDataAsyncTask.execute(new Void[0]);
    }

    private void refreshExecute(BaseResultModel<List<UserInfoModel>> result) {
        this.userList.clear();
        this.userList.addAll((Collection) result.getData());
    }

    private void moreExecute(BaseResultModel<List<UserInfoModel>> result) {
        this.userList.addAll((Collection) result.getData());
    }

    private BaseResultModel<List<UserInfoModel>> loadData() {
//        BDLocation location = SharedPreferencesDB.getInstance(this.activity.getApplicationContext()).getLocation();
//        if (location != null) {
//            this.longitude = location.getLongitude();
//            this.latitude = location.getLatitude();
//        }
        return this.userService.getUserList(this.userId, this.currentPage, this.pageSize, this.userType, this.isLocal, this.orderBy, this.longitude, this.latitude);
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.loadDataAsyncTask != null) {
            this.loadDataAsyncTask.cancel(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.inTimeHelper.remove(this.delegate);
    }

    protected void componentDealTopbar() {
        super.componentDealTopbar();
        TopSettingModel topSettingModel = createTopSettingModel();
        String title = null;
        if ("all".equals(this.userType)) {
            title = this.resource.getString("mc_forum_surround_user");
        } else if ("recommend".equals(this.userType)) {
            title = this.resource.getString("mc_forum_recommend_users");
        } else if (UserConstant.USER_FAN.equals(this.userType)) {
            title = this.resource.getString("mc_forum_user_fan");
        } else if ("follow".equals(this.userType)) {
            title = this.resource.getString("mc_forum_user_follow");
        } else if ("friend".equals(this.userType)) {
            title = this.resource.getString("mc_forum_friend");
        }
        if (!(this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle()))) {
            title = this.moduleModel.getTitle();
        }
        topSettingModel.title = title;
        dealTopBar(topSettingModel);
    }

    public void loadDataByNet() {
        this.isLocal = false;
        if (this.userListView != null) {
            this.userListView.onRefresh();
        }
    }
}
