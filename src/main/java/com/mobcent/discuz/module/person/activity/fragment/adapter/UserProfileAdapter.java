package com.mobcent.discuz.module.person.activity.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.mobcent.discuz.android.model.UserProfileModel;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class UserProfileAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private MCResource resource;
    private List<UserProfileModel> userProfileModels = new ArrayList();

    public void setUserProfileModels(List<UserProfileModel> userProfileModels) {
        this.userProfileModels = userProfileModels;
    }

    public UserProfileAdapter(Context context, List<UserProfileModel> userProfileModels) {
        this.userProfileModels = userProfileModels;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return ((UserProfileModel) this.userProfileModels.get(groupPosition)).getUserProfileModels().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        UserProfileModel model = (UserProfileModel) ((UserProfileModel) this.userProfileModels.get(groupPosition)).getUserProfileModels().get(childPosition);
        convertView = this.inflater.inflate(this.resource.getLayoutId("user_profile_item"), null);
        TextView dateTextView = (TextView) convertView.findViewById(this.resource.getViewId("mc_forum_date"));
        ((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_title"))).setText(model.getTitle());
        dateTextView.setText(model.getData());
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return ((UserProfileModel) this.userProfileModels.get(groupPosition)).getUserProfileModels().size();
    }

    public Object getGroup(int groupPosition) {
        return this.userProfileModels.get(groupPosition);
    }

    public int getGroupCount() {
        return this.userProfileModels.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public View getGroupView(int arg0, boolean arg1, View convertView, ViewGroup arg3) {
        convertView = this.inflater.inflate(this.resource.getLayoutId("user_profile_parent_item"), null);
        convertView.setClickable(true);
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return false;
    }
}
