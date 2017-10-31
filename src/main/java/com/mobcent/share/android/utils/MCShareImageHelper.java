package com.mobcent.share.android.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import com.mobcent.android.utils.PhoneUtil;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.io.File;

public class MCShareImageHelper {
    public static Bitmap compressBitmap(String pathName, float targetWidth, Activity act) {
        int dip = PhoneUtil.getDisplayDpi(act);
        if (dip == 120) {
            targetWidth *= CustomConstant.RATIO_ONE_HEIGHT;
        } else if (dip == 160) {
            targetWidth = (float) (((double) targetWidth) * 0.8d);
        } else if (dip == 240) {
            targetWidth *= 2.0f;
        }
        File file = new File(pathName);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        int maxW;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        int outh = options.outHeight;
        int outw = options.outWidth;
        if (outh > outw) {
            maxW = outh;
        } else {
            maxW = outw;
        }
        int be = (int) (((float) maxW) / targetWidth);
        MCLogUtil.e("", "be=" + be);
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (resizedBitmap == bitmap) {
                return bitmap;
            }
            bitmap.recycle();
            return resizedBitmap;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
