package com.mobcent.discuz.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.android.user.helper.UserManageHelper.ChangeUserInfoListener;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.android.ui.module.weather.helper.WeatherWidgetHelper;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCBitmapUtil;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.weather.model.WeatherModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.List;

public class TopBarWidget extends RelativeLayout {
    public static final int ID_BACK = -1;
    public static final int ID_TITLE = -2;
    public static final int ID_WEATHER = -3;
    public String TAG;
    private final int _ID_TITLE;
    private View arrow;
    private OnClickListener clickListener;
    private LinearLayout leftBox;
    private MyHeadChangeListener listener;
    private Handler mHandler;
    public OnClickListener outSideListener;
    private MCResource resource;
    private LinearLayout rightBox;
    private TextView titleText;
    private int topBarHeight;

    private class MyHeadChangeListener implements ChangeUserInfoListener {
        private View userBtn;

        public MyHeadChangeListener(View userBtn) {
            this.userBtn = userBtn;
        }

        public void change(boolean isLogin, UserInfoModel userInfoModel) {
            if (isLogin) {
                TopBarWidget.this.mHandler.post(new Runnable() {
                    public void run() {
                        TopBarWidget.this.loadHeadIcon(MyHeadChangeListener.this.userBtn);
                    }
                });
            } else {
                TopBarWidget.this.post(new Runnable() {
                    public void run() {
                        MyHeadChangeListener.this.userBtn.setBackgroundDrawable(TopBarWidget.this.resource.getDrawable("mc_forum_top_bar_button6"));
                    }
                });
            }
        }
    }

    public TopBarWidget(Context context) {
        this(context, null);
    }

