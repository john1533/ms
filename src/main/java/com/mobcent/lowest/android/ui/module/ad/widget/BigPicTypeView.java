package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.module.ad.delegate.AdViewDelegate;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.utils.AdStringUtils;

public class BigPicTypeView extends RelativeLayout implements AdViewDelegate {
    public String TAG = "BigPicTypeView";
    private AdContainerModel adContainerModel;
    private AdModel adModel;
    private int adPosition;
    private LayoutParams lps = null;
    private BasePicView picView = null;
    private AdProgress progress = null;

    public BigPicTypeView(Context context) {
        super(context);
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
        this.adContainerModel = adContainerModel;
        this.adPosition = adContainerModel.getPosition();
        initView(getContext());
    }

    private void initView(Context context) {
        setBackgroundColor(-1);
        this.adModel = (AdModel) this.adContainerModel.getAdSet().iterator().next();
        this.adModel.setPo(this.adPosition);
        this.lps = new LayoutParams(-1, -1);
        this.picView = MCAdViewUtils.createPicView(context, this.adModel);
        addView(this.picView, this.lps);
        this.picView.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        this.progress = new AdProgress(context);
        this.lps = new LayoutParams(MCAdViewUtils.dipToPx(getContext(), 24), MCAdViewUtils.dipToPx(getContext(), 24));
        this.lps.addRule(13, -1);
        addView(this.progress, this.lps);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.picView.loadPic(AdStringUtils.parseImgUrl(this.adModel.getDt(), this.adModel.getPu()), this.progress);
    }

    public void freeMemery() {
        this.picView.recyclePic();
    }
}
