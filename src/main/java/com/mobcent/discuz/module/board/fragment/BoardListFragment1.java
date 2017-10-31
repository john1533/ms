package com.mobcent.discuz.module.board.fragment;

import android.widget.BaseAdapter;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.module.board.fragment.adapter.BoardListAdapter1;

public class BoardListFragment1 extends BaseBoardListFragment {
    private BoardListAdapter1 adapter;

    protected void onBoardDataLoaded(BaseResultModel<BoardModel> result) {
        if (((BoardModel) result.getData()).getParentList() != null) {
            this.parentList.clear();
            this.parentList.addAll(((BoardModel) result.getData()).getParentList());
        }
        boolean isTodayTotalVisible = false;
        if (this.settingModel != null) {
            isTodayTotalVisible = this.settingModel.getIsTodayPostCount() != 0;
        }
        this.adapter.setTodayNumVisibile(isTodayTotalVisible);
        this.adapter.setDatas(this.parentList);
        this.adapter.notifyDataSetChanged();
    }

    protected BaseAdapter getListAdapter() {
        this.adapter = new BoardListAdapter1(this.activity, this.parentList);
        this.adapter.setComponentModel(this.moduleModel);
        if (this.fid != -1) {
            this.adapter.setChildBoard(true);
        }
        return this.adapter;
    }
}
