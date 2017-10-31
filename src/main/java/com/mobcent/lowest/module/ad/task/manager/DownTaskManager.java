package com.mobcent.lowest.module.ad.task.manager;

import android.content.Context;
import android.os.AsyncTask.Status;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import com.mobcent.lowest.module.ad.task.DownTask;
import java.util.HashMap;
import java.util.Map;

public class DownTaskManager {
    private static Map<String, DownTask> downloadTaskMap;
    private static DownTaskManager manager;
    public String TAG = "DownloadTaskManager";

    public static DownTaskManager getInastance() {
        if (manager == null) {
            manager = new DownTaskManager();
            downloadTaskMap = new HashMap();
        }
        return manager;
    }

    public synchronized void downloadApk(Context context, AdDownDbModel adDownDbModel) {
        if (adDownDbModel != null) {
            String key = MCStringUtil.getMD5Str(adDownDbModel.getUrl());
            if (downloadTaskMap.get(key) == null || ((DownTask) downloadTaskMap.get(key)).getStatus() != Status.RUNNING) {
                if (downloadTaskMap.get(key) == null || ((DownTask) downloadTaskMap.get(key)).getStatus() == Status.FINISHED) {
                    if (downloadTaskMap.get(key) != null) {
                        downloadTaskMap.remove(key);
                    }
                    DownTask task = new DownTask(context, adDownDbModel);
                    downloadTaskMap.put(key, task);
                    task.execute(new Void[0]);
                } else {
                    ((DownTask) downloadTaskMap.get(key)).execute(new Void[0]);
                }
            }
        }
    }

    public void removeTask(String downloadUrl) {
        try {
            String key = MCStringUtil.getMD5Str(downloadUrl);
            if (downloadTaskMap.get(key) != null) {
                downloadTaskMap.remove(key);
            }
        } catch (Exception e) {
        }
    }

    public boolean isHaveTaskRunning() {
        for (String key : downloadTaskMap.keySet()) {
            DownTask task = (DownTask) downloadTaskMap.get(key);
            if (task != null && task.getStatus() == Status.RUNNING) {
                return true;
            }
        }
        return false;
    }
}
