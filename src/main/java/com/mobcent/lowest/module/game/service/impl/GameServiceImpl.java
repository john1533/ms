package com.mobcent.lowest.module.game.service.impl;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.service.impl.helper.BaseJsonHelper;
import com.mobcent.lowest.module.game.api.GameApiRequester;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.db.ContentCacheDB;
import com.mobcent.lowest.module.game.db.GameDBUtil;
import com.mobcent.lowest.module.game.model.GameCommentModel;
import com.mobcent.lowest.module.game.model.WebGameModel;
import com.mobcent.lowest.module.game.service.GameService;
import com.mobcent.lowest.module.game.service.impl.helper.GameServiceImplHelper;
import java.util.ArrayList;
import java.util.List;

public class GameServiceImpl implements GameService, GameApiConstant {
    private ContentCacheDB contentCacheDB;
    private Context context;
    private int page;
    private String tagName;

    public GameServiceImpl(Context context) {
        this.context = context;
    }

    public GameServiceImpl(Context context, String tagName) {
        this.context = context;
        this.page = 1;
        this.tagName = tagName;
        this.contentCacheDB = ContentCacheDB.getInstance(context);
        MCLogUtil.e("GameServiceImpl", "tagName = " + tagName);
    }

    public List<WebGameModel> getRecommendGameList(int pageSize, int state) {
        List<WebGameModel> webGameList;
        switch (state) {
            case 1:
                return getRecommendGameListByLocal(this.page, pageSize);
            case 2:
                webGameList = getRecommendGameListByNet(1, pageSize);
                if (webGameList == null || webGameList.isEmpty()) {
                    return webGameList;
                }
                this.page++;
                setRefreshData(this.tagName, System.currentTimeMillis());
                return webGameList;
            case 3:
                if (getLocalList(this.tagName)) {
                    webGameList = getRecommendGameListByLocal(this.page, pageSize);
                } else {
                    webGameList = getRecommendGameListByNet(this.page, pageSize);
                }
                if (webGameList == null || webGameList.isEmpty()) {
                    return webGameList;
                }
                this.page++;
                return webGameList;
            default:
                return null;
        }
    }

    public List<WebGameModel> getRecommendGameListByNet(int page, int pageSize) {
        String jsonStr = GameApiRequester.getRecommendGames(this.context, page, pageSize);
        List<WebGameModel> gameList = GameServiceImplHelper.parseWebGameList(jsonStr);
        if (gameList == null || gameList.isEmpty()) {
            if (gameList == null) {
                gameList = new ArrayList();
            }
            String errorCode = BaseJsonHelper.formJsonRS(jsonStr);
            if (!MCStringUtil.isEmpty(errorCode)) {
                WebGameModel webGameModel = new WebGameModel();
                webGameModel.setErrorCode(errorCode);
                gameList.add(webGameModel);
            }
            setListLocal(this.tagName, true);
        } else {
            if (this.page == 1) {
                GameDBUtil.getInstance(this.context, this.tagName).updataWebGameList(page, this.tagName, jsonStr);
            } else {
                GameDBUtil.getInstance(this.context, this.tagName).saveWebGameList(this.page, this.tagName, jsonStr);
            }
            setListLocal(this.tagName, false);
        }
        return gameList;
    }

    public List<WebGameModel> getRecommendGameListByLocal(int page, int pageSize) {
        List<WebGameModel> webGameList = new ArrayList();
        int lastPage = GameDBUtil.getInstance(this.context, this.tagName).getMaxPage(this.tagName);
        String jsonStr = GameDBUtil.getInstance(this.context, this.tagName).getWebGameListByTag(page, this.tagName);
        if (MCStringUtil.isEmpty(jsonStr)) {
            setListLocal(this.tagName, false);
        } else {
            webGameList = GameServiceImplHelper.parseWebGameList(jsonStr);
            if (page >= lastPage) {
                ((WebGameModel) webGameList.get(0)).setHasNext(false);
            }
        }
        return webGameList;
    }

    public List<WebGameModel> getLatestGameList(int pageSize, int state) {
        List<WebGameModel> webGameList;
        switch (state) {
            case 1:
                return getLatestGameListByLocal(this.page, pageSize);
            case 2:
                webGameList = getLatestGameListByNet(1, pageSize);
                if (webGameList == null || webGameList.isEmpty()) {
                    return webGameList;
                }
                this.page++;
                setRefreshData(this.tagName, System.currentTimeMillis());
                return webGameList;
            case 3:
                if (getLocalList(this.tagName)) {
                    webGameList = getLatestGameListByLocal(this.page, pageSize);
                } else {
                    webGameList = getLatestGameListByNet(this.page, pageSize);
                }
                if (webGameList == null || webGameList.isEmpty()) {
                    return webGameList;
                }
                this.page++;
                return webGameList;
            default:
                return null;
        }
    }

