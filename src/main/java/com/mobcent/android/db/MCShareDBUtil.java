package com.mobcent.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mobcent.android.db.constant.MCShareDBConstant;

public class MCShareDBUtil implements MCShareDBConstant {
    private static final String CREATE_SITE_TABLE = "CREATE TABLE IF NOT EXISTS ShareSite(userId Long PRIMARY KEY,lan TEXT,cty TEXT,siteListJson TEXT )";
    private static final String DATABASE_NAME = "mc_share";
    private static long USER_ID = 1;
    private static MCShareDBUtil shareDBUtil;
    private int DEFAULT_VERSION = 1;
    private Context ctx;
    private MCShareDBOpenHelper dbHelper;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public class MCShareDBOpenHelper extends SQLiteOpenHelper {
        public MCShareDBOpenHelper(Context context, String databaseName, int version) {
            super(context, databaseName, null, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(MCShareDBUtil.CREATE_SITE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public boolean addOrUpdateSite(java.lang.String r9, java.lang.String r10, java.lang.String r11) {
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
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAN,r9);
        values.put(COLUMN_CTY,r10);
        values.put(COLUMN_SITE_JSON,r11);
        values.put(COLUMN_USER_ID,USER_ID);
//        r0 = r8.writableDatabase;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r1 = "ShareSite";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r3 = "userId='";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r2.<init>(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r3 = USER_ID;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r2 = r2.append(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r3 = "'";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r2 = r2.append(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r2 = r2.toString();	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r3 = 0;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
//        r0.update(r1, r7, r2, r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        boolean ret = false;
        if(this.isRowExisted(this.writableDatabase,TABLE_SITE,COLUMN_USER_ID,USER_ID)){
            StringBuilder stringBuilder = new StringBuilder("userId='");
            stringBuilder.append(USER_ID).append("'");
            this.writableDatabase.update(TABLE_SITE, values, stringBuilder.toString(), null);
            ret = true;
        }else{
            this.writableDatabase.insertOrThrow(TABLE_SITE,null,values);
            ret = true;
        }
        this.closeWriteableDB();
        return ret;
        /*
        r8 = this;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r7 = new android.content.ContentValues;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r7.<init>();	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0 = "lan";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r7.put(r0, r9);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0 = "cty";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r7.put(r0, r10);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0 = "siteListJson";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r7.put(r0, r11);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0 = "userId";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r1 = USER_ID;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r7.put(r0, r1);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r1 = r8.writableDatabase;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2 = "ShareSite";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r3 = "userId";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r4 = USER_ID;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0 = r8;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        if (r0 != 0) goto L_0x003e;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
    L_0x0031:
        r0 = r8.writableDatabase;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r1 = "ShareSite";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2 = 0;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0.insertOrThrow(r1, r2, r7);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
    L_0x0039:
        r8.closeWriteableDB();
        r0 = 1;
    L_0x003d:
        return r0;
    L_0x003e:
        r0 = r8.writableDatabase;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r1 = "ShareSite";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r3 = "userId='";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r3 = USER_ID;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r3 = "'";	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r3 = 0;	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        r0.update(r1, r7, r2, r3);	 Catch:{ Exception -> 0x005e, all -> 0x0064 }
        goto L_0x0039;
    L_0x005e:
        r6 = move-exception;
        r8.closeWriteableDB();
        r0 = 0;
        goto L_0x003d;
    L_0x0064:
        r0 = move-exception;
        r8.closeWriteableDB();
        throw r0;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.android.db.MCShareDBUtil.addOrUpdateSite(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    public boolean isRowExisted(android.database.sqlite.SQLiteDatabase r14, java.lang.String r15, java.lang.String r16, long r17) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0032 in list [B:7:0x002f]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        StringBuilder stringBuilder = new StringBuilder(r16);
        stringBuilder.append("=").append(r17);
        Cursor cursor = r14.query(r15,null,stringBuilder.toString(),null,null,null,null);
        boolean ret = false;
        if(cursor!=null){
            if(cursor.getCount()>0){
                ret = true;
            }
            cursor.close();
        }
        return ret;
        /*
        r13 = this;
        r10 = 0;
        r4 = 0;
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r3 = java.lang.String.valueOf(r16);	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r3 = "=";	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r0 = r17;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r2 = r2.append(r0);	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r5 = r2.toString();	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r6 = 0;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r7 = 0;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r8 = 0;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r9 = 0;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r2 = r14;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r3 = r15;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r10 = r2.query(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r12 = 0;	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        r2 = r10.getCount();	 Catch:{ Exception -> 0x0033, all -> 0x003b }
        if (r2 <= 0) goto L_0x002d;
    L_0x002c:
        r12 = 1;
    L_0x002d:
        if (r10 == 0) goto L_0x0032;
    L_0x002f:
        r10.close();
    L_0x0032:
        return r12;
    L_0x0033:
        r11 = move-exception;
        if (r10 == 0) goto L_0x0039;
    L_0x0036:
        r10.close();
    L_0x0039:
        r12 = 0;
        goto L_0x0032;
    L_0x003b:
        r2 = move-exception;
        if (r10 == 0) goto L_0x0041;
    L_0x003e:
        r10.close();
    L_0x0041:
        throw r2;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.android.db.MCShareDBUtil.isRowExisted(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String, long):boolean");
    }

    private MCShareDBUtil(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        this.dbHelper = new MCShareDBOpenHelper(ctx, DATABASE_NAME, this.DEFAULT_VERSION);
    }

    private synchronized void openWriteableDB() {
        if (this.writableDatabase == null || !this.writableDatabase.isOpen()) {
            this.writableDatabase = this.dbHelper.getWritableDatabase();
        }
    }

    private synchronized void openReadableDB() {
        if (this.readableDatabase == null || !this.readableDatabase.isOpen()) {
            this.readableDatabase = this.dbHelper.getReadableDatabase();
        }
    }

    private synchronized void closeWriteableDB() {
        if (this.writableDatabase != null && this.writableDatabase.isOpen()) {
            try {
                this.writableDatabase.close();
                this.writableDatabase = null;
            } catch (Exception e) {
            }
        }
    }

    private synchronized void closeReadableDB() {
        if (this.readableDatabase != null && this.readableDatabase.isOpen()) {
            try {
                this.readableDatabase.close();
                this.readableDatabase = null;
            } catch (Exception e) {
            }
        }
    }

    public static synchronized MCShareDBUtil getInstance(Context context) {
        MCShareDBUtil mCShareDBUtil;
        synchronized (MCShareDBUtil.class) {
            if (shareDBUtil == null) {
                shareDBUtil = new MCShareDBUtil(context);
            }
            mCShareDBUtil = shareDBUtil;
        }
        return mCShareDBUtil;
    }

    public String getLocalSite() throws Exception {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openReadableDB();
            cursor = this.readableDatabase.rawQuery("SELECT * FROM ShareSite where userId=" + USER_ID, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                jsonStr = cursor.getString(cursor.getColumnIndex(MCShareDBConstant.COLUMN_SITE_JSON));
            }
            if (cursor != null) {
                cursor.close();
            }
            closeReadableDB();
            return jsonStr;
        } catch (Exception e) {
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            closeReadableDB();
        }
        return jsonStr;
    }
}
