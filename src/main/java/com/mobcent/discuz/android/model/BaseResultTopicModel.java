package com.mobcent.discuz.android.model;

import java.util.List;

public class BaseResultTopicModel<T> extends BaseResultModel<T> {
    private static final long serialVersionUID = 1;
    private BoardChild forumInfo;
    private List<TopModel> topTopicList;

    public BoardChild getForumInfo() {
        return this.forumInfo;
    }

    public void setForumInfo(BoardChild forumInfo) {
        this.forumInfo = forumInfo;
    }

    public List<TopModel> getTopTopicList() {
        return this.topTopicList;
    }

    public void setTopTopicList(List<TopModel> topTopicList) {
        this.topTopicList = topTopicList;
    }
}
