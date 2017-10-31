package com.mobcent.lowest.module.ad.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.mobcent.lowest.module.ad.db.constant.AdDbConstant;
import com.mobcent.lowest.module.ad.model.AdDownDbModel;
import java.util.ArrayList;
import java.util.List;

public class DownSqliteHelper extends BaseSqliteOpenHelper {
    private static DownSqliteHelper helper;
    private Object _lock = new Object();

    public static DownSqliteHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DownSqliteHelper(context.getApplicationContext());
        }
        return helper;
    }

    public DownSqliteHelper(Context context) {
        super(context);
    }

    public synchronized void insert(AdDownDbModel model) {
        getWritableDatabase().insert(AdDbConstant.DOWNLOAD_TABLE, null, parseContentValues(model));
        close();
    }

    public synchronized void update(AdDownDbModel model) {
        getWritableDatabase().update(AdDbConstant.DOWNLOAD_TABLE, parseContentValues(model), "download_url = ? ", new String[]{model.getUrl()});
        close();
    }

    public synchronized AdDownDbModel query(String url) {
        AdDownDbModel adDownDbModel = null;
        synchronized (this) {
            if (url != null) {
                if (!url.equals("")) {
                    adDownDbModel = null;
                    try {
                        Cursor c = getReadableDatabase().query(AdDbConstant.DOWNLOAD_TABLE, null, "download_url = ? ", new String[]{url}, null, null, null);
                        while (c.moveToNext()) {
                            if (c.getString(c.getColumnIndex(AdDbConstant.DOWNLOAD_URL)).equals(url)) {
                                adDownDbModel = parseAdDownDbModel(c);
                                break;
                            }
                        }
                        c.close();
                        close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return adDownDbModel;
    }

    public synchronized AdDownDbModel queryByPackageName(String packageName) {
        AdDownDbModel adDownDbModel;
        adDownDbModel = null;
        try {
            Cursor c = getReadableDatabase().query(AdDbConstant.DOWNLOAD_TABLE, null, "download_pn = ? ", new String[]{packageName}, null, null, null);
            while (c.moveToNext()) {
                if (c.getString(c.getColumnIndex(AdDbConstant.DOWNLOAD_PN)).equals(packageName)) {
                    adDownDbModel = parseAdDownDbModel(c);
                    break;
                }
            }
            c.close();
            close();
        } catch (Exception e) {
        }
        return adDownDbModel;
    }

    public List<AdDownDbModel> queryUnDownDoList() {
        List<AdDownDbModel> adDownModels = new ArrayList();
        try {
            Cursor c = getReadableDatabase().query(AdDbConstant.DOWNLOAD_TABLE, null, "downloadDo = ? and download_status = ? ", new String[]{String.valueOf(2), String.valueOf(2)}, null, null, null);
            while (c.moveToNext()) {
                AdDownDbModel adDownDbModel = new AdDownDbModel();
                adDownModels.add(parseAdDownDbModel(c));
            }
            c.close();
            close();
        } catch (Exception e) {
        }
        return adDownModels;
    }

    public List<AdDownDbModel> queryUnActiveDoList() {
        List<AdDownDbModel> adDownModels = new ArrayList();
        try {
            Cursor c = getReadableDatabase().query(AdDbConstant.DOWNLOAD_TABLE, null, "activeDo = ?  and download_status = ? ", new String[]{String.valueOf(2), String.valueOf(2)}, null, null, null);
            while (c.moveToNext()) {
                AdDownDbModel adDownDbModel = new AdDownDbModel();
                adDownModels.add(parseAdDownDbModel(c));
            }
            c.close();
            close();
        } catch (Exception e) {
        }
        return adDownModels;
    }

    private ContentValues parseContentValues(AdDownDbModel model) {
        ContentValues values = new ContentValues();
        values.put(AdDbConstant.DOWNLOAD_AID, Long.valueOf(model.getAid()));
        values.put(AdDbConstant.DOWNLOAD_URL, model.getUrl());
        values.put(AdDbConstant.DOWNLOAD_PN, model.getPn());
        values.put(AdDbConstant.DOWNLOAD_CURRENT_PN, model.getCurrentPn());
        values.put(AdDbConstant.DOWNLOAD_PO, Integer.valueOf(model.getPo()));
        values.put(AdDbConstant.DOWNLOAD_DATE, model.getDate());
        values.put(AdDbConstant.DOWNLOAD_STATUS, Integer.valueOf(model.getStatus()));
        values.put("appName", model.getAppName());
        values.put(AdDbConstant.DOWNLOAD_IS_DOWNLOAD_DO, Integer.valueOf(model.getDownloadDo()));
        values.put(AdDbConstant.DOWNLOAD_IS_ACTIVE_DO, Integer.valueOf(model.getActiveDo()));
        return values;
    }

    private AdDownDbModel parseAdDownDbModel(Cursor c) {
        AdDownDbModel adDownDbModel = new AdDownDbModel();
        adDownDbModel.setId(c.getInt(c.getColumnIndex(AdDbConstant.DOWNLOAD_ID)));
        adDownDbModel.setAid(c.getLong(c.getColumnIndex(AdDbConstant.DOWNLOAD_AID)));
        adDownDbModel.setUrl(c.getString(c.getColumnIndex(AdDbConstant.DOWNLOAD_URL)));
        adDownDbModel.setPn(c.getString(c.getColumnIndex(AdDbConstant.DOWNLOAD_PN)));
        adDownDbModel.setCurrentPn(c.getString(c.getColumnIndex(AdDbConstant.DOWNLOAD_CURRENT_PN)));
        adDownDbModel.setPo(c.getInt(c.getColumnIndex(AdDbConstant.DOWNLOAD_PO)));
        adDownDbModel.setDate(c.getString(c.getColumnIndex(AdDbConstant.DOWNLOAD_DATE)));
        adDownDbModel.setStatus(c.getInt(c.getColumnIndex(AdDbConstant.DOWNLOAD_STATUS)));
        adDownDbModel.setAppName(c.getString(c.getColumnIndex("appName")));
        adDownDbModel.setDownloadDo(c.getInt(c.getColumnIndex(AdDbConstant.DOWNLOAD_IS_DOWNLOAD_DO)));
        adDownDbModel.setActiveDo(c.getInt(c.getColumnIndex(AdDbConstant.DOWNLOAD_IS_ACTIVE_DO)));
        return adDownDbModel;
    }
}
