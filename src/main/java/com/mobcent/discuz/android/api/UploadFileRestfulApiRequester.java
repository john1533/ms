package com.mobcent.discuz.android.api;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.UploadConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.util.DZUploadFileUtils;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UploadFileRestfulApiRequester extends BaseDiscuzApiRequester implements UploadConstant {
    public static String upload(Context context, String[] files, String module, String type, long fid, long sortId, long albumId) {
        String requestUrl = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_request_send_attachment")).toString();
        Map<String, String> params = new HashMap();
        params.put("fid", new StringBuilder(String.valueOf(fid)).toString());
        params.put("albumId", new StringBuilder(String.valueOf(albumId)).toString());
        params.put("sortId", new StringBuilder(String.valueOf(sortId)).toString());
        params.put("module", module);
        params.put("type", type);
        params.put(BaseApiConstant.APPHASH, MCStringUtil.stringToMD5(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString().substring(0, 5))).append(BaseApiConstant.AUTHKEY).toString()).substring(8, 16));
        String packageName = context.getPackageName();
        SharedPreferencesDB shareDB = SharedPreferencesDB.getInstance(context);
        String forumKey = shareDB.getForumKey();
        String accessToken = shareDB.getAccessToken();
        String accessSecret = shareDB.getAccessSecret();
        if (forumKey != null) {
            params.put("forumKey", forumKey);
        }
        if (accessToken != null) {
            params.put("accessToken", accessToken);
        }
        if (accessSecret != null) {
            params.put("accessSecret", accessSecret);
        }
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put(BaseApiConstant.FORUM_TYPE, "7");
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        params.put("platType", "1");
        return DZUploadFileUtils.upload(context, requestUrl, params, parseFiles(context, files), type, "uploadFile[]", false);
    }

    public static String uploadIcon(Context context, String iconPath) {
        String baseUrl = MCResource.getInstance(context).getString("mc_discuz_base_request_url");
        String requestUrl = new StringBuilder(String.valueOf(baseUrl)).append(MCResource.getInstance(context).getString("mc_forum_request_upload_avatarex")).toString();
        Map<String, String> params = new HashMap();
        params.put(BaseApiConstant.APPHASH, MCStringUtil.stringToMD5(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString().substring(0, 5))).append(BaseApiConstant.AUTHKEY).toString()).substring(8, 16));
        String packageName = context.getPackageName();
        SharedPreferencesDB shareDB = SharedPreferencesDB.getInstance(context);
        String forumKey = shareDB.getForumKey();
        String accessToken = shareDB.getAccessToken();
        String accessSecret = shareDB.getAccessSecret();
        if (forumKey != null) {
            params.put("forumKey", forumKey);
        }
        if (accessToken != null) {
            params.put("accessToken", accessToken);
        }
        if (accessSecret != null) {
            params.put("accessSecret", accessSecret);
        }
        params.put("imei", MCPhoneUtil.getIMEI(context));
        params.put("imsi", MCPhoneUtil.getIMSI(context));
        String appName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        params.put("packageName", packageName);
        params.put("appName", appName);
        params.put(BaseApiConstant.FORUM_TYPE, "7");
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        params.put("platType", "1");
        return DZUploadFileUtils.upload(context, requestUrl, params, parseFiles(context, new String[]{iconPath}), "image", PostsConstant.USER_ICON, false);
    }

    private static File[] parseFiles(Context context, String[] filesPath) {
        if (filesPath == null || filesPath.length == 0) {
            return null;
        }
        File[] files = new File[filesPath.length];
        int len = filesPath.length;
        for (int i = 0; i < len; i++) {
            String filePath;
            String path = filesPath[i];
            if (TextUtils.isEmpty(path) || !path.startsWith("file")) {
                filePath = path;
            } else {
                filePath = MCLibIOUtil.getRealFilePath(context, Uri.parse(path));
            }
            files[i] = new File(filePath);
        }
        return files;
    }
}
