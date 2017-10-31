package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardModel;

public interface BoardService {
    BaseResultModel<BoardModel> getBoardChildList(long j);

    BaseResultModel<BoardModel> getBoardModelByLocal();

    BaseResultModel<BoardModel> getBoardModelByNet();
}
