package com.mobcent.discuz.android.model;

import java.util.List;

public class ArticleModel extends BaseModel {
    private static final long serialVersionUID = 1;
    private int allowComment;
    private String articleUrl;
    private String author;
    private String boardName;
    private String catName;
    private int commentNum;
    private String contentJson;
    private List<ContentModel> contentList;
    private String dateline;
    private String from;
    private int pageCount;
    private String summary;
    private String title;
    private int viewNum;

    public String getBoardName() {
        return this.boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAriticleUrl() {
        return this.articleUrl;
    }

    public void setAriticleUrl(String shareUrl) {
        this.articleUrl = shareUrl;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContentJson() {
        return this.contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCatName() {
        return this.catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getArticleUrl() {
        return this.articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getViewNum() {
        return this.viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public int getCommentNum() {
        return this.commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getAllowComment() {
        return this.allowComment;
    }

    public void setAllowComment(int allowComment) {
        this.allowComment = allowComment;
    }

    public String getDateline() {
        return this.dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public List<ContentModel> getContentList() {
        return this.contentList;
    }

    public void setContentList(List<ContentModel> contentList) {
        this.contentList = contentList;
    }
}
