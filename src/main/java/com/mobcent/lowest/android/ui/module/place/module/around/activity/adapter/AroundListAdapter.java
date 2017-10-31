package com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.android.ui.module.place.activity.adapter.PlaceBaseListAdapter;
import com.mobcent.lowest.android.ui.module.place.constant.RouteConstant;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.adapter.holder.AroundListHolder;
import com.mobcent.lowest.android.ui.module.place.module.route.activity.RouteActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.model.PlacePoiInfoModel;
import java.text.DecimalFormat;
import java.util.List;

public class AroundListAdapter extends PlaceBaseListAdapter<PlacePoiInfoModel, AroundListHolder> {
    public String TAG;

    private class OnArriveBoxClickListener implements OnClickListener {
        private PlacePoiInfoModel model;

        public OnArriveBoxClickListener(PlacePoiInfoModel model) {
            this.model = model;
        }

        public void onClick(View v) {
            Intent intent = new Intent(AroundListAdapter.this.context, RouteActivity.class);
            intent.putExtra(RouteConstant.POI_MODEL_LIST, this.model);
            AroundListAdapter.this.context.startActivity(intent);
        }
    }

    private class OnPhoneBoxClickListener implements OnClickListener {
        private PlacePoiInfoModel model;

        public OnPhoneBoxClickListener(PlacePoiInfoModel model) {
            this.model = model;
        }

