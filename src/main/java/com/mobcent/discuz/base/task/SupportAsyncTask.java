package com.mobcent.discuz.base.task;

import android.content.Context;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;

public class SupportAsyncTask extends BaseTask<BaseResultModel<Object>> {
    private long pid;
    private PostsService postsService;
    private long tid;
    private String type;

    public SupportAsyncTask(Context context, long tid, long pid, String type, BaseRequestCallback<BaseResultModel<Object>> _callback) {
        super(context, _callback);
        this.tid = tid;
        this.pid = pid;
        this.type = type;
        this.postsService = new PostsServiceImpl(context.getApplicationContext());
    }

    protected BaseResultModel<Object> doInBackground(Void... params) {
        return this.postsService.support(this.tid, this.pid, this.type);
    }
}
