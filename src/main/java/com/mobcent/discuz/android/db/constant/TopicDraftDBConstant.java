package com.mobcent.discuz.android.db.constant;

public interface TopicDraftDBConstant {
    public static final String CHECK_COLOMN_ALLOWPOSTATTACHMENT = "checkallowPostAttachment";
    public static final String CHECK_COLOMN_ISANONYMOUS = "checkisanonymous";
    public static final String CHECK_COLOMN_ISHIDDEN = "checkishidden";
    public static final String CHECK_COLOMN_ISONLYAUTHOR = "checkisonlyauthor";
    public static final String CHECK_SETTING_MODEL = "checkSettingModel";
    public static final String COLUMN_BOARD_ID = "boardId";
    public static final String COLUMN_BOARD_NAME = "boardName";
    public static final String COLUMN_COMMON_ID = "commonId";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DRAFT = "draft_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON = "json";
    public static final String COLUMN_PIC_PATH = "pic_path";
    public static final String COLUMN_TIME = "save_time";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TYPE_ID = "typeId";
    public static final String COLUMN_TYPE_NAME = "typeName";
    public static final String COLUMN_VOICE_PATH = "voice_path";
    public static final long PUBLIC_TOPIC_ID = 1;
    public static final long PUBLIC_VOTE_ID = 2;
    public static final long REPLY_ID = 3;
    public static final String SQL_CREATE_TABLE_TOPIC_DRAFT = "CREATE TABLE IF NOT EXISTS topic_draft(id INTEGER PRIMARY KEY  AUTOINCREMENT, draft_id TEXT, type INTEGER, json TEXT, save_time LONG);";
    public static final String SQL_DROP_DRAFT = "drop table topic_draft";
    public static final String TABLE_TOPIC_DRAFT = "topic_draft";
    public static final int TYPE_PUBLISH = 1;
    public static final int TYPE_REPLY_ARTICLE = 3;
    public static final int TYPE_REPLY_TOPIC = 2;
}
