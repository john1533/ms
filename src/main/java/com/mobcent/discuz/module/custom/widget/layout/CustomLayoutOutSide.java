package com.mobcent.discuz.module.custom.widget.layout;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.mobcent.discuz.android.model.ConfigComponentHeaderModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.module.custom.widget.CustomBaseText;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.custom.widget.delegate.CustomStateDelegate;
import com.mobcent.discuz.module.custom.widget.delegate.CustomViewClickListener;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomDispatchView;
import com.mobcent.discuz.module.custom.widget.dispatch.CustomHelper;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;

public class CustomLayoutOutSide extends LinearLayout implements CustomStateDelegate, CustomConstant {
    public String TAG;
    private CustomBaseText bottomMoreText;
    private RelativeLayout bottomTitleBox;
    private CustomBaseText bottomTitleText;
    private LinearLayout containerBox;
    private LayoutParams lps;
    private Handler mHandler;
    private MCResource resource;
    private int timeDelay;
    private CustomBaseText topMoreText;
    private RelativeLayout topTitleBox;
    private CustomBaseText topTitleText;

    public CustomLayoutOutSide(Context context) {
        this(context, null);
    }

    public CustomLayoutOutSide(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.TAG = "CustomLayoutOutSide";
        this.timeDelay = 0;
        this.mHandler = new Handler();
        setOrientation(VERTICAL);
        this.resource = MCResource.getInstance(context);
        setBackgroundColor(-1);
        createBaseView();
    }

    private void createBaseView() {
        this.topTitleBox = new RelativeLayout(getContext());
        createTitleBox(this.topTitleBox, true);
        this.lps = new LayoutParams(-1, MCPhoneUtil.dip2px(getContext(), 33.0f));
        addView(this.topTitleBox, this.lps);
        this.containerBox = new LinearLayout(getContext());
        this.containerBox.setOrientation(VERTICAL);
        this.lps = new LayoutParams(-1, -2);
        addView(this.containerBox, this.lps);
        this.bottomTitleBox = new RelativeLayout(getContext());
        createTitleBox(this.bottomTitleBox, false);
        this.lps = new LayoutParams(-1, MCPhoneUtil.dip2px(getContext(), 33.0f));
        addView(this.bottomTitleBox, this.lps);
    }

    private void dealTitleBox(ConfigComponentModel component) {
        if (component.getHeaderModel() != null) {
            ConfigComponentHeaderModel headerModel = component.getHeaderModel();
            if (headerModel.isShowTitle()) {
                CustomBaseText titleText;
                CustomBaseText moreText;
                if (headerModel.getPosition() == 0) {
                    this.bottomTitleBox.setVisibility(VISIBLE);
                    this.topTitleBox.setVisibility(GONE);
                    titleText = this.bottomTitleText;
                    moreText = this.bottomMoreText;
                } else {
                    this.topTitleBox.setVisibility(VISIBLE);
                    this.bottomTitleBox.setVisibility(GONE);
                    titleText = this.topTitleText;
                    moreText = this.topMoreText;
                }
                if (headerModel.isShowMore()) {
                    moreText.setVisibility(VISIBLE);
                    if (headerModel.getMoreComponent() != null) {
                        moreText.setOnClickListener(new CustomViewClickListener(getContext(), headerModel.getMoreComponent()));
                    }
                } else {
                    moreText.setVisibility(GONE);
                }
                titleText.setText(headerModel.getTitle());
                return;
            }
            this.topTitleBox.setVisibility(GONE);
            this.bottomTitleBox.setVisibility(GONE);
        }
    }

