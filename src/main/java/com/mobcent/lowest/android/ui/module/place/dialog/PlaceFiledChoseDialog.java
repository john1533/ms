package com.mobcent.lowest.android.ui.module.place.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.AroundListFiledNavAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.AroundListFiledSubAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledNavModel;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledSubModel;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.ArrayList;
import java.util.List;

public class PlaceFiledChoseDialog extends BasePlaceFiledDialog {
    private AroundListFiledNavAdapter navAdapter = null;
    private List<PlaceFiledNavModel> navDataList = null;
    private ListView navListView;
    private int navPosition;
    private AroundListFiledSubAdapter subAdapter = null;
    private List<PlaceFiledSubModel> subDataList = null;
    private ListView subListView;
    private int subPosition;
    private String tagKeywords = null;

    public PlaceFiledChoseDialog(Context context, List<PlaceFiledNavModel> navDataList, int theme) {
        super(context, theme);
        this.navDataList = navDataList;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.resource.getLayoutId("mc_place_filter_widget_dialog"));
        this.subDataList = new ArrayList();
        this.navAdapter = new AroundListFiledNavAdapter(getContext(), this.navDataList);
        this.subAdapter = new AroundListFiledSubAdapter(getContext(), this.subDataList);
        this.navListView = (ListView) findViewById(this.resource.getViewId("first_category_list"));
        this.navListView.setAdapter(this.navAdapter);
        this.subListView = (ListView) findViewById(this.resource.getViewId("second_category_list"));
        this.subListView.setAdapter(this.subAdapter);
        initActions();
    }

    private void initActions() {
        this.navListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PlaceFiledChoseDialog.this.navPosition = position;
                PlaceFiledChoseDialog.this.setNavSelected(PlaceFiledChoseDialog.this.navDataList, position);
                PlaceFiledChoseDialog.this.navAdapter.notifyDataSetChanged();
                PlaceFiledChoseDialog.this.subAdapter.setDataList(((PlaceFiledNavModel) PlaceFiledChoseDialog.this.navDataList.get(position)).getSubList());
                PlaceFiledChoseDialog.this.subAdapter.notifyDataSetChanged();
            }
        });
        this.subListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PlaceFiledSubModel subModel = (PlaceFiledSubModel) PlaceFiledChoseDialog.this.subDataList.get(position);
                if (position != 0) {
                    PlaceFiledChoseDialog.this.tagKeywords = subModel.getName();
                } else if (PlaceFiledChoseDialog.this.navPosition != 0) {
                    PlaceFiledChoseDialog.this.tagKeywords = ((PlaceFiledNavModel) PlaceFiledChoseDialog.this.navDataList.get(PlaceFiledChoseDialog.this.navPosition)).getName();
                } else {
                    PlaceFiledChoseDialog.this.tagKeywords = null;
                }
                PlaceFiledChoseDialog.this.setSubSelected(PlaceFiledChoseDialog.this.subDataList, position);
                PlaceFiledChoseDialog.this.subAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setNavSelected(List<PlaceFiledNavModel> navList, int position) {
        int len = navList.size();
        for (int i = 0; i < len; i++) {
            if (i == position) {
                ((PlaceFiledNavModel) navList.get(i)).setSelected(true);
            } else {
                ((PlaceFiledNavModel) navList.get(i)).setSelected(false);
            }
        }
    }

    private void setSubSelected(List<PlaceFiledSubModel> subList, int position) {
        int len = subList.size();
        for (int i = 0; i < len; i++) {
            if (i == position) {
                ((PlaceFiledSubModel) subList.get(i)).setSelected(true);
            } else {
                ((PlaceFiledSubModel) subList.get(i)).setSelected(false);
            }
        }
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
        dialogWindow.setLayout(-1, MCPhoneUtil.getDisplayHeight(getContext()) - lp.x);
        dialogWindow.setAttributes(lp);
        super.show();
    }
}
