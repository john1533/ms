package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.delegate.AdGetDateDelegate;
import com.mobcent.lowest.android.ui.module.ad.helper.AdDataHelper;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewCreateHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.delegate.AdViewDelegate;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class AdCollectionView extends LinearLayout implements AdViewDelegate, Observer, AdConstant {
    private String adTag;
    private int imgAdWidth;
    private int marginTop;
    private String position;
    private int[] positions;
    private int searchAdStyle;
    private int searchLeftRightMargin;

    public AdCollectionView(Context context) {
        this(context, null);
    }

    public AdCollectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.positions = null;
        this.marginTop = 8;
        this.imgAdWidth = 0;
        this.searchAdStyle = 1;
        this.searchLeftRightMargin = 8;
        this.adTag = null;
        this.position = null;
        init(context);
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getImgAdWidth() {
        return this.imgAdWidth;
    }

    public void setImgAdWidth(int imgAdWidth) {
        this.imgAdWidth = imgAdWidth;
    }

    public int getSearchAdStyle() {
        return this.searchAdStyle;
    }

    public void setSearchAdStyle(int searchAdStyle) {
        this.searchAdStyle = searchAdStyle;
    }

    public void setSearchLeftRightMargin(int searchLeftRightMargin) {
        this.searchLeftRightMargin = MCPhoneUtil.dip2px(getContext(), (float) searchLeftRightMargin);
    }

    private void init(Context context) {
        setOrientation(1);
        this.marginTop = MCPhoneUtil.dip2px(getContext(), (float) this.marginTop);
        this.searchLeftRightMargin = MCPhoneUtil.dip2px(getContext(), (float) this.searchLeftRightMargin);
    }

    public void showAd(int[] adPositions) {
        if (adPositions != null && adPositions.length != 0) {
            this.positions = adPositions;
            AdDataHelper.createAdData(getContext(), this.positions, this, new AdGetDateDelegate() {
                public void onAdDateEndCallBack(List<AdContainerModel> adContainerModels) {
                    AdCollectionView.this.createAdItems(adContainerModels);
                }
            });
        }
    }

    public void showAd(final String adTag, int[] adPositions, final String position) {
        this.adTag = adTag;
        this.positions = adPositions;
        this.position = position;
        if (adPositions != null && adPositions.length != 0) {
            Map<String, List<AdContainerModel>> cacheMap = (Map) AdManager.getInstance().getActivityAdContainerCache().get(adTag);
            if (cacheMap == null || cacheMap.get(position) == null) {
                AdDataHelper.createAdData(getContext(), this.positions, this, new AdGetDateDelegate() {
                    public void onAdDateEndCallBack(List<AdContainerModel> adContainerModels) {
                        if (adContainerModels != null && !adContainerModels.isEmpty()) {
                            Map<String, Map<String, List<AdContainerModel>>> adContainerCache = AdManager.getInstance().getActivityAdContainerCache();
                            if (adContainerCache.get(adTag) == null) {
                                adContainerCache.put(adTag, new HashMap());
                            }
                            ((Map) adContainerCache.get(adTag)).put(position, adContainerModels);
                            AdCollectionView.this.createAdItems(adContainerModels);
                        }
                    }
                });
            } else {
                createAdItems((List) cacheMap.get(position));
            }
        }
    }

    private void createAdItems(List<AdContainerModel> adContainerModels) {
        if (adContainerModels != null && !adContainerModels.isEmpty()) {
            free();
            int count = adContainerModels.size();
            for (int i = 0; i < count; i++) {
                AdContainerModel adContainerModel = (AdContainerModel) adContainerModels.get(i);
                adContainerModel.setImgWidth(this.imgAdWidth);
                adContainerModel.setSearchStyle(this.searchAdStyle);
                View view = AdViewCreateHelper.createAdView(getContext(), adContainerModel);
                if (view != null) {
                    LayoutParams lps = new LayoutParams(-1, -2);
                    if (adContainerModel.getDtType() == 6 && this.searchAdStyle == 2) {
                        lps.setMargins(this.searchLeftRightMargin, this.marginTop, this.searchLeftRightMargin, 0);
                    } else {
                        lps.setMargins(0, this.marginTop, 0, 0);
                    }
                    if ((view instanceof MultiTypeView) || (view instanceof TextTypeView)) {
                        RelativeLayout adBox = new RelativeLayout(getContext());
                        TextView logoText = MCAdViewUtils.createLogoRightText2(getContext());
                        RelativeLayout.LayoutParams logoLps = new RelativeLayout.LayoutParams(MCAdViewUtils.dipToPx(getContext(), 28), MCAdViewUtils.dipToPx(getContext(), 15));
                        logoLps.addRule(15, -1);
                        logoLps.addRule(11, -1);
                        logoText.setLayoutParams(logoLps);
                        adBox.addView(view, new RelativeLayout.LayoutParams(-1, -2));
                        adBox.addView(logoText);
                        addView(adBox, lps);
                    } else {
                        addView(view, lps);
                    }
                }
            }
            postInvalidate();
        }
    }

    public void update(Observable observable, Object data) {
        if (this.adTag == null || this.position == null) {
            showAd(this.positions);
        } else {
            showAd(this.adTag, this.positions, this.position);
        }
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
    }

    public void freeMemery() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view != null && (view instanceof AdViewDelegate)) {
                ((AdViewDelegate) view).freeMemery();
            }
        }
    }

    public void free() {
        freeMemery();
        removeAllViews();
    }
}
