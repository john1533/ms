package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.BoardApiConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.android.util.DZErrorInfoUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class BoardServiceImplHelper implements BoardApiConstant {
    public static BaseResultModel<BoardModel> parseBoardModel(String jsonStr, Context context) {
        BaseResultModel<BoardModel> result = new BaseResultModel();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            BaseJsonHelper.formJsonRs(context, jsonStr, result);
            if (result.getRs() != 0) {
                BoardModel boardModel = new BoardModel();
                boardModel.setOnlineUserNum(jsonObject.optInt(BoardApiConstant.ONLINE_USER_NUM));
                boardModel.setTdVisitors(jsonObject.optInt(BoardApiConstant.TD_VISITORS));
                List<BoardParent> parentList = new ArrayList();
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                if (jsonArray != null) {
                    int parentCount = jsonArray.length();
                    for (int i = 0; i < parentCount; i++) {
                        JSONObject jobj = jsonArray.optJSONObject(i);
                        BoardParent parent = new BoardParent();
                        parent.setBoardCategoryId(jobj.optLong(BoardApiConstant.BOADR_CATEGORY_ID));
                        parent.setBoardCategoryName(jobj.optString(BoardApiConstant.BOADR_CATEGORY_NAME));
                        parent.setBoardCategoryType(jobj.optInt(BoardApiConstant.BOADR_CATEGORY_TYPE));
                        parent.setChildList(getChildList(jobj.optJSONArray(BoardApiConstant.BOADR_LIST), context));
                        parentList.add(parent);
                    }
                }
                boardModel.setParentList(parentList);
                result.setData(boardModel);
            }
        } catch (Exception e) {
            result.setRs(0);
            result.setErrorInfo(DZErrorInfoUtils.dataPaseError(context));
        }
        return result;
    }

    public static List<BoardChild> getChildList(JSONArray jsonBoardArray, Context context) {
        List<BoardChild> childList = new ArrayList();
        if (jsonBoardArray != null) {
            String img_url = MCResource.getInstance(context).getString("mc_discuz_base_request_url");
            int s = jsonBoardArray.length();
            for (int k = 0; k < s; k++) {
                BoardChild boardChild = new BoardChild();
                JSONObject boardjson = jsonBoardArray.optJSONObject(k);
                boardChild.setBoardId(boardjson.optLong("board_id"));
                boardChild.setBoardName(boardjson.optString("board_name"));
                boardChild.setDescription(boardjson.optString("description"));
                boardChild.setLastPostsDate(boardjson.optLong(BoardApiConstant.LAST_POSTS_DATE));
                boardChild.setPostsTotalNum(boardjson.optInt(BoardApiConstant.POSTS_TOTAL_NUM));
                boardChild.setTodayPostsNum(boardjson.optInt(BoardApiConstant.TD_POSTS_NUM));
                boardChild.setTopicTotalNum(boardjson.optInt(BoardApiConstant.TOPIC_TOTAL_NUM));
                boardChild.setBoardChild(boardjson.optInt(BoardApiConstant.BOARD_CHILD));
                boardChild.setBoardContent(boardjson.optInt(BoardApiConstant.BOARD_CONTENT));
                boardChild.setForumRedirect(boardjson.optString(BoardApiConstant.BOARD_REDIRECT));
                if (boardjson.optString(BoardApiConstant.BOARD_IMG).indexOf("http") > -1) {
                    boardChild.setBoardImg(boardjson.optString(BoardApiConstant.BOARD_IMG));
                } else if (MCStringUtil.isEmpty(boardjson.optString(BoardApiConstant.BOARD_IMG))) {
                    boardChild.setBoardImg("");
                } else {
                    boardChild.setBoardImg(new StringBuilder(String.valueOf(img_url)).append(boardjson.optString(BoardApiConstant.BOARD_IMG)).toString());
                }
                childList.add(boardChild);
            }
        }
        return childList;
    }
}
