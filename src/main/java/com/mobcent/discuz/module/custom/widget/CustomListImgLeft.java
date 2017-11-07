package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.delegate.CustomViewClickListener;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;

public class CustomListImgLeft extends CustomBaseRelativeLayout {
    private CustomBaseText descText;
    private CustomBaseImg img;
    private View line;
    private View rightIcon;
    private CustomBaseText titleText;

    public CustomListImgLeft(Context context) {
        super(context);
        ViewGroup.LayoutParams lps = createViewGroupLps();
        lps.height = dip2px(61.0f) + 1;
        setLayoutParams(lps);
    }

    public void initViews(ConfigComponentModel data) {
        setBackgroundResource(this.resource.getDrawableId("mc_forum_list_item2"));
        setOnClickListener(new CustomViewClickListener(getContext(), data));
        this.img = new CustomBaseImg(getContext());
        this.img.setId(100);
        CustomBaseImg customBaseImg = this.img;
        MCAsyncTaskLoaderImage.getInstance(getContext());
        customBaseImg.setTag(MCAsyncTaskLoaderImage.formatUrl(data.getIcon(), FinalConstant.RESOLUTION_SMALL));
        this.lps = new RelativeLayout.LayoutParams(dip2px(70.0f), dip2px(45.0f));
        this.lps.addRule(9, -1);
        this.lps.leftMargin = dip2px(12.0f);
        this.lps.rightMargin = dip2px(10.0f);
        this.lps.addRule(15, -1);
        this.img.setLayoutParams(this.lps);
        addView(this.img);
        this.rightIcon = new View(getContext());
        this.rightIcon.setBackgroundDrawable(this.resource.getDrawable("mc_forum_squre_arrow"));
        this.rightIcon.setId(101);
        this.lps = new RelativeLayout.LayoutParams(dip2px(8.0f), dip2px(13.0f));
        this.lps.addRule(11, -1);
        this.lps.addRule(15, -1);
        this.lps.leftMargin = dip2px(10.0f);
        this.lps.rightMargin = dip2px(10.0f);
        this.rightIcon.setLayoutParams(this.lps);
        addView(this.rightIcon);
        this.lps = new RelativeLayout.LayoutParams(-1, -2);
        this.lps.addRule(1, this.img.getId());
        this.lps.addRule(0, this.rightIcon.getId());
        this.titleText = CustomHelper.createTitle(getContext());
        this.titleText.setPadding(0, 0, 0, 0);
        this.titleText.setId(102);
        this.titleText.setText(data.getTitle());
        if (TextUtils.isEmpty(data.getDesc())) {
            this.lps.addRule(15, -1);
        } else {
            this.lps.addRule(6, this.img.getId());
            this.lps.topMargin = dip2px(4.0f);
            this.lps.bottomMargin = dip2px(4.0f);
        }
        this.titleText.setLayoutParams(this.lps);
        addView(this.titleText);
        if (!TextUtils.isEmpty(data.getDesc())) {
            this.descText = CustomHelper.createDesc(getContext());
            this.descText.setPadding(0, 0, 0, 0);
            this.lps = new RelativeLayout.LayoutParams(-1, -2);
            this.lps.addRule(5, this.titleText.getId());
            this.lps.addRule(3, this.titleText.getId());
            this.lps.addRule(1, this.img.getId());
            this.lps.addRule(0, this.rightIcon.getId());
            this.descText.setLayoutParams(this.lps);
            this.descText.setText(data.getDesc());
            addView(this.descText);
        }
        this.lps = new RelativeLayout.LayoutParams(-1, 1);
        this.lps.addRule(12, -1);
        this.line = CustomHelper.createLineView(getContext(), null);
        addView(this.line, this.lps);
    }
}
