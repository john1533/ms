package com.mobcent.discuz.module.about.fragment;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class AboutFragment extends BaseFragment {
    private LinearLayout descLayout;
    private TextView descText;
    private LinearLayout emailLayout;
    private TextView emailText;
    private ImageView line1;
    private ImageView line2;
    private ImageView line3;
    private ImageView line4;
    private ImageView line5;
    private ImageView line6;
    private ImageView line7;
    private LinearLayout qqLayout;
    private TextView qqText;
    private ImageView sweepImg;
    private LinearLayout sweepPicLayout;
    private LinearLayout telLayout;
    private TextView telText;
    private String version = BaseApiConstant.APP_BUILD_VERSION;
    private TextView versionText;
    private LinearLayout websiteLayout;
    private TextView websiteText;
    private TextView weiboQQ;
    private LinearLayout weiboQQLayout;
    private TextView weiboSina;
    private LinearLayout weiboSinaayout;

    protected String getRootLayoutName() {
        return "about_fragment";
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.isTitleClickAble = false;
        if (this.moduleModel == null || TextUtils.isEmpty(this.moduleModel.getTitle())) {
            topSettingModel.title = this.resource.getString("mc_forum_about");
        } else {
            topSettingModel.title = this.moduleModel.getTitle();
        }
        dealTopBar(topSettingModel);
    }

    protected void initViews(View rootView) {
        this.descLayout = (LinearLayout) findViewByName(rootView, "mc_forum_desc_layout");
        this.descText = (TextView) findViewByName(rootView, "mc_forum_desc_text");
        this.emailText = (TextView) findViewByName(rootView, "mc_forum_about_email_text");
        this.qqText = (TextView) findViewByName(rootView, "mc_forum_aboout_qq_text");
        this.telText = (TextView) findViewByName(rootView, "mc_forum_about_tel_text");
        this.versionText = (TextView) findViewByName(rootView, "mc_forum_version");
        this.websiteText = (TextView) findViewByName(rootView, "mc_forum_about_web_text");
        this.weiboSina = (TextView) findViewByName(rootView, "mc_forum_about_weibo_sina_text");
        this.weiboQQ = (TextView) findViewByName(rootView, "mc_forum_about_webo_qq_text");
        this.line1 = (ImageView) findViewByName(rootView, "mc_forum_line1");
        this.line2 = (ImageView) findViewByName(rootView, "mc_forum_line2");
        this.line3 = (ImageView) findViewByName(rootView, "mc_forum_line3");
        this.line4 = (ImageView) findViewByName(rootView, "mc_forum_line4");
        this.line5 = (ImageView) findViewByName(rootView, "mc_forum_line5");
        this.line6 = (ImageView) findViewByName(rootView, "mc_forum_line6");
        this.sweepImg = (ImageView) findViewByName(rootView, "mc_forum_sweep_pic");
        this.line7 = (ImageView) findViewByName(rootView, "mc_forum_line7");
        this.qqLayout = (LinearLayout) findViewByName(rootView, "mc_forum_qq_box");
        this.telLayout = (LinearLayout) findViewByName(rootView, "mc_forum_tel_box");
        this.weiboQQLayout = (LinearLayout) findViewByName(rootView, "mc_forum_about_weibo_qq_box");
        this.weiboSinaayout = (LinearLayout) findViewByName(rootView, "mc_forum_about_weibo_sina_box");
        this.emailLayout = (LinearLayout) findViewByName(rootView, "mc_forum_about_email_box");
        this.websiteLayout = (LinearLayout) findViewByName(rootView, "mc_forum_about_web_box");
        this.sweepPicLayout = (LinearLayout) findViewByName(rootView, "mc_forum_sweep_pic_box");
        updateView();
    }

    protected void initActions(View rootView) {
        this.telText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (AboutFragment.this.telText.getText().toString() != null && AboutFragment.this.telText.getText().toString().trim() != "") {
                    ((ClipboardManager) AboutFragment.this.activity.getSystemService("clipboard")).setText(AboutFragment.this.telText.getText().toString());
                    MCToastUtils.toastByResName(AboutFragment.this.activity.getApplicationContext(), "mc_forum_tel_copy_to_clipboard");
                }
            }
        });
        this.emailText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent email = new Intent("android.intent.action.SEND");
                email.setType("text/plain");
                email.putExtra("android.intent.extra.EMAIL", new String[]{AboutFragment.this.emailText.getText().toString()});
                AboutFragment.this.startActivity(email);
            }
        });
        this.websiteText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String url = AboutFragment.this.websiteText.getText().toString();
                if (url.contains("http://")) {
                    AboutFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                } else if (!url.equals("{aboutWeb}")) {
                    AboutFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://" + url)));
                }
            }
        });
        this.weiboQQ.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String url = AboutFragment.this.resource.getString("mc_froum_about_weibo_qq");
                if (url.contains("http://")) {
                    AboutFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                } else if (!url.equals("{aboutWeiboqq}")) {
                    AboutFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://" + AboutFragment.this.resource.getString("mc_froum_about_weibo_qq"))));
                }
            }
        });
        this.weiboSina.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String url = AboutFragment.this.resource.getString("mc_froum_about_weibo_sina");
                if (url.contains("http://")) {
                    AboutFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                } else if (!url.equals("{aboutWeiboSina}")) {
                    AboutFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://" + AboutFragment.this.resource.getString("mc_froum_about_weibo_sina"))));
                }
            }
        });
    }

    public void updateView() {
        this.descText.setText(this.resource.getString("mc_froum_about_desc"));
        this.emailText.setText(this.resource.getString("mc_froum_about_email"));
        this.qqText.setText(this.resource.getString("mc_froum_about_qq"));
        this.telText.setText(this.resource.getString("mc_froum_about_tel"));
        this.websiteText.setText(this.resource.getString("mc_froum_about_web"));
        this.weiboQQ.setText(this.resource.getString("mc_froum_about_weibo_qq"));
        this.weiboSina.setText(this.resource.getString("mc_froum_about_weibo_sina"));
        if (MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_desc").trim())) {
            this.descLayout.setVisibility(8);
            this.line1.setVisibility(8);
        }
        if (MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_email").trim())) {
            this.emailLayout.setVisibility(8);
            this.line2.setVisibility(8);
        }
        if (MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_qq").trim())) {
            this.qqLayout.setVisibility(8);
            this.line4.setVisibility(8);
        }
        if (MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_tel").trim())) {
            this.telLayout.setVisibility(8);
            this.line5.setVisibility(8);
        }
        if (MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_web").trim())) {
            this.websiteLayout.setVisibility(8);
            this.line3.setVisibility(8);
        }
        if (this.weiboQQ.getText().equals("{aboutWeiboqq}") || MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_weibo_qq").trim())) {
            this.weiboQQLayout.setVisibility(8);
            this.line6.setVisibility(8);
        }
        if (this.weiboSina.getText().equals("{aboutWeiboSina}") || MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_weibo_sina").trim())) {
            this.weiboSinaayout.setVisibility(8);
            this.line7.setVisibility(8);
        }
        if (MCStringUtil.isEmpty(this.resource.getString("mc_froum_about_sweep_url").trim())) {
            this.sweepPicLayout.setVisibility(8);
        } else {
            ImageLoader.getInstance().displayImage(this.resource.getString("mc_froum_about_sweep_url"), this.sweepImg, new SimpleImageLoadingListener() {
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    AboutFragment.this.sweepImg.setBackgroundResource(AboutFragment.this.resource.getDrawableId("mc_forum_add_new_img"));
                }
            });
        }
        try {
            this.versionText.setText("version" + this.activity.getPackageManager().getPackageInfo(this.activity.getPackageName(), 0).versionName + " build " + this.version);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
