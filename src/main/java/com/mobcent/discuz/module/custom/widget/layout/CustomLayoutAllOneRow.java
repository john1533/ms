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

public class CustomLayoutAllOneRow extends CustomBaseRelativeLayout {
    private int childHeight;
    private int height;
    private int lineMarginLeft;
    private int width;

    public CustomLayoutAllOneRow(Context context) {
        super(context);
    }

    public void initViews(ConfigComponentModel data) {
        super.initViews(data);
        List<ConfigComponentModel> componentModels = data.getComponentList();
        if (componentModels.size() != 0) {
            for (int i = 0; i < componentModels.size(); i++) {
                LayoutParams lps;
                this.childHeight = this.selfHeight - (this.marginTopBottom * 2);
                if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONECOL_HIGH) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONECOLLOW) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONECOL_FIXED)) {
                    lps = computeLayout();
                } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TWOCOL_LOW) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TWOCOL_MID) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TWOCOL_HIGH)) {
                    lps = compute2Layout(i);
                } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_LOW) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_MID) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_HIGH)) {
                    lps = compute3Layout(i);
                } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_FOURCOL) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_MID) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_HIGH)) {
                    lps = compute4Layout(i);
                } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONECOL_ONEROW)) {
                    lps = computeLeftWithLayout(i);
                } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONEROW_ONECOL)) {
                    lps = computeLeftNarrowlayout(i);
                } else {
                    lps = computeLayout();
                }
                if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TEXT_TWO_COL)) {
                    lps = compute2Layout(i);
                } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TEXT_THREE_COL)) {
                    lps = compute3Layout(i);
                }
                View view = CustomDispatchView.dispatchChild(getContext(), (ConfigComponentModel) componentModels.get(i), lps.width, lps.height);
                view.setOnClickListener(new CustomViewClickListener(getContext(), (ConfigComponentModel) componentModels.get(i)));
                addView(view, lps);
                if (i != 0 && this.style.equals(CustomConstant.STYLE_LAYOUT_STYLE_LINE)) {
                    LayoutParams params = computeLineLayout(i, this.width, this.height);
                    addView(CustomHelper.createLineView(getContext(), params), params);
                }
            }
        }
    }

    private LayoutParams computeLineLayout(int i, int width, int height) {
        int marginLine = dip2px(7.0f);
        LayoutParams lps = new LayoutParams(1, height - (marginLine * 2));
        lps.leftMargin = this.lineMarginLeft;
        lps.topMargin = marginLine;
        return lps;
    }

    private LayoutParams computeLayout() {
        this.width = this.screenWidth;
        this.height = this.childHeight;
        LayoutParams lps = new LayoutParams(this.width, this.height);
        lps.leftMargin = this.marginLeftRight;
        lps.topMargin = this.marginTopBottom;
        return lps;
    }

    private LayoutParams compute2Layout(int position) {
        this.width = ((this.screenWidth - (this.marginLeftRight * 2)) - this.marginInside) / 2;
        this.height = this.childHeight;
        LayoutParams lps = new LayoutParams(this.width, this.height);
        if (position == 0) {
            this.lineMarginLeft = this.marginLeftRight;
        } else if (position == 1) {
            this.lineMarginLeft = (this.marginLeftRight + this.marginInside) + this.width;
        }
        lps.topMargin = this.marginTopBottom;
        lps.leftMargin = this.lineMarginLeft;
        return lps;
    }

    private LayoutParams compute3Layout(int position) {
        this.width = ((this.screenWidth - (this.marginLeftRight * 2)) - (this.marginInside * 2)) / 3;
        this.height = this.childHeight;
        LayoutParams lps = new LayoutParams(this.width, this.height);
        if (position == 0) {
            this.lineMarginLeft = this.marginLeftRight;
        } else if (position == 1) {
            this.lineMarginLeft = (this.marginLeftRight + this.marginInside) + this.width;
        } else if (position == 2) {
            this.lineMarginLeft = (this.marginLeftRight + (this.marginInside * 2)) + (this.width * 2);
        }
        lps.leftMargin = this.lineMarginLeft;
        lps.topMargin = this.marginTopBottom;
        return lps;
    }

    private LayoutParams compute4Layout(int position) {
        this.width = ((this.screenWidth - (this.marginLeftRight * 2)) - (this.marginInside * 3)) / 4;
        this.height = this.childHeight;
        LayoutParams lps = new LayoutParams(this.width, this.height);
        if (position == 0) {
            this.lineMarginLeft = this.marginLeftRight;
        } else if (position == 1) {
            this.lineMarginLeft = (this.marginLeftRight + this.marginInside) + this.width;
        } else if (position == 2) {
            this.lineMarginLeft = (this.marginLeftRight + (this.marginInside * 2)) + (this.width * 2);
        } else if (position == 3) {
            this.lineMarginLeft = (this.marginLeftRight + (this.marginInside * 3)) + (this.width * 3);
        }
        lps.leftMargin = this.lineMarginLeft;
        lps.topMargin = this.marginTopBottom;
        return lps;
    }

    private LayoutParams computeLeftNarrowlayout(int position) {
        this.width = ((this.screenWidth - (this.marginLeftRight * 2)) - (this.marginInside * 2)) / 3;
        this.height = this.childHeight;
        if (position == 0) {
            this.lps = new LayoutParams(this.width, this.height);
            this.lineMarginLeft = this.marginLeftRight;
        } else if (position == 1) {
            this.lps = new LayoutParams((this.width * 2) + this.marginInside, this.height);
            this.lineMarginLeft = (this.marginLeftRight + this.marginInside) + this.width;
        }
        this.lps.leftMargin = this.lineMarginLeft;
        this.lps.topMargin = this.marginTopBottom;
        return this.lps;
    }

    private LayoutParams computeLeftWithLayout(int i) {
        this.width = ((this.screenWidth - (this.marginLeftRight * 2)) - (this.marginInside * 2)) / 3;
        this.height = this.childHeight;
        if (i == 0) {
            this.lps = new LayoutParams((this.width * 2) + this.marginInside, this.height);
            this.lineMarginLeft = this.marginLeftRight;
        } else if (i == 1) {
            this.lps = new LayoutParams(this.width, this.height);
            this.lineMarginLeft = ((this.marginLeftRight * 2) + this.marginInside) + (this.width * 2);
        }
        this.lps.leftMargin = this.lineMarginLeft;
        this.lps.topMargin = this.marginTopBottom;
        return this.lps;
    }

    private void initRatio(ConfigComponentModel data) {
        if (!CustomConstant.STYLE_LAYOUT_STYLE_DEFAULT.equals(this.style)) {
            return;
        }
        if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONECOLLOW) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_LOW) || data.getStyle().equals(CustomConstant.STYLE_LAYOUT_FOURCOL)) {
            this.ratio = 0.5f;
        } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_ONECOL_HIGH)) {
            this.ratio = CustomConstant.RATIO_ONE_HEIGHT;
        } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TWOCOL_MID)) {
            this.ratio = 0.425f;
        } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TWOCOL_LOW)) {
            this.ratio = CustomConstant.RATIO_ALL_LOW;
        } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_TWOCOL_HIGH)) {
            this.ratio = CustomConstant.RATIO_TWO_HEIGHT;
        } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_MID)) {
            this.ratio = CustomConstant.RATIO_THREE_MIDDLE;
        } else if (data.getStyle().equals(CustomConstant.STYLE_LAYOUT_THREECOL_HIGH)) {
            this.ratio = 0.425f;
        }
    }
}
