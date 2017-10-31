package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.EssenceTopicDBConstant;
import com.mobcent.lowest.base.utils.MCDateUtil;

public class EssenceTopicDBUtil extends BaseDBUtil implements EssenceTopicDBConstant {
    private static EssenceTopicDBUtil essenceTopicDBUtil;

    public boolean updateEssenceTopicJsonString(java.lang.String strJson, long idValue) {

        this.openWriteableDB();
        SQLiteDatabase db =  this.writableDatabase;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,idValue);
        values.put(COLUMN_JSON_STR,strJson);
        values.put(COLUMN_UPDATE_TIME, MCDateUtil.getCurrentTime());

        boolean isRowExist = this.isRowExisted(db,TABLE_ESSENCE_TOPIC,COLUMN_ID,idValue);
        if(!isRowExist){
            db.insertOrThrow(TABLE_ESSENCE_TOPIC,null,values);
        }else{
            StringBuilder where = new StringBuilder("id=");
            where.append(idValue);
            db.update(TABLE_ESSENCE_TOPIC,values,where.toString(),null);
        }
        this.closeWriteableDB();
        return true;
    }

    public EssenceTopicDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized EssenceTopicDBUtil getInstance(Context context) {
        synchronized (EssenceTopicDBUtil.class) {
            if (essenceTopicDBUtil == null) {
                essenceTopicDBUtil = new EssenceTopicDBUtil(context.getApplicationContext());
            }
        }
        return essenceTopicDBUtil;
    }

    public String getEssenceTopicJsonString(long boardId) {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.query(EssenceTopicDBConstant.TABLE_ESSENCE_TOPIC, null, "id=" + boardId, null, null, null, null);
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

    public boolean delteEssenceTopicList() {
        return removeAllEntries(EssenceTopicDBConstant.TABLE_ESSENCE_TOPIC);
    }
}
