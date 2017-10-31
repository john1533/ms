package com.mobcent.discuz.module.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.module.publish.adapter.holder.MentionFriendAdapterHolder;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class MentionFriendAdapter extends BaseAdapter {
    private MentionFriendAdapterHolder holder;
    private LayoutInflater inflater;
    private MCResource resource;
    private List<UserInfoModel> userInfoList = new ArrayList();

    public List<UserInfoModel> getUserInfoList() {
        return this.userInfoList;
    }

    public void setUserInfoList(List<UserInfoModel> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public MentionFriendAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context);
    }

    public int getCount() {
        return this.userInfoList.size();
    }

    public Object getItem(int position) {
        return this.userInfoList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfoModel infoModel = (UserInfoModel) this.userInfoList.get(position);
        if (infoModel.getUserId() == -1) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mention_friends_divider_item"), null);
            if (infoModel.getRoleNum() == 8) {
                ((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_posts_mention_friends_title_text"))).setText(this.resource.getStringId("mc_forum_mention_friend_admin"));
            } else if (infoModel.getRoleNum() == 2) {
                ((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_posts_mention_friends_title_text"))).setText(this.resource.getStringId("mc_forum_my_friends"));
            } else if (infoModel.getRoleNum() == 6) {
                ((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_posts_mention_friends_title_text"))).setText(this.resource.getStringId("mc_forum_my_follow"));
            }
            return convertView;
        }
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mention_friend_item"), null);
            this.holder = new MentionFriendAdapterHolder();
            this.holder.setNameText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_mention_friend_name_text")));
            convertView.setTag(this.holder);
        } else {
            this.holder = (MentionFriendAdapterHolder) convertView.getTag();
        }
        if (this.holder == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mention_friend_item"), null);
            this.holder = new MentionFriendAdapterHolder();
            this.holder.setNameText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_mention_friend_name_text")));
            convertView.setTag(this.holder);
        }
        this.holder.getNameText().setText(infoModel.getNickname());
        return convertView;
    }
}
