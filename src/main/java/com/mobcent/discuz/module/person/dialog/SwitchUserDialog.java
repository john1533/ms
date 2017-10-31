package com.mobcent.discuz.module.person.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.PopMenuDialog.PopModel;
import com.mobcent.discuz.activity.view.PopMenuDialog.PopupListDialogListener;
import com.mobcent.discuz.module.person.activity.fragment.BaseUserHomeFragment;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class SwitchUserDialog extends AlertDialog {
    private PopAdapter listAdapter;
    private ListView listView;
    private LinearLayout logoutBox;
    private PopupListDialogListener onItemClickListener;
    private List<PopModel> popList;
    public MCResource resource;
    private int width;

    public class PopAdapter extends BaseAdapter {
        protected LayoutInflater inflater;
        private List<PopModel> popList;
        private MCResource resource;

        public PopAdapter(Context context, List<PopModel> popList) {
            this.popList = popList;
            this.inflater = LayoutInflater.from(context);
            this.resource = MCResource.getInstance(context);
        }

        public int getCount() {
            return this.popList.size();
        }

        public PopModel getItem(int position) {
            return (PopModel) this.popList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PopHolder holder;
            if (convertView == null) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("user_home_switch_user_dialog_item"), null);
                holder = new PopHolder();
                holder.itemImg = (ImageView) convertView.findViewById(this.resource.getViewId("icon"));
                holder.itemText = (TextView) convertView.findViewById(this.resource.getViewId("text"));
                convertView.setTag(holder);
            } else {
                holder = (PopHolder) convertView.getTag();
            }
            PopModel popModel = getItem(position);
            holder.itemText.setText(popModel.itemName);
            if (TextUtils.isEmpty(popModel.drawableName)) {
                holder.itemImg.setVisibility(8);
            } else {
                holder.itemImg.setVisibility(0);
                holder.itemImg.setBackgroundResource(this.resource.getDrawableId(popModel.drawableName));
            }
            return convertView;
        }
    }

    private final class PopHolder {
        public ImageView itemImg;
        public TextView itemText;

        private PopHolder() {
        }
    }

    public PopupListDialogListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public void setOnItemClickListener(PopupListDialogListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SwitchUserDialog(Context context) {
        this(context, MCResource.getInstance(context.getApplicationContext()).getStyleId("mc_forum_dialog_switch_user"));
    }

    protected SwitchUserDialog(Context context, int theme) {
        super(context, theme);
        this.popList = null;
        initData(context);
    }

    private void initData(Context context) {
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.popList = new ArrayList();
        setPopWidth(240);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(this.resource.getLayoutId("user_home_switch_user_dialog"), null);
        this.listView = (ListView) view.findViewById(this.resource.getViewId("content_list"));
        this.logoutBox = (LinearLayout) view.findViewById(this.resource.getViewId("logout_layout"));
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (SwitchUserDialog.this.onItemClickListener != null) {
                    SwitchUserDialog.this.onItemClickListener.onItemClick((PopModel) SwitchUserDialog.this.popList.get(position), position);
                }
                SwitchUserDialog.this.dismiss();
            }
        });
        this.logoutBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PopModel popModel = new PopModel();
                popModel.action = BaseUserHomeFragment.FLAG_LOGOUT;
                if (SwitchUserDialog.this.onItemClickListener != null) {
                    SwitchUserDialog.this.onItemClickListener.onItemClick(popModel, 0);
                }
                SwitchUserDialog.this.dismiss();
            }
        });
        this.listAdapter = new PopAdapter(getContext(), this.popList);
        this.listView.setAdapter(this.listAdapter);
        if (this.popList.size() > 5) {
            setContentView(view, new LayoutParams(this.width, MCPhoneUtil.dip2px(getContext(), 300.0f)));
        } else {
            setContentView(view, new LayoutParams(this.width, -2));
        }
    }

    public void setPopWidth(int dip) {
        this.width = MCPhoneUtil.dip2px(getContext(), (float) dip);
    }

    public void setPopList(List<PopModel> popList) {
        this.popList.clear();
        this.popList.addAll(popList);
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public void showCenter() {
        WindowManager.LayoutParams lps = getWindow().getAttributes();
        lps.height = MCPhoneUtil.dip2px(300.0f);
        getWindow().setAttributes(lps);
        show();
    }
}
