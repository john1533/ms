package com.mobcent.lowest.module.game.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameBaseDBUtil {
    protected SQLiteOpenHelper autogenDBOpenHelper;
    protected SQLiteDatabase readableDatabase;
    protected SQLiteDatabase writableDatabase;

    protected boolean isRowExisted(android.database.sqlite.SQLiteDatabase r11, java.lang.String r12, java.lang.String r13, long r14) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0036 in list []
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
        boolean ret = false;
        StringBuilder sqlBuilder = new StringBuilder(String.valueOf(r13));
        sqlBuilder.append("=").append(r14);
        Cursor cursor = r11.query(r12, null, sqlBuilder.toString(), null, null, null, null);
        if(cursor != null){
            if(cursor.getCount()>0){
                ret = true;
            }
            cursor.close();
        }
        return ret;
        /*
        r10 = this;
        r8 = 0;
        r2 = 0;
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r1 = java.lang.String.valueOf(r13);	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r0.<init>(r1);	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r1 = "=";	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r0 = r0.append(r14);	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r3 = r0.toString();	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r4 = 0;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r5 = 0;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r6 = 0;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r7 = 0;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r0 = r11;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r1 = r12;	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x0030, all -> 0x0038 }
        if (r0 <= 0) goto L_0x003f;
    L_0x0029:
        if (r8 == 0) goto L_0x002e;
    L_0x002b:
        r8.close();
    L_0x002e:
        r0 = 1;
    L_0x002f:
        return r0;
    L_0x0030:
        r9 = move-exception;
        if (r8 == 0) goto L_0x0036;
    L_0x0033:
        r8.close();
    L_0x0036:
        r0 = 0;
        goto L_0x002f;
    L_0x0038:
        r0 = move-exception;
        if (r8 == 0) goto L_0x003e;
    L_0x003b:
        r8.close();
    L_0x003e:
        throw r0;
    L_0x003f:
        if (r8 == 0) goto L_0x0044;
    L_0x0041:
        r8.close();
    L_0x0044:
        r0 = 0;
        goto L_0x002f;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.module.game.db.GameBaseDBUtil.isRowExisted(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String, long):boolean");
    }

    protected boolean isRowExisted(android.database.sqlite.SQLiteDatabase r12, java.lang.String r13, java.lang.String r14, java.lang.String r15) {
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

        boolean ret = false;
        StringBuilder sqlBuilder = new StringBuilder(String.valueOf(r14));
        sqlBuilder.append("='").append(r15).append("'");
        Cursor cursor = r12.query(r13, null, sqlBuilder.toString(), null, null, null, null);
        if(cursor != null){
            if(cursor.getCount()>0){
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
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.module.game.db.GameBaseDBUtil.isRowExisted(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    protected boolean isRowExisted(android.database.sqlite.SQLiteDatabase r12, java.lang.String r13, java.lang.String r14, java.lang.String[] r15) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0024 in list []
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

        boolean ret = false;
        Cursor cursor = r12.query(r13, null, r14, r15, null, null, null);
        if(cursor != null){
            if(cursor.getCount()>0){
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
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r0 = r12;
        r1 = r13;
        r3 = r14;
        r4 = r15;
        r8 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x001b, all -> 0x0026 }
        r0 = r8.getCount();	 Catch:{ Exception -> 0x001b, all -> 0x0026 }
        if (r0 <= 0) goto L_0x002d;
    L_0x0014:
        if (r8 == 0) goto L_0x0019;
    L_0x0016:
        r8.close();
    L_0x0019:
        r0 = 1;
    L_0x001a:
        return r0;
    L_0x001b:
        r9 = move-exception;
        r9.printStackTrace();	 Catch:{ Exception -> 0x001b, all -> 0x0026 }
        if (r8 == 0) goto L_0x0024;
    L_0x0021:
        r8.close();
    L_0x0024:
        r0 = r10;
        goto L_0x001a;
    L_0x0026:
        r0 = move-exception;
        if (r8 == 0) goto L_0x002c;
    L_0x0029:
        r8.close();
    L_0x002c:
        throw r0;
    L_0x002d:
        if (r8 == 0) goto L_0x0032;
    L_0x002f:
        r8.close();
    L_0x0032:
        r0 = r10;
        goto L_0x001a;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.module.game.db.GameBaseDBUtil.isRowExisted(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String, java.lang.String[]):boolean");
    }

    protected boolean tabbleIsExist(android.database.sqlite.SQLiteDatabase r9, java.lang.String r10) {
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/      boolean ret = false;
        if(r10!=null){
            StringBuilder stringBuilder = new StringBuilder("select count(*) as c from Sqlite_master  where type ='table' and name ='");
            stringBuilder.append(r10.trim()).append("' ");
            Cursor cursor = r9.rawQuery(stringBuilder.toString(), null);
            if(cursor != null){
                if(cursor.moveToNext()&&cursor.getInt(0)>0){
                    ret = true;
                }
                cursor.close();
            }
        }
        return ret;

        /*
        r8 = this;
        r5 = 0;
        r3 = 0;
        if (r10 != 0) goto L_0x0005;
    L_0x0004:
        return r5;
    L_0x0005:
        r0 = 0;
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r7 = "select count(*) as c from Sqlite_master  where type ='table' and name ='";	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r6.<init>(r7);	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r7 = r10.trim();	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r7 = "' ";	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r4 = r6.toString();	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r6 = 0;	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r0 = r9.rawQuery(r4, r6);	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r6 = r0.moveToNext();	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        if (r6 == 0) goto L_0x0032;	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
    L_0x002a:
        r6 = 0;	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        r1 = r0.getInt(r6);	 Catch:{ Exception -> 0x0039, all -> 0x0040 }
        if (r1 <= 0) goto L_0x0032;
    L_0x0031:
        r3 = 1;
    L_0x0032:
        if (r0 == 0) goto L_0x0037;
    L_0x0034:
        r0.close();
    L_0x0037:
        r5 = r3;
        goto L_0x0004;
    L_0x0039:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0004;
    L_0x003c:
        r0.close();
        goto L_0x0004;
    L_0x0040:
        r5 = move-exception;
        if (r0 == 0) goto L_0x0046;
    L_0x0043:
        r0.close();
    L_0x0046:
        throw r5;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.lowest.module.game.db.GameBaseDBUtil.tabbleIsExist(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
    }

    protected boolean removeAllEntries(String table) {
        try {
            openWriteableDB();
            this.writableDatabase.delete(table, null, null);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            closeWriteableDB();
        }
    }

    protected void closeWriteableDB() {
        try {
            this.autogenDBOpenHelper.close();
        } catch (Exception e) {
        }
        if (this.writableDatabase != null && this.writableDatabase.isOpen()) {
            try {
                this.writableDatabase.close();
                this.writableDatabase = null;
            } catch (Exception e2) {
            }
        }
    }

    protected void closeReadableDB() {
        try {
            this.autogenDBOpenHelper.close();
        } catch (Exception e) {
        }
        if (this.readableDatabase != null && this.readableDatabase.isOpen()) {
            try {
                this.readableDatabase.close();
                this.readableDatabase = null;
            } catch (Exception e2) {
            }
        }
    }

    protected void openWriteableDB() {
        this.writableDatabase = this.autogenDBOpenHelper.getWritableDatabase();
    }

    protected void openReadableDB() {
        this.readableDatabase = this.autogenDBOpenHelper.getReadableDatabase();
    }
}
