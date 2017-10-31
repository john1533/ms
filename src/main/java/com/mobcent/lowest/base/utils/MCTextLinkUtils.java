package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MCTextLinkUtils {
    private static String TAG = "TextViewUtils";

    public interface LinkUrlClickListener {
        void onWebUrlClick(Context context, String str);
    }

    private static class MySpan extends ClickableSpan {
        private LinkUrlClickListener listener;
        private String url;

        public MySpan(LinkUrlClickListener listener, String url) {
            this.url = url;
            this.listener = listener;
        }

        public void onClick(View widget) {
            Log.e(MCTextLinkUtils.TAG, this.url);
            if (this.listener != null) {
                this.listener.onWebUrlClick(widget.getContext(), this.url);
                return;
            }
            widget.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.url)));
        }
    }

    public static void setLinkClick(TextView tv, LinkUrlClickListener listener) {
        int i = 0;
        CharSequence textStr = tv.getText();
        SpannableString ss = new SpannableString(textStr);
        URLSpan[] urls = (URLSpan[]) ss.getSpans(0, ss.length(), URLSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(textStr);
        style.clearSpans();
        int length = urls.length;
        while (i < length) {
            URLSpan url = urls[i];
            style.setSpan(new MySpan(listener, url.getURL()), ss.getSpanStart(url), ss.getSpanEnd(url), 33);
            i++;
        }
        tv.setText(style);
    }
}
