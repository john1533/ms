package com.mobcent.lowest.android.ui.widget.gif;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.lowest.base.service.impl.FileTransferServiceImpl;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class GifCache extends AsyncTask<Void, Void, InputStream> {
    public static HashMap<String, InputStream> _isCache;
    private static Context context;
    private static String imagePath = null;
    private GifCallback gifCallback;
    private String url;

    public interface GifCallback {
        void onGifLoaded(InputStream inputStream, String str);
    }

    public GifCache(Context context, String url, GifCallback gifCallback) {
        _isCache = new HashMap();
        this.gifCallback = gifCallback;
        context = context;
        this.url = url;
        imagePath = createFileDir();
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

    private InputStream inputStreamFromCache(String hash) {
        if (_isCache.containsKey(hash)) {
            return (InputStream) _isCache.get(hash);
        }
        return null;
    }

    private String createFileDir() {
        String imagePath = MCLibIOUtil.getImageCachePathByApp(context);
        if (MCLibIOUtil.isDirExist(imagePath) || MCLibIOUtil.makeDirs(imagePath)) {
            return imagePath;
        }
        return null;
    }

    private InputStream getInputStream(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected InputStream doInBackground(Void... params) {
        File f;
        String hash = getHash(this.url);
        InputStream is = inputStreamFromCache(hash);
        if (imagePath == null) {
            f = new File(context.getCacheDir(), hash);
        } else {
            f = new File(imagePath + hash);
        }
        if (is == null) {
            if (!f.exists()) {
                new FileTransferServiceImpl(context).downloadFile(this.url, f);
            }
            if (f.exists()) {
                is = getInputStream(f.getAbsolutePath());
                if (is == null) {
                    f.delete();
                }
            }
            if (is != null) {
                _isCache.put(hash, is);
            }
        }
        return is;
    }

    public static void removeInputStream(InputStream is) {
        if (_isCache != null && _isCache.containsValue(is)) {
            for (String hash : _isCache.keySet()) {
                if (is.equals((InputStream) _isCache.get(hash))) {
                    File f;
                    if (imagePath == null) {
                        f = new File(context.getCacheDir(), hash);
                    } else {
                        f = new File(imagePath + hash);
                    }
                    if (f != null) {
                        _isCache.put(hash, null);
                        f.delete();
                    }
                }
            }
        }
    }

    protected void onPostExecute(InputStream is) {
        this.gifCallback.onGifLoaded(is, this.url);
    }
}
