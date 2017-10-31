package com.mobcent.lowest.android.ui.module.plaza.activity.model;

import com.mobcent.lowest.base.model.BaseModel;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.Arrays;
import java.util.List;

public class IntentPlazaModel extends BaseModel {
    private static final long serialVersionUID = -6529123640276225577L;
    private int forumId;
    private String forumKey;
    private List<PlazaAppModel> plazaAppModels = null;
    private int[] searchTypes;
    private boolean showTopBar = true;
    private boolean showWebSearch = true;
    private String title;
    private long userId;

    public int getForumId() {
        return this.forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getForumKey() {
        return this.forumKey;
    }

    public void setForumKey(String forumKey) {
        this.forumKey = forumKey;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int[] getSearchTypes() {
        return this.searchTypes;
    }

    public void setSearchTypes(int[] searchTypes) {
        this.searchTypes = searchTypes;
    }

    public List<PlazaAppModel> getPlazaAppModels() {
        return this.plazaAppModels;
    }

    public void setPlazaAppModels(List<PlazaAppModel> plazaAppModels) {
        this.plazaAppModels = plazaAppModels;
    }

    public String toString() {
        return "IntentPlazaModel [forumId=" + this.forumId + ", forumKey=" + this.forumKey + ", userId=" + this.userId + ", searchTypes=" + Arrays.toString(this.searchTypes) + ", plazaAppModels=" + this.plazaAppModels + "]";
    }

    public boolean isShowTopBar() {
        return this.showTopBar;
    }

    public void setShowTopBar(boolean showTopBar) {
        this.showTopBar = showTopBar;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowWebSearch() {
        return this.showWebSearch;
    }

    public void setShowWebSearch(boolean showWebSearch) {
        this.showWebSearch = showWebSearch;
    }
}
