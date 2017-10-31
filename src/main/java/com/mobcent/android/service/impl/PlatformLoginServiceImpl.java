package com.mobcent.android.service.impl;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import com.mobcent.android.constant.MCShareApiConstant;
import com.mobcent.android.model.PlatformLoginInfoModel;
import com.mobcent.android.service.PlatformLoginService;
import com.mobcent.discuz.android.api.util.DZHttpClientUtil;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;

public class PlatformLoginServiceImpl implements PlatformLoginService {
    private Context context;

    public PlatformLoginServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public PlatformLoginInfoModel getWeChatModel(String code, String appid, String appsecret) {
        HashMap<String, String> params = new HashMap();
        params.put("appid", appid);
        params.put("secret", appsecret);
        params.put(WBConstants.AUTH_PARAMS_CODE, code);
        params.put(WBConstants.AUTH_PARAMS_GRANT_TYPE, "authorization_code");
        return getPlatFormLoginInfoModel(this.context, doPostSimpleRequest("https://api.weixin.qq.com/sns/oauth2/access_token", params, this.context));
    }

    private PlatformLoginInfoModel getPlatFormLoginInfoModel(Context context, String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        PlatformLoginInfoModel platFormLoginInfoModel = new PlatformLoginInfoModel();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            platFormLoginInfoModel.setAccessToken(jsonObject.optString("access_token"));
            platFormLoginInfoModel.setExpiresIn(jsonObject.optInt(Constants.PARAM_EXPIRES_IN));
            platFormLoginInfoModel.setRefreshToken(jsonObject.optString("refresh_token"));
            platFormLoginInfoModel.setOpenid(jsonObject.optString("openid"));
            platFormLoginInfoModel.setScope(jsonObject.optString("scope"));
            return platFormLoginInfoModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        httpPost.setHeader("User-Agent", DZHttpClientUtil.DISCUZ_USER_AGENT);
        try {
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                return read(response, context);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpClient getNewHttpClient(Context context) {
        HttpClient client = null;
        Cursor mCursor = null;
        try {
            KeyStore.getInstance(KeyStore.getDefaultType()).load(null, null);
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 50000);
            HttpConnectionParams.setSoTimeout(params, 50000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
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
                throw e5;
            }
        } catch (IOException e6) {
            e2 = e6;
            throw new Exception(e2);
        }
        return result;
    }
}
