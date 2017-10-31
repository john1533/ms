package com.mobcent.lowest.module.game.service;

import com.mobcent.lowest.module.game.model.GameCommentModel;
import com.mobcent.lowest.module.game.model.WebGameModel;
import java.util.List;

public interface GameService {
    int commentGame(String str, String str2, long j, boolean z, long j2, long j3, String str3, int i, int i2);

    String createCommentJson(String str, String str2, String str3);

    boolean deleteWebGame(long j);

    List<GameCommentModel> getGameCommentList(long j, long j2, int i, int i2);

    List<WebGameModel> getLatestGameList(int i, int i2);

    List<WebGameModel> getLatestGameListByLocal(int i, int i2);

    List<WebGameModel> getLatestGameListByNet(int i, int i2);

    boolean getLocalList(String str);

    List<WebGameModel> getMyGameList(int i, int i2);

    List<WebGameModel> getRecommendGameList(int i, int i2);

    List<WebGameModel> getRecommendGameListByLocal(int i, int i2);

    List<WebGameModel> getRecommendGameListByNet(int i, int i2);

    String getRefreshData(String str);

    void setListLocal(String str, boolean z);

    void setRefreshData(String str, long j);
}
