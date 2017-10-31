package com.mobcent.discuz.android.api;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class PostsRestfulApiRequester extends BaseDiscuzApiRequester implements PostsConstant {
    public static String getPortalList(Context context, int page, int pageSize, String moudleId, int isImageList) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_portal_topic_list");
        HashMap<String, String> params = new HashMap();
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        params.put("moduleId", moudleId);
        params.put(PostsConstant.IS_IMAGE_LIST, new StringBuilder(String.valueOf(isImageList)).toString());
        BDLocation location = SharedPreferencesDB.getInstance(context.getApplicationContext()).getLocation();
        if (location != null) {
            params.put("longitude", new StringBuilder(String.valueOf(location.getLongitude())).toString());
            params.put("latitude", new StringBuilder(String.valueOf(location.getLatitude())).toString());
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getTopicList(Context context, long boardId, int page, int pageSize, String sortBy, String filterType, int filterId, int isImageList, int topOrder) {
        String url = MCResource.getInstance(context).getString("mc_forum_topic_list");
        HashMap<String, String> params = new HashMap();
        params.put("boardId", new StringBuilder(String.valueOf(boardId)).toString());
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        params.put(PostsConstant.SORT, sortBy);
        params.put(PostsConstant.FILTER_TYPE, filterType);
        params.put("filterId", new StringBuilder(String.valueOf(filterId)).toString());
        params.put(PostsConstant.IS_IMAGE_LIST, new StringBuilder(String.valueOf(isImageList)).toString());
        params.put(PostsConstant.TOP_ORDER, new StringBuilder(String.valueOf(topOrder)).toString());
        BDLocation location = SharedPreferencesDB.getInstance(context.getApplicationContext()).getLocation();
        if (location != null) {
            params.put("longitude", new StringBuilder(String.valueOf(location.getLongitude())).toString());
            params.put("latitude", new StringBuilder(String.valueOf(location.getLatitude())).toString());
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getUserTopicList(Context context, long userId, int page, int pageSize, String type) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_user_topic_list");
        HashMap<String, String> params = new HashMap();
        params.put("uid", new StringBuilder(String.valueOf(userId)).toString());
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        params.put("type", type);
        BDLocation location = SharedPreferencesDB.getInstance(context.getApplicationContext()).getLocation();
        if (location != null) {
            params.put("longitude", new StringBuilder(String.valueOf(location.getLongitude())).toString());
            params.put("latitude", new StringBuilder(String.valueOf(location.getLatitude())).toString());
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getSearchTopicList(Context context, String keyword, int page, int pageSize, int searchId) {
        String url = new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_forum_imsdk_url"))).append(MCResource.getInstance(context).getString("mc_forum_topic_search")).toString();
        HashMap<String, String> params = new HashMap();
        try {
            if (!MCStringUtil.isEmpty(keyword)) {
                params.put("keyword", URLEncoder.encode(keyword, "utf-8").replaceAll("\\+", "%20"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        params.put("baikeType", "1");
        params.put("searchid", new StringBuilder(String.valueOf(searchId)).toString());
        params.put("baseUrl", MCResource.getInstance(context).getString("mc_discuz_base_request_url"));
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getPhotoList(Context context, long userId, String type, int aid, int page, int pageSize) {
        String url;
        HashMap<String, String> params = new HashMap();
        if (type.equals("list")) {
            url = MCResource.getInstance(context).getString("mc_forum_user_photo_list");
        } else {
            url = MCResource.getInstance(context).getString("mc_forum_user_photo_pic_list");
            params.put("albumId", new StringBuilder(String.valueOf(aid)).toString());
        }
        params.put("uid", new StringBuilder(String.valueOf(userId)).toString());
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getSurroundTopic(Context context, int page, int pageSize, Double latitude, Double longitude, String poi) {
        String url = MCResource.getInstance(context).getString("mc_forum_surround_topic_list");
        HashMap<String, String> params = new HashMap();
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        params.put("poi", poi);
        params.put("longitude", ""+longitude);
        params.put("latitude", ""+latitude);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getTopicDetail(Context context, long boardId, long topicId, long authorId, int page, int pageSize, int type) {
        String url = MCResource.getInstance(context).getString("mc_forum_reply_list");
        HashMap<String, String> params = new HashMap();
        if (boardId > 0) {
            params.put("boardId", String.valueOf(boardId));
        }
        params.put("topicId", String.valueOf(topicId));
        params.put("page", String.valueOf(page));
        params.put("pageSize", String.valueOf(pageSize));
        if (type == 2) {
            params.put("order", String.valueOf(1));
        }
        params.put("authorId", String.valueOf(authorId));

        String ret = BaseDiscuzApiRequester.doPostRequest(context, url, params);
        MCLogUtil.v("RecordUtils","ret:"+ret);
        return ret;
    }

    public static String getPicTopicList(Context context, int page, int pageSize) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_common_picture_set");
        HashMap<String, String> params = new HashMap();
        params.put("page", new StringBuilder(String.valueOf(page)).toString());
        params.put("pageSize", new StringBuilder(String.valueOf(pageSize)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getClassifiedModleList(Context context, int sortId, long boardId) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_post_list");
        HashMap<String, String> params = new HashMap();
        if (sortId > 0) {
            params.put(PostsConstant.CLASSIFICATIONTOP_ID, new StringBuilder(String.valueOf(sortId)).toString());
        }
        params.put("fid", new StringBuilder(String.valueOf(boardId)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String publishTopic(Context context, String json, String action) {
        String url = MCResource.getInstance(context).getString("mc_forum_publish_and_reply");
        HashMap<String, String> params = new HashMap();
        try {
            params.put("json", URLEncoder.encode(json, "utf-8").replaceAll("\\+", "%20"));
            params.put("act", URLEncoder.encode(action, "utf-8").replaceAll("\\+", "%20"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String uploadAttachment(Context context, String attachent, String uploadFile, String module, long boardId) {
        return BaseDiscuzApiRequester.uploadFile(new StringBuilder(String.valueOf(MCResource.getInstance(context).getString("mc_discuz_base_request_url"))).append(MCResource.getInstance(context).getString("mc_forum_post_upload_attachment")).toString(), uploadFile, context, boardId, "audio", attachent, module);
    }

    public static String publishPollTopic(Context context, long boardId, String title, String content, String type, int isVisiable, String deadline, String voteContent, double longitude, double latitude, String location, int requireLocation, String aid, PermissionModel isCheckedSettingModel) {
        String url = MCResource.getInstance(context).getString("mc_forum_publish_poll_topic");
        HashMap<String, String> params = new HashMap();
        if (boardId > 0) {
            params.put("boardId", new StringBuilder(String.valueOf(boardId)).toString());
        }
        try {
            params.put("title", URLEncoder.encode(title, "utf-8").replaceAll("\\+", "%20"));
            params.put("content", URLEncoder.encode(content, "utf-8").replaceAll("\\+", "%20"));
            params.put(PostsConstant.POLLITEM, URLEncoder.encode(voteContent, "utf-8").replaceAll("\\+", "%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("type", new StringBuilder(String.valueOf(type)).toString());
        params.put(PostsConstant.IS_VISIBLE, new StringBuilder(String.valueOf(isVisiable)).toString());
        params.put(PostsConstant.DEADLINE, new StringBuilder(String.valueOf(deadline)).toString());
        if (!(longitude == 0.0d || latitude == 0.0d || MCStringUtil.isEmpty(location))) {
            params.put("longitude", new StringBuilder(String.valueOf(longitude)).toString());
            params.put("latitude", new StringBuilder(String.valueOf(latitude)).toString());
            try {
                params.put("location", URLEncoder.encode(location, "utf-8").replaceAll("\\+", "%20"));
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
            if (requireLocation > 0) {
                params.put("r", new StringBuilder(String.valueOf(requireLocation)).toString());
            }
        }
        params.put("aid", aid);
        params.put("isHidden", new StringBuilder(String.valueOf(isCheckedSettingModel.getIsHidden())).toString());
        params.put("isAnonymous", new StringBuilder(String.valueOf(isCheckedSettingModel.getIsAnonymous())).toString());
        params.put("isOnlyAuthor", new StringBuilder(String.valueOf(isCheckedSettingModel.getIsOnlyAuthor())).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getAnnoDetail(Context context, long announceId) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_announce_detail");
        HashMap<String, String> params = new HashMap();
        params.put("id", new StringBuilder(String.valueOf(announceId)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String getUserPoll(Context context, long boardId, long topicId, String itemId) {
        String url = MCResource.getInstance(context).getString("mc_forum_user_poll");
        HashMap<String, String> params = new HashMap();
        if (boardId > 0) {
            params.put("boardId", new StringBuilder(String.valueOf(boardId)).toString());
        }
        params.put("tid", new StringBuilder(String.valueOf(topicId)).toString());
        params.put(PostsConstant.OPTIONS, new StringBuilder(String.valueOf(itemId)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String support(Context context, long tid, long pid, String type) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_support_topic");
        params.put("tid", new StringBuilder(String.valueOf(tid)).toString());
        params.put(PostsConstant.PID, new StringBuilder(String.valueOf(pid)).toString());
        params.put("type", type);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String favoriteTopic(Context context, long topicId, String idType, String type) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_request_favorite_topic");
        params.put("id", new StringBuilder(String.valueOf(topicId)).toString());
        params.put("action", type);
        params.put("idType", idType);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }

    public static String saveAlbumInfo(Context context, int albumId, String picDesc, String aid) {
        String url = MCResource.getInstance(context).getString("mc_forum_request_save_album");
        HashMap<String, String> params = new HashMap();
        params.put("albumId", new StringBuilder(String.valueOf(albumId)).toString());
        params.put(PostsConstant.PID_DESC, picDesc);
        params.put("ids", aid);
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
