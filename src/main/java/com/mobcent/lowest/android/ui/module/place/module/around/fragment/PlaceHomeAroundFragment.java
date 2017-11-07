package com.mobcent.lowest.android.ui.module.place.module.around.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.mobcent.lowest.android.ui.module.place.fragment.BasePlaceFragment;
import com.mobcent.lowest.android.ui.module.place.module.around.activity.AroundListActivity1;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.module.place.delegate.PlacePoiSearchDelegate;
import com.mobcent.lowest.module.place.helper.PlacePoiRequestHelper;
import com.mobcent.lowest.module.place.model.PlacePoiResult;
import com.mobcent.lowest.module.place.model.PlaceQueryModel;
import java.util.ArrayList;
import java.util.List;

public class PlaceHomeAroundFragment extends BasePlaceFragment {
    public String TAG = "PlaceHomeAroundFragment";
    private int columnCount = 3;
    private int columnWidth = 0;
    private RelativeLayout iconContainerBox;
    private String[] iconDrawables;
    private String[] iconItems = null;
    private String[] iconTypes;
    private PlacePoiRequestHelper mSearch;
    private int margin = 10;
    private int marginLeft = 0;
    private int marginTop = 0;
    private Button moreBtn;
    private List<PlaceQueryModel> queryModels = null;
    private Button searchBtn;
    private EditText searchEdit;

    protected void initData() {
        super.initData();
        this.queryModels = new ArrayList();
        this.iconItems = this.context.getResources().getStringArray(this.resource.getArrayId("mc_place_home_around_names"));
        this.iconTypes = this.context.getResources().getStringArray(this.resource.getArrayId("mc_place_home_around_types"));
        this.iconDrawables = this.context.getResources().getStringArray(this.resource.getArrayId("mc_place_home_around_icons"));
        this.margin = MCPhoneUtil.dip2px(this.context, (float) this.margin);
        this.columnWidth = (MCPhoneUtil.getDisplayWidth(this.context) - ((this.columnCount + 1) * this.margin)) / this.columnCount;
        this.mSearch = new PlacePoiRequestHelper(this.context);
        this.mSearch.registerPoiSearchDelegate(new PlacePoiSearchDelegate() {
            public void onPlacePoiResult(PlacePoiResult result) {
                if (result.getStatus() == 0) {
                    TextView tv = new TextView(PlaceHomeAroundFragment.this.context);
                    tv.setText(result.toString());
                    tv.setBackgroundColor(-1);
                    tv.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                    tv.setMovementMethod(new ScrollingMovementMethod());
                    PlaceHomeAroundFragment.this.iconContainerBox.addView(tv, new LayoutParams(-1, -1));
                    PlaceHomeAroundFragment.this.startActivity(new Intent(PlaceHomeAroundFragment.this.context, AroundListActivity1.class));
                    return;
                }
                Toast.makeText(PlaceHomeAroundFragment.this.context, "query failed", 1000).show();
            }
        });
    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(this.resource.getLayoutId("mc_place_activity_around_fragment"), null);
        this.iconContainerBox = (RelativeLayout) findViewByName(this.rootView, "place_home_icon_container_layout");
        this.searchEdit = (EditText) findViewByName(this.rootView, "place_home_around_search_edit");
        this.searchBtn = (Button) findViewByName(this.rootView, "place_home_around_search_btn");
        this.moreBtn = (Button) findViewByName(this.rootView, "more_btn");
        return this.rootView;
    }

    protected void initActions() {
        createIconViews();
        this.searchBtn.setOnClickListener(this.clickListener);
        this.moreBtn.setOnClickListener(this.clickListener);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void createIconViews() {
        for (int i = 0; i < this.iconItems.length; i++) {
            PlaceQueryModel model = new PlaceQueryModel();
            model.setQuery(this.iconItems[i]);
            model.setQueryType(this.iconTypes[i]);
            model.setDrawableName(this.iconDrawables[i]);
            this.queryModels.add(model);
            addItems(model, i);
        }
        this.iconContainerBox.postInvalidate();
    }

    private void addItems(PlaceQueryModel model, int position) {
        int a = position % this.columnCount;
        int b = position / this.columnCount;
        this.marginTop = ((b + 1) * this.margin) + (this.columnWidth * b);
        this.marginLeft = ((a + 1) * this.margin) + (this.columnWidth * a);
        View view = this.inflater.inflate(this.resource.getLayoutId("mc_place_activity_around_fragment_item"), null);
        LayoutParams lps = new LayoutParams(this.columnWidth, this.columnWidth);
        lps.setMargins(this.marginLeft, this.marginTop, 0, 0);
        ((TextView) findViewByName(view, "place_home_around_item_text")).setText(model.getQuery());
        ImageView img = (ImageView) findViewByName(view, "place_home_around_item_img");
        img.setOnClickListener(this.clickListener);
        img.setBackgroundResource(this.resource.getDrawableId(model.getDrawableName()));
        img.setTag(model);
        view.setLayoutParams(lps);
        this.iconContainerBox.addView(view);
    }

    protected void onViewClick(View v) {
        if (v.getTag() != null && (v.getTag() instanceof PlaceQueryModel)) {
            startAroundListActivity((PlaceQueryModel) v.getTag(), false);
        } else if (v.equals(this.searchBtn)) {
            PlaceQueryModel queryModel = new PlaceQueryModel();
            queryModel.setQuery(this.searchEdit.getText().toString());
            startAroundListActivity(queryModel, true);
        } else if (v.equals(this.moreBtn)) {

        }
    }

    private void startAroundListActivity(final PlaceQueryModel queryModel, final boolean isSearch) {

    }

    public void setRootViewBack() {
        if (this.rootView != null) {
            this.rootView.setBackgroundColor(-1);
        }
    }
}
