package com.mobcent.discuz.android.api;

import android.content.Context;
import com.mobcent.discuz.android.constant.BoardApiConstant;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.HashMap;

public abstract class BoardRestfulApiRequester extends BaseDiscuzApiRequester implements BoardApiConstant {
    public static String getBoards(Context context) {
        return BaseDiscuzApiRequester.doPostRequest(context, MCResource.getInstance(context).getString("mc_forum_board_list"), new HashMap());
    }

    public static String getBoardChildList(Context context, long boardId) {
        HashMap<String, String> params = new HashMap();
        String url = MCResource.getInstance(context).getString("mc_forum_board_list");
        params.put("fid", new StringBuilder(String.valueOf(boardId)).toString());
        return BaseDiscuzApiRequester.doPostRequest(context, url, params);
    }
}
