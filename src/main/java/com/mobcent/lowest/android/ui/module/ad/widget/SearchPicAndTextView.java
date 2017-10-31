package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.android.ui.module.ad.helper.AdViewHelper;
import com.mobcent.lowest.android.ui.utils.MCAdViewUtils;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.delegate.AdViewDelegate;
import com.mobcent.lowest.module.ad.model.AdContainerModel;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.utils.AdStringUtils;

public class SearchPicAndTextView extends RelativeLayout implements AdViewDelegate, AdConstant {
    private Context context;
    private LinearLayout.LayoutParams lllps = null;
    private BasePicView preImg;
    private MCResource resource;
    private RelativeLayout.LayoutParams rlps = null;
    private int style;

    public SearchPicAndTextView(Context context) {
        super(context);
        this.context = context;
        this.resource = MCResource.getInstance(context);
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
        AdModel adModel = (AdModel) adContainerModel.getAdSet().iterator().next();
        adModel.setPo(adContainerModel.getPosition());
        this.style = adContainerModel.getSearchStyle();
        if (this.style == 2) {
            createStyle2Box(adModel);
        } else {
            createStyle1Box(adModel);
        }
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }

    public void createStyle1Box(AdModel adModel) {
        LinearLayout topBox = new LinearLayout(this.context);
        topBox.setId(1);
        topBox.setOrientation(0);
        topBox.setGravity(16);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(-1, -2);
        lps.topMargin = getPx(10);
        lps.rightMargin = getPx(10);
        LinearLayout topLeftBox = new LinearLayout(this.context);
        topLeftBox.setOrientation(1);
        LinearLayout topLeftTopBox = new LinearLayout(this.context);
        topLeftTopBox.setGravity(16);
        topLeftTopBox.setOrientation(0);

//        LayoutParams layoutParams = new LayoutParams(-1, -2);

        topLeftTopBox.addView(MCAdViewUtils.createLogoLeftText(this.context), new LinearLayout.LayoutParams(getPx(28), getPx(15), 0.0f));
        TextView titleTextView = MCAdViewUtils.createTitleText(this.context);
        titleTextView.setText(adModel.getTx());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2, CustomConstant.RATIO_ONE_HEIGHT);
        layoutParams.leftMargin = getPx(4);
        topLeftTopBox.addView(titleTextView, layoutParams);

