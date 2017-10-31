package com.mobcent.share.android.helper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import com.mobcent.android.db.MCShareSharedPreferencesDB;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.mobcent.share.android.view.MCShareDialog;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.io.File;

public class MCShareQQHelper {
    private static final String TAG = "MCShareQQHelper";
    private static QQShare qqShare = null;
    private static int shareType = 1;

    public static void shareToQQ(Context context, MCShareModel shareModel, Tencent tencent) {
        qqShare = new QQShare(context, MCShareDialog.qqAuth.getQQToken());
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            shareModel.setSkipUrl(MCShareSharedPreferencesDB.getInstance(context).getShareUrl());
            MCLogUtil.e(TAG, "type=" + shareModel.getType() + " skipurl=" + shareModel.getSkipUrl());
        }
        switch (shareModel.getType()) {
            case 1:
                shareType = 5;
                sharePicture(context, shareModel, tencent);
                return;
            case 2:
            case 3:
            case 5:
            case 6:
            case 7:
                shareType = 1;
                shareMusicOrVideoOrDefault(context, shareModel, tencent);
                return;
            case 4:
                shareType = 2;
                shareMusicOrVideoOrDefault(context, shareModel, tencent);
                return;
            default:
                return;
        }
    }

    private static void sharePicture(Context context, MCShareModel shareModel, Tencent tencent) {
        Bundle params = new Bundle();
        try {
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            if (new File(shareModel.getImageFilePath()).exists()) {
                params.putString("imageLocalUrl", shareModel.getImageFilePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.putInt("req_type", shareType);
        doShareToQQ((Activity) context, params);
    }

    private static void shareMusicOrVideoOrDefault(Context context, MCShareModel shareModel, Tencent tencent) {
        Bundle params = new Bundle();
        params.putString("title", shareModel.getTitle());
        params.putString("summary", shareModel.getContent());
        Cursor cursor = null;
        try {
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            File file = new File(shareModel.getImageFilePath());
            if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
                params.putString("imageUrl", shareModel.getPicUrl());
            } else if (file.exists()) {
                params.putString("imageUrl", shareModel.getImageFilePath());
            } else {
                String uri;
                MCShareSharedPreferencesDB shareSharedPreferencesDB = MCShareSharedPreferencesDB.getInstance(context);
                if (MCStringUtil.isEmpty(shareSharedPreferencesDB.getAppIconUri())) {
                    uri = Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), null, null);
                    shareSharedPreferencesDB.setAppIconUri(uri);
                    cursor = context.getContentResolver().query(Uri.parse(uri), null, null, null, null);
                } else {
                    cursor = context.getContentResolver().query(Uri.parse(shareSharedPreferencesDB.getAppIconUri()), null, null, null, null);
                }
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        params.putString("imageUrl", cursor.getString(1));
                    } else {
                        uri = Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), null, null);
                        shareSharedPreferencesDB.setAppIconUri(uri);
                        cursor = context.getContentResolver().query(Uri.parse(uri), null, null, null, null);
                        if (cursor.moveToFirst()) {
                            params.putString("imageUrl", cursor.getString(1));
                        }
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        params.putInt("req_type", shareType);
        if (shareModel.getType() == 4 || shareModel.getType() == 5) {
            if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                params.putString("targetUrl", shareModel.getDownloadUrl());
            } else if (shareModel.getType() == 4) {
                params.putString("audio_url", shareModel.getSkipUrl());
                params.putString("targetUrl", shareModel.getDownloadUrl());
            } else if (shareModel.getType() == 5) {
                params.putString("targetUrl", shareModel.getSkipUrl());
            }
        } else if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            params.putString("targetUrl", shareModel.getDownloadUrl());
        } else {
            params.putString("targetUrl", shareModel.getSkipUrl());
        }
        doShareToQQ((Activity) context, params);
    }

    private static void doShareToQQ(final Activity activity, final Bundle params) {
        new Thread(new Runnable() {
            public void run() {
                MCShareQQHelper.qqShare.shareToQQ(activity, params, new IUiListener() {
                    public void onCancel() {
                    }

                    public void onComplete(Object response) {
                    }

                    public void onError(UiError e) {
                        MCShareToastUtil.toast(activity, "onError: " + e.errorMessage);
                    }
                });
            }
        }).start();
    }
}
