package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCResource;

public class MCColorUtil {
    public static void setTextViewPart(Context context, TextView textView, String s, int startPostion, int endPostion, String colorName) {
        try {
            if("CN".equalsIgnoreCase(LowestManager.getInstance().getConfig().getCtr())){
                textView.setText(s, BufferType.SPANNABLE);
            }else{//繁体
                textView.setText(ChineseHelper.convertToTraditionalChinese(s), BufferType.SPANNABLE);
            }
            if (endPostion > startPostion) {
                MCResource resource = MCResource.getInstance(context);
                Spannable sp = (Spannable) textView.getText();
                if (endPostion > sp.length()) {
                    endPostion = sp.length() - 1;
                }
                sp.setSpan(new ForegroundColorSpan(resource.getColor(colorName)), startPostion, endPostion, 33);
            }
        } catch (Exception e) {
        }
    }

    public static void setTextViewPart(TextView textView, String s, int startPostion, int endPostion, int color) {
        try {
            textView.setText(s, BufferType.SPANNABLE);
            if (endPostion > startPostion) {
                Spannable sp = (Spannable) textView.getText();
                if (endPostion > sp.length()) {
                    endPostion = sp.length() - 1;
                }
                sp.setSpan(new ForegroundColorSpan(color), startPostion, endPostion, 33);
            }
        } catch (Exception e) {
        }
    }
}
