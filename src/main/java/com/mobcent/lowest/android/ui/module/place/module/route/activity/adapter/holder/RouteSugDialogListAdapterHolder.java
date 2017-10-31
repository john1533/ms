package com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.holder;

import android.widget.RelativeLayout;
import android.widget.TextView;

public class RouteSugDialogListAdapterHolder {
    private TextView city;
    private RelativeLayout itemLayout;
    private TextView name;

    public TextView getName() {
        return this.name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getCity() {
        return this.city;
    }

    public void setCity(TextView city) {
        this.city = city;
    }

    public RelativeLayout getItemLayout() {
        return this.itemLayout;
    }

    public void setItemLayout(RelativeLayout itemLayout) {
        this.itemLayout = itemLayout;
    }
}
