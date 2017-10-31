package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.model.UserProfileModel;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.UserInfoAsynTask;
import com.mobcent.discuz.module.person.activity.fragment.adapter.UserProfileAdapter;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends BaseFragment {
    private int UPDATE = 1;
    private UserProfileAdapter adapter;
    private ExpandableListView listView;
    private List<UserProfileModel> profileList;
    private UserInfoModel userInfoModel;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userInfoModel = (UserInfoModel) getBundle().getSerializable(IntentConstant.INTENT_USER_INFO);
        this.profileList = getProfileList();
    }

    private List<UserProfileModel> getProfileList() {
        List<UserProfileModel> list = new ArrayList();
        UserProfileModel model = new UserProfileModel();
        model.setUserProfileModels(this.userInfoModel.getUserProfileList());
        list.add(model);
        UserProfileModel model1 = new UserProfileModel();
        model1.setUserProfileModels(this.userInfoModel.getCreditList());
        list.add(model1);
        return list;
    }

    protected String getRootLayoutName() {
        return "user_profile_fragment";
    }

    private void expandAllView() {
        int groupCount = this.adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            this.listView.expandGroup(i);
        }
    }

    protected void initViews(View rootView) {
        this.listView = (ExpandableListView) findViewByName(rootView, "list_view");
        this.adapter = new UserProfileAdapter(this.activity.getApplicationContext(), this.profileList);
        this.listView.setAdapter(this.adapter);
        this.listView.setGroupIndicator(null);
        this.mHandler.post(new Runnable() {
            public void run() {
                UserProfileFragment.this.adapter.notifyDataSetChanged();
                UserProfileFragment.this.expandAllView();
            }
        });
    }

    protected void initActions(View rootView) {
    }

    public void onResume() {
        onRefrsh();
        super.onResume();
    }

    private void onRefrsh() {
        if (this.userInfoModel.getUserId() == this.sharedPreferencesDB.getUserId()) {
            new UserInfoAsynTask(this.activity, new BaseRequestCallback<BaseResultModel<UserInfoModel>>() {
                public void onPreExecute() {
                }

                public void onPostExecute(BaseResultModel<UserInfoModel> result) {
                    if (result.getRs() == 1) {
                        UserProfileFragment.this.userInfoModel = (UserInfoModel) result.getData();
                        UserProfileFragment.this.profileList = UserProfileFragment.this.getProfileList();
                        UserProfileFragment.this.adapter.setUserProfileModels(UserProfileFragment.this.profileList);
                        UserProfileFragment.this.adapter.notifyDataSetChanged();
                    }
                }
            }, this.userInfoModel.getUserId()).execute(new Void[0]);
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        topSettingModel.title = this.resource.getString("mc_forum_user_profile");
        if (this.userInfoModel.getUserId() == this.sharedPreferencesDB.getUserId()) {
            List<TopBtnModel> rightModels = new ArrayList();
            TopBtnModel topBtnModel = new TopBtnModel();
            topBtnModel.icon = "mc_forum_top_bar_button2";
            topBtnModel.title = this.resource.getString("mc_forum_userifo_update");
            topBtnModel.action = this.UPDATE;
            rightModels.add(topBtnModel);
            topSettingModel.rightModels = rightModels;
        }
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action == UserProfileFragment.this.UPDATE) {
                    Intent intent = new Intent(UserProfileFragment.this.activity, WebViewFragmentActivity.class);
                    String url = UserProfileFragment.this.resource.getString("mc_discuz_base_request_url") + UserProfileFragment.this.resource.getString("mc_forum_request_userinfo_adminview") + "&accessToken=" + UserProfileFragment.this.sharedPreferencesDB.getAccessToken() + "&accessSecret=" + UserProfileFragment.this.sharedPreferencesDB.getAccessSecret() + "&sdkVersion=" + BaseApiConstant.SDK_VERSION_VALUE + "&apphash=" + MCStringUtil.stringToMD5((System.currentTimeMillis() + "").substring(0, 5) + BaseApiConstant.AUTHKEY).substring(8, 16);
                    intent.putExtra("webViewUrl", url);
                    MCLogUtil.e("test", "url==" + url);
                    UserProfileFragment.this.activity.startActivity(intent);
                }
            }
        });
    }
}
