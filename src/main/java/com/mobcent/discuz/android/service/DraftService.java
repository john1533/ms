package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.TopicDraftModel;
import java.util.List;

public interface DraftService {
    void deleteDraftModel(TopicDraftModel topicDraftModel);

    long getNewestDraftTime(int i);

    boolean isHasDraftToAlert(int i);

    TopicDraftModel queryByTimeDesc(int i);

    List<TopicDraftModel> queryDraftModel(TopicDraftModel topicDraftModel);

    void saveDraftModel(TopicDraftModel topicDraftModel);

    void setLastIgnoreAlert(int i);
}
