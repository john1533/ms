package com.mobcent.discuz.base.fragment;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.android.constant.MCShareConstant;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.view.MCPopupListView;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupClickListener;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.android.api.util.DZHttpClientUtil;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.widget.MCWebView;
import com.mobcent.lowest.android.ui.widget.web.MCWebChromeClient;
import com.mobcent.lowest.android.ui.widget.web.MCWebViewClient;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.update.android.constant.MCUpdateConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebViewFragment extends BaseFragment implements SlideDelegate {
    public static final String VIDEO = "video";
    private int CLOSE = 1;
    private int MORE = 0;
    private int adPostition;
    private AdView adView;
    private MCWebChromeClient mChromeClient;
    private FrameLayout mainContent;
    private ProgressBar mcProgressBar;
    private MCWebView mcWebview;
    private MCPopupListView popupListView;
    private String title;
    private String type;
    private String url;
    private String webUrl;

    final class MyWebViewClient extends MCWebViewClient {
        MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebViewFragment.this.url = url;
            if (url != null && url.indexOf(WebViewFragment.this.resource.getString("mc_forum_topic_activity_return_view")) > -1) {
                WebViewFragment.this.getAppApplication().setActivityTopic(true);
                if (FinalConstant.RATE.equals(WebViewFragment.this.type)) {
                    WebViewFragment.this.getAppApplication().setRateSucc(true);
                }
                WebViewFragment.this.activity.finish();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        if (this.moduleModel == null) {
            this.webUrl = getBundle().getString("webViewUrl");
            this.adPostition = getBundle().getInt("adPosition", 0);
            this.title = getBundle().getString(BaseIntentConstant.BUNDLE_WEB_TITLE);
            this.type = getBundle().getString(BaseIntentConstant.BUNDLE_WEB_TYPE);
        } else {
            this.title = this.moduleModel.getTitle();
            this.webUrl = this.moduleModel.getRedirect();
        }
        if (this.adPostition == 0) {
            this.adPostition = Integer.parseInt(this.resource.getString("mc_forum_webview_position"));
        }
        if (MCStringUtil.isEmpty(this.title)) {
            this.title = this.resource.getString(MCUpdateConstant.UPDATE_APP_NAME);
        }
    }

    protected String getRootLayoutName() {
        return "webview_fragment";
    }

    protected void initViews(View rootView) {
        this.mcProgressBar = (ProgressBar) findViewByName(rootView, "mc_forum_web_progressbar");
        this.adView = (AdView) findViewByName(rootView, "mc_ad_box");
        this.popupListView = (MCPopupListView) findViewByName(rootView, "popup_listview");
        this.mainContent = (FrameLayout) findViewByName(rootView, "main_content");
        this.mcWebview = (MCWebView) findViewByName(rootView, "mc_forum_webview_browser");
        this.mChromeClient = new MCWebChromeClient(this.activity, this.mcProgressBar);
        this.url = this.webUrl;
        if (this.url == null) {
            MCToastUtils.toastByResName(this.activity.getApplicationContext(), "mc_forum_webview_url_error");
            this.activity.finish();
        }
        this.mcWebview.setWebViewClient(new MyWebViewClient());
        this.mcWebview.setWebChromeClient(this.mChromeClient);
        if (SharedPreferencesDB.getInstance(this.activity.getApplicationContext()).getForumType().equals(FinalConstant.PHPWIND)) {
            this.mcWebview.getSettings().setUserAgentString(DZHttpClientUtil.PHPWIND_USER_AGENT);
        }
        this.mcWebview.loadUrl(this.url);
        this.adView.showAd(this.adPostition);
    }

    protected void initActions(View rootView) {
        this.popupListView.setResource("mc_forum_pop_upmenu_bg1", 40);
        LayoutParams layoutParams = new LayoutParams(MCPhoneUtil.getRawSize(this.activity, 1, 120.0f), -2);
        layoutParams.addRule(11);
        layoutParams.rightMargin = MCPhoneUtil.getRawSize(this.activity, 1, 8.0f);
        this.popupListView.setPopupListViewLayoutParams(layoutParams);
        this.popupListView.init(initClassifyView(), new PopupClickListener() {
            public void initTextView(TextView textView) {
                textView.setTextColor(WebViewFragment.this.getResources().getColorStateList(WebViewFragment.this.resource.getColorId("mc_forum_bubble_color")));
                textView.setTextSize(14.0f);
            }

            @SuppressLint({"NewApi"})
            public void click(TextView textView, ImageView imageView, PopupModel popupModel, int position) {
                WebViewFragment.this.popupListView.setPopupList(WebViewFragment.this.initClassifyView());
                WebViewFragment.this.popupListView.setVisibility(8);
                if (popupModel.getId() == -1) {
                    ((ClipboardManager) WebViewFragment.this.activity.getSystemService("clipboard")).setText(WebViewFragment.this.url.toString());
                    MCToastUtils.toastByResName(WebViewFragment.this.activity.getApplicationContext(), "mc_forum_webview_copy_url_succ");
                } else if (popupModel.getId() == -2) {
                    try {
                        WebViewFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(WebViewFragment.this.url)));
                    } catch (Exception e) {
                    }
                } else if (popupModel.getId() == -3) {
                    WebViewFragment.this.share(WebViewFragment.this.url.toString());
                }
            }

            public void hideView() {
            }
        });
    }

    public void share(String url) {
        MCShareModel shareModel = new MCShareModel();
        shareModel.setTitle(this.title);
        shareModel.setType(3);
        shareModel.setLinkUrl(url);
        shareModel.setSkipUrl(url);
        shareModel.setContent(this.title);
        shareModel.setDownloadUrl(this.resource.getString("mc_share_download_url"));
        HashMap<String, String> params = new HashMap();
        params.put(MCShareConstant.PARAM_SHARE_FROM, MCShareConstant.FROM_PHP);
        params.put("baseUrl", this.resource.getString("mc_discuz_base_request_url"));
        params.put(MCShareConstant.PARAM_CONTENT_TYPE, String.valueOf(3));
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        shareModel.setParams(params);
        MCForumLaunchShareHelper.share(this.activity, shareModel);
    }

    private List<PopupModel> initClassifyView() {
        List<PopupModel> list = new ArrayList();
        PopupModel model = new PopupModel();
        model.setId(-1);
        model.setName(this.resource.getString("mc_forum_copy_url"));
        list.add(model);
        model = new PopupModel();
        model.setId(-2);
        model.setName(this.resource.getString("mc_forum_open_in_browser"));
        list.add(model);
        model = new PopupModel();
        model.setId(-3);
        model.setName(this.resource.getString("mc_forum_announce_share"));
        list.add(model);
        return list;
    }

    public void onDestroy() {
        try {
            if (this.adView != null) {
                this.adView.free();
            }
            this.mcWebview.onDestory();
            super.onDestroy();
        } catch (Exception e) {
        }
    }

    public void onResume() {
        super.onResume();
        this.activity.getWindow().clearFlags(128);
        if (this.mcWebview != null) {
            this.mcWebview.onWebResume();
        }
    }

    public void onPause() {
        if (this.mChromeClient != null) {
            this.mChromeClient.onHideCustomView();
        }
        super.onPause();
        this.activity.getWindow().clearFlags(128);
        if (this.mcWebview != null) {
            this.mcWebview.onWebPause();
        }
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        List<TopBtnModel> LeftModels = new ArrayList();
        TopBtnModel topBtnModel1 = new TopBtnModel();
        topBtnModel1.icon = "mc_forum_top_bar_button20";
        topBtnModel1.action = this.CLOSE;
        LeftModels.add(topBtnModel1);
        List<TopBtnModel> rightModels = new ArrayList();
        if (this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle())) {
            topSettingModel.title = this.title;
        } else {
            topSettingModel.title = this.moduleModel.getTitle();
        }
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button12";
        topBtnModel.action = this.MORE;
        rightModels.add(topBtnModel);
        topSettingModel.style = 0;
        topSettingModel.rightModels = rightModels;
        topSettingModel.leftModels = LeftModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                TopBtnModel t = (TopBtnModel) v.getTag();
                if (t.action == WebViewFragment.this.MORE) {
                    WebViewFragment.this.topMoreClick();
                } else if (t.action == WebViewFragment.this.CLOSE) {
                    WebViewFragment.this.activity.finish();
                }
            }
        });
    }

    public void topMoreClick() {
        if (this.popupListView != null) {
            if (this.popupListView.getVisibility() == 0) {
                this.popupListView.setVisibility(8);
            } else {
                this.popupListView.setVisibility(0);
            }
        }
    }

    public boolean isChildInteruptBackPress() {
        if (this.mcWebview != null && this.mcWebview.canGoBack()) {
            this.mcWebview.goBack();
            return true;
        } else if (this.popupListView == null || this.popupListView.onKeyDown()) {
            return super.isChildInteruptBackPress();
        } else {
            return true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.mChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isSlideFullScreen() {
        return false;
    }
}
