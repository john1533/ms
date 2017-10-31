package com.mobcent.android.api;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import com.mobcent.android.constant.MCShareApiConstant;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneConnectionUtil;
import com.tencent.connect.common.Constants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
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
import java.net.Proxy.Type;
import java.net.SocketTimeoutException;
import java.net.URL;
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

public class MCShareHttpClientUtil {
    public static final String FS = File.separator;
    private static final int SET_CONNECTION_TIMEOUT = 50000;
    private static final int SET_SOCKET_TIMEOUT = 200000;
    private static final String TAG = "MCShareHttpClientUtil";

    public static String doPostRequest(String urlString, HashMap<String, String> params, Context context) {
        String result = "";
        String tempURL = new StringBuilder(String.valueOf(urlString)).append("?").toString();
        HttpClient client = getNewHttpClient(context);
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
            if (response.getStatusLine().getStatusCode() != 200) {
                return "connection_fail";
            }
            result = read(response);
            MCLogUtil.i(TAG, "result = " + result);
            result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
            if (result == null) {
                return "{}";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "connection_fail";
        }
    }

//    public static synchronized byte[] getFileInByte(String url, Context context) {
//        URL url2;
//        Throwable th;
//        byte[] bArr = null;
//        synchronized (MCShareHttpClientUtil.class) {
//            byte[] imgData = null;
//            try {
//                URL myFileUrl = new URL(url);
//                InputStream is = null;
//                try {
//                    HttpURLConnection connection = getNewHttpURLConnection(myFileUrl, context);
//                    connection.setDoInput(true);
//                    connection.connect();
//                    is = connection.getInputStream();
//                    int length = connection.getContentLength();
//                    if (length != -1) {
//                        imgData = new byte[length];
//                        byte[] temp = new byte[512];
//                        int destPos = 0;
//                        while (true) {
//                            int readLen = is.read(temp);
//                            if (readLen > 0) {
//                                System.arraycopy(temp, 0, imgData, destPos, readLen);
//                                destPos += readLen;
//                            }
//                        }
//                        closeStream(is);
//                        url2 = myFileUrl;
//                        bArr = imgData;
//                    }
//                    closeStream(is);
//                    url2 = myFileUrl;
//                    bArr = imgData;
//                } catch (IOException e) {
//                    closeStream(is);
//                    url2 = myFileUrl;
//                } catch (Throwable th2) {
//                    th = th2;
//                    url2 = myFileUrl;
//                    throw th;
//                }
//            } catch (Exception e2) {
//            } catch (Throwable th3) {
//                th = th3;
//                throw th;
//            }
//        }
//        return bArr;
//    }

