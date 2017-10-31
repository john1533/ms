package com.mobcent.lowest.android.ui.widget.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MCWebViewClient extends WebViewClient {
    private WebView web;

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (this.web == null) {
            this.web = view;
        }
        if (url.startsWith("tel:")) {
            this.web.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        } else if (!url.startsWith("http")) {
            return super.shouldOverrideUrlLoading(view, url);
        } else {
            this.web.loadUrl(url);
        }
        return true;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}
