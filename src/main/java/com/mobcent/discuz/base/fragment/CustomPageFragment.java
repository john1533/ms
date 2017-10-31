package com.mobcent.discuz.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.task.BaseRequestCallback;
import com.mobcent.discuz.base.task.ModuleTask;
import com.mobcent.discuz.module.custom.widget.layout.CustomLayoutOutSide;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView;
import com.mobcent.lowest.android.ui.widget.PullToRefreshListView.OnRefreshListener;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPageFragment extends BaseModuleFragment {
    private ListAdapter adapter;
    private List<ConfigComponentModel> componentModels;
    private PullToRefreshListView list;

    class Holder {
        public CustomLayoutOutSide outSideBox;

        Holder() {
        }
    }

    class ListAdapter extends BaseAdapter {
        private Map<Integer, Integer> itemsHeight = new HashMap();

        public int getCount() {
            return CustomPageFragment.this.componentModels.size();
        }

        public ConfigComponentModel getItem(int position) {
            return (ConfigComponentModel) CustomPageFragment.this.componentModels.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            boolean isAnim;
            if (convertView == null) {
                convertView = CustomPageFragment.this.inflater.inflate(CustomPageFragment.this.resource.getLayoutId("base_custom_fragment_item"), null);
                holder = new Holder();
                holder.outSideBox = (CustomLayoutOutSide) convertView.findViewById(CustomPageFragment.this.resource.getViewId("out_side_layout"));
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            ConfigComponentModel component = getItem(position);
            LayoutParams lps = (LayoutParams) holder.outSideBox.getLayoutParams();
            if (this.itemsHeight.get(Integer.valueOf(position)) == null || ((Integer) this.itemsHeight.get(Integer.valueOf(position))).intValue() == 0) {
                lps.height = -2;
                isAnim = false;
            } else {
                MCLogUtil.e(CustomPageFragment.this.TAG, "set item height " + this.itemsHeight.get(Integer.valueOf(position)));
                lps.height = ((Integer) this.itemsHeight.get(Integer.valueOf(position))).intValue();
                isAnim = true;
            }
            if (position == getCount() - 1) {
                lps.bottomMargin = MCPhoneUtil.dip2px(CustomPageFragment.this.activity, 10.0f);
            } else {
                lps.bottomMargin = 0;
            }
            holder.outSideBox.setLayoutParams(lps);
            holder.outSideBox.initViews(component, isAnim);
            holder.outSideBox.measure(lps.width, lps.height);
            int heightMeasure = holder.outSideBox.getMeasuredHeight();
            MCLogUtil.e(CustomPageFragment.this.TAG, "position == " + position + "====heightMeasure===" + heightMeasure);
            if (heightMeasure > 0 && (this.itemsHeight.get(Integer.valueOf(position)) == null || ((Integer) this.itemsHeight.get(Integer.valueOf(position))).intValue() == 0)) {
                this.itemsHeight.put(Integer.valueOf(position), Integer.valueOf(heightMeasure));
            }
            return convertView;
        }

        public void notifyDataSetChanged() {
            this.itemsHeight.clear();
            super.notifyDataSetChanged();
        }
    }

    protected String getRootLayoutName() {
        return "base_custom_fragment";
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.componentModels = new ArrayList();
    }

    protected void initViews(View rootView) {
        super.initViews(rootView);
        this.list = (PullToRefreshListView) findViewByName(rootView, "pull_refresh_list");
        this.list.setAutoLoadMore(false);
        if (this.adapter == null) {
            this.adapter = new ListAdapter();
        }
        this.list.setAdapter(this.adapter);
    }

    protected void initActions(View rootView) {
        this.list.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (DiscuzApplication._instance.isPayed()) {
                    new ModuleTask(CustomPageFragment.this.getActivity(), false, CustomPageFragment.this.moduleModel.getId(), new BaseRequestCallback<BaseResultModel<ConfigModuleModel>>() {
                        public void onPreExecute() {
                        }

                        public void onPostExecute(BaseResultModel<ConfigModuleModel> result) {
                            if (CustomPageFragment.this.getActivity() != null) {
                                CustomPageFragment.this.list.onRefreshComplete();
                                if (result.getRs() == 0 || result.getData() == null || MCListUtils.isEmpty(((ConfigModuleModel) result.getData()).getComponentList())) {
                                    CustomPageFragment.this.list.onBottomRefreshComplete(4);
                                    return;
                                }
                                CustomPageFragment.this.componentModels.clear();
                                CustomPageFragment.this.componentModels.addAll(((ConfigModuleModel) result.getData()).getComponentList());
                                CustomPageFragment.this.adapter.notifyDataSetChanged();
                                CustomPageFragment.this.list.onBottomRefreshComplete(6);
                            }
                        }
                    }).execute(new Void[0]);
                    return;
                }
                CustomPageFragment.this.list.onRefreshComplete();
                CustomPageFragment.this.list.onBottomRefreshComplete(6);
            }
        });
    }

    protected void firstCreate() {
        super.firstCreate();
        if (DiscuzApplication._instance.isPayed()) {
            setData();
        }
    }

    private void setData() {
        new ModuleTask(getActivity(), true, this.moduleModel.getId(), new BaseRequestCallback<BaseResultModel<ConfigModuleModel>>() {
            public void onPreExecute() {
                CustomPageFragment.this.list.onBottomRefreshComplete(6);
            }

            public void onPostExecute(BaseResultModel<ConfigModuleModel> result) {
                if (CustomPageFragment.this.getActivity() != null) {
                    if (!(result.getRs() == 0 || result.getData() == null || MCListUtils.isEmpty(((ConfigModuleModel) result.getData()).getComponentList()))) {
                        CustomPageFragment.this.componentModels.clear();
                        CustomPageFragment.this.componentModels.addAll(((ConfigModuleModel) result.getData()).getComponentList());
                        CustomPageFragment.this.adapter.notifyDataSetChanged();
                    }
                    CustomPageFragment.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            CustomPageFragment.this.list.onRefresh(true);
                        }
                    }, 500);
                }
            }
        }).execute(new Void[0]);
    }
}
