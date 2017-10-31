package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.BoardRestfulApiRequester;
import com.mobcent.discuz.android.db.BoardDBUtil;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.android.service.BoardService;
import com.mobcent.discuz.android.service.impl.helper.BoardServiceImplHelper;

public class BoardServiceImpl implements BoardService {
    private Context context;

    public BoardServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel<BoardModel> getBoardModelByLocal() {
        return BoardServiceImplHelper.parseBoardModel(BoardDBUtil.getInstance(this.context).getBoardJsonString(), this.context);
    }

    public BaseResultModel<BoardModel> getBoardModelByNet() {
        String jsonStr = BoardRestfulApiRequester.getBoards(this.context);
        BaseResultModel<BoardModel> result = BoardServiceImplHelper.parseBoardModel(jsonStr, this.context);
        if (result.getRs() == 1) {
            BoardDBUtil.getInstance(this.context).updateBoardJsonString(jsonStr);
        }
        return result;
    }

    public BaseResultModel<BoardModel> getBoardChildList(long boardId) {
        return BoardServiceImplHelper.parseBoardModel(BoardRestfulApiRequester.getBoardChildList(this.context, boardId), this.context);
    }
}
