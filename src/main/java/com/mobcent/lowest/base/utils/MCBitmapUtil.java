package com.mobcent.lowest.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.model.BitmapModel;

public class MCBitmapUtil {
    public static BitmapModel getBitmapWidHeightByScreen(Context context, Bitmap bitmap, boolean doOverScreen) {
        return getBitmapWidHeightByScreen(context, bitmap.getWidth(), bitmap.getHeight(), doOverScreen, 1);
    }

    public static BitmapModel getBitmapWidHeightByScreen(Context context, int width, int height, boolean doOverScreen, int num) {
        int screenWidth = MCPhoneUtil.getDisplayWidth((Activity) context) / num;
        int screenHeight = MCPhoneUtil.getDisplayHeight((Activity) context) / num;
        float widthPoint = ((float) width) / ((float) screenWidth);
        float heightPoint = ((float) height) / ((float) screenHeight);
        float h_w = ((float) height) / ((float) width);
        float w_h = ((float) width) / ((float) height);
        BitmapModel bitmapModel = new BitmapModel();
        if (widthPoint > CustomConstant.RATIO_ONE_HEIGHT || heightPoint > CustomConstant.RATIO_ONE_HEIGHT || !doOverScreen) {
            if (widthPoint <= CustomConstant.RATIO_ONE_HEIGHT && heightPoint <= CustomConstant.RATIO_ONE_HEIGHT && !doOverScreen) {
                bitmapModel.setWidth(width);
                bitmapModel.setHeight(height);
            } else if (widthPoint > CustomConstant.RATIO_ONE_HEIGHT && heightPoint < CustomConstant.RATIO_ONE_HEIGHT) {
                bitmapModel.setWidth(screenWidth);
                bitmapModel.setHeight((int) (((float) screenWidth) * h_w));
            } else if (widthPoint < CustomConstant.RATIO_ONE_HEIGHT && heightPoint > CustomConstant.RATIO_ONE_HEIGHT) {
                bitmapModel.setHeight(screenHeight);
                bitmapModel.setWidth((int) (((float) screenHeight) * w_h));
            } else if (widthPoint > CustomConstant.RATIO_ONE_HEIGHT && heightPoint > CustomConstant.RATIO_ONE_HEIGHT) {
                if (widthPoint >= heightPoint) {
                    bitmapModel.setWidth(screenWidth);
                    bitmapModel.setHeight((int) (((float) screenWidth) * h_w));
                } else {
                    bitmapModel.setHeight(screenHeight);
                    bitmapModel.setWidth((int) (((float) screenHeight) * w_h));
                }
            }
        } else if (widthPoint >= heightPoint) {
            bitmapModel.setWidth(screenWidth);
            bitmapModel.setHeight((int) (((float) screenWidth) * h_w));
        } else if (widthPoint < heightPoint) {
            bitmapModel.setHeight(screenHeight);
            bitmapModel.setWidth((int) (((float) screenHeight) * w_h));
        }
        return bitmapModel;
    }

    private static Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        Bitmap output = Bitmap.createBitmap(x, y, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF outerRect = new RectF(0.0f, 0.0f, (float) x, (float) y);
        Paint paint = new Paint(1);
        paint.setColor(-1);
        int sc = canvas.saveLayer(0.0f, 0.0f, (float) x, (float) y, null, 31);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat == ((float) x) ? (float) x : outerRadiusRat, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        if (!(image == null || image.isRecycled())) {
            int realWidth;
            int bmWidth = image.getWidth();
            int bmHeight = image.getHeight();
            int left = bmWidth > bmHeight ? (-(bmWidth - bmHeight)) / 2 : 0;
            int top = bmHeight > bmWidth ? (-(bmHeight - bmWidth)) / 2 : 0;
            if (bmWidth > bmHeight) {
                realWidth = bmHeight;
            } else {
                realWidth = bmWidth;
            }
            canvas.drawBitmap(image, new Rect(left, top, left + realWidth, top + realWidth), new RectF(0.0f, 0.0f, (float) x, (float) y), paint);
        }
        canvas.restoreToCount(sc);
        return output;
    }

    public static Bitmap createRoundHeadIcon(Context context, float width, float height, int contentWidth, Bitmap image) {
        float whiteBgContentWidth = (float) contentWidth;
        float imgContentWidth = (float) (contentWidth - MCPhoneUtil.dip2px(context, 2.0f));
        Bitmap output = Bitmap.createBitmap((int) width, (int) height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint(1);
        paint.setColor(-1);
        float round1Left = (width - whiteBgContentWidth) / 2.0f;
        RectF outerRect = new RectF(round1Left, round1Left, round1Left + whiteBgContentWidth, round1Left + whiteBgContentWidth);
        canvas.drawRoundRect(outerRect, outerRect.width() / 2.0f, outerRect.height() / 2.0f, paint);
        Paint paint2 = new Paint(1);
        paint2.setColor(-1);
        int sc = canvas.saveLayer(0.0f, 0.0f, width, height, null, 31);
        float round2Left = (width - imgContentWidth) / 2.0f;
        RectF dp26Rect = new RectF(round2Left, round2Left, round2Left + imgContentWidth, round2Left + imgContentWidth);
        canvas.drawRoundRect(dp26Rect, dp26Rect.width() / 2.0f, dp26Rect.height() / 2.0f, paint2);
        paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        if (!(image == null || image.isRecycled())) {
            canvas.drawBitmap(image, null, new RectF(dp26Rect), paint2);
        }
        canvas.restoreToCount(sc);
        return output;
    }

    public static Bitmap createRoundIcon(Context context, float width, float height, Bitmap image) {
        float imgContentWidth = width;
        Bitmap output = Bitmap.createBitmap((int) width, (int) height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint2 = new Paint(1);
        paint2.setColor(-1);
        int sc = canvas.saveLayer(0.0f, 0.0f, width, height, null, 31);
        float round2Left = (width - imgContentWidth) / 2.0f;
        RectF dp26Rect = new RectF(round2Left, round2Left, round2Left + imgContentWidth, round2Left + imgContentWidth);
        canvas.drawRoundRect(dp26Rect, dp26Rect.width() / 2.0f, dp26Rect.height() / 2.0f, paint2);
        paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        if (!(image == null || image.isRecycled())) {
            canvas.drawBitmap(image, null, new RectF(dp26Rect), paint2);
        }
        canvas.restoreToCount(sc);
        return output;
    }
}
