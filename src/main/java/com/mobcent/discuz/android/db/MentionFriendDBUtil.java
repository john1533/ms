package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.mobcent.discuz.android.db.constant.MentionFriendsDBConstant;

public class MentionFriendDBUtil extends BaseDBUtil implements MentionFriendsDBConstant {
    private static MentionFriendDBUtil friendDBUtil;

    public MentionFriendDBUtil(Context ctx) {
        super(ctx);
    }

    public static synchronized MentionFriendDBUtil getInstance(Context context) {
        MentionFriendDBUtil mentionFriendDBUtil;
        synchronized (MentionFriendDBUtil.class) {
            if (friendDBUtil == null) {
                friendDBUtil = new MentionFriendDBUtil(context);
            }
            mentionFriendDBUtil = friendDBUtil;
        }
        return mentionFriendDBUtil;
    }

    public synchronized String getMentionFriendJsonString(long userId) {
        String jsonStr;
        jsonStr = null;
        Cursor cursor = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.query(MentionFriendsDBConstant.TABLE_MENTION_FRIEND, null, "userid=" + userId, null, null, null, null);
            while (cursor.moveToNext()) {
                jsonStr = cursor.getString(2);
            }
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        } catch (Exception e) {
            jsonStr = null;
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        }
        return jsonStr;
    }

    public synchronized boolean updateMentionFriendJsonString(String jsonStr, long openPlatformid, long userid, long lastUpdateTime) {
        boolean z = false;
        try {
            openWriteableDB();
            ContentValues values = new ContentValues();
            values.put(MentionFriendsDBConstant.COLUMN_JSON_STR, jsonStr);
            values.put(MentionFriendsDBConstant.COLUMN_OPEN_PLATFORM_ID, Long.valueOf(openPlatformid));
            values.put(MentionFriendsDBConstant.COLUMN_USER_ID, Long.valueOf(userid));
            values.put(MentionFriendsDBConstant.COLUMN_UPDATE_TIME, Long.valueOf(lastUpdateTime));
            if (isRowExisted(this.writableDatabase, MentionFriendsDBConstant.TABLE_MENTION_FRIEND, MentionFriendsDBConstant.COLUMN_USER_ID, userid)) {
                this.writableDatabase.update(MentionFriendsDBConstant.TABLE_MENTION_FRIEND, values, "userid=" + userid, null);
            } else {
                this.writableDatabase.insertOrThrow(MentionFriendsDBConstant.TABLE_MENTION_FRIEND, null, values);
            }
            closeWriteableDB();
            z = true;
        } catch (Exception e) {
            e.printStackTrace();
            closeWriteableDB();
            z = false;
        } catch (Throwable th) {
            closeWriteableDB();
        }
        return z;
    }

    public boolean delteMentionFriendList() {
        return removeAllEntries(MentionFriendsDBConstant.TABLE_MENTION_FRIEND);
    }
}
