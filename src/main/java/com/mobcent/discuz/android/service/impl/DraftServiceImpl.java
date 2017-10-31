package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.db.TopicDraftDBUtil;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.service.DraftService;
import java.util.List;

public class DraftServiceImpl extends BaseServiceImpl implements DraftService {
    TopicDraftDBUtil dbUtil = TopicDraftDBUtil.getInstance(this.context);

    public DraftServiceImpl(Context context) {
        super(context);
    }

    public void saveDraftModel(TopicDraftModel draftModel) {
        this.dbUtil.saveDraftModel(draftModel);
    }

    public void deleteDraftModel(TopicDraftModel draftModel) {
        this.dbUtil.deleteDraftModel(draftModel);
    }

    public List<TopicDraftModel> queryDraftModel(TopicDraftModel draftModel) {
        return this.dbUtil.queryDraftModel(draftModel);
    }

    public TopicDraftModel queryByTimeDesc(int type) {
        return this.dbUtil.queryByTimeDesc(type);
    }

    public boolean isHasDraftToAlert(int type) {
        return this.dbUtil.isHasDraftToAlert(this.db.getLong("draftAlertTime=" + type), type);
    }

    public long getNewestDraftTime(int type) {
        return this.dbUtil.getNewestTime(type);
    }

    public void setLastIgnoreAlert(int type) {
        this.db.saveLong("draftAlertTime=" + type, System.currentTimeMillis());
    }
}
