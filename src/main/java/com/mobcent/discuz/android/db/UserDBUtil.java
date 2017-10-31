package com.mobcent.discuz.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobcent.discuz.android.db.constant.UserDBConstant;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.lowest.base.utils.MCStringUtil;

import java.util.ArrayList;

public class UserDBUtil extends BaseDBUtil implements UserDBConstant {
    private static UserDBUtil userDBUtil;

    public boolean addUserInfo(com.mobcent.discuz.android.model.UserInfoModel r13, boolean r14) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0142 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)



	r12 = this;
        r9 = 0;
        r0 = r13.getUserId();
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x0172;
    L_0x000b:
        r12.openWriteableDB();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = r12.writableDatabase;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0.beginTransaction();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.<init>();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = "userId";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getUserId();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = java.lang.Long.valueOf(r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = "userName";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getNickname();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = "userIcon";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getIcon();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = "userTitle";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getLevelName();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = "userGender";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getGender();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = r13.getPermission();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = com.mobcent.lowest.base.utils.MCStringUtil.isEmpty(r0);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        if (r0 != 0) goto L_0x0060;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x0057:
        r0 = "userPermission";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getPermission();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
L_0x0060:
        r0 = r13.getPwd();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = com.mobcent.lowest.base.utils.MCStringUtil.isEmpty(r0);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        if (r0 != 0) goto L_0x0073;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x006a:
        r0 = "userPwd";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getPwd();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x0073:
        r0 = r13.getExtraUserInfo();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
            r0 = com.mobcent.lowest.base.utils.MCStringUtil.isEmpty(r0);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
            if (r0 != 0) goto L_0x0086;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        L_0x007d:
            r0 = "extraUserInfo";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
            r1 = r13.getExtraUserInfo();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
            r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
         L_0x0086:
        if (r14 == 0) goto L_0x011c;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x0088:
        r0 = "isLogin";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = 1;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x0092:
        r0 = r13.getToken();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = com.mobcent.lowest.base.utils.MCStringUtil.isEmpty(r0);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        if (r0 != 0) goto L_0x00a5;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x009c:
        r0 = "userToken";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getToken();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00a5:
    r0 = r13.getSecret();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = com.mobcent.lowest.base.utils.MCStringUtil.isEmpty(r0);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        if (r0 != 0) goto L_0x00b8;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00af:
        r0 = "userSecret";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = r13.getSecret();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00b8:
    r1 = r12.writableDatabase;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = "table_user_info";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = "userId";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r4 = r13.getUserId();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = r12;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = r0.isRowExisted(r1, r2, r3, r4);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        if (r0 == 0) goto L_0x0131;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00c9:
        r0 = r12.writableDatabase;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = "table_user_info";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = "userId = ";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = r13.getUserId();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = 0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r6 = r0.update(r1, r10, r2, r3);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        if (r6 > 0) goto L_0x00e8;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00e7:
        r9 = 1;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00e8:
        if (r14 == 0) goto L_0x0118;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x00ea:
        r11 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r11.<init>();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = "isLogin";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = 0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r11.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = r12.writableDatabase;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = "table_user_info";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = "userId != ";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2.<init>(r3);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = r13.getUserId();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r3 = 0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r6 = r0.update(r1, r11, r2, r3);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }

