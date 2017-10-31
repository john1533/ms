package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.delegate.AdViewDelegate;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdModel;

public class LinkTextView extends LinearLayout implements AdViewDelegate, AdConstant {
    private int adPosition;

    public LinkTextView(Context context) {
        super(context);
        setOrientation(0);
        setGravity(16);
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
        this.adPosition = adContainerModel.getPosition();
        addView(MCAdViewUtils.createLogoText(getContext()), new LayoutParams(getPx(28), getPx(15)));
        LayoutParams lps = new LayoutParams(-2, -2);
        AdModel adModel = (AdModel) adContainerModel.getAdSet().iterator().next();
        adModel.setPo(this.adPosition);
        TextView linkText = MCAdViewUtils.createLinkText(getContext());
        linkText.setText(adModel.getTx());
        linkText.setTag(adModel);
        linkText.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        lps = new LayoutParams(-2, -2);
        lps.leftMargin = getPx(9);
        addView(linkText, lps);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }

    public void freeMemery() {
    }

    private int getPx(int dip) {
        return MCPhoneUtil.dip2px(getContext(), (float) dip);
    }
}
