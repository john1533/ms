package com.mobcent.discuz.module.msg.fragment.adapter;

import android.content.Context;
import com.mobcent.discuz.android.model.CommentAtModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import java.util.List;

public class CommentList1FragmentAdapter extends BaseCommentAtListFragmentAdapter {
    public CommentList1FragmentAdapter(Context context, List<CommentAtModel> list, ConfigComponentModel componentModel) {
        super(context, list, componentModel);
    }

    protected String getLayoutName() {
        return "comment_list1_fragment_item";
    }
}
