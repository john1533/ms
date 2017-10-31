package com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.activity.adapter.PlaceBaseListAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.holder.AroundListFiledRuleHolder;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledNavModel;
import java.util.List;

public class AroundListFiledSortAdapter extends PlaceBaseListAdapter<PlaceFiledNavModel, AroundListFiledRuleHolder> {
    public AroundListFiledSortAdapter(Context context, List<PlaceFiledNavModel> dataList) {
        super(context, dataList);
    }

    protected void initViews(View convertView, AroundListFiledRuleHolder holder) {
        holder.setFiledNameText((TextView) findViewById(convertView, "filed_name_text"));
        holder.setSelectImg(findViewById(convertView, "filed_select_img"));
    }

    protected void initDatas(AroundListFiledRuleHolder holder, PlaceFiledNavModel model, int position) {
        holder.getFiledNameText().setText(model.getName());
        if (model.isSelected()) {
            holder.getSelectImg().setVisibility(0);
        } else {
            holder.getSelectImg().setVisibility(8);
        }
    }

    protected AroundListFiledRuleHolder instanceHolder() {
        return new AroundListFiledRuleHolder();
    }

    protected String getConvertViewName() {
        return "mc_place_around_list_filed_sort_item";
    }
}
