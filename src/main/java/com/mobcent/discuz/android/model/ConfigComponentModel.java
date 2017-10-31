package com.mobcent.discuz.android.model;

import java.io.Serializable;
import java.util.List;

public class ConfigComponentModel implements Serializable {
    private static final long serialVersionUID = 1;
    private long articleId;
    private List<ConfigComponentModel> componentList;
    private String defaultTitle;
    private String desc;
    private List<ConfigFastPostModel> fastPostList;
    private String filter;
    private int filterId;
    private long forumId;
    private ConfigComponentHeaderModel headerModel;
    private String icon;
    private String iconStyle;
    private String id;
    private boolean isShowForumIcon;
    private boolean isShowForumTwoCols;
    private boolean isShowMessageList;
    private boolean isShowTopicSort;
    private boolean isShowTopicTitle;
    private int listImagePosition;
    private int listSummaryLength;
    private int listTitleLength;
    private long moduleId;
    private long newsModuleId;
    private int order;
    private String orderby;
    private String padding;
    private String redirect;
    private String style;
    private String subDetailViewStyle;
    private String subListStyle;
    private String title;
    private long topicId;
    private String type;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRedirect() {
        return this.redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getPadding() {
        return this.padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public List<ConfigComponentModel> getComponentList() {
        return this.componentList;
    }

    public void setComponentList(List<ConfigComponentModel> componentList) {
        this.componentList = componentList;
    }

    public long getNewsModuleId() {
        return this.newsModuleId;
    }

    public void setNewsModuleId(long newsModuleId) {
        this.newsModuleId = newsModuleId;
    }

    public long getForumId() {
        return this.forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public boolean isShowForumIcon() {
        return this.isShowForumIcon;
    }

    public void setShowForumIcon(boolean isShowForumIcon) {
        this.isShowForumIcon = isShowForumIcon;
    }

    public boolean isShowForumTwoCols() {
        return this.isShowForumTwoCols;
    }

    public void setShowForumTwoCols(boolean isShowForumTwoCols) {
        this.isShowForumTwoCols = isShowForumTwoCols;
    }

    public boolean isShowTopicTitle() {
        return this.isShowTopicTitle;
    }

    public void setShowTopicTitle(boolean isShowTopicTitle) {
        this.isShowTopicTitle = isShowTopicTitle;
    }

    public boolean isShowTopicSort() {
        return this.isShowTopicSort;
    }

    public void setShowTopicSort(boolean isShowTopicSort) {
        this.isShowTopicSort = isShowTopicSort;
    }

    public String getIconStyle() {
        return this.iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    public ConfigComponentHeaderModel getHeaderModel() {
        return this.headerModel;
    }

    public void setHeaderModel(ConfigComponentHeaderModel headerModel) {
        this.headerModel = headerModel;
    }

    public List<ConfigFastPostModel> getFastPostList() {
        return this.fastPostList;
    }

    public void setFastPostList(List<ConfigFastPostModel> fastPostList) {
        this.fastPostList = fastPostList;
    }

    public long getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getDefaultTitle() {
        return this.defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getArticleId() {
        return this.articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOrderby() {
        return this.orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public boolean isShowMessageList() {
        return this.isShowMessageList;
    }

    public void setShowMessageList(boolean isShowMessageList) {
        this.isShowMessageList = isShowMessageList;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getFilterId() {
        return this.filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public int getListTitleLength() {
        return this.listTitleLength;
    }

    public void setListTitleLength(int listTitleLength) {
        this.listTitleLength = listTitleLength;
    }

    public int getListSummaryLength() {
        return this.listSummaryLength;
    }

    public void setListSummaryLength(int listSummaryLength) {
        this.listSummaryLength = listSummaryLength;
    }

    public int getListImagePosition() {
        return this.listImagePosition;
    }

    public void setListImagePosition(int listImagePosition) {
        this.listImagePosition = listImagePosition;
    }

    public String getSubListStyle() {
        return this.subListStyle;
    }

    public void setSubListStyle(String subListStyle) {
        this.subListStyle = subListStyle;
    }

    public String getSubDetailViewStyle() {
        return this.subDetailViewStyle;
    }

    public void setSubDetailViewStyle(String subDetailViewStyle) {
        this.subDetailViewStyle = subDetailViewStyle;
    }
}
