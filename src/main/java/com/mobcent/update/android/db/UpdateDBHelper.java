package com.mobcent.update.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mobcent.update.android.db.constant.UpdateDBConstant;

public class UpdateDBHelper extends SQLiteOpenHelper implements UpdateDBConstant {
    public UpdateDBHelper(Context context) {
        super(context, UpdateDBConstant.DBNAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS update_status (updateId LONG PRIMARY KEY, update_status INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS download_table (id integer primary key autoincrement, downpath varchar(100), postionid INTEGER, downlength INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS update_status");
        db.execSQL("DROP TABLE IF EXISTS download_table");
        onCreate(db);
    }
}
