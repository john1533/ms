package com.mobcent.discuz.android.db.constant;

public interface TopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_TOPIC = "CREATE TABLE IF NOT EXISTS topic(id LONG PRIMARY KEY,jsonStr TEXT , update_time LONG );";
    public static final String SQL_SELECT_TOPTIC = "SELECT * FROM topic";
    public static final String TABLE_TOPIC = "topic";
}
