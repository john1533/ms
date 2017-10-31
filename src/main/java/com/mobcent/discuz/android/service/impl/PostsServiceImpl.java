package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.PostsRestfulApiRequester;
import com.mobcent.discuz.android.api.UploadFileRestfulApiRequester;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.db.EssenceTopicDBUtil;
import com.mobcent.discuz.android.db.NewTopicDBUtil;
import com.mobcent.discuz.android.db.PicTopicDBUtil;
import com.mobcent.discuz.android.db.PortalTopicDBUtil;
import com.mobcent.discuz.android.db.SurroundTopicDBUtil;
import com.mobcent.discuz.android.db.TopTopicDBUtil;
import com.mobcent.discuz.android.db.TopicDBUtil;
import com.mobcent.discuz.android.model.AnnoModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ClassifiedModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.android.model.PollItemModel;
import com.mobcent.discuz.android.model.TopicContentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.helper.BaseJsonHelper;
import com.mobcent.discuz.android.service.impl.helper.PostsServiceImplHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class PostsServiceImpl implements PostsService, PostsConstant {
    private Context context;

    public PostsServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel<List<TopicModel>> getPortalListByNet(int page, int pageSize, String moudleId, int isImageList) {
        String jsonStr = PostsRestfulApiRequester.getPortalList(this.context, page, pageSize, moudleId, isImageList);
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        if (MCStringUtil.isEmpty(jsonStr) || !BaseJsonHelper.isJson(jsonStr)) {
            return PostsServiceImplHelper.getPortalList(this.context, PortalTopicDBUtil.getInstance(this.context).getPortalTopicJsonString(moudleId));
        }
        if (page == 1 && baseResultModel.getRs() == 1) {
            PortalTopicDBUtil.getInstance(this.context).updatePortalTopicJsonString(jsonStr, moudleId);
        }
        return PostsServiceImplHelper.getPortalList(this.context, jsonStr);
    }

    public BaseResultModel<List<TopicModel>> getPortalList(int page, int pageSize, String moudleId, boolean isLocal, int isImageList) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        if (!isLocal) {
            return getPortalListByNet(page, pageSize, moudleId, isImageList);
        }
        if (MCStringUtil.isEmpty(PortalTopicDBUtil.getInstance(this.context).getPortalTopicJsonString(moudleId))) {
            return getPortalListByNet(page, pageSize, moudleId, isImageList);
        }
        return baseResultModel;
    }

    public BaseResultModel<List<TopicModel>> getTopicList(long boardId, int page, int pageSize, String sortBy, String filterType, int filterId, int isImageList, int topOrder) {
        String jsonStr = PostsRestfulApiRequester.getTopicList(this.context, boardId, page, pageSize, sortBy, filterType, filterId, isImageList, topOrder);
        BaseResultModel<List<TopicModel>> baseResultModel = PostsServiceImplHelper.getTopicList(this.context, jsonStr, boardId);
        if (baseResultModel.getData() != null && baseResultModel.getRs() == 1) {
            if (sortBy.equals("essence")) {
                EssenceTopicDBUtil.getInstance(this.context).updateEssenceTopicJsonString(jsonStr, boardId);
            } else {
                if (sortBy.equals("new")) {
                    NewTopicDBUtil.getInstance(this.context).updateNewsTopicJsonString(jsonStr, boardId);
                } else {
                    if (sortBy.equals("top")) {
                        TopTopicDBUtil.getInstance(this.context).updateTopTopicJsonString(jsonStr, boardId);
                    } else {
                        TopicDBUtil.getInstance(this.context).updateTopicJsonString(jsonStr, boardId);
                    }
                }
            }
        }
        return baseResultModel;
    }

    public BaseResultModel<List<TopicModel>> getTopicListByLocal(long boardId, int page, int pageSize, String sortBy, String filterType, int filterId, boolean isLocal, int isImageList, int topOrder) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        if (!isLocal) {
            return getTopicList(boardId, page, pageSize, sortBy, filterType, filterId, isImageList, topOrder);
        }
        String jsonStr;
        if (sortBy.equals("essence")) {
            jsonStr = EssenceTopicDBUtil.getInstance(this.context).getEssenceTopicJsonString(boardId);
        } else {
            if (sortBy.equals("new")) {
                jsonStr = NewTopicDBUtil.getInstance(this.context).getNewsTopicJsonString(boardId);
            } else {
                if (sortBy.equals("top")) {
                    jsonStr = TopTopicDBUtil.getInstance(this.context).getTopTopicJsonString(boardId);
                } else {
                    jsonStr = TopicDBUtil.getInstance(this.context).getTopicJsonString(boardId);
                }
            }
        }
        if (MCStringUtil.isEmpty(jsonStr)) {
            return baseResultModel;
        }
        return PostsServiceImplHelper.getTopicList(this.context, jsonStr, boardId);
    }

    public BaseResultModel<List<TopicModel>> getUserTopicList(long userId, int page, int pageSize, String type) {
        return PostsServiceImplHelper.getTopicList(this.context, PostsRestfulApiRequester.getUserTopicList(this.context, userId, page, pageSize, type), 0);
    }

    public BaseResultModel<List<PhotoModel>> getPhotoList(long userId, String type, int aid, int page, int pageSize) {
        return PostsServiceImplHelper.getPhotoList(this.context, PostsRestfulApiRequester.getPhotoList(this.context, userId, type, aid, page, pageSize));
    }

    public BaseResultModel<List<TopicModel>> getSearchTopicList(int page, int pageSize, String keyword, int searchId) {
        return PostsServiceImplHelper.getSearchTopicList(this.context, PostsRestfulApiRequester.getSearchTopicList(this.context, keyword, page, pageSize, searchId), 0);
    }

    public BaseResultModel<List<TopicModel>> getSurroundtopicList(int page, int pageSize, Double latitude, Double longitude, String poi) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        String jsonStr = PostsRestfulApiRequester.getSurroundTopic(this.context, page, pageSize, latitude, longitude, poi);
        baseResultModel = PostsServiceImplHelper.getSurroundtopicList(this.context, jsonStr);
        if (baseResultModel.getData() != null && ((List) baseResultModel.getData()).size() > 0) {
            SurroundTopicDBUtil.getInstance(this.context).updateSurroundTopicJsonString(jsonStr);
        }
        return baseResultModel;
    }

    public BaseResultModel<List<TopicModel>> getSurroundtopicList() {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        String json = "";
        try {
            json = SurroundTopicDBUtil.getInstance(this.context).getSurroundTopicJsonString();
        } catch (Exception e) {
            json = null;
        }
        if (MCStringUtil.isEmpty(json)) {
            return baseResultModel;
        }
        return PostsServiceImplHelper.getSurroundtopicList(this.context, json);
    }

    public BaseResultModel<List<Object>> getTopicDetail(long boardId, long topicId, long authorId, int page, int pageSize, int type) {
        return PostsServiceImplHelper.getTopicDetail(this.context, PostsRestfulApiRequester.getTopicDetail(this.context, boardId, topicId, authorId, page, pageSize, type));
    }

    public BaseResultModel<List<TopicModel>> getPicTopicList(int page, int pageSize, boolean isLocal) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        String[] result = PicTopicDBUtil.getInstance(this.context).getPicTopicJsonString();
        if (!isLocal) {
            return getPicTopicListByNet(page, pageSize);
        }
        String jsonStr;
        try {
            jsonStr = result[0];
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = null;
        }
        if (MCStringUtil.isEmpty(jsonStr)) {
            return getPicTopicListByNet(page, pageSize);
        }
        return PostsServiceImplHelper.getTopicList(this.context, jsonStr, 0);
    }

    public BaseResultModel<List<TopicModel>> getPicTopicListByNet(int page, int pageSize) {
        BaseResultModel<List<TopicModel>> baseResultModel = new BaseResultModel();
        String jsonStr = PostsRestfulApiRequester.getPicTopicList(this.context, page, pageSize);
        baseResultModel = PostsServiceImplHelper.getTopicList(this.context, jsonStr, 0);
        if (baseResultModel != null && ((List) baseResultModel.getData()).size() > 0) {
            PicTopicDBUtil.getInstance(this.context).updatePicTopicJsonString(jsonStr);
        }
        return baseResultModel;
    }

    public BaseResultModel<List<ClassifiedModel>> getClassifiedModleList(int classificationTopId, long boardId) {
        BaseResultModel<List<ClassifiedModel>> baseResultModel = new BaseResultModel();
        String jsonStr = PostsRestfulApiRequester.getClassifiedModleList(this.context, classificationTopId, boardId);
        if (MCStringUtil.isEmpty(jsonStr)) {
            return baseResultModel;
        }
        return PostsServiceImplHelper.getClassifiedModleList(this.context, jsonStr, (long) classificationTopId);
    }

    public BaseResultModel<Object> publishTopic(String json, String action) {
        String jsonStr = PostsRestfulApiRequester.publishTopic(this.context, json, action);
        BaseResultModel<Object> baseResultModel = new BaseResultModel();
        BaseJsonHelper.formJsonRs(this.context, jsonStr, baseResultModel);
        return baseResultModel;
    }

    public String createContentJson(String content, String splitChar, String tagImg, String audioPath, List<UploadPictureModel> pictureModels) {
        return PostsServiceImplHelper.createPublishTopicJson(content, splitChar, tagImg, audioPath, pictureModels);
    }

    public String createPublishClassifiedModelJson(List<ClassifiedModel> classifiedModelLoadList) {
        return PostsServiceImplHelper.createPublishClassifiedModelJson(this.context, classifiedModelLoadList);
    }

    public BaseResultModel<Object> publishPollTopic(long boardId, String title, String content, String type, int isVisiable, String pollContent, String deadline, double longitude, double latitude, String location, int requireLocation, String aid, PermissionModel isCheckedSettingModel) {
        return PostsServiceImplHelper.publishPoll(PostsRestfulApiRequester.publishPollTopic(this.context, boardId, title, content, type, isVisiable, deadline, pollContent, longitude, latitude, location, requireLocation, aid, isCheckedSettingModel), this.context);
    }

    public String createPublishTopicJson(String content, String splitChar, String tagImg, String audioPath, List<UploadPictureModel> uploadPictureModels) {
        return PostsServiceImplHelper.createPublishTopicJson(content, splitChar, tagImg, audioPath, uploadPictureModels);
    }

    public String createPublishPollTopicJson(List<String> pollLists) {
        return PostsServiceImplHelper.createPublishTopicJson(pollLists);
    }

    public String createReplyJson(long boardId, long topicId, String rContent, long toReplyId, boolean isQuote, double longitude, double latitude, String location, int requireLocation, String aid, PermissionModel settingModel) {
        return PostsServiceImplHelper.createReplyJsonStr(this.context, boardId, topicId, rContent, toReplyId, isQuote, longitude, latitude, location, requireLocation, aid, settingModel);
    }

    public List<TopicContentModel> createContentList(String content, String splitChar, String tagImg, List<UserInfoModel> mentionedFriends, String audioPath, List<UploadPictureModel> uploadPictureModels) {
        return PostsServiceImplHelper.createContentList(content, splitChar, tagImg, mentionedFriends, audioPath, uploadPictureModels);
    }

    public BaseResultModel<AnnoModel> getAnnoDetail(long announceId) {
        return PostsServiceImplHelper.getAnnoDetail(this.context, PostsRestfulApiRequester.getAnnoDetail(this.context, announceId));
    }

    public BaseResultModel<List<PollItemModel>> getUserPolls(long boardId, long topicId, String pollItemId) {
        return PostsServiceImplHelper.getUserPoll(this.context, PostsRestfulApiRequester.getUserPoll(this.context, boardId, topicId, pollItemId));
    }

    public BaseResultModel<Object> support(long tid, long pid, String type) {
        return BaseJsonHelper.formJsonRs(PostsRestfulApiRequester.support(this.context, tid, pid, type), this.context);
    }

    public BaseResultModel<Object> favoriteTopic(long topicId, String idType, String type) {
        return BaseJsonHelper.formJsonRs(PostsRestfulApiRequester.favoriteTopic(this.context, topicId, idType, type), this.context);
    }

    public BaseResultModel<Object> saveAlbumInfo(int albumId, String picDesc, String aid) {
        return BaseJsonHelper.formJsonRs(PostsRestfulApiRequester.saveAlbumInfo(this.context, albumId, picDesc, aid), this.context);
    }

    public BaseResultModel<List<UploadPictureModel>> upload(String[] files, String module, String type, long fid, long sortId, long albumId) {
        return PostsServiceImplHelper.parseUpload(this.context, UploadFileRestfulApiRequester.upload(this.context, files, module, type, fid, sortId, albumId));
    }
}
