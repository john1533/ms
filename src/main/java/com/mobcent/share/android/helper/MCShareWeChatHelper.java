package com.mobcent.share.android.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.mobcent.android.db.MCShareSharedPreferencesDB;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX.Req;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MCShareWeChatHelper {
    public static final String MOMENTS = "Moments";
    private static final String TAG = "MCShareWeChatHelper";
    private static final String TYPE_IMG = "img";
    private static final String TYPE_MUSIC = "music";
    private static final String TYPE_VIDEO = "video";
    private static final String TYPE_WEB_PAGE = "webpage";
    public static final String WECHAT = "WeChat";

    public static void shareToWeChatOrMoments(Context context, MCShareModel shareModel, String name, IWXAPI api) {
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            shareModel.setSkipUrl(MCShareSharedPreferencesDB.getInstance(context).getShareUrl());
            MCLogUtil.e(TAG, "type=" + shareModel.getType() + " skipurl=" + shareModel.getSkipUrl());
        }
        switch (shareModel.getType()) {
            case 1:
                sharePicture(context, shareModel, name, api);
                return;
            case 2:
            case 3:
            case 6:
            case 7:
                shareWebPage(context, shareModel, name, api);
                return;
            case 4:
                if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                    shareWebPage(context, shareModel, name, api);
                    return;
                } else {
                    shareMusic(context, shareModel, name, api);
                    return;
                }
            case 5:
                if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                    shareWebPage(context, shareModel, name, api);
                    return;
                } else {
                    shareVideo(context, shareModel, name, api);
                    return;
                }
            default:
                return;
        }
    }

    private static void sharePicture(Context context, final MCShareModel shareModel, final String name, final IWXAPI api) {
        final WXMediaMessage msg = new WXMediaMessage();
        if (shareModel.getImageFilePath() == null) {
            shareModel.setImageFilePath("");
        }
        File file = new File(shareModel.getImageFilePath());
        if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
            new Thread() {
                public void run() {
                    WXImageObject imgObj = new WXImageObject();
                    imgObj.imageUrl = shareModel.getPicUrl();
                    msg.mediaObject = imgObj;
                    try {
                        msg.thumbData = MCShareWeChatHelper.getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MCShareWeChatHelper.sendReq("img", msg, shareModel, name, api);
                }
            }.start();
        } else if (file.exists()) {
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(shareModel.getImageFilePath());
            msg.mediaObject = imgObj;
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            sendReq("img", msg, shareModel, name, api);
        }
    }

    private static void sendReq(String type, WXMediaMessage msg, MCShareModel shareModel, String name, IWXAPI api) {
        Req req = new Req();
        req.transaction = buildTransaction(type);
        if (MOMENTS.equals(name)) {
            if ("img".equals(type)) {
                req.scene = 1;
            } else if (TYPE_WEB_PAGE.equals(type)) {
                msg.title = shareModel.getTitle();
                msg.description = shareModel.getContent();
                req.scene = 1;
            } else if (TYPE_MUSIC.equals(type)) {
                req.scene = 1;
            } else if ("video".equals(type)) {
                req.scene = 1;
            }
        } else if (WECHAT.equals(name)) {
            if ("img".equals(type)) {
                msg.title = shareModel.getTitle();
                req.scene = 0;
            } else if (TYPE_WEB_PAGE.equals(type)) {
                msg.title = shareModel.getTitle();
                msg.description = shareModel.getContent();
                req.scene = 0;
            } else if (TYPE_MUSIC.equals(type)) {
                req.scene = 0;
            } else if ("video".equals(type)) {
                req.scene = 0;
            }
        }
        req.message = msg;
        api.sendReq(req);
    }

    private static void shareWebPage(Context context, final MCShareModel shareModel, final String name, final IWXAPI api) {
        WXWebpageObject webpage = new WXWebpageObject();
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            webpage.webpageUrl = shareModel.getDownloadUrl();
        } else {
            webpage.webpageUrl = shareModel.getSkipUrl();
        }
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        if (shareModel.getImageFilePath() == null) {
            shareModel.setImageFilePath("");
        }
        File file = new File(shareModel.getImageFilePath());
        if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
            new Thread() {
                public void run() {
                    try {
                        msg.thumbData = MCShareWeChatHelper.getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
                        MCShareWeChatHelper.sendReq(MCShareWeChatHelper.TYPE_WEB_PAGE, msg, shareModel, name, api);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (file.exists()) {
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            sendReq(TYPE_WEB_PAGE, msg, shareModel, name, api);
        } else {
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), true);
            sendReq(TYPE_WEB_PAGE, msg, shareModel, name, api);
        }
    }

    private static void shareMusic(Context context, final MCShareModel shareModel, final String name, final IWXAPI api) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = shareModel.getSkipUrl();
        final WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        if (!MCStringUtil.isEmpty(shareModel.getTitle())) {
            String[] names = shareModel.getTitle().split("-");
            msg.title = names[0];
            if (names.length == 2) {
                msg.description = names[1];
            }
        }
        if (shareModel.getImageFilePath() == null) {
            shareModel.setImageFilePath("");
        }
        File file = new File(shareModel.getImageFilePath());
        if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
            new Thread() {
                public void run() {
                    try {
                        msg.thumbData = MCShareWeChatHelper.getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
                        MCShareWeChatHelper.sendReq(MCShareWeChatHelper.TYPE_MUSIC, msg, shareModel, name, api);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (file.exists()) {
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            sendReq(TYPE_MUSIC, msg, shareModel, name, api);
        } else {
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), true);
            sendReq(TYPE_MUSIC, msg, shareModel, name, api);
        }
    }

    private static void shareVideo(Context context, final MCShareModel shareModel, final String name, final IWXAPI api) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = shareModel.getSkipUrl();
        final WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = shareModel.getTitle();
        msg.description = shareModel.getContent();
        if (shareModel.getImageFilePath() == null) {
            shareModel.setImageFilePath("");
        }
        File file = new File(shareModel.getImageFilePath());
        if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
            new Thread() {
                public void run() {
                    try {
                        msg.thumbData = MCShareWeChatHelper.getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
                        MCShareWeChatHelper.sendReq("video", msg, shareModel, name, api);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (file.exists()) {
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            sendReq("video", msg, shareModel, name, api);
        } else {
            msg.thumbData = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), true);
            sendReq("video", msg, shareModel, name, api);
        }
    }

    private static byte[] getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        int i;
        int j;
        byte[] arrayOfByte;
        Bitmap localBitmap = Bitmap.createBitmap(80, 80, Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);
        if (bitmap.getHeight() > bitmap.getWidth()) {
            i = bitmap.getWidth();
            j = bitmap.getWidth();
        } else {
            i = bitmap.getHeight();
            j = bitmap.getHeight();
        }
        while (true) {
            localCanvas.drawBitmap(bitmap, new Rect(0, 0, i, j), new Rect(0, 0, 80, 80), null);
            if (paramBoolean) {
                bitmap.recycle();
            }
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(CompressFormat.JPEG, 100, localByteArrayOutputStream);
            localBitmap.recycle();
            arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                i = bitmap.getHeight();
                j = bitmap.getHeight();
            }
        }
        return arrayOfByte;
    }

    private static String buildTransaction(String type) {
        return type == null ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
