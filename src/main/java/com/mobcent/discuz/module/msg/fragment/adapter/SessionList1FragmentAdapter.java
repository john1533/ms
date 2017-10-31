package com.mobcent.discuz.module.msg.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.SessionListFragmentAdapterHolder1;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.SessionListFragmentAdapterHolder2;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class SessionList1FragmentAdapter extends BaseSessionFragmentAdapter {
    public SessionList1FragmentAdapter(Context context, List<MsgUserListModel> userList, ConfigComponentModel componentModel) {
        super(context, userList, componentModel);
    }

    protected View getView(int position, View convertView) {
        if (position < 3) {
            convertView = getItem1ConvertView(convertView);
            SessionListFragmentAdapterHolder1 holder1 = (SessionListFragmentAdapterHolder1) convertView.getTag();
            if (position == 0) {
                initItemByStrId(holder1.getTitleText(), "mc_forum_at_me");
                initItemByDrawableId(holder1.getIconImg(), "mc_forum_card_news_button1");
                initItemMsgSign(holder1.getMsgSignText(), this.sharedPreferencesDB.getAtMeCount(this.sharedPreferencesDB.getUserId()));
            } else if (position == 1) {
                initItemByStrId(holder1.getTitleText(), "mc_forum_comment");
                initItemByDrawableId(holder1.getIconImg(), "mc_forum_card_news_button2");
                initItemMsgSign(holder1.getMsgSignText(), this.sharedPreferencesDB.getReplyCount(this.sharedPreferencesDB.getUserId()));
            } else if (position == 2) {
                initItemByStrId(holder1.getTitleText(), "mc_forum_new_friend");
                initItemByDrawableId(holder1.getIconImg(), "mc_forum_card_news_button3");
                initItemMsgSign(holder1.getMsgSignText(), this.sharedPreferencesDB.getFriendCount(this.sharedPreferencesDB.getUserId()));
            }
        } else {
            convertView = getItem2ConvertView(convertView);
            SessionListFragmentAdapterHolder2 holder2 = (SessionListFragmentAdapterHolder2) convertView.getTag();
            MsgUserListModel msgUserModel = (MsgUserListModel) this.datas.get(position - 3);
            initItemByStr(holder2.getNameText(), msgUserModel.getToUserName());
            if (MCStringUtil.isEmpty(msgUserModel.getToUserAvatar())) {
                holder2.getIconImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
            } else {
                loadImage(holder2.getIconImg(), msgUserModel.getToUserAvatar());
            }
            initItemMsgSign(holder2.getMsgSignText(), msgUserModel.getUnReadCount());
            initItemByTime(holder2.getTimeText(), msgUserModel.getLastDateLine());
            initItemByStr(holder2.getSummaryText(), msgUserModel.getLastSummary());
        }
        return convertView;
    }

    private View getItem1ConvertView(View convertView) {
        if (convertView != null && (convertView.getTag() instanceof SessionListFragmentAdapterHolder1)) {
            return convertView;
        }
        convertView = this.inflater.inflate(this.resource.getLayoutId("msg_list1_fragment_item1"), null);
        SessionListFragmentAdapterHolder1 holder = new SessionListFragmentAdapterHolder1();
        initItem1(convertView, holder);
        convertView.setTag(holder);
        return convertView;
    }

    private View getItem2ConvertView(View convertView) {
        if (convertView != null && (convertView.getTag() instanceof SessionListFragmentAdapterHolder2)) {
            return convertView;
        }
        convertView = this.inflater.inflate(this.resource.getLayoutId("msg_list1_fragment_item2"), null);
        SessionListFragmentAdapterHolder2 holder = new SessionListFragmentAdapterHolder2();
        initItem2(convertView, holder);
        convertView.setTag(holder);
        return convertView;
    }

    private void initItem1(View convertView, SessionListFragmentAdapterHolder1 holder) {
        holder.setIconImg((MCHeadIcon) findViewByName(convertView, "icon_img"));
        holder.setTitleText((TextView) findViewByName(convertView, "title_text"));
        holder.setMsgSignText((TextView) findViewByName(convertView, "msg_sign"));
    }

    private void initItem2(View convertView, SessionListFragmentAdapterHolder2 holder) {
        holder.setIconImg((MCHeadIcon) findViewByName(convertView, "icon_img"));
        holder.setMsgSignText((TextView) findViewByName(convertView, "msg_sign"));
        holder.setNameText((TextView) findViewByName(convertView, "user_name_text"));
        holder.setTimeText((TextView) findViewByName(convertView, "msg_time_text"));
        holder.setSummaryText((TextView) findViewByName(convertView, "msg_summary_text"));
    }
}
