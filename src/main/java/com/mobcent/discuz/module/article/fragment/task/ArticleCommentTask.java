package com.mobcent.discuz.module.article.fragment.task;

import android.content.Context;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.service.impl.ArticleServiceImpl;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.BaseTask;

public class ArticleCommentTask extends BaseTask<BaseResultModel<Object>> {
    private String commentJson;
    private Context context;

    public ArticleCommentTask(Context context, BaseRequestCallback<BaseResultModel<Object>> _callback, String commentJson) {
        super(context, _callback);
        this.context = context.getApplicationContext();
        this.commentJson = commentJson;
    }

    protected BaseResultModel<Object> doInBackground(Void... arg0) {
        return new ArticleServiceImpl(this.context).commentArticle(this.commentJson);
    }
}
