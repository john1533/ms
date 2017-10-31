package com.mobcent.lowest.android.ui.module.ad.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.widget.MCWebView;
import com.mobcent.lowest.base.utils.MCApkUtil;
import com.mobcent.lowest.base.utils.MCAppUtil;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.db.DownSqliteHelper;
import com.mobcent.lowest.module.ad.model.AdApkModel;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import com.mobcent.lowest.module.ad.model.AdIntentModel;
import com.mobcent.lowest.module.ad.task.manager.DownTaskManager;
import com.mobcent.lowest.module.ad.utils.AdStringUtils;

public class AdWebViewActivity extends Activity {
    public static final String AD_INTENT_MODEL = "adModel";
    private String TAG = "WebViewActivity";
    private AdIntentModel adIntentModel;
    private Button closeBtn;
    private DownTaskManager downTaskManager;
    private MCResource resource;
    private DownSqliteHelper sqliteHelper;
    private MCWebView webView;
    private ProgressBar webviewProgressbar;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initData();
        initViews();
        initWidgetActions();
        overridePendingTransition(17432576, 17432577);
    }

    protected void initData() {
        try {
            this.adIntentModel = (AdIntentModel) getIntent().getSerializableExtra(AD_INTENT_MODEL);
        } catch (Exception e) {
            MCLogUtil.e(this.TAG, e.toString());
        }
        if (this.adIntentModel == null || MCStringUtil.isEmpty(this.adIntentModel.getUrl())) {
            finish();
            overridePendingTransition(0, 17432577);
        } else {
            MCLogUtil.i(this.TAG, "url--" + this.adIntentModel.getUrl());
        }
        this.resource = MCResource.getInstance(this);
        this.sqliteHelper = DownSqliteHelper.getInstance(this);
        this.downTaskManager = DownTaskManager.getInastance();
    }

    protected void initViews() {
        requestWindowFeature(1);
        setContentView(this.resource.getLayoutId("mc_ad_web_view"));
        this.webviewProgressbar = (ProgressBar) findViewById(this.resource.getViewId("progress_bar"));
        this.closeBtn = (Button) findViewById(this.resource.getViewId("close_btn"));
        this.webView = (MCWebView) findViewById(this.resource.getViewId("web_view"));
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    AdWebViewActivity.this.webviewProgressbar.setVisibility(8);
                } else {
                    AdWebViewActivity.this.webviewProgressbar.setVisibility(0);
                }
                AdWebViewActivity.this.webviewProgressbar.setProgress(progress);
            }
        });
        this.webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                AdWebViewActivity.this.downApk(url);
            }
        });
        if (this.adIntentModel != null) {
            this.webView.loadUrl(this.adIntentModel.getUrl());
        }
    }

    protected void initWidgetActions() {
        this.closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AdWebViewActivity.this.webView.setWebViewClient(new WebViewClient() {
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    }

                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }
                });
                AdWebViewActivity.this.finish();
                AdWebViewActivity.this.overridePendingTransition(0, 17432577);
            }
        });
    }

    private void downApk(String downloadUrl) {
        if (MCLibIOUtil.getExternalStorageState()) {
            AdApkModel adApkModel = AdStringUtils.parseAdApkModel(downloadUrl);
            if (MCStringUtil.isEmpty(adApkModel.getPackageName()) || !MCApkUtil.isInstallApk(adApkModel.getPackageName(), this)) {
                AdDownDbModel adDbModel = this.sqliteHelper.query(downloadUrl);
                if (adDbModel == null) {
                    adDbModel = new AdDownDbModel();
                    adDbModel.setAid(this.adIntentModel.getAid());
                    adDbModel.setCurrentPn(MCAppUtil.getPackageName(this));
                    adDbModel.setPn(adApkModel.getPackageName());
                    adDbModel.setStatus(1);
                    adDbModel.setUrl(downloadUrl);
                    adDbModel.setPo(this.adIntentModel.getPo());
                    adDbModel.setAppName(adApkModel.getAppName());
                    this.sqliteHelper.insert(adDbModel);
                    adDbModel = this.sqliteHelper.query(downloadUrl);
                }
                this.downTaskManager.downloadApk(this, adDbModel);
                finish();
                return;
            }
            MCApkUtil.launchApk(this, adApkModel.getPackageName());
            finish();
            return;
        }
        warnByName("mc_ad_warn_sd_not_exist");
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.webView == null || !this.webView.canGoBack()) {
            finish();
            overridePendingTransition(0, 17432577);
            return super.onKeyDown(keyCode, event);
        }
        this.webView.goBack();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.webView.stopLoading();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.webView.onDestory();
    }

    private void warnByName(String name) {
        Toast.makeText(this, this.resource.getString(name), 0).show();
    }
}
