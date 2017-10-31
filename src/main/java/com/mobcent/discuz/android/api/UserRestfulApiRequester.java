package com.mobcent.discuz.android.api;

import android.content.Context;
import android.net.Uri;
import com.mobcent.discuz.android.api.util.DZHttpClientUtil;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.UserConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class UserRestfulApiRequester extends BaseDiscuzApiRequester implements UserConstant {
    public static String getMentionFriend(Context context) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_post_friends");
        HashMap<String, String> params = new HashMap();
        params.put("connection_timeout", "1000");
        params.put("socket_timeout", "2000");
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getUserInfo(Context context, long userId) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_user_info");
        HashMap<String, String> params = new HashMap();
        params.put("userId", new StringBuilder(String.valueOf(userId)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getUser(Context context) {
        return BaseDiscuzApiRequester.doPostRequest(context, MCResource.getInstance(context).getString("mc_forum_request_user_info"), new HashMap());
    }

    public static String getUpdateUserInfo(Context context, String type, String gender, String avatar, String oldPassword, String newPassword) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_update_user");
        HashMap<String, String> params = new HashMap();
        params.put("gender", gender);
        params.put("type", type);
        params.put("avatar", avatar);
        params.put(BaseApiConstant.USER_OLDPASSWORD, oldPassword);
        params.put(BaseApiConstant.USER_NEWPASSWORD, newPassword);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String uploadIcon(Context context, String uploadFile, long userId) {
        return uploadFile(new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_updata_img")).toString(), uploadFile, context, 0, "icon", "", "");
    }

    public static String uploadFile(String urlString, String uploadFile, Context ctx, long boardId, String type, String json, String module) {
        SharedPreferencesDB shareDB = SharedPreferencesDB.getInstance(ctx.getApplicationContext());
        return DZHttpClientUtil.uploadFile(urlString, new File(uploadFile), uploadFile, shareDB.getAccessToken(), shareDB.getAccessSecret(), json, type, module, shareDB.getForumKey());
    }

    public static String loginUser(Context context, String email, String password, String type) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_login_or_logout");
        HashMap<String, String> params = getRegLoginParams(context, email, password);
        params.put("type", type);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    private static HashMap<String, String> getRegLoginParams(Context context, String email, String password) {
        HashMap<String, String> params = new HashMap();
        params.put("username", Uri.encode(email, "utf-8"));
        params.put("password", Uri.encode(password, "utf-8"));
        String forumKey = SharedPreferencesDB.getInstance(context).getForumKey();
        if (forumKey != null) {
            params.put("forumKey", forumKey);
        }
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        String packageName = context.getPackageName();
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put("sdkType", "");
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        params.put("platType", "1");
        return params;
    }

    public static String switchUser(Context context, String userName) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_switch_user");
        HashMap<String, String> params = new HashMap();
        try {
            params.put("username", URLEncoder.encode(userName, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getManageUser(Context context, long userId, String type) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_manage_user");
        HashMap<String, String> params = new HashMap();
        params.put("uid", new StringBuilder(String.valueOf(userId)).toString());
        params.put("type", type);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String registUser(Context context, String user, String password, String email) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_registe_user");
        HashMap<String, String> params = new HashMap();
        params.put("username", Uri.encode(user, "utf-8"));
        params.put("password", password);
        params.put("email", email);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getUserList(Context context, long userId, int page, int pageSize, String userType, String orderBy, double longitude, double latitude) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_user_friend_list");
        HashMap<String, String> params = new HashMap();
        params.put("uid", String.valueOf(userId));
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("type", userType);
        params.put("orderBy", orderBy);
        params.put("longitude", new StringBuilder(String.valueOf(longitude)).toString());
        params.put("latitude", new StringBuilder(String.valueOf(latitude)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getSurroudUserByNet(Context context, double longitude, double latitude, int radius, int page, int pageSize) {
        String url = MCResource.getInstance(context).getString("mc_forum_surround_topic_list");
        HashMap<String, String> params = new HashMap();
        params.put("poi", "user");
        params.put("longitude", new StringBuilder(String.valueOf(longitude)).toString());
        params.put("latitude", new StringBuilder(String.valueOf(latitude)).toString());
        if (radius > 0) {
            params.put("radius", new StringBuilder(String.valueOf(radius)).toString());
        }
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String setUserLocationSetting(Context context, String settingJson) {
        String url = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_user_location_settting")).toString();
        HashMap<String, String> params = new HashMap();
        try {
            params.put("setting", URLEncoder.encode(settingJson, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getPlatFormLoginUserInfoModel(Context context, String openId, long paltformId, String oauthToken) {
        String url = MCResource.getInstance(context).getString("mc_forum_qq_login_result_openid");
        HashMap<String, String> params = new HashMap();
        params.put(UserConstant.OPENID, openId);
        params.put("platformId", new StringBuilder(String.valueOf(paltformId)).toString());
        params.put(UserConstant.OAUTH_TOKEN, oauthToken);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String saveWebRegisteUser(Context context, UserInfoModel userInfoModel, String webId) {
        String url = MCResource.getInstance(context).getString("mc_forum_qq_login_save_qq_info");
        HashMap<String, String> params = new HashMap();
        try {
            params.put("username", MCStringUtil.isEmpty(userInfoModel.getNickname()) ? "" : Uri.encode(userInfoModel.getNickname(), "utf-8"));
            params.put("email", MCStringUtil.isEmpty(userInfoModel.getEmail()) ? "" : URLEncoder.encode(userInfoModel.getEmail(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put(UserConstant.OPENID, userInfoModel.getOpenId());
        params.put(UserConstant.OAUTH_TOKEN, userInfoModel.getAutoToken());
        params.put("platformId", webId);
        params.put("gender", new StringBuilder(String.valueOf(userInfoModel.getGender())).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getUserPlatforminfo(Context context, String openid, String oauthToken, String platformId) {
        String url = MCResource.getInstance(context).getString("mc_forum_user_platforminfo");
        HashMap<String, String> params = new HashMap();
        params.put(UserConstant.OPENID, openid);
        params.put(UserConstant.OAUTH_TOKEN, oauthToken);
        params.put("platformId", platformId);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String saveUserPlatforminfo(Context context, UserInfoModel userInfoModel, String action, String platformId) {
        String url = MCResource.getInstance(context).getString("mc_forum_user_saveplatforminfo");
        HashMap<String, String> params = getRegLoginParams(context, userInfoModel.getNickname(), userInfoModel.getPwd());
        params.put("email", userInfoModel.getEmail());
        params.put("gender", new StringBuilder(String.valueOf(userInfoModel.getGender())).toString());
        params.put(UserConstant.OAUTH_TOKEN, userInfoModel.getToken());
        params.put(UserConstant.OPENID, userInfoModel.getOpenId());
        params.put("act", action);
        params.put("platformId", platformId);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
