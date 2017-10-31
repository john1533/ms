package com.mobcent.lowest.android.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import com.baidu.location.BDLocation;
import com.mobcent.lowest.android.ui.widget.web.MCWebViewClient;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.utils.MCLibIOUtil;

public class MCWebView extends WebView {

    final class InJavaScriptLocalObj {
        InJavaScriptLocalObj() {
        }

        public void showSource(String html) {
        }
    }

    public MCWebView(Context context) {
        this(context, null);
    }

    public MCWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        getSettings().setSaveFormData(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        String dbPath = MCLibIOUtil.getCachePath(context) + "web";
        getSettings().setDatabasePath(dbPath);
        getSettings().setGeolocationEnabled(true);
        getSettings().setGeolocationDatabasePath(dbPath);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAllowFileAccess(true);
        setDrawingCacheEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setCacheMode(-1);
        setScrollBarStyle(0);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocusFromTouch();
        requestFocus();
        getSettings().setSupportZoom(false);
        getSettings().setBuiltInZoomControls(true);
        addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        setWebViewClient(new MCWebViewClient());
        setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                MCWebView.this.downApk(url);
            }
        });
    }

    private void downApk(String downurl) {
        Intent intent;
        try {
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(downurl));
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            getContext().startActivity(intent);
        } catch (Exception e) {
            try {
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(downurl));
                getContext().startActivity(intent);
            } catch (Exception e2) {
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void onWebResume() {
        if (VERSION.SDK_INT >= 11) {
            super.onResume();
        }
        resumeTimers();
    }

    @SuppressLint({"NewApi"})
    public void onWebPause() {
        if (VERSION.SDK_INT >= 11) {
            super.onPause();
        }
        pauseTimers();
    }

    public void onDestory() {
        setVisibility(8);
        stopLoading();
        clearView();
        freeMemory();
        destroy();
    }

    public void loadUrl(String url) {
        if (url.contains("wsh.appbyme.com")) {
            BDLocation location = LowestManager.getInstance().getConfig().getLocation();
            if (location != null) {
                if (url.contains("?")) {
                    url = url + "&longitude=" + location.getLongitude() + "&latitude=" + location.getLatitude();
                } else {
                    url = url + "?longitude=" + location.getLongitude() + "&latitude=" + location.getLatitude();
                }
            }
        }
        super.loadUrl(url);
    }
}
