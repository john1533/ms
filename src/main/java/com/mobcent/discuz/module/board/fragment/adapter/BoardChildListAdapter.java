package com.mobcent.discuz.module.board.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mobcent.discuz.activity.WebViewFragmentActivity;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BoardChild;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.board.fragment.adapter.holder.BoardChildAdapterDoubleHolder;
import com.mobcent.discuz.module.board.fragment.adapter.holder.BoardChildAdapterSingleHolder;
import com.mobcent.discuz.module.topic.list.fragment.activity.TopicListActivty;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class BoardChildListAdapter extends BaseChildBoardAdatper {
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag() != null && (v.getTag() instanceof BoardChild)) {
                Intent intent;
                BoardChild board = (BoardChild) v.getTag();
                if (MCStringUtil.isEmpty(board.getForumRedirect())) {
                    intent = new Intent(BoardChildListAdapter.this.context, TopicListActivty.class);
                    intent.putExtra("boardId", board.getBoardId());
                    intent.putExtra(IntentConstant.INTENT_BOARD_CHILD, board.getBoardChild());
                    intent.putExtra(IntentConstant.INTENT_BOARD_CONTENT, board.getBoardContent());
                    intent.putExtra("boardName", board.getBoardName());
                    intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, BoardChildListAdapter.this.componentModel);
                } else {
                    intent = new Intent(BoardChildListAdapter.this.context, WebViewFragmentActivity.class);
                    intent.putExtra("webViewUrl", board.getForumRedirect());
                }
                v.getContext().startActivity(intent);
            }
        }
    };
    private ConfigComponentModel componentModel;
    private boolean isTodayNumVisibile = false;

    public ConfigComponentModel getComponentModel() {
        return this.componentModel;
    }

    public void setComponentModel(ConfigComponentModel componentModel) {
        this.componentModel = componentModel;
    }

    public void setTodayNumVisibile(boolean isTodayNumVisibile) {
        this.isTodayNumVisibile = isTodayNumVisibile;
    }

    public BoardChildListAdapter(Context context, List<BoardChild> datas, String style) {
        super(context, datas, style);
    }

    protected void initViews(View convertView, Object holder) {
        if (holder instanceof BoardChildAdapterSingleHolder) {
            BoardChildAdapterSingleHolder mHolder = (BoardChildAdapterSingleHolder) holder;
            mHolder.setImg((ImageView) findViewByName(convertView, "mc_forum_board_img"));
            mHolder.setBoardName((TextView) findViewByName(convertView, "mc_forum_board_name_text"));
            mHolder.setBoardTime((TextView) findViewByName(convertView, "mc_forum_board_time_text"));
            mHolder.setMidLine(findViewByName(convertView, "mc_forum_single_line"));
            mHolder.setTotalText((TextView) findViewByName(convertView, "mc_forum_today_total_text"));
            mHolder.setRootBox(findViewByName(convertView, "root_layout"));
            return;
        }
        BoardChildAdapterDoubleHolder mHolder2 = (BoardChildAdapterDoubleHolder) holder;
        mHolder2.setLeftBox((LinearLayout) findViewByName(convertView, "mc_forum_board_left_item_box"));
        mHolder2.setLeftImg((ImageView) findViewByName(convertView, "mc_forum_board_img1"));
        mHolder2.setLeftBoardName((TextView) findViewByName(convertView, "mc_forum_board_left_name_text"));
        mHolder2.setLeftBoardTime((TextView) findViewByName(convertView, "mc_forum_board_left_time_text"));
        mHolder2.setLeftTotalNum((TextView) findViewByName(convertView, "mc_forum_board_left_today_total_text"));
        mHolder2.setRightBox((LinearLayout) findViewByName(convertView, "mc_forum_board_right_item_box"));
        mHolder2.setRightImg((ImageView) findViewByName(convertView, "mc_forum_board_img2"));
        mHolder2.setRightBoardName((TextView) findViewByName(convertView, "mc_forum_board_right_name_text"));
        mHolder2.setRightBoardTime((TextView) findViewByName(convertView, "mc_forum_board_right_time_text"));
        mHolder2.setRightTotalNum((TextView) findViewByName(convertView, "mc_forum_board_right_today_total_text"));
    }

    protected void init1Column(Object holder, BoardChild data) {
        if (holder instanceof BoardChildAdapterSingleHolder) {
            BoardChildAdapterSingleHolder mHolder = (BoardChildAdapterSingleHolder) holder;
            mHolder.getBoardName().setText(data.getBoardName());
            mHolder.getBoardTime().setText(MCDateUtil.getFormatTimeByWord(this.resource, data.getLastPostsDate(), "yyyy-MM-dd HH:mm"));
            if (TextUtils.isEmpty(data.getBoardImg())) {
                mHolder.getImg().setVisibility(8);
            } else {
                loadImage(mHolder.getImg(), data.getBoardImg());
            }
            if (data.getTodayPostsNum() == 0 || !this.isTodayNumVisibile) {
                mHolder.getTotalText().setVisibility(8);
                mHolder.getMidLine().setVisibility(8);
            } else {
                mHolder.getTotalText().setVisibility(0);
                mHolder.getMidLine().setVisibility(8);
                mHolder.getTotalText().setText(data.getTodayPostsNum() + "");
            }
            mHolder.getRootBox().setTag(data);
            mHolder.getRootBox().setOnClickListener(this.clickListener);
        }
    }

    protected void init2Column(Object holder, BoardChild leftChild, BoardChild rightChild) {
        if (holder instanceof BoardChildAdapterDoubleHolder) {
            BoardChildAdapterDoubleHolder mHolder = (BoardChildAdapterDoubleHolder) holder;
            if (leftChild != null) {
                mHolder.getLeftBoardName().setText(leftChild.getBoardName());
                mHolder.getLeftBoardTime().setText(MCDateUtil.getFormatTimeByWord(this.resource, leftChild.getLastPostsDate(), "yyyy-MM-dd HH:mm"));
                if (leftChild.getTodayPostsNum() == 0 || !this.isTodayNumVisibile) {
                    mHolder.getLeftTotalNum().setVisibility(8);
                } else {
                    mHolder.getLeftTotalNum().setVisibility(0);
                    mHolder.getLeftTotalNum().setText("(" + Integer.toString(leftChild.getTodayPostsNum()) + ")");
                }
                if (TextUtils.isEmpty(leftChild.getBoardImg())) {
                    mHolder.getLeftImg().setVisibility(8);
                } else {
                    loadImage(mHolder.getLeftImg(), leftChild.getBoardImg());
                }
                mHolder.getLeftBox().setTag(leftChild);
                mHolder.getLeftBox().setOnClickListener(this.clickListener);
            }
            if (rightChild != null) {
                mHolder.getRightBoardName().setText(rightChild.getBoardName());
                mHolder.getRightBoardTime().setText(MCDateUtil.getFormatTimeByWord(this.resource, rightChild.getLastPostsDate(), "yyyy-MM-dd HH:mm"));
                if (rightChild.getTodayPostsNum() == 0 || !this.isTodayNumVisibile) {
                    mHolder.getRightTotalNum().setVisibility(8);
                } else {
                    mHolder.getRightTotalNum().setVisibility(0);
                    mHolder.getRightTotalNum().setText("(" + Integer.toString(rightChild.getTodayPostsNum()) + ")");
                }
                if (TextUtils.isEmpty(rightChild.getBoardImg())) {
                    mHolder.getRightImg().setVisibility(8);
                } else {
                    loadImage(mHolder.getRightImg(), rightChild.getBoardImg());
                }
                mHolder.getRightBox().setTag(rightChild);
                mHolder.getRightBox().setOnClickListener(this.clickListener);
                return;
            }
            mHolder.getRightImg().setImageBitmap(null);
            mHolder.getRightBoardName().setText("");
            mHolder.getRightBoardTime().setText("");
            mHolder.getRightTotalNum().setText("");
        }
    }

    protected Object instanceHolder() {
        if (this.column == 1) {
            return new BoardChildAdapterSingleHolder();
        }
        return new BoardChildAdapterDoubleHolder();
    }

    protected String getItemLayoutName() {
        if (this.style.equals(StyleConstant.STYLE_DEFAULT)) {
            if (this.column == 1) {
                return "board_child_list_fragment_item1_single";
            }
            return "board_child_list_fragment_item1_double";
        } else if (this.column == 1) {
            return "board_child_list_fragment_item2_single";
        } else {
            return "board_child_list_fragment_item2_double";
        }
    }
}
