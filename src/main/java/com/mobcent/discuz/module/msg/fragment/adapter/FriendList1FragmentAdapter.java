package com.mobcent.discuz.module.msg.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.model.FriendModel;
import com.mobcent.discuz.android.model.OtherPanelModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.FriendList1FragmentAdapterHolder;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class FriendList1FragmentAdapter extends BaseListAdatper<FriendModel, FriendList1FragmentAdapterHolder> {
    private Context context;

    public FriendList1FragmentAdapter(Context context, List<FriendModel> datas) {
        super(context, datas);
        this.context = context;
    }

    protected void initViews(View convertView, FriendList1FragmentAdapterHolder holder) {
        holder.setIconImg((MCHeadIcon) findViewByName(convertView, "friend_icon_img"));
        holder.setNoteText((TextView) findViewByName(convertView, "friend_note_text"));
        holder.setNameText((TextView) findViewByName(convertView, "friend_name_text"));
        holder.setApplyBtn((Button) findViewByName(convertView, "friend_apply_btn"));
    }

    protected void initViewDatas(FriendList1FragmentAdapterHolder holder, final FriendModel data, int position) {
        holder.getIconImg().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FriendList1FragmentAdapter.this.context.getApplicationContext(), UserHomeActivity.class);
                intent.putExtra("userId", data.getAuthorId());
                FriendList1FragmentAdapter.this.context.startActivity(intent);
            }
        });
        if (MCStringUtil.isEmpty(data.getAuthorAvatar())) {
            holder.getIconImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            loadImage(holder.getIconImg(), data.getAuthorAvatar());
        }
        if (MCStringUtil.isEmpty(data.getNote())) {
            holder.getNoteText().setText("");
        } else {
            holder.getNoteText().setText(data.getNote());
        }
        if (MCStringUtil.isEmpty(data.getAuthor())) {
            holder.getNameText().setText("");
        } else {
            holder.getNameText().setText(data.getAuthor());
        }
        if (data.getActions() == null || data.getActions().isEmpty()) {
            holder.getApplyBtn().setVisibility(8);
            return;
        }
        for (int i = 0; i < data.getActions().size(); i++) {
            holder.getApplyBtn().setVisibility(8);
            final OtherPanelModel model = (OtherPanelModel) data.getActions().get(i);
            if ("friend".equals(model.getType())) {
                holder.getApplyBtn().setVisibility(0);
                holder.getApplyBtn().setText(model.getTitle());
                holder.getApplyBtn().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(FriendList1FragmentAdapter.this.context, WebViewFragmentActivity.class);
                        intent.putExtra(BaseIntentConstant.BUNDLE_WEB_TITLE, model.getTitle());
                        intent.putExtra("webViewUrl", model.getAction());
                        FriendList1FragmentAdapter.this.context.startActivity(intent);
                    }
                });
                return;
            }
        }
    }

    protected String getLayoutName() {
        return "friend_list1_fragment_item";
    }

    protected FriendList1FragmentAdapterHolder instanceHolder() {
        return new FriendList1FragmentAdapterHolder();
    }
}
