package com.mobcent.discuz.module.topic.list.fragment.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.view.MomentsView;

public class TopicListFragmentAdapterHolder extends BaseTopicListFragmentAdapterHolder {
    private LinearLayout locationBox;
    private TextView locationText;
    private MomentsView momentsView;
    private View divider;

    public LinearLayout getLocationBox() {
        return this.locationBox;
    }

    public void setLocationBox(LinearLayout locationBox) {
        this.locationBox = locationBox;
    }

    public TextView getLocationText() {
        return this.locationText;
    }

    public void setLocationText(TextView locationText) {
        this.locationText = locationText;
    }

    public MomentsView getMomentsView() {
        return this.momentsView;
    }

    public void setMomentsView(MomentsView momentsView) {
        this.momentsView = momentsView;
    }

    public View getDivider() {
        return divider;
    }

    public void setDivider(View divider) {
        this.divider = divider;
    }
}
