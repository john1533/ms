package com.mobcent.discuz.module.person.activity.task;

import android.content.Context;
import android.os.AsyncTask;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.module.person.activity.delegate.TaskExecuteDelegate;

public class UserLogoutAsyncTaskLoader extends AsyncTask<Void, Void, BaseResultModel> {
    private Context context;
    private TaskExecuteDelegate taskExecuteDelegate;
    private UserService userService;

    public UserLogoutAsyncTaskLoader(Context context) {
        this.context = context;
        this.userService = new UserServiceImpl(context);
    }

    protected BaseResultModel doInBackground(Void... params) {
        return this.userService.logoutUser("logout");
    }

    protected void onPostExecute(BaseResultModel result) {
        super.onPostExecute(result);
        DZToastAlertUtils.toast(this.context, result);
        if (result.getRs() == 1) {
            if (this.taskExecuteDelegate != null) {
                this.taskExecuteDelegate.executeSuccess("");
            }
        } else if (this.taskExecuteDelegate != null) {
            this.taskExecuteDelegate.executeFail();
        }
    }

    public TaskExecuteDelegate getTaskExecuteDelegate() {
        return this.taskExecuteDelegate;
    }

    public void setTaskExecuteDelegate(TaskExecuteDelegate taskExecuteDelegate) {
        this.taskExecuteDelegate = taskExecuteDelegate;
    }
}
