package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.TopTopicDBConstant;
import com.mobcent.lowest.base.utils.MCDateUtil;

public class TopTopicDBUtil extends BaseDBUtil implements TopTopicDBConstant {
    private static TopTopicDBUtil topTopicDBUtil;

    public boolean updateTopTopicJsonString(java.lang.String strJson, long idValue) {
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/

        this.openWriteableDB();
        SQLiteDatabase db =  this.writableDatabase;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,idValue);
        values.put(COLUMN_JSON_STR,strJson);
        values.put(COLUMN_UPDATE_TIME, MCDateUtil.getCurrentTime());

        boolean isRowExist = this.isRowExisted(db,TABLE_TOP_TOPIC,COLUMN_ID,idValue);
        if(!isRowExist){
            db.insertOrThrow(TABLE_TOP_TOPIC,null,values);
        }else{
            StringBuilder where = new StringBuilder("id=");
            where.append(idValue);
            db.update(TABLE_TOP_TOPIC,values,where.toString(),null);
        }
        this.closeWriteableDB();
        return true;
        /*
        r8 = this;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r7 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r7.<init>();	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0 = "id";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r1 = java.lang.Long.valueOf(r10);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r7.put(r0, r1);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0 = "jsonStr";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r7.put(r0, r9);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0 = "update_time";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r1 = com.mobcent.lowest.base.utils.MCDateUtil.getCurrentTime();	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r7.put(r0, r1);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r1 = r8.writableDatabase;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r2 = "top_topic";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r3 = "id";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0 = r8;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r4 = r10;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        if (r0 != 0) goto L_0x003e;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
    L_0x0031:
        r0 = r8.writableDatabase;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r1 = "top_topic";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r2 = 0;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0.insertOrThrow(r1, r2, r7);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
    L_0x0039:
        r8.closeWriteableDB();
        r0 = 1;
    L_0x003d:
        return r0;
    L_0x003e:
        r0 = r8.writableDatabase;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r1 = "top_topic";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r3 = "id=";	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r2 = r2.append(r10);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r3 = 0;	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r0.update(r1, r7, r2, r3);	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        goto L_0x0039;
    L_0x0056:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ Exception -> 0x0056, all -> 0x005f }
        r8.closeWriteableDB();
        r0 = 0;
        goto L_0x003d;
    L_0x005f:
        r0 = move-exception;
        r8.closeWriteableDB();
        throw r0;
        */
    }

    public TopTopicDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized TopTopicDBUtil getInstance(Context context) {
        synchronized (TopTopicDBUtil.class) {
            if (topTopicDBUtil == null) {
                topTopicDBUtil = new TopTopicDBUtil(context);
            }
        }
        return topTopicDBUtil;
    }

    public String getTopTopicJsonString(long boardId) {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.query(TopTopicDBConstant.TABLE_TOP_TOPIC, null, "id=" + boardId, null, null, null, null);
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
        return removeAllEntries(TopTopicDBConstant.TABLE_TOP_TOPIC);
    }
}
