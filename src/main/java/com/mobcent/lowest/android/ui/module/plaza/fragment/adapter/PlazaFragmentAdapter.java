package com.mobcent.lowest.android.ui.module.plaza.fragment.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.holder.PlazaFragmentHolder;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class PlazaFragmentAdapter extends BaseAdapter {
    public String TAG = "PlazaFragmentAdapter";
    private MCResource adResource;
    private Context context;
    private Handler handler = new Handler();
    private LayoutInflater inflater;
    private List<PlazaAppModel> plazaAppModelList;

    public PlazaFragmentAdapter(Context context, Handler handler, List<PlazaAppModel> plazaAppModelList) {
        this.handler = handler;
        this.plazaAppModelList = plazaAppModelList;
        this.inflater = LayoutInflater.from(context);
        this.adResource = MCResource.getInstance(context);
        this.context = context;
    }

    public int getCount() {
        return this.plazaAppModelList.size();
    }

    public Object getItem(int position) {
        return this.plazaAppModelList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        PlazaFragmentHolder holder;
        PlazaAppModel plazaAppModel = (PlazaAppModel) this.plazaAppModelList.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.adResource.getLayoutId("mc_plaza_fragment_item"), null);
            holder = new PlazaFragmentHolder();
            initGridView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (PlazaFragmentHolder) convertView.getTag();
        }
        initGridViewData(holder, plazaAppModel);
        return convertView;
    }

    private void initGridView(View convertView, PlazaFragmentHolder holder) {
        holder.setAppImg((ImageView) convertView.findViewById(this.adResource.getViewId("plaza_gird_item_img")));
        holder.setAppText((TextView) convertView.findViewById(this.adResource.getViewId("plaza_grid_item_text")));
    }

    private void initGridViewData(PlazaFragmentHolder holder, PlazaAppModel plazaAppModel) {
        holder.getAppText().setText(plazaAppModel.getModelName());
        loadImgByUrl(holder.getAppImg(), plazaAppModel);
    }

    private void loadImgByUrl(ImageView img, PlazaAppModel plazaAppModel) {
        img.setImageBitmap(null);
        if (!TextUtils.isEmpty(plazaAppModel.getModelDrawable())) {
            if (plazaAppModel.getModelDrawable().startsWith("http")) {
                ImageLoader.getInstance().displayImage(plazaAppModel.getModelDrawable(), img);
            } else {
                img.setImageResource(this.adResource.getDrawableId(plazaAppModel.getModelDrawable()));
            }
        }
    }
}
