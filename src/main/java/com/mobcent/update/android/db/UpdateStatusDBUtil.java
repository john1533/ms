package com.mobcent.update.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mobcent.update.android.db.constant.UpdateDBConstant;
import com.mobcent.update.android.model.UpdateModel;

public class UpdateStatusDBUtil implements UpdateDBConstant {
    private UpdateDBHelper updateDBHelper;

    public boolean saveOrUpdate(com.mobcent.update.android.model.UpdateModel r9, int r10) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0068 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        SQLiteDatabase db = this.updateDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("updateId",r9.getId());
        values.put("update_status",r10);
        if(this.isRowExisted(db,"update_status","updateId",r9.getId())){
            StringBuilder stringBuilder = new StringBuilder("updateId = '");
            stringBuilder.append(r9.getId()).append("'");
            db.update("update_status",values,stringBuilder.toString(),null);
        }else {
            db.insertOrThrow("update_status",null,values);
        }
        if(db != null){
            db.close();
        }
        return true;
        /*
        r8 = this;
        r1 = 0;
        r0 = r8.updateDBHelper;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r1 = r0.getWritableDatabase();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r7 = new android.content.ContentValues;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r7.<init>();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r0 = "updateId";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = r9.getId();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r7.put(r0, r2);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r0 = "update_status";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r7.put(r0, r2);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = "update_status";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r3 = "updateId";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r0 = r9.getId();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r4 = (long) r0;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r0 = r8;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        if (r0 != 0) goto L_0x003f;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
    L_0x0032:
        r0 = "update_status";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = 0;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r1.insertOrThrow(r0, r2, r7);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
    L_0x0038:
        if (r1 == 0) goto L_0x003d;
    L_0x003a:
        r1.close();
    L_0x003d:
        r0 = 1;
    L_0x003e:
        return r0;
    L_0x003f:
        r0 = "update_status";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r3 = "updateId = '";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2.<init>(r3);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r3 = r9.getId();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r3 = "'";	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r2 = r2.toString();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r3 = 0;	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        r1.update(r0, r7, r2, r3);	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        goto L_0x0038;
    L_0x005f:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ Exception -> 0x005f, all -> 0x006a }
        if (r1 == 0) goto L_0x0068;
    L_0x0065:
        r1.close();
    L_0x0068:
        r0 = 0;
        goto L_0x003e;
    L_0x006a:
        r0 = move-exception;
        if (r1 == 0) goto L_0x0070;
    L_0x006d:
        r1.close();
    L_0x0070:
        throw r0;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.update.android.db.UpdateStatusDBUtil.saveOrUpdate(com.mobcent.update.android.model.UpdateModel, int):boolean");
    }

    public UpdateStatusDBUtil(Context context) {
        this.updateDBHelper = new UpdateDBHelper(context);
    }

    public void saveStatus(UpdateModel model, int status) {
        SQLiteDatabase db = null;
        try {
            db = this.updateDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UpdateDBConstant.UPDATE_ID, Integer.valueOf(model.getId()));
            values.put("update_status", Integer.valueOf(status));
            db.insertOrThrow("update_status", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void updateStatus(UpdateModel model, int status) {
        SQLiteDatabase db = null;
        try {
            db = this.updateDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UpdateDBConstant.UPDATE_ID, Integer.valueOf(model.getId()));
            values.put("update_status", Integer.valueOf(status));
            db.update("update_status", values, "updateId = " + model.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public int getUpdateStatus(UpdateModel updateModel) {
        int status = 0;
        try {
            SQLiteDatabase db = this.updateDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select *  from update_status where updateId=?", new String[]{new StringBuilder(String.valueOf(updateModel.getId())).toString()});
            while (cursor.moveToNext()) {
                status = cursor.getInt(1);
            }
            cursor.close();
            db.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public UpdateModel getUpdateModel(UpdateModel updateModel) {
        Exception e;
        SQLiteDatabase db = null;
        try {
            UpdateModel model;
            db = this.updateDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select *  from update_status where updateId=?", new String[]{new StringBuilder(String.valueOf(updateModel.getId())).toString()});
            UpdateModel model2 = null;
            while (cursor.moveToNext()) {
                try {
                    model = new UpdateModel();
                    model.setId(cursor.getInt(0));
                    model.setStatus(cursor.getInt(1));
                    model2 = model;
                } catch (Exception e2) {
                    e = e2;
                    model = model2;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    model = model2;
                }
            }
            cursor.close();
            db.close();
            model = model2;
            return model2;
        } catch (Exception e3) {
            e = e3;
        }
        try {
            e.printStackTrace();
            db.close();
            return null;
        } catch (Throwable th3) {
            db.close();
        }
        return null;
    }

    public boolean isRowExisted(SQLiteDatabase database, String table, String column, long id) {
        try {
            if (database.query(table, null, new StringBuilder(String.valueOf(column)).append("=").append(id).toString(), null, null, null, null).getCount() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