*/

        long userId = r13.getUserId();
        boolean noAffected = false;
        if(userId!=0){
            this.openWriteableDB();
            SQLiteDatabase db = this.writableDatabase;
            db.beginTransaction();;
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID,userId);
            values.put(COLUMN_USER_NAME, r13.getNickname());
            values.put(COLUMN_USER_ICON, r13.getIcon());
            values.put(COLUMN_USET_TITLE, r13.getLevelName());
            values.put(COLUMN_USER_GENDER, Integer.valueOf(r13.getGender()));
            if(!MCStringUtil.isEmpty(r13.getPermission())){
                values.put(COLUMN_USER_PERMISSION,r13.getPermission());
            }
            if(!MCStringUtil.isEmpty(r13.getPwd())){
                values.put(COLUMN_USER_PWD,r13.getPwd());
            }
            if(!MCStringUtil.isEmpty(r13.getExtraUserInfo())){
                values.put(COLUMN_EXTRA_USER_INFO,r13.getExtraUserInfo());
            }
            if(r14){
                values.put(COLUMN_IS_LOGIN,1);
            }else{
                values.put(COLUMN_IS_LOGIN,0);
            }
            if(!MCStringUtil.isEmpty(r13.getToken())){
                values.put(COLUMN_USER_TOKEN,r13.getToken());
            }
            if(!MCStringUtil.isEmpty(r13.getSecret())){
                values.put(COLUNM_USER_SECRET,r13.getSecret());
            }

            if(this.isRowExisted(db,TABLE_USER_INFO,COLUMN_USER_ID,r13.getUserId())){
                StringBuffer stringBuffer = new StringBuffer(COLUMN_USER_ID);
                stringBuffer.append(" = ").append(r13.getUserId());
                if(db.update(TABLE_USER_INFO,values,stringBuffer.toString(),null)<=0){
                    noAffected = true;
                    //L_0x00e8
                    if(r14){//如果登录，要把其他的用户的login状态设置为0,保证数据库只有最新登录的用户为等待状态
                        //L_0x00ea
                        ContentValues values1 = new ContentValues();
                        values1.put(COLUMN_IS_LOGIN,0);
                        StringBuffer stringBuffer1 = new StringBuffer(COLUMN_USER_ID);
                        stringBuffer1.append(" != ").append(r13.getUserId());
                        if(db.update(TABLE_USER_INFO,values1,stringBuffer1.toString(),null)<0){
//                            L_0x0117
                            noAffected = true;
//                            return false;
                        }else{
                            //L_0x0118
//                            return false;
                        }

//                        return false;
                    }else{
//                        L_0x0118
//                        return false;
                    }
                    return false;
                }else{

//                  L_0x00e8
                    if(r14){
                        //L_0x00ea
                        ContentValues values1 = new ContentValues();
                        values1.put(COLUMN_IS_LOGIN,0);
                        StringBuffer stringBuffer1 = new StringBuffer(COLUMN_USER_ID);
                        stringBuffer1.append(" != ").append(r13.getUserId());
                        if(db.update(TABLE_USER_INFO,values1,stringBuffer1.toString(),null)<0){
//                            L_0x0117
                            noAffected = true;
                            return false;
                        }else{
                            //L_0x0118  L_0x0164

                            db.setTransactionSuccessful();
                            db.endTransaction();
                            this.closeWriteableDB();
                            return true;
                        }

                    }else{
//                        L_0x0118
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        this.closeWriteableDB();
                        return true;
                    }

                }

            }else{
                //L_0x0131
                long inNum = db.insert(TABLE_USER_INFO,null,values);
                if(inNum!=-1){//
                    if(r14){
                        //L_0x00ea
                        ContentValues values1 = new ContentValues();
                        values1.put(COLUMN_IS_LOGIN,0);
                        StringBuffer stringBuffer1 = new StringBuffer(COLUMN_USER_ID);
                        stringBuffer1.append(" != ").append(r13.getUserId());
                        if(db.update(TABLE_USER_INFO,values1,stringBuffer1.toString(),null)<0){
//                            L_0x0117
                            noAffected = true;
                            return false;
                        }else{
                            //L_0x0118  L_0x0164

                            db.setTransactionSuccessful();
                            db.endTransaction();
                            this.closeWriteableDB();
                            return true;
                        }

                    }else{
//                        L_0x0118
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        this.closeWriteableDB();
                        return true;
                    }
                }else{
                    noAffected = true;
                    if(r14){
                        //L_0x00ea
                        ContentValues values1 = new ContentValues();
                        values1.put(COLUMN_IS_LOGIN,0);
                        StringBuffer stringBuffer1 = new StringBuffer(COLUMN_USER_ID);
                        stringBuffer1.append(" != ").append(r13.getUserId());
                        if(db.update(TABLE_USER_INFO,values1,stringBuffer1.toString(),null)<0){
//                            L_0x0117
                            noAffected = true;
//                            return false;
                        }else{
                            //L_0x0118
//                            return false;
                        }

//                        return false;
                    }else{
//                        L_0x0118
//                        return false;
                    }
                    return false;
                }
            }
        }else{
            return false;
        }
        /*



        if (r6 >= 0) goto L_0x0118;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x0117:
        r9 = 1;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x0118:
        if (r9 == 0) goto L_0x0164;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x011a:
        r0 = 0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x011b:
        return r0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
    L_0x011c:
        r0 = "isLogin";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = 0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = java.lang.Integer.valueOf(r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r10.put(r0, r1);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        goto L_0x0092;
    L_0x0128:
        r8 = move-exception;
        r8.printStackTrace();	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r9 = 1;
        if (r9 == 0) goto L_0x0142;
    L_0x012f:
        r0 = 0;
        goto L_0x011b;
    L_0x0131:
        r0 = r12.writableDatabase;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r1 = "table_user_info";	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r2 = 0;	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r6 = r0.insert(r1, r2, r10);	 Catch:{ Exception -> 0x0128, all -> 0x0151 }
        r0 = -1;
        r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1));
        if (r0 != 0) goto L_0x00e8;
    L_0x0140:
        r9 = 1;
        goto L_0x00e8;
    L_0x0142:
        r0 = r12.writableDatabase;
        r0.setTransactionSuccessful();
        r0 = r12.writableDatabase;
        r0.endTransaction();
        r12.closeWriteableDB();
    L_0x014f:
        r0 = 1;
        goto L_0x011b;
    L_0x0151:
        r0 = move-exception;
        if (r9 == 0) goto L_0x0156;
    L_0x0154:
        r0 = 0;
        goto L_0x011b;
    L_0x0156:
        r1 = r12.writableDatabase;
        r1.setTransactionSuccessful();
        r1 = r12.writableDatabase;
        r1.endTransaction();
        r12.closeWriteableDB();
        throw r0;
    L_0x0164:
        r0 = r12.writableDatabase;
        r0.setTransactionSuccessful();
        r0 = r12.writableDatabase;
        r0.endTransaction();
        r12.closeWriteableDB();
        goto L_0x014f;
    L_0x0172:
        r0 = 0;
        goto L_0x011b;
        */
