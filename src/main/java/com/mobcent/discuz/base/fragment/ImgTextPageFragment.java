package com.mobcent.discuz.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.base.adapter.BaseListAdatper;
import com.mobcent.discuz.base.dispatch.ActivityDispatchHelper;
import com.mobcent.discuz.base.helper.ConfigOptHelper;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.lowest.base.utils.MCListUtils;
import java.util.ArrayList;
import java.util.List;

public class ImgTextPageFragment extends BaseModuleFragment {
    private ListAdapter adapter;
    private List<ConfigComponentModel> componentModels;
    private ListView list;

    class Holder {
        public TextView descText;
        public ImageView img;
        public TextView titleText;

        Holder() {
        }
    }

    class ListAdapter extends BaseListAdatper<ConfigComponentModel, Holder> {
        public ListAdapter(Context context, List<ConfigComponentModel> datas) {
            super(context, datas);
        }

        protected void initViews(View convertView, Holder holder) {
            holder.titleText = (TextView) findViewByName(convertView, "title_text");
            holder.descText = (TextView) findViewByName(convertView, "desc_text");
            holder.img = (ImageView) findViewByName(convertView, "pre_img");
        }

        protected void initViewDatas(Holder holder, ConfigComponentModel data, int position) {
            if (TextUtils.isEmpty(data.getTitle())) {
                holder.titleText.setVisibility(8);
            } else {
                holder.titleText.setVisibility(0);
                holder.titleText.setText(data.getTitle());
            }
            if (TextUtils.isEmpty(data.getDesc())) {
                holder.descText.setVisibility(8);
            } else {
                holder.descText.setVisibility(0);
                holder.descText.setText(data.getDesc());
            }
            if (TextUtils.isEmpty(data.getIcon())) {
                holder.img.setBackgroundColor(-1);
            } else {
                holder.img.setBackgroundDrawable(null);
            }
            loadImage(holder.img, data.getIcon());
        }

        protected String getLayoutName() {
            return "base_img_text_fragment_item";
        }

        protected Holder instanceHolder() {
            return new Holder();
        }
    }

    protected String getRootLayoutName() {
        return "base_img_text_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.componentModels = new ArrayList();
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        this.list = (ListView) findViewByName(rootView, "pull_refresh_list");
        this.adapter = new ListAdapter(this.activity, this.componentModels);
        this.list.setAdapter(this.adapter);
    }

    protected void initActions(View rootView) {
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (!ConfigOptHelper.isNeedLogin(((ConfigComponentModel) ImgTextPageFragment.this.componentModels.get(position)).getType()) || LoginHelper.doInterceptor(ImgTextPageFragment.this.getActivity(), null, null)) {
                    ActivityDispatchHelper.dispatchActivity(ImgTextPageFragment.this.activity, (ConfigComponentModel) ImgTextPageFragment.this.componentModels.get(position));
                }
            }
        });
    }

    protected void firstCreate() {
        super.firstCreate();
        new Thread() {
            public void run() {
                ImgTextPageFragment.this.setData();
            }
        }.start();
    }

    private void setData() {
        if (this.moduleModel != null && !MCListUtils.isEmpty(this.moduleModel.getComponentList())) {
            this.componentModels.addAll(this.moduleModel.getComponentList());
            this.mHandler.post(new Runnable() {
                public void run() {
                    ImgTextPageFragment.this.adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
