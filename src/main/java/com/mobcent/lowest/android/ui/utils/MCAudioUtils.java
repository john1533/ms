package com.mobcent.lowest.android.ui.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.mobcent.lowest.base.utils.MCLibIOUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.utils.DownloadUtils;
import com.mobcent.lowest.module.ad.utils.DownloadUtils.DownProgressDelegate;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MCAudioUtils {
    public static final int STATUS_DOWNERROR = 4;
    public static final int STATUS_DOWNSUCC = 5;
    public static final int STATUS_END = 6;
    public static final int STATUS_ERROR = 7;
    public static final int STATUS_NOLOCAL_FILE = 8;
    public static final int STATUS_PAUSE = 3;
    public static final int STATUS_PLAYING = 2;
    public static final int STATUS_PREPARE = 1;
    private static MCAudioUtils _instance;
    private static final Object _lock = new Object();
    public String TAG = "AudioUtils";
    private AudioPlayListener audioListener;
    private String baseDir = null;
    private Context context;
    private Thread downThread;
    private String filePath = null;
    private Handler handler2 = new Handler() {
        public void dispatchMessage(Message msg) {
            if (msg.what == 2) {
                Obj obj = (Obj)msg.obj;
                MCAudioUtils.this.audioListener.onAudioStatusChange(obj.url, obj.filePath, obj.status, obj.percent);
            }
        }
    };
    private boolean isPause = false;
    private boolean isSoundList = false;
    private MediaPlayer mp = null;
    private int percent = 0;
    private List<String> playList = new ArrayList();
    private TimerTask task = null;
    private Timer timer = new Timer();
    private String url;

    public interface AudioPlayListener {
        void onAudioStatusChange(String str, String str2, int i, int i2);
    }

    private interface DownListener {
        void onFileLoaded(String str, int i);
    }

    private class DownThread extends Thread {
        private DownListener listener;
        private String url;

        public DownThread(DownListener downListener, String url) {
            this.listener = downListener;
            this.url = url;
        }

        public void run() {
            String filePath = MCAudioUtils.this.baseDir + MCStringUtil.getMD5Str(this.url);
            File file = new File(filePath);
            if (!(file.exists() || DownloadUtils.downloadFile(MCAudioUtils.this.context, this.url, file, new DownProgressDelegate() {
                int max;

                public void setProgress(int progress) {
                    Log.e(MCAudioUtils.this.TAG, "down---progress------------" + ((progress * 100) / this.max));
                }

                public void setMax(int max) {
                    this.max = max;
                }
            }))) {
                MCAudioUtils.this.notifyDown(this.listener, null, 4);
            }
            MCAudioUtils.this.notifyDown(this.listener, filePath, 8);
        }
    }

    class Obj {
        public String filePath;
        public int percent;
        public int status;
        public String url;

        Obj() {
        }
    }

    public void registerListener(AudioPlayListener audioListener) {
        this.audioListener = audioListener;
    }

    public MCAudioUtils(Context context) {
        this.baseDir = MCLibIOUtil.getAudioCachePath(context);
        this.context = context;
    }

    public static MCAudioUtils getInstance(Context context) {
        synchronized (_lock) {
            if (_instance == null) {
                _instance = new MCAudioUtils(context.getApplicationContext());
            }
        }
        return _instance;
    }

    public void playAudioOnLine(String url) {
        this.url = url;
        if (TextUtils.isEmpty(url)) {
            notifyStatus(url, url, 7, 0);
        } else if (this.isPause) {
            pauseToStart();
        } else {
            startAudio(url);
        }
    }

    public void playAudio(final String audioUrl) {
        if (TextUtils.isEmpty(audioUrl)) {
            notifyStatus(audioUrl, audioUrl, 7, 0);
        } else if (this.isPause) {
            pauseToStart();
        } else {
            try {
                stopAudio();
                downFile(audioUrl, new DownListener() {
                    public void onFileLoaded(String path, int status) {
                        if (status == 4 || TextUtils.isEmpty(path)) {
                            MCAudioUtils.this.notifyStatus(audioUrl, path, 7, 0);
                            return;
                        }
                        MCAudioUtils.this.filePath = path;
                        MCAudioUtils.this.url = audioUrl;
                        MCAudioUtils.this.startAudio(path);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void playAudio(List<String> playList) {
        if (this.isPause) {
            pauseToStart();
        } else {
            this.isSoundList = true;
            if (playList == null || playList.isEmpty()) {
                notifyStatus(this.url, this.filePath, 7, 0);
            } else {
                this.playList.clear();
                this.playList.addAll(playList);
                playAudio((String) playList.get(0));
            }
        }
    }

    public synchronized void stopAudio() {
        if (!this.isPause) {
            stopListenPlay();
            if (this.downThread != null && this.downThread.isAlive()) {
                this.downThread.interrupt();
            }
            if (this.mp != null) {
                this.mp.stop();
                this.mp.release();
                this.mp = null;
            }
            notifyStatus(this.url, this.filePath, 6, 100);
        }
    }

    public synchronized void pauseAudio() {
        if (this.mp != null) {
            if (this.mp.isPlaying()) {
                this.mp.isPlaying();
                this.mp.pause();
                this.isPause = true;
            }
            notifyStatus(this.url, this.filePath, 3, 0);
            stopListenPlay();
        }
    }

    public void phonePauseAudio() {
        if (this.mp != null && this.mp.isPlaying()) {
            pauseAudio();
        }
    }

    public void phonePlayAudio() {
        pauseToStart();
    }

    public void downFile(String uri, DownListener downListener) {
        this.url = uri;
        if (uri.startsWith("http")) {
            this.filePath = this.baseDir + getHash(uri);
        } else {
            this.filePath = uri;
        }
        if (isFileExist(this.filePath)) {
            notifyDown(downListener, this.filePath, 5);
        } else if (uri.startsWith("http")) {
            this.downThread = new DownThread(downListener, uri);
            this.downThread.start();
        } else {
            notifyDown(downListener, uri, 4);
        }
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        if (!file.exists() || file.length() <= 0) {
            return false;
        }
        return true;
    }

    public void notifyDown(DownListener downListener, String path, int status) {
        if (downListener != null) {
            downListener.onFileLoaded(path, status);
        }
    }

    public static String getHash(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            return new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return url;
        }
    }

    private MediaPlayer startAudio(String path) {
        if (this.mp == null) {
            this.mp = new MediaPlayer();
            this.mp.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    if (mp != null) {
                        MCAudioUtils.this.isPause = false;
                        MCAudioUtils.this.notifyStatus(MCAudioUtils.this.url, MCAudioUtils.this.filePath, 2, 0);
                        mp.start();
                        MCAudioUtils.this.listenPlay();
                    }
                }
            });
            this.mp.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.e(MCAudioUtils.this.TAG, "============播放结束===========" + MCAudioUtils.this.url);
                    MCAudioUtils.this.notifyStatus(MCAudioUtils.this.url, MCAudioUtils.this.filePath, 6, 0);
                    if (MCAudioUtils.this.isSoundList) {
                        MCAudioUtils.this.playList.remove(0);
                        MCAudioUtils.this.stopAudio();
                        if (MCAudioUtils.this.playList.isEmpty()) {
                            MCAudioUtils.this.isSoundList = false;
                            return;
                        } else {
                            MCAudioUtils.this.playAudio((String) MCAudioUtils.this.playList.get(0));
                            return;
                        }
                    }
                    MCAudioUtils.this.isPause = false;
                    MCAudioUtils.this.stopAudio();
                }
            });
            this.mp.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    MCAudioUtils.this.notifyStatus(MCAudioUtils.this.url, MCAudioUtils.this.filePath, 7, 0);
                    MCAudioUtils.this.stopAudio();
                    return false;
                }
            });
            try {
                this.mp.setDataSource(path);
                this.mp.prepareAsync();
                notifyStatus(this.url, this.filePath, 1, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            stopAudio();
        }
        return this.mp;
    }

    private synchronized void pauseToStart() {
        if (this.mp != null && this.isPause) {
            this.isPause = false;
            this.mp.start();
            listenPlay();
        }
    }

    private synchronized void notifyStatus(String url, String filePath, int status, int percent) {
        if (this.audioListener != null) {
            Message msg = new Message();
            msg.what = 2;
            Obj obj = new Obj();
            obj.url = url;
            obj.filePath = filePath;
            obj.status = status;
            obj.percent = percent;
            msg.obj = obj;
            this.handler2.sendMessage(msg);
        }
    }

    private void listenPlay() {
        if (this.task == null) {
            this.task = new TimerTask() {
                public synchronized void run() {
                    MCAudioUtils.this.percent = 0;
                    if (MCAudioUtils.this.mp != null && MCAudioUtils.this.mp.isPlaying()) {
                        MCAudioUtils.this.percent = (MCAudioUtils.this.mp.getCurrentPosition() * 100) / MCAudioUtils.this.mp.getDuration();
                        MCAudioUtils.this.notifyStatus(MCAudioUtils.this.url, MCAudioUtils.this.filePath, 2, MCAudioUtils.this.percent);
                    }
                }
            };
        }
        if (this.timer == null) {
            this.timer = new Timer();
        }
        this.timer.schedule(this.task, 0, 200);
    }

    public void stopListenPlay() {
        Log.e(this.TAG, "======stopListenPlay===========");
        this.handler2.removeMessages(2);
        if (this.task != null) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.timer.purge();
            this.timer.cancel();
            this.timer = null;
            this.task = null;
        }
    }

    public void release() {
        this.isPause = false;
        this.isSoundList = false;
        stopAudio();
        if (this.mp != null) {
            this.mp.release();
        }
        if (this.timer != null) {
            if (this.task != null) {
                this.task.cancel();
            }
            this.timer.purge();
            this.timer.cancel();
            this.timer = null;
            this.task = null;
        }
    }

    public boolean isPlaying() {
        if (this.mp != null) {
            return this.mp.isPlaying();
        }
        return false;
    }

    public String getCurrentAudioUrl() {
        return this.url;
    }
}
