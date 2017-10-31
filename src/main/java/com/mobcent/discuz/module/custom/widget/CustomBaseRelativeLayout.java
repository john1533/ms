package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.delegate.CustomStateDelegate;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class CustomBaseRelativeLayout extends RelativeLayout implements CustomStateDelegate, CustomConstant {
    protected LayoutParams lps;
    protected int marginInside;
    protected int marginLeftRight;
    protected int marginTopBottom;
    protected float ratio;
    protected MCResource resource;
    protected int screenWidth;
    protected int selfHeight;
    protected int selfWidth;
    protected String style;

    public CustomBaseRelativeLayout(Context context) {
        this(context, null);
    }

    public CustomBaseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ratio = CustomConstant.RATIO_ONE_HEIGHT;
        initData();
    }

    protected void initData() {
        this.resource = MCResource.getInstance(getContext());
        this.screenWidth = MCPhoneUtil.getDisplayWidth(getContext());
        this.marginLeftRight = dip2px(12.0f);
        this.marginTopBottom = dip2px(8.0f);
        this.marginInside = dip2px(10.0f);
    }

    protected void computeSelf() {
        this.selfWidth = this.screenWidth;
        this.selfHeight = (int) (((float) this.screenWidth) * this.ratio);
        setLayoutParams(new ViewGroup.LayoutParams(this.selfWidth, this.selfHeight));
    }

    public void onResume() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof CustomStateDelegate) {
                ((CustomStateDelegate) child).onResume();
            }
        }
    }

    public void onPause() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child instanceof CustomStateDelegate) {
                ((CustomStateDelegate) child).onPause();
            }
        }
    }

    public void initViews(ConfigComponentModel data) {
        computeSelf();
    }

    protected int dip2px(float dip) {
        return MCPhoneUtil.dip2px(getContext(), dip);
    }

    public float getRatio() {
        return this.ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getMarginLeftRight() {
        return this.marginLeftRight;
    }

    public void setMarginLeftRight(int marginLeftRight) {
        this.marginLeftRight = dip2px((float) marginLeftRight);
    }

    public int getMarginTopBottom() {
        return this.marginTopBottom;
    }

    public void setMarginTopBottom(int marginTopBottom) {
        this.marginTopBottom = dip2px((float) marginTopBottom);
    }

    public int getMarginInside() {
        return this.marginInside;
    }

    public void setMarginInside(int marginInside) {
        this.marginInside = dip2px((float) marginInside);
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public ViewGroup.LayoutParams createViewGroupLps() {
        return new ViewGroup.LayoutParams(-1, -2);
    }
}
