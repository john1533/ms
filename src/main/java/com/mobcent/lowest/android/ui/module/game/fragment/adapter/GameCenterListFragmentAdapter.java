package com.mobcent.lowest.android.ui.module.game.fragment.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.game.activity.GameDetailActivity;
import com.mobcent.lowest.android.ui.module.game.constant.GameConstance;
import com.mobcent.lowest.android.ui.module.game.fragment.adapter.holder.GameCenterListFragmentAdapterHolder;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.game.api.constant.GameApiConstant;
import com.mobcent.lowest.module.game.db.GameDBUtil;
import com.mobcent.lowest.module.game.model.WebGameModel;
import java.util.List;

public class GameCenterListFragmentAdapter extends BaseGameListFragmentAdapter {
    private final int FULL_STAT = 5;
    private Activity activity;
    private AlertDialog dlg;
    private GameListListener gameListListener;
    private boolean isScrolling;
    private String tag;
    private List<WebGameModel> webGameList;

    public interface GameListListener {
        void deleteGameFromLocal(long j);
    }

    public boolean isScrolling() {
        return this.isScrolling;
    }

    public void setScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }

    public GameCenterListFragmentAdapter(Context context, List<WebGameModel> webGameList, String tag, GameListListener gameListListener) {
        super(context);
        this.context = context;
        this.webGameList = webGameList;
        this.tag = tag;
        this.gameListListener = gameListListener;
        this.activity = (Activity) context;
    }

    public int getCount() {
        return this.webGameList.size();
    }

    public Object getItem(int position) {
        return this.webGameList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GameCenterListFragmentAdapterHolder holder;
        WebGameModel webGameModel = (WebGameModel) getItem(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.mcResource.getLayoutId("mc_game_center_fragment_item"), null);
            holder = new GameCenterListFragmentAdapterHolder();
            initGameCenterFragmentAdapterHolder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (GameCenterListFragmentAdapterHolder) convertView.getTag();
        }
        updataGameCenterListFragmentAdapterHolder(holder, webGameModel, position);
        onClickGameCenterListFragmentAdapterHolder(holder, webGameModel, position, convertView);
        return convertView;
    }

    private void initGameCenterFragmentAdapterHolder(View convertView, GameCenterListFragmentAdapterHolder holder) {
        ImageView thumbnail = (ImageView) convertView.findViewById(this.mcResource.getViewId("mc_game_center_list_thumbnail"));
        TextView gameName = (TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_center_list_game_name"));
        Button intoGameBtn = (Button) convertView.findViewById(this.mcResource.getViewId("mc_game_center_center_list_into_game"));
        TextView gameDescription = (TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_center_list_game_description"));
        LinearLayout startLayout = (LinearLayout) convertView.findViewById(this.mcResource.getViewId("mc_game_center_list_game_rank"));
        TextView players = (TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_center_list_game_player"));
        RelativeLayout itemLayout = (RelativeLayout) convertView.findViewById(this.mcResource.getViewId("game_item_layout"));
        ((TextView) convertView.findViewById(this.mcResource.getViewId("mc_game_center_content_msg_text"))).getPaint().setFakeBoldText(true);
        holder.setThumbnail(thumbnail);
        holder.setGameName(gameName);
        holder.setGameUrl(intoGameBtn);
        holder.setGameDesc(gameDescription);
        holder.setStartLayout(startLayout);
        holder.setPlayers(players);
        holder.setGameItemLayout(itemLayout);
    }

    private void updataGameCenterListFragmentAdapterHolder(GameCenterListFragmentAdapterHolder holder, WebGameModel webGameModel, int position) {
        webGameModel.setPosition(position);
        holder.getGameName().setText(webGameModel.getGameName());
        holder.getGameDesc().setText(webGameModel.getGameDesc());
        holder.getThumbnail().setBackgroundResource(this.mcResource.getRawId("mc_forum_x_img"));
        holder.getThumbnail().setTag(webGameModel.getGameIcon());
        holder.getPlayers().setText(this.mcResource.getString("mc_game_player_num_pre") + webGameModel.getGamePlayer() + this.mcResource.getString("mc_game_player_num_suffix"));
        addStar(holder.getStartLayout(), webGameModel.getGameStars());
        ImageView iv = holder.getThumbnail();
        iv.setImageDrawable(null);
        if (!this.isScrolling) {
            loadImageByUrl(iv);
        }
    }

    private void addStar(LinearLayout startLayout, int starNum) {
        startLayout.removeAllViews();
        for (int i = 0; i < starNum; i++) {
            ImageView iv = new ImageView(this.activity);
            iv.setImageDrawable(this.mcResource.getDrawable("mc_forum_game_star1"));
            int width = MCPhoneUtil.dip2px(this.activity, 15.0f);
            startLayout.addView(iv, width, width);
        }
        for (int j = 0; j < 5 - starNum; j++) {
            ImageView iv = new ImageView(this.activity);
            iv.setImageDrawable(this.mcResource.getDrawable("mc_forum_game_star2"));
            int width = MCPhoneUtil.dip2px(this.activity, 15.0f);
            startLayout.addView(iv, width, width);
        }
    }

    private void onClickGameCenterListFragmentAdapterHolder(GameCenterListFragmentAdapterHolder holder, final WebGameModel webGameModel, final int position, View convertView) {
        holder.getGameUrl().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameCenterListFragmentAdapter.this.context, GameDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GameConstance.INTENT_TO_DETAIL_MODEL, webGameModel);
                intent.putExtras(bundle);
                GameCenterListFragmentAdapter.this.context.startActivity(intent);
            }
        });
        holder.getGameItemLayout().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameCenterListFragmentAdapter.this.context, GameDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(GameConstance.INTENT_TO_DETAIL_MODEL, webGameModel);
                intent.putExtras(bundle);
                GameCenterListFragmentAdapter.this.context.startActivity(intent);
            }
        });
        if (GameApiConstant.MY_TAG.equals(this.tag)) {
            convertView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    GameCenterListFragmentAdapter.this.showCommentDiaLog(webGameModel, position);
                    return true;
                }
            });
        }
    }

    private void addMyGame(WebGameModel webGameModel) {
        GameDBUtil.getInstance(this.context, GameApiConstant.MY_TAG).saveMyWebGame(webGameModel, GameApiConstant.MY_TAG);
    }

    private void showCommentDiaLog(final WebGameModel webGameModel, final int position) {
        this.dlg = new Builder(this.context).create();
        this.dlg.show();
        Window window = this.dlg.getWindow();
        this.dlg.setContentView(this.mcResource.getLayoutId("mc_game_center_delete_dialog"));
        TextView deleteFunction = (TextView) window.findViewById(this.mcResource.getViewId("mc_game_center_delete_function"));
        ((TextView) window.findViewById(this.mcResource.getViewId("mc_game_center_delete_name"))).setText(webGameModel.getGameName());
        deleteFunction.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameCenterListFragmentAdapter.this.gameListListener.deleteGameFromLocal(webGameModel.getGameId());
                GameCenterListFragmentAdapter.this.webGameList.remove(position);
                GameCenterListFragmentAdapter.this.notifyDataSetChanged();
                GameCenterListFragmentAdapter.this.dlg.dismiss();
            }
        });
    }
}
