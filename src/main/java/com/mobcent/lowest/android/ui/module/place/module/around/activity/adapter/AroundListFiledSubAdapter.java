package com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.activity.adapter.PlaceBaseListAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.holder.AroundListFiledSubHolder;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledSubModel;
import java.util.List;

public class AroundListFiledSubAdapter extends PlaceBaseListAdapter<PlaceFiledSubModel, AroundListFiledSubHolder> {
    public AroundListFiledSubAdapter(Context context, List<PlaceFiledSubModel> dataList) {
        super(context, dataList);
    }

    protected void initViews(View convertView, AroundListFiledSubHolder holder) {
        holder.setFiledNameText((TextView) findViewById(convertView, "filed_name_text"));
        holder.setSelectImg(findViewById(convertView, "filed_select_img"));
    }

    protected void initDatas(AroundListFiledSubHolder holder, PlaceFiledSubModel model, int position) {
        holder.getFiledNameText().setText(model.getName());
        if (model.isSelected()) {
            holder.getSelectImg().setVisibility(0);
        } else {
            holder.getSelectImg().setVisibility(8);
        }
    }

    protected AroundListFiledSubHolder instanceHolder() {
        return new AroundListFiledSubHolder();
    }

    protected String getConvertViewName() {
        return "mc_place_around_list_filed_sub_item";
    }
}