    public void initViews(ConfigComponentModel component, boolean isAnim) {
        dealTitleBox(component);
        if (!MCListUtils.isEmpty(component.getComponentList())) {
            if (this.containerBox != null) {
                onPause();
                this.containerBox.removeAllViews();
            }
            this.timeDelay = 50;
            final int count = component.getComponentList().size();
            for (int i = 0; i < count; i++) {
                final int position = i;
                if (isAnim) {
                    final ConfigComponentModel configComponentModel = component;
                    final boolean z = isAnim;
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            CustomLayoutOutSide.this.addItem(configComponentModel, position, count, z);
                        }
                    }, (long) this.timeDelay);
                    this.timeDelay += 50;
                } else {
                    addItem(component, position, count, isAnim);
                }
            }
        }
    }

    private void addItem(ConfigComponentModel component, int position, int count, boolean isAnim) {
        View view = CustomDispatchView.dispatchLayout(getContext(), (ConfigComponentModel) component.getComponentList().get(position), component.getStyle(), isAnim);
        if (view != null) {
            ViewGroup.LayoutParams lps = view.getLayoutParams();
            LayoutParams llps = new LayoutParams(-1, -2);
            if (lps != null) {
                llps.width = lps.width;
                llps.height = lps.height;
            }
            if (position != 0) {
                if (CustomConstant.STYLE_LAYOUT_STYLE_IMAGE.equals(component.getStyle())) {
                    llps.topMargin = -MCPhoneUtil.dip2px(getContext(), 5.0f);
                } else if (!CustomConstant.STYLE_LAYOUT_STYLE_LINE.equals(component.getStyle())) {
                    llps.topMargin = -MCPhoneUtil.dip2px(getContext(), 8.0f);
                }
                view.setLayoutParams(llps);
            }
            this.containerBox.addView(view);
            if (CustomConstant.STYLE_LAYOUT_STYLE_LINE.equals(component.getStyle()) && position != count - 1) {
                LayoutParams lineLps = new LayoutParams(-1, 1);
                lineLps.leftMargin = MCPhoneUtil.dip2px(getContext(), 7.0f);
                lineLps.rightMargin = MCPhoneUtil.dip2px(getContext(), 7.0f);
                this.containerBox.addView(CustomHelper.createLineView(getContext(), lineLps));
            }
            if (view instanceof CustomStateDelegate) {
                ((CustomStateDelegate) view).onResume();
            }
        }
    }

    private void createTitleBox(RelativeLayout parent, boolean isTop) {
        if (isTop) {
            RelativeLayout.LayoutParams lps = createRLps();
            lps.addRule(11, -1);
            this.topMoreText = CustomHelper.createMoreTitle(getContext());
            this.topMoreText.setId(1);
            this.topMoreText.setText(getMoreText());
            this.topMoreText.setLayoutParams(lps);
            parent.addView(this.topMoreText);
            lps = createRLps();
            lps.width = -1;
            lps.addRule(0, this.topMoreText.getId());
            lps.addRule(9, -1);
            this.topTitleText = CustomHelper.createTitle(getContext());
            this.topTitleText.setLayoutParams(lps);
            parent.addView(this.topTitleText);
            View line = new View(getContext());
            line.setBackgroundDrawable(this.resource.getDrawable("mc_forum_wire"));
            lps = new RelativeLayout.LayoutParams(-1, 1);
            lps.addRule(12, -1);
            parent.addView(line, lps);
            return;
        }
        RelativeLayout.LayoutParams lps = createRLps();
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
        this.bottomMoreText = CustomHelper.createMoreTitle(getContext());
        this.bottomMoreText.setId(2);
        this.bottomMoreText.setLayoutParams(lps);
        this.bottomMoreText.setText(getMoreText());
        parent.addView(this.bottomMoreText);
        lps = createRLps();
        lps.width = -1;
        lps.addRule(9, -1);
        lps.addRule(0, this.bottomMoreText.getId());
        this.bottomTitleText = CustomHelper.createTitle(getContext());
        this.bottomTitleText.setLayoutParams(lps);
        parent.addView(this.bottomTitleText);
        View line = new View(getContext());
        line.setBackgroundDrawable(this.resource.getDrawable("mc_forum_wire"));
        lps = new RelativeLayout.LayoutParams(-1, 1);
        lps.addRule(10, -1);
        parent.addView(line, lps);
    }

    private RelativeLayout.LayoutParams createRLps() {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(-2, -2);
        lps.addRule(15, -1);
        return lps;
    }

    public void onResume() {
        dealState(true);
    }

    public void onPause() {
        dealState(false);
    }

    private void dealState(boolean isResume) {
        int count = this.containerBox.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = this.containerBox.getChildAt(i);
            if (view instanceof CustomStateDelegate) {
                CustomStateDelegate delegate = (CustomStateDelegate) view;
                if (isResume) {
                    delegate.onResume();
                } else {
                    delegate.onPause();
                }
            }
        }
    }

    private String getMoreText() {
        return this.resource.getString("mc_forum_custom_more");
    }
}
