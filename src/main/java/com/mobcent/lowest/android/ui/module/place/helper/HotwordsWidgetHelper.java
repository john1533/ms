package com.mobcent.lowest.android.ui.module.place.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.AroundListActivity;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;
import com.mobcent.lowest.module.place.model.PlaceHotNavModel;
import com.mobcent.lowest.module.place.model.PlaceHotSubModel;
import com.mobcent.lowest.module.place.model.PlaceLocationIntentModel;
import com.mobcent.lowest.module.place.model.PlacePoiLocationModel;
import com.mobcent.lowest.module.place.model.PlaceQueryModel;
import java.util.List;

public class HotwordsWidgetHelper {
    private final int LEFT = 2;
    private final int LEFT_DOWN = 1;
    public String TAG = "HotwordsWidgetHelper";
    private final int TOP = 3;
    private int boxHeigth;
    private int boxWidth;
    private OnClickListener clickListener = new OnClickListener() {
        public void onClick(final View v) {
            if (HotwordsWidgetHelper.this.locationModel != null) {
                HotwordsWidgetHelper.this.goToAroundList(v, HotwordsWidgetHelper.this.locationModel);
            } else {
                PlaceLocationHelper.getInastance().getCurrentLocation(HotwordsWidgetHelper.this.context, false, new LocationDelegate() {
                    public void onReceiveLocation(BDLocation location) {
                        if (location == null || location.getCity() == null || location.getCityCode() == null) {
                            Toast.makeText(v.getContext(), HotwordsWidgetHelper.this.resource.getStringId("mc_place_get_location_error"), LocationClientOption.MIN_SCAN_SPAN).show();
                            return;
                        }
                        PlacePoiLocationModel locationModel = new PlacePoiLocationModel();
                        locationModel.setLat(location.getLatitude());
                        locationModel.setLng(location.getLongitude());
                        locationModel.setCity(location.getCity());
                        locationModel.setAreaCode(location.getCityCode());
                        HotwordsWidgetHelper.this.goToAroundList(v, locationModel);
                    }
                });
            }
        }
    };
    private int columnCount = 3;
    private Context context;
    private LayoutInflater inflater;
    private int itemHeight = 60;
    private int itemWidth;
    private PlacePoiLocationModel locationModel;
    private int marginLeft = 5;
    private int marginRight = 5;
    private MCResource resource;
    private int screenWidth;

    public PlacePoiLocationModel getLocationModel() {
        return this.locationModel;
    }

