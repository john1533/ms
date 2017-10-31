package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.HomeActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.android.user.helper.UserManageHelper.ChangeUserInfoListener;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.draft.activity.DraftActivity;
import com.mobcent.discuz.module.msg.fragment.activity.SessionListActivity;
import com.mobcent.discuz.module.person.activity.UserListActivity;
import com.mobcent.discuz.module.person.activity.UserMyInfoActivity;
import com.mobcent.discuz.module.person.activity.UserMyPwdActivity;
import com.mobcent.discuz.module.person.activity.UserPhotosActivity;
import com.mobcent.discuz.module.person.activity.UserProfileActivity;
import com.mobcent.discuz.module.person.activity.UserSettingActivity;
import com.mobcent.discuz.module.topic.list.fragment.activity.UserTopicListActivity;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;

public class UserHomeSelfFragment1 extends BaseUserHomeFragment implements ChangeUserInfoListener {
//    private Button backBtn;
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
//            if (v == UserHomeSelfFragment1.this.backBtn) {
//                UserHomeSelfFragment1.this.activity.finish();
//            } else
                if (!LoginHelper.doInterceptor(UserHomeSelfFragment1.this.activity, null, null)) {
            } else {
                Intent intent;
                if (v == UserHomeSelfFragment1.this.favoritesBox) {
                    if (UserHomeSelfFragment1.this.currentUserInfo != null) {
                        intent = new Intent(UserHomeSelfFragment1.this.activity.getApplicationContext(), UserTopicListActivity.class);
                        intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                        intent.putExtra(PostsConstant.TOPIC_TYPE, "favorite");
                        UserHomeSelfFragment1.this.startActivity(intent);
                    }
                }
//                else if (v == UserHomeSelfFragment1.this.manageUserBtn) {
//                    UserHomeSelfFragment1.this.showSwitchUserDialog();
//                }
                else if (v == UserHomeSelfFragment1.this.settingBox) {
                    UserHomeSelfFragment1.this.startActivity(new Intent(UserHomeSelfFragment1.this.activity, UserSettingActivity.class));
                }
//                else if (v == UserHomeSelfFragment1.this.titleText) {
//                    UserHomeSelfFragment1.this.refreshUserInfo();
//                }
                else if (v == UserHomeSelfFragment1.this.iconImg) {
                    UserHomeSelfFragment1.this.startActivityForResult(new Intent(UserHomeSelfFragment1.this.activity.getApplicationContext(), UserMyInfoActivity.class), 100);
                } else if (v == UserHomeSelfFragment1.this.photoBox) {
                    intent = new Intent(UserHomeSelfFragment1.this.activity, UserPhotosActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", UserHomeSelfFragment1.this.currentUserId);
                    intent.putExtras(bundle);
                    UserHomeSelfFragment1.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment1.this.followBox) {
                    intent = new Intent(UserHomeSelfFragment1.this.activity, UserListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                    intent.putExtra("userType", "follow");
                    intent.putExtra("orderBy", "dateline");
                    UserHomeSelfFragment1.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment1.this.fansBox) {
                    intent = new Intent(UserHomeSelfFragment1.this.activity, UserListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                    intent.putExtra("userType", UserConstant.USER_FAN);
                    intent.putExtra("orderBy", "dateline");
                    UserHomeSelfFragment1.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment1.this.publishBox) {
                    intent = new Intent(UserHomeSelfFragment1.this.activity.getApplicationContext(), UserTopicListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "topic");
                    UserHomeSelfFragment1.this.startActivity(intent);
                } else if (v == UserHomeSelfFragment1.this.replyBox) {
                    intent = new Intent(UserHomeSelfFragment1.this.activity, UserTopicListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "reply");
                    UserHomeSelfFragment1.this.activity.startActivity(intent);
                } else if (v == UserHomeSelfFragment1.this.messageBox) {
                    intent = new Intent(UserHomeSelfFragment1.this.activity, SessionListActivity.class);
                    intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                    intent.putExtra(PostsConstant.TOPIC_TYPE, "reply");
                    UserHomeSelfFragment1.this.activity.startActivity(intent);
                } else if (v == UserHomeSelfFragment1.this.infomationBox) {
                    if (UserHomeSelfFragment1.this.currentUserInfo != null) {
                        intent = new Intent(UserHomeSelfFragment1.this.activity, UserProfileActivity.class);
                        intent.putExtra(IntentConstant.INTENT_USER_INFO, UserHomeSelfFragment1.this.currentUserInfo);
                        UserHomeSelfFragment1.this.activity.startActivity(intent);
                    }
                } else if (v == UserHomeSelfFragment1.this.headBox) {
                    UserHomeSelfFragment1.this.iconImg.performClick();
                } else if (v == UserHomeSelfFragment1.this.friendBox) {
                    if (UserHomeSelfFragment1.this.currentUserInfo != null) {
                        intent = new Intent(UserHomeSelfFragment1.this.activity, UserListActivity.class);
                        intent.putExtra("userId", UserHomeSelfFragment1.this.currentUserId);
                        intent.putExtra("userType", "friend");
                        intent.putExtra("orderBy", "dateline");
                        UserHomeSelfFragment1.this.activity.startActivity(intent);
                    }
                } else if (v == UserHomeSelfFragment1.this.draftBox) {
                    UserHomeSelfFragment1.this.activity.startActivity(new Intent(UserHomeSelfFragment1.this.activity, DraftActivity.class));
                }
                else if (v == UserHomeSelfFragment1.this.passwordSettingBox) {
                    UserHomeSelfFragment1.this.startActivity(new Intent(UserHomeSelfFragment1.this.activity, UserMyPwdActivity.class));
                }
            }
        }
    };
    private TextView creditsText;
    private LinearLayout draftBox;
    private LinearLayout fansBox;
    private LinearLayout favoritesBox;
    private LinearLayout followBox;
    private LinearLayout friendBox;
    private TextView genderText;
    private RelativeLayout headBox;
    private MCHeadIcon iconImg;
    private LinearLayout infomationBox;
    private boolean isOnHome = false;
    private boolean isShowMessageList = false;
    private TextView levelNameText;
//    private Button manageUserBtn;
    private LinearLayout messageBox;
    private TextView nicknameText;
    private LinearLayout photoBox;
    private LinearLayout publishBox;
    private LinearLayout replyBox;
    private LinearLayout settingBox;
//    private TextView titleText;
    private LinearLayout passwordSettingBox;

    protected String getRootLayoutName() {
        return "user_home_self_fragment1";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        setHasOptionsMenu(true);
        this.isOnHome = this.activity instanceof HomeActivity;
        if (getArguments() != null) {
            this.isShowMessageList = getArguments().getBoolean(BaseIntentConstant.BUNDLE_IS_SHOW_MESSAGELIST);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("注销").setShowAsAction(SHOW_AS_ACTION_ALWAYS);

//        menu.add("注销").setIcon(resource.getDrawableId("mc_forum_top_bar_button5"));
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        item.getTitle()
//        this.showSwitchUserDialog();
        this.logoutUser();
        return true;
    }


    protected void initViews(View rootView) {
//        this.backBtn = (Button) findViewByName(rootView, "back_btn");
        if (this.isOnHome) {
//            this.backBtn.setVisibility(4);
            UserManageHelper.getInstance(this.activity.getApplicationContext()).registListener(this);
        } else {
//            this.backBtn.setVisibility(0);
        }
//        this.manageUserBtn = (Button) findViewByName(rootView, "more_btn");
//        MCTouchUtil.createTouchDelegate(this.backBtn, 10);
//        MCTouchUtil.createTouchDelegate(this.manageUserBtn, 10);
//        this.titleText = (TextView) findViewByName(rootView, "title_text");
//        if (!(this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle()))) {
//            this.titleText.setText(this.moduleModel.getTitle());
//        }
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
        this.draftBox = (LinearLayout) findViewByName(rootView, "mc_forum_draft_layout");
        this.levelNameText = (TextView) findViewByName(rootView, "mc_froum_user_level_text");
        this.headBox = (RelativeLayout) findViewByName(rootView, "head_box");
//        this.titleText.setText(this.resource.getString("mc_forum_more_info"));
//        if (!(this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle()))) {
//            this.titleText.setText(this.moduleModel.getTitle());
//        }
        if (this.isShowMessageList) {
            findViewByName(rootView, "message_line").setVisibility(0);
            this.messageBox.setVisibility(0);
            return;
        }
        findViewByName(rootView, "message_line").setVisibility(8);
        this.messageBox.setVisibility(8);
        passwordSettingBox = (LinearLayout) findViewByName(rootView,"mc_forum_password_setting_box");
    }

    protected void initActions(View rootView) {
        this.favoritesBox.setOnClickListener(this.clickListener);
//        this.manageUserBtn.setOnClickListener(this.clickListener);
        this.settingBox.setOnClickListener(this.clickListener);
//        this.titleText.setOnClickListener(this.clickListener);
//        this.backBtn.setOnClickListener(this.clickListener);
        this.iconImg.setOnClickListener(this.clickListener);
        this.photoBox.setOnClickListener(this.clickListener);
        this.followBox.setOnClickListener(this.clickListener);
        this.fansBox.setOnClickListener(this.clickListener);
        this.publishBox.setOnClickListener(this.clickListener);
        this.replyBox.setOnClickListener(this.clickListener);
        this.messageBox.setOnClickListener(this.clickListener);
        this.infomationBox.setOnClickListener(this.clickListener);
        this.friendBox.setOnClickListener(this.clickListener);
        this.headBox.setOnClickListener(this.clickListener);
        this.draftBox.setOnClickListener(this.clickListener);
        passwordSettingBox.setOnClickListener(clickListener);
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
    }

    private void updateGender(UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            if (userInfoModel.getGender() == 1) {
                this.genderText.setVisibility(0);
                this.genderText.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon29_n"));
            } else if (userInfoModel.getGender() == 0) {
                this.genderText.setVisibility(8);
            } else {
                this.genderText.setVisibility(0);
                this.genderText.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon28_n"));
            }
        }
    }

    private void clearAllViewState() {
        this.currentUserId = 0;
        this.iconImg.setBackgroundDrawable(null);
        this.iconImg.setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.activity));
        this.nicknameText.setText("");
        this.genderText.setText("");
        this.creditsText.setText("");
        this.levelNameText.setText("");
    }

    public void change(final boolean isLogin, final UserInfoModel userInfo) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (!isLogin) {
                    UserHomeSelfFragment1.this.clearAllViewState();
                } else if (UserHomeSelfFragment1.this.currentUserId != userInfo.getUserId()) {
                    UserHomeSelfFragment1.this.currentUserId = UserHomeSelfFragment1.this.sharedPreferencesDB.getUserId();
                    UserHomeSelfFragment1.this.refreshUserInfo();
                }
            }
        });
    }
}
