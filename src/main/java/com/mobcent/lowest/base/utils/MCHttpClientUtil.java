package com.mobcent.lowest.base.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import com.mobcent.android.constant.MCShareApiConstant;
import com.mobcent.lowest.module.ad.utils.DownloadUtils.DownProgressDelegate;
import com.tencent.connect.common.Constants;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class MCHttpClientUtil {
    private static final int SET_CONNECTION_TIMEOUT = 30000;
    public static final String SET_CONNECTION_TIMEOUT_STR = "connection_timeout";
    private static final int SET_SOCKET_TIMEOUT = 100000;
    public static final String SET_SOCKET_TIMEOUT_STR = "socket_timeout";
    private static final String TAG = "HttpClientUtil";

    public static java.net.HttpURLConnection getNewHttpURLConnection(java.net.URL r15, android.content.Context r16) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/

        try {
            HttpURLConnection connection = (HttpURLConnection) r15.openConnection();
            if (!((WifiManager) r16.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
                Cursor mCursor = r16.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
                if (mCursor != null && mCursor.moveToFirst()) {
                    String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
                    if (proxyStr != null && proxyStr.trim().length() > 0) {
                        connection = (HttpURLConnection) r15.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyStr, 80)));
                    }
                    mCursor.close();
                }
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return (HttpURLConnection) r15.openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }

        /*
        r14 = 0;
        r7 = 0;
        r10 = 0;
        r1 = r15.openConnection();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r0 = r1;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r7 = r0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = "wifi";	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r0 = r16;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r13 = r0.getSystemService(r1);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r13 = (android.net.wifi.WifiManager) r13;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = r13.isWifiEnabled();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        if (r1 != 0) goto L_0x0061;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
    L_0x001b:
        r1 = "content://telephony/carriers/preferapn";	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r2 = android.net.Uri.parse(r1);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = r16.getContentResolver();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r3 = 0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r4 = 0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r5 = 0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r6 = 0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r10 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        if (r10 == 0) goto L_0x0061;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
    L_0x002f:
        r1 = r10.moveToFirst();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        if (r1 == 0) goto L_0x0061;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
    L_0x0035:
        r1 = "proxy";	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = r10.getColumnIndex(r1);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r12 = r10.getString(r1);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        if (r12 == 0) goto L_0x0061;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
    L_0x0041:
        r1 = r12.trim();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = r1.length();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        if (r1 <= 0) goto L_0x0061;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
    L_0x004b:
        r11 = new java.net.Proxy;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = java.net.Proxy.Type.HTTP;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r3 = new java.net.InetSocketAddress;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r4 = 80;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r3.<init>(r12, r4);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r11.<init>(r1, r3);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = r15.openConnection(r11);	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r0 = r1;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r7 = r0;	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
    L_0x0061:
        if (r10 == 0) goto L_0x0066;
    L_0x0063:
        r10.close();
    L_0x0066:
        r1 = r7;
    L_0x0067:
        return r1;
    L_0x0068:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        r1 = r15.openConnection();	 Catch:{ IOException -> 0x0078 }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ IOException -> 0x0078 }
        if (r10 == 0) goto L_0x0067;
    L_0x0074:
        r10.close();
        goto L_0x0067;
    L_0x0078:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ Exception -> 0x0068, all -> 0x0083 }
        if (r10 == 0) goto L_0x0081;
    L_0x007e:
        r10.close();
    L_0x0081:
        r1 = r14;
        goto L_0x0067;
    L_0x0083:
        r1 = move-exception;
        if (r10 == 0) goto L_0x0089;
    L_0x0086:
        r10.close();
    L_0x0089:
        throw r1;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.base.utils.MCHttpClientUtil.getNewHttpURLConnection(java.net.URL, android.content.Context):java.net.HttpURLConnection");
    }

    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        String result = "";
        String tempURL = new StringBuilder(String.valueOf(urlString)).append("?").toString();
        int connectionTimeout = -1;
        int socketTimeout = -1;
        if (!(params == null || params.isEmpty())) {
            if (params.get("connection_timeout") != null) {
                connectionTimeout = Integer.parseInt((String) params.get("connection_timeout"));
            }
            if (params.get("socket_timeout") != null) {
                socketTimeout = Integer.parseInt((String) params.get("socket_timeout"));
            }
        }
        HttpClient client = getNewHttpClient(context, connectionTimeout, socketTimeout);
        HttpPost httpPost = new HttpPost(urlString);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList();
            if (!(params == null || params.isEmpty())) {
                for (String key : params.keySet()) {
                    nameValuePairs.add(new BasicNameValuePair(key, (String) params.get(key)));
                    tempURL = new StringBuilder(String.valueOf(tempURL)).append(key).append("=").append((String) params.get(key)).append("&").toString();
                }
            }
            MCLogUtil.i(TAG, "tempURL = " + tempURL);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            MCLogUtil.i(TAG, "statusCode=" + statusCode);
            if (statusCode != 200) {
                return "connection_fail";
            }
            result = read(response);
            MCLogUtil.i(TAG, "result = " + result);
            if (result == null) {
                return "{}";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "connection_fail";
        }
    }

    public static synchronized byte[] getFileInByte(String url, Context context) {
        URL myFileUrl;
        URL url2;
        Throwable th;
        byte[] bArr = null;
        synchronized (MCHttpClientUtil.class) {
            byte[] imgData = null;
            try {
                myFileUrl = new URL(url);
                HttpURLConnection connection = null;
                InputStream is = null;
                try {
                    connection = getNewHttpURLConnection(myFileUrl, context);
                    connection.setDoInput(true);
                    connection.connect();
                    is = connection.getInputStream();
                    int length = connection.getContentLength();
                    if (length != -1) {
                        imgData = new byte[length];
                        byte[] temp = new byte[512];
                        int destPos = 0;
                        while (true) {
                            int readLen = is.read(temp);
                            if (readLen <= 0) {
                                break;
                            }
                            System.arraycopy(temp, 0, imgData, destPos, readLen);
                            destPos += readLen;
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                    url2 = myFileUrl;
                    bArr = imgData;
                } catch (IOException e2) {
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    url2 = myFileUrl;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
//        url2 = myFileUrl;
//        return bArr;
        return bArr;
    }

    public static void downloadFile(String url, File f, Context context) {
        IOException e;
        Throwable th;
        try {
            URL url2;
            MCLogUtil.i(TAG, "Image url is " + url);
            if (url.toLowerCase().contains(".png") || url.toLowerCase().contains(".gif") || url.toLowerCase().contains(".jpg") || url.toLowerCase().contains(".bmp") || url.toLowerCase().contains(".jpeg")) {
                if (Uri.decode(url).equals(url)) {
                    url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/").replace("%3F", "?").replace("%3D", "=").replace("%26", "&");
                }
                MCLogUtil.e(TAG, "url=" + url);
            }
            URL myFileUrl = new URL(url);
            HttpURLConnection connection = null;
            InputStream is = null;
            FileOutputStream fo = null;
            try {
                connection = getNewHttpURLConnection(myFileUrl, context);
                connection.setDoInput(true);
                connection.connect();
                is = connection.getInputStream();
                if (f.createNewFile()) {
                    FileOutputStream fo2 = new FileOutputStream(f);
                    try {
                        byte[] buffer = new byte[256];
                        while (true) {
                            int size = is.read(buffer);
                            if (size <= 0) {
                                break;
                            }
                            fo2.write(buffer, 0, size);
                        }
                        fo = fo2;
                    } catch (MalformedURLException e2) {
                        fo = fo2;
                    } catch (IOException e3) {
                        e = e3;
                        fo = fo2;
                    } catch (Throwable th2) {
                        th = th2;
                        fo = fo2;
                    }
                }
                if (fo != null) {
                    try {
                        fo.flush();
                        fo.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
                if (is != null) {
                    is.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (MalformedURLException e5) {
                try {
                    MCLogUtil.i(TAG, "URL is format error");
                    if (fo != null) {
                        try {
                            fo.flush();
                            fo.close();
                        } catch (IOException e42) {
                            e42.printStackTrace();
                        }
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                    url2 = myFileUrl;
                } catch (Throwable th3) {
                    th = th3;
                    if (fo != null) {
                        try {
                            fo.flush();
                            fo.close();
                        } catch (IOException e422) {
                            e422.printStackTrace();
                        }
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } catch (IOException e6) {
                e6.printStackTrace();
                MCLogUtil.i(TAG, "IO error when download file");
                MCLogUtil.i(TAG, "The URL is " + url + ";the file name " + f.getName());
                if (fo != null) {
                    try {
                        fo.flush();
                        fo.close();
                    } catch (IOException e4222) {
                        e4222.printStackTrace();
                    }
                }
                if (is != null) {
                    is.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
                url2 = myFileUrl;
            }
            url2 = myFileUrl;
        } catch (Exception e7) {
        }
    }

    public static void downloadFile(String url, File f, DownProgressDelegate downloadProgressDelegate, Context context) {
        Throwable th;
        try {
            URL url2;
            MCLogUtil.i(TAG, "Image url is " + url);
            if (url.toLowerCase().contains(".png") || url.toLowerCase().contains(".gif") || url.toLowerCase().contains(".jpg") || url.toLowerCase().contains(".bmp") || url.toLowerCase().contains(".jpeg")) {
                url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/");
            }
            URL myFileUrl = new URL(url);
            HttpURLConnection connection = null;
            InputStream is = null;
            FileOutputStream fo = null;
            try {
                connection = getNewHttpURLConnection(myFileUrl, context);
                connection.setDoInput(true);
                connection.connect();
                is = connection.getInputStream();
                downloadProgressDelegate.setMax(connection.getContentLength());
                if (f.createNewFile()) {
                    FileOutputStream fo2 = new FileOutputStream(f);
                    try {
                        byte[] buffer = new byte[256];
                        int destPos = 0;
                        while (true) {
                            int size = is.read(buffer);
                            if (size <= 0) {
                                break;
                            }
                            fo2.write(buffer, 0, size);
                            destPos += size;
                            downloadProgressDelegate.setProgress(destPos);
                        }
                        fo = fo2;
                    } catch (MalformedURLException e) {
                        fo = fo2;
                    } catch (IOException e2) {
                        fo = fo2;
                    } catch (Throwable th2) {
                        th = th2;
                        fo = fo2;
                    }
                }
                if (fo != null) {
                    try {
                        fo.flush();
                        fo.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                if (is != null) {
                    is.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (MalformedURLException e4) {
                try {
                    MCLogUtil.i(TAG, "URL is format error");
                    if (fo != null) {
                        try {
                            fo.flush();
                            fo.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                    url2 = myFileUrl;
                } catch (Throwable th3) {
                    th = th3;
                    if (fo != null) {
                        try {
                            fo.flush();
                            fo.close();
                        } catch (IOException e322) {
                            e322.printStackTrace();
                        }
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } catch (IOException e5) {
                MCLogUtil.i(TAG, "IO error when download file");
                MCLogUtil.i(TAG, "The URL is " + url + ";the file name " + f.getName());
                if (fo != null) {
                    try {
                        fo.flush();
                        fo.close();
                    } catch (IOException e3222) {
                        e3222.printStackTrace();
                    }
                }
                if (is != null) {
                    is.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
                url2 = myFileUrl;
            }
            url2 = myFileUrl;
        } catch (Exception e6) {
        }
    }

//    public static synchronized byte[] getFileInByteByProgress(String url, Context context, DownProgressDelegate downProgress) {
//        Throwable th;
//        byte[] bArr = null;
//        synchronized (MCHttpClientUtil.class) {
//            URL myFileUrl;
//            URL url2;
//            byte[] imgData = null;
//            try {
//                myFileUrl = new URL(url);
//                HttpURLConnection connection = null;
//                InputStream is = null;
//                try {
//                    connection = getNewHttpURLConnection(myFileUrl, context);
//                    connection.setDoInput(true);
//                    connection.connect();
//                    is = connection.getInputStream();
//                    int length = connection.getContentLength();
//                    downProgress.setMax(length);
//                    if (length != -1) {
//                        imgData = new byte[length];
//                        byte[] temp = new byte[512];
//                        int destPos = 0;
//                        while (true) {
//                            int readLen = is.read(temp);
//                            if (readLen <= 0) {
//                                break;
//                            }
//                            System.arraycopy(temp, 0, imgData, destPos, readLen);
//                            destPos += readLen;
//                            downProgress.setProgress(destPos);
//                        }
//                    }
//                    if (is != null) {
//                        try {
//                            is.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                    url2 = myFileUrl;
//                    bArr = imgData;
//                } catch (IOException e2) {
//                    if (is != null) {
//                        is.close();
//                    }
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }  catch (Throwable th2) {
//                    th = th2;
//                    url2 = myFileUrl;
//                    throw th;
//                }
//            } catch (Throwable th3) {
//                th = th3;
//            }
//        }
//        return bArr;
//    }

//    public static String uploadFile(String actionUrl, String uploadFile, Context context, String parameter) {
//        IOException e;
//        ClientProtocolException e2;
//        Throwable th;
//        HttpURLConnection con = null;
//        InputStream is = null;
//        DataOutputStream ds = null;
//        FileInputStream fStream = null;
//        actionUrl = new StringBuilder(String.valueOf(actionUrl)).append("?").append(parameter).toString();
//        MCLogUtil.i(TAG, "uploadUrl = " + actionUrl + ";uploadFile=" + uploadFile);
//        if (MCPhoneConnectionUtil.isNetworkAvailable(context)) {
//            String end = "\r\n";
//            String twoHyphens = "--";
//            String boundary = "*****";
//            String fileName = "";
//            if (uploadFile != null && uploadFile.lastIndexOf(MCLibIOUtil.FS) > -1) {
//                fileName = uploadFile.substring(uploadFile.lastIndexOf(MCLibIOUtil.FS) + 1);
//            }
//            try {
//                con = getNewHttpURLConnection(new URL(actionUrl), context);
//                con.setDoInput(true);
//                con.setDoOutput(true);
//                con.setUseCaches(false);
//                con.setConnectTimeout(180000);
//                con.setReadTimeout(180000);
//                con.setRequestMethod(Constants.HTTP_POST);
//                con.setRequestProperty("Connection", "Keep-Alive");
//                con.setRequestProperty("Charset", "UTF-8");
//                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                DataOutputStream ds2 = new DataOutputStream(con.getOutputStream());
//                try {
//                    ds2.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(end).toString());
//                    ds2.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\"" + fileName + "\"" + end);
//                    ds2.writeBytes(end);
//                    FileInputStream fStream2 = new FileInputStream(uploadFile);
//                    try {
//                        byte[] buffer = new byte[1024];
//                        while (true) {
//                            int length = fStream2.read(buffer);
//                            if (length == -1) {
//                                break;
//                            }
//                            ds2.write(buffer, 0, length);
//                        }
//                        ds2.writeBytes(end);
//                        ds2.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(end).toString());
//                        is = con.getInputStream();
//                        String jsonStr = MCLibIOUtil.convertStreamToString(is);
//                        MCLogUtil.i(TAG, "jsonStr=" + jsonStr);
//                        if (is != null) {
//                            try {
//                                is.close();
//                            } catch (IOException e3) {
//                                e3.printStackTrace();
//                            }
//                        }
//                        if (fStream2 != null) {
//                            fStream2.close();
//                        }
//                        if (ds2 != null) {
//                            ds2.flush();
//                            ds2.close();
//                        }
//                        if (con != null) {
//                            con.disconnect();
//                        }
//                        fStream = fStream2;
//                        ds = ds2;
//                        return jsonStr;
//                    } catch (ClientProtocolException e4) {
//                        e2 = e4;
//                        fStream = fStream2;
//                        ds = ds2;
//                    } catch (SocketTimeoutException e5) {
//                        fStream = fStream2;
//                        ds = ds2;
//                    } catch (IOException e6) {
//                        e3 = e6;
//                        fStream = fStream2;
//                        ds = ds2;
//                    } catch (Exception e7) {
//                        fStream = fStream2;
//                        ds = ds2;
//                    } catch (Throwable th2) {
//                        th = th2;
//                        fStream = fStream2;
//                        ds = ds2;
//                    }
//                } catch (ClientProtocolException e8) {
//                    e2 = e8;
//                    ds = ds2;
//                    try {
//                        e2.printStackTrace();
//                        if (is != null) {
//                            try {
//                                is.close();
//                            } catch (IOException e32) {
//                                e32.printStackTrace();
//                                return "connection_fail";
//                            }
//                        }
//                        if (fStream != null) {
//                            fStream.close();
//                        }
//                        if (ds != null) {
//                            ds.flush();
//                            ds.close();
//                        }
//                        if (con != null) {
//                            con.disconnect();
//                        }
//                        return "connection_fail";
//                    } catch (Throwable th3) {
//                        th = th3;
//                        if (is != null) {
//                            try {
//                                is.close();
//                            } catch (IOException e322) {
//                                e322.printStackTrace();
//                                throw th;
//                            }
//                        }
//                        if (fStream != null) {
//                            fStream.close();
//                        }
//                        if (ds != null) {
//                            ds.flush();
//                            ds.close();
//                        }
//                        if (con != null) {
//                            con.disconnect();
//                        }
//                        throw th;
//                    }
//                } catch (SocketTimeoutException e9) {
//                    ds = ds2;
//                    MCLogUtil.i(TAG, "SocketTimeoutException");
//                    if (is != null) {
//                        try {
//                            is.close();
//                        } catch (IOException e3222) {
//                            e3222.printStackTrace();
//                            return "connection_fail";
//                        }
//                    }
//                    if (fStream != null) {
//                        fStream.close();
//                    }
//                    if (ds != null) {
//                        ds.flush();
//                        ds.close();
//                    }
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                    return "connection_fail";
//                } catch (IOException e10) {
//                    ds = ds2;
//                    e10.printStackTrace();
//                    if (is != null) {
//                        try {
//                            is.close();
//                        } catch (IOException e32222) {
//                            e32222.printStackTrace();
//                            return "connection_fail";
//                        }
//                    }
//                    if (fStream != null) {
//                        fStream.close();
//                    }
//                    if (ds != null) {
//                        ds.flush();
//                        ds.close();
//                    }
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                    return "connection_fail";
//                } catch (Exception e11) {
//                    ds = ds2;
//                    if (is != null) {
//                        try {
//                            is.close();
//                        } catch (IOException e322222) {
//                            e322222.printStackTrace();
//                            return "upload_images_fail";
//                        }
//                    }
//                    if (fStream != null) {
//                        fStream.close();
//                    }
//                    if (ds != null) {
//                        ds.flush();
//                        ds.close();
//                    }
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                    return "upload_images_fail";
//                } catch (Throwable th4) {
//                    th = th4;
//                    ds = ds2;
//                    if (is != null) {
//                        is.close();
//                    }
//                    if (fStream != null) {
//                        fStream.close();
//                    }
//                    if (ds != null) {
//                        ds.flush();
//                        ds.close();
//                    }
//                    if (con != null) {
//                        con.disconnect();
//                    }
//                    throw th;
//                }
//            } catch (ClientProtocolException e12) {
//                e2 = e12;
//                e2.printStackTrace();
//                if (is != null) {
//                    is.close();
//                }
//                if (fStream != null) {
//                    fStream.close();
//                }
//                if (ds != null) {
//                    ds.flush();
//                    ds.close();
//                }
//                if (con != null) {
//                    con.disconnect();
//                }
//                return "connection_fail";
//            } catch (SocketTimeoutException e13) {
//                MCLogUtil.i(TAG, "SocketTimeoutException");
//                if (is != null) {
//                    is.close();
//                }
//                if (fStream != null) {
//                    fStream.close();
//                }
//                if (ds != null) {
//                    ds.flush();
//                    ds.close();
//                }
//                if (con != null) {
//                    con.disconnect();
//                }
//                return "connection_fail";
//            } catch (IOException e14) {
//                e14.printStackTrace();
//                if (is != null) {
//                    is.close();
//                }
//                if (fStream != null) {
//                    fStream.close();
//                }
//                if (ds != null) {
//                    ds.flush();
//                    ds.close();
//                }
//                if (con != null) {
//                    con.disconnect();
//                }
//                return "connection_fail";
//            } catch (Exception e15) {
//                if (is != null) {
//                    is.close();
//                }
//                if (fStream != null) {
//                    fStream.close();
//                }
//                if (ds != null) {
//                    ds.flush();
//                    ds.close();
//                }
//                if (con != null) {
//                    con.disconnect();
//                }
//                return "upload_images_fail";
//            }
//        }
//        MCLogUtil.i(TAG, "connnect fail");
//        return "connection_fail";
//    }

    public static HttpClient getNewHttpClient(Context context, int connectionTimeout, int socketTimeout) {
        HttpClient client = null;
        Cursor mCursor = null;
        try {
            KeyStore.getInstance(KeyStore.getDefaultType()).load(null, null);
            HttpParams params = new BasicHttpParams();
            if (connectionTimeout > 0) {
                HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
            } else {
                HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
            }
            if (socketTimeout > 0) {
                HttpConnectionParams.setSoTimeout(params, socketTimeout);
            } else {
                HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
            }
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            client = new DefaultHttpClient(params);
            if (!((WifiManager) context.getSystemService("wifi")).isWifiEnabled()) {
                mCursor = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
                if (mCursor != null && mCursor.moveToFirst()) {
                    String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
                    if (proxyStr != null && proxyStr.trim().length() > 0) {
                        client.getParams().setParameter("http.route.default-proxy", new HttpHost(proxyStr, 80));
                    }
                }
            }
            if (mCursor != null) {
                mCursor.close();
            }
        } catch (Exception e) {
            client = new DefaultHttpClient();
            if (mCursor != null) {
                mCursor.close();
            }
        } catch (Throwable th) {
            if (mCursor != null) {
                mCursor.close();
            }
        }
        return client;
    }

//    public static HttpURLConnection getNewHttpURLConnection1(Context context) {
//        MalformedURLException e;
//        IOException e2;
//        try {
//            URL url = new URL("http://192.168.1.44:8080/test3/time");
//            try {
//                BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
//                while (true) {
//                    String line = br.readLine();
//                    if (line == null) {
//                        break;
//                    }
//                    System.out.println(line);
//                }
//                MCLogUtil.e("test", url.toString());
//                URL url2 = url;
//            } catch (MalformedURLException e3) {
//                e = e3;
//                url2 = url;
//                e.printStackTrace();
//                return null;
//            } catch (IOException e4) {
//                e2 = e4;
//                url2 = url;
//                e2.printStackTrace();
//                return null;
//            }
//        } catch (MalformedURLException e5) {
//            e = e5;
//            e.printStackTrace();
//            return null;
//        } catch (IOException e6) {
//            e2 = e6;
//            e2.printStackTrace();
//            return null;
//        }
//        return null;
//    }

    private static String read(HttpResponse response) throws Exception {
        IllegalStateException e;
        IOException e2;
        Throwable th;
        String result = "";
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            inputStream = response.getEntity().getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            try {
                Header header = response.getFirstHeader("Content-Encoding");
                if (header != null && header.getValue().toLowerCase().indexOf(MCShareApiConstant.GZIP) > -1) {
                    inputStream = new GZIPInputStream(inputStream);
                }
                byte[] sBuffer = new byte[512];
                while (true) {
                    int readBytes = inputStream.read(sBuffer);
                    if (readBytes == -1) {
                        break;
                    }
                    content.write(sBuffer, 0, readBytes);
                }
                result = new String(content.toByteArray());
                if (content != null) {
                    content.flush();
                    content.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return result;
            } catch (IllegalStateException e3) {
                e = e3;
                byteArrayOutputStream = content;
            } catch (IOException e4) {
                e2 = e4;
                byteArrayOutputStream = content;
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = content;
            }
        } catch (IllegalStateException e5) {
            e = e5;
            try {
                throw new Exception(e);
            } catch (Throwable th3) {
                th = th3;
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e6) {
            e2 = e6;
            throw new Exception(e2);
        }
        return result;
    }
}
