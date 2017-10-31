package com.mobcent.lowest.android.ui.module.plaza.fragment.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class PlazaFragmentNewAdapter extends BaseExpandableListAdapter {
    public String TAG = "PlazaFragmentNewAdapter";
    private Context context;
    private boolean hasHeaderView;
    private LayoutInflater inflater;
    private Handler mHandler;
    private List<List<PlazaAppModel>> plazaModuleList;
    private MCResource resource;

    private class Holder {
        public ImageView img;
        public TextView titleText;

        private Holder() {
        }
    }

    public boolean isHasHeaderView() {
        return this.hasHeaderView;
    }

    public void setHasHeaderView(boolean hasHeaderView) {
        this.hasHeaderView = hasHeaderView;
    }

    public PlazaFragmentNewAdapter(Context context, List<List<PlazaAppModel>> plazaModuleList) {
        this.context = context;
        this.plazaModuleList = plazaModuleList;
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.mHandler = new Handler();
    }

    public int getGroupCount() {
        return this.plazaModuleList.size();
    }

    public int getChildrenCount(int groupPosition) {
        return ((List) this.plazaModuleList.get(groupPosition)).size();
    }

    public List<PlazaAppModel> getGroup(int groupPosition) {
        try {
            return (List) this.plazaModuleList.get(groupPosition);
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public PlazaAppModel getChild(int groupPosition, int childPosition) {
        try {
            return (PlazaAppModel) ((List) this.plazaModuleList.get(groupPosition)).get(childPosition);
        } catch (Exception e) {
            return new PlazaAppModel();
        }
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mc_plaza_fragment_new_group_item"), null);
        }
        if (groupPosition == 0 && isHasHeaderView()) {
            convertView.findViewById(this.resource.getViewId("content_view")).setVisibility(8);
        } else {
            convertView.findViewById(this.resource.getViewId("content_view")).setVisibility(0);
        }
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Holder holder;
        PlazaAppModel appModel = getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mc_plaza_fragment_new_item"), null);
            holder = new Holder();
            holder.img = (ImageView) convertView.findViewById(this.resource.getViewId("icon_img"));
            holder.titleText = (TextView) convertView.findViewById(this.resource.getViewId("title_text"));
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (TextUtils.isEmpty(appModel.getModelDrawable())) {
            holder.img.setVisibility(8);
        } else {
            holder.img.setVisibility(0);
        }
        loadImage(holder.img, appModel.getModelDrawable());
        holder.titleText.setText(appModel.getModelName());
        return convertView;
    }

    private void loadImage(ImageView img, String imgUrl) {
        img.setImageBitmap(null);
        if (!TextUtils.isEmpty(imgUrl)) {
            img.setTag(imgUrl);
            if (imgUrl.startsWith("http")) {
                ImageLoader.getInstance().displayImage(imgUrl, img);
            } else {
                img.setImageDrawable(this.resource.getDrawable(imgUrl));
            }
        }
    }
}
