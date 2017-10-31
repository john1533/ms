package com.mobcent.discuz.module.publish.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.publish.adapter.MentionFriendAdapter;
import com.mobcent.discuz.module.publish.delegate.MentionFriendReturnDelegate;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MentionFriendFragment extends BaseFragment {
    private ListView listView;
    private ProgressDialog loadFriendDialog;
    private Handler mHandler;
    private MentionFriendTask mentionFriendTask;
    private MentionFriendAdapter metionFriendAdapter;
    private TextView noMentionText;
    private List<UserInfoModel> postsUserList;
    private List<UserInfoModel> userInfoModels;

    private class MentionFriendTask extends AsyncTask<Boolean, Void, BaseResultModel<List<UserInfoModel>>> {
        private MentionFriendTask() {
        }

        protected BaseResultModel<List<UserInfoModel>> doInBackground(Boolean... params) {
            UserService userService = new UserServiceImpl(MentionFriendFragment.this.activity);
            long currentUserId = MentionFriendFragment.this.sharedPreferencesDB.getUserId();
            Boolean isLocal = params[0];
            MentionFriendFragment.this.mHandler.post(new Runnable() {
                public void run() {
                    MentionFriendFragment.this.loadFriendDialog = ProgressDialog.show(MentionFriendFragment.this.activity, MentionFriendFragment.this.activity.getApplicationContext().getResources().getString(MentionFriendFragment.this.resource.getStringId("mc_forum_please_wait")), MentionFriendFragment.this.activity.getResources().getString(MentionFriendFragment.this.resource.getStringId("mc_forum_loading")), true);
                }
            });
            return userService.getMentionFriend(currentUserId, isLocal.booleanValue());
        }

        protected void onPostExecute(BaseResultModel<List<UserInfoModel>> result) {
            super.onPostExecute(result);
            if (MentionFriendFragment.this.loadFriendDialog.isShowing()) {
                MentionFriendFragment.this.loadFriendDialog.dismiss();
            }
            List<UserInfoModel> tempList = new ArrayList();
            if (result != null) {
                if (!MCStringUtil.isEmpty(result.getErrorInfo())) {
                    Toast.makeText(MentionFriendFragment.this.activity.getApplicationContext(), result.getErrorInfo(), 0).show();
                } else if (result.getData() != null) {
                    tempList.addAll((Collection) result.getData());
                    MentionFriendFragment.this.noMentionText.setVisibility(8);
                }
            }
            if (MentionFriendFragment.this.userInfoModels != null && MentionFriendFragment.this.userInfoModels.size() > 0) {
                UserInfoModel user = new UserInfoModel();
                user.setUserId(-1);
                user.setRoleNum(-1);
                tempList.add(user);
                tempList.addAll(MentionFriendFragment.this.userInfoModels);
            }
            MentionFriendFragment.this.userInfoModels = tempList;
            MentionFriendFragment.this.listView.setAdapter(MentionFriendFragment.this.metionFriendAdapter);
            MentionFriendFragment.this.metionFriendAdapter.setUserInfoList(tempList);
            MentionFriendFragment.this.metionFriendAdapter.notifyDataSetInvalidated();
            MentionFriendFragment.this.metionFriendAdapter.notifyDataSetChanged();
            if (MentionFriendFragment.this.userInfoModels == null || MentionFriendFragment.this.userInfoModels.size() == 0) {
                MentionFriendFragment.this.noMentionText.setVisibility(0);
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userInfoModels = new ArrayList();
        this.postsUserList = new ArrayList();
        this.postsUserList = (List) getBundle().getSerializable(FinalConstant.POSTS_USER_LIST);
        this.mHandler = new Handler();
        if (this.postsUserList != null) {
            this.userInfoModels.addAll(this.postsUserList);
        }
        if (this.metionFriendAdapter == null) {
            this.metionFriendAdapter = new MentionFriendAdapter(this.activity.getApplicationContext());
        }
        this.mentionFriendTask = new MentionFriendTask();
        this.mentionFriendTask.execute(new Boolean[]{Boolean.valueOf(true)});
    }

    protected String getRootLayoutName() {
        return "mention_friend_fragment";
    }

    protected void initViews(View rootView) {
        this.listView = (ListView) findViewByName(rootView, "mc_forum_mention_friend_list");
        this.noMentionText = (TextView) findViewByName(rootView, "mc_forum_no_mention_friend_Text");
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_mention_friend");
        dealTopBar(topSettingModel);
    }

    protected void initActions(View rootView) {
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserInfoModel mentionFriend = (UserInfoModel) MentionFriendFragment.this.userInfoModels.get(position);
                if (MentionFriendReturnDelegate.getInstance().getOnMentionChannelListener() != null) {
                    MentionFriendReturnDelegate.getInstance().getOnMentionChannelListener().changeMentionFriend(mentionFriend);
                }
                MentionFriendFragment.this.activity.finish();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mentionFriendTask != null) {
            this.mentionFriendTask.cancel(true);
        }
    }
}
