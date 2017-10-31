package com.mobcent.android.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import com.mobcent.android.db.constant.SharedPreferencesDBConstant;

public class MCShareSharedPreferencesDB implements SharedPreferencesDBConstant {
    private static MCShareSharedPreferencesDB sharedPreferencesDB = null;
    private String PREFS_FILE = "mc_share.prefs";
    private SharedPreferences prefs = null;

    protected MCShareSharedPreferencesDB(Context cxt) {
        try {
            this.prefs = cxt.createPackageContext(cxt.getPackageName(), 2).getSharedPreferences(this.PREFS_FILE, 3);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MCShareSharedPreferencesDB getInstance(Context context) {
        if (sharedPreferencesDB == null) {
            sharedPreferencesDB = new MCShareSharedPreferencesDB(context);
        }
        return sharedPreferencesDB;
    }

    public void setShareAppContent(String appContent) {
        Editor editor = this.prefs.edit();
        editor.putString(SharedPreferencesDBConstant.SHARE_APP_CONTENT, appContent);
        editor.commit();
    }

    public void setShareNormalContent(String normalContent) {
        Editor editor = this.prefs.edit();
        editor.putString("content", normalContent);
        editor.commit();
    }

    public String getShareAppContent() {
        return this.prefs.getString(SharedPreferencesDBConstant.SHARE_APP_CONTENT, "");
    }

    public String getShareNormalContent() {
        return this.prefs.getString("content", "");
    }

    public void setShareUrl(String content) {
        Editor editor = this.prefs.edit();
        editor.putString("shareUrl", content);
        editor.commit();
    }

    public String getShareUrl() {
        return this.prefs.getString("shareUrl", "");
    }

    public void setSelectedSiteIds(String ids) {
        Editor editor = this.prefs.edit();
        editor.putString(SharedPreferencesDBConstant.SELECT_SITE_IDS, ids);
        editor.commit();
    }

    public String getSelectedSiteIds() {
        return this.prefs.getString(SharedPreferencesDBConstant.SELECT_SITE_IDS, "");
    }

    public void setAppIconUri(String uriStr) {
        Editor editor = this.prefs.edit();
        editor.putString(SharedPreferencesDBConstant.APP_ICON_URI, uriStr);
        editor.commit();
    }

    public String getAppIconUri() {
        return this.prefs.getString(SharedPreferencesDBConstant.APP_ICON_URI, "");
    }
}
