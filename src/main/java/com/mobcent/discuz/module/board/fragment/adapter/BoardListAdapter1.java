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
import com.mobcent.discuz.android.model.BoardParent;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.module.board.fragment.adapter.holder.BoardAdapterHolder;
import com.mobcent.discuz.module.topic.list.fragment.activity.TopicListActivty;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.List;

public class BoardListAdapter1 extends BaseBoardAdatper {
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v.getTag() != null && (v.getTag() instanceof BoardChild)) {
                Intent intent;
                BoardChild board = (BoardChild) v.getTag();
                if (MCStringUtil.isEmpty(board.getForumRedirect())) {
                    intent = new Intent(BoardListAdapter1.this.context, TopicListActivty.class);
                    intent.putExtra("boardId", board.getBoardId());
                    intent.putExtra(IntentConstant.INTENT_BOARD_CHILD, board.getBoardChild());
                    intent.putExtra(IntentConstant.INTENT_BOARD_CONTENT, board.getBoardContent());
                    intent.putExtra("boardName", board.getBoardName());
                    intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, BoardListAdapter1.this.getComponentModel());
                    if (BoardListAdapter1.this.isCard()) {
                        intent.putExtra("style", "card");
                    } else {
                        intent.putExtra("style", StyleConstant.STYLE_DEFAULT);
                    }
                } else {
                    intent = new Intent(BoardListAdapter1.this.context, WebViewFragmentActivity.class);
                    intent.putExtra("webViewUrl", board.getForumRedirect());
                }
                v.getContext().startActivity(intent);
            }
        }
    };
    protected boolean isTodayNumVisibile = true;

    public boolean isTodayNumVisibile() {
        return this.isTodayNumVisibile;
    }

    public void setTodayNumVisibile(boolean isTodayNumVisibile) {
        this.isTodayNumVisibile = isTodayNumVisibile;
    }

    public BoardListAdapter1(Context context, List<BoardParent> datas) {
        super(context, datas);
    }

    protected String getLayoutName() {
        return "board_list_fragment_item1";
    }

    protected BoardAdapterHolder instanceHolder() {
        return new BoardAdapterHolder();
    }

    protected String getSingleLayoutName() {
        return "board_list_fragment_item1_single";
    }

    protected String getDoubleLayoutName() {
        return "board_list_fragment_item1_double";
    }

    protected void initSingleView(View parent, BoardChild boardChild, boolean isLast) {
        LinearLayout topBox = (LinearLayout) findViewByName(parent, "item_top_layout");
        ImageView img = (ImageView) findViewByName(parent, "mc_forum_board_img");
        TextView boardName = (TextView) findViewByName(parent, "mc_forum_board_name_text");
        TextView boardTime = (TextView) findViewByName(parent, "mc_forum_board_time_text");
        TextView totalText = (TextView) findViewByName(parent, "mc_forum_today_total_text");
        View midLine = findViewByName(parent, "mc_forum_single_line");
        boardName.setText(boardChild.getBoardName());
        boardTime.setText(MCDateUtil.getFormatTimeByWord(this.resource, boardChild.getLastPostsDate(), "yyyy-MM-dd HH:mm"));
        totalText.setText(boardChild.getTodayPostsNum() + "");
        if (isLast && isCard()) {
            findViewByName(parent, "line_view").setVisibility(8);
        }
        if (boardChild.getTodayPostsNum() == 0 || !this.isTodayNumVisibile) {
            totalText.setText("");
        } else {
            totalText.setText(boardChild.getTodayPostsNum() + "");
        }
        if (midLine != null) {
            midLine.setVisibility(8);
        }
        if (TextUtils.isEmpty(boardChild.getBoardImg())) {
            img.setVisibility(8);
        } else {
            loadImage(img, boardChild.getBoardImg());
        }
        topBox.setTag(boardChild);
        topBox.setOnClickListener(this.clickListener);
    }

    protected void initDoubleView(View parent, BoardChild leftChild, BoardChild rightChild, boolean isLast) {
        if (leftChild != null) {
            LinearLayout leftBox = (LinearLayout) findViewByName(parent, "mc_forum_board_left_item_box");
            ImageView leftImg = (ImageView) findViewByName(parent, "mc_forum_board_img1");
            TextView leftName = (TextView) findViewByName(parent, "mc_forum_board_left_name_text");
            TextView leftTotal = (TextView) findViewByName(parent, "mc_forum_board_left_today_total_text");
            TextView leftTime = (TextView) findViewByName(parent, "mc_forum_board_left_time_text");
            if (TextUtils.isEmpty(leftChild.getBoardImg())) {
                leftImg.setVisibility(8);
            } else {
                loadImage(leftImg, leftChild.getBoardImg());
            }
            leftName.setText(leftChild.getBoardName());
            if (leftChild.getTodayPostsNum() == 0 || !this.isTodayNumVisibile) {
                leftTotal.setText("");
            } else {
                leftTotal.setText("(" + leftChild.getTodayPostsNum() + ")");
            }
            leftTime.setText(MCDateUtil.getFormatTimeByWord(this.resource, leftChild.getLastPostsDate(), "yyyy-MM-dd HH:mm"));
            leftBox.setTag(leftChild);
            leftBox.setOnClickListener(this.clickListener);
        }
        if (rightChild != null) {
            LinearLayout rightBox = (LinearLayout) findViewByName(parent, "mc_forum_board_right_item_box");
            ImageView rightImg = (ImageView) findViewByName(parent, "mc_forum_board_img2");
            TextView rightName = (TextView) findViewByName(parent, "mc_forum_board_right_name_text");
            TextView rightTotal = (TextView) findViewByName(parent, "mc_forum_board_right_today_total_text");
            TextView rightTime = (TextView) findViewByName(parent, "mc_forum_board_right_time_text");
            if (TextUtils.isEmpty(rightChild.getBoardImg())) {
                rightImg.setVisibility(8);
            } else {
                loadImage(rightImg, rightChild.getBoardImg());
            }
            rightName.setText(rightChild.getBoardName());
            if (rightChild.getTodayPostsNum() == 0 || !this.isTodayNumVisibile) {
                rightTotal.setText("");
            } else {
                rightTotal.setText("(" + rightChild.getTodayPostsNum() + ")");
            }
            rightTime.setText(MCDateUtil.getFormatTimeByWord(this.resource, rightChild.getLastPostsDate(), "yyyy-MM-dd HH:mm"));
            rightBox.setTag(rightChild);
            rightBox.setOnClickListener(this.clickListener);
        }
        if (isLast && isCard()) {
            findViewByName(parent, "mc_forum_board_double_line").setVisibility(8);
        }
    }
}