    public List<WebGameModel> getLatestGameListByNet(int page, int pageSize) {
        String jsonStr = GameApiRequester.getLatestGames(this.context, page, pageSize);
        List<WebGameModel> gameList = GameServiceImplHelper.parseWebGameList(jsonStr);
        if (gameList == null || gameList.isEmpty()) {
            if (gameList == null) {
                gameList = new ArrayList();
            }
            String errorCode = BaseJsonHelper.formJsonRS(jsonStr);
            if (!MCStringUtil.isEmpty(errorCode)) {
                WebGameModel webGameModel = new WebGameModel();
                webGameModel.setErrorCode(errorCode);
                gameList.add(webGameModel);
            }
            setListLocal(this.tagName, true);
        } else {
            if (this.page == 1) {
                GameDBUtil.getInstance(this.context, this.tagName).updataWebGameList(page, this.tagName, jsonStr);
            } else {
                GameDBUtil.getInstance(this.context, this.tagName).saveWebGameList(this.page, this.tagName, jsonStr);
            }
            setListLocal(this.tagName, false);
        }
        return gameList;
    }

    public List<WebGameModel> getLatestGameListByLocal(int page, int pageSize) {
        List<WebGameModel> webGameList = new ArrayList();
        int lastPage = GameDBUtil.getInstance(this.context, this.tagName).getMaxPage(this.tagName);
        String jsonStr = GameDBUtil.getInstance(this.context, this.tagName).getWebGameListByTag(page, this.tagName);
        if (MCStringUtil.isEmpty(jsonStr)) {
            setListLocal(this.tagName, false);
        } else {
            webGameList = GameServiceImplHelper.parseWebGameList(jsonStr);
            if (page >= lastPage) {
                ((WebGameModel) webGameList.get(0)).setHasNext(false);
            }
        }
        return webGameList;
    }

    public List<WebGameModel> getMyGameList(int pageSize, int state) {
        List<WebGameModel> webGameList = null;
        MCLogUtil.e("GameServiceImpl ", "getMyGameList   state = " + state);
        switch (state) {
            case 2:
                webGameList = GameDBUtil.getInstance(this.context, this.tagName).getMyWebGame(this.tagName, 1, 2);
                if (webGameList.size() > 0) {
                    this.page = 2;
                    break;
                }
                break;
            case 3:
                webGameList = GameDBUtil.getInstance(this.context, this.tagName).getMyWebGame(this.tagName, this.page, 2);
                if (webGameList.size() > 0) {
                    this.page++;
                    break;
                }
                break;
        }
        return webGameList;
    }

    public List<GameCommentModel> getGameCommentList(long boardId, long topicId, int page, int pageSize) {
        return GameServiceImplHelper.parsePosts(GameApiRequester.getPostsByDesc(this.context, boardId, topicId, page, pageSize));
    }

    public boolean deleteWebGame(long gameId) {
        return GameDBUtil.getInstance(this.context, this.tagName).deleteMyWebGame(this.tagName, gameId);
    }

    public boolean getLocalList(String tagName) {
        return this.contentCacheDB.getListLocal(new StringBuilder(String.valueOf(tagName)).append("local").toString());
    }

    public void setListLocal(String tagName, boolean flag) {
        this.contentCacheDB.setListLocal(new StringBuilder(String.valueOf(tagName)).append("local").toString(), flag);
    }

    public void setRefreshData(String tagName, long time) {
        this.contentCacheDB.saveRefreshTime(new StringBuilder(String.valueOf(tagName)).append("refresh_time").toString(), time);
    }

    public String getRefreshData(String tagName) {
        return MCDateUtil.getFormatTime(this.contentCacheDB.getRefreshTime(new StringBuilder(String.valueOf(tagName)).append("refresh_time").toString()));
    }

    public int commentGame(String rTitle, String rContent, long topicId, boolean isQuote, long longitude, long latitude, String location, int r, int isAnnounce) {
        return GameServiceImplHelper.formJsonRS(GameApiRequester.replyGame(this.context, rTitle, rContent, topicId, isQuote, longitude, latitude, location, r, isAnnounce));
    }

    public String createCommentJson(String content, String splitChar, String tagImg) {
        return GameServiceImplHelper.createCommentJson(content, splitChar, tagImg, "", 0).toString();
    }
}
