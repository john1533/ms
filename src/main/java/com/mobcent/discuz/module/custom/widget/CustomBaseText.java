package com.mobcent.discuz.module.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.delegate.CustomStateDelegate;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public class CustomBaseText extends TextView implements CustomStateDelegate {
    public CustomBaseText(Context context) {
        this(context, null);
    }

    public CustomBaseText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIncludeFontPadding(false);
        setPadding(MCPhoneUtil.dip2px(context, 5.0f), 0, MCPhoneUtil.dip2px(context, 5.0f), 0);
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void initViews(ConfigComponentModel componentModel) {
        if (componentModel != null && !TextUtils.isEmpty(componentModel.getDesc())) {
            setText(componentModel.getDesc());
        }
    }

    public void setTextSize(String dimenName) {
        setTextSize(0, getResources().getDimension(MCResource.getInstance(getContext()).getDimenId(dimenName)));
    }
}
