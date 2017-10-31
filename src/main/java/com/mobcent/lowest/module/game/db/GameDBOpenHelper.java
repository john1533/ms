package com.mobcent.lowest.module.game.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mobcent.lowest.module.game.db.constant.GameDbSqlConstant;

public class GameDBOpenHelper extends SQLiteOpenHelper {
    private String tagName;

    public GameDBOpenHelper(Context context, String databaseName, int version, String tagName) {
        super(context, databaseName, null, version);
        this.tagName = tagName;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GameDbSqlConstant.RECOMMEND_TABLE_SQL);
        db.execSQL(GameDbSqlConstant.LATEST_TABLE_SQL);
        db.execSQL(GameDbSqlConstant.MY_GAME_TABLE_SQL);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }
}