        topLeftBox.addView(topLeftTopBox, new LayoutParams(-1, -2));
        if (!MCStringUtil.isEmpty(adModel.getDx())) {
            TextView desText = MCAdViewUtils.createDesText(this.context);
            desText.setText(adModel.getDx());

//            LayoutParams layoutParams = new LayoutParams(-1, -2);
            LayoutParams relativeParams = new LayoutParams(-1, -2);
            layoutParams.topMargin = getPx(7);
            layoutParams.leftMargin = getPx(9);
            topLeftBox.addView(desText, relativeParams);
        }
        layoutParams = new LinearLayout.LayoutParams(-1, -2, CustomConstant.RATIO_ONE_HEIGHT);
        layoutParams.gravity = 16;
        layoutParams.rightMargin = getPx(9);
        topBox.addView(topLeftBox, layoutParams);
        if (!MCStringUtil.isEmpty(adModel.getPu())) {
            this.preImg = new AdImageView(this.context);
            this.preImg.setScaleType(ScaleType.CENTER_CROP);
            layoutParams = new LinearLayout.LayoutParams(getPx(66), getPx(66), 0.0f);
            this.preImg.loadPic(AdStringUtils.parseImgUrl(adModel.getDt(), adModel.getPu()), null);
            topBox.addView(this.preImg, layoutParams);
        }
        AdModel topModel = adModel.clone();
        if (topModel.getGt() == 1 && !MCStringUtil.isEmpty(topModel.getAdu())) {
            topModel.setGt(2);
            topModel.setDu(adModel.getAdu());
        }
        topBox.setTag(topModel);
        topBox.setOnClickListener(AdViewHelper.getInstance(this.context).getAdViewClickListener());
        addView(topBox, lps);
        View bottomBox = createBottomBox(adModel);
        if (bottomBox != null) {
            lps = new RelativeLayout.LayoutParams(-1, getPx(34));
            lps.addRule(3, topBox.getId());
            addView(bottomBox, lps);
        }
    }

    public void createStyle2Box(AdModel adModel) {
        setBackgroundResource(this.resource.getDrawableId("mc_forum_card_bg2"));
        LinearLayout topBox = new LinearLayout(this.context);
        topBox.setPadding(getPx(11), getPx(10), getPx(10), getPx(13));
        topBox.setId(1);
        topBox.setOrientation(1);
        TextView titleText = MCAdViewUtils.createTitleText(this.context);
        titleText.setPadding(0, 0, getPx(28), 0);
        titleText.setText(adModel.getTx());
        this.lllps = new LinearLayout.LayoutParams(-2, -2);
        topBox.addView(titleText, this.lllps);
        if (!MCStringUtil.isEmpty(adModel.getPu())) {
            this.preImg = MCAdViewUtils.createPicView(this.context, adModel);
            this.preImg.setScaleType(ScaleType.CENTER_CROP);
            this.lllps = new LinearLayout.LayoutParams(-1, (MCPhoneUtil.getDisplayWidth(this.context) - getPx(38)) / 2);
            this.lllps.topMargin = getPx(11);
            this.lllps.gravity = 16;
            this.preImg.loadPic(AdStringUtils.parseImgUrl(adModel.getDt(), adModel.getPu()), null);
            topBox.addView(this.preImg, this.lllps);
        }
        if (!MCStringUtil.isEmpty(adModel.getDx())) {
            TextView desText = MCAdViewUtils.createDesText(this.context);
            desText.setText(adModel.getDx());
            LayoutParams lllps = new LayoutParams(-1, -2);
            if (MCStringUtil.isEmpty(adModel.getPu())) {
                lllps.topMargin = getPx(15);
            } else {
                lllps.topMargin = getPx(13);
            }
            topBox.addView(desText, lllps);
        }


        AdModel topModel = adModel.clone();
        if (topModel.getGt() == 1 && !MCStringUtil.isEmpty(topModel.getAdu())) {
            topModel.setGt(2);
            topModel.setDu(adModel.getAdu());
        }
        topBox.setTag(topModel);
        topBox.setOnClickListener(AdViewHelper.getInstance(this.context).getAdViewClickListener());
        this.rlps = new RelativeLayout.LayoutParams(-1, -2);
        addView(topBox, this.lllps);
        View bottomBox = createBottomBox(adModel);
        if (bottomBox != null) {
            View line = new View(this.context);
            line.setBackgroundResource(this.resource.getDrawableId("mc_forum_wire"));
            line.setId(2);
            this.rlps = new RelativeLayout.LayoutParams(-1, 1);
            this.rlps.addRule(3, topBox.getId());
            addView(line, this.rlps);
            this.rlps = new RelativeLayout.LayoutParams(-1, getPx(40));
            this.rlps.addRule(3, line.getId());
            addView(bottomBox, this.rlps);
        }
        TextView logoText = MCAdViewUtils.createLogoRightText(this.context);
        this.rlps = new RelativeLayout.LayoutParams(getPx(28), getPx(15));
        this.rlps.topMargin = getPx(11);
        this.rlps.addRule(11, -1);
        addView(logoText, this.rlps);
    }

    public View createBottomBox(AdModel adModel) {
        View view = null;
        if (!(adModel.getGt() == 2 && MCStringUtil.isEmpty(adModel.getTel()))) {
            view = LayoutInflater.from(this.context).inflate(this.resource.getLayoutId("mc_ad_search_item_bottom_layout"), null);
            RelativeLayout downBox = (RelativeLayout) view.findViewById(this.resource.getViewId("down_layout"));
            final Button downBtn = (Button) view.findViewById(this.resource.getViewId("down_btn"));
            View line = view.findViewById(this.resource.getViewId("line_view"));
            RelativeLayout callBox = (RelativeLayout) view.findViewById(this.resource.getViewId("call_layout"));
            final Button callBtn = (Button) view.findViewById(this.resource.getViewId("call_btn"));
            if (adModel.getGt() == 2) {
                downBox.setVisibility(8);
                line.setVisibility(8);
            } else {
                adModel.setGt(1);
                downBox.setTag(adModel.clone());
                downBox.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == 0) {
                            downBtn.setSelected(true);
                        } else if (event.getAction() == 1 || event.getAction() == 3) {
                            downBtn.setSelected(false);
                        }
                        return false;
                    }
                });
                downBox.setOnClickListener(AdViewHelper.getInstance(this.context).getAdViewClickListener());
            }
            if (MCStringUtil.isEmpty(adModel.getTel())) {
                callBox.setVisibility(8);
                line.setVisibility(8);
            } else {
                AdModel callModel = adModel.clone();
                callModel.setGt(3);
                callBox.setTag(callModel);
                callBox.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == 0) {
                            callBtn.setSelected(true);
                        } else if (event.getAction() == 1 || event.getAction() == 3) {
                            callBtn.setSelected(false);
                        }
                        return false;
                    }
                });
                callBox.setOnClickListener(AdViewHelper.getInstance(this.context).getAdViewClickListener());
            }
        }
        return view;
    }

    public void freeMemery() {
        if (this.preImg != null) {
            this.preImg.recyclePic();
        }
    }

    private int getPx(int dip) {
        return MCPhoneUtil.dip2px(this.context, (float) dip);
    }
}
