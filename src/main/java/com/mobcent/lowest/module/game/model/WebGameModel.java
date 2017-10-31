package com.mobcent.lowest.module.game.model;

import com.mobcent.lowest.base.model.BaseModel;

public class WebGameModel extends BaseModel {
    private static final long serialVersionUID = -579012416096115843L;
    private String gameDesc;
    private String gameIcon;
    private long gameId;
    private String gameName;
    private int gamePlayer;
    private String[] gameScreenshots;
    private int gameStars;
    private String gameTag;
    private int gameType;
    private String gameUrl;
    private int hits;
    private int position;
    private int replies;

    public String getGameName() {
        return this.gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getGameDesc() {
        return this.gameDesc;
    }

    public void setGameDesc(String gameDesc) {
        this.gameDesc = gameDesc;
    }

    public String getGameIcon() {
        return this.gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }

    public long getGameId() {
        return this.gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String[] getGameScreenshots() {
        return this.gameScreenshots;
    }

    public void setGameScreenshots(String[] gameScreenshots) {
        this.gameScreenshots = gameScreenshots;
    }

    public String getGameTag() {
        return this.gameTag;
    }

    public void setGameTag(String gameTag) {
        this.gameTag = gameTag;
    }

    public String getGameUrl() {
        return this.gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public int getHits() {
        return this.hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getReplies() {
        return this.replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public int getGameType() {
        return this.gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getGamePlayer() {
        return this.gamePlayer;
    }

    public void setGamePlayer(int gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getGameStars() {
        return this.gameStars;
    }

    public void setGameStars(int gameStars) {
        this.gameStars = gameStars;
    }
}
