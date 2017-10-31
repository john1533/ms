package com.mobcent.discuz.module.publish.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.base.widget.MCBoardSortView;
import com.mobcent.discuz.module.publish.adapter.holder.PublishBoardListAdapterHolder;
import java.util.List;

public class PublishBoardListAdapter extends BaseListAdatper<BoardParent, PublishBoardListAdapterHolder> {
    private long boardId;
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag() != null && (v.getTag() instanceof BoardChild)) {
                BoardChild boardChild = (BoardChild) v.getTag();
            }
        }
    };
    private PermissionModel permissionModel;

    public PermissionModel getPermissionModel() {
        return this.permissionModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public PublishBoardListAdapter(Context context, List<BoardParent> datas, long boardId) {
        super(context, datas);
        this.boardId = boardId;
    }

    protected void initViews(View convertView, PublishBoardListAdapterHolder holder) {
        holder.setTitleText((TextView) findViewByName(convertView, "mc_forum_board_title"));
        holder.setBox1((LinearLayout) findViewByName(convertView, "mc_forum_board_content_box1"));
        holder.setBox2((LinearLayout) findViewByName(convertView, "mc_forum_board_content_box2"));
    }

    protected void initViewDatas(PublishBoardListAdapterHolder holder, BoardParent data, int position) {
        int len;
        List<BoardChild> boardList = data.getChildList();
        holder.getTitleText().setText(data.getBoardCategoryName());
        holder.getBox1().removeAllViews();
        holder.getBox2().removeAllViews();
        int size = boardList.size();
        if (size % 2 == 0) {
            len = size / 2;
        } else {
            len = (size / 2) + 1;
        }
        int currentPosition = 0;
        for (int i = 0; i < len; i++) {
            BoardChild child1 = (BoardChild) boardList.get(currentPosition);
            View view = new MCBoardSortView(this.context, child1, this.permissionModel, this.boardId);
            view.setTag(child1);
            holder.getBox1().addView(view);
            currentPosition++;
            BoardChild child2 = null;
            if (currentPosition < size) {
                child2 = (BoardChild) boardList.get(currentPosition);
                currentPosition++;
            }
            if (child2 != null) {
                View view2 = new MCBoardSortView(this.context, child2, this.permissionModel, this.boardId);
                view2.setTag(child2);
                holder.getBox2().addView(view2);
            }
        }
    }

    protected String getLayoutName() {
        return "select_board_sort_item";
    }

    protected PublishBoardListAdapterHolder instanceHolder() {
        return new PublishBoardListAdapterHolder();
    }
}
