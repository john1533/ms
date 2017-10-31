package com.mobcent.discuz.module.msg.fragment.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.base.adapter.BaseSimpleAdapter;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.SessionListFragmentAdapterHolder1;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.SessionListFragmentAdapterHolder2;
import com.mobcent.lowest.base.utils.MCDateUtil;
import java.util.List;

public abstract class BaseSessionFragmentAdapter extends BaseSimpleAdapter<MsgUserListModel> {
    public static final int INVARIABLE = 3;
    protected ConfigComponentModel componentModel;
    protected SharedPreferencesDB sharedPreferencesDB;

    protected abstract View getView(int i, View view);

    public BaseSessionFragmentAdapter(Context context, List<MsgUserListModel> userList, ConfigComponentModel componentModel) {
        super(context, userList);
        this.componentModel = componentModel;
        this.sharedPreferencesDB = SharedPreferencesDB.getInstance(context.getApplicationContext());
    }

    public int getCount() {
        return this.datas.size() + 3;
    }

    public Object getItem(int position) {
        if (this.datas.size() - position > 0) {
            return this.datas.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public long getChatUserId(int position) {
        if (this.datas.size() - position > 0) {
            return ((MsgUserListModel) this.datas.get(position)).getToUserId();
        }
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView);
    }

    protected void initItemByStrId(TextView titleText, String strId) {
        titleText.setText(this.resource.getStringId(strId));
    }

    protected void initItemByStr(TextView titleText, String str) {
        titleText.setText(str);
    }

    protected void initItemByDrawableId(ImageView imageView, String drawableId) {
        imageView.setImageBitmap(((BitmapDrawable) this.resource.getDrawable(drawableId)).getBitmap());
    }

    protected void initItemMsgSign(TextView msgSignText, int count) {
        if (count > 0) {
            msgSignText.setVisibility(0);
            msgSignText.setText(String.valueOf(count));
            return;
        }
        msgSignText.setVisibility(8);
    }

    protected void initItemByTime(TextView textView, String lastDate) {
        textView.setText(MCDateUtil.getFormatTimeByWord(this.resource, Long.valueOf(lastDate).longValue(), "yyyy-MM-dd HH:mm"));
    }

    public void changeCount(int msgType, int msgNum, SessionListFragmentAdapterHolder1 holder1, SessionListFragmentAdapterHolder2 holder2) {
        switch (msgType) {
            case 1:
                if (holder1 == null) {
                    return;
                }
                if (msgNum > 0) {
                    holder1.getMsgSignText().setVisibility(0);
                    holder1.getMsgSignText().setText(msgNum + "");
                    return;
                }
                holder1.getMsgSignText().setVisibility(8);
                return;
            case 2:
                if (holder1 == null) {
                    return;
                }
                if (msgNum > 0) {
                    holder1.getMsgSignText().setVisibility(0);
                    holder1.getMsgSignText().setText(msgNum + "");
                    return;
                }
                holder1.getMsgSignText().setVisibility(8);
                return;
            case 3:
                if (holder2 == null) {
                    return;
                }
                if (msgNum > 0) {
                    holder2.getMsgSignText().setVisibility(0);
                    holder2.getMsgSignText().setText(msgNum + "");
                    return;
                }
                holder2.getMsgSignText().setVisibility(8);
                return;
            case 4:
                if (holder1 == null) {
                    return;
                }
                if (msgNum > 0) {
                    holder1.getMsgSignText().setVisibility(0);
                    holder1.getMsgSignText().setText(msgNum + "");
                    return;
                }
                holder1.getMsgSignText().setVisibility(8);
                return;
            default:
                return;
        }
    }
}
