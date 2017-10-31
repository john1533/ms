package com.mobcent.discuz.module.publish.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.discuz.base.activity.BaseFragmentActivity;
import com.mobcent.discuz.base.adapter.BaseSimpleAdapter;
import com.mobcent.discuz.module.publish.delegate.PoiItemDelegate;
import com.mobcent.discuz.module.publish.delegate.PoiItemDelegate.ClickPoiItemLisenter;
import java.util.ArrayList;
import java.util.List;

public class PoiFragmentAcitvity extends BaseFragmentActivity {
    public static final String POI_LIST = "poiList";
    private Button backBtn;
    private TextView noPoiText;
    private PoiAdapter poiAdapter;
    private List<String> poiList;
    private ListView poiListView;

    public class PoiAdapter extends BaseSimpleAdapter<String> {

        private class PoiAdapterHolder {
            private TextView textView;

            private PoiAdapterHolder() {
            }

            public TextView getTextView() {
                return this.textView;
            }

            public void setTextView(TextView textView) {
                this.textView = textView;
            }
        }

        public PoiAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        public int getCount() {
            return this.datas.size();
        }

        public Object getItem(int position) {
            return this.datas.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PoiAdapterHolder holder;
            if (convertView == null) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("mention_friend_item"), null);
                holder = new PoiAdapterHolder();
                holder.setTextView((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_mention_friend_name_text")));
                convertView.setTag(holder);
            } else {
                holder = (PoiAdapterHolder) convertView.getTag();
            }
            holder.getTextView().setText((String) getItem(position));
            return convertView;
        }
    }

    protected String getLayoutName() {
        return "poi_fragment_acitvity";
    }

    protected void initDatas() {
        super.initDatas();
        this.poiList = new ArrayList();
        Intent intent = getIntent();
        if (intent != null) {
            this.poiList = (List) intent.getSerializableExtra(POI_LIST);
        }
    }

    protected boolean isContainTopBar() {
        return false;
    }

    protected void initViews() {
        this.backBtn = (Button) findViewByName("back_btn");
        this.poiListView = (ListView) findViewByName("poi_list");
        this.noPoiText = (TextView) findViewByName("no_poi_text");
    }

    protected void initActions() {
        if (this.poiList == null || this.poiList.isEmpty()) {
            this.noPoiText.setVisibility(0);
            this.poiListView.setVisibility(8);
        } else {
            this.noPoiText.setVisibility(8);
            this.poiListView.setVisibility(0);
            this.poiAdapter = new PoiAdapter(this, this.poiList);
            this.poiListView.setAdapter(this.poiAdapter);
        }
        this.backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PoiFragmentAcitvity.this.finish();
            }
        });
        this.poiListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ClickPoiItemLisenter lisenter = PoiItemDelegate.getInstance().getClickPoiItemLisenter();
                if (lisenter != null) {
                    lisenter.clickItem((String) PoiFragmentAcitvity.this.poiList.get(position));
                }
                PoiFragmentAcitvity.this.finish();
            }
        });
    }
}
