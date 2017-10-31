package com.mobcent.discuz.android.os;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.HeartBeatModel;
import com.mobcent.discuz.android.os.helper.HeartBeatPushHelper;
import com.mobcent.discuz.android.os.helper.MsgManageHelper;
import com.mobcent.discuz.android.service.HeartBeatService;
import com.mobcent.discuz.android.service.impl.HeartBeatServiceImpl;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.lowest.base.utils.MCActivityUtils;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class HeartBeatOSService extends Service {
    public static final String AT_NOTICE_NUM = "at_notice_num";
    public static final String INTENT_HEART_ICON = "icon";
    public static final String INTENT_HEART_STATUS = "status";
    public static final String MSG_NOTICE_NUM = "msg_notice_num";
    public static final int PM_LIMIT_10 = 10;
    public static final String PUSH_NOTICE_NUM = "push_notice_num";
    public static final String REPLY_NOTICE_NUM = "reply_notice_num";
    public static final String SERVER_NOTIFICATION_MSG = ".forum.service.heart.beat.notify";
    public static final int STATUS_IN_PM = 2;
    public static final int STATUS_OUT_PM = 1;
    public final int HEART_BEAT_SPLIT_LONG = 300000;
    public final int HEART_BEAT_TIME_OUT_10 = 600000;
    public final int HEART_INTERVAL = BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
    public final int HEART_PUSH_SPLIT_LONG = 1800000;
    public final int HEART_PUSH_SPLIT_SHORT = 60000;
    public final String TAG = "HeartBeatOSService";
    private HeartBeatService heartMsgService;
//    private HeartPushTrhead heartPushThread;
    private HeartThread heartThread;
    private int iconId = 0;
    private boolean isInMsgRoom = false;
    private boolean isOpenMonitorServer = true;
    private boolean isTimeOut;
    private long lastHeartBeatRequestTime = 0;
    private int status = 1;
    private long timeHeart;
    private long timePm;
    private long timePush;

//    private class HeartPushTrhead extends Thread {
//        private HeartPushTrhead() {
//        }
//
//        public void run() {
//            while (HeartBeatOSService.this.isOpenMonitorServer && HeartBeatOSService.this.heartPushThread == Thread.currentThread()) {
//                try {
//                    HeartBeatOSService.this.dealPush();
//                    if (HeartBeatOSService.this.isTimeOut) {
//                        HeartBeatOSService.this.timePush = 1800000;
//                    } else {
//                        HeartBeatOSService.this.timePush = 60000;
//                    }
//                    Thread.sleep(HeartBeatOSService.this.timePush);
//                } catch (InterruptedException inte) {
//                    inte.printStackTrace();
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return;
//                }
//            }
//        }
//    }

    private class HeartThread extends Thread {
        private HeartThread() {
        }

        public void run() {
            while (HeartBeatOSService.this.isOpenMonitorServer && HeartBeatOSService.this.heartThread == Thread.currentThread()) {
                try {
                    if (MCActivityUtils.isAction(HeartBeatOSService.this.getApplicationContext())) {
                        HeartBeatOSService.this.lastHeartBeatRequestTime = 0;
                    } else if (HeartBeatOSService.this.lastHeartBeatRequestTime == 0) {
                        HeartBeatOSService.this.lastHeartBeatRequestTime = MCDateUtil.getCurrentTime();
                    }
                    if (HeartBeatOSService.this.lastHeartBeatRequestTime <= 0 || MCDateUtil.getCurrentTime() - HeartBeatOSService.this.lastHeartBeatRequestTime < 600000) {
                        HeartBeatOSService.this.isTimeOut = false;
                    } else {
                        HeartBeatOSService.this.timeHeart = 300000;
                        HeartBeatOSService.this.isTimeOut = true;
                    }
                    HeartBeatOSService.this.dealHeart();
                    Thread.sleep(HeartBeatOSService.this.timeHeart);
                } catch (InterruptedException inte) {
                    inte.printStackTrace();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void setOpenMonitorServer(boolean isOpenMonitorServer) {
        this.isOpenMonitorServer = isOpenMonitorServer;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        initService();
    }

    private void initService() {
        this.heartMsgService = new HeartBeatServiceImpl(this);
        this.heartThread = new HeartThread();
//        this.heartPushThread = new HeartPushTrhead();
        this.timeHeart = 20000;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            this.iconId = intent.getIntExtra("icon", 0);
            this.status = intent.getIntExtra("status", -1);
        }
        startService();
        return super.onStartCommand(intent, flags, startId);
    }

    private void inMsgRoomChange(boolean isInRoom) {
        if (isInRoom) {
            if (this.timePm < 2000) {
                this.timePm = 2000;
            }
            this.isInMsgRoom = true;
            this.timeHeart = this.timePm;
            startTwoThread();
            return;
        }
        this.timeHeart = 20000;
        this.isInMsgRoom = false;
        startTwoThread();
    }

    private synchronized void startService() {
        if (this.status == 2) {
            inMsgRoomChange(true);
        } else if (this.status == 1) {
            inMsgRoomChange(false);
        } else if (this.timeHeart == 300000 && this.heartThread.isAlive()) {
            startTwoThread();
        } else if (!this.heartThread.isAlive()) {
            try {
                this.heartThread.start();
//                this.heartPushThread.start();
            } catch (Exception e) {
            }
        }
    }

    public synchronized void startTwoThread() {
        this.isTimeOut = false;
        if (this.heartThread != null && this.heartThread.isAlive()) {
            this.heartThread.interrupt();
        }
//        if (this.heartPushThread != null && this.heartPushThread.isAlive()) {
//            this.heartPushThread.interrupt();
//        }
        this.heartThread = new HeartThread();
//        this.heartPushThread = new HeartPushTrhead();
        this.heartThread.start();
//        this.heartPushThread.start();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void setPmPeriod(long pmPeriod) {
        this.timePm = pmPeriod;
    }

    private void setHeartBeatTime(long hBTime) {
        if (!this.isTimeOut && hBTime > 0) {
            if (this.isInMsgRoom) {
                this.timeHeart = this.timePm;
            } else {
                this.timeHeart = hBTime;
            }
        }
    }

    private void dealHeart() {
        if (new UserServiceImpl(getApplicationContext()).isLogin()) {
            BaseResultModel<HeartBeatModel> result = this.heartMsgService.getHeartBeatModel();
            HeartBeatModel heartModel = (HeartBeatModel) result.getData();
            if (result.getRs() != 0 && heartModel != null) {
                setPmPeriod(heartModel.getPmPeriod());
                setHeartBeatTime(heartModel.getHeartTime());
                sendNotify(heartModel);
            }
        }
    }

    private void dealPush() {
        HeartBeatPushHelper.dealPush(getApplicationContext(), this.heartMsgService.getHeartPushModel(SharedPreferencesDB.getInstance(getApplicationContext()).getUserId()));
    }

    private void sendNotify(HeartBeatModel heartModel) {
        MsgManageHelper.getInstance(getApplicationContext()).dealMsgFromHeart(heartModel);
    }
}
