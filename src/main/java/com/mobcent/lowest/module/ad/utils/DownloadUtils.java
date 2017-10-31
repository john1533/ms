package com.mobcent.lowest.module.ad.utils;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCHttpClientUtil;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUtils {
    private static final String AD_DIR = "ad";
    private static String TAG = "DownloadUtils";

    public interface DownProgressDelegate {
        void setMax(int i);

        void setProgress(int i);
    }

    public static String getDownloadDirPath(Context context) {
        return MCLibIOUtil.createDirInCache(context, AD_DIR);
    }

    public static boolean downloadFile(Context context, String url, File f, DownProgressDelegate delegate) {
        Exception e;
        URL url2;
        MalformedURLException e2;
        Throwable th;
        try {
            URL fileUrl = new URL(url);
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            RandomAccessFile raf = null;
            try {
                f.getParentFile().mkdirs();
                f.createNewFile();
                int currentLength = (int) f.length();
                httpURLConnection = MCHttpClientUtil.getNewHttpURLConnection(fileUrl, context);
                int length = httpURLConnection.getContentLength();
                if (currentLength == length) {
                    if (raf != null) {
                        try {
                            raf.close();
                        } catch (Exception e3) {
                            MCLogUtil.e(TAG, e3.toString());
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    url2 = fileUrl;
                    return true;
                }
                httpURLConnection = MCHttpClientUtil.getNewHttpURLConnection(fileUrl, context);
                httpURLConnection.addRequestProperty("Range", "bytes=" + currentLength + "-" + length);
                delegate.setMax(length);
                delegate.setProgress(currentLength);
                inputStream = httpURLConnection.getInputStream();
                RandomAccessFile raf2 = new RandomAccessFile(f, "rwd");
                try {
                    raf2.seek((long) currentLength);
                    byte[] buffer = new byte[102400];
                    while (true) {
                        int size = inputStream.read(buffer);
                        if (size <= 0) {
                            break;
                        }
                        raf2.write(buffer, 0, size);
                        currentLength += size;
                        delegate.setProgress(currentLength);
                    }
                    if (raf2 != null) {
                        try {
                            raf2.close();
                        } catch (Exception e4) {
//                            e3 = e4;
                            raf = raf2;
//                            MCLogUtil.e(TAG, e3.toString());
                            url2 = fileUrl;
                            return true;
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e5) {
//                            e3 = e5;
//                            MCLogUtil.e(TAG, e3.toString());
                            url2 = fileUrl;
                            return true;
                        }
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    url2 = fileUrl;
                    return true;
                } catch (MalformedURLException e6) {
                    e2 = e6;
                    raf = raf2;
                } catch (Exception e7) {
//                    e3 = e7;
                    raf = raf2;
                } catch (Throwable th2) {
                    th = th2;
                    raf = raf2;
                }
            } catch (MalformedURLException e8) {
                e2 = e8;
                try {
                    MCLogUtil.e(TAG, e2.toString());
                    if (raf != null) {
                        try {
                            raf.close();
                        } catch (Exception e32) {
                            MCLogUtil.e(TAG, e32.toString());
                            url2 = fileUrl;
                            return false;
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    url2 = fileUrl;
                    return false;
                } catch (Throwable th3) {
                    th = th3;
                    if (raf != null) {
                        try {
                            raf.close();
                        } catch (Exception e322) {
                            MCLogUtil.e(TAG, e322.toString());
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            } catch (Exception e9) {
//                e322 = e9;
//                MCLogUtil.e(TAG, e322.toString());
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (Exception e3222) {
                        MCLogUtil.e(TAG, e3222.toString());
                        url2 = fileUrl;
                        return false;
                    }
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                url2 = fileUrl;
                return false;
            }
        } catch (Exception e10) {
            return false;
        }

        return false;
    }
}
