package com.mobcent.share.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.media.TransportMediator;
import android.view.View;
import android.view.View.OnClickListener;
//import android.webkit.CacheManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.share.android.constant.MCShareIntentConstant;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.mobcent.share.android.widget.MCShareProgressBar;
import java.io.File;

public class MCShareWebActivity extends Activity {
    private final String TAG = "MCShareWebActivity";
    private String appKey;
    private Button backBtn;
    private String bindUrl;
    private boolean isCleanWebCache = false;
    private RelativeLayout loadingBox;
    private WebView mWebView;
    private int markId;
    private MCShareProgressBar progressBar;
    private Button refreshBtn;
    private MCResource resource;
    private MCShareSiteModel siteModel;
    private String url;
    private long userId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resource = MCResource.getInstance(this);
        requestWindowFeature(1);
        initData();
        initViews();
        initWidgetActions();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            this.siteModel = (MCShareSiteModel) intent.getSerializableExtra(MCShareIntentConstant.MC_SHARE_SITE_MODEL);
            this.appKey = intent.getStringExtra(MCShareIntentConstant.MC_SHARE_APP_KEY);
            this.bindUrl = this.siteModel.getBindUrl();
            this.markId = this.siteModel.getSiteId();
            this.userId = this.siteModel.getUserId();
        }
    }

    private void initViews() {
        setContentView(this.resource.getLayoutId("mc_share_web_activity"));
        this.backBtn = (Button) findViewById(this.resource.getViewId("mc_share_back_btn"));
        this.refreshBtn = (Button) findViewById(this.resource.getViewId("mc_share_refresh_btn"));
        this.mWebView = (WebView) findViewById(this.resource.getViewId("mc_share_web_view"));
        this.loadingBox = (RelativeLayout) findViewById(this.resource.getViewId("mc_share_loading_box"));
        this.progressBar = (MCShareProgressBar) findViewById(this.resource.getViewId("mc_share_progress_bar"));
        initBaseData();
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocus(TransportMediator.KEYCODE_MEDIA_RECORD);
        if (this.isCleanWebCache) {
            this.mWebView.getSettings().setCacheMode(2);
        }
    }

    private void initWidgetActions() {
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCShareWebActivity.this.cleanCache();
                MCShareWebActivity.this.finish();
            }
        });
        this.refreshBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!MCStringUtil.isEmpty(MCShareWebActivity.this.bindUrl)) {
                    MCShareWebActivity.this.fillWebViewPage(MCShareWebActivity.this.bindUrl + "?mark=" + MCShareWebActivity.this.markId + "&userId=" + MCShareWebActivity.this.userId + "&appKey=" + MCShareWebActivity.this.appKey + "&packageName=" + MCShareWebActivity.this.getPackageName());
                }
            }
        });
    }

    private void cleanCache() {
        if (this.isCleanWebCache) {
            try {
//                File file = CacheManager.getCacheFileBaseDir();
//                if (file != null && file.exists() && file.isDirectory()) {
//                    for (File item : file.listFiles()) {
//                        item.delete();
//                    }
//                    file.delete();
//                }
                deleteDatabase("webview.db");
                deleteDatabase("webviewCache.db");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fillWebViewPage(String webUrl) {
        this.mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                MCLogUtil.i("MCShareWebView", "url = " + url);
                if (url != null && url.indexOf("success.jsp") > -1) {
                    MCShareWebActivity.this.siteModel.setBindState(1);
                    MCShareWebActivity.this.cleanCache();
                    MCShareToastUtil.toast(MCShareWebActivity.this, "　 " + MCShareWebActivity.this.resource.getString("mc_share_bind_succ") + "\n" + MCShareWebActivity.this.resource.getString("mc_share_long_unbind"));
                    Intent intent = new Intent();
                    intent.putExtra(MCShareIntentConstant.MC_SHARE_SITE_MODEL, MCShareWebActivity.this.siteModel);
                    MCShareWebActivity.this.setResult(-1, intent);
                    MCShareWebActivity.this.finish();
                } else if (url == null || url.indexOf("fail.jsp") <= -1) {
                    MCShareWebActivity.this.showLoading();
                } else {
                    MCShareToastUtil.toast(MCShareWebActivity.this, MCShareWebActivity.this.resource.getString("mc_share_bind_fail"));
                    MCShareWebActivity.this.cleanCache();
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                MCShareWebActivity.this.hideLoading();
            }
        });
        this.mWebView.loadUrl(webUrl);
    }

    private void showLoading() {
        this.loadingBox.setVisibility(View.VISIBLE);
        this.progressBar.show();
    }

    private void hideLoading() {
        this.loadingBox.setVisibility(View.INVISIBLE);
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    private void initBaseData() {
        if (this.isCleanWebCache) {
            try {
                CookieSyncManager.createInstance(this);
                CookieSyncManager.getInstance().startSync();
                CookieManager.getInstance().removeSessionCookie();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mWebView.clearView();
        this.mWebView.clearHistory();
        clearCacheFolder(getCacheDir(), System.currentTimeMillis());
        this.url = this.bindUrl + "?mark=" + this.markId + "&userId=" + this.userId + "&appKey=" + this.appKey + "&packageName=" + getPackageName();
        MCLogUtil.i("MCShareWebActivity", "url = " + this.url);
        fillWebViewPage(this.url);
    }

    public int clearCacheFolder(File dir, long numDays) {
        if (!this.isCleanWebCache) {
            return -1;
        }
        int deletedFiles = 0;
        if (dir == null || !dir.isDirectory()) {
            return 0;
        }
        try {
            for (File child : dir.listFiles()) {
                if (child.isDirectory()) {
                    deletedFiles += clearCacheFolder(child, numDays);
                }
                if (child.lastModified() < numDays && child.delete()) {
                    deletedFiles++;
                }
            }
            return deletedFiles;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
