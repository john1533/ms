package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.activity.view.PopMenuDialog;
import com.mobcent.discuz.activity.view.PopMenuDialog.PopModel;
import com.mobcent.discuz.activity.view.PopMenuDialog.PopupListDialogListener;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.msg.fragment.activity.ChatRoomActivity;
import com.mobcent.discuz.module.person.activity.ReportActivity;
import com.mobcent.discuz.module.person.activity.UserListActivity;
import com.mobcent.discuz.module.person.activity.UserPhotosActivity;
import com.mobcent.discuz.module.person.activity.UserProfileActivity;
import com.mobcent.discuz.module.topic.list.fragment.activity.UserTopicListActivity;
import com.mobcent.lowest.android.ui.utils.MCTouchUtil;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserHomeOtherFragment1 extends BaseUserHomeFragment {
    private int ACTION_ADD_BLACK = 1;
    private int ACTION_REPORT = 2;
    private RelativeLayout addFollowBox;
    private ImageView addFollowImg;
    private TextView addFollowText;
    private RelativeLayout addFriendBox;
    private ImageView addFriendImg;
    private TextView addFriendText;
    private Button backBtn;
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            if (v == UserHomeOtherFragment1.this.addFriendBox) {
                if (UserHomeOtherFragment1.this.currentUserInfo != null) {
                    UserHomeOtherFragment1.this.isNeedRefresh = true;
                    String requestUrl = "";
                    if (UserHomeOtherFragment1.this.currentUserInfo.isFriend()) {
                        requestUrl = UserHomeOtherFragment1.this.userService.getFriendOptionRequestUrl(UserHomeOtherFragment1.this.currentUserId, UserConstant.ACT_IGNORE);
                    } else {
                        requestUrl = UserHomeOtherFragment1.this.userService.getFriendOptionRequestUrl(UserHomeOtherFragment1.this.currentUserId, UserConstant.ACT_ADD);
                    }
                    intent = new Intent(UserHomeOtherFragment1.this.activity, WebViewFragmentActivity.class);
                    intent.putExtra("webViewUrl", requestUrl);
                    UserHomeOtherFragment1.this.startActivity(intent);
                }
            } else if (v == UserHomeOtherFragment1.this.sendMsgBox) {
                if (UserHomeOtherFragment1.this.currentUserInfo != null) {
                    MsgUserListModel msgUserListModel = new MsgUserListModel();
                    msgUserListModel.setToUserId(UserHomeOtherFragment1.this.currentUserInfo.getUserId());
                    msgUserListModel.setToUserName(UserHomeOtherFragment1.this.currentUserInfo.getNickname());
                    HashMap<String, Serializable> param = new HashMap();
                    param.put(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                    if (LoginHelper.doInterceptor(UserHomeOtherFragment1.this.activity, ChatRoomActivity.class, param)) {
                        intent = new Intent(UserHomeOtherFragment1.this.activity.getApplicationContext(), ChatRoomActivity.class);
                        intent.putExtra(IntentConstant.INTENT_MSG_USER_LIST_MODEL, msgUserListModel);
                        UserHomeOtherFragment1.this.activity.startActivity(intent);
                    }
                }
            } else if (v == UserHomeOtherFragment1.this.addFollowBox) {
                if (UserHomeOtherFragment1.this.currentUserInfo == null) {
                    return;
                }
                if (UserHomeOtherFragment1.this.currentUserInfo.getIsFollow() == 1) {
                    UserHomeOtherFragment1.this.managerOption(FinalConstant.UNFOLLOW_USER);
                } else {
                    UserHomeOtherFragment1.this.managerOption("follow");
                }
            } else if (v == UserHomeOtherFragment1.this.titleText) {
                UserHomeOtherFragment1.this.refreshUserInfo();
            } else if (v == UserHomeOtherFragment1.this.backBtn) {
                UserHomeOtherFragment1.this.getActivity().finish();
            } else if (v == UserHomeOtherFragment1.this.iconImg) {
                if (UserHomeOtherFragment1.this.currentUserInfo != null && !TextUtils.isEmpty(UserHomeOtherFragment1.this.currentUserInfo.getIcon())) {
                    ArrayList<RichImageModel> richImageModelList = new ArrayList();
                    RichImageModel model = new RichImageModel();
                    model.setImageUrl(UserHomeOtherFragment1.this.currentUserInfo.getIcon().replace("xgsize", FinalConstant.RESOLUTION_ORIGINAL));
                    richImageModelList.add(model);
                    ImagePreviewHelper.getInstance().startImagePreview(UserHomeOtherFragment1.this.activity, richImageModelList, "", null);
                }
            } else if (v == UserHomeOtherFragment1.this.moreBtn) {
                UserHomeOtherFragment1.this.showDialog(v);
            } else if (v == UserHomeOtherFragment1.this.photoBox) {
                intent = new Intent(UserHomeOtherFragment1.this.activity, UserPhotosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId", UserHomeOtherFragment1.this.currentUserId);
                intent.putExtras(bundle);
                UserHomeOtherFragment1.this.startActivity(intent);
            } else if (v == UserHomeOtherFragment1.this.followBox) {
                intent = new Intent(UserHomeOtherFragment1.this.activity, UserListActivity.class);
                intent.putExtra("userId", UserHomeOtherFragment1.this.currentUserId);
                intent.putExtra("userType", "follow");
                intent.putExtra("orderBy", "dateline");
                UserHomeOtherFragment1.this.startActivity(intent);
            } else if (v == UserHomeOtherFragment1.this.fansBox) {
                intent = new Intent(UserHomeOtherFragment1.this.activity, UserListActivity.class);
                intent.putExtra("userId", UserHomeOtherFragment1.this.currentUserId);
                intent.putExtra("userType", UserConstant.USER_FAN);
                intent.putExtra("orderBy", "dateline");
                UserHomeOtherFragment1.this.startActivity(intent);
            } else if (v == UserHomeOtherFragment1.this.publishBox) {
                intent = new Intent(UserHomeOtherFragment1.this.activity.getApplicationContext(), UserTopicListActivity.class);
                intent.putExtra("userId", UserHomeOtherFragment1.this.currentUserId);
                intent.putExtra(PostsConstant.TOPIC_TYPE, "topic");
                UserHomeOtherFragment1.this.startActivity(intent);
            } else if (v == UserHomeOtherFragment1.this.replyBox) {
                intent = new Intent(UserHomeOtherFragment1.this.activity.getApplicationContext(), UserTopicListActivity.class);
                intent.putExtra("userId", UserHomeOtherFragment1.this.currentUserId);
                intent.putExtra(PostsConstant.TOPIC_TYPE, "reply");
                UserHomeOtherFragment1.this.activity.startActivity(intent);
            } else if (v == UserHomeOtherFragment1.this.infomationBox) {
                if (UserHomeOtherFragment1.this.currentUserInfo != null) {
                    intent = new Intent(UserHomeOtherFragment1.this.activity, UserProfileActivity.class);
                    intent.putExtra(IntentConstant.INTENT_USER_INFO, UserHomeOtherFragment1.this.currentUserInfo);
                    UserHomeOtherFragment1.this.activity.startActivity(intent);
                }
            } else if (v == UserHomeOtherFragment1.this.headBox) {
                UserHomeOtherFragment1.this.infomationBox.performClick();
            }
        }
    };
    private TextView creditsText;
    private LinearLayout fansBox;
    private LinearLayout followBox;
    private TextView genderText;
    private RelativeLayout headBox;
    private MCHeadIcon iconImg;
    private LinearLayout infomationBox;
    private boolean isNeedRefresh = true;
    private TextView levelNameText;
    private ManageUserAsyncTask manageUserTask = null;
    private Button moreBtn;
    private TextView nicknameText;
    private LinearLayout photoBox;
    private PopMenuDialog popMenu;
    private LinearLayout publishBox;
    private LinearLayout replyBox;
    private RelativeLayout sendMsgBox;
    private TextView titleText;

    private class ManageUserAsyncTask extends AsyncTask<Object, Void, BaseResultModel<Object>> {
        private String type;

        private ManageUserAsyncTask() {
        }

        protected void onPreExecute() {
            UserHomeOtherFragment1.this.getLoadingPro().show();
        }

        protected BaseResultModel<Object> doInBackground(Object... params) {
            this.type = (String) params[0];
            return UserHomeOtherFragment1.this.userService.manageUser(UserHomeOtherFragment1.this.currentUserId, this.type);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            UserHomeOtherFragment1.this.getLoadingPro().hide();
            DZToastAlertUtils.toast(UserHomeOtherFragment1.this.getActivity().getApplicationContext(), result);
            if (UserHomeOtherFragment1.this.currentUserInfo != null) {
                if (this.type.equals("follow")) {
                    UserHomeOtherFragment1.this.currentUserInfo.setIsFollow(1);
                    UserHomeOtherFragment1.this.addFollowText.setText(UserHomeOtherFragment1.this.resource.getString("mc_forum_cancle_follow"));
                    UserHomeOtherFragment1.this.addFollowImg.setBackgroundResource(UserHomeOtherFragment1.this.resource.getDrawableId("mc_forum_personal_icon25_n"));
                    try {
                        UserHomeOtherFragment1.this.userService.addLocalForumMentionFriend(UserHomeOtherFragment1.this.currentUserId, UserHomeOtherFragment1.this.currentUserInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (this.type.equals(FinalConstant.UNFOLLOW_USER)) {
                    UserHomeOtherFragment1.this.currentUserInfo.setIsFollow(0);
                    UserHomeOtherFragment1.this.addFollowText.setText(UserHomeOtherFragment1.this.resource.getString("mc_forum_follow_ta"));
                    UserHomeOtherFragment1.this.addFollowImg.setBackgroundResource(UserHomeOtherFragment1.this.resource.getDrawableId("mc_forum_personal_icon24_n"));
                    UserHomeOtherFragment1.this.userService.deletedLocalForumMentionFriend(UserHomeOtherFragment1.this.currentUserId, UserHomeOtherFragment1.this.currentUserInfo);
                } else if (UserHomeOtherFragment1.this.currentUserInfo.getBlackStatus() == 1) {
                    UserHomeOtherFragment1.this.currentUserInfo.setBlackStatus(0);
                } else {
                    UserHomeOtherFragment1.this.currentUserInfo.setBlackStatus(1);
                }
            }
        }
    }

    protected String getRootLayoutName() {
        return "user_home_other_fragment1";
    }

    protected void initViews(View rootView) {
        this.moreBtn = (Button) findViewByName(rootView, "more_btn");
        this.backBtn = (Button) findViewByName(rootView, "back_btn");
        MCTouchUtil.createTouchDelegate(this.backBtn, 10);
        MCTouchUtil.createTouchDelegate(this.moreBtn, 10);
        this.titleText = (TextView) findViewByName(rootView, "title_text");
        this.titleText.setText(this.resource.getString("mc_forum_more_info_other"));
        this.iconImg = (MCHeadIcon) findViewByName(rootView, "mc_forum_user_icon");
        this.nicknameText = (TextView) findViewByName(rootView, "mc_forum_user_name");
        this.genderText = (TextView) findViewByName(rootView, "mc_forum_user_gender");
        this.creditsText = (TextView) findViewByName(rootView, "mc_froum_user_credits");
        this.levelNameText = (TextView) findViewByName(rootView, "mc_froum_user_level_text");
        this.infomationBox = (LinearLayout) findViewByName(rootView, "mc_forum_infomation_layout");
        this.photoBox = (LinearLayout) findViewByName(rootView, "mc_forum_photo_other_layout");
        this.followBox = (LinearLayout) findViewByName(rootView, "mc_forum_follow_other_layout");
        this.fansBox = (LinearLayout) findViewByName(rootView, "mc_forum_fan_other_layout");
        this.publishBox = (LinearLayout) findViewByName(rootView, "mc_forum_publish_other_layout");
        this.replyBox = (LinearLayout) findViewByName(rootView, "mc_forum_reply_other_layout");
        this.addFriendBox = (RelativeLayout) findViewByName(rootView, "add_friend_layout");
        this.addFollowBox = (RelativeLayout) findViewByName(rootView, "add_follow_layout");
        this.sendMsgBox = (RelativeLayout) findViewByName(rootView, "send_msg_layout");
        this.addFriendImg = (ImageView) findViewByName(rootView, "add_friend_img");
        this.addFollowImg = (ImageView) findViewByName(rootView, "add_follow_img");
        this.addFriendText = (TextView) findViewByName(rootView, "add_friend_text");
        this.addFollowText = (TextView) findViewByName(rootView, "add_follow_text");
        this.headBox = (RelativeLayout) findViewByName(rootView, "head_box");
    }

    protected void initActions(View rootView) {
        this.addFriendBox.setOnClickListener(this.clickListener);
        this.addFollowBox.setOnClickListener(this.clickListener);
        this.sendMsgBox.setOnClickListener(this.clickListener);
        this.titleText.setOnClickListener(this.clickListener);
        this.backBtn.setOnClickListener(this.clickListener);
        this.iconImg.setOnClickListener(this.clickListener);
        this.moreBtn.setOnClickListener(this.clickListener);
        this.photoBox.setOnClickListener(this.clickListener);
        this.followBox.setOnClickListener(this.clickListener);
        this.fansBox.setOnClickListener(this.clickListener);
        this.publishBox.setOnClickListener(this.clickListener);
        this.replyBox.setOnClickListener(this.clickListener);
        this.infomationBox.setOnClickListener(this.clickListener);
        this.headBox.setOnClickListener(this.clickListener);
    }

    public void onResume() {
        super.onResume();
        if (this.isNeedRefresh) {
            refreshUserInfo();
            this.isNeedRefresh = false;
        }
    }

    protected void onUserInfoResult(UserInfoModel userInfoModel) {
        if (userInfoModel != null) {
            this.levelNameText.setText(userInfoModel.getLevelName());
            this.nicknameText.setText(userInfoModel.getNickname());
            if (TextUtils.isEmpty(userInfoModel.getCreditInfo())) {
                this.creditsText.setVisibility(8);
            } else {
                this.creditsText.setVisibility(0);
                this.creditsText.setText(userInfoModel.getCreditInfo());
            }
            if (this.moreBtn != null) {
                this.moreBtn.setVisibility(0);
            }
            if (userInfoModel.getIsFollow() == 0) {
                this.addFollowText.setText(this.resource.getString("mc_forum_follow_ta"));
                this.addFollowImg.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon24_n"));
            } else {
                this.addFollowText.setText(this.resource.getString("mc_forum_cancle_follow"));
                this.addFollowImg.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon25_n"));
            }
            if (userInfoModel.isFriend()) {
                this.addFriendText.setText(this.resource.getString("mc_forum_cancel_friend"));
                this.addFriendImg.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon26_n"));
            } else {
                this.addFriendText.setText(this.resource.getString("mc_forum_add_friend"));
                this.addFriendImg.setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon22_n"));
            }
            updateGender(this.currentUserInfo);
            loadIcon(this.iconImg, this.currentUserInfo);
        }
    }

    private void updateGender(UserInfoModel userInfoModel) {
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

    private void showDialog(View v) {
        if (this.popMenu == null) {
            this.popMenu = new PopMenuDialog(this.activity);
            this.popMenu.setPopBackground("mc_forum_personal_publish_bg");
            this.popMenu.setPopWidth(100);
            this.popMenu.setItemHeight(40);
            this.popMenu.setOnItemClickListener(new PopupListDialogListener() {
                public void onItemClick(PopModel popModel, int position) {
                    if (popModel.action != UserHomeOtherFragment1.this.ACTION_ADD_BLACK) {
                        Intent intent = new Intent(UserHomeOtherFragment1.this.activity, ReportActivity.class);
                        intent.putExtra(IntentConstant.REPORT_TYPE, 3);
                        intent.putExtra(IntentConstant.REPOR_OBJECT_ID, UserHomeOtherFragment1.this.currentUserId);
                        UserHomeOtherFragment1.this.startActivity(intent);
                    } else if (UserHomeOtherFragment1.this.currentUserInfo == null) {
                    } else {
                        if (UserHomeOtherFragment1.this.currentUserInfo.getBlackStatus() == 1) {
                            UserHomeOtherFragment1.this.managerOption(FinalConstant.UNBLACK_USER);
                        } else {
                            UserHomeOtherFragment1.this.managerOption(FinalConstant.BLACK_USER);
                        }
                    }
                }
            });
        }
        this.popMenu.setPopList(getCurrentPopMenu());
        this.popMenu.showRight(v);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    private void managerOption(String type) {
        if (this.currentUserInfo != null) {
            if (!(this.manageUserTask == null || this.manageUserTask.isCancelled())) {
                this.manageUserTask.cancel(true);
            }
            this.manageUserTask = new ManageUserAsyncTask();
            this.manageUserTask.execute(new Object[]{type});
        }
    }

    private List<PopModel> getCurrentPopMenu() {
        List<PopModel> popList = new ArrayList();
        if (this.currentUserInfo != null) {
            PopModel popModel = new PopModel();
            if (this.currentUserInfo.getBlackStatus() == 1) {
                popModel.itemName = this.resource.getString("mc_forum_cancel_black");
                popModel.action = this.ACTION_ADD_BLACK;
            } else {
                popModel.itemName = this.resource.getString("mc_forum_add_black");
                popModel.action = this.ACTION_ADD_BLACK;
            }
            popList.add(popModel);
            popModel = new PopModel();
            popModel.itemName = this.resource.getString("mc_forum_add_report_user");
            popModel.action = this.ACTION_REPORT;
            popList.add(popModel);
        }
        return popList;
    }
}
