package com.mobcent.update.android.db.constant;

public interface UpdateDBConstant {
    public static final String DBNAME = "update.db";
    public static final String DOWN_LENGTH = "downlength";
    public static final String DOWN_PATH = "downpath";
    public static final String POSTION_ID = "postionid";
    public static final int STATUS_UPDATE_FINISH = 3;
    public static final int STATUS_UPDATE_INIT = 0;
    public static final int STATUS_UPDATE_LATER = 2;
    public static final int STATUS_UPDATE_NOT = -1;
    public static final int STATUS_UPDATE_NOW = 1;
    public static final String TABLE_DOWNLOAD = "download_table";
    public static final String TABLE_UPDATE_STATUS = "update_status";
    public static final String UPDATE_ID = "updateId";
    public static final String UPDATE_STATUS = "update_status";
    public static final int VERSION = 1;
}
