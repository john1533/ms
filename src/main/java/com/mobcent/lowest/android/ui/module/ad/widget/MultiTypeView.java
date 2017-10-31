package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.delegate.AdViewDelegate;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.utils.AdStringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiTypeView extends LinearLayout implements AdViewDelegate, AdConstant {
    public String TAG = "MulityTypeView";
    private AdContainerModel adContainerModel;
    private int adPosition;
    private int imgAdWidth;
    private LayoutParams lps;
    private List<BasePicView> picViewList = new ArrayList();

    public MultiTypeView(Context context) {
        super(context);
        setOrientation(0);
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
        this.adContainerModel = adContainerModel;
        this.adPosition = adContainerModel.getPosition();
        this.imgAdWidth = adContainerModel.getImgWidth();
        initView();
    }

    private void initView() {
        switch (this.adContainerModel.getType()) {
            case 1:
                createLongText();
                break;
            case 2:
                createThreeText();
                break;
            case 3:
                createLongImg();
                break;
            case 4:
                createTwoImg();
                break;
        }
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }

    public void createLongText() {
        setBackgroundResource(MCResource.getInstance(getContext()).getDrawableId("mc_ad_line"));
        TextView tv = MCAdViewUtils.createAdText(getContext());
        AdModel adModel = (AdModel) this.adContainerModel.getAdSet().iterator().next();
        adModel.setPo(this.adPosition);
        tv.setText(adModel.getTx());
        tv.setTag(adModel);
        tv.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        this.lps = new LayoutParams(-1, MCAdViewUtils.getBannarsTextHeigth(getContext()));
        this.lps.gravity = 16;
        addView(tv, this.lps);
    }

    public void createThreeText() {
        setBackgroundResource(MCResource.getInstance(getContext()).getDrawableId("mc_ad_line"));
        Iterator<AdModel> iterator = this.adContainerModel.getAdSet().iterator();
        for (int i = 0; i < 3; i++) {
            this.lps = new LayoutParams(0, MCAdViewUtils.getBannarsTextHeigth(getContext()), CustomConstant.RATIO_ONE_HEIGHT);
            this.lps.gravity = 16;
            AdModel adModel = (AdModel) iterator.next();
            adModel.setPo(this.adPosition);
            TextView tv = MCAdViewUtils.createAdText(getContext());
            tv.setText(adModel.getTx());
            tv.setTag(adModel);
            tv.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
            addView(tv, this.lps);
        }
    }

    public void createLongImg() {
        setBackgroundColor(-1);
        AdModel adModel = (AdModel) this.adContainerModel.getAdSet().iterator().next();
        adModel.setPo(this.adPosition);
        BasePicView picView = MCAdViewUtils.createPicView(getContext(), adModel);
        picView.setScaleType(ScaleType.CENTER_CROP);
        picView.setShowWidthReal(this.imgAdWidth);
        picView.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        addView(picView, new LayoutParams(-1, getAdHeight()));
        picView.loadPic(AdStringUtils.parseImgUrl(adModel.getDt(), adModel.getPu()), null);
        this.picViewList.add(picView);
    }

    public void createTwoImg() {
        setBackgroundColor(-1);
        Iterator<AdModel> iterator = this.adContainerModel.getAdSet().iterator();
        for (int i = 0; i < 2; i++) {
            this.lps = new LayoutParams(0, getAdHeight(), CustomConstant.RATIO_ONE_HEIGHT);
            AdModel adModel = (AdModel) iterator.next();
            adModel.setPo(this.adPosition);
            BasePicView picView = MCAdViewUtils.createPicView(getContext(), adModel);
            addView(picView, this.lps);
            picView.setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
            picView.loadPic(AdStringUtils.parseImgUrl(adModel.getDt(), adModel.getPu()), null);
            this.picViewList.add(picView);
        }
    }

    public int getImgAdWidth() {
        return this.imgAdWidth;
    }

    public void setImgAdWidth(int imgAdWidth) {
        this.imgAdWidth = imgAdWidth;
    }

    private int getAdHeight() {
        return this.imgAdWidth == 0 ? MCAdViewUtils.getBannarsHeight(getContext()) : (int) (((float) this.imgAdWidth) * AdConstant.RADIO_IMG_AD);
    }

    public void freeMemery() {
        for (int i = 0; i < this.picViewList.size(); i++) {
            ((BasePicView) this.picViewList.get(i)).recyclePic();
        }
        this.picViewList.clear();
    }
}
