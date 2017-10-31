package com.mobcent.discuz.android.model;

import java.util.List;
import java.util.Map;

public class ReplyModel extends BaseModel {
    private static final long serialVersionUID = -115304228049930360L;
    private Map<String, OtherPanelModel> extraPanel;
    private String icon;
    private boolean isFollow;
    private int isQuote;
    private int level;
    private String location;
    private Map<String, OtherPanelModel> managePanel;
    private String mobileSign;
    private int position;
    private long postsDate;
    private String quoteContent;
    private long quotePid;
    private String quoteUserName;
    private List<TopicContentModel> replyContent;
    private String replyName;
    private long replyPostsId;
    private String replyType;
    private long replyUserId;
    private String userTitle;

    public long getReplyUserId() {
        return this.replyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public List<TopicContentModel> getReplyContent() {
        return this.replyContent;
    }

    public void setReplyContent(List<TopicContentModel> replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyType() {
        return this.replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getReplyName() {
        return this.replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public long getReplyPostsId() {
        return this.replyPostsId;
    }

    public void setReplyPostsId(long replyPostsId) {
        this.replyPostsId = replyPostsId;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getPostsDate() {
        return this.postsDate;
    }

    public void setPostsDate(long postsDate) {
        this.postsDate = postsDate;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUserTitle() {
        return this.userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileSign() {
        return this.mobileSign;
    }

    public void setMobileSign(String mobileSign) {
        this.mobileSign = mobileSign;
    }

    public int getIsQuote() {
        return this.isQuote;
    }

    public void setIsQuote(int isQuote) {
        this.isQuote = isQuote;
    }

    public long getQuotePid() {
        return this.quotePid;
    }

    public void setQuotePid(long quotePid) {
        this.quotePid = quotePid;
    }

    public String getQuoteContent() {
        return this.quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }

    public String getQuoteUserName() {
        return this.quoteUserName;
    }

    public void setQuoteUserName(String quoteUserName) {
        this.quoteUserName = quoteUserName;
    }

    public Map<String, OtherPanelModel> getManagePanel() {
        return this.managePanel;
    }

    public void setManagePanel(Map<String, OtherPanelModel> managePanel) {
        this.managePanel = managePanel;
    }

    public Map<String, OtherPanelModel> getExtraPanel() {
        return this.extraPanel;
    }

    public void setExtraPanel(Map<String, OtherPanelModel> extraPanel) {
        this.extraPanel = extraPanel;
    }

    public boolean isFollow() {
        return this.isFollow;
    }

    public void setFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }
}
