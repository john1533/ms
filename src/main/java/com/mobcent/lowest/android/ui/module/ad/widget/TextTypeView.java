package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.delegate.AdViewDelegate;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdModel;
import java.util.Iterator;

public class TextTypeView extends LinearLayout implements AdViewDelegate {
    private AdContainerModel adContainerModel;
    private int adPosition;
    private LayoutParams lps;
    private TextView tv1;
    private TextView tv2;

    public TextTypeView(Context context) {
        super(context);
        setOrientation(0);
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
        this.adContainerModel = adContainerModel;
        this.adPosition = adContainerModel.getPosition();
        initView();
    }

    public void initView() {
        setBackgroundResource(MCResource.getInstance(getContext()).getDrawableId("mc_ad_line"));
        this.tv1 = MCAdViewUtils.createAdText(getContext());
        this.tv2 = MCAdViewUtils.createAdText(getContext());
        this.lps = new LayoutParams(-2, MCAdViewUtils.getBannarsTextHeigth(getContext()), CustomConstant.RATIO_ONE_HEIGHT);
        this.lps.gravity = 17;
        addView(this.tv1, this.lps);
        this.lps = new LayoutParams(-2, MCAdViewUtils.getBannarsTextHeigth(getContext()), CustomConstant.RATIO_ONE_HEIGHT);
        this.lps.gravity = 17;
        addView(this.tv2, this.lps);
        Iterator<AdModel> iterator = this.adContainerModel.getAdSet().iterator();
        AdModel adModel1 = (AdModel) iterator.next();
        adModel1.setPo(this.adPosition);
        AdModel adModel2 = (AdModel) iterator.next();
        adModel2.setPo(this.adPosition);
        this.tv1.setText(adModel1.getTx());
        this.tv1.setTag(adModel1);
        this.tv2.setText(adModel2.getTx());
        this.tv2.setTag(adModel2);
        this.tv1.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        this.tv2.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }

    public void freeMemery() {
    }
}
