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

public class CustomLayoutOneTwo extends CustomBaseRelativeLayout {
    private int oneHeight;
    private int oneWidth;
    private int twoHeight;
    private int twoWidth;

    public CustomLayoutOneTwo(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel data) {
        int i;
        super.initViews(data);
        if (CustomConstant.STYLE_LAYOUT_STYLE_IMAGE.equals(this.style)) {
            this.twoWidth = ((this.selfWidth - (this.marginLeftRight * 2)) - (this.marginInside * 2)) / 3;
            this.oneWidth = (this.twoWidth * 2) + this.marginInside;
            this.oneHeight = this.selfHeight - (this.marginTopBottom * 2);
            this.twoHeight = (this.oneHeight - this.marginInside) / 2;
        } else {
            int i2 = ((this.selfWidth - (this.marginLeftRight * 2)) - this.marginInside) / 2;
            this.twoWidth = i2;
            this.oneWidth = i2;
            this.oneHeight = this.selfHeight - (this.marginTopBottom * 2);
            this.twoHeight = (this.oneHeight - this.marginInside) / 2;
        }
        List<ConfigComponentModel> componentModels = data.getComponentList();
        int count = componentModels.size();
        for (i = 0; i < count; i++) {
            ConfigComponentModel component = (ConfigComponentModel) componentModels.get(i);
            LayoutParams lps = computeLayout(i, data.getStyle());
            View view = CustomDispatchView.dispatchChild(getContext(), component, lps.width, lps.height);
            view.setOnClickListener(new CustomViewClickListener(getContext(), component));
            addView(view, lps);
        }
        if (CustomConstant.STYLE_LAYOUT_STYLE_LINE.equals(this.style)) {
            for (i = 0; i < 2; i++) {
                addView(CustomHelper.createLineView(getContext(), computeLineLayout(i, data.getStyle())));
            }
        }
    }

    private LayoutParams computeLineLayout(int position, String componentStyle) {
        LayoutParams lps = new LayoutParams(0, 0);
        int marginLine = dip2px(7.0f);
        if (CustomConstant.STYLE_LAYOUT_ONECOL_TWOROW.equals(componentStyle)) {
            if (position == 0) {
                lps = new LayoutParams(1, this.oneHeight - (marginLine * 2));
                lps.leftMargin = this.oneWidth;
                lps.topMargin = marginLine;
                return lps;
            } else if (position != 1) {
                return lps;
            } else {
                lps = new LayoutParams(this.twoWidth - marginLine, 1);
                lps.leftMargin = this.oneWidth;
                lps.topMargin = this.twoHeight;
                return lps;
            }
        } else if (!CustomConstant.STYLE_LAYOUT_TWOROW_ONECOL.equals(componentStyle)) {
            return lps;
        } else {
            if (position == 0) {
                lps = new LayoutParams(this.twoWidth - marginLine, 1);
                lps.topMargin = this.twoHeight;
                lps.leftMargin = marginLine;
                return lps;
            } else if (position != 1) {
                return lps;
            } else {
                lps = new LayoutParams(1, this.oneHeight - (marginLine * 2));
                lps.leftMargin = this.twoWidth;
                lps.topMargin = marginLine;
                return lps;
            }
        }
    }

    private LayoutParams computeLayout(int position, String componentStyle) {
        LayoutParams lps = new LayoutParams(0, 0);
        if (CustomConstant.STYLE_LAYOUT_ONECOL_TWOROW.equals(componentStyle)) {
            if (position == 0) {
                lps = new LayoutParams(this.oneWidth, this.oneHeight);
                lps.leftMargin = this.marginLeftRight;
                lps.topMargin = this.marginTopBottom;
                return lps;
            } else if (position == 1) {
                lps = new LayoutParams(this.twoWidth, this.twoHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.oneWidth;
                lps.topMargin = this.marginTopBottom;
                return lps;
            } else if (position != 2) {
                return lps;
            } else {
                lps = new LayoutParams(this.twoWidth, this.twoHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.oneWidth;
                lps.topMargin = (this.marginTopBottom + this.marginInside) + this.twoHeight;
                return lps;
            }
        } else if (!CustomConstant.STYLE_LAYOUT_TWOROW_ONECOL.equals(componentStyle)) {
            return lps;
        } else {
            if (position == 0) {
                lps = new LayoutParams(this.twoWidth, this.twoHeight);
                lps.topMargin = this.marginTopBottom;
                lps.leftMargin = this.marginLeftRight;
                return lps;
            } else if (position == 1) {
                lps = new LayoutParams(this.twoWidth, this.twoHeight);
                lps.leftMargin = this.marginLeftRight;
                lps.topMargin = (this.marginTopBottom + this.marginInside) + this.twoHeight;
                return lps;
            } else if (position != 2) {
                return lps;
            } else {
                lps = new LayoutParams(this.oneWidth, this.oneHeight);
                lps.leftMargin = (this.marginLeftRight + this.marginInside) + this.twoWidth;
                lps.topMargin = this.marginTopBottom;
                return lps;
            }
        }
    }
}
