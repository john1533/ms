package com.mobcent.lowest.android.ui.module.game.cache;

import com.mobcent.lowest.module.game.model.GameCommentModel;
import com.mobcent.lowest.module.game.model.WebGameModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDataCache implements Serializable {
    private static GameDataCache gameDataCache = null;
    private static final long serialVersionUID = 1;
    private List<GameCommentModel> gameCommentList = new ArrayList();
    private Map<String, List<WebGameModel>> gameMap = new HashMap();
    private Map<String, Integer> positionMap = new HashMap();

    private GameDataCache() {
    }

    public static GameDataCache getInstance() {
        if (gameDataCache == null) {
            gameDataCache = new GameDataCache();
        }
        return gameDataCache;
    }

    public List<WebGameModel> getWebGameList(String key) {
        return (List) this.gameMap.get(key);
    }

    public void clearAllList() {
        this.gameMap.clear();
        this.positionMap.clear();
    }

    public void removeList(String key) {
        this.gameMap.remove(key);
    }

    public void setWebGameList(String key, List<WebGameModel> webGameList) {
        this.gameMap.put(key, webGameList);
    }

    public void savePostion(String key, Integer scroolPosition) {
        this.positionMap.put(key, scroolPosition);
    }

    public Integer getPosition(String key) {
        return (Integer) this.positionMap.get(key);
    }

    public List<GameCommentModel> getGameCommentList() {
        return this.gameCommentList;
    }

    public void setGameCommentList(List<GameCommentModel> gameCommentList) {
        this.gameCommentList = gameCommentList;
    }

    public void clearGameCommentList() {
        this.gameCommentList.clear();
    }
}
