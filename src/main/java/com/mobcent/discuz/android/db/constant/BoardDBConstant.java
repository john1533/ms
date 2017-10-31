package com.mobcent.discuz.android.db.constant;

public interface BoardDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String SQL_CREATE_TABLE_BOARD = "CREATE TABLE IF NOT EXISTS board(id LONG PRIMARY KEY,jsonStr TEXT);";
    public static final String TABLE_BOARD = "board";
}
