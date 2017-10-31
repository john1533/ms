package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;
import com.mobcent.lowest.base.utils.MCPhoneUtil;

public class CustomImgUpDown extends CustomBaseRelativeLayout {
    private CustomBaseImg img;
    private CustomBaseText text;

    public CustomImgUpDown(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel data) {
        this.img = new CustomBaseImg(getContext());
        this.img.setTag(data.getIcon());
        this.text = CustomHelper.createUpTextTitle(getContext());
        this.text.setId(1);
        this.lps = new LayoutParams(-1, -2);
        this.lps.addRule(12, -1);
        addView(this.text, this.lps);
        if (!TextUtils.isEmpty(data.getDesc())) {
            this.text.setText(data.getDesc());
        }
        this.lps = new LayoutParams(-1, -1);
        this.lps.addRule(2, this.text.getId());
        this.lps.bottomMargin = MCPhoneUtil.dip2px(getContext(), 5.0f);
        addView(this.img, this.lps);
    }

    public CustomBaseImg getImg() {
        return this.img;
    }

    public CustomBaseText getText() {
        return this.text;
    }
}
