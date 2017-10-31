package com.mobcent.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MCShareSnapshotIOUtil {
    public static final String FS = File.separator;

    public static String getUrlResponse(String url) {
        try {
            return convertStreamToString(new DefaultHttpClient().execute(new HttpGet(url)).getEntity().getContent());
        } catch (Exception e) {
            return null;
        }
    }

    public static String unGzip(HttpEntity compressedEntity) throws IOException {
        if (compressedEntity == null) {
            return "";
        }
        String results = convertStreamToString(new GZIPInputStream(compressedEntity.getContent()));
        if (results == null) {
            return results;
        }
        if (results.trim().startsWith("{") && results.trim().endsWith("}")) {
            return results;
        }
        if (results.trim().startsWith("[") && results.trim().endsWith("]")) {
            return results;
        }
        return convertStreamToString(compressedEntity.getContent());
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String convertStreamToString(java.io.InputStream r6) {
        BufferedReader r2 = new BufferedReader(new InputStreamReader(r6),8192);
        StringBuilder r3 = new StringBuilder();
        String tmp;
        try {
            while((tmp = r2.readLine())!=null){
                r3.append(tmp).append("\n");
            }
            r6.close();
            /*
            r2 = new java.io.BufferedReader;
            r4 = new java.io.InputStreamReader;
            r4.<init>(r6);
            r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r2.<init>(r4, r5);
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r1 = 0;
        L_0x0012:
            r1 = r2.readLine();	 Catch:{ IOException -> 0x0037 }
            if (r1 != 0) goto L_0x0020;
        L_0x0018:
            r6.close();	 Catch:{ IOException -> 0x004c }
        L_0x001b:
            r4 = r3.toString();
            return r4;
        L_0x0020:
            r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0037 }
            r5 = java.lang.String.valueOf(r1);	 Catch:{ IOException -> 0x0037 }
            r4.<init>(r5);	 Catch:{ IOException -> 0x0037 }
            r5 = "\n";
            r4 = r4.append(r5);	 Catch:{ IOException -> 0x0037 }
            r4 = r4.toString();	 Catch:{ IOException -> 0x0037 }
            r3.append(r4);	 Catch:{ IOException -> 0x0037 }
            goto L_0x0012;
        L_0x0037:
            r0 = move-exception;
            r4 = "convertStreamToString";
            r5 = "convertStreamToString error";
            android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0045 }
            r6.close();	 Catch:{ IOException -> 0x0043 }
            goto L_0x001b;
        L_0x0043:
            r4 = move-exception;
            goto L_0x001b;
        L_0x0045:
            r4 = move-exception;
            r6.close();	 Catch:{ IOException -> 0x004a }
        L_0x0049:
            throw r4;
        L_0x004a:
            r5 = move-exception;
            goto L_0x0049;
        L_0x004c:
            r4 = move-exception;
            goto L_0x001b;
            */
            //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.android.utils.MCShareSnapshotIOUtil.convertStreamToString(java.io.InputStream):java.lang.String");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r3.toString();
    }

    public static Bitmap getBitmapFromUrl(URL url) {
        Throwable th;
        InputStream in = null;
        OutputStream out = null;
        try {
            InputStream in2 = new BufferedInputStream(url.openStream(), 4096);
            try {
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                OutputStream out2 = new BufferedOutputStream(dataStream, 4096);
                try {
                    copy(in2, out2);
                    out2.flush();
                    byte[] data = dataStream.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, new Options());
                    closeStream(in2);
                    closeStream(out2);
                    out = out2;
                    in = in2;
                    return bitmap;
                } catch (IOException e) {
                    out = out2;
                    in = in2;
                    closeStream(in);
                    closeStream(out);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    out = out2;
                    in = in2;
                    closeStream(in);
                    closeStream(out);
                    throw th;
                }
            } catch (IOException e2) {
                in = in2;
                closeStream(in);
                closeStream(out);
                return null;
            } catch (Throwable th3) {
                th = th3;
                in = in2;
                closeStream(in);
                closeStream(out);
                throw th;
            }
        } catch (IOException e3) {
            closeStream(in);
            closeStream(out);
            return null;
        } catch (Throwable th4) {
            th = th4;
            closeStream(in);
            closeStream(out);
//            throw th;
        }
        return  null;
    }

    public static Drawable getDrawableFromUrl(URL url) {
        try {
            return Drawable.createFromStream(url.openStream(), "src");
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[4096];
        while (true) {
            int read = in.read(b);
            if (read != -1) {
                out.write(b, 0, read);
            } else {
                return;
            }
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

    public static boolean saveFile(byte[] fileStream, String relativePath, String basePath) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            for (byte write : fileStream) {
                byteOutputStream.write(write);
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new StringBuilder(String.valueOf(basePath)).append(FS).append(relativePath).toString());
            fileOutputStream.write(byteOutputStream.toByteArray());
            byteOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean saveImageFile(Context activity, Bitmap bitmap, CompressFormat compressFormat, String relativePath, String basePath, int quality) {
        if (bitmap == null) {
            return false;
        }
        File file = new File(new StringBuilder(String.valueOf(basePath)).append(FS).append(relativePath).toString());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            try {
                bitmap.compress(compressFormat, quality, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            } catch (Exception e) {
                OutputStream outputStream = fileOutputStream;
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean getExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if ("mounted".equals(state)) {
            return true;
        }
        return "mounted_ro".equals(state) ? false : false;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean isDirExist(String path) {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean makeDirs(String path) {
        return new File(path).mkdirs();
    }

    public static String getBaseLocalLocation(Context context) {
        String baseLocation = "";
        if (getExternalStorageState()) {
            return getSDCardPath();
        }
        return context.getFilesDir().getAbsolutePath();
    }

    public static int getFileSize(File file) {
        FileNotFoundException e;
        IOException e2;
        int size = 0;
        try {
            FileInputStream fis = new FileInputStream(file);
            FileInputStream fileInputStream;
            try {
                size = fis.available();
                fileInputStream = fis;
            } catch (FileNotFoundException e3) {
                e = e3;
                fileInputStream = fis;
                e.printStackTrace();
                return size;
            } catch (IOException e4) {
                e2 = e4;
                fileInputStream = fis;
                e2.printStackTrace();
                return size;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            return size;
        }
        return size;
    }
}
