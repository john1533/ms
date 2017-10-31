package com.mobcent.share.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import com.mobcent.android.db.MCShareSharedPreferencesDB;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.android.service.impl.MCShareServiceImpl;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.share.android.activity.MCShareActivity;
import com.mobcent.share.android.constant.MCShareIntentConstant;
import com.mobcent.share.android.helper.MCShareQQHelper;
import com.mobcent.share.android.helper.MCShareQZoneHelper;
import com.mobcent.share.android.helper.MCShareSinaWeiBoHelper;
import com.mobcent.share.android.helper.MCShareWeChatHelper;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.mobcent.share.android.view.MCShareListAdapter.ShareItemClickListener;
import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MCShareDialog extends Dialog implements ShareItemClickListener {
    public static QQAuth qqAuth = null;
    private String OPEN_MOMENTS = "26";
    private String OPEN_QQ = "25";
    private String OPEN_QZONE = "20";
    private String OPEN_WECHAT = "27";
    private String OPEN_WEIBO = "1";
    private final String TAG = "MCShareDialog";
    private IWXAPI WXAPI = null;
    private MCShareListAdapter adapter;
    private Button cancelBtn;
    private List<MCShareSiteModel> contentList;
    private Context context;
    private ListView listView;
    private String openPlatType;
    private MCResource resource;
    private MCShareModel shareModel;
    private MCShareSharedPreferencesDB shareSharedPreferencesDB;
    private Tencent tencent = null;
    private IWeiboShareAPI weiboShareAPI = null;
    private String appidStr;

    public MCShareDialog(Context context) {
        super(context);
        init(context);
    }

    public MCShareDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public MCShareDialog(Context context, int theme, MCShareModel shareModel) {
        super(context, theme);
        this.shareModel = shareModel;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context);
        this.shareSharedPreferencesDB = MCShareSharedPreferencesDB.getInstance(context);
        setContentView(this.resource.getLayoutId("mc_share_dialog"));
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth();
        getWindow().setAttributes(lp);
        initData();
        initViews();
        initActions();
    }

    private void initData() {
        this.shareModel.setSkipUrl(getSkipUrlByType());
        this.contentList = new ArrayList();
        this.contentList.add(getShareSiteModel(0, "mc_share_direct", "mc_forum_ico16_h"));
        if (!(MCStringUtil.isEmpty(this.resource.getString("mc_wechat_appid").trim()) || this.resource.getString("mc_wechat_appid").trim().startsWith("{"))) {
            appidStr = this.resource.getString("mc_wechat_appid");
            this.WXAPI = WXAPIFactory.createWXAPI(this.context, appidStr);
            this.WXAPI.registerApp(appidStr);
            this.contentList.add(getShareSiteModel(1, "mc_share_wechat", "mc_forum_ico17_h"));
            this.contentList.add(getShareSiteModel(2, "mc_share_moments", "mc_forum_ico18_h"));
        }
        if (!(MCStringUtil.isEmpty(this.resource.getString("mc_tencent_appid").trim()) || this.resource.getString("mc_tencent_appid").trim().startsWith("{"))) {
            appidStr = this.resource.getString("mc_tencent_appid");
            this.tencent = Tencent.createInstance(appidStr, this.context.getApplicationContext());
            qqAuth = QQAuth.createInstance(appidStr, this.context.getApplicationContext());
            this.contentList.add(getShareSiteModel(3, "mc_share_zone", "mc_forum_ico19_h"));
            this.contentList.add(getShareSiteModel(4, "mc_share_tencent", "mc_forum_ico22_h"));
        }
        if (!(MCStringUtil.isEmpty(this.resource.getString("mc_weibo_appid").trim()) || this.resource.getString("mc_weibo_appid").trim().startsWith("{"))) {
            this.weiboShareAPI = WeiboShareSDK.createWeiboAPI(this.context, this.resource.getString("mc_weibo_appid"));
            this.contentList.add(getShareSiteModel(5, "mc_share_sina_weibo", "mc_forum_ico23_h"));
        }
        this.contentList.add(getShareSiteModel(6, "mc_share_more", "mc_forum_ico41_n"));
    }

    private void initViews() {
        this.listView = (ListView) findViewById(this.resource.getViewId("mc_share_dialog_listview"));
        this.cancelBtn = (Button) findViewById(this.resource.getViewId("mc_share_dialog_cancel"));
        this.adapter = new MCShareListAdapter(this.context, this.contentList, this);
        this.listView.setAdapter(this.adapter);
    }

    private void initActions() {
//        this.cancelBtn.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                MCShareDialog.this.dismiss();
//            }
//        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MCShareDialog.this.dismiss();
            }
        });
    }

    private MCShareSiteModel getShareSiteModel(int id, String nameId, String imageId) {
        MCShareSiteModel model = new MCShareSiteModel();
        model.setSiteName(this.resource.getString(nameId));
        model.setSiteImage(imageId);
        model.setSiteId(id);
        return model;
    }

    public void itemClick(MCShareSiteModel model) {
        switch (model.getSiteId()) {
            case 0:
                Intent intent = new Intent(this.context, MCShareActivity.class);
                intent.putExtra(MCShareIntentConstant.MC_SHARE_MODEL, this.shareModel);
                this.context.startActivity(intent);
                break;
            case 1:
                this.openPlatType = this.OPEN_WECHAT;
                click();
                if (this.WXAPI == null) {
                    MCShareToastUtil.toast((Activity) this.context, this.resource.getString("mc_share_wechat_tip"));
                    break;
                } else {
                    MCShareWeChatHelper.shareToWeChatOrMoments(this.context, this.shareModel, MCShareWeChatHelper.WECHAT, this.WXAPI);
                    break;
                }
            case 2:
                this.openPlatType = this.OPEN_MOMENTS;
                click();
                if (this.WXAPI == null) {
                    MCShareToastUtil.toast((Activity) this.context, this.resource.getString("mc_share_wechat_tip"));
                    break;
                } else {
                    MCShareWeChatHelper.shareToWeChatOrMoments(this.context, this.shareModel, MCShareWeChatHelper.MOMENTS, this.WXAPI);
                    break;
                }
            case 3:
                this.openPlatType = this.OPEN_QZONE;
                click();
                if (this.tencent == null) {
                    MCShareToastUtil.toast((Activity) this.context, this.resource.getString("mc_share_tencent_tip"));
                    break;
                } else {
                    MCShareQZoneHelper.shareToQZone(this.context, this.shareModel, this.tencent);
                    break;
                }
            case 4:
                this.openPlatType = this.OPEN_QQ;
                click();
                if (this.tencent == null) {
                    MCShareToastUtil.toast((Activity) this.context, this.resource.getString("mc_share_tencent_tip"));
                    break;
                } else {
                    MCShareQQHelper.shareToQQ(this.context, this.shareModel, this.tencent);
                    break;
                }
            case 5:
                this.openPlatType = this.OPEN_WEIBO;
                click();
                if (this.weiboShareAPI == null) {
                    MCShareToastUtil.toast((Activity) this.context, this.resource.getString("mc_share_sina_weibo_tip"));
                    break;
                }
                this.weiboShareAPI.registerApp();
                if (!this.weiboShareAPI.isWeiboAppInstalled()) {
                    this.weiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                        public void onCancel() {
                            MCShareToastUtil.toast((Activity) MCShareDialog.this.context, MCShareDialog.this.resource.getString("mc_share_download_weibo"));
                        }
                    });
                    break;
                } else {
                    MCShareSinaWeiBoHelper.shareToWeiBo(this.context, this.shareModel, this.weiboShareAPI);
                    break;
                }
            case 6:
                shareToMore();
                break;
        }
        dismiss();
    }

    private void click() {
        new Thread(new Runnable() {
            public void run() {
                new MCShareServiceImpl(MCShareDialog.this.context).shareLog(MCShareDialog.this.shareModel.getAppKey(), MCShareDialog.this.openPlatType, MCShareDialog.this.resource.getString("mc_share_domain_url"), MCShareDialog.this.context);
            }
        }).start();
    }

    private String getSkipUrlByType() {
        String appKey = this.shareModel.getAppKey();
        int type = this.shareModel.getType();
        String skipUrl = this.shareModel.getSkipUrl();
        HashMap<String, String> params = this.shareModel.getParams();
        if ((type == 2 || type == 3) && MCStringUtil.isEmpty(skipUrl) && params != null && !params.isEmpty()) {
            skipUrl = this.resource.getString("mc_share_domain_url") + "share/shareWeb.do" + "?";
            MCLogUtil.e("getSkipUrlByType", "params != null");
            params.put("forumKey", appKey);
            for (String key : params.keySet()) {
                skipUrl = skipUrl + key + "=" + ((String) params.get(key)) + "&";
            }
        }
        MCLogUtil.e("MCShareDialog", "skipUrl = " + skipUrl);
        return skipUrl;
    }

    private void shareToSms() {
        MCLogUtil.e("MCShareDialog", "shareToSms");
        String content = this.shareModel.getContent();
        String shareUrl = getShareUrl();
        if (!MCStringUtil.isEmpty(shareUrl.trim())) {
            content = content + " " + shareUrl;
        }
        Intent intent;
        if (MCStringUtil.isEmpty(this.shareModel.getImageFilePath())) {
            intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:"));
            intent.putExtra("sms_body", content);
            try {
                this.context.startActivity(intent);
                return;
            } catch (Exception e) {
                return;
            }
        }
        boolean isMmsActivityExist = true;
        Intent mmsTestIntent = new Intent();
        mmsTestIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
        if (this.context.getPackageManager().resolveActivity(mmsTestIntent, 0) == null) {
            isMmsActivityExist = false;
        }
        intent = new Intent("android.intent.action.SEND");
        if (isMmsActivityExist) {
            intent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
        }
        intent.putExtra("sms_body", content);
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.shareModel.getImageFilePath())));
        intent.setType("image/*");
        try {
            this.context.startActivity(intent);
        } catch (Exception e2) {
        }
    }

    private void shareToMail() {
        MCLogUtil.e("MCShareDialog", "shareToMail");
        String content = this.shareModel.getContent();
        String shareUrl = getShareUrl();
        if (!MCStringUtil.isEmpty(shareUrl.trim())) {
            content = content + " " + shareUrl;
        }
        Intent emailIntent;
        if (MCStringUtil.isEmpty(this.shareModel.getImageFilePath())) {
            emailIntent = new Intent("android.intent.action.SENDTO", Uri.parse("mailto:"));
            emailIntent.putExtra("android.intent.extra.SUBJECT", this.shareModel.getTitle());
            emailIntent.putExtra("android.intent.extra.TEXT", content);
            try {
                this.context.startActivity(emailIntent);
                return;
            } catch (Exception e) {
                return;
            }
        }
        emailIntent = new Intent("android.intent.action.SEND", Uri.parse("mailto:"));
        emailIntent.putExtra("android.intent.extra.SUBJECT", this.shareModel.getTitle());
        emailIntent.putExtra("android.intent.extra.TEXT", content);
        emailIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.shareModel.getImageFilePath())));
        emailIntent.setType("application/octet-stream");
        try {
            this.context.startActivity(emailIntent);
        } catch (Exception e2) {
        }
    }

    private void shareToMore() {
        MCLogUtil.e("MCShareDialog", "shareToMore");
        String content = this.shareModel.getContent();
        String shareUrl = getShareUrl();
        if (!MCStringUtil.isEmpty(shareUrl.trim())) {
            content = content + " " + shareUrl;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        if (MCStringUtil.isEmpty(this.shareModel.getImageFilePath())) {
            intent.putExtra("android.intent.extra.SUBJECT", this.shareModel.getTitle());
            intent.putExtra("android.intent.extra.TEXT", content);
        } else {
            intent.putExtra("android.intent.extra.SUBJECT", this.shareModel.getTitle());
            intent.putExtra("android.intent.extra.TEXT", content);
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(this.shareModel.getImageFilePath())));
        }
        intent.setType("*/*");
        try {
            this.context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    private String getShareUrl() {
        String shareUrl = "";
        if (!MCStringUtil.isEmpty(this.shareModel.getLinkUrl())) {
            shareUrl = this.shareModel.getLinkUrl() + " ";
        }
        if (MCStringUtil.isEmpty(this.shareSharedPreferencesDB.getShareUrl())) {
            return shareUrl;
        }
        return shareUrl + this.shareSharedPreferencesDB.getShareUrl() + " ";
    }
}
