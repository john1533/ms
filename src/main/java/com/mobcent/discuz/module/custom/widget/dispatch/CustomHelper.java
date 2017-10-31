package com.mobcent.discuz.module.custom.widget.dispatch;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.mobcent.discuz.module.custom.widget.CustomBaseText;
import com.mobcent.lowest.base.utils.MCResource;

public class CustomHelper {
    public static View createLineView(Context context, LayoutParams lps) {
        View imageView = new View(context);
        if (lps != null) {
            imageView.setLayoutParams(lps);
        }
        imageView.setBackgroundResource(MCResource.getInstance(context.getApplicationContext()).getDrawableId("mc_forum_module_line"));
        return imageView;
    }

    public static CustomBaseText createTitle(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setEllipsize(TruncateAt.END);
        text.setTextSize("mc_forum_text_size_16");
        text.setTextColor(MCResource.getInstance(context).getColor("mc_forum_custom_text_color"));
        text.setSingleLine(true);
        return text;
    }

    public static CustomBaseText createDesc(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setEllipsize(TruncateAt.END);
        text.setTextSize("mc_forum_text_size_14");
        text.setTextColor(MCResource.getInstance(context).getColor("mc_forum_text4_desc_normal_color"));
        text.setSingleLine(true);
        return text;
    }

    public static CustomBaseText createOverTitle(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setEllipsize(TruncateAt.END);
        text.setTextSize("mc_forum_text_size_13");
        text.setTextColor(-1);
        text.setBackgroundResource(MCResource.getInstance(context).getDrawableId("mc_forum_card_bg1"));
        text.setGravity(16);
        text.setSingleLine(true);
        return text;
    }

    public static CustomBaseText createMoreTitle(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setEllipsize(TruncateAt.END);
        text.setTextSize("mc_forum_text_size_14");
        text.setTextColor(MCResource.getInstance(context).getColor("mc_forum_custom_text_color"));
        text.setSingleLine(true);
        return text;
    }

    public static CustomBaseText createUpTextTitle(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setEllipsize(TruncateAt.END);
        text.setTextSize("mc_forum_text_size_15");
        text.setTextColor(MCResource.getInstance(context).getColor("mc_forum_custom_text_color"));
        text.setMaxLines(4);
        return text;
    }

    public static CustomBaseText createBtnTitle(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setEllipsize(TruncateAt.END);
        text.setTextSize("mc_forum_text_size_13");
        text.setTextColor(MCResource.getInstance(context).getColor("mc_forum_custom_text_color"));
        return text;
    }

    public static CustomBaseText createTextOnlyStyle(Context context) {
        CustomBaseText text = new CustomBaseText(context);
        text.setTextColor(MCResource.getInstance(context).getColor("mc_forum_custom_text_color"));
        text.setSingleLine(true);
        text.setEllipsize(TruncateAt.END);
        text.setGravity(17);
        text.setTextSize("mc_forum_text_size_16");
        text.setBackgroundDrawable(MCResource.getInstance(context).getDrawable("mc_forum_module_bg"));
        return text;
    }
}
