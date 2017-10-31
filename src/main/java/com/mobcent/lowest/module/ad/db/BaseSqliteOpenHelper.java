package com.mobcent.lowest.module.ad.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.mobcent.lowest.module.ad.db.constant.AdDbConstant;

public class BaseSqliteOpenHelper extends SQLiteOpenHelper implements AdDbConstant {
    public BaseSqliteOpenHelper(Context context) {
        this(context, AdDbConstant.DATABASE_NAME, null, 1);
    }

    public BaseSqliteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AdDbConstant.CREATE_TABLE_DOWNLOAD);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AdDbConstant.DROP_TABLE_DOWNLOAD);
        onCreate(db);
    }
}
