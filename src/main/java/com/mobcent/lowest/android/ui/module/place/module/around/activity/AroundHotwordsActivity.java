package com.mobcent.lowest.android.ui.module.place.module.around.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.internal.view.SupportMenu;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.mobcent.lowest.android.ui.module.place.activity.BasePlaceFragmentActivity;
import com.mobcent.lowest.android.ui.module.place.helper.HotwordsWidgetHelper;
import com.mobcent.lowest.base.utils.MCLocationUtil.LocationDelegate;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.place.delegate.PlaceHotwordsListener;
import com.mobcent.lowest.module.place.helper.PlaceHotwordsHelper;
import com.mobcent.lowest.module.place.helper.PlaceLocationHelper;
import com.mobcent.lowest.module.place.model.PlaceHotNavModel;
import com.mobcent.lowest.module.place.model.PlaceLocationIntentModel;
import com.mobcent.lowest.module.place.model.PlacePoiLocationModel;
import com.mobcent.lowest.module.place.model.PlaceQueryModel;
import java.util.List;

public class AroundHotwordsActivity extends BasePlaceFragmentActivity {
    private TextView addressText;
    private Button backBtn;
    private String baseAddressSearchStr;
    private LinearLayout contentBox;
    private PlaceHotwordsHelper dataHelper = null;
    private PlacePoiLocationModel locationModel;
    private Button searchButton;
    private EditText searchEdit;
    private HotwordsWidgetHelper widgetHelper = null;

    protected void initData() {
        super.initData();
        this.widgetHelper = new HotwordsWidgetHelper(this);
        this.dataHelper = new PlaceHotwordsHelper();
        this.baseAddressSearchStr = this.resource.getString("mc_place_hotwords_search_base_text");
    }

    protected void initView() {
        setContentView(this.resource.getLayoutId("mc_place_around_hotwords_activity"));
        try {
            this.locationModel = (PlacePoiLocationModel) getIntent().getSerializableExtra("location");
            this.widgetHelper.setLocationModel(this.locationModel);
        } catch (Exception e) {
        }
        this.backBtn = (Button) findViewById("back_btn");
        this.searchButton = (Button) findViewById("search_btn");
        this.searchEdit = (EditText) findViewById("search_edit");
        this.contentBox = (LinearLayout) findViewById("content_layout");
        this.addressText = (TextView) findViewById("address_text");
        if (this.locationModel == null || MCStringUtil.isEmpty(this.locationModel.getAddress())) {
            this.addressText.setVisibility(8);
            return;
        }
        String address = this.baseAddressSearchStr.replace("{0}", this.locationModel.getAddress());
        int start = address.indexOf(this.locationModel.getAddress());
        setTextViewPart(this.context, this.addressText, address, start, start + this.locationModel.getAddress().length(), SupportMenu.CATEGORY_MASK);
    }

    protected void initActions() {
        this.backBtn.setOnClickListener(this.clickListener);
        this.searchButton.setOnClickListener(this.clickListener);
        this.dataHelper.queryHotwordsList(this.context, new PlaceHotwordsListener() {
            public void onResult(List<PlaceHotNavModel> hotNavList) {
                if (hotNavList != null) {
                    int len = hotNavList.size();
                    for (int i = 0; i < len; i++) {
                        final PlaceHotNavModel hotNavModel = (PlaceHotNavModel) hotNavList.get(i);
                        AroundHotwordsActivity.this.contentBox.getHandler().post(new Runnable() {
                            public void run() {
                                View item = AroundHotwordsActivity.this.widgetHelper.createItemBox(hotNavModel);
                                LayoutParams lps = (LayoutParams) item.getLayoutParams();
                                lps.topMargin = MCPhoneUtil.dip2px(AroundHotwordsActivity.this.context, 10.0f);
                                item.setLayoutParams(lps);
                                AroundHotwordsActivity.this.contentBox.addView(item);
                                AroundHotwordsActivity.this.contentBox.postInvalidate();
                            }
                        });
                    }
                }
            }
        });
    }

    protected void onViewClick(final View v) {
        super.onViewClick(v);
        if (v.equals(this.backBtn)) {
            onBackPressed();
        } else if (!v.equals(this.searchButton)) {
        } else {
            if (MCStringUtil.isEmpty(this.searchEdit.getText().toString())) {
                warnStr("input something");
            } else if (this.locationModel != null) {
                goToAroundList(v, this.locationModel);
            } else {
                PlaceLocationHelper.getInastance().getCurrentLocation(this.context, false, new LocationDelegate() {
                    public void onReceiveLocation(BDLocation location) {
                        if (location == null || location.getCity() == null || location.getCityCode() == null) {
                            Toast.makeText(v.getContext(), AroundHotwordsActivity.this.resource.getStringId("mc_place_get_location_error"), LocationClientOption.MIN_SCAN_SPAN).show();
                            return;
                        }
                        PlacePoiLocationModel locationModel = new PlacePoiLocationModel();
                        locationModel.setLat(location.getLatitude());
                        locationModel.setLng(location.getLongitude());
                        locationModel.setCity(location.getCity());
                        locationModel.setAreaCode(location.getCityCode());
                        AroundHotwordsActivity.this.goToAroundList(v, locationModel);
                    }
                });
            }
        }
    }

    private void goToAroundList(View v, PlacePoiLocationModel locationModel) {
        PlaceQueryModel queryModel = new PlaceQueryModel();
        queryModel.setQuery(this.searchEdit.getText().toString());
        queryModel.setQueryType("life");
        Intent intent = new Intent(this.context, AroundListActivity.class);
        PlaceLocationIntentModel model = new PlaceLocationIntentModel();
        model.setLocationModel(locationModel);
        model.setQueryModel(queryModel);
        model.setSearch(true);
        intent.putExtra("location", model);
        v.getContext().startActivity(intent);
    }

    public void setTextViewPart(Context context, TextView textView, String s, int startPostion, int endPostion, int color) {
        textView.setText(s, BufferType.SPANNABLE);
        if (endPostion > startPostion) {
            ((Spannable) textView.getText()).setSpan(new ForegroundColorSpan(color), startPostion, endPostion, 33);
        }
    }
}
