package com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.activity.adapter.PlaceBaseListAdapter;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.holder.AroundListFiledNavHolder;
import com.mobcent.lowest.android.ui.module.place.module.around.model.PlaceFiledNavModel;
import java.util.List;

public class AroundListFiledNavAdapter extends PlaceBaseListAdapter<PlaceFiledNavModel, AroundListFiledNavHolder> {
    public AroundListFiledNavAdapter(Context context, List<PlaceFiledNavModel> dataList) {
        super(context, dataList);
    }

    protected void initViews(View convertView, AroundListFiledNavHolder holder) {
        holder.setFiledNameText((TextView) findViewById(convertView, "filed_name_text"));
    }

    protected void initDatas(AroundListFiledNavHolder holder, PlaceFiledNavModel model, int position) {
        holder.getFiledNameText().setText(model.getName());
        if (model.isSelected()) {
            holder.getFiledNameText().setSelected(true);
        } else {
            holder.getFiledNameText().setSelected(false);
        }
    }

    protected AroundListFiledNavHolder instanceHolder() {
        return new AroundListFiledNavHolder();
    }

    protected String getConvertViewName() {
        return "mc_place_around_list_filed_item";
    }
}
