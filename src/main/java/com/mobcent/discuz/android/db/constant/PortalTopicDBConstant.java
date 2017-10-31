package com.mobcent.discuz.android.db.constant;

public interface PortalTopicDBConstant {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON_STR = "jsonStr";
    public static final String COLUMN_TYPE_ID = "moudle_id";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String SQL_CREATE_TABLE_PORTAL_TOPIC = "CREATE TABLE IF NOT EXISTS portal_topic(jsonStr TEXT , update_time TEXT ,moudle_id TEXT PRIMARY KEY );";
    public static final String SQL_SELECT_HOT_TOPTIC = "SELECT * FROM portal_topic";
    public static final String TABLE_PORTAL_TOPIC = "portal_topic";
}
