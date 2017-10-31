package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import com.mobcent.lowest.base.service.impl.FileTransferServiceImpl;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MCAsyncTaskLoaderImage {
    public static LruCache<String, Bitmap> _cache;
    private static final Object _lock = new Object();
    private static MCAsyncTaskLoaderImage asyncTaskLoadImage = null;
    public static ExecutorService originalThreadPool = Executors.newFixedThreadPool(4);
    private final String TAG = "AsyncTaskLoaderImage";
    private HashMap<String, List<BitmapImageCallback>> _callbacks;
    private Context context;
    private String imagePath = null;
    private HashMap<String, LoaderImageThread> loaderImageThreads;
    private int maxHeight;
    private int maxWidth;

    public interface BitmapImageCallback {
        void onImageLoaded(Bitmap bitmap, String str);
    }

    private class LoaderImageThread extends Thread {
        private String hash;
        private String url;
        private WeakReference<Bitmap> wb = null;
        private int width = 0;

        public void run() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            Bitmap bitmap = MCAsyncTaskLoaderImage.this.loadSync(this.url,this.hash,this.width);
            List r0;
            synchronized (MCAsyncTaskLoaderImage._lock){
                r0 = (List)MCAsyncTaskLoaderImage.this._callbacks.remove(this.hash);
            }
            if(r0==null||r0.isEmpty()){
                LoaderImageThread r4 = MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash);
                if(r4!=null){
                    MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                }
                return;
            }else{//006c
                this.wb = new WeakReference<Bitmap>(bitmap);
                Bitmap r4 = this.wb.get();
                if(r4!=null&&!r4.isRecycled()){

                    Iterator it = r0.iterator();
                    while(it.hasNext()){
                        BitmapImageCallback r3 = (BitmapImageCallback)it.next();
                        r3.onImageLoaded(r4,this.url);
                    }
                }else{//00df
                    Iterator it = r0.iterator();
                    while (it.hasNext()){
                        BitmapImageCallback r3 = (BitmapImageCallback)it.next();
                        r3.onImageLoaded(null,this.url);
                    }
                }
                r0.clear();
                if(MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash)!=null){
                    MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                }

            }
            /*
            r8 = this;
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;	 Catch:{ Exception -> 0x0045 }
            r5 = r8.url;	 Catch:{ Exception -> 0x0045 }
            r6 = r8.hash;	 Catch:{ Exception -> 0x0045 }
            r7 = r8.width;	 Catch:{ Exception -> 0x0045 }
            r1 = r4.loadSync(r5, r6, r7);	 Catch:{ Exception -> 0x0045 }
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage._lock;	 Catch:{ Exception -> 0x0045 }
            monitor-enter(r5);	 Catch:{ Exception -> 0x0045 }
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;	 Catch:{ Exception -> 0x0045 }
            r4 = r4._callbacks;	 Catch:{ Exception -> 0x0045 }
            r6 = r8.hash;	 Catch:{ Exception -> 0x0045 }
            r0 = r4.remove(r6);	 Catch:{ Exception -> 0x0045 }
            r0 = (java.util.List) r0;	 Catch:{ Exception -> 0x0045 }
            monitor-exit(r5);	 Catch:{ Exception -> 0x0045 }
            if (r0 == 0) goto L_0x0028;
        L_0x0022:
            r4 = r0.isEmpty();	 Catch:{ Exception -> 0x0045 }
            if (r4 == 0) goto L_0x006c;
        L_0x0028:
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r4 = r4.loaderImageThreads;
            r5 = r8.hash;
            r4 = r4.get(r5);
            if (r4 == 0) goto L_0x0041;
        L_0x0036:
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r4 = r4.loaderImageThreads;
            r5 = r8.hash;
            r4.remove(r5);
        L_0x0041:
            return;
        L_0x0042:
            r4 = move-exception;
            monitor-exit(r5);	 Catch:{ Exception -> 0x0045 }
            throw r4;	 Catch:{ Exception -> 0x0045 }
        L_0x0045:
            r2 = move-exception;
            r2.printStackTrace();	 Catch:{ all -> 0x00c4 }
            r4 = "AsyncTaskLoaderImage";	 Catch:{ all -> 0x00c4 }
            r5 = com.mobcent.lowest.base.utils.MCErrorLogUtil.getErrorInfo(r2);	 Catch:{ all -> 0x00c4 }
            com.mobcent.lowest.base.utils.MCLogUtil.e(r4, r5);	 Catch:{ all -> 0x00c4 }
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r4 = r4.loaderImageThreads;
            r5 = r8.hash;
            r4 = r4.get(r5);
            if (r4 == 0) goto L_0x0041;
        L_0x0060:
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r4 = r4.loaderImageThreads;
            r5 = r8.hash;
            r4.remove(r5);
            goto L_0x0041;
        L_0x006c:
            r4 = new java.lang.ref.WeakReference;	 Catch:{ Exception -> 0x0045 }
            r4.<init>(r1);	 Catch:{ Exception -> 0x0045 }
            r8.wb = r4;	 Catch:{ Exception -> 0x0045 }
            r4 = r8.wb;	 Catch:{ Exception -> 0x0045 }
            r4 = r4.get();	 Catch:{ Exception -> 0x0045 }
            if (r4 == 0) goto L_0x00df;	 Catch:{ Exception -> 0x0045 }
        L_0x007b:
            r4 = r8.wb;	 Catch:{ Exception -> 0x0045 }
            r4 = r4.get();	 Catch:{ Exception -> 0x0045 }
            r4 = (android.graphics.Bitmap) r4;	 Catch:{ Exception -> 0x0045 }
            r4 = r4.isRecycled();	 Catch:{ Exception -> 0x0045 }
            if (r4 != 0) goto L_0x00df;	 Catch:{ Exception -> 0x0045 }
        L_0x0089:
            r5 = r0.iterator();	 Catch:{ Exception -> 0x0045 }
        L_0x008d:
            r4 = r5.hasNext();	 Catch:{ Exception -> 0x0045 }
            if (r4 != 0) goto L_0x00b0;	 Catch:{ Exception -> 0x0045 }
        L_0x0093:
            r0.clear();	 Catch:{ Exception -> 0x0045 }
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r4 = r4.loaderImageThreads;
            r5 = r8.hash;
            r4 = r4.get(r5);
            if (r4 == 0) goto L_0x0041;
        L_0x00a4:
            r4 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r4 = r4.loaderImageThreads;
            r5 = r8.hash;
            r4.remove(r5);
            goto L_0x0041;
        L_0x00b0:
            r3 = r5.next();	 Catch:{ Exception -> 0x0045 }
            r3 = (com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.BitmapImageCallback) r3;	 Catch:{ Exception -> 0x0045 }
            r4 = r8.wb;	 Catch:{ Exception -> 0x0045 }
            r4 = r4.get();	 Catch:{ Exception -> 0x0045 }
            r4 = (android.graphics.Bitmap) r4;	 Catch:{ Exception -> 0x0045 }
            r6 = r8.url;	 Catch:{ Exception -> 0x0045 }
            r3.onImageLoaded(r4, r6);	 Catch:{ Exception -> 0x0045 }
            goto L_0x008d;
        L_0x00c4:
            r4 = move-exception;
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5 = r5.get(r6);
            if (r5 == 0) goto L_0x00de;
        L_0x00d3:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5.remove(r6);
        L_0x00de:
            throw r4;
        L_0x00df:
            r4 = r0.iterator();	 Catch:{ Exception -> 0x0045 }
        L_0x00e3:
            r5 = r4.hasNext();	 Catch:{ Exception -> 0x0045 }
            if (r5 == 0) goto L_0x0093;	 Catch:{ Exception -> 0x0045 }
        L_0x00e9:
            r3 = r4.next();	 Catch:{ Exception -> 0x0045 }
            r3 = (com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.BitmapImageCallback) r3;	 Catch:{ Exception -> 0x0045 }
            r5 = 0;	 Catch:{ Exception -> 0x0045 }
            r6 = r8.url;	 Catch:{ Exception -> 0x0045 }
            r3.onImageLoaded(r5, r6);	 Catch:{ Exception -> 0x0045 }
            goto L_0x00e3;
            */
            //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.LoaderImageThread.run():void");
        }

        public LoaderImageThread(String url, String hash) {
            this.url = url;
            this.hash = hash;
        }

        public LoaderImageThread(String url, String hash, int width) {
            this.url = url;
            this.hash = hash;
            this.width = width;
        }
    }

    private class LoaderLocalImageThread extends LoaderImageThread {
        private String hash;
        private String url;
        private int width;

        public void run() {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            MCLogUtil.e("LoaderLocalImageThread","LoaderLocalImageThread");
            if(this.width!=0){
                WeakReference r4 = new WeakReference(MCAsyncTaskLoaderImage.this.getBitmapByPath(this.url,this.width));
                List r0;
                synchronized (MCAsyncTaskLoaderImage._lock){
                    r0 = (List)MCAsyncTaskLoaderImage.this._callbacks.remove(this.hash);
                }
                if(r0!=null){
                    if(r0.isEmpty()){
                        if(MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash)!=null){
                            MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                        }
                    }else{//0083
                        Iterator it = r0.iterator();
                        while (it.hasNext()){//00ba
                            BitmapImageCallback r3 = (BitmapImageCallback)it.next();
                            if(r3==null)
                                continue;
                            else{
                                r3.onImageLoaded((Bitmap)r4.get(),this.url);
                            }
                        }
                        if(r4.get()!=null){
                            MCAsyncTaskLoaderImage.this.addBitmapToMemoryCache(this.hash,(Bitmap)r4.get());
                        }
                        if(MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash)!=null){
                            MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                        }

                    }
                }else{//0036
                    if(MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash)!=null){
                        MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                    }
                }

            }else{//0050
                Bitmap bitmap = MCAsyncTaskLoaderImage.this.getBitmapByPath(this.url);
                WeakReference r4 = new WeakReference(bitmap);
                List r0;
                synchronized (MCAsyncTaskLoaderImage._lock){
                    r0 = (List)MCAsyncTaskLoaderImage.this._callbacks.remove(this.hash);
                }
                if(r0 != null&&!r0.isEmpty()){//0083
                    Iterator it = r0.iterator();
                    while (it.hasNext()){//00ba
                        BitmapImageCallback r3 = (BitmapImageCallback)it.next();
                        if(r3==null)
                            continue;
                        else{
                            r3.onImageLoaded((Bitmap)r4.get(),this.url);
                        }
                    }
                    if(r4.get()!=null){
                        MCAsyncTaskLoaderImage.this.addBitmapToMemoryCache(this.hash,(Bitmap)r4.get());
                    }
                    if(MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash)!=null){
                        MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                    }

                }else{//0036
                    if(MCAsyncTaskLoaderImage.this.loaderImageThreads.get(this.hash)!=null){
                        MCAsyncTaskLoaderImage.this.loaderImageThreads.remove(this.hash);
                    }
                }
            }


            /*
            r8 = this;
            r5 = "LoaderLocalImageThread";	 Catch:{ Exception -> 0x005c }
            r6 = "LoaderLocalImageThread";	 Catch:{ Exception -> 0x005c }
            com.mobcent.lowest.base.utils.MCLogUtil.e(r5, r6);	 Catch:{ Exception -> 0x005c }
            r5 = r8.width;	 Catch:{ Exception -> 0x005c }
            if (r5 == 0) goto L_0x0050;	 Catch:{ Exception -> 0x005c }
        L_0x000b:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;	 Catch:{ Exception -> 0x005c }
            r6 = r8.url;	 Catch:{ Exception -> 0x005c }
            r7 = r8.width;	 Catch:{ Exception -> 0x005c }
            r1 = r5.getBitmapByPath(r6, r7);	 Catch:{ Exception -> 0x005c }
        L_0x0015:
            r4 = new java.lang.ref.WeakReference;	 Catch:{ Exception -> 0x005c }
            r4.<init>(r1);	 Catch:{ Exception -> 0x005c }
            r6 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage._lock;	 Catch:{ Exception -> 0x005c }
            monitor-enter(r6);	 Catch:{ Exception -> 0x005c }
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;	 Catch:{ Exception -> 0x005c }
            r5 = r5._callbacks;	 Catch:{ Exception -> 0x005c }
            r7 = r8.hash;	 Catch:{ Exception -> 0x005c }
            r0 = r5.remove(r7);	 Catch:{ Exception -> 0x005c }
            r0 = (java.util.List) r0;	 Catch:{ Exception -> 0x005c }
            monitor-exit(r6);	 Catch:{ Exception -> 0x005c }
            if (r0 == 0) goto L_0x0036;
        L_0x0030:
            r5 = r0.isEmpty();	 Catch:{ Exception -> 0x005c }
            if (r5 == 0) goto L_0x0083;
        L_0x0036:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5 = r5.get(r6);
            if (r5 == 0) goto L_0x004f;
        L_0x0044:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5.remove(r6);
        L_0x004f:
            return;
        L_0x0050:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;	 Catch:{ Exception -> 0x005c }
            r6 = r8.url;	 Catch:{ Exception -> 0x005c }
            r1 = r5.getBitmapByPath(r6);	 Catch:{ Exception -> 0x005c }
            goto L_0x0015;
        L_0x0059:
            r5 = move-exception;
            monitor-exit(r6);	 Catch:{ Exception -> 0x005c }
            throw r5;	 Catch:{ Exception -> 0x005c }
        L_0x005c:
            r2 = move-exception;
            r2.printStackTrace();	 Catch:{ all -> 0x00ce }
            r5 = "AsyncTaskLoaderImage";	 Catch:{ all -> 0x00ce }
            r6 = com.mobcent.lowest.base.utils.MCErrorLogUtil.getErrorInfo(r2);	 Catch:{ all -> 0x00ce }
            com.mobcent.lowest.base.utils.MCLogUtil.e(r5, r6);	 Catch:{ all -> 0x00ce }
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5 = r5.get(r6);
            if (r5 == 0) goto L_0x004f;
        L_0x0077:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5.remove(r6);
            goto L_0x004f;
        L_0x0083:
            r6 = r0.iterator();	 Catch:{ Exception -> 0x005c }
        L_0x0087:
            r5 = r6.hasNext();	 Catch:{ Exception -> 0x005c }
            if (r5 != 0) goto L_0x00ba;	 Catch:{ Exception -> 0x005c }
        L_0x008d:
            r5 = r4.get();	 Catch:{ Exception -> 0x005c }
            if (r5 == 0) goto L_0x00a0;	 Catch:{ Exception -> 0x005c }
        L_0x0093:
            r6 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;	 Catch:{ Exception -> 0x005c }
            r7 = r8.hash;	 Catch:{ Exception -> 0x005c }
            r5 = r4.get();	 Catch:{ Exception -> 0x005c }
            r5 = (android.graphics.Bitmap) r5;	 Catch:{ Exception -> 0x005c }
            r6.addBitmapToMemoryCache(r7, r5);	 Catch:{ Exception -> 0x005c }
        L_0x00a0:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5 = r5.get(r6);
            if (r5 == 0) goto L_0x004f;
        L_0x00ae:
            r5 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r5 = r5.loaderImageThreads;
            r6 = r8.hash;
            r5.remove(r6);
            goto L_0x004f;
        L_0x00ba:
            r3 = r6.next();	 Catch:{ Exception -> 0x005c }
            r3 = (com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.BitmapImageCallback) r3;	 Catch:{ Exception -> 0x005c }
            if (r3 == 0) goto L_0x0087;	 Catch:{ Exception -> 0x005c }
        L_0x00c2:
            r5 = r4.get();	 Catch:{ Exception -> 0x005c }
            r5 = (android.graphics.Bitmap) r5;	 Catch:{ Exception -> 0x005c }
            r7 = r8.url;	 Catch:{ Exception -> 0x005c }
            r3.onImageLoaded(r5, r7);	 Catch:{ Exception -> 0x005c }
            goto L_0x0087;
        L_0x00ce:
            r5 = move-exception;
            r6 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r6 = r6.loaderImageThreads;
            r7 = r8.hash;
            r6 = r6.get(r7);
            if (r6 == 0) goto L_0x00e8;
        L_0x00dd:
            r6 = com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.this;
            r6 = r6.loaderImageThreads;
            r7 = r8.hash;
            r6.remove(r7);
        L_0x00e8:
            throw r5;
            */
