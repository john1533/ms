package com.mobcent.discuz.module.person.activity.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.module.person.activity.fragment.adapter.holder.UserListFragmentAdapterHolder;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public abstract class BaseUserListFragmentAdapter extends BaseListAdatper<UserInfoModel, UserListFragmentAdapterHolder> {
    protected ConfigComponentModel componentModel;
    protected String orderBy;
    protected SharedPreferencesDB sharedPreferencesDB;

    public BaseUserListFragmentAdapter(Context context, List<UserInfoModel> list, String orderBy, ConfigComponentModel componentModel) {
        super(context, list);
        this.componentModel = componentModel;
        this.orderBy = orderBy;
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(context.getApplicationContext());
    }

    protected void initViews(View convertView, UserListFragmentAdapterHolder holder) {
        holder.setUserIcon((MCHeadIcon) convertView.findViewById(this.resource.getViewId("user_icon_img")));
        holder.setUserName((TextView) convertView.findViewById(this.resource.getViewId("user_name_text")));
        holder.setUserGender((ImageView) convertView.findViewById(this.resource.getViewId("user_gender_text")));
        holder.setUserStatus((ImageView) convertView.findViewById(this.resource.getViewId("user_status_text")));
        holder.setLocationText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_location_text")));
        holder.setTimeText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_time_text")));
        holder.setSignature((TextView) convertView.findViewById(this.resource.getViewId("mc_foum_sign")));
    }

    protected void initViewDatas(UserListFragmentAdapterHolder holder, UserInfoModel userInfoModel, int position) {
        holder.getUserName().setText(userInfoModel.getNickname());
        if (userInfoModel.getGender() == 1) {
            holder.getUserGender().setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon29_n"));
        } else if (userInfoModel.getGender() == 2) {
            holder.getUserGender().setBackgroundResource(this.resource.getDrawableId("mc_forum_personal_icon28_n"));
        } else if (userInfoModel.getGender() == 0) {
            holder.getUserGender().setBackgroundDrawable(null);
        }
        if (userInfoModel.getStatus() == 1) {
            holder.getUserStatus().setVisibility(0);
        } else {
            holder.getUserStatus().setVisibility(8);
        }
        if (!MCStringUtil.isEmpty(userInfoModel.getDateline())) {
            holder.getTimeText().setText(MCDateUtil.getFormatTimeByWord(this.resource, Long.valueOf(userInfoModel.getDateline()).longValue(), "yyyy-MM-dd HH:mm"));
        }
        if (MCStringUtil.isEmpty(userInfoModel.getSignature())) {
            holder.getSignature().setText(this.resource.getString("mc_forum_user_sign"));
        } else {
            holder.getSignature().setText(userInfoModel.getSignature());
        }
        float distance = 0.0f;
        String Str = "";
        if (!MCStringUtil.isEmpty(userInfoModel.getDistance())) {
            distance = Float.parseFloat(userInfoModel.getDistance());
        }
        if (((double) distance) > 0.0d) {
            if (distance > 1000.0f) {
                Str = MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_surround_distance_str2"), (((float) Math.round(distance / 10.0f)) / 100.0f) + "", this.context.getApplicationContext());
            } else {
                Str = MCStringBundleUtil.resolveString(this.resource.getStringId("mc_forum_surround_distance_str1"), (((float) Math.round(distance * 100.0f)) / 100.0f) + "", this.context.getApplicationContext());
            }
            holder.getLocationText().setText(Str);
        } else {
            holder.getLocationText().setVisibility(8);
        }
        updateImage(holder.getUserIcon(), userInfoModel.getIcon());
    }

    protected UserListFragmentAdapterHolder instanceHolder() {
        return new UserListFragmentAdapterHolder();
    }

    private void updateImage(MCHeadIcon imageView, String imgUrl) {
        imageView.setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        if (new SettingSharePreference(this.context).isPicAvailable()) {
            loadImage(imageView, imgUrl);
        }
    }
}
