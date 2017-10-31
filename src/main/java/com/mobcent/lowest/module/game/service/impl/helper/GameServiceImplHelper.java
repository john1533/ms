package com.mobcent.lowest.module.game.service.impl.helper;

import com.mobcent.lowest.base.constant.BaseRestfulApiConstant;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.model.GameCommentModel;
import com.mobcent.lowest.module.game.model.WebGameModel;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameServiceImplHelper implements BaseRestfulApiConstant, GameApiConstant {
    public static List<WebGameModel> parseWebGameList(String jsonStr) {
        List<WebGameModel> webGameModelList = new ArrayList();
        try {
            JSONObject jsonRoot = new JSONObject(jsonStr);
            if (jsonRoot.optInt("rs") == 0) {
                return null;
            }
            boolean hasNextPage = jsonRoot.optBoolean("has_next");
            int currentPage = jsonRoot.optInt("page");
            JSONArray gameArr = jsonRoot.optJSONArray(GameApiConstant.GAMES);
            int gameCount = gameArr.length();
            for (int i = 0; i < gameCount; i++) {
                JSONObject gameJson = gameArr.getJSONObject(i);
                WebGameModel webGameModel = new WebGameModel();
                webGameModel.setGameDesc(gameJson.optString("game_desc"));
                webGameModel.setGameIcon(gameJson.optString("game_icon"));
                webGameModel.setGameId(gameJson.optLong("game_id"));
                webGameModel.setGameName(gameJson.optString("game_name"));
                JSONArray picArr = gameJson.optJSONArray("game_screenshots");
                int picLength = picArr.length();
                String[] urlArr = new String[picLength];
                for (int j = 0; j < picLength; j++) {
                    urlArr[j] = picArr.optString(j);
                }
                webGameModel.setGameScreenshots(urlArr);
                webGameModel.setGameTag(gameJson.optString("game_tag"));
                webGameModel.setGameUrl(gameJson.optString("game_url"));
                webGameModel.setHits(gameJson.optInt("hits"));
                webGameModel.setReplies(gameJson.optInt("replies"));
                webGameModelList.add(webGameModel);
            }
            if (webGameModelList.size() <= 0) {
                return webGameModelList;
            }
            ((WebGameModel) webGameModelList.get(0)).setPage(currentPage);
            ((WebGameModel) webGameModelList.get(0)).setHasNext(hasNextPage);
            return webGameModelList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<GameCommentModel> parsePosts(String jsonStr) {
        List<GameCommentModel> postsList = new ArrayList();
        try {
            JSONObject jsonRoot = new JSONObject(jsonStr);
            if (jsonRoot.optInt("rs") == 0) {
                return null;
            }
            JSONArray jArr = jsonRoot.optJSONArray("list");
            int totalNum = jsonRoot.optInt("total_num");
            int jLength = jArr.length();
            for (int i = 0; i < jLength; i++) {
                GameCommentModel commentModel = new GameCommentModel();
                JSONObject jsonObj = jArr.getJSONObject(i);
                commentModel.setCritic(jsonObj.getString("reply_name"));
                commentModel.setTime(new StringBuilder(String.valueOf(jsonObj.getLong("posts_date"))).toString());
                commentModel.setTotalNum(totalNum);
                JSONArray commentArr = jsonObj.optJSONArray("reply_content");
                for (int j = 0; j < commentArr.length(); j++) {
                    commentModel.setCommentContent(commentArr.getJSONObject(j).optString("infor"));
                }
                postsList.add(commentModel);
            }
            return postsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return postsList;
        }
    }

    public static int formJsonRS(String jsonStr) {
        JSONObject jsonRoot = null;
        try {
            jsonRoot = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonRoot.optInt("rs");
    }

    public static JSONArray createCommentJson(String content, String splitChar, String tagImg, String audioPath, int audioDuration) {
        JSONArray jsonArray = new JSONArray();
        String[] s = content.split(splitChar);
        try {
            JSONObject jsonObj;
            if (!MCStringUtil.isEmpty(audioPath)) {
                jsonObj = new JSONObject();
                jsonObj.put("type", 5);
                jsonObj.put("infor", audioPath);
                jsonObj.put("desc", audioDuration);
                jsonArray.put(jsonObj);
            }
            for (int i = 0; i < s.length; i++) {
                jsonObj = new JSONObject();
                if (s[i].indexOf(tagImg) > -1) {
                    jsonObj.put("type", 1);
                    jsonObj.put("infor", s[i].substring(1, s[i].length()));
                } else {
                    jsonObj.put("type", 0);
                    jsonObj.put("infor", s[i].toString());
                }
                jsonArray.put(jsonObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
