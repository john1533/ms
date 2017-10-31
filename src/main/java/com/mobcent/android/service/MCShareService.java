package com.mobcent.android.service;

import android.content.Context;
import com.mobcent.android.model.MCShareSiteModel;
import java.util.List;

public interface MCShareService {
    List<MCShareSiteModel> getAllSites(String str, String str2, String str3, String str4);

    List<MCShareSiteModel> getAllSitesByLocal(String str, String str2, String str3, String str4);

    List<MCShareSiteModel> getAllSitesByNet(String str, String str2, String str3, String str4);

    String shareInfo(long j, String str, String str2, String str3, String str4, String str5, String str6);

    void shareLog(String str, String str2, String str3, Context context);

    boolean unbindSite(long j, int i, String str, String str2);

    String uploadImage(long j, String str, String str2, String str3);
}
