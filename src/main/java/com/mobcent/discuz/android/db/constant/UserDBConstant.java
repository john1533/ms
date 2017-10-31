package com.mobcent.discuz.android.db.constant;

public interface UserDBConstant {
    public static final String COLUMN_EXTRA_USER_INFO = "extraUserInfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IS_LOGIN = "isLogin";
    public static final String COLUMN_USER_GENDER = "userGender";
    public static final String COLUMN_USER_ICON = "userIcon";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_USER_JSON = "userJson";
    public static final String COLUMN_USER_NAME = "userName";
    public static final String COLUMN_USER_PERMISSION = "userPermission";
    public static final String COLUMN_USER_PWD = "userPwd";
    public static final String COLUMN_USER_TOKEN = "userToken";
    public static final String COLUMN_USER_TYPE = "userType";
    public static final String COLUMN_USET_TITLE = "userTitle";
    public static final String COLUNM_USER_SECRET = "userSecret";
    public static final int IS_LOGIN = 1;
    public static final int NOT_LOGIN = 0;
    public static final String SQL_CREATE_TABLE_USER = "create table if not exists table_user ( userId long,userType varchar(100),userJson text); ";
    public static final String SQL_CREATE_TABLE_USER_INFO = "create table if not exists table_user_info ( _id integer primary key autoincrement, userId long, userName text, userIcon text, userTitle text, userGender integer, userPermission text,userPwd text, extraUserInfo text, isLogin integer, userToken text, userSecret text  ) ";
    public static final String SQL_SAVE_PERMISSION = " update table_user_info set userPermission =? where userId =? ";
    public static final String SQL_SELECT_ALL_USER = " select * from table_user_info where userPwd !=  \"\" ";
    public static final String SQL_SELECT_INFO_BY_ID = " select * from table_user_info where userId=?";
    public static final String SQL_SELECT_INFO_BY_LOGIN = " select * from table_user_info where isLogin = 1";
    public static final String SQL_SELECT_USER_JSON = "select * from table_user where userId=? and userType=?;";
    public static final String TABLE_USER = "table_user";
    public static final String TABLE_USER_INFO = "table_user_info";
}
