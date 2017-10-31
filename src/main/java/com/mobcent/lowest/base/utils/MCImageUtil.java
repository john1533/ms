package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.service.impl.FileTransferServiceImpl;
import java.io.File;
import java.io.FileOutputStream;

public class MCImageUtil {
    public static final int DEPENDS_ON_HEIGHT = 4;
    public static final int DEPENDS_ON_LONGEST_EDGE = 2;
    public static final int DEPENDS_ON_SHORTEST_EDGE = 1;
    public static final int DEPENDS_ON_WIDTH = 3;
    private static final int OPTIONS_NONE = 0;
    public static final int OPTIONS_RECYCLE_INPUT = 2;
    private static final int OPTIONS_SCALE_UP = 1;
    private static final int defaultMaxWidth = 640;
    private static final float defaultScale = 0.8f;
    private static final int defaultShowWidth = 200;

    public static android.graphics.Bitmap compressBitmap(android.content.Context r11, byte[] r12, int r13, int r14, float r15, boolean r16) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:32:? in {2, 5, 11, 13, 16, 19, 26, 28, 29, 30, 31, 33, 34, 35} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/

        Options r5 = new Options();
        r5.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(r12,0,r13,r5);
        r5.inJustDecodeBounds = false;
        int r3;
        if(r5.outHeight<=r5.outWidth){
            r3 = r5.outWidth;
        }else {
            r3 = r5.outHeight;
        }
        int r2 = 0;
        int r9 = r15 > 1.0 ? 1:(r15 == 1.0?0:-1);

        if(r9 != 0){//0033
            r2 = (int)((float)r3/(float)getMinWidthOfBitmap(r11,r15,r14));
            if(r2>0){//003f
                if(r2>=10){
                    r2 = 10;
                }
            }else {
                r2 = 1;
            }
        }else{
            r2 = 1;
        }
        r5.inSampleSize = r2;
        Bitmap r0 = BitmapFactory.decodeByteArray(r12,r9,r13,r5);
        r12 = null;
        if(r16&&r0!=null){
            r0 = showMatrixBitmap(r11,r0);
        }
        return  r0;

