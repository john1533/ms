package com.mobcent.lowest.android.ui.module.plaza.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.PlazaSearchChannelModel;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public class SearchTypeDialog extends AlertDialog {
    public static final int DIALOG_DOWN_LOAD = 1;
    public static final int DIALOG_SET_WALLPAPER = 2;
    private DialogAdapter dialogAdapter = null;
    private MCResource resource;
    private SearchDialogListener searchDialogListener;
    private List<PlazaSearchChannelModel> searchListData = new ArrayList();
    private ListView searchListView;

    private class DialogAdapter extends BaseAdapter {

        class Holder {
            public TextView chanelText;

            Holder() {
            }
        }

        private DialogAdapter() {
        }

        public int getCount() {
            return SearchTypeDialog.this.searchListData.size();
        }

        public PlazaSearchChannelModel getItem(int position) {
            return (PlazaSearchChannelModel) SearchTypeDialog.this.searchListData.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchTypeDialog.this.getContext()).inflate(SearchTypeDialog.this.resource.getLayoutId("mc_plaza_search_dialog_layout_item"), null);
                holder = new Holder();
                holder.chanelText = (TextView) convertView.findViewById(SearchTypeDialog.this.resource.getViewId("chanel_dialog_text"));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.chanelText.setText(getItem(position).getTypeName());
            holder.chanelText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (SearchTypeDialog.this.searchDialogListener != null) {
                        SearchTypeDialog.this.searchDialogListener.onDialogDismiss(position, SearchTypeDialog.this);
                    }
                    SearchTypeDialog.this.dismiss();
                }
            });
            return convertView;
        }
    }

    public interface SearchDialogListener {
        void onDialogDismiss(int i, SearchTypeDialog searchTypeDialog);
    }

    public List<PlazaSearchChannelModel> getSearchListData() {
        return this.searchListData;
    }

    public void setSearchListData(List<PlazaSearchChannelModel> searchListData) {
        this.searchListData = searchListData;
    }

    public SearchDialogListener getSearchDialogListener() {
        return this.searchDialogListener;
    }

    public void setSearchDialogListener(SearchDialogListener searchDialogListener) {
        this.searchDialogListener = searchDialogListener;
    }

    public SearchTypeDialog(Context context) {
        super(context);
        this.resource = MCResource.getInstance(context);
    }

    public SearchTypeDialog(Context context, int theme) {
        super(context, theme);
        this.resource = MCResource.getInstance(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.resource.getLayoutId("mc_plaza_search_dialog_layout"));
        this.searchListView = (ListView) findViewById(this.resource.getViewId("search_list"));
        this.dialogAdapter = new DialogAdapter();
        this.searchListView.setAdapter(this.dialogAdapter);
    }

    public String[] getCurrentItemStr() {
        String[] items = new String[this.searchListData.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = ((PlazaSearchChannelModel) this.searchListData.get(i)).getTypeName();
        }
        return items;
    }

    public void show(int x, int y) {
        Window dialogWindow = getWindow();
        LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(51);
        lp.x = x;
        lp.y = y;
        dialogWindow.setAttributes(lp);
        super.show();
    }

    public void show(View v) {
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawableResource(17170445);
        LayoutParams lp = dialogWindow.getAttributes();
        int[] location = new int[2];
        v.getLocationInWindow(location);
        dialogWindow.setGravity(51);
        lp.x = location[0] + 10;
        lp.y = (location[1] + v.getHeight()) - MCPhoneUtil.dip2px(getContext(), 25.0f);
        dialogWindow.setAttributes(lp);
        super.show();
    }
}
