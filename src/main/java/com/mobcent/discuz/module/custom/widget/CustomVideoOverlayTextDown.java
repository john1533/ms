package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;

public class CustomVideoOverlayTextDown extends CustomBaseRelativeLayout {
    private CustomBaseImg img;
    private LayoutParams lps;
    private CustomBaseText text;
    private View video;

    public CustomVideoOverlayTextDown(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel componentModel) {
        this.img = new CustomBaseImg(getContext());
        this.img.setTag(componentModel.getIcon());
        this.lps = new LayoutParams(-1, -1);
        addView(this.img, this.lps);
        this.video = new View(getContext());
        this.video.setBackgroundDrawable(this.resource.getDrawable("mc_forum_module_play"));
        this.lps = new LayoutParams(dip2px(35.0f), dip2px(35.0f));
        this.lps.addRule(13, -1);
        addView(this.video, this.lps);
        this.text = CustomHelper.createOverTitle(getContext());
        if (TextUtils.isEmpty(componentModel.getDesc())) {
            this.text.setVisibility(8);
        } else {
            this.text.setText(componentModel.getDesc());
            this.text.setVisibility(0);
        }
        this.lps = new LayoutParams(-1, -2);
        this.lps.addRule(12, -1);
        addView(this.text, this.lps);
    }

    public CustomBaseImg getImg() {
        return this.img;
    }

    public CustomBaseText getText() {
        return this.text;
    }
}
