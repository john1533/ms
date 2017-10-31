package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.mobcent.discuz.android.db.constant.SurrroundTopicDBConstant;
import com.mobcent.lowest.base.utils.MCDateUtil;

public class SurroundTopicDBUtil extends BaseDBUtil implements SurrroundTopicDBConstant {
    private static final int SURROUND_ID = 1;
    private static SurroundTopicDBUtil surroundTopicDBUtil;

    public boolean updateSurroundTopicJsonString(java.lang.String r10) {
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
        boolean ret = false;
        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put("id",1);
        values.put("jsonStr",r10);
        values.put("update_time", MCDateUtil.getCurrentTime());
        String table = "surround_topic_table";
        if(this.isRowExisted(this.writableDatabase,table,"id",1)){
            this.writableDatabase.update(table,values,"id=1",null);
            ret = true;
        }else{
            this.writableDatabase.insertOrThrow(table,null,values);
            ret = true;
        }
        closeWriteableDB();
        return  ret;

        /*
        r9 = this;
        r8 = 1;
        r9.openWriteableDB();	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r7 = new android.content.ContentValues;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r7.<init>();	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0 = "id";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = 1;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r7.put(r0, r1);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0 = "jsonStr";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r7.put(r0, r10);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0 = "update_time";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = com.mobcent.lowest.base.utils.MCDateUtil.getCurrentTime();	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r7.put(r0, r1);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = r9.writableDatabase;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r2 = "surround_topic_table";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r3 = "id";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r4 = 1;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0 = r9;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        if (r0 != 0) goto L_0x0041;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
    L_0x0034:
        r0 = r9.writableDatabase;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = "surround_topic_table";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r2 = 0;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0.insertOrThrow(r1, r2, r7);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
    L_0x003c:
        r9.closeWriteableDB();
        r0 = r8;
    L_0x0040:
        return r0;
    L_0x0041:
        r0 = r9.writableDatabase;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r1 = "surround_topic_table";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r2 = "id=1";	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r3 = 0;	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r0.update(r1, r7, r2, r3);	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        goto L_0x003c;
    L_0x004c:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ Exception -> 0x004c, all -> 0x0055 }
        r9.closeWriteableDB();
        r0 = 0;
        goto L_0x0040;
    L_0x0055:
        r0 = move-exception;
        r9.closeWriteableDB();
        throw r0;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.SurroundTopicDBUtil.updateSurroundTopicJsonString(java.lang.String):boolean");
    }

    protected SurroundTopicDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized SurroundTopicDBUtil getInstance(Context context) {
        synchronized (SurroundTopicDBUtil.class) {
            if (surroundTopicDBUtil == null) {
                surroundTopicDBUtil = new SurroundTopicDBUtil(context);
            }
        }
        return surroundTopicDBUtil;
    }

    public String getSurroundTopicJsonString() {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.rawQuery(SurrroundTopicDBConstant.SQL_SELECT_BOARD, null);
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

    public boolean deleteSurroundTopicList() {
        return removeAllEntries(SurrroundTopicDBConstant.TABLE_SURROUND_TOPIC);
    }
}
