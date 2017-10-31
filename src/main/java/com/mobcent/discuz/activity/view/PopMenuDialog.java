package com.mobcent.discuz.activity.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class PopMenuDialog extends AlertDialog {
    private String dividerName;
    private String itemColorName;
    private int itemHeight;
    private int itemIconWidth;
    private PopAdapter listAdapter;
    private String listBackResourceName;
    private ListView listView;
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
                convertView = this.inflater.inflate(this.resource.getLayoutId("popup_menu_item"), null);
                holder = new PopHolder();
                holder.itemImg = (ImageView) convertView.findViewById(this.resource.getViewId("popup_img"));
                LayoutParams lps = holder.itemImg.getLayoutParams();
                lps.width = PopMenuDialog.this.itemIconWidth;
                lps.height = PopMenuDialog.this.itemIconWidth;
                holder.itemImg.setLayoutParams(lps);
                holder.itemText = (TextView) convertView.findViewById(this.resource.getViewId("popup_text"));
                convertView.setTag(holder);
            } else {
                holder = (PopHolder) convertView.getTag();
            }
            PopModel popModel = getItem(position);
            holder.itemText.setText(popModel.itemName);
            if (PopMenuDialog.this.itemColorName != null) {
                holder.itemText.setTextColor(this.resource.getColor(PopMenuDialog.this.itemColorName));
            }
            if (TextUtils.isEmpty(popModel.drawableName)) {
                holder.itemImg.setVisibility(8);
            } else {
                holder.itemImg.setVisibility(0);
                holder.itemImg.setBackgroundResource(this.resource.getDrawableId(popModel.drawableName));
            }
            convertView.setLayoutParams(new AbsListView.LayoutParams(-1, PopMenuDialog.this.itemHeight));
            return convertView;
        }
    }

    private final class PopHolder {
        public ImageView itemImg;
        public TextView itemText;

        private PopHolder() {
        }
    }

    public static final class PopModel {
        public int action;
        public Object data;
        public String drawableName;
        public String itemName;
    }

    public interface PopupListDialogListener {
        void onItemClick(PopModel popModel, int i);
    }

    public String getItemColorName() {
        return this.itemColorName;
    }

    public void setItemColorName(String itemColorName) {
        this.itemColorName = itemColorName;
    }

    public int getItemHeight() {
        return this.itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = MCPhoneUtil.dip2px(getContext(), (float) itemHeight);
    }

    public int getItemIconWidth() {
        return this.itemIconWidth;
    }

    public void setItemIconWidth(int itemIconWidth) {
        this.itemIconWidth = MCPhoneUtil.dip2px(getContext(), (float) itemIconWidth);
    }

    public String getDividerName() {
        return this.dividerName;
    }

    public void setDividerName(String dividerName) {
        this.dividerName = dividerName;
    }

    public PopupListDialogListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public void setOnItemClickListener(PopupListDialogListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PopMenuDialog(Context context) {
        this(context, MCResource.getInstance(context.getApplicationContext()).getStyleId("mc_forum_dialog_top_menu"));
    }

    protected PopMenuDialog(Context context, int theme) {
        super(context, theme);
        this.popList = null;
        this.dividerName = "mc_forum_wire";
        initData(context);
    }

    private void initData(Context context) {
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.popList = new ArrayList();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(this.resource.getLayoutId("popup_menu"), null);
        view.setBackgroundResource(this.resource.getDrawableId(this.listBackResourceName));
        this.listView = (ListView) view.findViewById(this.resource.getViewId("list"));
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (PopMenuDialog.this.onItemClickListener != null) {
                    PopMenuDialog.this.onItemClickListener.onItemClick((PopModel) PopMenuDialog.this.popList.get(position), position);
                }
                PopMenuDialog.this.dismiss();
            }
        });
        this.listView.setDivider(this.resource.getDrawable(this.dividerName));
        this.listAdapter = new PopAdapter(getContext(), this.popList);
        this.listView.setAdapter(this.listAdapter);
        setContentView(view, new LayoutParams(this.width, -2));
    }

    public void setPopBackground(String backgroundName) {
        this.listBackResourceName = backgroundName;
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

    public void showRight(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        int x = location[0];
        WindowManager.LayoutParams lps = getWindow().getAttributes();
        lps.width = this.width;
        lps.gravity = 51;
        lps.x = ((x - lps.width) + v.getWidth()) - MCPhoneUtil.dip2px(getContext(), 10.0f);
        lps.y = v.getHeight() + 10;
        getWindow().setAttributes(lps);
        show();
    }

    public void showCenter() {
        WindowManager.LayoutParams lps = getWindow().getAttributes();
        lps.width = MCPhoneUtil.dip2px(getContext(), (float) this.width);
        lps.gravity = 17;
        getWindow().setAttributes(lps);
        show();
    }

    protected void onStart() {
        super.onStart();
    }
}
