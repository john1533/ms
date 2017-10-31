package com.mobcent.discuz.android.db.constant;

public interface EssenceTopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_ESSENCE_TOPIC = "CREATE TABLE IF NOT EXISTS essence_topic(id LONG PRIMARY KEY,jsonStr TEXT , update_time LONG );";
    public static final String SQL_SELECT_ESSENCE_TOPTIC = "SELECT * FROM essence_topic";
    public static final String TABLE_ESSENCE_TOPIC = "essence_topic";
}
