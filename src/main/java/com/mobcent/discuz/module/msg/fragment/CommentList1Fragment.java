package com.mobcent.discuz.module.msg.fragment;

import android.view.View;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.msg.fragment.adapter.CommentList1FragmentAdapter;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import java.util.List;

public class CommentList1Fragment extends BaseCommentAtListFragment {
    protected String getRootLayoutName() {
        return "comment_list1_fragment";
    }

    protected void initViews(View rootView) {
        this.listView = (PullToRefreshListView) findViewByName(rootView, "comment_list");
        if (this.adapter == null) {
            this.adapter = new CommentList1FragmentAdapter(this.activity, this.commentAtList, this.componentModel);
        }
        this.listView.setAdapter(this.adapter);
    }

    public BaseResultModel<List<CommentAtModel>> getCommentAtList() {
        return this.msgService.getCommentAtList("post", this.currentPage, this.pageSize, this.isLocal);
    }

    protected void componentDealTopbar() {
        TopSettingModel topSettingModel = createTopSettingModel();
        topSettingModel.title = this.resource.getString("mc_forum_comment");
        dealTopBar(topSettingModel);
    }
}
