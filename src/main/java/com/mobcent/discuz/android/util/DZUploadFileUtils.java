package com.mobcent.discuz.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.mobcent.lowest.base.utils.MCImageUtil;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.tencent.connect.common.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class DZUploadFileUtils {
    private static final String AUDIO_TYPE = "audio/mp3";
    private static final String ENCORDING = "UTF-8";
    private static final String IMG_TYPE = "image/jpeg";
    public static int TYPE_AUDIO = 2;
    public static int TYPE_IMG = 1;

    public static String upload(Context context, String requestUrl, Map<String, String> params, File[] files, String type, String uploadKeyName, boolean isOriginal) {
        Log.v("discuz", DZUploadFileUtils.class.getName() + "--upload()");
//        Exception e;
//        if (files == null || files.length == 0) {
//            return "";
//        }
//        if (!(params == null || params.isEmpty())) {
//            for (String key : params.keySet()) {
//                requestUrl = new StringBuilder(String.valueOf(requestUrl)).append("&").append(key).append("=").append((String) params.get(key)).toString();
//            }
//        }
//        MCLogUtil.i("test", "requestUrl==" + requestUrl);
//        String LINED = "\r\n";
//        String boundary = "---------------------------7db1c523809b2";
//        HttpURLConnection httpURLConnection = null;
//        OutputStream outputStream = null;
//        httpURLConnection = (HttpURLConnection) new URL(requestUrl).openConnection();
//        httpURLConnection.setConnectTimeout(10000);
//        httpURLConnection.setRequestMethod(Constants.HTTP_POST);
//        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//        httpURLConnection.setDoOutput(true);
//        httpURLConnection.setDoInput(true);
//        outputStream = httpURLConnection.getOutputStream();
//        byte[] end = new StringBuilder(String.valueOf(LINED)).append("--").append(boundary).append("--").append(LINED).toString().getBytes(ENCORDING);
//        int length = files.length;
//        int i = 0;
//        FileInputStream fis = null;
//        while (i < length) {
//            String str;
//            FileInputStream fileInputStream;
//            File file = files[i];
//            StringBuilder sb = new StringBuilder();
//            sb.append("--" + boundary + LINED);
//            sb.append("Content-Disposition: form-data; name=\"" + uploadKeyName + "\"; filename=\"" + file.getName() + "\"" + LINED);
//            StringBuilder stringBuilder = new StringBuilder("Content-Type: ");
//            if ("image".equals(type)) {
//                str = IMG_TYPE;
//            } else {
//                try {
//                    str = AUDIO_TYPE;
//                } catch (Exception e2) {
//                    e = e2;
//                    fileInputStream = fis;
//                } catch (Throwable th) {
//                    Throwable th2 = th;
//                    fileInputStream = fis;
//                }
//            }
//            sb.append(stringBuilder.append(str).append(LINED).toString());
//            sb.append(LINED);
//            outputStream.write(sb.toString().getBytes(ENCORDING));
//            if ("image".equals(type)) {
//                String filePath;
//                ImageSize imageSize;
//                if (isOriginal) {
//                    imageSize = new ImageSize(720, 1280);
//                } else {
//                    imageSize = new ImageSize(640, 960);
//                }
//                if (TextUtils.isEmpty(file.getPath()) || !file.getPath().startsWith("/")) {
//                    filePath = file.getPath();
//                } else {
//                    filePath = Uri.fromFile(file).toString();
//                }
//                Bitmap bitmap = MCImageUtil.getBitmapFromMedia(context, Scheme.FILE.crop(filePath), targetImageSize.getWidth(), targetImageSize.getHeight());
//                if (bitmap != null) {
//                    bitmap.compress(CompressFormat.JPEG, 70, outputStream);
//                    bitmap.recycle();
//                    fileInputStream = fis;
//                } else {
//                    fileInputStream = fis;
//                }
//            } else {
//                fileInputStream = new FileInputStream(file.getAbsolutePath());
//                try {
//                    byte[] buf = new byte[5120];
//                    while (true) {
//                        int len = fileInputStream.read(buf);
//                        if (len != -1) {
//                            outputStream.write(buf, 0, len);
//                        }
//                    }
//                    outputStream.write(LINED.getBytes());
//                    fileInputStream.close();
//                    fileInputStream = null;
//                    i++;
//                    fis = fileInputStream;
//                } catch (Exception e3) {
//                    e = e3;
//                }
//            }
//            outputStream.write(LINED.getBytes());
//            try {
//                fileInputStream.close();
//                fileInputStream = null;
//            } catch (Exception e4) {
//            }
//            i++;
//            fis = fileInputStream;
//        }
//        outputStream.write(end);
//        String jsonStr = MCLibIOUtil.convertStreamToString(httpURLConnection.getInputStream());
//        MCLogUtil.i("test", "jsonStr==" + jsonStr);
//        if (httpURLConnection != null) {
//            try {
//                httpURLConnection.disconnect();
//            } catch (Exception e5) {
//                e5.printStackTrace();
//            }
//        }
//        if (outputStream != null) {
//            outputStream.close();
//        }
//        return jsonStr;
//        try {
//            e5.printStackTrace();
//            if (httpURLConnection != null) {
//                try {
//                    httpURLConnection.disconnect();
//                } catch (Exception e52) {
//                    e52.printStackTrace();
//                    return "upload_images_fail";
//                }
//            }
//            if (outputStream != null) {
//                outputStream.close();
//            }
//            return "upload_images_fail";
//        } catch (Throwable th3) {
//            th2 = th3;
//            if (httpURLConnection != null) {
//                try {
//                    httpURLConnection.disconnect();
//                } catch (Exception e522) {
//                    e522.printStackTrace();
//                    throw th2;
//                }
//            }
//            if (outputStream != null) {
//                outputStream.close();
//            }
//            throw th2;
//        }
        return "";
    }

    public static DisplayImageOptions getUploadOptions() {
        return new Builder().cacheInMemory(false).cacheOnDisk(false).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }
}