        public void onClick(View v) {
            if (MCStringUtil.isEmpty(this.model.telephone)) {
                AroundListAdapter.this.warnById("mc_place_no_telephone");
                return;
            }
            AroundListAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("tel:" + this.model.telephone)));
        }
    }

    public AroundListAdapter(Context context, List<PlacePoiInfoModel> modelList) {
        super(context, modelList);
        this.TAG = "AroundListAdapter";
        this.isItemClickAble = true;
    }

    protected void initViews(View convertView, AroundListHolder holder) {
        holder.setTitleText((TextView) findViewById(convertView, "around_list_item_title_text"));
        holder.setDistanceText((TextView) findViewById(convertView, "around_list_item_distance_text"));
        holder.setAddressText((TextView) findViewById(convertView, "around_list_item_address_text"));
        holder.setRatingBar((RatingBar) findViewById(convertView, "around_service_item_ratingbar"));
        holder.setPriceLeft((TextView) findViewById(convertView, "around_list_price_left_text"));
        holder.setPriceRight((TextView) findViewById(convertView, "around_list_price_right_text"));
        holder.setTagText((TextView) findViewById(convertView, "around_list_tag_text"));
        holder.setArriveBtn((Button) findViewById(convertView, "around_service_item_arrive_btn"));
        holder.setPhoneBtn((Button) findViewById(convertView, "around_service_item_phone_btn"));
        holder.setGroupImg((ImageView) findViewById(convertView, "around_group_img"));
        holder.setSlidLine(findViewById(convertView, "around_service_item_bottom_slid_line"));
        holder.setPhoneBox((RelativeLayout) findViewById(convertView, "around_item_phone_layout"));
        holder.setArriveBox((RelativeLayout) findViewById(convertView, "around_item_arrive_layout"));
        holder.setArriveImgBtn((ImageButton) findViewById(convertView, "around_service_item_arrive_img"));
        holder.setPhoneImgBtn((ImageButton) findViewById(convertView, "around_service_item_phone_img"));
        holder.setArrowImg((ImageView) findViewById(convertView, "arrow_img"));
        holder.setItemBox((LinearLayout) findViewById(convertView, "item_layout"));
    }

    protected void initDatas(final AroundListHolder holder, PlacePoiInfoModel model, int position) {
        holder.getTitleText().setText(model.name);
        holder.getAddressText().setText(model.address);
        dealDetailInfo(holder, model);
        if (model.detailInfoModel == null || model.detailInfoModel.grouponNum <= 0) {
            holder.getGroupImg().setVisibility(8);
        } else {
            holder.getGroupImg().setVisibility(0);
        }
        if (model.detailInfoModel == null || MCStringUtil.isEmpty(model.detailInfoModel.detailUrl)) {
            holder.getArrowImg().setVisibility(8);
        } else {
            holder.getArrowImg().setVisibility(0);
        }
        if (MCStringUtil.isEmpty(model.telephone)) {
            holder.getSlidLine().setVisibility(8);
            holder.getPhoneBox().setVisibility(8);
        } else {
            holder.getSlidLine().setVisibility(0);
            holder.getPhoneBox().setVisibility(0);
        }
        holder.getItemBox().setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    holder.getArrowImg().setSelected(true);
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    holder.getArrowImg().setSelected(false);
                }
                return false;
            }
        });
        OnPhoneBoxClickListener onPhoneBoxClickListener = new OnPhoneBoxClickListener(model);
        OnArriveBoxClickListener onArriveBoxClickListener = new OnArriveBoxClickListener(model);
        holder.getPhoneBox().setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    holder.getPhoneBtn().setSelected(true);
                    holder.getPhoneImgBtn().setSelected(true);
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    holder.getPhoneBtn().setSelected(false);
                    holder.getPhoneImgBtn().setSelected(false);
                }
                return false;
            }
        });
        holder.getPhoneBox().setOnClickListener(onPhoneBoxClickListener);
        holder.getPhoneBtn().setOnClickListener(onPhoneBoxClickListener);
        holder.getPhoneImgBtn().setOnClickListener(onPhoneBoxClickListener);
        holder.getArriveBox().setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    holder.getArriveBtn().setSelected(true);
                    holder.getArriveImgBtn().setSelected(true);
                } else if (event.getAction() == 1 || event.getAction() == 3) {
                    holder.getArriveBtn().setSelected(false);
                    holder.getArriveImgBtn().setSelected(false);
                }
                return false;
            }
        });
        holder.getArriveBox().setOnClickListener(onArriveBoxClickListener);
        holder.getArriveBtn().setOnClickListener(onArriveBoxClickListener);
        holder.getArriveImgBtn().setOnClickListener(onArriveBoxClickListener);
    }

    protected String getConvertViewName() {
        return "mc_place_around_list_item";
    }

    protected AroundListHolder instanceHolder() {
        return new AroundListHolder();
    }

    protected void onItemClick(View v, PlacePoiInfoModel data) {
        Intent intent = new Intent(this.context, PlazaWebViewActivity.class);
        if (data.detailInfoModel != null && !MCStringUtil.isEmpty(data.detailInfoModel.detailUrl)) {
            intent.putExtra(PlazaConstant.WEB_VIEW_URL, data.detailInfoModel.detailUrl);
            intent.putExtra(PlazaConstant.WEB_VIEW_TOP, true);
            this.context.startActivity(intent);
        }
    }

    private void dealDetailInfo(AroundListHolder holder, PlacePoiInfoModel model) {
        if (model.detailInfoModel != null) {
            if (model.detailInfoModel.distance != 0) {
                holder.getDistanceText().setVisibility(0);
                if (model.detailInfoModel.distance > LocationClientOption.MIN_SCAN_SPAN) {
                    holder.getDistanceText().setText(new DecimalFormat("#.00").format((double) (((float) model.detailInfoModel.distance) / 1000.0f)) + " km");
                } else {
                    holder.getDistanceText().setText(model.detailInfoModel.distance + "m");
                }
            } else {
                holder.getDistanceText().setVisibility(8);
            }
            if (MCStringUtil.isEmpty(model.detailInfoModel.tag)) {
                holder.getTagText().setVisibility(8);
            } else {
                holder.getTagText().setVisibility(0);
                holder.getTagText().setText(model.detailInfoModel.tag);
            }
            if (model.detailInfoModel.type.equals("cater")) {
                setCaterStyle(holder, model);
            } else if (model.detailInfoModel.type.equals("hotel")) {
                setHotelStyle(holder, model);
            } else if (model.detailInfoModel.type.equals("life")) {
                setLiftStyle(holder, model);
            } else {
                setLiftStyle(holder, model);
            }
            holder.getRatingBar().setVisibility(0);
            Float rating = Float.valueOf(0.0f);
            try {
                rating = Float.valueOf(Float.parseFloat(model.detailInfoModel.overallRating));
            } catch (Exception e) {
            }
            holder.getRatingBar().setRating(rating.floatValue());
            return;
        }
        holder.getDistanceText().setVisibility(8);
        holder.getTagText().setVisibility(8);
        holder.getPriceLeft().setVisibility(8);
        holder.getPriceRight().setVisibility(8);
        holder.getRatingBar().setVisibility(8);
    }

    private float get18Dimens() {
        return 18.0f;
    }

    private float get14Dimens() {
        return 14.0f;
    }

    private void setHotelStyle(AroundListHolder holder, PlacePoiInfoModel model) {
        String price = model.detailInfoModel.price;
        if (MCStringUtil.isEmpty(price)) {
            price = "0";
            holder.getPriceLeft().setVisibility(8);
            holder.getPriceRight().setVisibility(8);
        } else {
            holder.getPriceLeft().setVisibility(0);
            holder.getPriceRight().setVisibility(0);
        }
        holder.getPriceLeft().setTextSize(get18Dimens());
        holder.getPriceLeft().setTextColor(this.resource.getColor("mc_forum_peripheral_information_color"));
        holder.getPriceLeft().setText(this.resource.getString("mc_place_rmb_icon") + price);
        holder.getPriceRight().setTextSize(get14Dimens());
        holder.getPriceRight().setTextColor(this.resource.getColor("mc_forum_text4_desc_normal_color"));
        holder.getPriceRight().setText(this.resource.getString("mc_place_above"));
    }

    private void setCaterStyle(AroundListHolder holder, PlacePoiInfoModel model) {
        String price = model.detailInfoModel.price;
        if (MCStringUtil.isEmpty(price)) {
            price = "0";
            holder.getPriceLeft().setVisibility(8);
            holder.getPriceRight().setVisibility(8);
        } else {
            holder.getPriceLeft().setVisibility(0);
            holder.getPriceRight().setVisibility(0);
        }
        holder.getPriceLeft().setTextSize(get14Dimens());
        holder.getPriceLeft().setTextColor(this.resource.getColor("mc_forum_text4_desc_normal_color"));
        holder.getPriceLeft().setText(this.resource.getString("mc_place_every_consumption"));
        holder.getPriceRight().setTextSize(get14Dimens());
        holder.getPriceRight().setTextColor(this.resource.getColor("mc_forum_peripheral_information_color"));
        holder.getPriceRight().setText(this.resource.getString("mc_place_rmb_icon") + price);
    }

    private void setLiftStyle(AroundListHolder holder, PlacePoiInfoModel model) {
        String price = model.detailInfoModel.price;
        if (MCStringUtil.isEmpty(price)) {
            price = "0";
            holder.getPriceLeft().setVisibility(8);
            holder.getPriceRight().setVisibility(8);
        } else {
            holder.getPriceLeft().setVisibility(0);
            holder.getPriceRight().setVisibility(0);
        }
        holder.getPriceLeft().setTextSize(get14Dimens());
        holder.getPriceLeft().setTextColor(this.resource.getColor("mc_forum_text4_desc_normal_color"));
        holder.getPriceLeft().setText(this.resource.getString("mc_place_reference_price"));
        holder.getPriceRight().setTextSize(get14Dimens());
        holder.getPriceRight().setTextColor(this.resource.getColor("mc_forum_peripheral_information_color"));
        holder.getPriceRight().setText(this.resource.getString("mc_place_rmb_icon") + price);
    }
}
