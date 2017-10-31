package com.mobcent.update.android.os.service.helper;

import android.content.Context;
import com.mobcent.update.android.task.UpdateCallback;
import com.mobcent.update.android.task.UpdateCheckTask;

public class UpdateCheckHelper {
    public static void check(Context context, UpdateCallback delegate) {
        new UpdateCheckTask(context, delegate).start();
    }
}
