package com.mobcent.lowest.android.ui.module.plaza.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.widget.MCWebView;
import com.mobcent.lowest.android.ui.widget.web.MCWebChromeClient;
import com.mobcent.lowest.base.utils.MCAppUtil;

public class PlazaWebViewActivity extends BasePlazaFragmentActivity implements PlazaConstant {
    private RelativeLayout bottomBox;
    private boolean isTop = false;
    private MCWebChromeClient mWebChromeClient;
    private String title;
    private TextView titleText;
    private RelativeLayout topBox;
    private String webUrl;
    private Button webviewBackBtn;
    private MCWebView webviewBrowser;
    private Button webviewCloseBtn;
    private ProgressBar webviewProgressbar;
    private Button webviewRefreshBtn;

    protected void initData() {
        Intent intent = getIntent();
        this.webUrl = intent.getStringExtra(PlazaConstant.WEB_VIEW_URL);
        this.isTop = intent.getBooleanExtra(PlazaConstant.WEB_VIEW_TOP, false);
        this.title = intent.getStringExtra("title");
        if (TextUtils.isEmpty(this.title)) {
            this.title = MCAppUtil.getAppName(getApplicationContext());
        }
        if (this.webUrl == null) {
            warById("mc_forum_webview_url_error");
            finish();
            overridePendingTransition(0, 17432577);
        }
    }

    protected void initViews() {
        requestWindowFeature(1);
        setContentView(this.adResource.getLayoutId("mc_plaza_web_view"));
        this.titleText = (TextView) findViewById(this.adResource.getViewId("title_text"));
        this.titleText.setText(this.title);
        this.topBox = (RelativeLayout) findViewById(this.adResource.getViewId("top_layout"));
        this.bottomBox = (RelativeLayout) findViewById(this.adResource.getViewId("bottom_layout"));
        this.webviewProgressbar = (ProgressBar) findViewById(this.adResource.getViewId("progress_bar"));
        if (this.isTop) {
            this.topBox.setVisibility(0);
            this.bottomBox.setVisibility(8);
            this.webviewCloseBtn = (Button) findViewById(this.adResource.getViewId("top_close_btn"));
        } else {
            this.topBox.setVisibility(8);
            this.bottomBox.setVisibility(0);
            this.webviewRefreshBtn = (Button) findViewById(this.adResource.getViewId("bottom_refresh_btn"));
            this.webviewCloseBtn = (Button) findViewById(this.adResource.getViewId("bottom_close_btn"));
            this.webviewBackBtn = (Button) findViewById(this.adResource.getViewId("bottom_back_btn"));
        }
        this.webviewBrowser = (MCWebView) findViewById(this.adResource.getViewId("web_view"));
        this.mWebChromeClient = new MCWebChromeClient(this, this.webviewProgressbar);
        this.webviewBrowser.setWebChromeClient(this.mWebChromeClient);
        this.webviewBrowser.loadUrl(this.webUrl);
    }

    protected void initWidgetActions() {
        if (this.webviewBackBtn != null) {
            this.webviewBackBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PlazaWebViewActivity.this.webviewBrowser == null || !PlazaWebViewActivity.this.webviewBrowser.canGoBack()) {
                        PlazaWebViewActivity.this.onBackPressed();
                    } else {
                        PlazaWebViewActivity.this.webviewBrowser.goBack();
                    }
                }
            });
        }
        this.webviewCloseBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PlazaWebViewActivity.this.finish();
                PlazaWebViewActivity.this.overridePendingTransition(0, 17432577);
            }
        });
        if (this.webviewRefreshBtn != null) {
            this.webviewRefreshBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    PlazaWebViewActivity.this.webviewBrowser.reload();
                }
            });
        }
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        overridePendingTransition(17432576, 17432577);
    }

    protected void onResume() {
        super.onResume();
        getWindow().clearFlags(128);
        if (this.webviewBrowser != null) {
            this.webviewBrowser.onWebResume();
        }
    }

    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(128);
        if (this.mWebChromeClient != null) {
            this.mWebChromeClient.onHideCustomView();
        }
        if (this.webviewBrowser != null) {
            this.webviewBrowser.onWebPause();
        }
    }

    public void onBackPressed() {
        if (this.webviewBrowser == null || !this.webviewBrowser.canGoBack()) {
            super.onBackPressed();
        } else {
            this.webviewBrowser.goBack();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, 17432577);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.webviewBrowser.onDestory();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }
}
