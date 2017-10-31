package com.mobcent.lowest.module.plaza.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.mobcent.lowest.module.plaza.db.constant.PlazaDbColumnConstant;

public class PlazaDbHelper extends SQLiteOpenHelper implements PlazaDbColumnConstant {
    private SQLiteDatabase sqLiteDatabase;

    public PlazaDbHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    public PlazaDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlazaDbColumnConstant.CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PlazaDbColumnConstant.DROP_TABLE);
        onCreate(db);
    }

    public String getJsonStr() {
        String jsonStr = null;
        try {
            this.sqLiteDatabase = getReadableDatabase();
            Cursor cursor = this.sqLiteDatabase.query(PlazaDbColumnConstant.TABLE_NAME, null, null, null, null, null, null);
            if (cursor == null) {
                return null;
            }
            while (cursor.moveToNext()) {
                jsonStr = cursor.getString(cursor.getColumnIndex("jsonStr"));
                if (jsonStr != null) {
                    break;
                }
            }
            cursor.close();
            this.sqLiteDatabase.close();
            this.sqLiteDatabase = null;
            return jsonStr;
        } catch (Exception e) {
            return null;
        }
    }

    public void setJsonStr(String jsonStr) {
        try {
            this.sqLiteDatabase = getWritableDatabase();
            this.sqLiteDatabase.delete(PlazaDbColumnConstant.TABLE_NAME, "1 = 1", null);
            ContentValues values = new ContentValues();
            values.put("jsonStr", jsonStr);
            this.sqLiteDatabase.insert(PlazaDbColumnConstant.TABLE_NAME, null, values);
            this.sqLiteDatabase.close();
            this.sqLiteDatabase = null;
        } catch (Exception e) {
        }
    }
}
