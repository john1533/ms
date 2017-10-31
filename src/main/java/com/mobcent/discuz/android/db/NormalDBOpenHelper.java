package com.mobcent.discuz.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mobcent.discuz.android.db.constant.BoardDBConstant;
import com.mobcent.discuz.android.db.constant.EssenceTopicDBConstant;
import com.mobcent.discuz.android.db.constant.MentionFriendsDBConstant;
import com.mobcent.discuz.android.db.constant.MsgDBConstant;
import com.mobcent.discuz.android.db.constant.NewsTopicDBConstant;
import com.mobcent.discuz.android.db.constant.PicTopicDBConstant;
import com.mobcent.discuz.android.db.constant.PortalTopicDBConstant;
import com.mobcent.discuz.android.db.constant.SurrroundTopicDBConstant;
import com.mobcent.discuz.android.db.constant.TopTopicDBConstant;
import com.mobcent.discuz.android.db.constant.TopicDBConstant;
import com.mobcent.discuz.android.db.constant.TopicDraftDBConstant;
import com.mobcent.discuz.android.db.constant.UserDBConstant;

public class NormalDBOpenHelper extends SQLiteOpenHelper {
    private static NormalDBOpenHelper normalDBOpenHelper;

    public static synchronized NormalDBOpenHelper getInstance(Context context, String databaseName, int version) {
        synchronized (NormalDBOpenHelper.class) {
            if (normalDBOpenHelper == null) {
                normalDBOpenHelper = new NormalDBOpenHelper(context, databaseName, version);
            }
        }
        return normalDBOpenHelper;
    }

    private NormalDBOpenHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PortalTopicDBConstant.SQL_CREATE_TABLE_PORTAL_TOPIC);
        db.execSQL(MentionFriendsDBConstant.SQL_CREATE_TABLE_FRIEND);
        db.execSQL(UserDBConstant.SQL_CREATE_TABLE_USER_INFO);
        db.execSQL(EssenceTopicDBConstant.SQL_CREATE_TABLE_ESSENCE_TOPIC);
        db.execSQL(NewsTopicDBConstant.SQL_CREATE_TABLE_NEWS_TOPIC);
        db.execSQL(BoardDBConstant.SQL_CREATE_TABLE_BOARD);
        db.execSQL(TopicDBConstant.SQL_CREATE_TABLE_TOPIC);
        db.execSQL(TopTopicDBConstant.SQL_CREATE_TABLE_TOP_TOPIC);
        db.execSQL(SurrroundTopicDBConstant.SQL_CREATE_TABLE_LOCATION);
        db.execSQL(MsgDBConstant.SQL_CREATE_SESSION_TABLE);
        db.execSQL(UserDBConstant.SQL_CREATE_TABLE_USER);
        db.execSQL(MsgDBConstant.SQL_CREATE_COMMENT_AT_TABLE);
        db.execSQL(PicTopicDBConstant.SQL_CREATE_TABLE_PIC_TOPIC);
        db.execSQL(TopicDraftDBConstant.SQL_CREATE_TABLE_TOPIC_DRAFT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.delete(PortalTopicDBConstant.TABLE_PORTAL_TOPIC, null, null);
        db.delete(MentionFriendsDBConstant.TABLE_MENTION_FRIEND, null, null);
        db.delete(UserDBConstant.TABLE_USER_INFO, null, null);
        db.delete(NewsTopicDBConstant.TABLE_NEWS_TOPIC, null, null);
        db.delete(EssenceTopicDBConstant.TABLE_ESSENCE_TOPIC, null, null);
        db.delete(BoardDBConstant.TABLE_BOARD, null, null);
        db.delete("topic", null, null);
        db.delete(TopTopicDBConstant.TABLE_TOP_TOPIC, null, null);
        db.delete(SurrroundTopicDBConstant.TABLE_SURROUND_TOPIC, null, null);
        db.delete(MsgDBConstant.TABLE_SESSION_JSON, null, null);
        db.delete(UserDBConstant.TABLE_USER, null, null);
        db.delete(MsgDBConstant.TABLE_COMMENT_AT, null, null);
        db.delete(PicTopicDBConstant.TABLE_PIC_TOPIC, null, null);
        db.execSQL(TopicDraftDBConstant.SQL_DROP_DRAFT);
        onCreate(db);
    }
}
