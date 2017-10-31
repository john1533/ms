package com.mobcent.lowest.base.service.impl;

import android.content.Context;
import com.mobcent.lowest.base.service.FileTransferService;
import com.mobcent.lowest.base.utils.MCHttpClientUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTransferServiceImpl implements FileTransferService {
    private Context context;

    public FileTransferServiceImpl(Context context) {
        this.context = context;
    }

    public void downloadFile(String url, File targetFile) {
        MCHttpClientUtil.downloadFile(url, targetFile, this.context);
    }

    public void copyFile(String sourcePath, String destPath) {
        Exception e;
        Throwable th;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.length() > 0) {
            File destFile = new File(destPath);
            if (destFile.exists()) {
                destFile.delete();
            }
            FileInputStream is = null;
            FileOutputStream os = null;
            try {
                destFile.createNewFile();
                FileInputStream is2 = new FileInputStream(sourceFile);
                try {
                    FileOutputStream os2 = new FileOutputStream(destFile);
                    try {
                        byte[] b = new byte[512];
                        while (true) {
                            int len = is2.read(b);
                            if (len == -1) {
                                break;
                            }
                            os2.write(b, 0, len);
                            os2.flush();
                        }
                        if (is2 != null) {
                            try {
                                is2.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (os2 != null) {
                            os2.close();
                            os = os2;
                            is = is2;
                            return;
                        }
                        os = os2;
                        is = is2;
                    } catch (Exception e3) {
                        os = os2;
                        is = is2;
                    } catch (Throwable th2) {
                        th = th2;
                        os = os2;
                        is = is2;
                    }
                } catch (Exception e4) {
                    is = is2;
                    try {
                        e4.printStackTrace();
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception e22) {
                                e22.printStackTrace();
                                return;
                            }
                        }
                        if (os != null) {
                            os.close();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Exception e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (os != null) {
                            os.close();
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    is = is2;
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }
            } catch (Exception e5) {
                e5.printStackTrace();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    public byte[] getImageStream(String url) {
        return MCHttpClientUtil.getFileInByte(url, this.context);
    }
}
