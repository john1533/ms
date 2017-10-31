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
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import java.io.File;
import java.util.ArrayList;

public class MCShareQZoneHelper {
    private static final String TAG = "MCShareQZoneHelper";
    private static int shareType = 1;

    public static void shareToQZone(Context context, MCShareModel shareModel, Tencent tencent) {
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            shareModel.setSkipUrl(MCShareSharedPreferencesDB.getInstance(context).getShareUrl());
            MCLogUtil.e(TAG, "type=" + shareModel.getType() + " skipurl=" + shareModel.getSkipUrl());
        }
        switch (shareModel.getType()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                shareType = 1;
                shareImageOrImageText(context, shareModel, tencent);
                return;
            default:
                return;
        }
    }

    private static void shareImageOrImageText(Context context, MCShareModel shareModel, Tencent tencent) {
        Bundle params = new Bundle();
        params.putInt("req_type", shareType);
        params.putString("title", shareModel.getTitle());
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            params.putString("targetUrl", shareModel.getDownloadUrl());
        } else {
            params.putString("targetUrl", shareModel.getSkipUrl());
        }
        params.putString("summary", shareModel.getContent());
        ArrayList<String> imageUrls = new ArrayList();
        Cursor cursor = null;
        try {
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            File file = new File(shareModel.getImageFilePath());
            if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
                imageUrls.add(shareModel.getPicUrl());
            } else if (file.exists()) {
                imageUrls.add(shareModel.getImageFilePath());
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
                        imageUrls.add(cursor.getString(1));
                    } else {
                        uri = Media.insertImage(context.getContentResolver(), BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), null, null);
                        shareSharedPreferencesDB.setAppIconUri(uri);
                        cursor = context.getContentResolver().query(Uri.parse(uri), null, null, null, null);
                        if (cursor.moveToFirst()) {
                            String imgPath = cursor.getString(1);
                            MCLogUtil.e(TAG, " url=" + imgPath);
                            imageUrls.add(imgPath);
                        }
                    }
                }
            }
            if (!(imageUrls == null || imageUrls.isEmpty())) {
                params.putStringArrayList("imageUrl", imageUrls);
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
        doShareToQzone((Activity) context, params, tencent);
    }

    private static void doShareToQzone(final Activity activity, final Bundle params, final Tencent tencent) {
        MCLogUtil.e(TAG, "doShareToQzone");
        new Thread(new Runnable() {
            public void run() {
                tencent.shareToQzone(activity, params, new IUiListener() {
                    public void onError(UiError e) {
                        MCShareToastUtil.toast(activity, "onError: " + e.errorMessage);
                    }

                    public void onComplete(Object response) {
                    }

                    public void onCancel() {
                    }
                });
            }
        }).start();
    }
}
