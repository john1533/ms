package com.mobcent.share.android.helper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.android.db.MCShareSharedPreferencesDB;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.android.service.MCShareService;
import com.mobcent.android.service.impl.MCShareServiceImpl;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.mobcent.share.android.view.MCShareDialog;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.util.List;

public class MCShareHelper {

    private static class GetDataTask extends AsyncTask<String, Void, List<MCShareSiteModel>> {
        private Context context;
        private String cty;
        private String domainUrl;
        private String lan;
        private MCShareModel shareModel;
        private MCShareService shareService;
        private MCShareSharedPreferencesDB sharedPreferencesDB;

        public GetDataTask(Context context, MCShareModel shareModel) {
            this.context = context;
            this.shareModel = shareModel;
            this.domainUrl = MCResource.getInstance(context.getApplicationContext()).getString("mc_share_domain_url");
            this.lan = context.getResources().getConfiguration().locale.getLanguage();
            this.cty = context.getResources().getConfiguration().locale.getCountry();
            this.sharedPreferencesDB = MCShareSharedPreferencesDB.getInstance(context);
            this.shareService = new MCShareServiceImpl(context);
        }

        protected void onPreExecute() {
            if (MCStringUtil.isEmpty(this.sharedPreferencesDB.getShareUrl())) {
                this.sharedPreferencesDB.setShareUrl(this.shareModel.getDownloadUrl());
            }
            if (this.shareModel.getType() != 6) {
                this.sharedPreferencesDB.setShareNormalContent(this.shareModel.getContent());
            } else if (MCStringUtil.isEmpty(this.sharedPreferencesDB.getShareAppContent())) {
                this.sharedPreferencesDB.setShareAppContent(this.shareModel.getContent());
            }
        }

        protected List<MCShareSiteModel> doInBackground(String... params) {
            return this.shareService.getAllSites(this.shareModel.getAppKey(), this.domainUrl, this.lan, this.cty);
        }

        protected void onPostExecute(List<MCShareSiteModel> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                if (MCStringUtil.isEmpty(((MCShareSiteModel) result.get(0)).getRsReason())) {
                    String shareUrl = ((MCShareSiteModel) result.get(0)).getShareUrl();
                    if (MCStringUtil.isEmpty(shareUrl.trim())) {
                        this.sharedPreferencesDB.setShareUrl(this.shareModel.getDownloadUrl());
                    } else {
                        this.sharedPreferencesDB.setShareUrl(shareUrl);
                    }
                    if (this.shareModel.getType() == 6) {
                        String appContent = ((MCShareSiteModel) result.get(0)).getShareContent();
                        if (MCStringUtil.isEmpty(appContent.trim())) {
                            this.sharedPreferencesDB.setShareNormalContent(this.shareModel.getContent());
                            return;
                        }
                        this.sharedPreferencesDB.setShareAppContent(appContent);
                        this.shareModel.setContent(appContent);
                        return;
                    }
                    return;
                }
                MCShareToastUtil.toast((Activity) this.context, ((MCShareSiteModel) result.get(0)).getRsReason());
            }
        }
    }

    public static void share(Context context, MCShareModel shareModel) {
        MCLogUtil.e("shareModel", "shareModel-->" + shareModel.toString());
        new GetDataTask(context, shareModel).execute(new String[0]);
        popupDialog(context, shareModel);
    }

    private static void popupDialog(Context context, MCShareModel shareModel) {
        new MCShareDialog(context, MCResource.getInstance(context.getApplicationContext()).getStyleId("mc_share_dialog_style"), shareModel).show();
    }

    public static void shareToWeChat(Context context, MCShareModel shareModel) {
        new GetDataTask(context, shareModel).execute(new String[0]);
        MCResource resource = MCResource.getInstance(context.getApplicationContext());
        IWXAPI WXAPI = getIWXAPI(context, resource);
        if (WXAPI != null) {
            MCShareWeChatHelper.shareToWeChatOrMoments(context, shareModel, MCShareWeChatHelper.WECHAT, WXAPI);
        } else {
            MCShareToastUtil.toast((Activity) context, resource.getString("mc_share_wechat_tip"));
        }
    }

    public static void shareToMoments(Context context, MCShareModel shareModel) {
        new GetDataTask(context, shareModel).execute(new String[0]);
        MCResource resource = MCResource.getInstance(context.getApplicationContext());
        IWXAPI WXAPI = getIWXAPI(context, resource);
        if (WXAPI != null) {
            MCShareWeChatHelper.shareToWeChatOrMoments(context, shareModel, MCShareWeChatHelper.MOMENTS, WXAPI);
        } else {
            MCShareToastUtil.toast((Activity) context, resource.getString("mc_share_wechat_tip"));
        }
    }

    public static IWXAPI getIWXAPI(Context context, MCResource resource) {
        if (MCStringUtil.isEmpty(resource.getString("mc_wechat_appid").trim()) || resource.getString("mc_wechat_appid").trim().startsWith("{")) {
            return null;
        }
        String appidStr = resource.getString("mc_wechat_appid");
        IWXAPI WXAPI = WXAPIFactory.createWXAPI(context, appidStr);
        WXAPI.registerApp(appidStr);
        return WXAPI;
    }
}
