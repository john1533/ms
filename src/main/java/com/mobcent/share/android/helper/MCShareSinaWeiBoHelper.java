package com.mobcent.share.android.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.ApiUtils;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.sina.weibo.sdk.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MCShareSinaWeiBoHelper {
    private static final String TAG = "MCShareSinaWeiBoHelper";
    private static Context context;

    public static void shareToWeiBo(Context context, MCShareModel shareModel, IWeiboShareAPI api) {
        context = context;
        try {
            if (!api.checkEnvironment(true)) {
                return;
            }
            if (!api.isWeiboAppSupportAPI()) {
                MCShareToastUtil.toast((Activity) context, MCResource.getInstance(context).getString("mc_share_sina_weibo_not_support_api_hint"));
            } else if (api.getWeiboAppSupportAPI() < ApiUtils.BUILD_INT_VER_2_2 || shareModel.getType() != 3) {
                WeiboMessage weiboMessage = getSingleMessage(context, shareModel);
                if (weiboMessage != null) {
                    SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
                    request.transaction = String.valueOf(System.currentTimeMillis());
                    request.message = weiboMessage;
                    api.sendRequest(request);
                }
            } else {
                WeiboMultiMessage weiboMultiMessage = getMultiMessage(context, shareModel);
                if (weiboMultiMessage != null) {
                    SendMultiMessageToWeiboRequest request2 = new SendMultiMessageToWeiboRequest();
                    request2.transaction = String.valueOf(System.currentTimeMillis());
                    request2.multiMessage = weiboMultiMessage;
                    api.sendRequest(request2);
                }
            }
        } catch (WeiboShareException e) {
            e.printStackTrace();
            MCShareToastUtil.toast((Activity) context, e.getMessage());
        }
    }

    private static WeiboMessage getSingleMessage(Context context, MCShareModel shareModel) {
        WeiboMessage weiboMessage = new WeiboMessage();
        switch (shareModel.getType()) {
            case 1:
                ImageObject imageObject = getImageObj(shareModel);
                if (imageObject == null) {
                    return null;
                }
                weiboMessage.mediaObject = imageObject;
                return weiboMessage;
            case 2:
            case 6:
            case 7:
                weiboMessage.mediaObject = getWebpageObj(context, shareModel);
                return weiboMessage;
            case 3:
                weiboMessage.mediaObject = getTextObj(context, shareModel);
                return weiboMessage;
            case 4:
                if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                    weiboMessage.mediaObject = getWebpageObj(context, shareModel);
                    return weiboMessage;
                }
                weiboMessage.mediaObject = getMusicObj(context, shareModel);
                return weiboMessage;
            case 5:
                if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                    weiboMessage.mediaObject = getWebpageObj(context, shareModel);
                    return weiboMessage;
                }
                weiboMessage.mediaObject = getVideoObj(context, shareModel);
                return weiboMessage;
            default:
                return weiboMessage;
        }
    }

    private static WeiboMultiMessage getMultiMessage(Context context, MCShareModel shareModel) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        switch (shareModel.getType()) {
            case 1:
                ImageObject imageObject = getImageObj(shareModel);
                if (imageObject == null) {
                    return null;
                }
                weiboMessage.imageObject = imageObject;
                return weiboMessage;
            case 2:
            case 6:
            case 7:
                weiboMessage.mediaObject = getWebpageObj(context, shareModel);
                return weiboMessage;
            case 3:
                weiboMessage.textObject = getTextObj(context, shareModel);
                ImageObject imageObject1 = getImageObj(shareModel);
                if (imageObject1 == null) {
                    return weiboMessage;
                }
                weiboMessage.imageObject = imageObject1;
                return weiboMessage;
            case 4:
                if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                    weiboMessage.mediaObject = getWebpageObj(context, shareModel);
                    return weiboMessage;
                }
                weiboMessage.mediaObject = getMusicObj(context, shareModel);
                return weiboMessage;
            case 5:
                if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
                    weiboMessage.mediaObject = getWebpageObj(context, shareModel);
                    return weiboMessage;
                }
                weiboMessage.mediaObject = getVideoObj(context, shareModel);
                return weiboMessage;
            default:
                return weiboMessage;
        }
    }

    private static ImageObject getImageObj(MCShareModel shareModel) {
        MCLogUtil.e(TAG, "getImageObj");
        ImageObject imageObject = new ImageObject();
        try {
            Bitmap bitmap;
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            File file = new File(shareModel.getImageFilePath());
            if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
                bitmap = getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
            } else if (!file.exists()) {
                return null;
            } else {
                bitmap = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            }
            imageObject.setImageObject(bitmap);
            return imageObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static TextObject getTextObj(Context context, MCShareModel shareModel) {
        TextObject textObject = new TextObject();
        String text = "【" + shareModel.getTitle() + "】" + shareModel.getContent() + "..." + MCResource.getInstance(context).getString("mc_share_check_more");
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            text = text + shareModel.getDownloadUrl();
        } else {
            text = text + shareModel.getSkipUrl();
        }
        textObject.text = text;
        return textObject;
    }

    private static WebpageObject getWebpageObj(Context context, MCShareModel shareModel) {
        MCLogUtil.e(TAG, "getWebpageObj");
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareModel.getTitle();
        mediaObject.description = shareModel.getContent();
        MCLogUtil.e(TAG, "getWebpageObj-base");
        try {
            Bitmap bitmap;
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            File file = new File(shareModel.getImageFilePath());
            if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
                bitmap = getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
            } else if (file.exists()) {
                bitmap = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            } else {
                bitmap = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), true);
            }
            mediaObject.setThumbImage(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MCStringUtil.isEmpty(shareModel.getSkipUrl())) {
            mediaObject.actionUrl = shareModel.getDownloadUrl();
        } else {
            mediaObject.actionUrl = shareModel.getSkipUrl();
        }
        mediaObject.defaultText = shareModel.getContent();
        return mediaObject;
    }

    private static MusicObject getMusicObj(Context context, MCShareModel shareModel) {
        MCLogUtil.e(TAG, "getMusicObj");
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = shareModel.getTitle();
        musicObject.description = shareModel.getContent();
        try {
            Bitmap bitmap;
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            File file = new File(shareModel.getImageFilePath());
            if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
                bitmap = getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
            } else if (file.exists()) {
                bitmap = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            } else {
                bitmap = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), true);
            }
            musicObject.setThumbImage(bitmap);
        } catch (IOException e) {
        }
        musicObject.actionUrl = shareModel.getSkipUrl();
        musicObject.dataUrl = shareModel.getSkipUrl();
        musicObject.dataHdUrl = shareModel.getSkipUrl();
        musicObject.duration = 10;
        musicObject.defaultText = shareModel.getContent();
        return musicObject;
    }

    private static VideoObject getVideoObj(Context context, MCShareModel shareModel) {
        MCLogUtil.e(TAG, "getVideoObj");
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = shareModel.getTitle();
        videoObject.description = shareModel.getContent();
        try {
            Bitmap bitmap;
            if (shareModel.getImageFilePath() == null) {
                shareModel.setImageFilePath("");
            }
            File file = new File(shareModel.getImageFilePath());
            if (!MCStringUtil.isEmpty(shareModel.getPicUrl())) {
                bitmap = getBitmapBytes(BitmapFactory.decodeStream(new URL(shareModel.getPicUrl()).openStream()), true);
            } else if (file.exists()) {
                bitmap = getBitmapBytes(BitmapFactory.decodeFile(shareModel.getImageFilePath()), true);
            } else {
                bitmap = getBitmapBytes(BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("app_icon128")), true);
            }
            videoObject.setThumbImage(bitmap);
        } catch (IOException e) {
        }
        videoObject.actionUrl = shareModel.getSkipUrl();
        videoObject.dataUrl = shareModel.getSkipUrl();
        videoObject.dataHdUrl = shareModel.getSkipUrl();
        videoObject.duration = 10;
        videoObject.defaultText = shareModel.getContent();
        return videoObject;
    }

    private static Bitmap getBitmapBytes(Bitmap bitmap, boolean paramBoolean) {
        return MCImageUtil.compressBitmap(bitmap, 3, context);
    }
}
