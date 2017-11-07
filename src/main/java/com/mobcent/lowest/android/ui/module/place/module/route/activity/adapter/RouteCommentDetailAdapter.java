package com.mobcent.lowest.android.ui.module.place.module.route.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.place.activity.PlaceMapViewActivity;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.module.route.model.RouteSearchMessageModel;
import java.util.ArrayList;

public class RouteCommentDetailAdapter extends BaseRouteAdapter {
    private Activity activity;
    private Context context;
    private ArrayList<String[]> routeList;
    private RouteSearchMessageModel searchMsgModel;
    private int searcheType;

    class DetailHodler {
        private TextView detail;
        private RelativeLayout itemView;
        private Button step;

        DetailHodler() {
        }

        public TextView getDetail() {
            return this.detail;
        }

        public void setDetail(TextView detail) {
            this.detail = detail;
        }

        public Button getStep() {
            return this.step;
        }

        public void setStep(Button step) {
            this.step = step;
        }

        public RelativeLayout getItemView() {
            return this.itemView;
        }

        public void setItemView(RelativeLayout itemView) {
            this.itemView = itemView;
        }
    }

    public RouteCommentDetailAdapter(Activity activity, ArrayList<String[]> routeList, RouteSearchMessageModel searchMsgModel) {
        super(activity);
        this.context = activity;
        this.activity = activity;
        this.routeList = routeList;
        this.searchMsgModel = searchMsgModel;
    }

    public void updataData(ArrayList<String[]> routeList, RouteSearchMessageModel searchMsgModel) {
        this.routeList = routeList;
        this.searchMsgModel = searchMsgModel;
    }

    public int getCount() {
        return this.routeList.size();
    }

    public Object getItem(int position) {
        return this.routeList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DetailHodler holder;
        String step = ((String[]) getItem(position))[1];
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mc_place_route_comment_detail_item"), null);
            holder = new DetailHodler();
            initDetailLineAdapterHolder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (DetailHodler) convertView.getTag();
        }
        updataLineView(holder, step, position);
        clickViewAction(holder, position);
        return convertView;
    }

    private void clickViewAction(DetailHodler holder, final int position) {
        holder.getItemView().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent mapViewIntent = new Intent(RouteCommentDetailAdapter.this.context, PlaceMapViewActivity.class);
                Bundle bundle = new Bundle();
                if (RouteCommentDetailAdapter.this.searchMsgModel.getSearchType() != 1) {
                    RouteCommentDetailAdapter.this.searchMsgModel.setSearchNum(position);
                }
                bundle.putSerializable(RouteConstant.SEARCH_MSG_MODEL, RouteCommentDetailAdapter.this.searchMsgModel);
                mapViewIntent.putExtra(RouteConstant.MAP_VIEW_BUNDLE, bundle);
                RouteCommentDetailAdapter.this.activity.startActivity(mapViewIntent);
            }
        });
    }

    private void updataLineView(DetailHodler holder, String detailStep, int position) {
        holder.getStep().setText((position + 1) + "");
        if (detailStep != null) {
            holder.getDetail().setText(Html.fromHtml(detailStep));
        }
    }

    private void initDetailLineAdapterHolder(View convertView, DetailHodler holder) {
        Button step = (Button) convertView.findViewById(this.resource.getViewId("comment_detail_item_step"));
        RelativeLayout item = (RelativeLayout) convertView.findViewById(this.resource.getViewId("route_comment_detail_item"));
        holder.setDetail((TextView) convertView.findViewById(this.resource.getViewId("comment_detail_item_text")));
        holder.setStep(step);
        holder.setItemView(item);
    }
}
