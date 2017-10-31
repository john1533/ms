package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.BoardDBConstant;

public class BoardDBUtil extends BaseDBUtil implements BoardDBConstant {
    private static final int BOARD_ID = 1;
    private static String SQL_SELECT_BOARD = "SELECT * FROM board";
    private static BoardDBUtil boardDBUtil;

    public boolean updateBoardJsonString(java.lang.String jsonStr) {

        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JSON_STR,jsonStr);
        values.put(COLUMN_ID,1);
        SQLiteDatabase db = this.writableDatabase;
        boolean isRowExits = isRowExisted(db,TABLE_BOARD,COLUMN_ID,1);
        if(isRowExits){
            db.insertOrThrow(TABLE_BOARD,null,values);
        }else {
            db.update(TABLE_BOARD,values,"id=1",null);
        }
        this.closeWriteableDB();
        return true;
        /*
        r9 = this;
        r8 = 1;
        r9.openWriteableDB();	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r7 = new android.content.ContentValues;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r7.<init>();	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r0 = "jsonStr";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r7.put(r0, r10);	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r0 = "id";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r1 = 1;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r7.put(r0, r1);	 Catch:{ Exception -> 0x003f, all -> 0x0048 }

        r1 = r9.writableDatabase;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r2 = "board";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r3 = "id";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r4 = 1;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r0 = r9;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        if (r0 != 0) goto L_0x0034;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
    L_0x0027:
        r0 = r9.writableDatabase;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r1 = "board";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r2 = 0;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r0.insertOrThrow(r1, r2, r7);	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
    L_0x002f:
        r9.closeWriteableDB();
        r0 = r8;
    L_0x0033:
        return r0;
    L_0x0034:
        r0 = r9.writableDatabase;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r1 = "board";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r2 = "id=1";	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r3 = 0;	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r0.update(r1, r7, r2, r3);	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        goto L_0x002f;
    L_0x003f:
        r6 = move-exception;
        r6.printStackTrace();	 Catch:{ Exception -> 0x003f, all -> 0x0048 }
        r9.closeWriteableDB();
        r0 = 0;
        goto L_0x0033;
    L_0x0048:
        r0 = move-exception;
        r9.closeWriteableDB();
        throw r0;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.BoardDBUtil.updateBoardJsonString(java.lang.String):boolean");
    }

    public BoardDBUtil(Context context) {
        super(context);
    }

    public static synchronized BoardDBUtil getInstance(Context context) {
        synchronized (BoardDBUtil.class) {
            if (boardDBUtil == null) {
                boardDBUtil = new BoardDBUtil(context.getApplicationContext());
            }
        }
        return boardDBUtil;
    }

    public String getBoardJsonString() {
        String jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.rawQuery(SQL_SELECT_BOARD, null);
            while (cursor.moveToNext()) {
                jsonStr = cursor.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        }
        return jsonStr;
    }

    public boolean delteBoardList() {
        return removeAllEntries(BoardDBConstant.TABLE_BOARD);
    }
}
