package com.mobcent.android.util;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

public class RecordMp3Util {
    public static final int MSG_ERROR_AUDIO_ENCODE = 6;
    public static final int MSG_ERROR_AUDIO_RECORD = 5;
    public static final int MSG_ERROR_CLOSE_FILE = 8;
    public static final int MSG_ERROR_CREATE_FILE = 3;
    public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = 2;
    public static final int MSG_ERROR_REC_START = 4;
    public static final int MSG_ERROR_WRITE_FILE = 7;
    public static final int MSG_MAX_AMPLITUDE = 9;
    public static final int MSG_REC_STARTED = 0;
    public static final int MSG_REC_STOPPED = 1;
    public static final int RECORD_TYPE_AMR = 2;
    public static final int RECORD_TYPE_MP3 = 1;
    public static final float pi = 3.1415925f;
    private String mFilePath;
    private Handler mHandler;
    private boolean mIsRecording = false;
    private int mSampleRate;
    public MediaRecorder mediaRecorder;

    static {
        System.loadLibrary("mp3lame");
    }

    public RecordMp3Util(String filePath, int sampleRate) {
        if (sampleRate <= 0) {
            throw new InvalidParameterException("Invalid sample rate specified.");
        }
        this.mFilePath = filePath;
        this.mSampleRate = sampleRate;
    }

    public void start() {
        start(2);
    }

    public void start(int type) {
        if (!this.mIsRecording) {
            this.mIsRecording = true;
            switch (type) {
                case 1:
                    startRecordMp3();
                    return;
                case 2:
                    startRecording();
                    return;
                default:
                    return;
            }
        }
    }

    public void stop() {
        this.mIsRecording = false;
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }

    public void setHandle(Handler handler) {
        this.mHandler = handler;
    }

