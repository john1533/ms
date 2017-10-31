package com.mobcent.discuz.android.api.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.mobcent.android.constant.MCShareApiConstant;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.utils.DownloadUtils.DownProgressDelegate;
import com.tencent.connect.common.Constants;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class DZHttpClientUtil {
    public static final String DISCUZ_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36";
    public static final String PHPWIND_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Android; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36";
    private static final int SET_CONNECTION_TIMEOUT = 50000;
    public static final String SET_CONNECTION_TIMEOUT_STR = "connection_timeout";
    private static final int SET_SOCKET_TIMEOUT = 200000;
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
        HttpURLConnection ret = null;
        try {
        ret  = (HttpURLConnection)r15.openConnection();
        ret.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");

        WifiManager r13 = (WifiManager)r16.getSystemService(Context.WIFI_SERVICE);
        if(!r13.isWifiEnabled()){
//            r1 = "content://telephony/carriers/preferapn";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r2 = android.net.Uri.parse(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r1 = r16.getContentResolver();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r3 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r4 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r5 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r6 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r10 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            if (r10 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            L_0x0036:
//            r1 = r10.moveToFirst();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            if (r1 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            L_0x003c:
//            r1 = "proxy";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r1 = r10.getColumnIndex(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            r12 = r10.getString(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//            if (r12 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
            Uri uri = Uri.parse("content://telephony/carriers/preferapn");
            ContentResolver resolver = r16.getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()){
                String strProxy = cursor.getString(cursor.getColumnIndex("proxy"));
                if(strProxy != null&&strProxy.trim().length()>0){
//                    r11 = new java.net.Proxy;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r1 = java.net.Proxy.Type.HTTP;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r3 = new java.net.InetSocketAddress;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r4 = 80;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r3.<init>(r12, r4);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r11.<init>(r1, r3);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r1 = r15.openConnection(r11);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r0 = r1;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
//                    r7 = r0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
                    Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(strProxy,80));
                    ret = (HttpURLConnection)r15.openConnection(proxy);
                }
                cursor.close();
            }

        }

        /*
        r14 = 0;
        r7 = 0;
        r10 = 0;
        r1 = r15.openConnection();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r0 = r1;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r7 = r0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = "User-Agent";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r3 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r7.setRequestProperty(r1, r3);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = "wifi";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r0 = r16;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r13 = r0.getSystemService(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r13 = (android.net.wifi.WifiManager) r13;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = r13.isWifiEnabled();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        if (r1 != 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
    L_0x0022:
        r1 = "content://telephony/carriers/preferapn";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r2 = android.net.Uri.parse(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = r16.getContentResolver();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r3 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r4 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r5 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r6 = 0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r10 = r1.query(r2, r3, r4, r5, r6);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        if (r10 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
    L_0x0036:
        r1 = r10.moveToFirst();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        if (r1 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
    L_0x003c:
        r1 = "proxy";	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = r10.getColumnIndex(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r12 = r10.getString(r1);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        if (r12 == 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
    L_0x0048:
        r1 = r12.trim();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = r1.length();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        if (r1 <= 0) goto L_0x0068;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
    L_0x0052:
        r11 = new java.net.Proxy;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = java.net.Proxy.Type.HTTP;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r3 = new java.net.InetSocketAddress;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r4 = 80;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r3.<init>(r12, r4);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r11.<init>(r1, r3);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = r15.openConnection(r11);	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r0 = r1;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r0 = (java.net.HttpURLConnection) r0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r7 = r0;	 Catch:{ Exception -> 0x006f, all -> 0x008a }
    L_0x0068:
        if (r10 == 0) goto L_0x006d;
    L_0x006a:
        r10.close();
    L_0x006d:
        r1 = r7;
    L_0x006e:
        return r1;
    L_0x006f:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        r1 = r15.openConnection();	 Catch:{ IOException -> 0x007f }
        r1 = (java.net.HttpURLConnection) r1;	 Catch:{ IOException -> 0x007f }
        if (r10 == 0) goto L_0x006e;
    L_0x007b:
        r10.close();
        goto L_0x006e;
    L_0x007f:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ Exception -> 0x006f, all -> 0x008a }
        if (r10 == 0) goto L_0x0088;
    L_0x0085:
        r10.close();
    L_0x0088:
        r1 = r14;
        goto L_0x006e;
    L_0x008a:
        r1 = move-exception;
        if (r10 == 0) goto L_0x0090;
    L_0x008d:
        r10.close();
    L_0x0090:
        throw r1;
        */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ret;
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.api.util.DZHttpClientUtil.getNewHttpURLConnection(java.net.URL, android.content.Context):java.net.HttpURLConnection");
    }

    /**
    *@param urlString 相对路经
    *@param params post参数
    *@param context
    *@return 服务器返回结果
    * */
    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        String result = "";
        String tempURL = "";
        if (!urlString.contains("http://")) {
            urlString = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(urlString).toString();
        }
        if (urlString.contains("?")) {
            tempURL = urlString;
        } else {
//            tempURL = new StringBuilder(String.valueOf(urlString)).append("?").toString();
            tempURL = urlString+"?";
        }
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
        params.put("sdkVersion", BaseApiConstant.SDK_VERSION_VALUE);
        HttpClient client = getNewHttpClient(context, connectionTimeout, socketTimeout);
        HttpPost httpPost = new HttpPost(urlString);
        if (SharedPreferencesDB.getInstance(context.getApplicationContext()).getForumType().equals(FinalConstant.PHPWIND)) {
            httpPost.setHeader("User-Agent", PHPWIND_USER_AGENT);
        } else {
            httpPost.setHeader("User-Agent", DISCUZ_USER_AGENT);
        }
        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.addHeader("Accept-Encoding", MCShareApiConstant.GZIP);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            if (!(params == null || params.isEmpty())) {
            if (params != null && !params.isEmpty()) {
                for (String key : params.keySet()) {
                    nameValuePairs.add(new BasicNameValuePair(key, (String) params.get(key)));
                    if (urlString.contains("?")) {
//                        tempURL = new StringBuilder(String.valueOf(tempURL)).append("&").append(key).append("=").append((String) params.get(key)).toString();
                        tempURL = tempURL+"&"+key+"="+params.get(key);
                    } else {
//                        tempURL = new StringBuilder(String.valueOf(tempURL)).append(key).append("=").append((String) params.get(key)).append("&").toString();
                        tempURL = tempURL+key+"="+params.get(key)+"&";
                    }
                }
            }
            MCLogUtil.i(TAG, "tempURL = " + tempURL);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                return "connection_fail";
            }
            if (isNeedDealCookie(context, tempURL)) {
                List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
                if (!(cookies == null || cookies.isEmpty())) {
                    int len = cookies.size();
                    for (int i = 0; i < len; i++) {
                        Cookie cookie = (Cookie) cookies.get(i);
                        setCookie(context, cookie.getDomain(), cookie.getName() + "=" + cookie.getValue());
                    }
                }
            }
            result = read(response, context);
            MCLogUtil.i(TAG, "result = " + result);
            result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
