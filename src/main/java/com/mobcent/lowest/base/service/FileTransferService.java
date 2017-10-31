package com.mobcent.lowest.base.service;

import java.io.File;

public interface FileTransferService {
    void copyFile(String str, String str2);

    void downloadFile(String str, File file);

    byte[] getImageStream(String str);
}
