package com.mobcent.discuz.activity.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
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

public class MCPopupListDialog extends AlertDialog {
    private PopAdapter listAdapter;
    private ListView listView;
    private PopupListDialogListener onItemClickListener;
    private List<PopModel> popList;
    public MCResource resource;

    public static class PopAdapter extends BaseAdapter {
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
                convertView = this.inflater.inflate(this.resource.getLayoutId("popup_list_item_alert"), null);
                holder = new PopHolder();
                holder.itemImg = (ImageView) convertView.findViewById(this.resource.getViewId("popup_img"));
                holder.itemText = (TextView) convertView.findViewById(this.resource.getViewId("popup_text"));
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

    private static class PopHolder {
        public ImageView itemImg;
        public TextView itemText;

        private PopHolder() {
        }
    }

    public static final class PopModel {
        public Object data;
        public String drawableName;
        public String itemName;
    }

    public interface PopupListDialogListener {
        void onItemClick(PopModel popModel, int i);
    }

    public PopupListDialogListener getOnItemClickListener() {
        return this.onItemClickListener;
    }

    public void setOnItemClickListener(PopupListDialogListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MCPopupListDialog(Context context) {
        this(context, MCResource.getInstance(context.getApplicationContext()).getStyleId("mc_forum_dialog_top_menu"));
    }

    protected MCPopupListDialog(Context context, int theme) {
        super(context, theme);
        this.popList = null;
        initData(context);
    }

    private void initData(Context context) {
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.popList = new ArrayList();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(this.resource.getLayoutId("popup_list"), null);
        this.listView = (ListView) view.findViewById(this.resource.getViewId("list"));
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (MCPopupListDialog.this.onItemClickListener != null) {
                    MCPopupListDialog.this.onItemClickListener.onItemClick((PopModel) MCPopupListDialog.this.popList.get(position), position);
                }
                MCPopupListDialog.this.cancel();
            }
        });
        this.listAdapter = new PopAdapter(getContext(), this.popList);
        this.listView.setAdapter(this.listAdapter);
        setContentView(view);
    }

    public void setPopBackground(String backgroundName) {
        if (this.listView != null) {
            this.listView.setBackgroundResource(this.resource.getDrawableId(backgroundName));
        }
    }

    public void setPopList(List<PopModel> popList) {
        this.popList.clear();
        this.popList.addAll(popList);
        if (this.listAdapter != null) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    public void show(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int x = location[0];
        LayoutParams lps = getWindow().getAttributes();
        lps.width = MCPhoneUtil.dip2px(getContext(), 130.0f);
        lps.gravity = 51;
        lps.x = MCPhoneUtil.getDisplayWidth(getContext())-lps.width;


        TypedValue typedValue = new TypedValue();

        //TODO
        getContext().getTheme().resolveAttribute(resource.getAttrId("light"), typedValue, true);
        int[] attribute = new int[] { resource.getAttrId("actionBarSize") };
        TypedArray array = getContext().obtainStyledAttributes(typedValue.resourceId, attribute);
        int actionBarSize = array.getDimensionPixelSize(0 /* index */, -1 /* default size */);
        lps.y =  actionBarSize;
        array.recycle();


        getWindow().setAttributes(lps);
        show();
    }

    protected void onStart() {
        super.onStart();
    }
}