        /*
        r0 = 0;
        r5 = new android.graphics.BitmapFactory$Options;
        r5.<init>();
        r9 = 1;
        r5.inJustDecodeBounds = r9;
        r9 = 0;
        android.graphics.BitmapFactory.decodeByteArray(r12, r9, r13, r5);
        r9 = 0;
        r5.inJustDecodeBounds = r9;
        r6 = r5.outHeight;
        r7 = r5.outWidth;
        if (r6 <= r7) goto L_0x0031;
    L_0x0016:
        r3 = r6;
    L_0x0017:
        r2 = 0;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = (r15 > r9 ? 1 : (r15 == r9 ? 0 : -1));
        if (r9 != 0) goto L_0x0033;
    L_0x001e:
        r2 = 1;
    L_0x001f:
        r5.inSampleSize = r2;
        r9 = 0;
        r0 = android.graphics.BitmapFactory.decodeByteArray(r12, r9, r13, r5);	 Catch:{ OutOfMemoryError -> 0x0046, Exception -> 0x004d, all -> 0x0051 }
        r12 = 0;
        if (r16 == 0) goto L_0x002f;
    L_0x0029:
        if (r0 == 0) goto L_0x002f;
    L_0x002b:
        r0 = showMatrixBitmap(r11, r0);
    L_0x002f:
        r9 = r0;
    L_0x0030:
        return r9;
    L_0x0031:
        r3 = r7;
        goto L_0x0017;
    L_0x0033:
        r8 = getMinWidthOfBitmap(r11, r15, r14);
        r9 = (float) r3;
        r10 = (float) r8;
        r9 = r9 / r10;
        r2 = (int) r9;
        if (r2 > 0) goto L_0x003f;
    L_0x003d:
        r2 = 1;
        goto L_0x001f;
    L_0x003f:
        r9 = 10;
        if (r2 < r9) goto L_0x001f;
    L_0x0043:
        r2 = 10;
        goto L_0x001f;
    L_0x0046:
        r4 = move-exception;
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x0046, Exception -> 0x004d, all -> 0x0051 }
        r12 = 0;
        r9 = 0;
        goto L_0x0030;
    L_0x004d:
        r1 = move-exception;
        r12 = 0;
        r9 = 0;
        goto L_0x0030;
    L_0x0051:
        r9 = move-exception;
        r12 = 0;
        throw r9;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.base.utils.MCImageUtil.compressBitmap(android.content.Context, byte[], int, int, float, boolean):android.graphics.Bitmap");
    }

    public static android.graphics.Bitmap compressBitmap(android.graphics.Bitmap r3, int r4, int r5, android.content.Context r6) {
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        try {
            Bitmap r1 = resizeBitmap(r3,r4,r5);
            if(r3 != r1 ){
                r3.recycle();
                r3 = r1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r3;
        /*
        r1 = 0;
        r1 = resizeBitmap(r3, r4, r5);	 Catch:{ OutOfMemoryError -> 0x000c, Exception -> 0x0017, all -> 0x001f }
        if (r1 == r3) goto L_0x000b;
    L_0x0007:
        r3.recycle();
        r3 = r1;
    L_0x000b:
        return r3;
    L_0x000c:
        r0 = move-exception;
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x000c, Exception -> 0x0017, all -> 0x001f }
        if (r1 == r3) goto L_0x000b;
    L_0x0012:
        r3.recycle();
        r3 = r1;
        goto L_0x000b;
    L_0x0017:
        r2 = move-exception;
        if (r1 == r3) goto L_0x000b;
    L_0x001a:
        r3.recycle();
        r3 = r1;
        goto L_0x000b;
    L_0x001f:
        r2 = move-exception;
        if (r1 == r3) goto L_0x0026;
    L_0x0022:
        r3.recycle();
        r3 = r1;
    L_0x0026:
        throw r2;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.base.utils.MCImageUtil.compressBitmap(android.graphics.Bitmap, int, int, android.content.Context):android.graphics.Bitmap");
    }

    public static boolean downloadGifImage(String url, String size, Context context) {
        return downloadGif(url, size, context);
    }

    public static boolean downloadGif(String urlString, String size, Context context) {
        if (urlString == null || size == null || urlString.equals("")) {
            return false;
        }
        if (urlString.indexOf("xgsize") > -1) {
            urlString = urlString.replace("xgsize", size);
        }
        String imagePath = MCLibIOUtil.getImageCachePath(context);
        if (!MCLibIOUtil.isDirExist(imagePath) && !MCLibIOUtil.makeDirs(imagePath)) {
            return false;
        }
        String fileName = getPathName(urlString);
        if (MCLibIOUtil.isFileExist(new StringBuilder(String.valueOf(imagePath)).append(fileName).toString())) {
            return true;
        }
        return MCLibIOUtil.saveFile(new FileTransferServiceImpl(context).getImageStream(urlString), fileName, imagePath);
    }

    public static String getPathName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int policy, Context context) {
        return compressBitmap(bitmap, 200, policy, context);
    }

    public static Bitmap getBitmapFromMedia(Context context, String pathName, int maxWidth, int maxHeight) {
        Options options = new Options();
        try {
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFile(pathName, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            if (bitmap == null) {
                return null;
            }
            int result = new ExifInterface(pathName).getAttributeInt("Orientation", -1);
            if (result != -1) {
                int rotate = 0;
                switch (result) {
                    case 3:
                        rotate = 180;
                        break;
                    case 6:
                        rotate = 90;
                        break;
                    case 8:
                        rotate = 270;
                        break;
                }
                if (rotate > 0) {
                    Matrix matrix = new Matrix();
                    matrix.preRotate((float) rotate);
                    Bitmap tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    bitmap = tempBitmap;
                }
            }
            return bitmap;
        } catch (Exception e) {
            System.gc();
            return null;
        }
    }

    public static int calculateInSampleSize(Options options, int maxWidth, int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round(((float) height) / ((float) maxHeight));
            } else {
                inSampleSize = Math.round(((float) width) / ((float) maxWidth));
            }
            float totalPixels = (float) (width * height);
            float maxTotalPixels = (float) (maxWidth * maxHeight);
            while (totalPixels / ((float) (inSampleSize * inSampleSize)) > maxTotalPixels && (totalPixels / ((float) (inSampleSize * inSampleSize))) / maxTotalPixels > 1.4f) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static boolean compressBitmap(String compressPath, Bitmap bitmap, int quality, int policy, Context context) {
        return compressBitmap(compressPath, bitmap, (int) defaultMaxWidth, quality, policy, context);
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean compressBitmap(java.lang.String r5, android.graphics.Bitmap r6, int r7, int r8, int r9, android.content.Context r10) {
        Bitmap r2 = null;
        boolean r4 = false;
        try {
            r2 = resizeBitmap(r6,r7,r9);
            FileOutputStream r1 = new FileOutputStream(new File(r5));
            r4 = r2.compress(CompressFormat.JPEG,r8,r1);
            if(r2 != r6){
                r2.recycle();
                r2 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r4;

        /*
        r2 = 0;
        r2 = resizeBitmap(r6, r7, r9);	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        r3 = new java.io.File;	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        r3.<init>(r5);	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        r1 = 0;
        r1 = new java.io.FileOutputStream;	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        r1.<init>(r3);	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        r4 = android.graphics.Bitmap.CompressFormat.JPEG;	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        r4 = r2.compress(r4, r8, r1);	 Catch:{ OutOfMemoryError -> 0x0020, Exception -> 0x002f }
        if (r2 == r6) goto L_0x001f;
    L_0x0018:
        r2.recycle();
        r2 = 0;
        java.lang.System.gc();
    L_0x001f:
        return r4;
    L_0x0020:
        r0 = move-exception;
        java.lang.System.gc();	 Catch:{ all -> 0x003a }
        if (r2 == r6) goto L_0x002d;
    L_0x0026:
        r2.recycle();
        r2 = 0;
        java.lang.System.gc();
    L_0x002d:
        r4 = 0;
        goto L_0x001f;
    L_0x002f:
        r4 = move-exception;
        if (r2 == r6) goto L_0x002d;
    L_0x0032:
        r2.recycle();
        r2 = 0;
        java.lang.System.gc();
        goto L_0x002d;
    L_0x003a:
        r4 = move-exception;
        if (r2 == r6) goto L_0x0044;
    L_0x003d:
        r2.recycle();
        r2 = 0;
        java.lang.System.gc();
    L_0x0044:
        throw r4;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.base.utils.MCImageUtil.compressBitmap(java.lang.String, android.graphics.Bitmap, int, int, int, android.content.Context):boolean");
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int upperLimitSize, int policy) throws Exception {
        if (policy == 4) {
            return resizeAccordHeightEdgeBitmap(bitmap, upperLimitSize);
        }
        if (policy == 3) {
            return resizeAccordWidthEdgeBitmap(bitmap, upperLimitSize);
        }
        if (policy == 1) {
            return resizeAccordShortestEdgeBitmap(bitmap, upperLimitSize);
        }
        if (policy == 2) {
            return resizeAccordLongestEdgeBitmap(bitmap, upperLimitSize);
        }
        return resizeAccordWidthEdgeBitmap(bitmap, upperLimitSize);
    }

    public static Bitmap resizeAccordWidthEdgeBitmap(Bitmap bitmap, int upperLimitSize) throws Exception {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= upperLimitSize && height <= upperLimitSize) {
            return bitmap;
        }
        float scaleWidth = ((float) upperLimitSize) / ((float) width);
        float scaleHeight = ((float) ((int) (((float) upperLimitSize) * (((float) height) / ((float) width))))) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeAccordHeightEdgeBitmap(Bitmap bitmap, int upperLimitSize) throws Exception {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= upperLimitSize && height <= upperLimitSize) {
            return bitmap;
        }
        float scaleWidth = ((float) ((int) (((float) upperLimitSize) * (((float) width) / ((float) height))))) / ((float) width);
        float scaleHeight = ((float) upperLimitSize) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeAccordShortestEdgeBitmap(Bitmap bitmap, int upperLimitSize) throws Exception {
        if (bitmap == null) {
            return null;
        }
        float scaleWidth;
        float scaleHeight;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= height) {
            scaleWidth = ((float) upperLimitSize) / ((float) width);
            scaleHeight = ((float) ((int) (((float) upperLimitSize) * (((float) height) / ((float) width))))) / ((float) height);
        } else {
            scaleWidth = ((float) ((int) (((float) upperLimitSize) * (((float) width) / ((float) height))))) / ((float) width);
            scaleHeight = ((float) upperLimitSize) / ((float) height);
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap resizeAccordLongestEdgeBitmap(Bitmap bitmap, int upperLimitSize) throws Exception {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= upperLimitSize && height <= upperLimitSize) {
            return bitmap;
        }
        float scaleWidth;
        float scaleHeight;
        if (width >= height) {
            scaleWidth = ((float) upperLimitSize) / ((float) width);
            scaleHeight = ((float) ((int) (((float) upperLimitSize) * (((float) height) / ((float) width))))) / ((float) height);
        } else {
            scaleWidth = ((float) ((int) (((float) upperLimitSize) * (((float) width) / ((float) height))))) / ((float) width);
            scaleHeight = ((float) upperLimitSize) / ((float) height);
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap == bitmap) {
            return bitmap;
        }
        bitmap.recycle();
        return resizedBitmap;
    }

    public static Bitmap compressBitmap(Context context, String pathName, float scale, int maxWidth, boolean isMatrix) {
        int maxW;
        int inSampleSize;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        int outputHeight = options.outHeight;
        int outputWidth = options.outWidth;
        if (outputHeight > outputWidth) {
            maxW = outputHeight;
        } else {
            maxW = outputWidth;
        }
        if (scale == CustomConstant.RATIO_ONE_HEIGHT) {
            inSampleSize = 1;
        } else {
            inSampleSize = (int) (((float) maxW) / ((float) getMinWidthOfBitmap(context, scale, maxWidth)));
            if (inSampleSize <= 0) {
                inSampleSize = 1;
            }
        }
        options.inSampleSize = inSampleSize;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            if (isMatrix && bitmap != null) {
                bitmap = showMatrixBitmap(context, bitmap);
            }
            return bitmap;
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static int getInSampleSizeByScreen(Context context, float scale, int outputWidth, int outputHeight) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidth = ((float) displayMetrics.widthPixels) * scale;
        float screenHeight = ((float) displayMetrics.heightPixels) * scale;
        int widthSmapleSize = (int) (((float) outputWidth) / screenWidth);
        int heightSmapleSize = (int) (((float) outputHeight) / screenHeight);
        MCLogUtil.i("ImageUtil", "outputWidth = " + outputWidth + " screenWidth = " + screenWidth + " widthSmapleSize = " + widthSmapleSize);
        MCLogUtil.i("ImageUtil", "outputHeight = " + outputHeight + " screenHeight = " + screenHeight + " heightSmapleSize = " + heightSmapleSize);
        if (widthSmapleSize >= heightSmapleSize) {
            return widthSmapleSize;
        }
        return heightSmapleSize;
    }

    private static int getMinWidthOfBitmap(Context context, float scale, int maxWidth) {
        int minWidth;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = (int) (((float) displayMetrics.widthPixels) * scale);
        int height = (int) (((float) displayMetrics.heightPixels) * scale);
        if (height > width) {
            minWidth = width;
        } else {
            minWidth = height;
        }
        if (minWidth > maxWidth) {
            return maxWidth;
        }
        return minWidth;
    }

    private static Bitmap showMatrixBitmap(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        float widthPoint = ((float) width) / ((float) screenWidth);
        float heightPoint = ((float) height) / ((float) screenHeight);
        Matrix matrix = new Matrix();
        float scale;
        if (widthPoint > CustomConstant.RATIO_ONE_HEIGHT || heightPoint > CustomConstant.RATIO_ONE_HEIGHT) {
            if (widthPoint > CustomConstant.RATIO_ONE_HEIGHT && heightPoint < CustomConstant.RATIO_ONE_HEIGHT) {
                scale = ((float) screenWidth) / ((float) width);
                matrix.postScale(scale, scale);
            } else if (widthPoint < CustomConstant.RATIO_ONE_HEIGHT && heightPoint > CustomConstant.RATIO_ONE_HEIGHT) {
                scale = ((float) screenHeight) / ((float) width);
                matrix.postScale(scale, scale);
            } else if (widthPoint > CustomConstant.RATIO_ONE_HEIGHT && heightPoint > CustomConstant.RATIO_ONE_HEIGHT) {
                if (widthPoint >= heightPoint) {
                    scale = ((float) screenWidth) / ((float) width);
                    matrix.postScale(scale, scale);
                } else {
                    scale = ((float) screenHeight) / ((float) height);
                    matrix.postScale(scale, scale);
                }
            }
        } else if (widthPoint >= heightPoint) {
            scale = ((float) screenWidth) / ((float) width);
            matrix.postScale(scale, scale);
        } else if (widthPoint < heightPoint) {
            scale = ((float) screenHeight) / ((float) height);
            matrix.postScale(scale, scale);
        }
        Bitmap bitmapNew = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        if (bitmapNew != bitmap) {
            bitmap.recycle();
            bitmap = bitmapNew;
        }
        return bitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static int getBitmapByteCount(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return 0;
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static Bitmap trimBitmap(Bitmap bitmap, int x, int y, int width, int height) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
        if (resizedBitmap == bitmap) {
            return bitmap;
        }
        bitmap.recycle();
        return resizedBitmap;
    }

    public static Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        Drawable imageDrawable = new BitmapDrawable(image);
        Bitmap output = Bitmap.createBitmap(x, y, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF outerRect = new RectF(0.0f, 0.0f, (float) x, (float) y);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, 31);
        imageDrawable.draw(canvas);
        canvas.restore();
        return output;
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
        return extractThumbnail(source, width, height, 0);
    }

    public static Bitmap extractThumbnail(Bitmap source, int width, int height, int options) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = ((float) width) / ((float) source.getWidth());
        } else {
            scale = ((float) height) / ((float) source.getHeight());
        }
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return transform(matrix, source, width, height, options | 1);
    }

    private static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, int options) {
        Bitmap b2;
        boolean scaleUp = (options & 1) != 0;
        boolean recycle = (options & 2) != 0;
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (scaleUp || (deltaX >= 0 && deltaY >= 0)) {
            Bitmap b1;
            float bitmapWidthF = (float) source.getWidth();
            float bitmapHeightF = (float) source.getHeight();
            float scale;
            if (bitmapWidthF / bitmapHeightF > ((float) targetWidth) / ((float) targetHeight)) {
                scale = ((float) targetHeight) / bitmapHeightF;
                if (scale < CustomConstant.RATIO_ITEM || scale > CustomConstant.RATIO_ONE_HEIGHT) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            } else {
                scale = ((float) targetWidth) / bitmapWidthF;
                if (scale < CustomConstant.RATIO_ITEM || scale > CustomConstant.RATIO_ONE_HEIGHT) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            }
            if (scaler != null) {
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
            } else {
                b1 = source;
            }
            if (recycle && b1 != source) {
                source.recycle();
            }
            b2 = Bitmap.createBitmap(b1, Math.max(0, b1.getWidth() - targetWidth) / 2, Math.max(0, b1.getHeight() - targetHeight) / 2, targetWidth, targetHeight);
            if (b2 != b1 && (recycle || b1 != source)) {
                b1.recycle();
            }
        } else {
            b2 = Bitmap.createBitmap(targetWidth, targetHeight, Config.ARGB_8888);
            Canvas c = new Canvas(b2);
            int deltaXHalf = Math.max(0, deltaX / 2);
            int deltaYHalf = Math.max(0, deltaY / 2);
            Rect rect = new Rect(deltaXHalf, deltaYHalf, Math.min(targetWidth, source.getWidth()) + deltaXHalf, Math.min(targetHeight, source.getHeight()) + deltaYHalf);
            int dstX = (targetWidth - rect.width()) / 2;
            int dstY = (targetHeight - rect.height()) / 2;
            c.drawBitmap(source, rect, new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY), null);
            if (recycle) {
                source.recycle();
            }
        }
        return b2;
    }

    public static String compressBitmap(String sourcePath, int upperLimitSize, Context context) {
        String lastFolderAbsolutePath = "";
        try {
            String compressPath = MCLibIOUtil.getImageCachePath(context) + sourcePath.substring(sourcePath.lastIndexOf("/") + 1).replace(" ", "");
            Bitmap sourceBitmap = getBitmapFromMedia(context, sourcePath, upperLimitSize, upperLimitSize);
            boolean isCompressed = sourceBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(new File(compressPath)));
            if (!(sourceBitmap == null || sourceBitmap.isRecycled())) {
                sourceBitmap.recycle();
            }
            if (isCompressed) {
                return compressPath.substring(0, compressPath.lastIndexOf("/"));
            }
            return sourcePath.substring(0, compressPath.lastIndexOf("/"));
        } catch (Exception e) {
            e.printStackTrace();
            return lastFolderAbsolutePath;
        }
    }

    public CompressFormat getImageType(String imageType) {
        if (imageType.equalsIgnoreCase("png")) {
            return CompressFormat.PNG;
        }
        if (imageType.equalsIgnoreCase("jpg")) {
            return CompressFormat.JPEG;
        }
        return CompressFormat.PNG;
    }
}
