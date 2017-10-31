package com.mobcent.discuz.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.mobcent.lowest.base.service.impl.FileTransferServiceImpl;
import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DZFaceLoaderUtils {
    private static Map<String, DZFaceLoaderUtils> _instanceMap = new HashMap();
    private static DZFaceLoaderUtils asyncTaskLoadImage = null;
    public static ExecutorService originalThreadPool = Executors.newFixedThreadPool(8);
    private final String TAG = "AsyncTaskLoaderFaceImage";
    public LruCache<String, Bitmap> _cache;
    private HashMap<String, List<BitmapImageCallback>> _callbacks;
    private final Object _lock = new Object();
    private Context context;
    private String imagePath = null;
    private HashMap<String, LoaderImageThread> loaderImageThreads;

    public interface BitmapImageCallback {
        void onImageLoaded(Bitmap bitmap, String str, int i, int i2);
    }

    private class LoaderImageThread extends Thread {
        private int endIndex = 0;
        private String hash;
        private boolean isInterrupted;
        private int maxWidth = 0;
        private float scale = 0.0f;
        private int startIndex = 0;
        private String url;

        public LoaderImageThread(String url, String hash, int startIndex, int endIndex) {
            this.url = url;
            this.hash = hash;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public boolean isInterrupted() {
            return this.isInterrupted;
        }

        public void run() {
            try {
                Bitmap d = DZFaceLoaderUtils.this.loadSync(this.url, this.hash, this, this.scale, this.maxWidth);
                DZFaceLoaderUtils.this.addBitmapToMemoryCache(this.hash, d);
                List<BitmapImageCallback> callbacks;
                synchronized (DZFaceLoaderUtils.this._lock) {
                    callbacks = (List) DZFaceLoaderUtils.this._callbacks.remove(this.hash);
                }
                for (BitmapImageCallback iter : callbacks) {
                    if (isInterrupted()) {
                        if (!(d == null || d.isRecycled())) {
                            d.recycle();
                        }
                        return;
                    }
                    iter.onImageLoaded(d, this.url, this.startIndex, this.endIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized DZFaceLoaderUtils getInstance(Context context, String tag) {
        DZFaceLoaderUtils dZFaceLoaderUtils;
        synchronized (DZFaceLoaderUtils.class) {
            if (_instanceMap == null) {
                _instanceMap = new HashMap();
            }
            if (_instanceMap.get(tag) == null) {
                _instanceMap.put(tag, new DZFaceLoaderUtils(context));
            }
            dZFaceLoaderUtils = (DZFaceLoaderUtils) _instanceMap.get(tag);
        }
        return dZFaceLoaderUtils;
    }

    public static void clearInstanceMap() {
        if (_instanceMap == null) {
            _instanceMap = new HashMap();
        } else {
            _instanceMap.clear();
        }
    }

    public static synchronized DZFaceLoaderUtils getInstance(Context context) {
        DZFaceLoaderUtils dZFaceLoaderUtils;
        synchronized (DZFaceLoaderUtils.class) {
            if (asyncTaskLoadImage == null) {
                asyncTaskLoadImage = new DZFaceLoaderUtils(context);
            }
            dZFaceLoaderUtils = asyncTaskLoadImage;
        }
        return dZFaceLoaderUtils;
    }

    private DZFaceLoaderUtils(Context context) {
        this._cache = new LruCache<String, Bitmap>(MCPhoneUtil.getCacheSize(context) / 4) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                MCLogUtil.e("AsyncTaskLoaderFaceImage", "oldValue" + oldValue);
            }
        };
        this.context = context;
        this._callbacks = new HashMap();
        this.loaderImageThreads = new HashMap();
        this.imagePath = MCLibIOUtil.getImageCachePath(context);
    }

    public static String getHash(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            url = new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return url;
    }

    public void addBitmapToMemoryCache(String hash, Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            this._cache.put(hash, bitmap);
        }
    }

    private Bitmap getbitmapFromCache(String url, String hash) {
        Bitmap b;
        synchronized (this._lock) {
            b = (Bitmap) this._cache.get(hash);
            if (b != null && b.isRecycled()) {
                this._cache.remove(hash);
                b = null;
            }
        }
        return b;
    }

    private Bitmap loadSync(String url, String hash, LoaderImageThread loaderImage, float scale, int maxWidth) {
        Bitmap b = null;
        try {
            File f;
            MCLogUtil.i("AsyncTaskLoaderFaceImage", "url=" + url);
            b = getbitmapFromCache(url, hash);
            if (this.imagePath == null) {
                f = new File(this.context.getCacheDir(), hash);
            } else {
                f = new File(this.imagePath + hash);
            }
            if (b == null) {
                if (f.exists()) {
                    b = getBitmap(f.getAbsolutePath(), scale, maxWidth);
                    if (b == null) {
                        f.delete();
                    }
                }
                if (!f.exists()) {
                    new FileTransferServiceImpl(this.context).downloadFile(url, f);
                }
                if (loaderImage.isInterrupted()) {
                    return null;
                }
                if (f.exists() && b == null) {
                    b = getBitmap(f.getAbsolutePath(), scale, maxWidth);
                    if (b == null) {
                        f.delete();
                    }
                }
                synchronized (this._lock) {
                    addBitmapToMemoryCache(hash, b);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadAsync(java.lang.String r9, int r10, int r11, com.mobcent.discuz.android.util.DZFaceLoaderUtils.BitmapImageCallback r12) {

        if(r9==null||"".equals(r9)){
            return;
        }
        String r3 = getHash(r9);
        Bitmap bitmap = this.getbitmapFromCache(r9,r3);
        if(bitmap!=null){
            if(bitmap.isRecycled()){
                synchronized (this._lock){
                    List r7 = (List)this._callbacks.get(r3);
                    if(r7!=null){
                        if(r12 != null){
                            r7.add(r12);
                        }
                        return;
                    }else{
                        r7 = new ArrayList();
                        if(r12 != null){
                            r7.add(r12);
                        }else{
                            this._callbacks.put(r3,r7);
                            LoaderImageThread loaderImageThread = new LoaderImageThread(r9,r3,r10,r11);
                            this.loaderImageThreads.put(r3,loaderImageThread);
                            originalThreadPool.execute(loaderImageThread);
                            return;
                        }
                    }
                }
            }else {
                r12.onImageLoaded(bitmap,r9,r10,r11);
                return;
            }
        }
        /*
        r8 = this;
        if (r9 == 0) goto L_0x000a;
    L_0x0002:
        r1 = "";
        r1 = r9.equals(r1);
        if (r1 == 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r3 = getHash(r9);
        r6 = r8.getbitmapFromCache(r9, r3);
        if (r6 == 0) goto L_0x001f;
    L_0x0015:
        r1 = r6.isRecycled();
        if (r1 != 0) goto L_0x001f;
    L_0x001b:
        r12.onImageLoaded(r6, r9, r10, r11);
        goto L_0x000a;
    L_0x001f:
        r2 = r8._lock;
        monitor-enter(r2);
        r1 = r8._callbacks;	 Catch:{ all -> 0x0033 }
        r7 = r1.get(r3);	 Catch:{ all -> 0x0033 }
        r7 = (java.util.List) r7;	 Catch:{ all -> 0x0033 }
        if (r7 == 0) goto L_0x0036;
    L_0x002c:
        if (r12 == 0) goto L_0x0031;
    L_0x002e:
        r7.add(r12);	 Catch:{ all -> 0x0033 }
    L_0x0031:
        monitor-exit(r2);	 Catch:{ all -> 0x0033 }
        goto L_0x000a;
    L_0x0033:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0033 }
        throw r1;
    L_0x0036:
        r7 = new java.util.ArrayList;	 Catch:{ all -> 0x0033 }
        r7.<init>();	 Catch:{ all -> 0x0033 }
        if (r12 == 0) goto L_0x0040;
    L_0x003d:
        r7.add(r12);	 Catch:{ all -> 0x0033 }
    L_0x0040:
        r1 = r8._callbacks;	 Catch:{ all -> 0x0033 }
        r1.put(r3, r7);	 Catch:{ all -> 0x0033 }
        monitor-exit(r2);	 Catch:{ all -> 0x0033 }
        r0 = new com.mobcent.discuz.android.util.DZFaceLoaderUtils$LoaderImageThread;
        r1 = r8;
        r2 = r9;
        r4 = r10;
        r5 = r11;
        r0.<init>(r2, r3, r4, r5);
        r1 = r8.loaderImageThreads;
        r1.put(r3, r0);
        r1 = originalThreadPool;
        r1.execute(r0);
        goto L_0x000a;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.util.DZFaceLoaderUtils.loadAsync(java.lang.String, int, int, com.mobcent.discuz.android.util.DZFaceLoaderUtils$BitmapImageCallback):void");
    }

    private Bitmap getBitmap(String fileName, float scale, int maxWidth) {
        if (maxWidth == 0 || scale == 0.0f) {
            return MCImageUtil.getBitmapFromMedia(this.context, fileName, 50, 50);
        }
        Bitmap bitmap = MCImageUtil.getBitmapFromMedia(this.context, fileName, 50, 50);
        Bitmap newBitmap = MCImageUtil.extractThumbnail(bitmap, maxWidth, (int) (((float) maxWidth) * scale), 0);
        bitmap.recycle();
        return newBitmap;
    }

    public String getImagePath(String url) {
        String hash = getHash(url);
        if (this.imagePath == null) {
            if (new File(this.context.getCacheDir() + hash).exists()) {
                return this.context.getCacheDir() + hash;
            }
            return null;
        } else if (new File(this.imagePath + hash).exists()) {
            return this.imagePath + hash;
        } else {
            return null;
        }
    }

    public void recycleBitmap(String urlStr) {
        String hash = getHash(urlStr);
        Bitmap bitmap = (Bitmap) this._cache.get(hash);
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                MCLogUtil.i("AsyncTaskLoaderFaceImage", "recycleBitmap");
            }
            this._cache.remove(hash);
        }
        this.loaderImageThreads.remove(hash);
        this._callbacks.remove(hash);
    }

    public void recycleBitmaps(List<String> urlStrs) {
        if (urlStrs != null && !urlStrs.isEmpty()) {
            for (String imageUrl : urlStrs) {
                recycleBitmap(imageUrl);
            }
        }
    }

    public static String formatUrl(String url, String resolution) {
        if (url == null) {
            return "";
        }
        if (resolution == null || !url.contains("xgsize")) {
            return url;
        }
        return url.replace("xgsize", resolution);
    }
}
