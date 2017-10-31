package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.NewsTopicDBConstant;
import com.mobcent.lowest.base.utils.MCDateUtil;

public class NewTopicDBUtil extends BaseDBUtil implements NewsTopicDBConstant {
    private static NewTopicDBUtil newTopicDBUtil;

    public boolean updateNewsTopicJsonString(java.lang.String strJson, long idValue) {
        this.openWriteableDB();
        SQLiteDatabase db =  this.writableDatabase;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,idValue);
        values.put(COLUMN_JSON_STR,strJson);
        values.put(COLUMN_UPDATE_TIME, MCDateUtil.getCurrentTime());

        boolean isRowExist = this.isRowExisted(db,TABLE_NEWS_TOPIC,COLUMN_ID,idValue);
        if(!isRowExist){
            db.insertOrThrow(TABLE_NEWS_TOPIC,null,values);
        }else{
            StringBuilder where = new StringBuilder("id=");
            where.append(idValue);
            db.update(TABLE_NEWS_TOPIC,values,where.toString(),null);
        }
        this.closeWriteableDB();
        return true;
    }

    public NewTopicDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized NewTopicDBUtil getInstance(Context context) {
        synchronized (NewTopicDBUtil.class) {
            if (newTopicDBUtil == null) {
                newTopicDBUtil = new NewTopicDBUtil(context);
            }
        }
        return newTopicDBUtil;
    }

    public String getNewsTopicJsonString(long boardId) {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.query(NewsTopicDBConstant.TABLE_NEWS_TOPIC, null, "id=" + boardId, null, null, null, null);
            while (cursor.moveToNext()) {
                jsonStr = cursor.getString(1);
            }
        } catch (Exception e) {
            jsonStr = null;
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        }
        return jsonStr;
    }

    public boolean delteNewTopicList() {
        return removeAllEntries(NewsTopicDBConstant.TABLE_NEWS_TOPIC);
    }
}
