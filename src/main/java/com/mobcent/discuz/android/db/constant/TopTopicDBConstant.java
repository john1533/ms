package com.mobcent.discuz.android.db.constant;

public interface TopTopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_TOP_TOPIC = "CREATE TABLE IF NOT EXISTS top_topic(id LONG PRIMARY KEY,jsonStr TEXT , update_time LONG );";
    public static final String SQL_SELECT_TOP_TOPTIC = "SELECT * FROM top_topic";
    public static final String TABLE_TOP_TOPIC = "top_topic";
}
