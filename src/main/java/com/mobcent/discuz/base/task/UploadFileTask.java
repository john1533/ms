package com.mobcent.discuz.base.task;

import android.content.Context;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import java.util.List;

public class UploadFileTask extends BaseTask<BaseResultModel<List<UploadPictureModel>>> {
    private long albumId;
    private long fid;
    private String[] files;
    private String module;
    private PostsService postsService;
    private long sortId;
    private String type;

    public UploadFileTask(Context context, String[] files, String module, String type, long fid, long sortId, long albumId, BaseRequestCallback<BaseResultModel<List<UploadPictureModel>>> _callback) {
        super(context, _callback);
        this.context = context;
        this.module = module;
        this.type = type;
        this.fid = fid;
        this.sortId = sortId;
        this.albumId = albumId;
        this.files = files;
        this.postsService = new PostsServiceImpl(context);
    }

    protected BaseResultModel<List<UploadPictureModel>> doInBackground(Void... arg0) {
        return this.postsService.upload(this.files, this.module, this.type, this.fid, this.sortId, this.albumId);
    }
}
