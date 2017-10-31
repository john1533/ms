package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.baidu.location.BDLocation;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.ActivityModel;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BaseResultTopicModel;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.ChoicesModle;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.discuz.android.model.OtherPanelModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.android.model.PollItemModel;
import com.mobcent.discuz.android.model.PollModel;
import com.mobcent.discuz.android.model.PostsModel;
import com.mobcent.discuz.android.model.ReplyModel;
import com.mobcent.discuz.android.model.RulesModle;
import com.mobcent.discuz.android.model.TopModel;
import com.mobcent.discuz.android.model.TopicContentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostsServiceImplHelper implements PostsConstant {
    public static BaseResultModel<List<TopicModel>> getPortalList(Context context, String jsonStr) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
        List<TopicModel> topicList = new ArrayList();
        List<TopicModel> portalRecommTopicList = new ArrayList();
        String baseUrl = "";
        try {
            baseUrl = MCResource.getInstance(context).getString("mc_discuz_base_request_url");
            JSONObject jsonObject = new JSONObject(jsonStr);
            try {
                List portalRecommTopicList2 = getPortalList(jsonObject.optJSONArray(PostsConstant.PORTAL_RECOMM_TOPIC_LIST), baseUrl);
                List topicList2 = getPortalList(jsonObject.optJSONArray("list"), baseUrl);
                if (topicList2.size() > 0) {
                    ((TopicModel) topicList2.get(0)).setPortalRecommList(portalRecommTopicList2);
                } else if (MCListUtils.isEmpty(topicList2) && !MCListUtils.isEmpty(portalRecommTopicList2)) {
                    TopicModel model = new TopicModel();
                    model.setPortalRecommList(portalRecommTopicList2);
                    topicList2.add(model);
                }
                baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
                baseResultModel.setPage(jsonObject.optInt("page"));
                baseResultModel.setRs(jsonObject.optInt("rs"));
                baseResultModel.setHasNext(jsonObject.optInt("has_next"));
                baseResultModel.setData(topicList2);
                JSONObject jSONObject = jsonObject;
            } catch (Exception e) {
//                jSONObject = jsonObject;
                baseResultModel.setRs(0);
                e.printStackTrace();
                return baseResultModel;
            }
        } catch (Exception e2) {
            Exception e3;
            e3 = e2;
            baseResultModel.setRs(0);
            e3.printStackTrace();
            return baseResultModel;
        }
        return baseResultModel;
    }

    public static List<TopicModel> getPortalList(JSONArray array, String baseUrl) {
        List<TopicModel> portalRecommList = new ArrayList();
        if (array != null) {
            int j = array.length();
            for (int i = 0; i < j; i++) {
                JSONObject jobj = array.optJSONObject(i);
                TopicModel topicModel = new TopicModel();
                topicModel.setBoardId(jobj.optLong("fid"));
                topicModel.setTitle(jobj.optString("title"));
                topicModel.setLastReplyDate(jobj.optLong("last_reply_date"));
                topicModel.setHits((long) jobj.optInt("hits"));
                topicModel.setReplies((long) jobj.optInt("replies"));
                topicModel.setUserId((long) jobj.optInt("user_id"));
                topicModel.setUserName(jobj.optString("user_nick_name"));
                topicModel.setPicToUrl(jobj.optString(PostsConstant.PIC_TO_PATH));
                String picPath = jobj.optString("pic_path");
                topicModel.setPicPath(jobj.optString("pic_path"));
                if (!MCStringUtil.isEmpty(picPath)) {
                    if (picPath.indexOf("http") > -1) {
                        topicModel.setPicPath(topicModel.getPicPath());
                    } else {
                        topicModel.setPicPath(new StringBuilder(String.valueOf(baseUrl)).append(topicModel.getPicPath()).toString());
                    }
                }
                topicModel.setPicPath(topicModel.getPicPath());
                topicModel.setBoardId(jobj.optLong("fid", 0));
                topicModel.setSubject(jobj.optString("summary"));
                topicModel.setSourceType(jobj.optString("source_type"));
                topicModel.setTopicId(jobj.optLong(PostsConstant.SOURCE_ID));
                topicModel.setRedirectUrl(jobj.optString(PostsConstant.REDIRECT_URL));
                if (!jobj.has("ratio")) {
                    topicModel.setRatio(CustomConstant.RATIO_ONE_HEIGHT);
                } else if (((double) ((float) jobj.optDouble("ratio"))) < 0.5d) {
                    topicModel.setRatio(0.5f);
                } else if (((double) ((float) jobj.optDouble("ratio"))) >= 2.0d) {
                    topicModel.setRatio(2.0f);
                } else {
                    topicModel.setRatio((float) jobj.optDouble("ratio"));
                }
                topicModel.setGender(jobj.optInt("gender"));
                topicModel.setIcon(jobj.isNull(PostsConstant.USER_ICON) ? "" : jobj.optString(PostsConstant.USER_ICON));
                topicModel.setRecommendAdd(jobj.isNull(PostsConstant.RECOMMEND_ADD) ? 0 : jobj.optInt(PostsConstant.RECOMMEND_ADD));
                topicModel.setIsHasRecommendAdd(jobj.isNull("isHasRecommendAdd") ? -1 : jobj.optInt("isHasRecommendAdd"));
                topicModel.setForumTopicUrl(jobj.isNull(PostsConstant.FORUM_TOPIC_URL) ? "" : jobj.optString(PostsConstant.FORUM_TOPIC_URL));
                JSONArray imageListArray = jobj.isNull(PostsConstant.IMAGE_LIST) ? null : jobj.optJSONArray(PostsConstant.IMAGE_LIST);
                List<String> imageList = new ArrayList();
                if (imageListArray != null) {
                    for (int k = 0; k < imageListArray.length(); k++) {
                        imageList.add(imageListArray.optString(k));
                    }
                }
                topicModel.setImageList(imageList);
                portalRecommList.add(topicModel);
            }
        }
        return portalRecommList;
    }

    public static BaseResultModel<List<TopicModel>> getTopicList(Context context, String jsonStr, long boardId) {
        JSONObject jSONObject;
        Exception e;
        BaseResultTopicModel<List<TopicModel>> baseResultModel = new BaseResultTopicModel();
        BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
        List<TopicModel> topicList = new ArrayList();
        String baseUrl = "";
        try {
            MCLogUtil.v("Json","jsonStr:"+jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);
            try {
                int i;
                baseUrl = MCResource.getInstance(context).getString("mc_discuz_base_request_url");
                JSONArray topArray = jsonObject.optJSONArray(PostsConstant.TOP_TOPICLIST);
                if (topArray != null) {
                    List<TopModel> list = new ArrayList();
                    for (i = 0; i < topArray.length(); i++) {
                        JSONObject object = topArray.optJSONObject(i);
                        TopModel topModel = new TopModel();
                        topModel.setId(object.optLong("id", 0));
                        topModel.setTitle(object.optString("title"));
                        list.add(topModel);
                    }
                    baseResultModel.setTopTopicList(list);
                }
                JSONObject jsonObject2 = jsonObject.optJSONObject(PostsConstant.FORUM_INFO);
                BoardChild boardChild = new BoardChild();
                if (jsonObject2 != null) {
                    boardChild.setBoardName(jsonObject2.optString("title"));
                    boardChild.setBoardImg(jsonObject2.optString("icon"));
                    boardChild.setBoardId(jsonObject2.optLong("id"));
                    boardChild.setDescription(jsonObject2.optString("description"));
                }
                baseResultModel.setForumInfo(boardChild);
                JSONArray jsonArrayAnno = jsonObject.optJSONArray(PostsConstant.ANNO_LIST);
                if (jsonArrayAnno != null) {
                    int j = jsonArrayAnno.length();
                    for (i = 0; i < j; i++) {
                        baseResultModel.getForumInfo().setHaveAnnoModel(true);
                        JSONObject jobjAnno = jsonArrayAnno.optJSONObject(i);
                        TopicModel annoModel = new AnnoModel();
                        ((AnnoModel) annoModel).setAnnoId((long) jobjAnno.optInt(PostsConstant.ANNO_ID));
                        ((AnnoModel) annoModel).setAuthor(jobjAnno.optString("author"));
                        annoModel.setBoardId(jobjAnno.optLong("board_id", boardId));
                        ((AnnoModel) annoModel).setTitle(jobjAnno.optString("title"));
                        ((AnnoModel) annoModel).setAnnoStartDate(jobjAnno.optLong(PostsConstant.ANNO_START_DATE));
                        ((AnnoModel) annoModel).setAnnoUrl(jobjAnno.optString("redirect"));
                        topicList.add(annoModel);
                    }
                }
                topicList = getTopicList(topicList, jsonObject.optJSONArray("list"), baseUrl, boardId);
                baseResultModel.setData(topicList);
                if (topicList.size() > 0) {
                    baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
                    baseResultModel.setPage(jsonObject.optInt("page"));
                    baseResultModel.setRs(jsonObject.optInt("rs"));
                    baseResultModel.setHasNext(jsonObject.optInt("has_next"));
                    jSONObject = jsonObject;
                } else if (jsonObject.optString("rs").equals("0")) {
                    baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
                    baseResultModel.setRs(jsonObject.optInt("rs"));
                    baseResultModel.setErrorCode(jsonObject.optString("errcode"));
                    baseResultModel.setErrorInfo(jsonObject.optString("errInfo"));
                    baseResultModel.setAlert(jsonObject.optInt("alert"));
                    jSONObject = jsonObject;
                }
            } catch (Exception e2) {
                e = e2;
                jSONObject = jsonObject;
                baseResultModel.setRs(0);
                e.printStackTrace();
                return baseResultModel;
            }
        } catch (Exception e3) {
            e = e3;
            baseResultModel.setRs(0);
            e.printStackTrace();
            return baseResultModel;
        }
        return baseResultModel;
    }

    public static List<TopicModel> getTopicList(List<TopicModel> list, JSONArray jsonArrayTopic, String baseUrl, long boardId) {
        if (jsonArrayTopic != null) {
            int len = jsonArrayTopic.length();
            for (int i = 0; i < len; i++) {
                JSONObject jobj = jsonArrayTopic.optJSONObject(i);
                TopicModel topicModel = new TopicModel();
                topicModel.setTopicId(jobj.optLong("topic_id"));
                topicModel.setTitle(jobj.optString("title"));
                topicModel.setLastReplyDate(jobj.optLong("last_reply_date"));
                topicModel.setHits((long) jobj.optInt("hits"));
                topicModel.setReplies((long) jobj.optInt("replies"));
                topicModel.setUserId((long) jobj.optInt("user_id"));
                topicModel.setUserName(jobj.optString("user_nick_name"));
                topicModel.setVote(jobj.optInt("vote"));
                String picPath = jobj.optString("pic_path");
                if (!MCStringUtil.isEmpty(picPath)) {
                    if (picPath.indexOf("http") > -1) {
                        topicModel.setPicPath(picPath);
                    } else {
                        topicModel.setPicPath(new StringBuilder(String.valueOf(baseUrl)).append(picPath).toString());
                    }
                }
                topicModel.setBoardId(jobj.optLong("board_id", boardId));
                topicModel.setTop(jobj.optInt("top"));
                topicModel.setBoardName(jobj.optString("board_name"));
                topicModel.setHot(jobj.optInt("hot"));
                topicModel.setEssence(jobj.optInt("essence"));
                topicModel.setAid(jobj.optInt("aid"));
                topicModel.setSubject(jobj.optString(PostsConstant.SUBJECT));
                topicModel.setStatus(jobj.optInt("status"));
                if (!jobj.isNull("location")) {
                    topicModel.setLocation(jobj.optString("location"));
                }
                if (!jobj.isNull("distance")) {
                    topicModel.setDistance(jobj.optDouble("distance"));
                }
                if (!jobj.has("ratio")) {
                    topicModel.setRatio(CustomConstant.RATIO_ONE_HEIGHT);
                } else if (((double) ((float) jobj.optDouble("ratio"))) < 0.5d) {
                    topicModel.setRatio(0.5f);
                } else if (((double) ((float) jobj.optDouble("ratio"))) > 2.0d) {
                    topicModel.setRatio(2.0f);
                } else {
                    topicModel.setRatio((float) jobj.optDouble("ratio"));
                }
                topicModel.setGender(jobj.optInt("gender"));
                topicModel.setIcon(jobj.isNull(PostsConstant.USER_ICON) ? "" : jobj.optString(PostsConstant.USER_ICON));
                topicModel.setRecommendAdd(jobj.isNull(PostsConstant.RECOMMEND_ADD) ? 0 : jobj.optInt(PostsConstant.RECOMMEND_ADD));
                topicModel.setIsHasRecommendAdd(jobj.isNull("isHasRecommendAdd") ? -1 : jobj.optInt("isHasRecommendAdd"));
                topicModel.setForumTopicUrl(jobj.isNull(PostsConstant.FORUM_TOPIC_URL) ? "" : jobj.optString(PostsConstant.FORUM_TOPIC_URL));
                JSONArray imageListArray = jobj.isNull(PostsConstant.IMAGE_LIST) ? null : jobj.optJSONArray(PostsConstant.IMAGE_LIST);
                List<String> imageList = new ArrayList();
                if (imageListArray != null) {
                    for (int j = 0; j < imageListArray.length(); j++) {
                        imageList.add(imageListArray.optString(j));
                    }
                }
                topicModel.setImageList(imageList);
                list.add(topicModel);
            }
        }
        return list;
    }

    public static BaseResultModel<UploadPictureModel> parseUploadImageJson(Context context, String jsonStr) {
        BaseResultModel<UploadPictureModel> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj.has("pic_path")) {
                    UploadPictureModel model = new UploadPictureModel();
                    model.setPicPath(jsonObj.optString("pic_path"));
                    baseResultModel.setData(model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<TopicModel>> getSearchTopicList(Context context, String jsonStr, long boardId) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        List<TopicModel> list = new ArrayList();
        try {
            JSONObject phpResultObject = new JSONObject(jsonStr);
            if (phpResultObject.optInt("rs") == 1) {
                JSONObject jsonObject = phpResultObject.optJSONObject(PostsConstant.PHP_RESULT);
                baseResultModel.setRs(jsonObject.optInt("rs"));
                JSONObject headObject = jsonObject.optJSONObject("head");
                baseResultModel.setAlert(headObject.optInt("alert"));
                if (headObject.optInt("alert") == 1) {
                    baseResultModel.setErrorInfo(headObject.optString("errInfo"));
                }
                if (jsonObject.optInt("rs") == 1) {
                    baseResultModel.setData(getTopicList(list, jsonObject.optJSONArray("list"), MCResource.getInstance(context).getString("mc_discuz_base_request_url"), boardId));
                    baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
                    baseResultModel.setPage(jsonObject.optInt("page"));
                    baseResultModel.setSearchId(jsonObject.optInt("searchid"));
                    baseResultModel.setHasNext(jsonObject.optInt("has_next"));
                }
            } else {
                baseResultModel.setRs(phpResultObject.optInt("rs"));
                baseResultModel.setErrorCode(phpResultObject.optString("errcode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<PhotoModel>> getPhotoList(Context context, String jsonStr) {
        List<PhotoModel> photoList = new ArrayList();
        BaseResultModel<List<PhotoModel>> baseResultModel = new BaseResultModel();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            baseResultModel.setHasNext(jsonObject.optInt("has_next"));
            baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
            baseResultModel.setPage(jsonObject.optInt("page"));
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                JSONArray array = jsonObject.getJSONArray("list");
                for (int i = 0; i < array.length(); i++) {
                    PhotoModel model = new PhotoModel();
                    JSONObject object = array.getJSONObject(i);
                    model.setAlbumId(object.optInt(PostsConstant.ALBUM_ID));
                    model.setUserId(object.optLong("user_id"));
                    model.setCreateDate(object.optLong(PostsConstant.RELEASE_DATE));
                    model.setLastReplyDate((long) object.optInt("last_reply_date"));
                    model.setUserName(object.optString("user_nick_name"));
                    model.setTitle(object.optString("title"));
                    model.setInfo(object.optString("info"));
                    model.setThumbnailUrl(object.optString(PostsConstant.THUMB_PIC));
                    model.setOriginalUrl(object.optString(PostsConstant.ORIGIN_PIC));
                    model.setHitCount(object.optInt("hot"));
                    model.setReplieCount(object.optInt("replies"));
                    model.setPicId(object.optInt(PostsConstant.PIC_ID));
                    photoList.add(model);
                }
                baseResultModel.setData(photoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<TopicModel>> getSurroundtopicList(Context context, String jsonStr) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        String baseUrl = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            baseUrl = MCResource.getInstance(context).getString("mc_discuz_base_request_url");
            BaseResultModel<Object> model = BaseJsonHelper.formJsonRs(jsonStr, context);
            if (model.getRs() == 1) {
                baseResultModel.setData(getTopicList(new ArrayList(), jsonObject.optJSONArray("pois"), baseUrl, 0));
                baseResultModel.setHasNext(jsonObject.optInt("has_next"));
                baseResultModel.setTotalNum(jsonObject.optInt("total_num"));
                baseResultModel.setPage(jsonObject.optInt("page"));
            }
            baseResultModel.setRs(model.getRs());
            baseResultModel.setErrorCode(model.getErrorCode());
            baseResultModel.setErrorInfo(model.getErrorInfo());
            baseResultModel.setAlert(model.getAlert());
        } catch (JSONException e) {
            e.printStackTrace();
            baseResultModel.setRs(0);
        }
        return baseResultModel;
    }

    public static BaseResultModel<List<Object>> getTopicDetail(Context context, String jsonStr) {
        List<Object> list;
        BaseResultModel<List<Object>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                List<Object> detailList = new ArrayList();
                try {
                    List contentList;
                    JSONObject jObject = new JSONObject(jsonStr);
                    JSONObject topicObject = jObject.optJSONObject("topic");
                    if (topicObject != null) {
                        ActivityModel activityModel;
                        Map map;
                        PostsModel postsModel = new PostsModel();
                        postsModel.setTopicId(topicObject.optLong("topic_id"));
                        postsModel.setTitle(topicObject.optString("title"));
                        postsModel.setType(topicObject.optString("type"));
                        postsModel.setSortId(topicObject.optLong("sortId"));
                        postsModel.setUserId(topicObject.optLong("user_id"));
                        postsModel.setUserNickName(topicObject.optString("user_nick_name"));
                        postsModel.setReplies(topicObject.optInt("replies"));
                        postsModel.setHits(topicObject.optInt("hits"));
                        postsModel.setEssence(topicObject.optInt("essence"));
                        postsModel.setHot(topicObject.optInt("hot"));
                        postsModel.setTop(topicObject.optInt("top"));
                        postsModel.setIsFavor(topicObject.optInt("is_favor"));
                        postsModel.setCreateDate(Long.valueOf(topicObject.optString(PostsConstant.CREATE_DATE)).longValue());
                        postsModel.setIcon(topicObject.optString("icon"));
                        postsModel.setLevel(topicObject.optInt("level"));
                        postsModel.setUserTitle(topicObject.optString("userTitle"));
                        contentList = (!topicObject.has("content") || topicObject.isNull("content")) ? null : getContentList(topicObject.optJSONArray("content"));
                        postsModel.setContent(contentList);
                        PollModel pollInfo = (!topicObject.has(PostsConstant.POLL_INFO) || topicObject.isNull(PostsConstant.POLL_INFO)) ? null : getPollInfo(topicObject.optJSONObject(PostsConstant.POLL_INFO));
                        postsModel.setPollInfo(pollInfo);
                        if (!topicObject.has(PostsConstant.ACTIVITY_INFO) || topicObject.isNull(PostsConstant.ACTIVITY_INFO)) {
                            activityModel = null;
                        } else {
                            activityModel = getActivityInfo(topicObject.optJSONObject(PostsConstant.ACTIVITY_INFO));
                        }
                        postsModel.setActivityInfo(activityModel);
                        boolean z = topicObject.has(PostsConstant.ACTIVITY_INFO) && !topicObject.isNull(PostsConstant.ACTIVITY_INFO);
                        postsModel.setHasActivityInfo(z);
                        postsModel.setLocation(topicObject.optString("location"));
                        postsModel.setMobileSign(topicObject.optString(PostsConstant.MOBILE_SIGN));
                        postsModel.setPostsId(topicObject.optLong(PostsConstant.REPLY_POSTS_ID));
                        if (topicObject.isNull("managePanel") || topicObject.optJSONArray("managePanel") == null) {
                            map = null;
                        } else {
                            map = getManagePanel(topicObject.optJSONArray("managePanel"));
                        }
                        postsModel.setManagePanel(map);
                        if (topicObject.isNull(PostsConstant.EXTRA_PANEL) || topicObject.optJSONArray(PostsConstant.EXTRA_PANEL) == null) {
                            map = null;
                        } else {
                            map = getExtraPanel(topicObject.optJSONArray(PostsConstant.EXTRA_PANEL));
                        }
                        postsModel.setExtraPanel(map);
                        if (topicObject.isNull(PostsConstant.RATE_LIST) || topicObject.optJSONObject(PostsConstant.RATE_LIST) == null) {
                            map = null;
                        } else {
                            map = getRate(topicObject.optJSONObject(PostsConstant.RATE_LIST));
                        }
                        postsModel.setRateList(map);
                        String optString = (topicObject.isNull(PostsConstant.RATE_LIST) || topicObject.optJSONObject(PostsConstant.RATE_LIST) == null || topicObject.optJSONObject(PostsConstant.RATE_LIST).isNull(PostsConstant.SHOW_ALL_URL)) ? null : topicObject.optJSONObject(PostsConstant.RATE_LIST).optString(PostsConstant.SHOW_ALL_URL);
                        postsModel.setRateShowAllUrl(optString);
                        postsModel.setForumTopicUrl(jObject.optString(PostsConstant.FORUM_TOPIC_URL));
                        postsModel.setBoardName(jObject.optString(PostsConstant.FORUM_NAME));
                        detailList.add(postsModel);
                    }
                    JSONArray replyArray = jObject.optJSONArray("list");
                    if (replyArray != null && replyArray.length() > 0) {
                        for (int i = 0; i < replyArray.length(); i++) {
                            JSONObject replyObject = replyArray.optJSONObject(i);
                            if (replyObject != null) {
                                ReplyModel replyModel = new ReplyModel();
                                replyModel.setReplyUserId(replyObject.optLong(PostsConstant.REPLY_USER_ID));
                                replyModel.setReplyType(replyObject.optString(PostsConstant.REPLY_TYPE));
                                replyModel.setReplyName(replyObject.optString("reply_name"));
                                replyModel.setReplyPostsId(replyObject.optLong(PostsConstant.REPLY_POSTS_ID));
                                replyModel.setPosition(replyObject.optInt("position"));
                                replyModel.setPostsDate(Long.valueOf(replyObject.optString("posts_date")).longValue());
                                replyModel.setIcon(replyObject.optString("icon"));
                                replyModel.setLevel(replyObject.optInt("level"));
                                replyModel.setUserTitle(replyObject.optString("userTitle"));
                                replyModel.setLocation(replyObject.optString("location"));
                                replyModel.setMobileSign(replyObject.optString(PostsConstant.MOBILE_SIGN));
                                replyModel.setIsQuote(replyObject.optInt(PostsConstant.REPLY_IS_QUOTE));
                                replyModel.setQuotePid(replyObject.optLong(PostsConstant.QUOTE_PID));
                                replyModel.setQuoteContent(replyObject.optString(PostsConstant.QUOTE_CONTENT));
                                replyModel.setQuoteUserName(replyObject.optString(PostsConstant.QUOTE_USER_NAME));
                                if (!replyObject.has("reply_content") || replyObject.isNull("reply_content")) {
                                    contentList = null;
                                } else {
                                    contentList = getContentList(replyObject.optJSONArray("reply_content"));
                                }
                                replyModel.setReplyContent(contentList);
                                replyModel.setManagePanel(replyObject.isNull("managePanel") ? null : getManagePanel(replyObject.optJSONArray("managePanel")));
                                replyModel.setExtraPanel(replyObject.isNull(PostsConstant.EXTRA_PANEL) ? null : getExtraPanel(replyObject.optJSONArray(PostsConstant.EXTRA_PANEL)));
                                detailList.add(replyModel);
                            }
                        }
                    }
                    baseResultModel.setHasNext(jObject.optInt("has_next"));
                    baseResultModel.setTotalNum(jObject.optInt("total_num") == 0 ? jObject.optInt("total_num") + 1 : jObject.optInt("total_num"));
                    baseResultModel.setData(detailList);
                    list = detailList;
                } catch (Exception e) {
                    Exception e2 = e;
                    list = detailList;
                    e2.printStackTrace();
                    baseResultModel.setRs(0);
                    return baseResultModel;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            baseResultModel.setRs(0);
            return baseResultModel;
        }
        return baseResultModel;
    }

    private static List<TopicContentModel> getContentList(JSONArray contentArray) {
        List<TopicContentModel> contentList = new ArrayList();
        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject contentObject = contentArray.optJSONObject(i);
            TopicContentModel contentModel = new TopicContentModel();
            contentModel.setType(contentObject.optInt("type"));
            if (contentObject.has("infor")) {
                contentModel.setInfor(contentObject.optString("infor"));
            }
            if (contentObject.has("url")) {
                contentModel.setUrl(contentObject.optString("url"));
            }
            if (contentObject.has(PostsConstant.ORIGINAL_INFO)) {
                contentModel.setOriginalInfo(contentObject.optString(PostsConstant.ORIGINAL_INFO));
            }
            if (contentObject.has("desc")) {
                contentModel.setDesc(contentObject.optString("desc"));
            }
            contentList.add(contentModel);
        }
        return contentList;
    }

    private static PollModel getPollInfo(JSONObject pollInfoObject) {
        PollModel pollModel = null;
        if (pollInfoObject != null) {
            if (!pollInfoObject.has(PostsConstant.DEAD_LINE)) {
                return null;
            }
            int i;
            long time = Long.valueOf(pollInfoObject.optString(PostsConstant.DEAD_LINE)).longValue();
            pollModel = new PollModel();
            pollModel.setDeadLine(time);
            pollModel.setIsVisible(1);
            pollModel.setVoters(pollInfoObject.optInt(PostsConstant.VOTERS));
            pollModel.setType(pollInfoObject.optInt("type"));
            pollModel.setPollStatus(pollInfoObject.optInt(PostsConstant.POLL_STATUS));
            if (pollInfoObject.has(PostsConstant.POLL_ID) && !pollInfoObject.isNull(PostsConstant.POLL_ID)) {
                JSONArray pollIdArray = pollInfoObject.optJSONArray(PostsConstant.POLL_ID);
                List<Long> pollId = new ArrayList();
                for (i = 0; i < pollIdArray.length(); i++) {
                    pollId.add(Long.valueOf(pollIdArray.optLong(i)));
                }
                pollModel.setPollId(pollId);
            }
            if (pollInfoObject.has(PostsConstant.POLL_ITEM_LIST) && !pollInfoObject.isNull(PostsConstant.POLL_ITEM_LIST)) {
                JSONArray pollItemListArray = pollInfoObject.optJSONArray(PostsConstant.POLL_ITEM_LIST);
                int count = pollModel.getVoters();
                List<PollItemModel> pollItemList = new ArrayList();
                for (i = 0; i < pollItemListArray.length(); i++) {
                    JSONObject itemObject = pollItemListArray.optJSONObject(i);
                    PollItemModel pollItemModel = new PollItemModel();
                    pollItemModel.setPollName(itemObject.optString("name"));
                    pollItemModel.setPollItemId(itemObject.optLong(PostsConstant.POLL_ITEM_ID));
                    pollItemModel.setTotalNum(itemObject.optInt("total_num"));
                    pollItemModel.setPercent(itemObject.optString(PostsConstant.PERCENT));
                    if (count != 0) {
                        pollItemModel.setRatio(((double) pollItemModel.getTotalNum()) / ((double) count));
                    }
                    pollItemModel.setResultVisiable(pollModel.getIsVisible());
                    pollItemModel.setItemId(i + 1);
                    pollItemList.add(pollItemModel);
                }
                pollModel.setPollItemList(pollItemList);
            }
        }
        return pollModel;
    }

    private static ActivityModel getActivityInfo(JSONObject activityInfoObject) {
        ActivityModel activityModel = null;
        if (activityInfoObject != null) {
            activityModel = new ActivityModel();
            activityModel.setImage(activityInfoObject.optString("image"));
            activityModel.setSummary(activityInfoObject.optString("summary"));
            if (activityInfoObject.has("action") && !activityInfoObject.isNull("action")) {
                JSONObject actionObject = activityInfoObject.optJSONObject("action");
                activityModel.setType(actionObject.optString("type"));
                activityModel.setTitle(actionObject.optString("title"));
                activityModel.setDescription(actionObject.optString("description"));
            }
            if (activityInfoObject.has(PostsConstant.APPLY_LIST) && !activityInfoObject.isNull(PostsConstant.APPLY_LIST)) {
                JSONObject applyListObject = activityInfoObject.optJSONObject(PostsConstant.APPLY_LIST);
                ActivityModel applyList = new ActivityModel();
                applyList.setTitle(applyListObject.optString("title"));
                applyList.setSummary(applyListObject.optString("summary"));
                activityModel.setApplyList(applyList);
            }
            if (activityInfoObject.has(PostsConstant.APPLY_LIST_VERIFIED) && !activityInfoObject.isNull(PostsConstant.APPLY_LIST_VERIFIED)) {
                List list;
                JSONObject applyListVerifiedObject = activityInfoObject.optJSONObject(PostsConstant.APPLY_LIST_VERIFIED);
                ActivityModel applyListVerified = new ActivityModel();
                applyListVerified.setTitle(applyListVerifiedObject.optString("title"));
                applyListVerified.setSummary(applyListVerifiedObject.optString("summary"));
                if (!applyListVerifiedObject.has("content") || applyListVerifiedObject.isNull("content")) {
                    list = null;
                } else {
                    list = getContentList(applyListVerifiedObject.optJSONArray("content"));
                }
                applyListVerified.setContent(list);
                activityModel.setApplyListVerified(applyListVerified);
            }
        }
        return activityModel;
    }

    private static Map<String, OtherPanelModel> getManagePanel(JSONArray managePanelArray) {
        Map<String, OtherPanelModel> managePanel = new HashMap();
        for (int i = 0; i < managePanelArray.length(); i++) {
            JSONObject object = managePanelArray.optJSONObject(i);
            OtherPanelModel model = new OtherPanelModel();
            model.setAction(object.optString("action").trim());
            model.setTitle(object.optString("title"));
            model.setType(object.optString("type"));
            managePanel.put(model.getTitle(), model);
        }
        return managePanel;
    }

    private static Map<String, OtherPanelModel> getExtraPanel(JSONArray extraPanelArray) {
        Map<String, OtherPanelModel> extraPanel = new HashMap();
        for (int i = 0; i < extraPanelArray.length(); i++) {
            JSONObject jsonObject = extraPanelArray.optJSONObject(i);
            OtherPanelModel model = new OtherPanelModel();
            model.setAction(jsonObject.optString("action").trim());
            model.setTitle(jsonObject.optString("title"));
            model.setType(jsonObject.optString("type"));
            if (!jsonObject.isNull("extParams")) {
                JSONObject extParamsObject = jsonObject.optJSONObject("extParams");
                if (extParamsObject.has(PostsConstant.BEFORE_ACTION)) {
                    model.setBeforeAction(extParamsObject.optString(PostsConstant.BEFORE_ACTION));
                }
                if (extParamsObject.has(PostsConstant.RECOMMEND_ADD)) {
                    model.setRecommendAdd(extParamsObject.optInt(PostsConstant.RECOMMEND_ADD));
                }
                if (extParamsObject.has("isHasRecommendAdd")) {
                    model.setHasRecommendAdd(extParamsObject.optInt("isHasRecommendAdd") == 1);
                }
            }
            extraPanel.put(model.getType(), model);
        }
        return extraPanel;
    }

    private static Map<String, List<String>> getRate(JSONObject rateObject) {
        Map<String, List<String>> rate = new LinkedHashMap();
        if (!rateObject.isNull("head")) {
            List<String> headList = new ArrayList();
            JSONObject headObject = rateObject.optJSONObject("head");
            headList.add(headObject.optString(PostsConstant.FIELD1));
            headList.add(headObject.optString(PostsConstant.FIELD2));
            headList.add(headObject.optString(PostsConstant.FIELD3));
            rate.put("head", headList);
        }
        if (!rateObject.isNull("total")) {
            List<String> totalList = new ArrayList();
            JSONObject totalObject = rateObject.optJSONObject("total");
            totalList.add(totalObject.optString(PostsConstant.FIELD1));
            totalList.add(totalObject.optString(PostsConstant.FIELD2));
            totalList.add(totalObject.optString(PostsConstant.FIELD3));
            rate.put("total", totalList);
        }
        JSONArray array = rateObject.optJSONArray("body");
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                List<String> list = new ArrayList();
                JSONObject object = array.optJSONObject(i);
                list.add(object.optString(PostsConstant.FIELD1));
                list.add(object.optString(PostsConstant.FIELD2));
                list.add(object.optString(PostsConstant.FIELD3));
                rate.put("body" + i, list);
            }
        }
        return rate;
    }

    private static boolean isUserFollow(Context context, long userId) {
        boolean isFollow = false;
        BaseResultModel<List<UserInfoModel>> baseResultModel = new UserServiceImpl(context).getMentionFriend(userId, true);
        if (baseResultModel.getRs() == 1) {
            List<UserInfoModel> friendList = (List) baseResultModel.getData();
            if (friendList != null && !friendList.isEmpty()) {
                for (UserInfoModel user : friendList) {
                    if (user.getUserId() == userId) {
                        isFollow = true;
                        break;
                    }
                }
            }
//          return false;
        }
        return isFollow;
    }

    public static String createPublishNormalJsonStr(Context context, long boardId, String title, String content, double longitude, double latitude, String location, int requireLocation, String aid, int classificationTypeId, int classificationTopId, PermissionModel settingModel) {
        Exception e;
        JSONObject jSONObject;
        String json;
        JSONObject object = null;
        try {
            JSONObject object2 = new JSONObject();
            try {
                JSONObject jsonObject;
                JSONObject bodyObject = new JSONObject();
                try {
                    jsonObject = new JSONObject();
                } catch (Exception e2) {
                    e = e2;
                    jSONObject = bodyObject;
                    object = object2;
                    e.printStackTrace();
                    json = object.toString();
                    MCLogUtil.e("PostsServiceImplHelper", "json->" + json);
                    return json;
                }
                JSONObject jSONObject2;
                try {
                    jsonObject.put("fid", boardId);
                    if (location == null) {
                        jsonObject.put("location", "");
                    } else {
                        jsonObject.put("location", location);
                    }
                    jsonObject.put("aid", aid);
                    jsonObject.put("content", content);
                    jsonObject.put("title", title);
                    BDLocation bdLocation = SharedPreferencesDB.getInstance(context.getApplicationContext()).getLocation();
                    if (bdLocation != null) {
                        longitude = bdLocation.getLongitude();
                        latitude = bdLocation.getLatitude();
                    }
                    jsonObject.put("longitude", String.valueOf(longitude));
                    jsonObject.put("latitude", String.valueOf(latitude));
                    jsonObject.put("isHidden", settingModel.getIsHidden());
                    jsonObject.put("isAnonymous", settingModel.getIsAnonymous());
                    jsonObject.put("isOnlyAuthor", settingModel.getIsOnlyAuthor());
                    jsonObject.put(PostsConstant.IS_SHOW_POSITION, requireLocation);
                    jsonObject.put("typeId", classificationTypeId);
                    bodyObject.put("json", jsonObject);
                    object2.put("body", bodyObject);
                    jSONObject2 = jsonObject;
                    jSONObject = bodyObject;
                    object = object2;
                } catch (Exception e3) {
                    e = e3;
                    jSONObject2 = jsonObject;
                    jSONObject = bodyObject;
                    object = object2;
                    e.printStackTrace();
                    json = object.toString();
                    MCLogUtil.e("PostsServiceImplHelper", "json->" + json);
                    return json;
                }
            } catch (Exception e4) {
                e = e4;
                object = object2;
                e.printStackTrace();
                json = object.toString();
                MCLogUtil.e("PostsServiceImplHelper", "json->" + json);
                return json;
            }
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            json = object.toString();
            MCLogUtil.e("PostsServiceImplHelper", "json->" + json);
            return json;
        }
        json = object.toString();
        MCLogUtil.e("PostsServiceImplHelper", "json->" + json);
        return json;
    }

    public static String getPublishSortJsonStr(Context context, long boardId, String title, String classified, String content, String aid, double longitude, double latitude, String location, int requireLocation, int classificationTypeId, int classificationTopId, PermissionModel settingModel) {
        Exception e;
        JSONObject jSONObject;
        JSONObject object = null;
        try {
            JSONObject object2 = new JSONObject();
            try {
                JSONObject jsonObject;
                JSONObject bodyObject = new JSONObject();
                try {
                    jsonObject = new JSONObject();
                } catch (Exception e2) {
                    e = e2;
                    jSONObject = bodyObject;
                    object = object2;
                    e.printStackTrace();
                    return object.toString();
                }
                JSONObject jSONObject2;
                try {
                    jsonObject.put("fid", boardId);
                    jsonObject.put("location", location);
                    jsonObject.put("aid", aid);
                    jsonObject.put("content", content);
                    jsonObject.put("title", title);
                    BDLocation bdLocation = SharedPreferencesDB.getInstance(context.getApplicationContext()).getLocation();
                    if (bdLocation != null) {
                        longitude = bdLocation.getLongitude();
                        latitude = bdLocation.getLatitude();
                    }
                    jsonObject.put("longitude", String.valueOf(longitude));
                    jsonObject.put("latitude", String.valueOf(latitude));
                    jsonObject.put("isHidden", settingModel.getIsHidden());
                    jsonObject.put("isAnonymous", settingModel.getIsAnonymous());
                    jsonObject.put("isOnlyAuthor", settingModel.getIsOnlyAuthor());
                    jsonObject.put(PostsConstant.IS_SHOW_POSITION, requireLocation);
                    jsonObject.put("typeId", classificationTypeId);
                    jsonObject.put("sortId", classificationTopId);
                    jsonObject.put(PostsConstant.TYPE_OPTION, classified);
                    bodyObject.put("json", jsonObject);
                    object2.put("body", bodyObject);
                    jSONObject2 = jsonObject;
                    jSONObject = bodyObject;
                    object = object2;
                } catch (Exception e3) {
                    e = e3;
                    jSONObject2 = jsonObject;
                    jSONObject = bodyObject;
                    object = object2;
                    e.printStackTrace();
                    return object.toString();
                }
            } catch (Exception e4) {
                e = e4;
                object = object2;
                e.printStackTrace();
                return object.toString();
            }
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            return object.toString();
        }
        return object.toString();
    }

    public static BaseResultModel<List<ClassifiedModel>> getClassifiedModleList(Context context, String jsonStr, long boardId) {
        BaseResultModel<List<ClassifiedModel>> baseResultModel = new BaseResultModel();
        List<ClassifiedModel> classifiedModelList = new ArrayList();
        try {
            JSONObject jSONObject = new JSONObject(jsonStr);
            if (jSONObject.optInt("rs") == 1) {
                JSONArray jsonArray = jSONObject.optJSONObject("body").optJSONArray(PostsConstant.CLASSIFIED);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClassifiedModel classifiedModel = new ClassifiedModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    classifiedModel.setClassifiedName(object.optString(PostsConstant.CLASSIFIED_NAME));
                    classifiedModel.setClassifiedType(object.optInt(PostsConstant.CLASSIFIED_TYPE));
                    classifiedModel.setClassifiedTitle(object.optString(PostsConstant.CLASSIFIED_TITLE));
                    classifiedModel.setRequired(object.optString(PostsConstant.REQUIRED));
                    classifiedModel.setUnchangeable(object.optString(PostsConstant.UNCHANGEABLE));
                    classifiedModel.setClassifiedTopId(object.optInt(PostsConstant.CLASSIFIED_TOP_ID));
                    if (object.optJSONObject(PostsConstant.CLASSIFIED_RULES) != null) {
                        JSONObject classifiedRulesjson = object.optJSONObject(PostsConstant.CLASSIFIED_RULES);
                        RulesModle rulesModle = new RulesModle();
                        JSONArray jsonArrayChoices = classifiedRulesjson.optJSONArray(PostsConstant.CHOICES);
                        if (!(jsonArrayChoices == null || jsonArrayChoices.length() == 0)) {
                            List<ChoicesModle> choicesModleList = new ArrayList();
                            int p = jsonArrayChoices.length();
                            for (int j = 0; j < p; j++) {
                                JSONObject jSONObjectChoices = jsonArrayChoices.optJSONObject(j);
                                ChoicesModle choicesModle = new ChoicesModle();
                                choicesModle.setChoicesName(jSONObjectChoices.optString("name"));
                                choicesModle.setChoicesValue(jSONObjectChoices.optString(PostsConstant.VALUE));
                                choicesModleList.add(choicesModle);
                            }
                            rulesModle.setChoicesModleList(choicesModleList);
                        }
                        rulesModle.setColsize(classifiedRulesjson.optString(PostsConstant.COL_SIZE));
                        rulesModle.setDefaultvalue(classifiedRulesjson.optString(PostsConstant.DEFAULT_VALUE));
                        rulesModle.setInputsize(classifiedRulesjson.optString(PostsConstant.INPUT_SIZE));
                        rulesModle.setMaxheight(classifiedRulesjson.optString(PostsConstant.MAX_HEIGHT));
                        rulesModle.setMaxlength(classifiedRulesjson.optString(PostsConstant.MAX_LENGTH));
                        rulesModle.setMaxnum(classifiedRulesjson.optString(PostsConstant.MAX_NUM));
                        rulesModle.setMinnum(classifiedRulesjson.optString(PostsConstant.MIN_NUM));
                        rulesModle.setProfile(classifiedRulesjson.optString(PostsConstant.PRO_FILE));
                        rulesModle.setRowsize(classifiedRulesjson.optString(PostsConstant.ROW_SIZE));
                        rulesModle.setMaxwidth(classifiedRulesjson.optString(PostsConstant.MAX_WIDTH));
                        rulesModle.setTypeNum(classifiedRulesjson.optInt(PostsConstant.ISNUMBER));
                        rulesModle.setIsDate(classifiedRulesjson.optInt(PostsConstant.ISDATE));
                        classifiedModel.setClassifiedRules(rulesModle);
                    } else {
                        classifiedModel.setClassifiedRules(null);
                    }
                    classifiedModelList.add(classifiedModel);
                }
                baseResultModel.setData(classifiedModelList);
            } else {
                JSONObject headObject = jSONObject.optJSONObject("head");
                baseResultModel.setErrorInfo(headObject.optString("errInfo"));
                baseResultModel.setAlert(headObject.optInt("alert"));
            }
            baseResultModel.setRs(jSONObject.optInt("rs"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResultModel;
    }

    public static String createPublishTopicJson(String content, String splitChar, String tagImg, String audioPath, List<UploadPictureModel> uploadPictureModels) {
        JSONArray jsonArray = new JSONArray();
        String[] s = content.split(splitChar);
        try{

            if (!MCStringUtil.isEmpty(audioPath)) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("type", 3);
                jsonObj.put("infor", audioPath);
                jsonArray.put(jsonObj);
            }
            int i = 0;
            while (i < s.length) {
                JSONObject jsonObj = new JSONObject();
                if (s[i].indexOf(tagImg) > -1) {
                    jsonObj.put("type", 1);
                    jsonObj.put("infor", s[i].substring(1, s[i].length()));
                } else {
                    jsonObj.put("type", 0);
                    jsonObj.put("infor", s[i]);
                }
                jsonArray.put(jsonObj);
                i++;
            }
            for (i = 0; i < uploadPictureModels.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", 1);
                jsonObject.put("infor", ((UploadPictureModel) uploadPictureModels.get(i)).getPicPath());
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    public static String createPublishTopicJson(List<String> pollContent) {
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        while (i < pollContent.size()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(PostsConstant.ITEM_NAME, pollContent.get(i));
                jsonArray.put(jsonObject);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static String createUploadJson(String uploadType, String audioPath, long boardId, int sortId, String attachFrom) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject bodyObject = new JSONObject();
            JSONObject attachment = new JSONObject();
            attachment.put("name", audioPath);
            attachment.put(PostsConstant.ISPOST, 1);
            attachment.put("data", "");
            attachment.put("type", uploadType);
            attachment.put("fid", boardId);
            attachment.put("sortId", sortId);
            attachment.put("module", attachFrom);
            bodyObject.put(PostsConstant.ARRACHMENT, attachment);
            bodyObject.put("externInfo", new JSONObject());
            jsonObject.put("body", bodyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static BaseResultModel<List<UploadPictureModel>> parseUpload(Context context, String jsonStr) {
        BaseResultModel<List<UploadPictureModel>> baseResultModel = new BaseResultModel();
        List<UploadPictureModel> list = new ArrayList();
        try {
            BaseResultModel<Object> baseModel = BaseJsonHelper.formJsonRs(jsonStr, context);
            baseResultModel.setRs(baseModel.getRs());
            baseResultModel.setAlert(baseModel.getAlert());
            baseResultModel.setErrorInfo(baseModel.getErrorInfo());
            baseResultModel.setErrorCode(baseModel.getErrorCode());
            if (baseModel.getRs() == 1) {
                JSONArray attachment = new JSONObject(jsonStr).optJSONObject("body").optJSONArray(PostsConstant.ARRACHMENT);
                for (int i = 0; i < attachment.length(); i++) {
                    JSONObject object = attachment.optJSONObject(i);
                    UploadPictureModel uploadPictureModel = new UploadPictureModel();
                    uploadPictureModel.setAid((long) object.optInt("id"));
                    uploadPictureModel.setPicPath(object.optString(PostsConstant.URL_NAME));
                    list.add(uploadPictureModel);
                }
                baseResultModel.setData(list);
            }
        } catch (JSONException e) {
            baseResultModel.setRs(0);
            e.printStackTrace();
        }
        return baseResultModel;
    }

    public static String createPublishClassifiedModelJson(Context context, List<ClassifiedModel> classifiedModelLoadList) {
        JSONObject classifiedJsonObj1 = new JSONObject();
        int listSize = classifiedModelLoadList.size();
        for (int i = 0; i < listSize; i++) {
            try {
                if (SharedPreferencesDB.getInstance(context).getForumType().equals(FinalConstant.PHPWIND)) {
                    classifiedJsonObj1.put(((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedName(), MCStringUtil.isEmpty(((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedValue()) ? "" : ((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedValue());
                } else if (((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedType() != 6) {
                    classifiedJsonObj1.put(((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedName(), MCStringUtil.isEmpty(((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedValue()) ? "" : ((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedValue());
                } else {
                    classifiedJsonObj1.put(((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedName(), MCStringUtil.isEmpty(((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedAid()) ? "" : ((ClassifiedModel) classifiedModelLoadList.get(i)).getClassifiedAid());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return classifiedJsonObj1.toString();
    }

    public static String createReplyJsonStr(Context context, long boardId, long topicId, String rContent, long toReplyId, boolean isQuote, double longitude, double latitude, String location, int requireLocation, String aid, PermissionModel settingModel) {
        JSONObject jsonObject;
        Exception e;
        JSONObject jSONObject;
        JSONObject jSONObject2;
        JSONObject object = null;
        try {
            JSONObject object2 = new JSONObject();
            try {
                JSONObject bodyObject = new JSONObject();
                try {
                    jsonObject = new JSONObject();
                } catch (Exception e2) {
                    e = e2;
                    jSONObject = bodyObject;
                    object = object2;
                    e.printStackTrace();
                    return object.toString();
                }
                try {
                    jsonObject.put("fid", boardId);
                    jsonObject.put("tid", topicId);
                    jsonObject.put("location", location);
                    jsonObject.put("aid", aid);
                    jsonObject.put("content", rContent);
                    BDLocation bdLocation = SharedPreferencesDB.getInstance(context.getApplicationContext()).getLocation();
                    if (bdLocation != null) {
                        longitude = bdLocation.getLongitude();
                        latitude = bdLocation.getLatitude();
                    }
                    jsonObject.put("longitude", String.valueOf(longitude));
                    jsonObject.put("latitude", String.valueOf(latitude));
                    jsonObject.put("isHidden", settingModel.getIsHidden());
                    jsonObject.put("isAnonymous", settingModel.getIsAnonymous());
                    jsonObject.put("isOnlyAuthor", settingModel.getIsOnlyAuthor());
                    jsonObject.put(PostsConstant.IS_SHOW_POSITION, requireLocation);
                    jsonObject.put(PostsConstant.REPLY_ID, toReplyId);
                    if (isQuote) {
                        jsonObject.put("isQuote", 1);
                    } else {
                        jsonObject.put("isQuote", 0);
                    }
                    bodyObject.put("json", jsonObject);
                    object2.put("body", bodyObject);
                    jSONObject2 = jsonObject;
                    jSONObject = bodyObject;
                    object = object2;
                } catch (Exception e3) {
                    e = e3;
                    jSONObject2 = jsonObject;
                    jSONObject = bodyObject;
                    object = object2;
                    e.printStackTrace();
                    return object.toString();
                }
            } catch (Exception e4) {
                e = e4;
                object = object2;
                e.printStackTrace();
                return object.toString();
            }
        } catch (Exception e5) {
            e = e5;
            e.printStackTrace();
            return object.toString();
        }
        return object.toString();
    }

    public static List<TopicContentModel> createContentList(String content, String splitChar, String tagImg, List<UserInfoModel> mentionedFriends, String audioPath, List<UploadPictureModel> uploadPictureModels) {
        List<TopicContentModel> list = new ArrayList();
        String[] s = content.split(splitChar);
        try {
            TopicContentModel contentModel;
            int i;
            if (!MCStringUtil.isEmpty(audioPath)) {
                contentModel = new TopicContentModel();
                contentModel.setType(3);
                contentModel.setInfor(audioPath);
                list.add(contentModel);
            }
            for (i = 0; i < s.length; i++) {
                if (i == 0 && mentionedFriends != null && mentionedFriends.size() > 0 && !mentionedFriends.isEmpty()) {
                    String name = null;
                    for (int j = 0; j < mentionedFriends.size(); j++) {
                        name = new StringBuilder(String.valueOf(((UserInfoModel) mentionedFriends.get(j)).getNickname())).append(AdApiConstant.RES_SPLIT_COMMA).toString();
                    }
                    if (MCStringUtil.isEmpty(name)) {
                        contentModel = new TopicContentModel();
                        contentModel.setType(0);
                        contentModel.setInfor(name);
                        list.add(contentModel);
                    }
                }
                if (s[i].indexOf(tagImg) > -1) {
                    contentModel = new TopicContentModel();
                    contentModel.setType(1);
                    contentModel.setInfor(s[i].substring(1, s[i].length()));
                    list.add(contentModel);
                } else {
                    contentModel = new TopicContentModel();
                    contentModel.setType(0);
                    contentModel.setInfor(s[i]);
                    list.add(contentModel);
                }
            }
            if (uploadPictureModels != null) {
                for (i = 0; i < uploadPictureModels.size(); i++) {
                    contentModel = new TopicContentModel();
                    contentModel.setType(1);
                    contentModel.setInfor(((UploadPictureModel) uploadPictureModels.get(i)).getPicPath());
                    list.add(contentModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static BaseResultModel<AnnoModel> getAnnoDetail(Context context, String jsonStr) {
        BaseResultModel<AnnoModel> baseResultModel = new BaseResultModel();
        AnnoModel model = new AnnoModel();
        try {
            BaseResultModel<Object> resultModel = BaseJsonHelper.formJsonRs(jsonStr, context);
            baseResultModel.setRs(resultModel.getRs());
            if (resultModel.getRs() == 1) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String baseURL = MCResource.getInstance(context).getString("mc_discuz_base_request_url");
                JSONObject jsonObj = jsonObject.optJSONObject("body").optJSONObject("list");
                model.setAuthor(jsonObj.optString("author"));
                model.setBoardId(jsonObj.optLong("board_id"));
                model.setAnnoStartTime(jsonObj.optString(PostsConstant.ANNO_START_TIME));
                model.setAnnoEndDate(jsonObj.optLong(PostsConstant.ANNO_END_DATE));
                model.setTitle(jsonObj.optString("title"));
                if (jsonObj.optString("icon").indexOf("http") > -1) {
                    baseURL = "";
                }
                model.setIcon(new StringBuilder(String.valueOf(baseURL)).append(jsonObj.optString("icon")).toString());
                JSONObject contentJsonObj = jsonObj.optJSONObject("content");
                model.setContent(contentJsonObj.optString("infor"));
                model.setType(contentJsonObj.optString("type"));
                baseResultModel.setData(model);
                return baseResultModel;
            }
            baseResultModel.setErrorInfo(resultModel.getErrorInfo());
            return baseResultModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BaseResultModel<List<PollItemModel>> getUserPoll(Context context, String jsonStr) {
        List<PollItemModel> list;
        BaseResultModel<List<PollItemModel>> baseResultModel = new BaseResultModel();
        try {
            BaseJsonHelper.formJsonRs(context, jsonStr, baseResultModel);
            if (baseResultModel.getRs() != 0) {
                List<PollItemModel> pollList = new ArrayList();
                int count = 0;
                try {
                    int i;
                    JSONArray array = new JSONObject(jsonStr).getJSONArray(PostsConstant.POLL_LIST);
                    for (i = 0; i < array.length(); i++) {
                        count += ((JSONObject) array.get(i)).optInt(PostsConstant.POLL_TOTAL_NUM);
                    }
                    for (i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        PollItemModel pollItemModel = new PollItemModel();
                        pollItemModel.setPollItemId((long) jsonObject.optInt(PostsConstant.POLL_ID));
                        pollItemModel.setPollName(jsonObject.optString("name"));
                        pollItemModel.setTotalNum(jsonObject.optInt(PostsConstant.POLL_TOTAL_NUM));
                        int total = jsonObject.optInt(PostsConstant.POLL_TOTAL_NUM);
                        if (count > 0) {
                            pollItemModel.setRatio(((double) total) / ((double) count));
                        } else {
                            pollItemModel.setRatio(0.0d);
                        }
                        pollList.add(pollItemModel);
                    }
                    baseResultModel.setData(pollList);
                    list = pollList;
                } catch (Exception e) {
                    Exception e2 = e;
                    list = pollList;
                    e2.printStackTrace();
                    baseResultModel.setRs(0);
                    return baseResultModel;
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            baseResultModel.setRs(0);
            return baseResultModel;
        }
        return baseResultModel;
    }

    public static BaseResultModel<Object> publishPoll(String jsonStr, Context context) {
        BaseResultModel<Object> baseResultModel = new BaseResultModel();
        try {
            baseResultModel.setRs(new JSONObject(jsonStr).optInt("rs"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseResultModel;
    }
}
