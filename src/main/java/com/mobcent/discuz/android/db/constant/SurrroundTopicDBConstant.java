package com.mobcent.discuz.android.db.constant;

public interface SurrroundTopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_LOCATION = "CREATE TABLE IF NOT EXISTS surround_topic_table(id LONG PRIMARY KEY,jsonStr TEXT , update_time LONG );";
    public static final String SQL_SELECT_BOARD = "SELECT * FROM surround_topic_table";
    public static final String TABLE_SURROUND_TOPIC = "surround_topic_table";
}
