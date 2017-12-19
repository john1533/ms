package com.org.fourtween.utils.download;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by John on 2016/5/5.
 */
public class Downloader {
    private HttpControl httpControl;
    private Context mContext;
    private DownLoadedCallback mCallback;
    private final static int MSG_SUCCESS = 1;
    private final static int MSG_MalformedURLException = 2;
    private final static int MSG_IOException = 3;

    public final static String TAG = "splash";
//    private String mResult;

    public interface DownLoadedCallback {
        public void onSuccess(Context context, String result);
        public void onFail(int code);
    }


    public Downloader(Context context, HttpControl httpControl){
        this.mContext = context;
        this.httpControl = httpControl;
    }

    public void setCallback(DownLoadedCallback callback){
        mCallback = callback;
    }



    private void handleResult(int status,String result){
        if(mCallback==null)
            return;
        switch (status) {
            case MSG_SUCCESS:
                mCallback.onSuccess(mContext,result);
                break;
            case MSG_MalformedURLException:
                mCallback.onFail(MSG_MalformedURLException);
                break;
            case MSG_IOException:
                mCallback.onFail(MSG_IOException);
                break;
        }
    }



    public void doDownload(){
        new Thread(){

            public void run() {
                String result = "";
                int status = MSG_SUCCESS;
                StringBuilder builder = null;
                InputStream inputStream = null;
                HttpURLConnection conn = null;
                try {
                    URL url;
                    byte method = httpControl.getMethod();
                    if(method==0){
                        url = new URL(httpControl.getFullUrlPath());
                    }else {
                        url = new URL(httpControl.getUrl());
                    }
//                    Log.v(TAG, "url:" + url.toString());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod(httpControl.getMethod()==0?"GET":"POST");
//                    conn.setRequestProperty("req",httpControl.getParm());
//                    conn.addRequestProperty("req",httpControl.getParm());
                    httpControl.getParm();
                    conn.setDoInput(true);
                    if(method==1){
                        conn.setDoOutput(true);
                    }
                    // Starts the query
                    conn.connect();

                    if(method==1){
                        DataOutputStream out =  new DataOutputStream(conn.getOutputStream());
                        out.write(httpControl.getParm().getBytes());
                        out.flush();
                        out.close();
                    }

                    inputStream = conn.getInputStream();
                    if (inputStream != null) {
                        if(httpControl.getDealType()==0){//不保存文件
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(inputStream));
                            builder = new StringBuilder();

                            String s = null;
                            for (s = reader.readLine(); s != null; s = reader.readLine()) {
                                builder.append(s);
                            }
                            result = builder.toString();
                        }else if (httpControl.getDealType()==1){
                            String savePath = httpControl.getSavePath();
                            if(TextUtils.isEmpty(savePath)){
                                throw new RuntimeException("Save Path must be set");
                            }
                            File parent = new File(savePath).getParentFile();

                            if(!parent.exists()){
                                parent.mkdirs();
                            }
//                          Log.v(TAG, parent.getAbsolutePath());
                            File saveFile = new File(savePath);
                            saveFile.createNewFile();
                            FileOutputStream outputStream = new FileOutputStream(saveFile);
                            try{
                                byte data[] = new byte[1024];
                                int length = -1;
                                while ((length = inputStream.read(data)) != -1) {
                                    outputStream.write(data, 0, length);
                                }
                                outputStream.flush();
                                result = saveFile.getAbsolutePath();
                            }finally {
                                outputStream.close();
                            }
                        }
                    }
                } catch (MalformedURLException e) {
                    status = MSG_MalformedURLException;
                    e.printStackTrace();

                } catch (IOException e1){
                    status = MSG_IOException;
                    e1.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                Log.v(TAG, "result1:" + result);
                status = MSG_SUCCESS;

                handleResult(status,result);

            }
        }.start();

    }


}
