package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.delegate.AdDelegate;
import com.mobcent.lowest.android.ui.module.ad.delegate.AdGetDateDelegate;
import com.mobcent.lowest.android.ui.module.ad.helper.AdDataHelper;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewCreateHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
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
import java.util.Timer;
import java.util.TimerTask;

public class AdView extends RelativeLayout implements Observer, AdConstant {
    private String TAG = "AdView";
    private AdDelegate adDelegate;
    private AdManager adManager;
    private int adPosition;
    private long delay = -1;
    private Handler handler = new Handler();
    private boolean hideExtendImg;
    private int imgAdWidth = 0;
    private boolean isTimerTaskRun = false;
    private int itemPosition = -1;
    private String listTag = null;
    private int searchAdStyle = 1;
    private int searchLeftRightMargin = 8;
    private Timer timer = null;
    private TimerTask timerTask = new TimerTask() {
        public void run() {
            AdView.this.handler.post(new Runnable() {
                public void run() {
                    AdView.this.showAd(AdView.this.adPosition, AdView.this.delay);
                }
            });
        }
    };

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.adManager = AdManager.getInstance();
        this.imgAdWidth = MCPhoneUtil.getDisplayWidth(getContext());
        this.searchLeftRightMargin = dip2px(this.searchLeftRightMargin);
        setVisibility(GONE);
    }

    public synchronized void showAd(int adPosition) {
        this.adPosition = adPosition;
        clearSource();
        try {
            requestAd();
        } catch (Exception e) {
            MCLogUtil.e(this.TAG, getErrorInfo(e));
        }
    }

    public synchronized void showAd(int adPosition, long delay) {
        this.adPosition = adPosition;
        this.delay = delay;
        clearSource();
        if (this.timer == null) {
            this.timer = new Timer();
        }
        try {
            requestAd();
        } catch (Exception e) {
            MCLogUtil.e(this.TAG, getErrorInfo(e));
        }
    }

    public synchronized void showAd(String tag, int adPosition, int itemPosition) {
        this.adPosition = adPosition;
        this.itemPosition = itemPosition;
        this.listTag = tag;
        clearSource();
        Map<String, Map<Integer, AdContainerModel>> activityCache = this.adManager.getActivityAdCache();
        if (activityCache.get(this.listTag) == null) {
            activityCache.put(this.listTag, new HashMap());
        }
        Map<Integer, AdContainerModel> adCache = (Map) activityCache.get(this.listTag);
        if (adCache.get(Integer.valueOf(itemPosition)) != null) {
            createAdViewByCache((AdContainerModel) adCache.get(Integer.valueOf(itemPosition)));
        } else {
            try {
                requestAd();
            } catch (Exception e) {
                MCLogUtil.e(this.TAG, getErrorInfo(e));
            }
        }
    }

    private void requestAd() {
        AdDataHelper.createAdData(getContext(), new int[]{this.adPosition}, this, new AdGetDateDelegate() {
            public void onAdDateEndCallBack(List<AdContainerModel> adContainerModels) {
                if (adContainerModels != null && !adContainerModels.isEmpty()) {
                    AdView.this.createAdView((AdContainerModel) adContainerModels.get(0));
                }
            }
        });
    }

    private void createAdView(AdContainerModel adContainerModel) {
        if (adContainerModel != null) {
            adContainerModel.setSearchStyle(this.searchAdStyle);
            adContainerModel.setImgWidth(this.imgAdWidth);
        }
        View view = AdViewCreateHelper.createAdView(getContext(), adContainerModel);
        if (!(adContainerModel == null || this.itemPosition == -1 || this.listTag == null)) {
            Map<Integer, AdContainerModel> adCache = (Map) this.adManager.getActivityAdCache().get(this.listTag);
            if (adCache == null) {
                this.adManager.getActivityAdCache().put(this.listTag, new HashMap());
            }
            try {
                adCache.put(Integer.valueOf(this.itemPosition), adContainerModel);
            } catch (Exception e) {
                MCLogUtil.e(this.TAG, getErrorInfo(e));
                free();
                return;
            }
        }
        initAdExtendView(view, adContainerModel);
    }

    public void createAdViewByCache(AdContainerModel adContainerModel) {
        initAdExtendView(AdViewCreateHelper.createAdView(getContext(), adContainerModel), adContainerModel);
    }

    private void initAdExtendView(View view, AdContainerModel adContainerModel) {
        if (view == null) {
            setVisibility(GONE);
            if (this.adDelegate != null) {
                this.adDelegate.onAdShow(false, adContainerModel);
                return;
            }
            return;
        }
        if (adContainerModel.getDtType() == 6 && this.searchAdStyle == 2) {
            setPadding(this.searchLeftRightMargin, 0, this.searchLeftRightMargin, 0);
        }
        addView(view);
        addLogoView(view);
        postInvalidate();
        setVisibility(VISIBLE);
        if (this.adDelegate != null) {
            this.adDelegate.onAdShow(true, adContainerModel);
        }
        if (view != null && this.delay != -1 && !this.isTimerTaskRun) {
            this.isTimerTaskRun = true;
            try {
                this.timer.schedule(this.timerTask, 2000, this.delay);
            } catch (Exception e) {
                MCLogUtil.e(this.TAG, getErrorInfo(e));
            }
        }
    }

    private void addLogoView(View view) {
        if (view != null && !this.hideExtendImg) {
            if ((view instanceof MultiTypeView) || (view instanceof TextTypeView)) {
                addView(createLogoRightCenter());
            } else if (view instanceof BigPicTypeView) {
                addView(createLogoRightTop());
            }
        }
    }

    private View createLogoRightCenter() {
        TextView logoText = MCAdViewUtils.createLogoRightText2(getContext());
        LayoutParams lps = new LayoutParams(MCAdViewUtils.dipToPx(getContext(), 28), MCAdViewUtils.dipToPx(getContext(), 15));
        lps.addRule(15, -1);
        lps.addRule(11, -1);
        logoText.setLayoutParams(lps);
        return logoText;
    }

    private View createLogoRightTop() {
        View logo = MCAdViewUtils.createLogoSimple(getContext());
        LayoutParams lps = new LayoutParams(MCAdViewUtils.dipToPx(getContext(), 32), MCAdViewUtils.dipToPx(getContext(), 11));
        lps.addRule(10, -1);
        lps.addRule(11, -1);
        logo.setLayoutParams(lps);
        return logo;
    }

    private synchronized void clearSource() {
        View contentView = getChildAt(0);
        if ((contentView instanceof AdViewDelegate) && contentView != null) {
            ((AdViewDelegate) contentView).freeMemery();
        }
        removeAllViews();
    }

    public void hideExtendImg(boolean isHide) {
        this.hideExtendImg = isHide;
    }

    public void free() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timerTask.cancel();
            this.isTimerTaskRun = false;
        }
        this.adManager.removeObserver(this);
        clearSource();
        setVisibility(GONE);
    }

    public void setSearchViewStyle(int style) {
        this.searchAdStyle = style;
    }

    public void setSearchLeftRightMargin(int margin) {
        this.searchLeftRightMargin = dip2px(margin);
    }

    public void setImgAdWidth(int width) {
        this.imgAdWidth = width;
    }

    public AdDelegate getAdDelegate() {
        return this.adDelegate;
    }

    public void hideLogoImg(boolean hideLogo) {
        this.hideExtendImg = hideLogo;
    }

    public void setAdDelegate(AdDelegate adDelegate) {
        this.adDelegate = adDelegate;
    }

    private String getErrorInfo(Exception e) {
        String error = "";
        StackTraceElement[] stacks = e.getStackTrace();
        if (stacks.length > 0) {
            error = stacks[1].getClassName() + "\n" + stacks[1].getMethodName() + "\n" + stacks[1].getLineNumber();
        }
        return error + "\n" + e.toString();
    }

    public void update(Observable observable, Object data) {
        if (this.listTag != null) {
            showAd(this.listTag, this.adPosition, this.itemPosition);
        } else if (this.delay != -1) {
            showAd(this.adPosition, this.delay);
        } else {
            showAd(this.adPosition);
        }
    }

    private int dip2px(int dip) {
        return MCPhoneUtil.dip2px(getContext(), (float) dip);
    }
}
