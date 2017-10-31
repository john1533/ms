package com.mobcent.android.service.impl;

import android.content.Context;
import com.mobcent.android.api.MCShareHttpClientUtil;
import com.mobcent.android.service.FileTransferService;
import java.io.File;

public class FileTransferServiceImpl implements FileTransferService {
    private Context context;

    public FileTransferServiceImpl(Context context) {
        this.context = context;
    }

    public void downloadFile(String url, File targetFile) {
        MCShareHttpClientUtil.downloadFile(url, targetFile, this.context);
    }
}
