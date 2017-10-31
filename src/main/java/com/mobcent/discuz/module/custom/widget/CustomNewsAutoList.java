package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.delegate.CustomStateDelegate;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;

public class CustomNewsAutoList extends LinearLayout implements CustomStateDelegate, CustomConstant {
    private String outSideStyle;

    public CustomNewsAutoList(Context context) {
        this(context, null);
    }

    public CustomNewsAutoList(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(1);
        setLayoutParams(new LayoutParams(-1, -2));
    }

    public void initViews(ConfigComponentModel data, boolean isAnim) {
        int padding;
        int count = 0;
        if (CustomConstant.STYLE_LAYOUT_STYLE_IMAGE.equals(this.outSideStyle)) {
            padding = dip2px(5.0f);
        } else if (CustomConstant.STYLE_LAYOUT_STYLE_LINE.equals(this.outSideStyle)) {
            padding = 0;
        } else {
            padding = dip2px(8.0f);
        }
        setPadding(0, padding, 0, padding);
        if (!MCListUtils.isEmpty(data.getComponentList())) {
            count = data.getComponentList().size();
        }
        setLayoutParams(new LayoutParams(-1, (padding * 2) + ((dip2px(61.0f) + 1) * count)));
        createItems(data, isAnim);
    }

    private void createItems(final ConfigComponentModel data, boolean isAnim) {
        int deleay = 50;
        if (!MCListUtils.isEmpty(data.getComponentList())) {
            int count = data.getComponentList().size();
            for (int i = 0; i < count; i++) {
                final int position = i;
                if (isAnim) {
                    postDelayed(new Runnable() {
                        public void run() {
                            CustomListImgLeft item = new CustomListImgLeft(CustomNewsAutoList.this.getContext());
                            item.initViews((ConfigComponentModel) data.getComponentList().get(position));
                            CustomNewsAutoList.this.addView(item);
                            item.onResume();
                        }
                    }, (long) deleay);
                    deleay += 50;
                } else {
                    CustomListImgLeft item = new CustomListImgLeft(getContext());
                    item.initViews((ConfigComponentModel) data.getComponentList().get(i));
                    addView(item);
                }
            }
        }
    }

    protected int dip2px(float dip) {
        return MCPhoneUtil.dip2px(getContext(), dip);
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

    public String getOutSideStyle() {
        return this.outSideStyle;
    }

    public void setOutSideStyle(String outSideStyle) {
        this.outSideStyle = outSideStyle;
    }
}
