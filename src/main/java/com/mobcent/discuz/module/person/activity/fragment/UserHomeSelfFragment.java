package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.HomeActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.activity.view.ScaleHeaderScrollView;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.android.user.helper.UserManageHelper.ChangeUserInfoListener;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.msg.fragment.activity.SessionListActivity;
import com.mobcent.discuz.module.person.activity.UserListActivity;
import com.mobcent.discuz.module.person.activity.UserMyInfoActivity;
import com.mobcent.discuz.module.person.activity.UserPhotosActivity;
import com.mobcent.discuz.module.person.activity.UserProfileActivity;
import com.mobcent.discuz.module.person.activity.UserSettingActivity;
import com.mobcent.discuz.module.topic.list.fragment.activity.UserTopicListActivity;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;

public class UserHomeSelfFragment extends BaseUserHomeFragment implements ChangeUserInfoListener {
    private Button backBtn;
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v == UserHomeSelfFragment.this.backBtn) {
                UserHomeSelfFragment.this.activity.finish();
            } else if (!LoginHelper.doInterceptor(UserHomeSelfFragment.this.activity, null, null)) {
            } else {
                Intent intent;
                if (v == UserHomeSelfFragment.this.favoritesBox) {
                    if (UserHomeSelfFragment.this.currentUserInfo != null) {
                        intent = new Intent(UserHomeSelfFragment.this.activity.getApplicationContext(), UserTopicListActivity.class);
                        intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                        intent.putExtra(PostsConstant.TOPIC_TYPE, "favorite");
                        UserHomeSelfFragment.this.startActivity(intent);
                    }
                } else if (v == UserHomeSelfFragment.this.manageUserBtn) {
                    UserHomeSelfFragment.this.showSwitchUserDialog();
                } else if (v == UserHomeSelfFragment.this.settingBox) {
                    UserHomeSelfFragment.this.startActivity(new Intent(UserHomeSelfFragment.this.activity, UserSettingActivity.class));
                } else if (v == UserHomeSelfFragment.this.titleText) {
                    UserHomeSelfFragment.this.refreshUserInfo();
                } else if (v == UserHomeSelfFragment.this.iconImg) {
                    UserHomeSelfFragment.this.startActivityForResult(new Intent(UserHomeSelfFragment.this.activity.getApplicationContext(), UserMyInfoActivity.class), 100);
                } else if (v == UserHomeSelfFragment.this.photoBox) {
                    intent = new Intent(UserHomeSelfFragment.this.activity, UserPhotosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtras(bundle);
                    UserHomeSelfFragment.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment.this.followBox) {
                    intent = new Intent(UserHomeSelfFragment.this.activity, UserListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtra("userType", "follow");
                    intent.putExtra("orderBy", "dateline");
                    UserHomeSelfFragment.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment.this.fansBox) {
                    intent = new Intent(UserHomeSelfFragment.this.activity, UserListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtra("userType", UserConstant.USER_FAN);
                    intent.putExtra("orderBy", "dateline");
                    UserHomeSelfFragment.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment.this.publishBox) {
                    intent = new Intent(UserHomeSelfFragment.this.activity.getApplicationContext(), UserTopicListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "topic");
                    UserHomeSelfFragment.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment.this.replyBox) {
                    intent = new Intent(UserHomeSelfFragment.this.activity, UserTopicListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "reply");
                    UserHomeSelfFragment.this.activity.startActivity(intent);
                } else if (v == UserHomeSelfFragment.this.messageBox) {
                    intent = new Intent(UserHomeSelfFragment.this.activity, SessionListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "reply");
                    UserHomeSelfFragment.this.activity.startActivity(intent);
                } else if (v == UserHomeSelfFragment.this.infomationBox) {
                    if (UserHomeSelfFragment.this.currentUserInfo != null) {
                        intent = new Intent(UserHomeSelfFragment.this.activity, UserProfileActivity.class);
                        intent.putExtra(IntentConstant.INTENT_USER_INFO, UserHomeSelfFragment.this.currentUserInfo);
                        UserHomeSelfFragment.this.activity.startActivity(intent);
                    }
                } else if (v == UserHomeSelfFragment.this.friendBox && UserHomeSelfFragment.this.currentUserInfo != null) {
                    intent = new Intent(UserHomeSelfFragment.this.activity, UserListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment.this.currentUserId);
                    intent.putExtra("userType", "friend");
                    intent.putExtra("orderBy", "dateline");
                    UserHomeSelfFragment.this.activity.startActivity(intent);
                }
            }
        }
    };
    private TextView creditsText;
    private LinearLayout fansBox;
    private LinearLayout favoritesBox;
    private LinearLayout followBox;
    private LinearLayout friendBox;
    private TextView genderText;
    private MCHeadIcon iconImg;
    private LinearLayout infomationBox;
    private boolean isOnHome = false;
    private boolean isShowMessageList = false;
    private TextView levelNameText;
    private Button manageUserBtn;
    private LinearLayout messageBox;
    private ScaleHeaderScrollView myScrollView;
    private TextView nicknameText;
    private LinearLayout photoBox;
    private LinearLayout publishBox;
    private LinearLayout replyBox;
    private LinearLayout settingBox;
    private TextView titleText;

    protected String getRootLayoutName() {
        return "user_home_self_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.isOnHome = this.activity instanceof HomeActivity;
        if (getArguments() != null) {
            this.isShowMessageList = getArguments().getBoolean(BaseIntentConstant.BUNDLE_IS_SHOW_MESSAGELIST);
        }
    }

    protected void initViews(View rootView) {
        this.backBtn = (Button) findViewByName(rootView, "back_btn");
        if (this.isOnHome) {
            this.backBtn.setVisibility(4);
            UserManageHelper.getInstance(this.activity.getApplicationContext()).registListener(this);
        } else {
            this.backBtn.setVisibility(0);
        }
        this.manageUserBtn = (Button) findViewByName(rootView, "more_btn");
        MCTouchUtil.createTouchDelegate(this.backBtn, 10);
        MCTouchUtil.createTouchDelegate(this.manageUserBtn, 10);
        this.titleText = (TextView) findViewByName(rootView, "title_text");
        this.myScrollView = (ScaleHeaderScrollView) findViewByName(rootView, "mc_forum_my_view");
        this.iconImg = (MCHeadIcon) findViewByName(rootView, "mc_forum_user_icon");
        this.nicknameText = (TextView) findViewByName(rootView, "mc_forum_user_name");
        this.genderText = (TextView) findViewByName(rootView, "mc_forum_user_gender");
        this.creditsText = (TextView) findViewByName(rootView, "mc_froum_user_credits");
        this.infomationBox = (LinearLayout) findViewByName(rootView, "mc_forum_infomation_layout");
        this.messageBox = (LinearLayout) findViewByName(rootView, "mc_forum_message_layout");
        this.followBox = (LinearLayout) findViewByName(rootView, "mc_forum_follow_layout");
        this.fansBox = (LinearLayout) findViewByName(rootView, "mc_forum_fan_layout");
        this.photoBox = (LinearLayout) findViewByName(rootView, "mc_forum_photo_layout");
        this.publishBox = (LinearLayout) findViewByName(rootView, "mc_forum_publish_layout");
        this.replyBox = (LinearLayout) findViewByName(rootView, "mc_forum_reply_layout");
        this.favoritesBox = (LinearLayout) findViewByName(rootView, "mc_forum_favorites_layout");
        this.settingBox = (LinearLayout) findViewByName(rootView, "mc_forum_setting_box");
        this.friendBox = (LinearLayout) findViewByName(rootView, "mc_forum_friend_layout");
        this.levelNameText = (TextView) findViewByName(rootView, "mc_froum_user_level_text");
        this.myScrollView.scaleHeaderImage((ImageView) findViewByName(this.myScrollView, "mc_forum_personal_bg"));
        this.titleText.setText(this.resource.getString("mc_forum_more_info"));
        if (this.isShowMessageList) {
            findViewByName(rootView, "message_line").setVisibility(0);
            this.messageBox.setVisibility(0);
            return;
        }
        findViewByName(rootView, "message_line").setVisibility(8);
        this.messageBox.setVisibility(8);
    }

    protected void initActions(View rootView) {
        this.favoritesBox.setOnClickListener(this.clickListener);
        this.manageUserBtn.setOnClickListener(this.clickListener);
        this.settingBox.setOnClickListener(this.clickListener);
        this.titleText.setOnClickListener(this.clickListener);
        this.backBtn.setOnClickListener(this.clickListener);
        this.iconImg.setOnClickListener(this.clickListener);
        this.photoBox.setOnClickListener(this.clickListener);
        this.followBox.setOnClickListener(this.clickListener);
        this.fansBox.setOnClickListener(this.clickListener);
        this.publishBox.setOnClickListener(this.clickListener);
        this.replyBox.setOnClickListener(this.clickListener);
        this.messageBox.setOnClickListener(this.clickListener);
        this.infomationBox.setOnClickListener(this.clickListener);
        this.friendBox.setOnClickListener(this.clickListener);
        refreshUserInfo();
    }

    protected void refreshUserInfo() {
        this.currentUserId = this.sharedPreferencesDB.getUserId();
        super.refreshUserInfo();
    }

    protected void onUserInfoResult(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            clearAllViewState();
            return;
        }
        if (TextUtils.isEmpty(userInfoModel.getCreditInfo())) {
            this.creditsText.setVisibility(8);
        } else {
            this.creditsText.setVisibility(0);
            this.creditsText.setText(userInfoModel.getCreditInfo());
        }
        this.levelNameText.setText(userInfoModel.getLevelName());
        this.nicknameText.setText(userInfoModel.getNickname());
        updateGender(userInfoModel);
        loadIcon(this.iconImg, userInfoModel);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.myScrollView.setBmp(null);
    }

    private void updateGender(UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            if (userInfoModel.getGender() == 1) {
                this.genderText.setText(this.resource.getStringId("mc_forum_user_gender_man"));
                this.genderText.setTextColor(this.activity.getResources().getColor(this.resource.getColorId("mc_forum_user_boy_color")));
            } else if (userInfoModel.getGender() == 0) {
                this.genderText.setText(this.resource.getStringId("mc_forum_gender_secrecy"));
                this.genderText.setTextColor(this.activity.getResources().getColor(this.resource.getColorId("mc_forum_text6_normal_color")));
            } else {
                this.genderText.setText(this.resource.getStringId("mc_forum_user_gender_woman"));
                this.genderText.setTextColor(this.activity.getResources().getColor(this.resource.getColorId("mc_forum_user_girl_color")));
            }
        }
    }

    private void clearAllViewState() {
        this.currentUserId = 0;
        this.iconImg.setImageBitmap(null);
        this.nicknameText.setText("");
        this.genderText.setText("");
        this.creditsText.setText("");
        this.levelNameText.setText("");
    }

    public void change(final boolean isLogin, final UserInfoModel userInfo) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (!isLogin) {
                    UserHomeSelfFragment.this.clearAllViewState();
                } else if (UserHomeSelfFragment.this.currentUserId != userInfo.getUserId()) {
                    UserHomeSelfFragment.this.currentUserId = UserHomeSelfFragment.this.sharedPreferencesDB.getUserId();
                    UserHomeSelfFragment.this.refreshUserInfo();
                }
            }
        });
    }
}