    public void setLocationModel(PlacePoiLocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public HotwordsWidgetHelper(Context context) {
        this.context = context;
        this.screenWidth = MCPhoneUtil.getDisplayWidth(context);
        this.marginLeft = MCPhoneUtil.dip2px(context, (float) this.marginLeft);
        this.marginRight = MCPhoneUtil.dip2px(context, (float) this.marginRight);
        this.boxWidth = (this.screenWidth - this.marginLeft) - this.marginRight;
        this.itemWidth = this.boxWidth / this.columnCount;
        this.itemHeight = MCPhoneUtil.dip2px(context, (float) this.itemHeight);
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
    }

    public View createItemBox(PlaceHotNavModel hotNavModel) {
        int i;
        List<PlaceHotSubModel> subList = hotNavModel.getSubList();
        int size = subList.size();
        boolean isDanShu = size % 2 != 0;
        int itemCount = size / 2;
        this.boxHeigth = this.itemHeight * itemCount;
        LayoutParams boxLps = new LayoutParams(this.boxWidth, this.boxHeigth);
        RelativeLayout box = new RelativeLayout(this.context);
        box.setBackgroundDrawable(this.resource.getDrawable("mc_forum_card_bg6"));
        box.setLayoutParams(boxLps);
        View categoryView = createCategoryView(hotNavModel);
        int i2 = this.itemWidth;
        if (isDanShu) {
            i = this.boxHeigth - this.itemHeight;
        } else {
            i = this.boxHeigth;
        }
        box.addView(categoryView, new RelativeLayout.LayoutParams(i2, i));
        for (int i3 = 0; i3 < size; i3++) {
            int leftMargin;
            int topMargin;
            View item;
            PlaceHotSubModel subModel = (PlaceHotSubModel) subList.get(i3);
            int currentItemIndex = i3 / 2;
            if (!isDanShu) {
                if (i3 % 2 == 0) {
                    leftMargin = this.itemWidth;
                } else {
                    leftMargin = this.itemWidth * 2;
                }
                topMargin = currentItemIndex * this.itemHeight;
                if (currentItemIndex == itemCount - 1) {
                    item = createItem(subModel, 2);
                } else {
                    item = createItem(subModel, 1);
                }
            } else if (currentItemIndex == itemCount - 1 || currentItemIndex > itemCount - 1) {
                topMargin = (itemCount - 1) * this.itemHeight;
                if (currentItemIndex != itemCount - 1) {
                    leftMargin = this.itemWidth * 2;
                    item = createItem(subModel, 2);
                } else if (i3 % 2 == 0) {
                    leftMargin = 0;
                    topMargin--;
                    item = createItem(subModel, 3);
                } else {
                    leftMargin = this.itemWidth;
                    item = createItem(subModel, 2);
                }
            } else {
                if (i3 % 2 == 0) {
                    leftMargin = this.itemWidth;
                } else {
                    leftMargin = this.itemWidth * 2;
                }
                topMargin = currentItemIndex * this.itemHeight;
                item = createItem(subModel, 1);
            }
            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(this.itemWidth, this.itemHeight);
            lps.leftMargin = leftMargin;
            lps.topMargin = topMargin;
            item.setLayoutParams(lps);
            box.addView(item);
        }
        return box;
    }

    private View createItem(PlaceHotSubModel subModel, int type) {
        TextView textView = new TextView(this.context);
        textView.setTextColor(Color.parseColor(subModel.getColor()));
        textView.setGravity(17);
        textView.setTextSize(16.0f);
        textView.setText(subModel.getName());
        textView.setTag(subModel);
        switch (type) {
            case 1:
                textView.setBackgroundDrawable(this.resource.getDrawable("mc_forum_peripheral_line3"));
                break;
            case 2:
                textView.setBackgroundDrawable(this.resource.getDrawable("mc_forum_peripheral_line2"));
                break;
            case 3:
                textView.setBackgroundDrawable(this.resource.getDrawable("mc_forum_peripheral_line1"));
                break;
        }
        textView.setOnClickListener(this.clickListener);
        return textView;
    }

    private View createCategoryView(PlaceHotNavModel hotNavModel) {
        View view = this.inflater.inflate(this.resource.getLayoutId("mc_place_hot_words_category_widget"), null);
        TextView categoryText = (TextView) view.findViewById(this.resource.getViewId("category_text"));
        ImageView categoryImg = (ImageView) view.findViewById(this.resource.getViewId("category_img"));
        MCLogUtil.e(this.TAG, hotNavModel.getDrawableName());
        categoryImg.setBackgroundResource(this.resource.getDrawableId(hotNavModel.getDrawableName()));
        categoryText.setTextColor(Color.parseColor(hotNavModel.getColor()));
        categoryText.setText(hotNavModel.getName());
        view.setTag(hotNavModel);
        view.setOnClickListener(this.clickListener);
        return view;
    }

    private void goToAroundList(View v, PlacePoiLocationModel locationModel) {
        PlaceQueryModel queryModel = new PlaceQueryModel();
        String queryName = null;
        String queryType = null;
        if (v.getTag() instanceof PlaceHotNavModel) {
            queryName = ((PlaceHotNavModel) v.getTag()).getName();
            queryType = ((PlaceHotNavModel) v.getTag()).getType();
        } else if (v.getTag() instanceof PlaceHotSubModel) {
            queryName = ((PlaceHotSubModel) v.getTag()).getName();
            queryType = ((PlaceHotSubModel) v.getTag()).getType();
        }
        queryModel.setQuery(queryName);
        queryModel.setQueryType(queryType);
        Intent intent = new Intent(this.context, AroundListActivity.class);
        PlaceLocationIntentModel model = new PlaceLocationIntentModel();
        model.setLocationModel(locationModel);
        model.setQueryModel(queryModel);
        model.setSearch(false);
        intent.putExtra("location", model);
        v.getContext().startActivity(intent);
    }
}
