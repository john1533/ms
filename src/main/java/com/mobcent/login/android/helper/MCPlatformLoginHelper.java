package com.mobcent.login.android.helper;

import android.app.Activity;
import android.content.Context;
import com.mobcent.android.model.PlatformLoginInfoModel;
import com.mobcent.android.service.impl.PlatformLoginServiceImpl;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.share.android.helper.MCShareHelper;
import com.tencent.mm.sdk.modelmsg.SendAuth.Req;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONObject;

public class MCPlatformLoginHelper {
    private static MCPlatformLoginHelper helper;
    public PlatformLoginInfoModel loginInfoModel;

    public interface LoginCallbackListener {
        void onComplete(String str);

        void onError();
    }

    private MCPlatformLoginHelper() {
    }

    public static MCPlatformLoginHelper getInstance() {
        if (helper == null) {
            helper = new MCPlatformLoginHelper();
        }
        return helper;
    }

    public void loginFromWeChat(Context context) {
        MCResource resource = MCResource.getInstance(context);
        Req req = new Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_login";
        IWXAPI iwxapi = MCShareHelper.getIWXAPI(context, resource);
        if (iwxapi != null) {
            if (iwxapi.isWXAppInstalled()) {
                iwxapi.sendReq(req);
            } else {
                MCToastUtils.toastByResName(context, "mc_forum_user_wechat_authorization_no_client");
            }
        }
    }

    public void requestWeChatInfo(Context context, String code, String appid, String appsecret) {
        this.loginInfoModel = null;
        this.loginInfoModel = new PlatformLoginServiceImpl(context).getWeChatModel(code, appid, appsecret);
        this.loginInfoModel.setPlatformType("30");
    }

    public void loginFromQQ(Activity activity, final LoginCallbackListener listener) {
        this.loginInfoModel = null;
        Tencent.createInstance(MCResource.getInstance(activity.getApplicationContext()).getString("mc_tencent_appid"), activity.getApplicationContext()).login(activity, "all", new IUiListener() {
            public void onError(UiError arg0) {
                listener.onError();
            }

            public void onComplete(Object response) {
                MCPlatformLoginHelper.this.loginInfoModel = new PlatformLoginInfoModel();
                MCPlatformLoginHelper.this.loginInfoModel.setOpenid(((JSONObject) response).optString("openid"));
                MCPlatformLoginHelper.this.loginInfoModel.setAccessToken(((JSONObject) response).optString("access_token"));
                MCPlatformLoginHelper.this.loginInfoModel.setPlatformType("20");
                listener.onComplete(MCPlatformLoginHelper.this.loginInfoModel.getOpenid());
            }

            public void onCancel() {
            }
        });
    }
}
