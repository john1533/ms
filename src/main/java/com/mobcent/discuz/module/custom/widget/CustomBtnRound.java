package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;
import com.mobcent.discuz.module.custom.widget.layout.CustomLayoutBox;
import com.mobcent.lowest.base.utils.MCToastUtils;

public class CustomBtnRound extends CustomBaseRelativeLayout {
    private CustomBaseImg img;
    private CustomBaseRelativeLayout imgBox;
    private int minSide;
    private CustomBaseText text;

    public CustomBtnRound(Context context) {
        super(context);
        this.ratio = CustomConstant.RATIO_IMAGE_ROUND;
    }

    public void setMinSide(int minSide) {
        this.minSide = minSide;
    }

    public void initViews(ConfigComponentModel data) {
        this.imgBox = new CustomLayoutBox(getContext());
        if (this.minSide == 0) {
            MCToastUtils.toast(getContext(), "you must set CustomBtnRound minSide");
            return;
        }
        int width = (int) (((float) this.minSide) * this.ratio);
        int height = (int) (((float) this.minSide) * this.ratio);
        this.img = new CustomBaseImg(getContext());
        this.img.setTag(data.getIcon());
        this.img.setId(1);
        this.img.setImgWidth(width);
        this.lps = new LayoutParams(width, height);
        this.lps.addRule(14, -1);
        this.imgBox.addView(this.img, this.lps);
        if (!TextUtils.isEmpty(data.getDesc())) {
            this.text = CustomHelper.createBtnTitle(getContext());
            this.text.setSingleLine(true);
            this.text.setGravity(17);
            this.text.setText(data.getDesc());
            this.lps = new LayoutParams(-2, -2);
            this.lps.topMargin = dip2px(9.0f);
            this.lps.addRule(3, this.img.getId());
            this.lps.addRule(14, -1);
            this.imgBox.addView(this.text, this.lps);
        }
        this.lps = new LayoutParams(-2, -2);
        this.lps.addRule(13, -1);
        addView(this.imgBox, this.lps);
    }
}
