package com.mobcent.discuz.android.service;

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
import java.util.List;

public interface PostsService {
    String createContentJson(String str, String str2, String str3, String str4, List<UploadPictureModel> list);

    List<TopicContentModel> createContentList(String str, String str2, String str3, List<UserInfoModel> list, String str4, List<UploadPictureModel> list2);

    String createPublishClassifiedModelJson(List<ClassifiedModel> list);

    String createPublishPollTopicJson(List<String> list);

    String createPublishTopicJson(String str, String str2, String str3, String str4, List<UploadPictureModel> list);

    String createReplyJson(long j, long j2, String str, long j3, boolean z, double d, double d2, String str2, int i, String str3, PermissionModel permissionModel);

    BaseResultModel<Object> favoriteTopic(long j, String str, String str2);

    BaseResultModel<AnnoModel> getAnnoDetail(long j);

    BaseResultModel<List<ClassifiedModel>> getClassifiedModleList(int i, long j);

    BaseResultModel<List<PhotoModel>> getPhotoList(long j, String str, int i, int i2, int i3);

    BaseResultModel<List<TopicModel>> getPicTopicList(int i, int i2, boolean z);

    BaseResultModel<List<TopicModel>> getPortalList(int i, int i2, String str, boolean z, int i3);

    BaseResultModel<List<TopicModel>> getPortalListByNet(int i, int i2, String str, int i3);

    BaseResultModel<List<TopicModel>> getSearchTopicList(int i, int i2, String str, int i3);

    BaseResultModel<List<TopicModel>> getSurroundtopicList();

    BaseResultModel<List<TopicModel>> getSurroundtopicList(int i, int i2, Double d, Double d2, String str);

    BaseResultModel<List<Object>> getTopicDetail(long j, long j2, long j3, int i, int i2, int i3);

    BaseResultModel<List<TopicModel>> getTopicList(long j, int i, int i2, String str, String str2, int i3, int i4, int i5);

    BaseResultModel<List<TopicModel>> getTopicListByLocal(long j, int i, int i2, String str, String str2, int i3, boolean z, int i4, int i5);

    BaseResultModel<List<PollItemModel>> getUserPolls(long j, long j2, String str);

    BaseResultModel<List<TopicModel>> getUserTopicList(long j, int i, int i2, String str);

    BaseResultModel<Object> publishPollTopic(long j, String str, String str2, String str3, int i, String str4, String str5, double d, double d2, String str6, int i2, String str7, PermissionModel permissionModel);

    BaseResultModel<Object> publishTopic(String str, String str2);

    BaseResultModel<Object> saveAlbumInfo(int i, String str, String str2);

    BaseResultModel<Object> support(long j, long j2, String str);

    BaseResultModel<List<UploadPictureModel>> upload(String[] strArr, String str, String str2, long j, long j2, long j3);
}