    public TopBarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "TopBarWidget";
        this.mHandler = new Handler();
        this._ID_TITLE = 11;
        this.topBarHeight = 44;
        this.clickListener = new OnClickListener() {
            public void onClick(View v) {
                if (TopBarWidget.this.outSideListener != null) {
                    TopBarWidget.this.outSideListener.onClick(v);
                }
            }
        };
        init(context);
    }

    private void init(Context context) {
        setGravity(16);
        this.resource = MCResource.getInstance(context);
        this.titleText = new TextView(context);
//        this.titleText.setId(11);
        this.titleText.setSingleLine(true);
        this.titleText.setTextColor(this.resource.getColor("mc_forum_topbar_title_color"));
        this.titleText.setGravity(17);
        this.titleText.setMaxWidth((MCPhoneUtil.getDisplayWidth(context) - dip2px(this.topBarHeight * 4)) - dip2px(10));
        this.titleText.setEllipsize(TruncateAt.END);
        this.titleText.setTextSize(0, getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_19")));
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.action = -2;
        this.titleText.setTag(topBtnModel);
        this.titleText.setOnClickListener(this.clickListener);
        LayoutParams lps = new LayoutParams(-2, -1);
        lps.addRule(13, -1);
        addView(this.titleText, lps);
    }

    private void setTitle(String title) {
        this.titleText.setText(title);
    }

    public void rotateTitleDrawable(boolean arrowDown) {
        if (this.arrow != null) {
            RotateAnimation animation;
            this.arrow.clearAnimation();
            if (arrowDown) {
//                RotateAnimation rotateAnimation = new RotateAnimation(180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
                animation = new RotateAnimation(180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
            } else {
                animation = new RotateAnimation(0.0f, 180.0f, 1, 0.5f, 1, 0.5f);
            }
            animation.setDuration(500);
            animation.setFillAfter(true);
            this.arrow.startAnimation(animation);
        }
    }

    public void resetTopSetting(TopSettingModel topSettingModel) {
        if (topSettingModel == null) {
            MCLogUtil.e(this.TAG, "the TopSettingModel you seted to topbar is null");
            return;
        }
        if (topSettingModel.isTitleClickAble) {
            this.arrow = new View(getContext());
            this.arrow.setBackgroundDrawable(this.resource.getDrawable("mc_forum_top_bar_arrow"));
            LayoutParams lps = new LayoutParams(dip2px(6), dip2px(5));
            lps.addRule(1, this.titleText.getId());
            lps.leftMargin = dip2px(5);
            lps.addRule(15, -1);
            addView(this.arrow, lps);
        }
        setTitle(topSettingModel.title);
        if (topSettingModel.isVisibile) {
            setVisibility(View.VISIBLE);
            if (this.leftBox != null) {
                this.leftBox.removeAllViews();
            }
            if (this.rightBox != null) {
                this.rightBox.removeAllViews();
            }
            if (topSettingModel.style == 1) {
                createPublicLeftBtn(-1);
            }
            createTopView(topSettingModel);
            return;
        }
        setVisibility(View.GONE);
    }

    private void createPublicLeftBtn(int action) {
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.action = action;
        if (action == -1) {
            topBtnModel.icon = "mc_forum_top_bar_button1";
        }
        View view = createTopBtn(topBtnModel);
        if (this.leftBox == null) {
            this.leftBox = createLinearLayout(true);
        }
        this.leftBox.addView(view);
    }

    private void createTopView(TopSettingModel topSettingModel) {
        initTopBtn(topSettingModel.leftModels, true);
        initTopBtn(topSettingModel.rightModels, false);
    }

    private void initTopBtn(List<TopBtnModel> topModels, boolean isLeft) {
        if (topModels == null || topModels.isEmpty()) {
            if (this.leftBox == null) {
                this.leftBox = createLinearLayout(true);
            }
            if (this.rightBox == null) {
                this.rightBox = createLinearLayout(false);
                return;
            }
            return;
        }
        for (int i = 0; i < topModels.size(); i++) {
            View child;
            TopBtnModel topModel = (TopBtnModel) topModels.get(i);
            ConfigComponentModel tag = null;
            if (topModel.tag instanceof ConfigComponentModel) {
                tag = (ConfigComponentModel)topModel.tag;
                if ("weather".equals(tag.getType())) {
                    topModel.action = -3;
                } else if (ConfigConstant.COMPONENT_SIGN.equals(tag.getType())) {
                    topModel.title = this.resource.getString("mc_forum_sign");
                }
            }
            if (topModel.action == -3) {
                child = createWeatherWidget(topModel);
            } else {
                child = createTopBtn(topModel);
            }
            if (isLeft && this.leftBox == null) {
                this.leftBox = createLinearLayout(true);
            }
            if (!isLeft && this.rightBox == null) {
                this.rightBox = createLinearLayout(false);
            }
            if (child != null) {
                if (tag != null && ConfigConstant.COMPONENT_USERINFO.equals(tag.getType())) {
                    if (new UserServiceImpl(getContext().getApplicationContext()).isLogin()) {
                        loadHeadIcon(child);
                    }
                    if (this.listener != null) {
                        UserManageHelper.getInstance(getContext()).removeListener(this.listener);
                    }
                    this.listener = new MyHeadChangeListener(child);
                    UserManageHelper.getInstance(getContext()).registListener(this.listener);
                }
                LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) child.getLayoutParams();
                if (isLeft && this.leftBox.getChildCount() > 0) {
                    lps.leftMargin = MCPhoneUtil.dip2px(5.0f);
                }
                if (!isLeft && this.rightBox.getChildCount() > 0) {
                    lps.leftMargin = MCPhoneUtil.dip2px(5.0f);
                }
                child.setLayoutParams(lps);
                if (isLeft) {
                    this.leftBox.addView(child);
                } else {
                    this.rightBox.addView(child, 0);
                }
            }
        }
    }

    private LinearLayout createLinearLayout(boolean isLeft) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams lps = new LayoutParams(-2, -1);
        linearLayout.setLayoutParams(lps);
        if (isLeft) {
            lps.addRule(9, -1);
        } else {
            lps.addRule(11, -1);
        }
        addView(linearLayout);
        return linearLayout;
    }

    public Button createTopBtn(TopBtnModel topBtnModel) {
        Button btn = new Button(getContext());
        btn.setIncludeFontPadding(false);
        int padding = -dip2px(5);
        btn.setPadding(padding, 0, padding, 0);
        btn.setSingleLine(true);
        btn.setTextColor(this.resource.getColor("mc_forum_topbar_title_color"));
        btn.setGravity(17);
        btn.setTextSize(0, getResources().getDimension(this.resource.getDimenId("mc_forum_text_size_15")));
        btn.setText(topBtnModel.title);
        if (TextUtils.isEmpty(topBtnModel.icon)) {
            btn.setBackgroundResource(this.resource.getDrawableId("mc_forum_top_bar_button2"));
        } else {
            btn.setBackgroundResource(this.resource.getDrawableId(topBtnModel.icon));
        }
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(dip2px(this.topBarHeight), dip2px(this.topBarHeight));
        lps.gravity = Gravity.CENTER_VERTICAL;//16

        btn.setLayoutParams(lps);
        btn.setOnClickListener(this.clickListener);
        btn.setTag(topBtnModel);
        return btn;
    }

    private View createWeatherWidget(TopBtnModel topBtnModel) {
        SettingModel settingModel = UserManageHelper.getInstance(getContext()).getSettingModel();
        List weatherList = LowestManager.getInstance().getConfig().getWeatherListCache();
        int qs = 0;
        if (settingModel != null) {
            qs = settingModel.getAllowCityQueryWeather();
        }
        WeatherModel weatherModel = null;
        if (!MCListUtils.isEmpty(weatherList)) {
            weatherModel = (WeatherModel) weatherList.get(0);
        }
        if (topBtnModel.tag2 instanceof WeatherModel) {
            weatherModel = (WeatherModel)topBtnModel.tag2;
        }
        View child = WeatherWidgetHelper.createWeatherWidget(getContext(), weatherModel, qs);
        child.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        return child;
    }

    public void registerClickListener(OnClickListener clickListener) {
        this.outSideListener = clickListener;
    }

    private int dip2px(int dipValue) {
        return MCPhoneUtil.dip2px(getContext(), (float) dipValue);
    }

    private void loadHeadIcon(final View headView) {
        String headUrl = SharedPreferencesDB.getInstance(getContext().getApplicationContext()).getIcon();
        if (!TextUtils.isEmpty(headUrl)) {
            ImageLoader.getInstance().loadImage(headUrl, new ImageSize(headView.getWidth(), headView.getHeight()), new SimpleImageLoadingListener() {
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    headView.setBackgroundResource(TopBarWidget.this.resource.getDrawableId("mc_forum_top_bar_button6"));
                }

                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (loadedImage == null || loadedImage.isRecycled()) {
                        headView.setBackgroundResource(TopBarWidget.this.resource.getDrawableId("mc_forum_top_bar_button6"));
                        return;
                    }
                    int imgWidth = TopBarWidget.this.dip2px(TopBarWidget.this.topBarHeight);
                    Bitmap currentIconBitmap = MCBitmapUtil.createRoundHeadIcon(TopBarWidget.this.getContext(), (float) imgWidth, (float) imgWidth, TopBarWidget.this.dip2px(26), loadedImage);
                    if (currentIconBitmap != null) {
                        headView.setBackgroundDrawable(new BitmapDrawable(TopBarWidget.this.getResources(), currentIconBitmap));
                    } else {
                        headView.setBackgroundResource(TopBarWidget.this.resource.getDrawableId("mc_forum_top_bar_button6"));
                    }
                }

                public void onLoadingCancelled(String imageUri, View view) {
                    headView.setBackgroundResource(TopBarWidget.this.resource.getDrawableId("mc_forum_top_bar_button6"));
                }
            });
        }
    }
}