//            throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage.LoaderLocalImageThread.run():void");
        }

        public LoaderLocalImageThread(String url, String hash, int width) {
            super(url, hash);
            this.url = url;
            this.hash = hash;
            this.width = width;
        }
    }

    public static MCAsyncTaskLoaderImage getInstance(Context context) {
        synchronized (_lock) {
            if (asyncTaskLoadImage == null) {
                asyncTaskLoadImage = new MCAsyncTaskLoaderImage(context.getApplicationContext());
            }
        }
        return asyncTaskLoadImage;
    }

    private MCAsyncTaskLoaderImage(Context context) {
        _cache = new LruCache<String, Bitmap>(MCPhoneUtil.getCacheSize(context)) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }

            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
        this.context = context;
        this._callbacks = new HashMap();
        this.loaderImageThreads = new HashMap();
        this.imagePath = MCLibIOUtil.getImageCachePath(context);
        this.maxWidth = MCPhoneUtil.getDisplayWidth(context);
        this.maxHeight = MCPhoneUtil.getDisplayHeight(context) * 2;
    }

    public static String getHash(String url) {
        return MCStringUtil.getMD5Str(url);
    }

    public void addBitmapToMemoryCache(String hash, Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            _cache.put(hash, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String hash) {
        Bitmap b;
        synchronized (_lock) {
            b = (Bitmap) _cache.get(hash);
            if (b != null && b.isRecycled()) {
                _cache.remove(hash);
                b = null;
            }
        }
        return b;
    }

    private Bitmap loadSync(String url, String hash, int maxWidth) {
        Exception ex;
        Bitmap bitmap = null;
        WeakReference<Bitmap> wb = null;
        try {
            bitmap = getBitmapFromCache(hash);
            if (bitmap == null) {
                File f;
                if (!url.startsWith("http")) {
                    f = new File(url);
                } else if (this.imagePath == null) {
                    f = new File(this.context.getCacheDir(), hash);
                } else {
                    f = new File(this.imagePath + hash);
                }
                if (f.exists()) {
                    if (maxWidth > 0) {
                        bitmap = getBitmapByPath(f.getAbsolutePath(), maxWidth);
                    } else {
                        bitmap = getBitmapByPath(f.getAbsolutePath());
                    }
                }
                if (bitmap == null) {
                    if (url.startsWith("http")) {
                        f.delete();
                        new FileTransferServiceImpl(this.context).downloadFile(url, f);
                    }
                    if (maxWidth > 0) {
                        bitmap = getBitmapByPath(f.getAbsolutePath(), maxWidth);
                    } else {
                        bitmap = getBitmapByPath(f.getAbsolutePath());
                    }
                }
                WeakReference<Bitmap> wb2 = new WeakReference(bitmap);
                if (bitmap != null) {
                    try {
                        synchronized (_lock) {
                            addBitmapToMemoryCache(hash, (Bitmap) wb2.get());
                        }
                        wb = wb2;
                    } catch (Exception e) {
                        ex = e;
                        wb = wb2;
                    }
                } else {
                    wb = wb2;
                }
            }
        } catch (Exception e2) {
            ex = e2;
            ex.printStackTrace();
            MCLogUtil.e("AsyncTaskLoaderImage", MCErrorLogUtil.getErrorInfo(ex));
            return wb != null ? (Bitmap) wb.get() : bitmap;
        }
        if (wb != null) {
        }
        return bitmap;
    }

    public void loadAsync1(String url, BitmapImageCallback callback) {
        loadAsync1(url, 0, callback);
    }

    public void loadAsync1(String url, int maxWidth, BitmapImageCallback callback) {
        if (url != null && !url.equals("")) {
            String hash = "";
            if (maxWidth > 0) {
                hash = getHash(new StringBuilder(String.valueOf(url)).append(maxWidth).toString());
            } else {
                hash = getHash(url);
            }
            Bitmap b = getBitmapFromCache(hash);
            if (b == null || b.isRecycled()) {
                synchronized (_lock) {
                    List<BitmapImageCallback> callbacks = (List) this._callbacks.get(hash);
                    if (callbacks == null) {
                        callbacks = new ArrayList();
                        this._callbacks.put(hash, callbacks);
                    }
                    if (callback != null) {
                        callbacks.add(callback);
                    }
                    LoaderImageThread thread = (LoaderImageThread) this.loaderImageThreads.get(hash);
                    if (thread == null || !thread.isAlive()) {
                        LoaderImageThread loaderImageThread = new LoaderImageThread(url, hash, maxWidth);
                        this.loaderImageThreads.put(hash, loaderImageThread);
                        originalThreadPool.execute(loaderImageThread);
                        return;
                    }
                    return;
                }
            }
            callback.onImageLoaded(b, url);
        } else if (callback != null) {
            callback.onImageLoaded(null, url);
        }
    }

    public void loadAsyncByLocal1(String path, int maxWidth, BitmapImageCallback callback) {
        if (path != null && !path.equals("")) {
            String hash = "";
            if (maxWidth != 0) {
                hash = getHash(new StringBuilder(String.valueOf(path)).append("thumbnail").toString());
            } else {
                hash = getHash(path);
            }
            Bitmap b = getBitmapFromCache(hash);
            if (b == null || b.isRecycled()) {
                synchronized (_lock) {
                    List<BitmapImageCallback> callbacks = (List) this._callbacks.get(hash);
                    if (callbacks == null) {
                        callbacks = new ArrayList();
                        this._callbacks.put(hash, callbacks);
                    }
                    if (callback != null) {
                        callbacks.add(callback);
                    }
                    LoaderImageThread thread = (LoaderImageThread) this.loaderImageThreads.get(hash);
                    if (thread == null || !thread.isAlive()) {
                        WeakReference<LoaderImageThread> wt = new WeakReference(new LoaderLocalImageThread(path, hash, maxWidth));
                        this.loaderImageThreads.put(hash, (LoaderImageThread) wt.get());
                        originalThreadPool.execute((Runnable) wt.get());
                        return;
                    }
                    return;
                }
            }
            callback.onImageLoaded(b, path);
        } else if (callback != null) {
            callback.onImageLoaded(null, path);
        }
    }

    private Bitmap getBitmapByPath(String filePath) {
        return MCImageUtil.getBitmapFromMedia(this.context, filePath, this.maxWidth, this.maxHeight);
    }

    private Bitmap getBitmapByPath(String filePath, int maxWidth) {
        return MCImageUtil.getBitmapFromMedia(this.context, filePath, maxWidth, maxWidth * 2);
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

    public static String formatUrl(String url, String resolution) {
        if (url == null) {
            return "";
        }
        if (resolution == null || !url.contains("xgsize")) {
            return url;
        }
        return url.replace("xgsize", resolution);
    }

    public void addCache(String url, Bitmap bitmap) {
        String hash = getHash(url);
        if (_cache != null) {
            _cache.put(hash, bitmap);
        }
    }

    public Bitmap getCache(String url) {
        Bitmap bitmap;
        String hash = getHash(url);
        synchronized (_lock) {
            bitmap = (Bitmap) _cache.get(hash);
        }
        return bitmap;
    }

    public void deleteBimap(String urlStr) {
        synchronized (_lock) {
            File f;
            String hash = getHash(urlStr);
            _cache.remove(hash);
            this.loaderImageThreads.remove(hash);
            this._callbacks.remove(hash);
            if (this.imagePath == null) {
                f = new File(this.context.getCacheDir(), hash);
            } else {
                f = new File(this.imagePath + hash);
            }
            if (f.exists()) {
                f.delete();
            }
        }
    }

    public void replaceIcon(String urlStr, Bitmap bitmap, final String newIconPath) {
        synchronized (_lock) {
            String sourcePath;
            String hash = getHash(urlStr);
            _cache.remove(hash);
            if (!(bitmap == null || bitmap.isRecycled())) {
                _cache.put(hash, bitmap);
            }
            if (this.imagePath == null) {
                sourcePath = this.context.getCacheDir() + hash;
            } else {
                sourcePath = this.imagePath + hash;
            }

            final String fnSourcePath  = sourcePath;
            if (!MCStringUtil.isEmpty(newIconPath) && new File(newIconPath).exists()) {
                new Thread() {
                    public void run() {
                        MCLogUtil.e("AsyncTaskLoaderImage", "newIconPath==" + newIconPath + "====sourcePath=======" + fnSourcePath);
                        new FileTransferServiceImpl(MCAsyncTaskLoaderImage.this.context).copyFile(newIconPath, fnSourcePath);
                    }
                }.start();
            }
        }
    }
}
