package com.mobcent.discuz.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseDBUtil {
    private static final String DATABASE_NAME = "dzForum.db";
    private static final int VERSION = 2;
    protected Context context;
    protected String databaseName;
    protected SQLiteOpenHelper dbOpenHelper;
    protected int version;
    protected SQLiteDatabase writableDatabase;
    private AtomicInteger writeInteger = new AtomicInteger();

    public boolean isRowExisted(android.database.sqlite.SQLiteDatabase db, java.lang.String tableName, java.lang.String column, long cVal) {
        StringBuilder queryBuilder = new StringBuilder(column);
        queryBuilder.append("=").append(cVal);
        Cursor cursor = db.query(tableName,null,queryBuilder.toString(),null,null,null,null,null);
        boolean ret = false;
        if(cursor != null&&cursor.getCount()>0){
            ret = true;
            cursor.close();
        }
        return ret;
    }

    public boolean isRowExisted(android.database.sqlite.SQLiteDatabase r12, java.lang.String r13, java.lang.String r14, java.lang.String r15) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0040 in list []
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

        StringBuffer stringBuffer = new StringBuffer(r14);
        stringBuffer.append("='").append(r15).append("'");
        Cursor cursor = r12.query(r14,null,stringBuffer.toString(),null,null,null,null);
        boolean ret = false;
        if(cursor!=null){
            if(cursor.getCount()>0) {
                ret = true;
            }
            cursor.close();
        }
        return ret;


        /*
        r11 = this;
        r10 = 0;
        r8 = 0;
        r2 = 0;
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r1 = java.lang.String.valueOf(r14);	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r1 = "='";	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r0 = r0.append(r15);	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r1 = "'";	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r3 = r0.toString();	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r4 = 0;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r5 = 0;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r6 = 0;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r7 = 0;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r0 = r12;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r1 = r13;	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        if (r0 <= 0) goto L_0x0049;
    L_0x0030:
        if (r8 == 0) goto L_0x0035;
    L_0x0032:
        r8.close();
    L_0x0035:
        r0 = 1;
    L_0x0036:
        return r0;
    L_0x0037:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ Exception -> 0x0037, all -> 0x0042 }
        if (r8 == 0) goto L_0x0040;
    L_0x003d:
        r8.close();
    L_0x0040:
        r0 = r10;
        goto L_0x0036;
    L_0x0042:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0048;
    L_0x0045:
        r8.close();
    L_0x0048:
        throw r0;
    L_0x0049:
        if (r8 == 0) goto L_0x004e;
    L_0x004b:
        r8.close();
    L_0x004e:
        r0 = r10;
        goto L_0x0036;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.BaseDBUtil.isRowExisted(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String, java.lang.String):boolean");
    }
    public boolean isRowExisted(android.database.sqlite.SQLiteDatabase r12, java.lang.String r13, java.lang.String[] r14, java.lang.String[] r15) {


/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x007a in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.Blocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r14[r10];	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = "='";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r15[r10];	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = "'";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r3 = r0.toString();	 Catch:{ Exception -> 0x0071, all -> 0x007c }

        * */
        StringBuffer stringBuffer = new StringBuffer();
        boolean ret = false;
        for(int i = 0;i < r14.length;i++){
            if(i==0){
                stringBuffer.append(r14[i]).append("='").append(r15[i]).append("'");
            }else{
                stringBuffer.append(" and ").append(r14[i]).append("='").append(r15[i]).append("'");
            }
        }
        Cursor cursor = r12.query(r13,null,stringBuffer.toString(),null,null,null,null);
        if(cursor != null){
            if(cursor.getCount()!=0){
                ret = true;
            }
            cursor.close();
        }
        return ret;
        /*
        r11 = this;
        r8 = 0;
        r3 = "";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r10 = 0;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
    L_0x0004:
        r0 = r14.length;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        if (r10 < r0) goto L_0x001f;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
    L_0x0007:
        r2 = 0;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r4 = 0;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r5 = 0;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r6 = 0;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r7 = 0;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r12;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r13;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        if (r0 <= 0) goto L_0x0083;
    L_0x0018:
        if (r8 == 0) goto L_0x001d;
    L_0x001a:
        r8.close();
    L_0x001d:
        r0 = 1;
    L_0x001e:
        return r0;
    L_0x001f:
        if (r10 != 0) goto L_0x0045;
    L_0x0021:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r14[r10];	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = java.lang.String.valueOf(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = "='";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r15[r10];	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = "'";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r3 = r0.toString();	 Catch:{ Exception -> 0x0071, all -> 0x007c }
    L_0x0042:
        r10 = r10 + 1;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        goto L_0x0004;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
    L_0x0045:
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = java.lang.String.valueOf(r3);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = " and ";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r14[r10];	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = "='";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = r15[r10];	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r1 = "'";	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        r3 = r0.toString();	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        goto L_0x0042;
    L_0x0071:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ Exception -> 0x0071, all -> 0x007c }
        if (r8 == 0) goto L_0x007a;
    L_0x0077:
        r8.close();
    L_0x007a:
        r0 = 0;
        goto L_0x001e;
    L_0x007c:
        r0 = move-exception;
        if (r8 == 0) goto L_0x0082;
    L_0x007f:
        r8.close();
    L_0x0082:
        throw r0;
    L_0x0083:
        if (r8 == 0) goto L_0x0088;
    L_0x0085:
        r8.close();
    L_0x0088:
        r0 = 0;
        goto L_0x001e;
        */
    //    throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.BaseDBUtil.isRowExisted(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String[], java.lang.String[]):boolean");
    }

    protected BaseDBUtil(Context context) {
        initData();
        this.context = context.getApplicationContext();
        this.dbOpenHelper = NormalDBOpenHelper.getInstance(context, this.databaseName, this.version);
    }

    protected void initData() {
        this.databaseName = DATABASE_NAME;
        this.version = 2;
    }

    public boolean removeAllEntries(String tableName) {
        try {
            openWriteableDB();
            this.writableDatabase.delete(tableName, null, null);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            closeWriteableDB();
        }
    }

    public synchronized void openWriteableDB() {
        if (this.writeInteger.incrementAndGet() == 1) {
            this.writableDatabase = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public synchronized void openReadableDB() {
        this.writableDatabase = this.dbOpenHelper.getReadableDatabase();
    }

    public synchronized void closeWriteableDB() {
        if (this.writeInteger.incrementAndGet() == 0) {
            if (this.writableDatabase != null && this.writableDatabase.isOpen()) {
                try {
                    this.writableDatabase.close();
                    this.writableDatabase = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                this.dbOpenHelper.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
