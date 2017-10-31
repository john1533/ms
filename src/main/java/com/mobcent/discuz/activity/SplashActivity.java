package com.mobcent.discuz.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.PayStateModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.helper.InitHelper;
import com.mobcent.discuz.base.task.RequestCalback;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SplashActivity extends Activity {
    private final int TIMEOUT = 5000;
    private boolean dataTaskExecuted = false;
    private SharedPreferencesDB db;
    private boolean getAllDataByNet = true;
    private String imgLoadPageUrl;
    private InitHelper initHelper;
//    private Handler mHandler = new Handler();
    private MCResource resource;
    private String skipToWhere = null;
    private ImageView splashBackgroundImg;
    private RelativeLayout splashBottomLayout;
    private MCProgressBar splashLoadingImg;
    private boolean timeOutExecuted = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resource = MCResource.getInstance(getApplicationContext());
        this.db = SharedPreferencesDB.getInstance(getApplicationContext());
        this.initHelper = InitHelper.getInstance(getApplicationContext());
        Intent intent = getIntent();
        if (intent != null) {
            this.getAllDataByNet = intent.getBooleanExtra(IntentConstant.INTENT_GET_ALL_DATA_BY_NET, true);
            this.skipToWhere = intent.getStringExtra(IntentConstant.INTENT_HOME_SKIP_TO_WHERE);
        }
        prepareView();
        this.initHelper.init(this, this.getAllDataByNet, new RequestCalback<BaseResultModel<ConfigModel>>() {
            public void onPreExecute() {
            }

            public void onPostExecute(BaseResultModel<ConfigModel> result) {
                ((DiscuzApplication) SplashActivity.this.getApplication()).setConfigModel(result);
                if (SplashActivity.this.timeOutExecuted) {
                    SplashActivity.this.initHelper.dispatchActivity(SplashActivity.this, SplashActivity.this.skipToWhere);
                }
                SplashActivity.this.dataTaskExecuted = true;
            }

            public void onPostExecute(SettingModel settingModel) {
                ((DiscuzApplication) SplashActivity.this.getApplication()).setSettingModel(settingModel);
            }

            public void onPostExecute(PermissionModel permissionModel) {
                ((DiscuzApplication) SplashActivity.this.getApplication()).setPermissionModel(permissionModel);
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SplashActivity.this.dataTaskExecuted) {
                    SplashActivity.this.initHelper.dispatchActivity(SplashActivity.this, SplashActivity.this.skipToWhere);
                }
                SplashActivity.this.timeOutExecuted = true;
            }
        }, TIMEOUT);
    }

    private void prepareView() {
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        setContentView(this.resource.getLayoutId("splash_activity"));
        this.splashBackgroundImg = (ImageView) findViewById(this.resource.getViewId("mc_forum_splash_background_img"));
        this.splashBottomLayout = (RelativeLayout) findViewById(this.resource.getViewId("mc_forum_splash_bottom_layout"));
        this.splashLoadingImg = (MCProgressBar) findViewById(this.resource.getViewId("mc_forum_splash_loading_img"));
        int screenHeight = MCPhoneUtil.getDisplayHeight(this);
        int screenWidth = MCPhoneUtil.getDisplayWidth(this);
        LayoutParams imgLps = (LayoutParams) this.splashBackgroundImg.getLayoutParams();
        if (div((double) screenHeight, 16.0d) == div((double) screenWidth, 9.0d)) {
            imgLps.width = screenWidth;
            imgLps.height = screenHeight;
            this.splashBackgroundImg.setBackgroundResource(this.resource.getDrawableId("mc_forum_loading_page2"));
            this.splashBottomLayout.setBackgroundColor(this.resource.getColorId("mc_forum_splash_text_background_color"));
            this.splashBottomLayout.getLayoutParams().height = MCPhoneUtil.dip2px(getApplicationContext(), 40.0f);
            this.splashBottomLayout.setVisibility(8);
        } else {
            int imgHeight = (int) (((double) screenWidth) * 1.5d);
            int bottomBoxHeight = screenHeight - imgHeight;
            imgLps.width = screenWidth;
            imgLps.height = imgHeight;
            this.splashBackgroundImg.setBackgroundResource(this.resource.getDrawableId("mc_forum_loading_page"));
            if (bottomBoxHeight == 0) {
                this.splashBottomLayout.setBackgroundColor(this.resource.getColorId("mc_forum_splash_text_background_color"));
                this.splashBottomLayout.getLayoutParams().height = MCPhoneUtil.dip2px(getApplicationContext(), 40.0f);
            } else {
                this.splashBottomLayout.getLayoutParams().width = screenWidth;
                this.splashBottomLayout.getLayoutParams().height = bottomBoxHeight;
            }
        }
        if (this.db.isPayStateExist()) {
            PayStateModel payStateModel = this.db.getPayState();
            String imageUrl = payStateModel.getImgUrl() + payStateModel.getLoadingPageImage().split(AdApiConstant.RES_SPLIT_COMMA)[0];
            if (div((double) screenHeight, 16.0d) == div((double) screenWidth, 9.0d)) {
                this.imgLoadPageUrl = MCAsyncTaskLoaderImage.formatUrl(imageUrl, "720x1280");
            } else {
                this.imgLoadPageUrl = MCAsyncTaskLoaderImage.formatUrl(imageUrl, "640x960");
            }
            ImageLoader.getInstance().displayImage(this.imgLoadPageUrl, this.splashBackgroundImg);
        }
        this.splashLoadingImg.show();
    }

    public double div(double v1, double v2) {
        return v1 / v2;
    }

    public void onBackPressed() {
    }

    protected void onDestroy() {
        if (this.splashBackgroundImg != null) {
            this.splashBackgroundImg.setImageBitmap(null);
            this.splashBackgroundImg.setImageDrawable(null);
        }
        super.onDestroy();
    }
}
