package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.widget.AdImageView;
import com.mobcent.lowest.android.ui.module.ad.widget.BasePicView;
import com.mobcent.lowest.android.ui.widget.gif.GifView;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.model.AdModel;
import com.mobcent.lowest.module.ad.utils.AdStringUtils;

public class MCAdViewUtils implements AdConstant {
    public static int getBannarsHeight(Context context) {
        return (int) (((float) MCPhoneUtil.getDisplayWidth(context)) * AdConstant.RADIO_IMG_AD);
    }

    public static int getSearchImgHeigth(Context context) {
        return (MCPhoneUtil.getDisplayWidth(context) - MCPhoneUtil.dip2px(context, 36.0f)) / 2;
    }

    public static int getBannarsTextHeigth(Context context) {
        return dipToPx(context, 35);
    }

    public static int dipToPx(Context context, int dipValue) {
        return MCPhoneUtil.getRawSize(context, 1, (float) dipValue);
    }

    public static TextView createAdText(Context context) {
        TextView tv = new TextView(context);
        tv.setTextColor(-1);
        tv.setTextSize(14.0f);
        tv.setGravity(17);
        tv.setIncludeFontPadding(false);
        return tv;
    }

    public static TextView createTitleText(Context context) {
        TextView tv = new TextView(context);
        tv.setTextColor(MCResource.getInstance(context).getColor("mc_forum_text4_normal_color"));
        tv.setTextSize(16.0f);
        tv.setSingleLine(true);
        tv.setIncludeFontPadding(false);
        tv.setGravity(19);
        tv.setEllipsize(TruncateAt.END);
        return tv;
    }

    public static TextView createDesText(Context context) {
        TextView tv = new TextView(context);
        tv.setTextColor(MCResource.getInstance(context).getColor("mc_forum_text4_desc_normal_color"));
        tv.setTextSize(12.0f);
        tv.setMaxLines(2);
        tv.setIncludeFontPadding(false);
        tv.setGravity(19);
        return tv;
    }

    public static TextView createLinkText(Context context) {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.parseColor("#54a9e3"));
        tv.getPaint().setUnderlineText(true);
        tv.setGravity(16);
        tv.setSingleLine(false);
        tv.setMaxLines(2);
        tv.setEllipsize(TruncateAt.END);
        tv.setIncludeFontPadding(false);
        tv.setTextSize(16.0f);
        return tv;
    }

    public static View createLogoSimple(Context context) {
        ImageView logo = new ImageView(context);
        logo.setBackgroundResource(MCResource.getInstance(context).getDrawableId("mc_ad_img"));
        return logo;
    }

    public static TextView createLogoText(Context context) {
        TextView tv = new TextView(context);
        tv.setWidth(MCPhoneUtil.dip2px(context, 28.0f));
        tv.setHeight(MCPhoneUtil.dip2px(context, 15.0f));
        tv.setBackgroundResource(MCResource.getInstance(context).getDrawableId("mc_ad_img3"));
        tv.setIncludeFontPadding(false);
        tv.setText(MCResource.getInstance(context).getString("mc_ad_logo_text"));
        tv.setGravity(16);
        tv.setTextColor(-1);
        tv.setTextSize(12.0f);
        return tv;
    }

    public static TextView createLogoRightText(Context context) {
        TextView tv = new TextView(context);
        tv.setWidth(MCPhoneUtil.dip2px(context, 28.0f));
        tv.setHeight(MCPhoneUtil.dip2px(context, 15.0f));
        tv.setBackgroundResource(MCResource.getInstance(context).getDrawableId("mc_ad_img4"));
        tv.setText(MCResource.getInstance(context).getString("mc_ad_logo_text"));
        tv.setGravity(21);
        tv.setIncludeFontPadding(false);
        tv.setTextColor(-1);
        tv.setTextSize(12.0f);
        return tv;
    }

    public static TextView createLogoRightText2(Context context) {
        TextView tv = new TextView(context);
        tv.setWidth(MCPhoneUtil.dip2px(context, 28.0f));
        tv.setHeight(MCPhoneUtil.dip2px(context, 15.0f));
        tv.setBackgroundResource(MCResource.getInstance(context).getDrawableId("mc_ad_img2"));
        tv.setText(MCResource.getInstance(context).getString("mc_ad_logo_text"));
        tv.setGravity(21);
        tv.setIncludeFontPadding(false);
        tv.setTextColor(-1);
        tv.setTextSize(12.0f);
        return tv;
    }

    public static TextView createLogoLeftText(Context context) {
        TextView tv = new TextView(context);
        tv.setWidth(MCPhoneUtil.dip2px(context, 28.0f));
        tv.setHeight(MCPhoneUtil.dip2px(context, 15.0f));
        tv.setBackgroundResource(MCResource.getInstance(context).getDrawableId("mc_ad_img5"));
        tv.setText(MCResource.getInstance(context).getString("mc_ad_logo_text"));
        tv.setGravity(19);
        tv.setIncludeFontPadding(false);
        tv.setTextColor(-1);
        tv.setTextSize(12.0f);
        return tv;
    }

    public static AdImageView createAdImage(Context context) {
        return new AdImageView(context);
    }

    public static BasePicView createPicView(Context context, AdModel adModel) {
        BasePicView basePicView;
        if (AdStringUtils.isGif(adModel.getPu())) {
            basePicView = new GifView(context);
        } else {
            basePicView = new AdImageView(context);
        }
        basePicView.setTag(adModel);
        return basePicView;
    }
}
