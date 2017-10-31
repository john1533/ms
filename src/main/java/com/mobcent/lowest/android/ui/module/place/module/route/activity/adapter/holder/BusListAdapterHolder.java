package com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter.holder;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BusListAdapterHolder {
    private RelativeLayout itemView;
    private TextView otherMsg;
    private TextView routeContent;
    private Button routeNum;

    public RelativeLayout getItemView() {
        return this.itemView;
    }

    public void setItemView(RelativeLayout itemView) {
        this.itemView = itemView;
    }

    public Button getRouteNum() {
        return this.routeNum;
    }

    public void setRouteNum(Button routeNum) {
        this.routeNum = routeNum;
    }

    public TextView getRouteContent() {
        return this.routeContent;
    }

    public void setRouteContent(TextView routeContent) {
        this.routeContent = routeContent;
    }

    public TextView getOtherMsg() {
        return this.otherMsg;
    }

    public void setOtherMsg(TextView otherMsg) {
        this.otherMsg = otherMsg;
    }
}
