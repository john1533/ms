package com.mobcent.lowest.android.ui.module.ad.widget;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
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

public class PicAndTextView extends RelativeLayout implements AdViewDelegate, AdConstant {
    private Context context;
    private BasePicView img;
    private MCResource resource;

    public PicAndTextView(Context context) {
        super(context);
        this.context = context;
        this.resource = MCResource.getInstance(context);
        setBackgroundResource(this.resource.getDrawableId("mc_forum_card_bg11"));
    }

    public void setAdContainerModel(AdContainerModel adContainerModel) {
        LayoutParams lps;
        AdModel adModel = (AdModel) adContainerModel.getAdSet().iterator().next();
        adModel.setPo(adContainerModel.getPosition());
        if (!MCStringUtil.isEmpty(adModel.getPu())) {
            this.img = MCAdViewUtils.createPicView(this.context, adModel);
            this.img.setScaleType(ScaleType.CENTER_CROP);
            this.img.loadPic(AdStringUtils.parseImgUrl(adModel.getDt(), adModel.getPu()), null);
            this.img.setId(1);
            lps = new LayoutParams(getPx(50), getPx(50));
            lps.setMargins(getPx(10), getPx(10), getPx(10), getPx(10));
            addView(this.img, lps);
        }
        LinearLayout contentBox = new LinearLayout(this.context);
        contentBox.setOrientation(1);
        lps = new LayoutParams(-1, -2);
        if (this.img == null) {
            lps.setMargins(getPx(10), getPx(10), getPx(10), getPx(10));
        } else {
            setPadding(0, 0, 0, getPx(10));
            lps.addRule(1, this.img.getId());
        }
        lps.addRule(15, -1);
        addView(contentBox, lps);
        TextView titleText = MCAdViewUtils.createTitleText(this.context);
        titleText.setId(2);
        titleText.setPadding(0, 0, getPx(28), 0);
        titleText.setText(adModel.getTx());
        contentBox.addView(titleText, new LinearLayout.LayoutParams(-1, -2));
        if (!MCStringUtil.isEmpty(adModel.getDx())) {
            TextView desText = MCAdViewUtils.createDesText(this.context);
            desText.setText(adModel.getDx());
            LinearLayout.LayoutParams tLps = new LinearLayout.LayoutParams(-1, -2);
            tLps.topMargin = getPx(4);
            contentBox.addView(desText, tLps);
        }
        TextView logoText = MCAdViewUtils.createLogoRightText(this.context);
        lps = new LayoutParams(getPx(28), getPx(15));
        lps.topMargin = getPx(10);
        lps.addRule(11, -1);
        addView(logoText, lps);
        setTag(adModel);
        setOnClickListener(AdViewHelper.getInstance(getContext()).getAdViewClickListener());
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    }

    public void freeMemery() {
        if (this.img != null) {
            this.img.recyclePic();
        }
    }

    private int getPx(int dip) {
        return MCPhoneUtil.dip2px(this.context, (float) dip);
    }
}