    public void startRecordMp3() {
        new Thread() {
            public void run() {
                Process.setThreadPriority(-19);
                int minBufferSize = AudioRecord.getMinBufferSize(RecordMp3Util.this.mSampleRate, 16, 2);
                if (minBufferSize < 0) {
                    if (RecordMp3Util.this.mHandler != null) {
                        RecordMp3Util.this.mHandler.sendEmptyMessage(2);
                    }
                    RecordMp3Util.this.mIsRecording = false;
                    return;
                }
                AudioRecord audioRecord = new AudioRecord(1, RecordMp3Util.this.mSampleRate, 16, 2, minBufferSize * 2);
                short[] buffer = new short[(((RecordMp3Util.this.mSampleRate * 2) * 1) * 5)];
                byte[] mp3buffer = new byte[((int) (7200.0d + (((double) (buffer.length * 2)) * 1.25d)))];
                try {
                    FileOutputStream output = new FileOutputStream(new File(RecordMp3Util.this.mFilePath));
                    SimpleLame.init(RecordMp3Util.this.mSampleRate, 1, RecordMp3Util.this.mSampleRate, 32);
                    try {
                        int flushResult;
                        audioRecord.startRecording();
                        if (RecordMp3Util.this.mHandler != null) {
                            RecordMp3Util.this.mHandler.sendEmptyMessage(0);
                        }
                        while (RecordMp3Util.this.mIsRecording) {
                            int readSize = audioRecord.read(buffer, 0, minBufferSize);
                            if (readSize < 0) {//cond_c below
                                if (RecordMp3Util.this.mHandler != null) {
                                    RecordMp3Util.this.mHandler.sendEmptyMessage(5);
                                }
                            } else if (readSize > 0) {
                                int encResult = SimpleLame.encode(buffer, buffer, readSize, mp3buffer);
                                if (encResult < 0) {//cond_d below
                                    if (RecordMp3Util.this.mHandler != null) {
                                        RecordMp3Util.this.mHandler.sendEmptyMessage(6);
                                    }
                                    break;
                                } else if (encResult != 0) {
                                    try {
                                        RecordMp3Util.this.covertFrequency(readSize, buffer);
                                        output.write(mp3buffer, 0, encResult);
                                    } catch (IOException e) {
                                        if (RecordMp3Util.this.mHandler != null) {
                                            RecordMp3Util.this.mHandler.sendEmptyMessage(7);
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            }else{//cond_4
                                continue;
                            }
//                            flushResult = SimpleLame.flush(mp3buffer);//cond_5
//                            if (flushResult < 0 && RecordMp3Util.this.mHandler != null) {
//                                RecordMp3Util.this.mHandler.sendEmptyMessage(6);
//                            }
//                            if (flushResult != 0) {
//                                try {
//                                    output.write(mp3buffer, 0, flushResult);
//                                } catch (IOException e2) {
//                                    e2.printStackTrace();
//                                    if (RecordMp3Util.this.mHandler != null) {
//                                        RecordMp3Util.this.mHandler.sendEmptyMessage(7);
//                                    }
//                                }
//                            }
//                            output.close();
//                            audioRecord.stop();
//                            audioRecord.release();
//                            SimpleLame.close();
//                            RecordMp3Util.this.mIsRecording = false;
//                            if (RecordMp3Util.this.mHandler != null) {
//                                RecordMp3Util.this.mHandler.sendEmptyMessage(1);
//                            }
                        }


                        flushResult = SimpleLame.flush(mp3buffer);
                        if(flushResult<0){
                            if (RecordMp3Util.this.mHandler != null) {
                                RecordMp3Util.this.mHandler.sendEmptyMessage(6);
                            }
                        }

//                        RecordMp3Util.this.mHandler.sendEmptyMessage(6);
                        if (flushResult != 0) {
                            output.write(mp3buffer, 0, flushResult);
                        }

                        try {
                            output.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();

                            if (RecordMp3Util.this.mHandler != null) {
                                RecordMp3Util.this.mHandler.sendEmptyMessage(8);
                            }
                        }
                        audioRecord.stop();
                        audioRecord.release();
                        SimpleLame.close();
                        RecordMp3Util.this.mIsRecording = false;
                        if (RecordMp3Util.this.mHandler != null) {
                            RecordMp3Util.this.mHandler.sendEmptyMessage(1);
                        }


                    } catch (IllegalStateException e4) {
                        e4.printStackTrace();
                        if (RecordMp3Util.this.mHandler != null) {
                            RecordMp3Util.this.mHandler.sendEmptyMessage(4);
                        }
                        SimpleLame.close();
                        RecordMp3Util.this.mIsRecording = false;
                    } catch (Throwable th) {
                        th.printStackTrace();
                        SimpleLame.close();
                        RecordMp3Util.this.mIsRecording = false;
                    }
                } catch (FileNotFoundException e5) {
                    e5.printStackTrace();
                    if (RecordMp3Util.this.mHandler != null) {
                        RecordMp3Util.this.mHandler.sendEmptyMessage(3);
                    }
                    RecordMp3Util.this.mIsRecording = false;
                }
            }
        }.start();
    }

    public void startRecording() {
        this.mediaRecorder = new MediaRecorder();
        if (VERSION.SDK_INT >= 8) {
            this.mediaRecorder.setAudioSamplingRate(8000);
        }
        this.mediaRecorder.setAudioSource(1);
        this.mediaRecorder.setOutputFormat(3);
        this.mediaRecorder.setOutputFile(this.mFilePath);
        this.mediaRecorder.setAudioEncoder(1);
        try {
            this.mediaRecorder.prepare();
        } catch (IOException e) {
            if (this.mediaRecorder != null) {
                this.mediaRecorder.reset();
                this.mediaRecorder.release();
                this.mediaRecorder = null;
            }
        }
        try {
            this.mediaRecorder.start();
        } catch (Exception e2) {
            if (this.mediaRecorder != null) {
                this.mediaRecorder.reset();
                this.mediaRecorder.release();
                this.mediaRecorder = null;
            }
        }
        if (this.mediaRecorder != null) {
            if (this.mHandler != null) {
                this.mHandler.sendEmptyMessage(0);
            }
            new Thread() {
                public void run() {
                    while (RecordMp3Util.this.mIsRecording) {
                        if (RecordMp3Util.this.mHandler != null) {
                            RecordMp3Util.this.mHandler.sendMessage(RecordMp3Util.this.mHandler.obtainMessage(9, 1, 1, Integer.valueOf(RecordMp3Util.this.mediaRecorder != null ? RecordMp3Util.this.mediaRecorder.getMaxAmplitude() : 0)));
                        }
                    }
                    try {
                        if (RecordMp3Util.this.mediaRecorder != null) {
                            RecordMp3Util.this.mediaRecorder.stop();
                            RecordMp3Util.this.mediaRecorder.release();
                            RecordMp3Util.this.mediaRecorder = null;
                            if (RecordMp3Util.this.mHandler != null) {
                                RecordMp3Util.this.mHandler.sendEmptyMessage(1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (this.mHandler != null) {
            this.mHandler.sendEmptyMessage(4);
            this.mIsRecording = false;
        }
    }

    private void covertFrequency(int readSize, short[] buffer) {
        int i = 0;
        int length = up2int(readSize);
        short[] tmpBuf = new short[length];
        System.arraycopy(buffer, 0, tmpBuf, 0, length);
        Complex[] complexs = new Complex[length];
        for (int i2 = 0; i2 < length; i2++) {
            complexs[i2] = new Complex(Short.valueOf(tmpBuf[i2]).doubleValue());
        }
        fft(complexs, length);
        if (this.mHandler != null) {
            Handler handler = this.mHandler;
            if (complexs[0] != null) {
                i = complexs[0].getIntValue();
            }
            this.mHandler.sendMessage(handler.obtainMessage(9, 1, 1, Integer.valueOf(i)));
        }
    }

    private int up2int(int iint) {
        int ret = 1;
        while (ret <= iint) {
            ret <<= 1;
        }
        return ret >> 1;
    }

    public void fft(Complex[] xin, int N) {
        int i;
        Complex w = new Complex();
        Complex t = new Complex();
        int N2 = N / 2;
        int f = N;
        int m = 1;
        while (true) {
            f /= 2;
            if (f == 1) {
                break;
            }
            m++;
        }
        int nm = N - 2;
        int j = N2;
        for (i = 1; i <= nm; i++) {
            if (i < j) {
                t = xin[j];
                xin[j] = xin[i];
                xin[i] = t;
            }
            int k = N2;
            while (j >= k) {
                j -= k;
                k /= 2;
            }
            j += k;
        }
        for (int L = 1; L <= m; L++) {
            int e2 = (int) Math.pow(2.0d, (double) L);
            int B = e2 / 2;
            for (j = 0; j < B; j++) {
                float p = 6.283185f / ((float) e2);
                w.real = Math.cos((double) (((float) j) * p));
                w.image = Math.sin((double) (((float) j) * p)) * -1.0d;
                for (i = j; i < N; i += e2) {
                    int ip = i + B;
                    t = xin[ip].cc(w);
                    xin[ip] = xin[i].cut(t);
                    xin[i] = xin[i].sum(t);
                }
            }
        }
    }
}
