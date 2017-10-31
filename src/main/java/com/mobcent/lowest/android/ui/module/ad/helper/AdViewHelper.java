package com.mobcent.lowest.android.ui.module.ad.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.ad.activity.AdWebViewActivity;
import com.mobcent.lowest.module.ad.cache.AdDatasCache;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.manager.AdManager;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdIntentModel;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.model.AdOtherModel;
import com.mobcent.lowest.module.ad.service.AdService;
import com.mobcent.lowest.module.ad.service.impl.AdServiceImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AdViewHelper implements AdConstant {
    private static AdViewHelper helper;
    public String TAG = "AdViewHelper";
    private AdService adService;
    private OnClickListener adViewClickListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = null;
            if (v.getTag() instanceof AdModel) {
                AdModel adModel = (AdModel) v.getTag();
                Context context = v.getContext();
                String url;
                AdIntentModel adIntentModel;
                switch (adModel.getGt()) {
                    case 1:
                        url = AdViewHelper.this.adService.getLinkUrl(adModel.getAid(), (long) adModel.getPo(), adModel.getDu());
                        intent = new Intent(context, AdWebViewActivity.class);
                        adIntentModel = new AdIntentModel();
                        adIntentModel.setAid(adModel.getAid());
                        adIntentModel.setPo(adModel.getPo());
                        adIntentModel.setUrl(url);
                        intent.putExtra(AdWebViewActivity.AD_INTENT_MODEL, adIntentModel);
                        break;
                    case 2:
                        url = AdViewHelper.this.adService.getLinkUrl(adModel.getAid(), (long) adModel.getPo(), adModel.getDu());
                        intent = new Intent(context, AdWebViewActivity.class);
                        adIntentModel = new AdIntentModel();
                        adIntentModel.setAid(adModel.getAid());
                        adIntentModel.setPo(adModel.getPo());
                        adIntentModel.setUrl(url);
                        intent.putExtra(AdWebViewActivity.AD_INTENT_MODEL, adIntentModel);
                        break;
                    case 3:
                        AdViewHelper.this.exposureAdDelayASync(AdViewHelper.this.adService.getLinkUrl(adModel.getAid(), (long) adModel.getPo(), adModel.getDu()));
                        intent = new Intent("android.intent.action.VIEW", Uri.parse("tel:" + adModel.getTel()));
                        break;
                    case 4:
                        AdViewHelper.this.exposureAdDelayASync(AdViewHelper.this.adService.getLinkUrl(adModel.getAid(), (long) adModel.getPo(), adModel.getDu()));
                        intent = new Intent("android.intent.action.VIEW", Uri.parse("smsto:" + adModel.getTel()));
                        intent.putExtra("sms_body", adModel.getTx());
                        break;
                }
                if (intent != null) {
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        try {
                            intent.addFlags(268435456);
                            context.startActivity(intent);
                        } catch (Exception e2) {
                        }
                    }
                }
                AdDatasCache adCache = AdManager.getInstance().getAdDatasCache();
                if (!(adCache == null || adCache.getAdDataMap().get(Integer.valueOf(adModel.getDt())) == null)) {
                    HashMap<Long, AdModel> adMap = (HashMap) adCache.getAdDataMap().get(Integer.valueOf(adModel.getDt()));
                    if (adMap.get(Long.valueOf(adModel.getAid())) != null) {
                        ((AdModel) adMap.get(Long.valueOf(adModel.getAid()))).setConsumed(true);
                    }
                }
                if (adModel.isOther() && adModel.getOthers().size() > 0) {
                    List<AdOtherModel> others = adModel.getOthers();
                    for (int i = 0; i < others.size(); i++) {
                        AdOtherModel other = (AdOtherModel) others.get(i);
                        if (other.getType() == 2) {
                            AdViewHelper.this.exposureAdDelay(other);
                        }
                    }
                }
            }
        }
    };

    public AdViewHelper(Context context) {
        this.adService = new AdServiceImpl(context);
    }

    public static AdViewHelper getInstance(Context context) {
        if (helper == null) {
            helper = new AdViewHelper(context);
        }
        return helper;
    }

    public OnClickListener getAdViewClickListener() {
        return this.adViewClickListener;
    }

    public void setAdViewClickListener(OnClickListener adViewClickListener) {
        this.adViewClickListener = adViewClickListener;
    }

    public void tip(Context context, String msg) {
        Toast.makeText(context, msg, 500).show();
    }

    public void exposureAd(AdContainerModel adContainerModel) {
        if (adContainerModel != null && adContainerModel.getAdSet().size() != 0) {
            Iterator<AdModel> iterator = adContainerModel.getAdSet().iterator();
            while (iterator.hasNext()) {
                AdModel adModel = (AdModel) iterator.next();
                Set<Long> aidSet = AdManager.getInstance().getExposureSet();
                if (!aidSet.contains(Long.valueOf(adModel.getAid())) && adModel.isOther() && adModel.getOthers().size() > 0) {
                    aidSet.add(Long.valueOf(adModel.getAid()));
                    List<AdOtherModel> others = adModel.getOthers();
                    for (int i = 0; i < others.size(); i++) {
                        AdOtherModel other = (AdOtherModel) others.get(i);
                        if (other.getType() == 1) {
                            exposureAdDelay(other);
                        }
                    }
                }
            }
        }
    }

    private void exposureAdDelay(final AdOtherModel other) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                AdViewHelper.this.exposureAdDelayASync(other.getUrl());
            }
        }, other.getDelay());
    }

    private void exposureAdDelayASync(final String url) {
        new Thread() {
            public void run() {
                AdViewHelper.this.adService.exposureAd(url);
            }
        }.start();
    }
}
