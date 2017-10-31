package com.mobcent.discuz.module.publish.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.BoardModel;
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.android.model.ConfigFastPostModel;
import com.mobcent.discuz.android.service.BoardService;
import com.mobcent.discuz.android.service.impl.BoardServiceImpl;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.publish.adapter.PublishBoardListAdapter;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.ArrayList;
import java.util.List;

public class SelectBoardFragment extends BaseFragment {
    private PublishBoardListAdapter adapter;
    private long boardId;
    private List<ConfigFastPostModel> boardList;
    protected BoardService boardService;
    private GetDataTask dataTask;
    private ListView listView;
    protected ArrayList<BoardParent> parentList = null;

    class GetDataTask extends AsyncTask<Void, Void, BaseResultModel<BoardModel>> {
        GetDataTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected BaseResultModel<BoardModel> doInBackground(Void... params) {
            BaseResultModel<BoardModel> baseResultModel = SelectBoardFragment.this.boardService.getBoardModelByLocal();
            if (baseResultModel == null || baseResultModel.getRs() != 0) {
                return baseResultModel;
            }
            return SelectBoardFragment.this.boardService.getBoardModelByNet();
        }

        protected void onPostExecute(BaseResultModel<BoardModel> result) {
            super.onPostExecute(result);
            DZToastAlertUtils.toast(SelectBoardFragment.this.activity, result);
            if (result != null && result.getData() != null) {
                if (((BoardModel) result.getData()).getParentList() != null) {
                    SelectBoardFragment.this.parentList.clear();
                    SelectBoardFragment.this.parentList.addAll(((BoardModel) result.getData()).getParentList());
                }
                SelectBoardFragment.this.adapter.setDatas(SelectBoardFragment.this.parentList);
                SelectBoardFragment.this.adapter.notifyDataSetChanged();
            }
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.boardService = new BoardServiceImpl(this.activity.getApplicationContext());
        this.parentList = new ArrayList();
        this.boardList = (List) getBundle().getSerializable(IntentConstant.INTENT_BOARDLIST);
        this.boardId = getBundle().getLong("boardId");
    }

    protected String getRootLayoutName() {
        return "select_board_fragment";
    }

    protected void initViews(View rootView) {
        this.listView = (ListView) findViewByName(rootView, "mc_forum_board_list_view");
        if (this.adapter == null) {
            this.adapter = new PublishBoardListAdapter(this.activity, this.parentList, this.boardId);
        }
        this.adapter.setPermissionModel(this.permissionModel);
        this.listView.setAdapter(this.adapter);
        if (MCListUtils.isEmpty(this.boardList)) {
            this.dataTask = new GetDataTask();
            this.dataTask.execute(new Void[0]);
            return;
        }
        this.adapter.setDatas(getBoardParentList());
        this.adapter.notifyDataSetChanged();
    }

    private ArrayList<BoardParent> getBoardParentList() {
        ArrayList<BoardParent> list = new ArrayList();
        List<BoardChild> boardChilds = new ArrayList();
        for (int i = 0; i < this.boardList.size(); i++) {
            BoardChild boardChild = new BoardChild();
            boardChild.setBoardId(((ConfigFastPostModel) this.boardList.get(i)).getFid());
            boardChild.setBoardName(((ConfigFastPostModel) this.boardList.get(i)).getName());
            boardChilds.add(boardChild);
        }
        BoardParent boardParent = new BoardParent();
        boardParent.setChildList(boardChilds);
        boardParent.setBoardCategoryName(this.resource.getString("mc_forum_select_board_item"));
        list.add(boardParent);
        return list;
    }

    protected void initActions(View rootView) {
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_select_borad");
        dealTopBar(topSettingModel);
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.dataTask != null) {
            this.dataTask.cancel(true);
        }
    }
}
