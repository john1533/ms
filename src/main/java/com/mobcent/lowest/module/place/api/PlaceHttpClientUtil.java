package com.mobcent.lowest.module.place.api;

import com.mobcent.lowest.base.utils.MCLogUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class PlaceHttpClientUtil {
    public static final int CONNECT_TIME_OUT = 5000;
    public static final int READ_TIME_OUT = 5000;
    public static String TAG = "PlaceHttpClientUtil";

    public static String executeHttpGet(String urlString, HashMap<String, String> params) {
        Throwable th;
        String result = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        String tempUrl = new StringBuilder(String.valueOf(urlString)).append("?").toString();
        if (params != null) {
            try {
                if (!params.isEmpty()) {
                    for (String key : params.keySet()) {
                        tempUrl = new StringBuilder(String.valueOf(tempUrl)).append(key).append("=").append((String) params.get(key)).append("&").toString();
                    }
                }
            } catch (Exception e) {
                Exception e2 = e;
            }
        }
        URL url = null;
        try {
            url = new URL(tempUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL url2;
        try {
            MCLogUtil.i(TAG, tempUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStreamReader in2 = new InputStreamReader(connection.getInputStream());
            try {
                BufferedReader bufferedReader = new BufferedReader(in2);
                StringBuffer strBuffer = new StringBuffer();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    strBuffer.append(line);
                }
                result = strBuffer.toString();
                if (connection != null) {
                    connection.disconnect();
                }
                if (in2 != null) {
                    try {
                        in2.close();
                        in = in2;
                        url2 = url;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return result;
                }
                in = in2;
                url2 = url;
            } catch (Exception e4) {
                in = in2;
                url2 = url;
            } catch (Throwable th2) {
                th = th2;
                in = in2;
                url2 = url;
            }
        } catch (Exception e5) {
            url2 = url;
            try {
                e5.printStackTrace();
                if (connection != null) {
                    connection.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
                return result;
            } catch (Throwable th3) {
                th = th3;
                if (connection != null) {
                    connection.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
            }
        } catch (Throwable th4) {
            th = th4;
            url2 = url;
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
