package com.mobcent.discuz.android.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import com.mobcent.discuz.android.service.impl.FeedBackServiceImpl;
import com.mobcent.lowest.base.utils.MCResource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    public static final boolean DEBUG = true;
    private static CrashHandler INSTANCE = null;
    private static final int PROJECT_TYPE_FORUM = 10;
    private static final String PROJECT_VERSION = "20150206442";
    public static String SERVER_VERSION = "";
    private static final String STACK_TRACE = "STACK_TRACE";
    public static final String TAG = "CrashHandler";
    private static final String VERSION_CODE = "versionCode";
    private static final String VERSION_NAME = "versionName";
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;
    private Properties mDeviceCrashInfo = new Properties();

    private CrashHandler() {
    }

    public static synchronized CrashHandler getInstance() {
        CrashHandler crashHandler;
        synchronized (CrashHandler.class) {
            if (INSTANCE == null) {
                INSTANCE = new CrashHandler();
            }
            crashHandler = INSTANCE;
        }
        return crashHandler;
    }

    public void init(Context ctx) {
        this.mContext = ctx;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.mDefaultHandler == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
            System.exit(10);
            return;
        }
        this.mDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        final StringBuffer buffer = stringWriter.getBuffer();
        ex.printStackTrace(writer);
        new Thread() {
            public void run() {
                new FeedBackServiceImpl().feedBack(CrashHandler.this.mContext, "{php-" + CrashHandler.SERVER_VERSION + "}\t" + "{Site-" + MCResource.getInstance(CrashHandler.this.mContext).getString("mc_discuz_base_request_url") + "}\t" + ex.toString(), buffer.toString(), 10, CrashHandler.PROJECT_VERSION);
                Process.killProcess(Process.myPid());
            }
        }.start();
        return false;
    }

    public boolean handleExceptionBySelf(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        final StringBuffer buffer = stringWriter.getBuffer();
        ex.printStackTrace(writer);
        new Thread() {
            public void run() {
                new FeedBackServiceImpl().feedBack(CrashHandler.this.mContext, "{AlreadyCatch}{php-" + CrashHandler.SERVER_VERSION + "}\t" + "{Site-" + MCResource.getInstance(CrashHandler.this.mContext).getString("mc_discuz_base_request_url") + "}\t" + ex.toString(), buffer.toString(), 10, CrashHandler.PROJECT_VERSION);
            }
        }.start();
        return false;
    }

    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                this.mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
                this.mDeviceCrashInfo.put("versionCode", Integer.valueOf(pi.versionCode));
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Error while collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.mDeviceCrashInfo.put(field.getName(), field.get(null));
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                Log.e(TAG, "Error while collect crash info", e2);
            }
        }
    }

    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        String result = info.toString();
        printWriter.close();
        this.mDeviceCrashInfo.put(STACK_TRACE, result);
        try {
            String fileName = "crash-" + System.currentTimeMillis() + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace = this.mContext.openFileOutput(fileName, 0);
            this.mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
            return null;
        }
    }

    private void sendCrashReportsToServer(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet();
            sortedFiles.addAll(Arrays.asList(crFiles));
            Iterator it = sortedFiles.iterator();
            while (it.hasNext()) {
                File cr = new File(ctx.getFilesDir(), (String) it.next());
                postReport(cr);
                cr.delete();
            }
        }
    }

    private String[] getCrashReportFiles(Context ctx) {
        return ctx.getFilesDir().list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(CrashHandler.CRASH_REPORTER_EXTENSION);
            }
        });
    }

    private void postReport(File file) {
    }

    public void sendPreviousReportsToServer() {
        sendCrashReportsToServer(this.mContext);
    }
}
