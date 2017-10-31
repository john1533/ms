package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.TopicDBConstant;
import com.mobcent.lowest.base.utils.MCDateUtil;

public class TopicDBUtil extends BaseDBUtil implements TopicDBConstant {
    private static TopicDBUtil topicDBUtil;

    public boolean updateTopicJsonString(java.lang.String strJson, long idValue) {

        this.openWriteableDB();
        SQLiteDatabase db =  this.writableDatabase;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,idValue);
        values.put(COLUMN_JSON_STR,strJson);
        values.put(COLUMN_UPDATE_TIME, MCDateUtil.getCurrentTime());

        boolean isRowExist = this.isRowExisted(db,TABLE_TOPIC,COLUMN_ID,idValue);
        if(!isRowExist){
            db.insertOrThrow(TABLE_TOPIC,null,values);
        }else{
            StringBuilder where = new StringBuilder("id=");
            where.append(idValue);
            db.update(TABLE_TOPIC,values,where.toString(),null);
        }
        this.closeWriteableDB();
        return true;
    }

    public TopicDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized TopicDBUtil getInstance(Context context) {
        synchronized (TopicDBUtil.class) {
            if (topicDBUtil == null) {
                topicDBUtil = new TopicDBUtil(context);
            }
        }
        return topicDBUtil;
    }

    public String getTopicJsonString(long boardId) {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.query("topic", null, "id=" + boardId, null, null, null, null);
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

    public boolean delteTopicList() {
        return removeAllEntries("topic");
    }
}
