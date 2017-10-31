package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.TransportMediator;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.mobcent.android.model.PlatformLoginInfoModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZProgressDialogUtils;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.observer.ObserverHelper;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopBtnModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.person.activity.AuthorizationSuccActivity;
import com.mobcent.discuz.module.person.activity.UserLoginBoundActivity;
import com.mobcent.discuz.module.person.activity.delegate.RegLoginFinishDelegate;
import com.mobcent.discuz.module.person.activity.helper.PlatformLoginHelper;
import com.mobcent.login.android.helper.MCPlatformLoginHelper;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.android.ui.widget.MCWebView;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class PlatformLoginFragment extends BaseFragment implements FinalConstant {
    private static RegLoginFinishDelegate regLoginFinishDelegate;
    private int REFRESH = 1;
    private boolean isPlatformUser = false;
    private String jsonStr;
    private RelativeLayout mcForumLoadingbox;
    private String oauthToken = "";
    private String openId = "";
    private long platformId;
    private PlatformUserInfoModelAsyncTask platformUserInfoModelAsyncTask;
    private MCWebView platformWebView;
    private MCProgressBar progressBar;
    private UserInfoModel userInfoModel;
    private UserService userService;

    class PlatformUserInfoModelAsyncTask extends AsyncTask<Object, Void, BaseResultModel<UserInfoModel>> {
        PlatformUserInfoModelAsyncTask() {
        }

        protected BaseResultModel<UserInfoModel> doInBackground(Object... params) {
            PlatformLoginInfoModel loginInfoModel = new PlatformLoginInfoModel();
            loginInfoModel.setOpenid((String) params[0]);
            loginInfoModel.setAccessToken((String) params[2]);
            loginInfoModel.setPlatformType("20");
            MCPlatformLoginHelper.getInstance().loginInfoModel = loginInfoModel;
            return PlatformLoginFragment.this.userService.getUserPlatforminfo(loginInfoModel.getOpenid(), loginInfoModel.getAccessToken(), loginInfoModel.getPlatformType());
        }

        protected void onPostExecute(BaseResultModel<UserInfoModel> baseResultModel) {
            super.onPostExecute(baseResultModel);
            if (baseResultModel == null || baseResultModel.getRs() == 0) {
                DZProgressDialogUtils.hideProgressDialog();
                MCToastUtils.toastByResName(PlatformLoginFragment.this.activity.getApplicationContext(), "mc_forum_user_wechat_authorization_error");
                PlatformLoginFragment.this.activity.finish();
                return;
            }
            final UserInfoModel userInfoModel = (UserInfoModel) baseResultModel.getData();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    PlatformLoginFragment.this.activity.finish();
                    DZProgressDialogUtils.hideProgressDialog();
                    if (userInfoModel.isRegister()) {
                        Intent intent = new Intent(PlatformLoginFragment.this.activity, UserLoginBoundActivity.class);
                        intent.putExtra(IntentConstant.INTENT_USER_LOGIN_BOUND_FLAG, true);
                        PlatformLoginFragment.this.activity.startActivity(intent);
                        return;
                    }
                    ObserverHelper.getInstance().getActivityObservable().loginSuccess();
                }
            }, 1000);
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.userService = new UserServiceImpl(this.activity.getApplicationContext());
        this.platformId = getBundle().getLong("platformId");
    }

    protected String getRootLayoutName() {
        return "platform_webview_fragment";
    }

    protected void initViews(View rootView) {
        this.platformWebView = (MCWebView) findViewByName(rootView, "mc_forum_webview");
        this.mcForumLoadingbox = (RelativeLayout) findViewByName(rootView, "mc_forum_loading_box");
        this.progressBar = (MCProgressBar) findViewByName(rootView, "mc_forum_web_progressbar");
        this.platformWebView.getSettings().setJavaScriptEnabled(true);
        this.platformWebView.requestFocus(TransportMediator.KEYCODE_MEDIA_RECORD);
        initData(this.platformId);
    }

    public void initData(long platformId) {
        this.platformId = platformId;
        String url = null;
        this.platformWebView.clearView();
        this.platformWebView.clearHistory();
        if (platformId == PlatformLoginHelper.PLATFORM_QQ.longValue()) {
            url = this.resource.getString("mc_discuz_base_request_url") + this.resource.getString("mc_forum_qq_login") + "&mod=login&op=init&referer=forum.php&statfrom=login_simple&sdkVersion=" + BaseApiConstant.SDK_VERSION_VALUE + "&" + BaseApiConstant.APPHASH + "=" + MCStringUtil.stringToMD5((System.currentTimeMillis() + "").substring(0, 5) + BaseApiConstant.AUTHKEY).substring(8, 16);
        }
        fillWebViewPage(url);
    }

    public String getOpenId(String url, String indexStr) {
        String openId = "";
        if (!url.contains(indexStr)) {
            return openId;
        }
        String tempStr = url.subSequence(url.indexOf(indexStr), url.length()).toString();
        int index = tempStr.indexOf("=");
        int indexEnd = tempStr.indexOf("&");
        if (indexEnd == -1) {
            indexEnd = (tempStr.length() - index) + 1;
        }
        return tempStr.substring(index + 1, indexEnd).toString().trim();
    }

    protected void fillWebViewPage(String webUrl) {
        this.platformWebView.setWebChromeClient(new WebChromeClient());
        this.platformWebView.setWebViewClient(new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                MCLogUtil.e("test", "url===" + url);
                if (url != null && url.indexOf("openid") > -1) {
                    try {
                        PlatformLoginFragment.this.jsonStr = new URL(url).getQuery();
                        PlatformLoginFragment.this.jsonStr = URLDecoder.decode(PlatformLoginFragment.this.jsonStr, "UTF-8");
                    } catch (Exception e) {
                        MCToastUtils.toastByResName(PlatformLoginFragment.this.activity, "mc_forum_platform_bind_fail_warn");
                        PlatformLoginFragment.this.activity.finish();
                    }
                    PlatformLoginFragment.this.userInfoModel = (UserInfoModel) PlatformLoginFragment.this.userService.parseOpenplatformUserInfo(PlatformLoginFragment.this.jsonStr).getData();
                    if (PlatformLoginFragment.this.userInfoModel != null) {
                        if (PlatformLoginFragment.this.userInfoModel.isRegister()) {
                            Intent intent = new Intent(PlatformLoginFragment.this.activity, AuthorizationSuccActivity.class);
                            intent.putExtra(IntentConstant.INTENT_USER_INFO, PlatformLoginFragment.this.userInfoModel);
                            intent.putExtra("platformId", PlatformLoginFragment.this.platformId);
                            PlatformLoginFragment.regLoginFinishDelegate.activityFinish();
                            PlatformLoginFragment.this.activity.startActivity(intent);
                            PlatformLoginFragment.this.activity.finish();
                        } else {
                            PlatformLoginFragment.regLoginFinishDelegate.activityFinish();
                            PlatformLoginFragment.this.activity.finish();
                        }
                    }
                } else if (url == null || url.indexOf("fail.php") <= -1) {
                    PlatformLoginFragment.this.showLoading();
                } else {
                    MCToastUtils.toastByResName(PlatformLoginFragment.this.activity, "mc_forum_platform_bind_fail_warn", 0);
                    PlatformLoginFragment.this.activity.finish();
                }
                PlatformLoginFragment.this.openId = PlatformLoginFragment.this.getOpenId(url, "openid");
                PlatformLoginFragment.this.oauthToken = PlatformLoginFragment.this.getOpenId(url, "oauth_token");
                if (!(MCStringUtil.isEmpty(PlatformLoginFragment.this.openId) || MCStringUtil.isEmpty(PlatformLoginFragment.this.oauthToken) || PlatformLoginFragment.this.isPlatformUser)) {
                    PlatformLoginFragment.this.isPlatformUser = true;
                    PlatformLoginFragment.this.platformUserInfoModelAsyncTask = new PlatformUserInfoModelAsyncTask();
                    PlatformLoginFragment.this.platformUserInfoModelAsyncTask.execute(new Object[]{PlatformLoginFragment.this.openId, Long.valueOf(PlatformLoginFragment.this.platformId), PlatformLoginFragment.this.oauthToken});
                }
                if (!PlatformLoginFragment.this.isPlatformUser) {
                    view.loadUrl(url);
                }
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                PlatformLoginFragment.this.showLoading();
                PlatformLoginFragment.this.openId = PlatformLoginFragment.this.getOpenId(url, "openid");
                PlatformLoginFragment.this.oauthToken = PlatformLoginFragment.this.getOpenId(url, "oauth_token");
                if (!(MCStringUtil.isEmpty(PlatformLoginFragment.this.openId) || PlatformLoginFragment.this.isPlatformUser)) {
                    PlatformLoginFragment.this.isPlatformUser = true;
                    PlatformLoginFragment.this.platformUserInfoModelAsyncTask = new PlatformUserInfoModelAsyncTask();
                    PlatformLoginFragment.this.platformUserInfoModelAsyncTask.execute(new Object[]{PlatformLoginFragment.this.openId, Long.valueOf(PlatformLoginFragment.this.platformId), PlatformLoginFragment.this.oauthToken});
                }
                if (PlatformLoginFragment.this.isPlatformUser) {
                    view.stopLoading();
                }
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                PlatformLoginFragment.this.hideLoading();
                PlatformLoginFragment.this.progressBar.setVisibility(4);
            }
        });
        this.platformWebView.loadUrl(webUrl);
    }

    protected void showLoading() {
        this.mcForumLoadingbox.setVisibility(0);
        this.progressBar.show();
    }

    protected void hideLoading() {
        this.mcForumLoadingbox.setVisibility(4);
    }

    protected void initActions(View rootView) {
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        List<TopBtnModel> rightModels = new ArrayList();
        topSettingModel.title = this.resource.getString("mc_share_platform");
        TopBtnModel topBtnModel = new TopBtnModel();
        topBtnModel.icon = "mc_forum_top_bar_button7";
        topBtnModel.action = this.REFRESH;
        rightModels.add(topBtnModel);
        topSettingModel.rightModels = rightModels;
        dealTopBar(topSettingModel);
        registerTopListener(new OnClickListener() {
            public void onClick(View v) {
                if (((TopBtnModel) v.getTag()).action == PlatformLoginFragment.this.REFRESH) {
                    PlatformLoginFragment.this.initData(PlatformLoginFragment.this.platformId);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        this.platformWebView.onDestory();
        if (this.platformUserInfoModelAsyncTask != null) {
            this.platformUserInfoModelAsyncTask.cancel(true);
        }
    }

    public static RegLoginFinishDelegate getLoginDelegate() {
        return regLoginFinishDelegate;
    }

    public static void setLoginDelegate(RegLoginFinishDelegate loginDelegate) {
        regLoginFinishDelegate = loginDelegate;
    }
}
