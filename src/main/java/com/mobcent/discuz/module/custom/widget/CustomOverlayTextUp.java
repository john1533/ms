package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;

public class CustomOverlayTextUp extends CustomBaseRelativeLayout {
    private CustomBaseImg img;
    private LayoutParams lps;
    private CustomBaseText text;

    public CustomOverlayTextUp(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel componentModel) {
        this.img = new CustomBaseImg(getContext());
        this.img.setTag(componentModel.getIcon());
        this.text = CustomHelper.createOverTitle(getContext());
        if (TextUtils.isEmpty(componentModel.getDesc())) {
            this.text.setVisibility(8);
        } else {
            this.text.setText(componentModel.getDesc());
            this.text.setVisibility(0);
        }
        this.lps = new LayoutParams(-1, -1);
        addView(this.img, this.lps);
        this.lps = new LayoutParams(-1, -2);
        this.lps.addRule(10, -1);
        addView(this.text, this.lps);
    }

    public CustomBaseImg getImg() {
        return this.img;
    }

    public CustomBaseText getText() {
        return this.text;
    }
}