//            if (result == null) {
//                return "{}";
//            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "connection_fail";
        }
    }

    public static String doGetRequest(String urlString, HashMap<String, String> params, Context context) {
        String result = "";
        String tempURL = new StringBuilder(String.valueOf(urlString)).append("?").toString();
        HttpClient client = getNewHttpClient(context);
        if (!(params == null || params.isEmpty())) {
            for (String key : params.keySet()) {
                tempURL = new StringBuilder(String.valueOf(tempURL)).append(key).append("=").append((String) params.get(key)).append("&").toString();
            }
        }
        HttpGet httpGet = new HttpGet(tempURL);
        httpGet.setHeader("User-Agent", DISCUZ_USER_AGENT);
        httpGet.addHeader("Accept-Encoding", MCShareApiConstant.GZIP);
        try {
            MCLogUtil.i(TAG, "tempURL = " + tempURL);
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                return MCResource.getInstance(context).getString("mc_forum_connection_fail");
            }
            result = read(response, context);
            MCLogUtil.i(TAG, "result = " + result);
            if (result == null) {
                return "{}";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return MCResource.getInstance(context).getString("mc_forum_connection_fail");
        }
    }

    public static synchronized byte[] getFileInByte(String url, Context context) {
        URL myFileUrl;
        URL url2;
        Throwable th;
        byte[] bArr = null;
        synchronized (DZHttpClientUtil.class) {
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
        Throwable th;
        URL url2;
        try {
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

    public static String getFileName(HttpURLConnection conn, String downloadUrl) {
        String filename = downloadUrl.substring(downloadUrl.lastIndexOf(47) + 1);
        if (filename == null || "".equals(filename.trim())) {
            int i = 0;
            while (true) {
                String mine = conn.getHeaderField(i);
                if (mine == null) {
                    break;
                }
                if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if (m.find()) {
                        return m.group(1);
                    }
                }
                i++;
            }
            filename = UUID.randomUUID() + ".tmp";
        }
        return filename;
    }

    public static synchronized byte[] getFileInByteByProgress(String url, Context context, DownProgressDelegate downProgress) {
        URL myFileUrl;
        URL url2;
        Throwable th;
        byte[] bArr = null;
        synchronized (DZHttpClientUtil.class) {
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
                    downProgress.setMax(length);
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
                            downProgress.setProgress(destPos);
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
                }  catch (Throwable th2) {
                    th = th2;
                    url2 = myFileUrl;
                    throw th;
                }
            } catch (Exception e4) {
            } catch (Throwable th3) {
                th = th3;
            }
        }
        return bArr;
//        url2 = myFileUrl;
//        return bArr;
    }

    public static String uploadFile(String urlString, File file, String uploadFile, String accessToken, String accessSecret, String json, String type, String module, String formKey) {
        Exception e;
        HttpURLConnection httpUrlConnection = null;
        OutputStream os = null;
        BufferedInputStream fis = null;
        try {
            File file2 = new File(uploadFile);
            try {
                String path;
                String apphash = MCStringUtil.stringToMD5(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(System.currentTimeMillis())).toString().substring(0, 5))).append(BaseApiConstant.AUTHKEY).toString()).substring(8, 16);
                if (type.equals("icon")) {
                    path = new StringBuilder(String.valueOf(urlString)).append("&filename=").append(file2.getName()).append("&uploadFile=").append(uploadFile).append("&accessToken=").append(accessToken).append("&accessSecret=").append(accessSecret).append("&sdkVersion=").append(BaseApiConstant.SDK_VERSION_VALUE).append("&apphash=").append(apphash).append("&forumKey=").append(formKey).toString();
                } else {
                    path = new StringBuilder(String.valueOf(urlString)).append("&attachment=").append(URLEncoder.encode(json, "utf-8")).append("&accessToken=").append(accessToken).append("&accessSecret=").append(accessSecret).append("&sdkVersion=").append(BaseApiConstant.SDK_VERSION_VALUE).append("&module=").append(module).append("&apphash=").append(apphash).append("&forumKey=").append(formKey).toString();
                }
                MCLogUtil.i("test", "uploadFile=" + uploadFile);
                MCLogUtil.i("test", "path=" + path);
                httpUrlConnection = (HttpURLConnection) new URL(path).openConnection();
                httpUrlConnection.setDoOutput(true);
                httpUrlConnection.setDoInput(true);
                httpUrlConnection.setRequestMethod(Constants.HTTP_POST);
                httpUrlConnection.setRequestProperty("User-Agent", DISCUZ_USER_AGENT);
                os = httpUrlConnection.getOutputStream();
                Thread.sleep(100);
                BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(file2));
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bufSize = fis2.read(buffer);
                        if (bufSize == -1) {
                            break;
                        }
                        os.write(buffer, 0, bufSize);
                    }
                    fis2.close();
                    System.out.println(httpUrlConnection.getResponseMessage());
                    String jsonStr = MCLibIOUtil.convertStreamToString(httpUrlConnection.getInputStream());
                    MCLogUtil.i(TAG, "jsonStr=" + jsonStr);
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fis2 != null) {
                        fis2.close();
                    }
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                    fis = fis2;
                    file = file2;
                    return jsonStr;
                } catch (Exception e3) {
                    e = e3;
                    fis = fis2;
                    file = file2;
                } catch (Throwable th2) {
                    fis = fis2;
                    file = file2;
                }
            } catch (Exception e4) {
                e = e4;
                file = file2;
                try {
                    e.printStackTrace();
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                            return "upload_images_fail";
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                    return "upload_images_fail";
                } catch (Throwable th3) {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                    if (httpUrlConnection != null) {
                        httpUrlConnection.disconnect();
                    }
                }
            } catch (Throwable th4) {
                file = file2;
                if (os != null) {
                    os.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                }
            }
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (httpUrlConnection != null) {
                httpUrlConnection.disconnect();
            }
        }
        return "upload_images_fail";
    }

    public static HttpClient getNewHttpClient(Context context) {
        HttpClient client = null;
        Cursor mCursor = null;
        try {
            KeyStore.getInstance(KeyStore.getDefaultType()).load(null, null);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
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

    private static String read(HttpResponse response, Context context) throws Exception {
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
            HttpProtocolParams.setUserAgent(params, DISCUZ_USER_AGENT);
            client = new DefaultHttpClient(params);
            if (!((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
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

    public static void setCookie(Context context, String url, String cookie) {
        CookieSyncManager.createInstance(context);
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
        MCLogUtil.e(TAG, new StringBuilder(String.valueOf(url)).append("==cookie===").append(cookie).toString());
    }

    public static void clearCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.removeSessionCookie();
        CookieSyncManager.getInstance().sync();
    }

    public static void logCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        MCLogUtil.e(TAG, new StringBuilder(String.valueOf(url)).append("==").append(CookieManager.getInstance().getCookie(url)).toString());
    }

    public static boolean isNeedDealCookie(Context context, String tempURL) {
        if (tempURL.contains(MCResource.getInstance(context).getString("mc_forum_request_login_or_logout")) || tempURL.contains(MCResource.getInstance(context).getString("mc_forum_request_switch_user")) || tempURL.contains(MCResource.getInstance(context).getString("mc_forum_request_registe_user")) || tempURL.contains(MCResource.getInstance(context).getString("mc_forum_qq_login_save_qq_info")) || tempURL.contains(MCResource.getInstance(context).getString("mc_forum_qq_login_result_openid")) || tempURL.contains(MCResource.getInstance(context).getString("mc_forum_user_platforminfo")) || tempURL.contains(MCResource.getInstance(context).getString("mc_forum_user_saveplatforminfo"))) {
            return true;
        }
        return false;
    }

    public static String doPostSimpleRequest(String urlString, HashMap<String, String> params, Context context) {
        String result = "";
        HttpClient client = getNewHttpClient(context);
        HttpPost httpPost = new HttpPost(urlString);
        List<NameValuePair> nameValuePairs = new ArrayList();
        if (!(params == null || params.isEmpty())) {
            for (String key : params.keySet()) {
                nameValuePairs.add(new BasicNameValuePair(key, (String) params.get(key)));
            }
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        httpPost.setHeader("User-Agent", DISCUZ_USER_AGENT);
        try {
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                return MCResource.getInstance(context).getString("mc_forum_connection_fail");
            }
            result = read(response, context);
            if (result == null) {
                return "{}";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return MCResource.getInstance(context).getString("mc_forum_connection_fail");
        }
    }
}
