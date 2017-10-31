package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.mobcent.android.util.RecordMp3Util;
import com.mobcent.lowest.base.service.FileTransferService;
import com.mobcent.lowest.base.service.impl.FileTransferServiceImpl;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.io.File;

public class MCRecordUtils {
    public static final int STATUS_TIMEOUT = -1;
    private static MCRecordUtils _instance;
    private static Object _lock = new Object();
    public String TAG = "RecordUtils";
    private String audioPath;
    private String basePath;
    private Context context;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    MCRecordUtils.this.notifyStatus(-1, MCRecordUtils.this.maxAmplitude);
                    MCRecordUtils.this.stopRecord();
                    return;
                case 0:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "In the recording");
                    break;
                case 1:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "To stop recording");
                    if (new File(MCRecordUtils.this.tempPath).exists()) {
                        new Thread() {
                            public void run() {
                                FileTransferService s = new FileTransferServiceImpl(MCRecordUtils.this.context);
                                MCRecordUtils.this.audioPath = MCRecordUtils.this.basePath + System.currentTimeMillis() + (MCRecordUtils.this.isMp3 ? ".mp3" : "awr");
                                s.copyFile(MCRecordUtils.this.tempPath, MCRecordUtils.this.audioPath);
                                MCRecordUtils.this.notifyStatus(1, 0);
                            }
                        }.start();
                        return;
                    }
                    return;
                case 2:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "Failed to get the buffer pool");
                    break;
                case 3:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "File creation failed");
                    break;
                case 4:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "Start recording failure");
                    break;
                case 5:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "Recording failure");
                    break;
                case 6:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "The recording encode failure");
                    break;
                case 7:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "The tape write to file failed");
                    break;
                case 8:
                    MCLogUtil.e(MCRecordUtils.this.TAG, "The recording failed to close the input stream");
                    break;
                case 9:
                    MCRecordUtils.this.maxAmplitude = ((Integer) msg.obj).intValue();
                    MCLogUtil.e("test", msg.obj.toString());
                    MCRecordUtils.this.notifyStatus(msg.what, MCRecordUtils.this.maxAmplitude);
                    return;
            }
            MCRecordUtils.this.notifyStatus(msg.what, 0);
        }
    };
    private boolean isMp3 = true;
    private int maxAmplitude;
    private int maxAudioTime = 300000;
    private Message msg;
    private RecordListener recordListener;
    private RecordMp3Util recordMp3Util = null;
    private int sampleRate = 8000;
    private String tempPath;

    public interface RecordListener {
        void onRecordStatusChange(String str, int i, int i2);
    }

    public MCRecordUtils(Context context) {
        this.basePath = MCLibIOUtil.getAudioCachePath(context.getApplicationContext());
        this.tempPath = this.basePath + "tempAudioFile";
        this.recordMp3Util = new RecordMp3Util(this.tempPath, this.sampleRate);
        this.recordMp3Util.setHandle(this.handler);
    }

    public static MCRecordUtils getInastance(Context context) {
        synchronized (_lock) {
            if (_instance == null) {
                _instance = new MCRecordUtils(context.getApplicationContext());
            }
        }
        return _instance;
    }

    public void startRecord() {
        this.isMp3 = false;
        this.recordMp3Util.start(2);
        startTimeCounter();
    }

    public void startRecordMp3() {
        this.isMp3 = true;
        this.recordMp3Util.start(1);
        startTimeCounter();
    }

    private void startTimeCounter() {
        this.msg = new Message();
        this.msg.what = -1;
        this.handler.sendMessageDelayed(this.msg, (long) this.maxAudioTime);
    }

    private void stopTimeCounter() {
        this.handler.removeMessages(-1);
    }

    public void stopRecord() {
        this.recordMp3Util.stop();
        stopTimeCounter();
    }

    private void notifyStatus(int status, int maxAmplitude) {
        if (this.recordListener != null) {
            this.recordListener.onRecordStatusChange(this.audioPath, status, maxAmplitude);
        }
    }

    public String getTempPath() {
        return this.tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public int getMaxAudioTime() {
        return this.maxAudioTime;
    }

    public void setMaxAudioTime(int maxAudioTime) {
        this.maxAudioTime = maxAudioTime;
    }

    public RecordListener getRecordListener() {
        return this.recordListener;
    }

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }
}
