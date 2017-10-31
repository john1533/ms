package com.mobcent.discuz.activity.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mobcent.discuz.android.model.PollItemModel;
import com.mobcent.lowest.base.utils.MCResource;

public class MCPollSelectBar {
    private final int MAX_PROGRESS = 100;
    private TextView contentText;
    private Context context;
    private MCResource forumResource;
    private boolean isSelect = false;
    private LayoutInflater layoutInflater;
    private TextView numText;
    private PollItemModel pollItem;
    private LinearLayout pollResultBox;
    private Button seclectBtn;
    private SelectPollDelegate selectPoll;
    private View view;
    private ProgressBar voteRsProgress;
    private TextView voteRsText;

    public interface SelectPollDelegate {
        void onItemSelect(View view, MCPollSelectBar mCPollSelectBar, boolean z);
    }

    public MCPollSelectBar(LayoutInflater layoutInflater, MCResource forumResource, PollItemModel pollItem, SelectPollDelegate selectPoll, Context context) {
        this.layoutInflater = layoutInflater;
        this.selectPoll = selectPoll;
        this.pollItem = pollItem;
        this.forumResource = forumResource;
        this.context = context;
        initSelectVoteBar();
        initSelectVoteBarActions();
    }

    private void initSelectVoteBar() {
        this.view = this.layoutInflater.inflate(this.forumResource.getLayoutId("poll_select_item"), null);
        this.seclectBtn = (Button) this.view.findViewById(this.forumResource.getViewId("mc_froum_select_btn"));
        this.contentText = (TextView) this.view.findViewById(this.forumResource.getViewId("mc_forum_poll_item_text"));
        this.voteRsProgress = (ProgressBar) this.view.findViewById(this.forumResource.getViewId("mc_forum_vote_rs_progress"));
        this.voteRsProgress.setMax(100);
        this.voteRsText = (TextView) this.view.findViewById(this.forumResource.getViewId("mc_forum_vote_rs_text"));
        this.pollResultBox = (LinearLayout) this.view.findViewById(this.forumResource.getViewId("mc_froum_poll_result_box"));
        this.numText = (TextView) this.view.findViewById(this.forumResource.getViewId("mc_forum_vote_num_lable_text"));
        this.numText.setText(this.pollItem.getItemId() + "");
        this.contentText.setText(this.pollItem.getPollName());
    }

    public void updateResultView(PollItemModel pollModel) {
        this.seclectBtn.setVisibility(8);
        if (pollModel != null) {
            this.voteRsProgress.setProgress((int) (pollModel.getRatio() * 100.0d));
            this.pollResultBox.setVisibility(0);
            double percent = pollModel.getRatio() * 100.0d;
            this.voteRsText.setText(pollModel.getTotalNum() + "(" + (String.format("%.2f", new Object[]{Double.valueOf(percent)}) + "%") + ")");
        }
    }

    private void initSelectVoteBarActions() {
        this.view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCPollSelectBar.this.getSelect();
                MCPollSelectBar.this.selectPoll.onItemSelect(v, MCPollSelectBar.this, MCPollSelectBar.this.isSelect);
            }
        });
        this.seclectBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCPollSelectBar.this.getSelect();
                MCPollSelectBar.this.selectPoll.onItemSelect(v, MCPollSelectBar.this, MCPollSelectBar.this.isSelect);
            }
        });
    }

    private void getSelect() {
        if (this.isSelect) {
            this.seclectBtn.setBackgroundResource(this.forumResource.getDrawableId("mc_forum_detail_select_n"));
            this.isSelect = false;
            return;
        }
        this.seclectBtn.setBackgroundResource(this.forumResource.getDrawableId("mc_forum_detail_select_h"));
        this.isSelect = true;
    }

    public View getView() {
        return this.view;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean isSelect) {
        if (isSelect) {
            this.seclectBtn.setBackgroundResource(this.forumResource.getDrawableId("mc_forum_detail_select_n"));
        } else {
            this.seclectBtn.setBackgroundResource(this.forumResource.getDrawableId("mc_forum_detail_select_h"));
        }
        this.isSelect = isSelect;
    }

    public PollItemModel getPollItem() {
        return this.pollItem;
    }

    public void setPollItem(PollItemModel pollItem) {
        this.pollItem = pollItem;
    }
}
