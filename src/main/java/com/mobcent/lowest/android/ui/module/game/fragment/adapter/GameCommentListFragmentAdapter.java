package com.mobcent.lowest.android.ui.module.game.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.game.fragment.adapter.holder.GameCommentAdapterHolder;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.game.model.GameCommentModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameCommentListFragmentAdapter extends BaseGameListFragmentAdapter {
    private List<GameCommentModel> gameList;

    public GameCommentListFragmentAdapter(Context context, List<GameCommentModel> gameList) {
        super(context);
        this.context = context;
        this.gameList = gameList;
    }

    public int getCount() {
        return this.gameList.size();
    }

    public Object getItem(int position) {
        return this.gameList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GameCommentAdapterHolder holder;
        GameCommentModel gameCommentModel = (GameCommentModel) getItem(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.mcResource.getLayoutId("mc_game_detail_comment_item"), null);
            holder = new GameCommentAdapterHolder();
            initGameComomentFragmentAdapterHolder(convertView, holder, gameCommentModel);
            convertView.setTag(holder);
        } else {
            holder = (GameCommentAdapterHolder) convertView.getTag();
        }
        updataGameCommendFragmentAdapterHolder(holder, gameCommentModel, position);
        return convertView;
    }

    private void initGameComomentFragmentAdapterHolder(View convertView, GameCommentAdapterHolder holder, GameCommentModel gameCommentModel) {
        TextView critic = (TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_datail_critic"));
        TextView commentDate = (TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_detail_comment_date"));
        holder.setCommentContent((TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_detail_comment_content")));
        holder.setCritic(critic);
        holder.setTime(commentDate);
    }

    private void updataGameCommendFragmentAdapterHolder(GameCommentAdapterHolder holder, GameCommentModel gameCommentModel, int position) {
        gameCommentModel.setPosition(position);
        holder.getCritic().setText(gameCommentModel.getCritic());
        holder.getCommentContent().setText(gameCommentModel.getCommentContent());
        holder.getTime().setText(convertTime(this.context, Long.valueOf(gameCommentModel.getTime()).longValue(), this.mcResource));
    }

    public static String convertTime(Context context, long lastReplyDate, MCResource mcResource) {
        Date createdAt = new Date(lastReplyDate);
        long l = new Date().getTime() - createdAt.getTime();
        long day = l / 86400000;
        long hour = (l / 3600000) - (24 * day);
        long min = ((l / 60000) - ((24 * day) * 60)) - (60 * hour);
        long s = (((l / 1000) - (((24 * day) * 60) * 60)) - ((60 * hour) * 60)) - (60 * min);
        if (day > 0) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(createdAt);
        }
        if (hour > 0) {
            return hour + mcResource.getString("mc_forum_hour_before");
        }
        if (min > 0) {
            return min + mcResource.getString("mc_forum_minute_before");
        }
        if (s < 0) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(createdAt);
        }
        return s + mcResource.getString("mc_forum_mill_before");
    }
}