//        throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.UserDBUtil.addUserInfo(com.mobcent.discuz.android.model.UserInfoModel, boolean):boolean");
    }

    public java.util.List<com.mobcent.discuz.android.model.UserInfoModel> getAllUserInfo() {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x00c0 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/

        ArrayList r2 = new ArrayList<>();
        this.openWriteableDB();
        SQLiteDatabase db = this.writableDatabase;
        String query = " select * from table_user_info where userPwd !=  \"\" ";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                UserInfoModel userInfoModel = new UserInfoModel();
                userInfoModel.setUserId(cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID)));
                userInfoModel.setNickname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)));
            }while(cursor.moveToNext());
        }else{
            //L_0x00ad

        }
        if(cursor != null){
            cursor.close();
        }
        this.closeWriteableDB();
        return r2;
        /*
        r7 = this;
        r0 = 0;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r7.openWriteableDB();	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r7.writableDatabase;	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r5 = " select * from table_user_info where userPwd !=  \"\" ";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r6 = 0;	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r0 = r4.rawQuery(r5, r6);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        if (r0 == 0) goto L_0x00ad;	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
    L_0x0014:
        r4 = r0.getCount();	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        if (r4 <= 0) goto L_0x00ad;	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
    L_0x001a:
        r0.moveToFirst();	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
    L_0x001d:
        r3 = new com.mobcent.discuz.android.model.UserInfoModel;	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.<init>();	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userId";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getLong(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setUserId(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userName";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setNickname(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userIcon";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setIcon(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userGender";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getInt(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setGender(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userPermission";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setPermission(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userPwd";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setPwd(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userTitle";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setLevelName(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "extraUserInfo";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setExtraUserInfo(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userToken";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setToken(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = "userSecret";	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getColumnIndex(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.getString(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r3.setSecret(r4);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r2.add(r3);	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r4 = r0.moveToNext();	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        if (r4 != 0) goto L_0x001d;
    L_0x00ad:
        if (r0 == 0) goto L_0x00b2;
    L_0x00af:
        r0.close();
    L_0x00b2:
        r7.closeWriteableDB();
    L_0x00b5:
        return r2;
    L_0x00b6:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x00b6, all -> 0x00c4 }
        r2 = 0;
        if (r0 == 0) goto L_0x00c0;
    L_0x00bd:
        r0.close();
    L_0x00c0:
        r7.closeWriteableDB();
        goto L_0x00b5;
    L_0x00c4:
        r4 = move-exception;
        if (r0 == 0) goto L_0x00ca;
    L_0x00c7:
        r0.close();
    L_0x00ca:
        r7.closeWriteableDB();
        throw r4;
        */
    }

    public java.lang.String getUserJson(long r9, java.lang.String r11) {
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0043 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        this.openWriteableDB();
        SQLiteDatabase db = this.writableDatabase;
        String strQuery = "select * from table_user where userId=? and userType=?";
        String[] values = new String[]{""+r9,r11};
        Cursor cursor = db.rawQuery(strQuery,values);
        String ret = "";
        if(cursor!=null&&cursor.getCount()>0){
            cursor.moveToFirst();
           ret = cursor.getString(cursor.getColumnIndex(COLUMN_USER_JSON));

        }
        if(cursor!=null){
            cursor.close();
        }
        this.closeWriteableDB();
        return ret;

        /*
        r8 = this;
        r0 = 0;
        r2 = "";
        r8.openWriteableDB();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r3 = r8.writableDatabase;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r4 = "select * from table_user where userId=? and userType=?;";	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5 = 2;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r6 = 0;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r7 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5[r6] = r7;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r6 = 1;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r5[r6] = r11;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r0 = r3.rawQuery(r4, r5);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        if (r0 == 0) goto L_0x0030;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
    L_0x001d:
        r3 = r0.getCount();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        if (r3 <= 0) goto L_0x0030;	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
    L_0x0023:
        r0.moveToFirst();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r3 = "userJson";	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r3 = r0.getColumnIndex(r3);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r2 = r0.getString(r3);	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
    L_0x0030:
        if (r0 == 0) goto L_0x0035;
    L_0x0032:
        r0.close();
    L_0x0035:
        r8.closeWriteableDB();
    L_0x0038:
        return r2;
    L_0x0039:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x0039, all -> 0x0047 }
        r2 = 0;
        if (r0 == 0) goto L_0x0043;
    L_0x0040:
        r0.close();
    L_0x0043:
        r8.closeWriteableDB();
        goto L_0x0038;
    L_0x0047:
        r3 = move-exception;
        if (r0 == 0) goto L_0x004d;
    L_0x004a:
        r0.close();
    L_0x004d:
        r8.closeWriteableDB();
        throw r3;
        */
    }

    public UserDBUtil(Context context) {
        super(context);
    }

    public static synchronized UserDBUtil getInstance(Context context) {
        UserDBUtil userDBUtil1;
        synchronized (UserDBUtil.class) {
            if (userDBUtil == null) {
                userDBUtil = new UserDBUtil(context);
            }
            userDBUtil1 = userDBUtil;
        }
        return userDBUtil1;
    }

    public boolean updateUserInfo(UserInfoModel userInfoModel) {
        if (userInfoModel.getUserId() != 0) {
            try {
                openWriteableDB();
                ContentValues values = new ContentValues();
                values.put(UserDBConstant.COLUMN_USER_ICON, userInfoModel.getIcon());
                values.put(UserDBConstant.COLUMN_USER_GENDER, Integer.valueOf(userInfoModel.getGender()));
                if (isRowExisted(this.writableDatabase, UserDBConstant.TABLE_USER_INFO, "userId", userInfoModel.getUserId()) && this.writableDatabase.update(UserDBConstant.TABLE_USER_INFO, values, "userId = " + userInfoModel.getUserId(), null) <= 0) {
                    return false;
                }
                closeWriteableDB();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeWriteableDB();
            }
        }
        return false;
    }

    public boolean savePermission(long userId, String permission) {
        try {
            openWriteableDB();
            if (isRowExisted(this.writableDatabase, UserDBConstant.TABLE_USER_INFO, "userId", userId)) {
                ContentValues values = new ContentValues();
                values.put(UserDBConstant.COLUMN_USER_PERMISSION, permission);
                if (this.writableDatabase.update(UserDBConstant.TABLE_USER_INFO, values, "userId = " + userId, null) <= 0) {
                    return false;
                }
                closeWriteableDB();
                return true;
            }
            closeWriteableDB();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeWriteableDB();
        }
    }

    public boolean changePassword(long userId, String pwd, String token, String secret) {
        try {
            openWriteableDB();
            if (isRowExisted(this.writableDatabase, UserDBConstant.TABLE_USER_INFO, "userId", userId)) {
                ContentValues values = new ContentValues();
                values.put(UserDBConstant.COLUMN_USER_PWD, pwd);
                values.put(UserDBConstant.COLUMN_USER_TOKEN, token);
                values.put(UserDBConstant.COLUNM_USER_SECRET, secret);
                if (this.writableDatabase.update(UserDBConstant.TABLE_USER_INFO, values, "userId = " + userId, null) <= 0) {
                    return false;
                }
                closeWriteableDB();
                return true;
            }
            closeWriteableDB();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeWriteableDB();
        }
    }

    public UserInfoModel getCurrUser(long userId) {
        Exception e;
        Throwable th;
        Cursor cursor = null;
        UserInfoModel userInfoModel = null;
        UserInfoModel userInfoModel2;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.rawQuery(UserDBConstant.SQL_SELECT_INFO_BY_ID, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                userInfoModel2 = new UserInfoModel();
                try {
                    userInfoModel2.setUserId(cursor.getLong(cursor.getColumnIndex("userId")));
                    userInfoModel2.setNickname(cursor.getString(cursor.getColumnIndex("userName")));
                    userInfoModel2.setIcon(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_ICON)));
                    userInfoModel2.setGender(cursor.getInt(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_GENDER)));
                    userInfoModel2.setPermission(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_PERMISSION)));
                    userInfoModel2.setPwd(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_PWD)));
                    userInfoModel2.setLevelName(cursor.getString(cursor.getColumnIndex("userTitle")));
                    userInfoModel2.setExtraUserInfo(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_EXTRA_USER_INFO)));
                    userInfoModel2.setToken(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_TOKEN)));
                    userInfoModel2.setSecret(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUNM_USER_SECRET)));
                    userInfoModel = userInfoModel2;
                } catch (Exception e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        userInfoModel = new UserInfoModel();
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
                        return userInfoModel;
                    } catch (Throwable th2) {
                        th = th2;
                        userInfoModel = userInfoModel2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
                        throw th;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        } catch (Exception e3) {
            e = e3;
            userInfoModel2 = null;
            e.printStackTrace();
            userInfoModel = new UserInfoModel();
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
            return userInfoModel;
        } catch (Throwable th3) {
            th = th3;
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        }
        return userInfoModel;
    }

    public UserInfoModel getLastUserInfo() {
        Exception e;
        Throwable th;
        Cursor cursor = null;
        UserInfoModel userInfoModel = null;
        try {
            openWriteableDB();
            cursor = this.writableDatabase.rawQuery(UserDBConstant.SQL_SELECT_INFO_BY_LOGIN, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                UserInfoModel userInfoModel2 = new UserInfoModel();
                try {
                    userInfoModel2.setUserId(cursor.getLong(cursor.getColumnIndex("userId")));
                    userInfoModel2.setNickname(cursor.getString(cursor.getColumnIndex("userName")));
                    userInfoModel2.setIcon(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_ICON)));
                    userInfoModel2.setGender(cursor.getInt(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_GENDER)));
                    userInfoModel2.setPermission(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_PERMISSION)));
                    userInfoModel2.setPwd(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_PWD)));
                    userInfoModel2.setLevelName(cursor.getString(cursor.getColumnIndex("userTitle")));
                    userInfoModel2.setExtraUserInfo(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_EXTRA_USER_INFO)));
                    userInfoModel2.setToken(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUMN_USER_TOKEN)));
                    userInfoModel2.setSecret(cursor.getString(cursor.getColumnIndex(UserDBConstant.COLUNM_USER_SECRET)));
                    userInfoModel = userInfoModel2;
                } catch (Exception e2) {
                    e = e2;
                    userInfoModel = userInfoModel2;
                    try {
                        e.printStackTrace();
                        userInfoModel = null;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
                        return userInfoModel;
                    } catch (Throwable th2) {
                        th = th2;
                        if (cursor != null) {
                            cursor.close();
                        }
                        closeWriteableDB();
//                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    userInfoModel = userInfoModel2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    closeWriteableDB();
//                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            userInfoModel = null;
            if (cursor != null) {
                cursor.close();
            }
            closeWriteableDB();
            return userInfoModel;
        }
        return userInfoModel;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveUserJson(long r9, java.lang.String r11, java.lang.String r12) {
        this.openWriteableDB();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID,r9);
        values.put(COLUMN_USER_TYPE,r11);
        values.put(COLUMN_USER_JSON,r12);
        SQLiteDatabase db = this.writableDatabase;
        String[] params = new String[]{COLUMN_USER_ID,COLUMN_USER_TYPE};
        String[] pValues = new String[]{""+r9,r11};
        if(this.isRowExisted(db,TABLE_USER,params,pValues)){
            StringBuffer stringBuffer = new StringBuffer("userId = ");
            stringBuffer.append(r9).append(" and ").append("userType = '").append(r11).append("'");
            db.update(TABLE_USER,values,stringBuffer.toString(),null);
        }else {
           db.insert(TABLE_USER,null,values);
        }
        closeWriteableDB();

        /*
        r8 = this;
        r8.openWriteableDB();	 Catch:{ Exception -> 0x007f }
        r1 = new android.content.ContentValues;	 Catch:{ Exception -> 0x007f }
        r1.<init>();	 Catch:{ Exception -> 0x007f }
        r2 = "userId";
        r3 = java.lang.Long.valueOf(r9);	 Catch:{ Exception -> 0x007f }
        r1.put(r2, r3);	 Catch:{ Exception -> 0x007f }
        r2 = "userType";
        r1.put(r2, r11);	 Catch:{ Exception -> 0x007f }
        r2 = "userJson";
        r1.put(r2, r12);	 Catch:{ Exception -> 0x007f }
        r2 = r8.writableDatabase;	 Catch:{ Exception -> 0x007f }
        r3 = "table_user";
        r4 = 2;
        r4 = new java.lang.String[r4];	 Catch:{ Exception -> 0x007f }
        r5 = 0;
        r6 = "userId";
        r4[r5] = r6;	 Catch:{ Exception -> 0x007f }
        r5 = 1;
        r6 = "userType";
        r4[r5] = r6;	 Catch:{ Exception -> 0x007f }
        r5 = 2;
        r5 = new java.lang.String[r5];	 Catch:{ Exception -> 0x007f }
        r6 = 0;
        r7 = java.lang.String.valueOf(r9);	 Catch:{ Exception -> 0x007f }
        r5[r6] = r7;	 Catch:{ Exception -> 0x007f }
        r6 = 1;
        r5[r6] = r11;	 Catch:{ Exception -> 0x007f }
        r2 = r8.isRowExisted(r2, r3, r4, r5);	 Catch:{ Exception -> 0x007f }
        if (r2 == 0) goto L_0x0076;
    L_0x003f:
    */

        /*
        r2 = r8.writableDatabase;	 Catch:{ Exception -> 0x007f }
        r3 = "table_user";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007f }
        r5 = "userId = ";
        r4.<init>(r5);	 Catch:{ Exception -> 0x007f }
        r4 = r4.append(r9);	 Catch:{ Exception -> 0x007f }
        r5 = " and ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r5 = "userType";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r5 = " ='";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r4 = r4.append(r11);	 Catch:{ Exception -> 0x007f }
        r5 = "'";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x007f }
        r4 = r4.toString();	 Catch:{ Exception -> 0x007f }
        r5 = 0;
        r2.update(r3, r1, r4, r5);	 Catch:{ Exception -> 0x007f }
    L_0x0072:
        r8.closeWriteableDB();
    L_0x0075:
        return;
    L_0x0076:
        r2 = r8.writableDatabase;	 Catch:{ Exception -> 0x007f }
        r3 = "table_user";
        r4 = 0;
        r2.insert(r3, r4, r1);	 Catch:{ Exception -> 0x007f }
        goto L_0x0072;
    L_0x007f:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0087 }
        r8.closeWriteableDB();
        goto L_0x0075;
    L_0x0087:
        r2 = move-exception;
        r8.closeWriteableDB();
        throw r2;
        */
        //throw new UnsupportedOperationException("Method not decompiled: com.mobcent.discuz.android.db.UserDBUtil.saveUserJson(long, java.lang.String, java.lang.String):void");
    }
}
