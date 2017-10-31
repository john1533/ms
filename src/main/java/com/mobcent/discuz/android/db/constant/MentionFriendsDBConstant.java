package com.mobcent.discuz.android.db.constant;

public interface MentionFriendsDBConstant {
    public static final String COLUMN_JSON_STR = "jsonstr";
    public static final String COLUMN_OPEN_PLATFORM_ID = "openplatformid";
    public static final String COLUMN_UPDATE_TIME = "updatetime";
    public static final String COLUMN_USER_ID = "userid";
    public static final String DELETE_USER_ID = "userid=?";
    public static final String SQL_CREATE_TABLE_FRIEND = "CREATE TABLE IF NOT EXISTS mentionfriend(userid LONG PRIMARY KEY,openplatformid LONG,jsonstr TEXT,updatetime LONG);";
    public static final String TABLE_MENTION_FRIEND = "mentionfriend";
}
