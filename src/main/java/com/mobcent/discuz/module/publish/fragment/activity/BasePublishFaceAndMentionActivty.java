package com.mobcent.discuz.module.publish.fragment.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.publish.adapter.FacePagerAdapter;
import com.mobcent.discuz.module.publish.adapter.FacePagerAdapter.OnFaceImageClickListener;
import com.mobcent.discuz.module.publish.delegate.MentionFriendReturnDelegate;
import com.mobcent.discuz.module.publish.delegate.MentionFriendReturnDelegate.OnMentionChannelListener;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BasePublishFaceAndMentionActivty extends BasePublishTopicActivity {
    private ImageView faceImg;
    private List<LinkedHashMap> faceList;
    private ViewPager facePager;
    private FacePagerAdapter facePagerAdapter;
    private ImageView mentionFriendImg;
    protected List<UserInfoModel> mentionedFriends;
    protected RelativeLayout multiFaceBox;
    private ImageView[] selects;
    private char spaceChar = ' ';
    protected ArrayList<UserInfoModel> userInfoModels;

    protected void initDatas() {
        super.initDatas();
        this.mentionedFriends = new ArrayList();
        this.userInfoModels = new ArrayList();
    }

    protected void initViews() {
        super.initViews();
        this.mentionFriendImg = (ImageView) findViewByName("mc_forum_mention_friend_img");
        this.faceImg = (ImageView) findViewByName("mc_forum_face_img");
        this.multiFaceBox = (RelativeLayout) findViewByName("multi_face_box");
        this.multiFaceBox.setPadding(0, MCPhoneUtil.dip2px(this, 8.0f), 0, 0);
        this.facePager = (ViewPager) findViewByName("face_pager");
        ImageView select1 = (ImageView) findViewByName("indicate_select1");
        ImageView select2 = (ImageView) findViewByName("indicate_select2");
        ImageView select3 = (ImageView) findViewByName("indicate_select3");
        this.selects = new ImageView[]{select1, select2, select3};
        this.faceList = DZFaceUtil.getFaceConstant(getApplicationContext()).getFaceList();
        this.facePagerAdapter = new FacePagerAdapter(getApplicationContext(), this.faceList);
        this.facePager.setAdapter(this.facePagerAdapter);
        if (this.settingModel == null || this.settingModel.getAllowAt() > 0) {
            this.mentionFriendImg.setVisibility(0);
        } else {
            this.mentionFriendImg.setVisibility(8);
        }
    }

    protected void initActions() {
        super.initActions();
        this.mentionFriendImg.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                BasePublishFaceAndMentionActivty.this.hideSoftKeyboard();
                MentionFriendReturnDelegate.getInstance().setOnLoginChannelListener(new OnMentionChannelListener() {
                    public void changeMentionFriend(UserInfoModel mentionFriend) {
                        BasePublishFaceAndMentionActivty.this.contentEdText.getText().insert(BasePublishFaceAndMentionActivty.this.contentEdText.getSelectionEnd(), "@");
                        if (mentionFriend.getUserId() > -1) {
                            BasePublishFaceAndMentionActivty.this.getMentionedFriend();
                            if (BasePublishFaceAndMentionActivty.this.mentionedFriends.size() < 20) {
                                BasePublishFaceAndMentionActivty.this.contentEdText.getText().insert(BasePublishFaceAndMentionActivty.this.contentEdText.getSelectionEnd(), mentionFriend.getNickname() + " ");
                                BasePublishFaceAndMentionActivty.this.mentionedFriends.add(mentionFriend);
                                return;
                            }
                            BasePublishFaceAndMentionActivty.this.toast(MCStringBundleUtil.resolveString(BasePublishFaceAndMentionActivty.this.resource.getStringId("mc_forum_mention_friend_count"), "20", BasePublishFaceAndMentionActivty.this));
                        }
                    }
                });
                Intent intent = new Intent(BasePublishFaceAndMentionActivty.this, MentionFriendFragmentActivity.class);
                intent.putExtra(FinalConstant.POSTS_USER_LIST, BasePublishFaceAndMentionActivty.this.userInfoModels);
                BasePublishFaceAndMentionActivty.this.startActivity(intent);
            }
        });
        this.faceImg.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                BasePublishFaceAndMentionActivty.this.hideSoftKeyboard();
                if (BasePublishFaceAndMentionActivty.this.multiFaceBox == null || BasePublishFaceAndMentionActivty.this.multiFaceBox.getVisibility() != 0) {
                    BasePublishFaceAndMentionActivty.this.multiFaceBox.setVisibility(0);
                    BasePublishFaceAndMentionActivty.this.imm.hideSoftInputFromWindow(BasePublishFaceAndMentionActivty.this.contentEdText.getWindowToken(), 2);
                    return;
                }
                BasePublishFaceAndMentionActivty.this.multiFaceBox.setVisibility(8);
            }
        });
        this.facePager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                for (int i = 0; i < BasePublishFaceAndMentionActivty.this.selects.length; i++) {
                    if (i == position) {
                        BasePublishFaceAndMentionActivty.this.selects[i].setImageDrawable(BasePublishFaceAndMentionActivty.this.getResources().getDrawable(BasePublishFaceAndMentionActivty.this.resource.getDrawableId("mc_forum_select2_2")));
                    } else {
                        BasePublishFaceAndMentionActivty.this.selects[i].setImageDrawable(BasePublishFaceAndMentionActivty.this.getResources().getDrawable(BasePublishFaceAndMentionActivty.this.resource.getDrawableId("mc_forum_select2_1")));
                    }
                }
            }
        });
        this.facePagerAdapter.setOnFaceImageClickListener(new OnFaceImageClickListener() {
            public void onFaceImageClick(String tag, Drawable drawable) {
                SpannableStringBuilder spannable = new SpannableStringBuilder(tag);
                spannable.setSpan(new ImageSpan(drawable, 1), 0, tag.length(), 33);
                BasePublishFaceAndMentionActivty.this.contentEdText.getText().insert(BasePublishFaceAndMentionActivty.this.contentEdText.getSelectionEnd(), spannable);
            }
        });
        this.contentEdText.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (BasePublishFaceAndMentionActivty.this.multiFaceBox != null && BasePublishFaceAndMentionActivty.this.multiFaceBox.getVisibility() == 0) {
                    BasePublishFaceAndMentionActivty.this.multiFaceBox.setVisibility(8);
                }
                BasePublishFaceAndMentionActivty.this.showSoftKeyboard();
                return false;
            }
        });
    }

    public List<UserInfoModel> getMentionedFriend() {
        String mentionfriendcount = "";
        String content = this.contentEdText.getText().toString();
        for (int i = 0; i < this.mentionedFriends.size(); i++) {
            mentionfriendcount = mentionfriendcount + ((UserInfoModel) this.mentionedFriends.get(i)).getNickname() + AdApiConstant.RES_SPLIT_COMMA;
        }
        List<UserInfoModel> tempList = new ArrayList();
        if (content.contains("@")) {
            String[] s = content.split("@");
            for (int j = 0; j < s.length; j++) {
                int endIndex = s[j].indexOf(this.spaceChar);
                String nickname = "";
                if (endIndex > -1) {
                    nickname = s[j].substring(0, endIndex);
                    UserInfoModel user = new UserInfoModel();
                    if (mentionfriendcount.contains(nickname + AdApiConstant.RES_SPLIT_COMMA)) {
                        user = getUserFromFriendsList(this.mentionedFriends, nickname);
                    } else {
                        user.setUserId(0);
                        user.setNickname(nickname);
                    }
                    if (user != null) {
                        tempList.add(user);
                    }
                }
            }
        }
        this.mentionedFriends = tempList;
        return this.mentionedFriends;
    }

    private UserInfoModel getUserFromFriendsList(List<UserInfoModel> friends, String name) {
        int s = friends.size();
        for (int i = 0; i < s; i++) {
            if (name.equals(((UserInfoModel) friends.get(i)).getNickname())) {
                return (UserInfoModel) friends.get(i);
            }
        }
        return null;
    }
}
