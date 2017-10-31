package com.mobcent.discuz.android.observer;

public class ObserverHelper {
    public static final int MSG_AT = 1;
    public static final int MSG_CHAT = 3;
    public static final int MSG_FRIEND = 4;
    public static final int MSG_REPLY = 2;
    private static ObserverHelper helper;
    private ActivityObservable activityObservable = new ActivityObservable();

    private ObserverHelper() {
    }

    public static synchronized ObserverHelper getInstance() {
        ObserverHelper observerHelper;
        synchronized (ObserverHelper.class) {
            if (helper == null) {
                helper = new ObserverHelper();
            }
            observerHelper = helper;
        }
        return observerHelper;
    }

    public ActivityObservable getActivityObservable() {
        return this.activityObservable;
    }
}
