package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.mobcent.discuz.android.db.constant.PortalTopicDBConstant;
import com.mobcent.lowest.base.utils.MCDateUtil;

public class PortalTopicDBUtil extends BaseDBUtil implements PortalTopicDBConstant {
    private static PortalTopicDBUtil portalTopicDBUtil;

    public boolean updatePortalTopicJsonString(java.lang.String r7, java.lang.String r8) {
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
        values.put("jsonStr",r7);
        values.put("update_time", MCDateUtil.getCurrentTime());
        values.put("moudle_id",r8);
        String table = "portal_topic";
        if(this.isRowExisted(this.writableDatabase,table,"moudle_id",r8)){
            StringBuilder stringBuilder = new StringBuilder("moudle_id=");
            stringBuilder.append(r8);
            this.writableDatabase.update(table,values,stringBuilder.toString(),null);
            ret = true;

        }else{
           this.writableDatabase.insertOrThrow(table,null,values);
            ret = true;
        }
        this.closeWriteableDB();
        return ret;
        /*
        r6 = this;
        r6.openWriteableDB();	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r1 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r1.<init>();	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2 = "jsonStr";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r1.put(r2, r7);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2 = "update_time";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r3 = com.mobcent.lowest.base.utils.MCDateUtil.getCurrentTime();	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r3 = java.lang.Long.valueOf(r3);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r1.put(r2, r3);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2 = "moudle_id";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r1.put(r2, r8);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2 = r6.writableDatabase;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r3 = "portal_topic";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r4 = "moudle_id";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2 = r6.isRowExisted(r2, r3, r4, r8);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        if (r2 != 0) goto L_0x0038;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
    L_0x002b:
        r2 = r6.writableDatabase;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r3 = "portal_topic";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r4 = 0;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2.insertOrThrow(r3, r4, r1);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
    L_0x0033:
        r6.closeWriteableDB();
        r2 = 1;
    L_0x0037:
        return r2;
    L_0x0038:
        r2 = r6.writableDatabase;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r3 = "portal_topic";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r5 = "moudle_id=";	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r4.<init>(r5);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r5 = 0;	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r2.update(r3, r1, r4, r5);	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        goto L_0x0033;
    L_0x0050:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Exception -> 0x0050, all -> 0x0059 }
        r6.closeWriteableDB();
        r2 = 0;
        goto L_0x0037;
    L_0x0059:
        r2 = move-exception;
        r6.closeWriteableDB();
        throw r2;
        */
    }

    public PortalTopicDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized PortalTopicDBUtil getInstance(Context context) {
        synchronized (PortalTopicDBUtil.class) {
            if (portalTopicDBUtil == null) {
                portalTopicDBUtil = new PortalTopicDBUtil(context);
            }
        }
        return portalTopicDBUtil;
    }

    public String getPortalTopicJsonString(String moudleId) {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.query(PortalTopicDBConstant.TABLE_PORTAL_TOPIC, null, "moudle_id=" + moudleId, null, null, null, null);
            while (cursor.moveToNext()) {
                jsonStr = cursor.getString(0);
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

    public boolean deltePortalTopicList() {
        return removeAllEntries(PortalTopicDBConstant.TABLE_PORTAL_TOPIC);
    }
}
