package com.mobcent.discuz.module.custom.widget.layout;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.CustomBaseRelativeLayout;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.delegate.CustomViewClickListener;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomDispatchView;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;
import java.util.List;

public class CustomLayoutOneThree extends CustomBaseRelativeLayout {
    private int oneHeight;
    private int oneWidth;
    private int threeHeight;
    private int threeWidth;

    public CustomLayoutOneThree(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel data) {
        List<ConfigComponentModel> componentModels;
        int count;
        int i;
        if (CustomConstant.STYLE_LAYOUT_STYLE_DEFAULT.equals(this.style)) {
            super.initViews(data);
            this.oneHeight = this.selfHeight - (this.marginTopBottom * 2);
            this.threeHeight = this.oneHeight / 3;
            this.threeWidth = (int) (CustomConstant.RATIO_ITEM * ((float) this.threeHeight));
            this.oneWidth = ((this.selfWidth - (this.marginLeftRight * 2)) - this.marginInside) - this.threeWidth;
            componentModels = data.getComponentList();
            count = componentModels.size();
        } else {
            super.initViews(data);
            this.oneHeight = this.selfHeight - (this.marginTopBottom * 2);
            this.threeHeight = this.oneHeight / 3;
            this.threeWidth = (int) (CustomConstant.RATIO_ITEM * ((float) this.threeHeight));
            this.oneWidth = ((this.selfWidth - (this.marginLeftRight * 2)) - this.marginInside) - this.threeWidth;
            componentModels = data.getComponentList();
            count = componentModels.size();
        }
        for (i = 0; i < count; i++) {
            ConfigComponentModel component = (ConfigComponentModel) componentModels.get(i);
            LayoutParams lps = computeLayout(i, data.getStyle());
            View view = CustomDispatchView.dispatchChild(getContext(), component, lps.width, lps.height);
            view.setOnClickListener(new CustomViewClickListener(getContext(), component));
            addView(view, lps);
        }
        if (CustomConstant.STYLE_LAYOUT_STYLE_LINE.equals(this.style)) {
            for (i = 0; i < 3; i++) {
                addView(CustomHelper.createLineView(getContext(), computeLineLayout(i, data.getStyle())));
            }
        }
    }

    private LayoutParams computeLineLayout(int position, String componentStyle) {
        LayoutParams lps = new LayoutParams(0, 0);
        int marginLine = dip2px(7.0f);
        if (CustomConstant.STYLE_LAYOUT_ONECOL_THREEROW.equals(componentStyle)) {
            if (position == 0) {
                lps = new LayoutParams(1, this.oneHeight - (marginLine * 2));
                lps.leftMargin = this.oneWidth;
                lps.topMargin = marginLine;
                return lps;
            } else if (position == 1) {
                lps = new LayoutParams(this.threeWidth - marginLine, 1);
                lps.leftMargin = this.oneWidth;
                lps.topMargin = this.threeWidth;
                return lps;
            } else if (position != 2) {
                return lps;
            } else {
                lps = new LayoutParams(this.threeWidth - marginLine, 1);
                lps.leftMargin = this.oneWidth;
                lps.topMargin = this.threeHeight * 2;
                return lps;
            }
        } else if (!CustomConstant.STYLE_LAYOUT_THREEROW_ONECOL.equals(componentStyle)) {
            return lps;
        } else {
            if (position == 0) {
                lps = new LayoutParams(this.threeWidth - marginLine, 1);
                lps.topMargin = this.threeHeight;
                lps.leftMargin = marginLine;
                return lps;
            } else if (position == 1) {
                lps = new LayoutParams(this.threeWidth - marginLine, 1);
                lps.topMargin = this.threeHeight * 2;
                lps.leftMargin = marginLine;
                return lps;
            } else if (position != 2) {
                return lps;
            } else {
                lps = new LayoutParams(1, this.oneHeight - (marginLine * 2));
                lps.leftMargin = this.threeWidth;
                lps.topMargin = marginLine;
                return lps;
            }
        }
    }

    private LayoutParams computeLayout(int position, String componentStyle) {
        LayoutParams lps = new LayoutParams(0, 0);
        if (CustomConstant.STYLE_LAYOUT_ONECOL_THREEROW.equals(componentStyle)) {
            if (position == 0) {
                lps = new LayoutParams(this.oneWidth, this.oneHeight);
                lps.leftMargin = this.marginLeftRight;
                lps.topMargin = this.marginTopBottom;
                return lps;
            } else if (position == 1) {
                lps = new LayoutParams(this.threeWidth, this.threeHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.oneWidth;
                lps.topMargin = this.marginTopBottom;
                return lps;
            } else if (position == 2) {
                lps = new LayoutParams(this.threeWidth, this.threeHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.oneWidth;
                lps.topMargin = this.marginTopBottom + this.threeHeight;
                return lps;
            } else if (position != 3) {
                return lps;
            } else {
                lps = new LayoutParams(this.threeWidth, this.threeHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.oneWidth;
                lps.topMargin = this.marginTopBottom + (this.threeHeight * 2);
                return lps;
            }
        } else if (!CustomConstant.STYLE_LAYOUT_THREEROW_ONECOL.equals(componentStyle)) {
            return lps;
        } else {
            if (position == 0) {
                lps = new LayoutParams(this.threeWidth, this.threeHeight);
                lps.topMargin = this.marginTopBottom;
                lps.leftMargin = this.marginLeftRight;
                return lps;
            } else if (position == 1) {
                lps = new LayoutParams(this.threeWidth, this.threeHeight);
                lps.leftMargin = this.marginLeftRight;
                lps.topMargin = this.marginTopBottom + this.threeHeight;
                return lps;
            } else if (position == 2) {
                lps = new LayoutParams(this.threeWidth, this.threeHeight);
                lps.leftMargin = this.marginLeftRight;
                lps.topMargin = this.marginTopBottom + (this.threeHeight * 2);
                return lps;
            } else if (position != 3) {
                return lps;
            } else {
                lps = new LayoutParams(this.oneWidth, this.oneHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.threeWidth;
                lps.topMargin = this.marginTopBottom;
                return lps;
            }
        }
    }
}