    public static void downloadFile(String url, File f, Context context) {
        try {
            MCLogUtil.i(TAG, "Image url is " + url);
            URL myFileUrl = new URL(url);
            try {
                HttpURLConnection connection = getNewHttpURLConnection(myFileUrl, context);
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                if (f.createNewFile()) {
                    FileOutputStream fo = new FileOutputStream(f);
                    byte[] buffer = new byte[256];
                    while (true) {
                        int size = is.read(buffer);
                        if (size <= 0) {
                            break;
                        }
                        fo.write(buffer, 0, size);
                    }
                    fo.flush();
                    fo.close();
                }
            } catch (MalformedURLException e) {
                MCLogUtil.i(TAG, "URL is format error");
            } catch (IOException e2) {
                MCLogUtil.i(TAG, "IO error when download file");
                MCLogUtil.i(TAG, "The URL is " + url + ";the file name " + f.getName());
            }
            URL url2 = myFileUrl;
        } catch (Exception e3) {
        }
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    public static String uploadFile(String actionUrl, String uploadFile, Context context) {
        HttpURLConnection con = null;
        MCLogUtil.i(TAG, "uploadUrl = " + actionUrl + ";uploadFile=" + uploadFile);
        if (MCPhoneConnectionUtil.isNetworkAvailable(context)) {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String fileName = "";
            if (uploadFile != null && uploadFile.lastIndexOf(FS) > -1) {
                fileName = uploadFile.substring(uploadFile.lastIndexOf(FS) + 1);
            }
            try {
                con = getNewHttpURLConnection(new URL(actionUrl), context);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setConnectTimeout(180000);
                con.setReadTimeout(180000);
                con.setRequestMethod(Constants.HTTP_POST);
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                ds.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(end).toString());
                ds.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\"" + fileName + "\"" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = fStream.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                ds.writeBytes(new StringBuilder(String.valueOf(twoHyphens)).append(boundary).append(twoHyphens).append(end).toString());
                fStream.close();
                ds.flush();
                String jsonStr = convertStreamToString(con.getInputStream());
                ds.close();
                return jsonStr;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return "connection_fail";
            } catch (SocketTimeoutException e2) {
                MCLogUtil.i(TAG, "SocketTimeoutException");
                return "connection_fail";
            } catch (IOException e3) {
                e3.printStackTrace();
                return "connection_fail";
            } catch (Exception e4) {
                return "{rs:0,reason:\"upload_images_fail\"}";
            } finally {
                con.disconnect();
            }
        } else {
            MCLogUtil.i(TAG, "connnect fail");
            return "connection_fail";
        }
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String convertStreamToString(java.io.InputStream r5) {
        BufferedReader r1 = new BufferedReader(new InputStreamReader(r5),8192);
        StringBuilder sb = new StringBuilder();
        String r0 = null;
        try {
            while((r0=r1.readLine())!=null){
                sb.append(r0).append("\n");
            }

            r5.close();

            /*
            r1 = new java.io.BufferedReader;
            r3 = new java.io.InputStreamReader;
            r3.<init>(r5);
            r4 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r1.<init>(r3, r4);
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r0 = 0;
        L_0x0012:
            r0 = r1.readLine();	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            if (r0 != 0) goto L_0x0020;
        L_0x0018:
            r5.close();	 Catch:{ IOException -> 0x0045 }
        L_0x001b:
            r3 = r2.toString();
            return r3;
        L_0x0020:
            r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            r4 = java.lang.String.valueOf(r0);	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            r3.<init>(r4);	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            r4 = "\n";
            r3 = r3.append(r4);	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            r3 = r3.toString();	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            r2.append(r3);	 Catch:{ IOException -> 0x0037, all -> 0x003e }
            goto L_0x0012;
        L_0x0037:
            r3 = move-exception;
            r5.close();	 Catch:{ IOException -> 0x003c }
            goto L_0x001b;
        L_0x003c:
            r3 = move-exception;
            goto L_0x001b;
        L_0x003e:
            r3 = move-exception;
            r5.close();	 Catch:{ IOException -> 0x0043 }
        L_0x0042:
            throw r3;
        L_0x0043:
            r4 = move-exception;
            goto L_0x0042;
        L_0x0045:
            r3 = move-exception;
            goto L_0x001b;
            */
//            throw new UnsupportedOperationException("Method not decompiled: com.mobcent.android.api.MCShareHttpClientUtil.convertStreamToString(java.io.InputStream):java.lang.String");
//            return r1.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static HttpClient getNewHttpClient(Context context) {
        try {
            KeyStore.getInstance(KeyStore.getDefaultType()).load(null, null);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            HttpClient client = new DefaultHttpClient(params);
            if (((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
                return client;
            }
            Cursor mCursor = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
            if (mCursor == null || !mCursor.moveToFirst()) {
                return client;
            }
            String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
            if (proxyStr != null && proxyStr.trim().length() > 0) {
                client.getParams().setParameter("http.route.default-proxy", new HttpHost(proxyStr, 80));
            }
            mCursor.close();
            return client;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static HttpURLConnection getNewHttpURLConnection(URL url, Context context) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (!((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()) {
                Cursor mCursor = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
                if (mCursor != null && mCursor.moveToFirst()) {
                    String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
                    if (proxyStr != null && proxyStr.trim().length() > 0) {
                        connection = (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress(proxyStr, 80)));
                    }
                    mCursor.close();
                }
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return (HttpURLConnection) url.openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    private static String read(HttpResponse response) throws Exception {
        String result = "";
        try {
            InputStream inputStream = response.getEntity().getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf(MCShareApiConstant.GZIP) > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }
            byte[] sBuffer = new byte[512];
            while (true) {
                int readBytes = inputStream.read(sBuffer);
                if (readBytes == -1) {
                    return new String(content.toByteArray());
                }
                content.write(sBuffer, 0, readBytes);
            }
        } catch (IllegalStateException e) {
            throw new Exception(e);
        } catch (IOException e2) {
            throw new Exception(e2);
        }
    }
}
