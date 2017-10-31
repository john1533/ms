package com.mobcent.lowest.module.game.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.mobcent.lowest.module.game.db.constant.ContentCacheConstant;

public class ContentCacheDB implements ContentCacheConstant {
    private static ContentCacheDB contentCacheDB = null;
    protected Context context;
    private SharedPreferences prefs = null;

    private ContentCacheDB(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(ContentCacheConstant.CONTENT_FILE_NAME, 3);
    }

    public static ContentCacheDB getInstance(Context context) {
        if (contentCacheDB == null) {
            contentCacheDB = new ContentCacheDB(context);
        }
        return contentCacheDB;
    }

    public void setListLocal(String tagName, boolean isLocal) {
        Editor editor = this.prefs.edit();
        editor.putBoolean(tagName, isLocal);
        editor.commit();
    }

    public boolean getListLocal(String tagName) {
        return this.prefs.getBoolean(tagName, false);
    }

    public void saveRefreshTime(String tagName, long time) {
        Editor editor = this.prefs.edit();
        editor.putLong(tagName, time);
        editor.commit();
    }

    public long getRefreshTime(String tagName) {
        return this.prefs.getLong(tagName, 0);
    }
}
