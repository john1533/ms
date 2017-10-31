package com.mobcent.lowest.android.ui.module.plaza.fragment.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.holder.SearchListFragmentHolder;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.plaza.config.PlazaConfig;
import com.mobcent.lowest.module.plaza.model.SearchModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class PlazaSearchListFragmentAdapter extends BaseAdapter {
    public String TAG;
    private Context context;
    private Handler handler;
    private LayoutInflater inflater;
    private boolean isBusy = false;
    private boolean isRight = true;
    private MCResource mcResource;
    private List<SearchModel> searchList;

    public boolean isBusy() {
        return this.isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public PlazaSearchListFragmentAdapter(Context context, String TAG, List<SearchModel> searchList, Handler handler) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.TAG = TAG;
        this.searchList = searchList;
        this.handler = handler;
        this.mcResource = MCResource.getInstance(context);
    }

    public int getCount() {
        return this.searchList.size();
    }

    public Object getItem(int position) {
        return this.searchList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup arg2) {
        SearchListFragmentHolder holder;
        final SearchModel searchModel = (SearchModel) this.searchList.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.mcResource.getLayoutId("mc_plaza_search_list_fragment_item"), null);
            holder = new SearchListFragmentHolder();
            initHodlerView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (SearchListFragmentHolder) convertView.getTag();
        }
        initHodlerData(searchModel, holder, position);
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                    PlazaConfig.getInstance().getPlazaDelegate().onSearchItemClick(PlazaSearchListFragmentAdapter.this.context, searchModel);
                }
            }
        });
        return convertView;
    }

    private void initHodlerView(View view, SearchListFragmentHolder holder) {
        holder.setTitleText((TextView) view.findViewById(this.mcResource.getViewId("title_text")));
        holder.setDescribeText((TextView) view.findViewById(this.mcResource.getViewId("describe_text")));
        holder.setThumbImg((ImageView) view.findViewById(this.mcResource.getViewId("thumb_img")));
        holder.setThumbImgRight((ImageView) view.findViewById(this.mcResource.getViewId("thumb_img_right")));
    }

    private void initHodlerData(SearchModel searchModel, SearchListFragmentHolder holder, int position) {
        holder.getTitleText().setText(searchModel.getTitle());
        if (searchModel.getBaikeType() == 4) {
            holder.getDescribeText().setText(searchModel.getSinger());
            ((LayoutParams) holder.getDescribeText().getLayoutParams()).setMargins(0, 5, 0, 0);
        } else {
            holder.getDescribeText().setText(searchModel.getSummary());
            ((LayoutParams) holder.getDescribeText().getLayoutParams()).setMargins(0, 0, 0, 0);
        }
        holder.getThumbImg().setImageDrawable(null);
        holder.getThumbImgRight().setImageDrawable(null);
        if (MCStringUtil.isEmpty(searchModel.getPicPath())) {
            holder.getThumbImg().setVisibility(8);
            holder.getThumbImgRight().setVisibility(8);
            return;
        }
        if (this.isRight) {
            holder.getThumbImgRight().setVisibility(0);
            holder.getThumbImg().setVisibility(8);
        } else {
            holder.getThumbImgRight().setVisibility(8);
            holder.getThumbImg().setVisibility(0);
        }
        String realUrl = MCAsyncTaskLoaderImage.formatUrl(searchModel.getBaseUrl() + searchModel.getPicPath(), searchModel.getBaikeType() == 16 ? PlazaConstant.RESOLUTION_200X200 : "100x100");
        holder.getThumbImgRight().setTag(realUrl);
        holder.getThumbImg().setTag(realUrl);
        if (!this.isBusy) {
            ImageView thumbImgRight;
            if (this.isRight) {
                thumbImgRight = holder.getThumbImgRight();
            } else {
                thumbImgRight = holder.getThumbImg();
            }
            loadImageByUrl(thumbImgRight, realUrl);
        }
    }

    public void loadImageByUrl(ImageView img, String url) {
        ImageLoader.getInstance().displayImage(url, img);
    }

    public void setSearchList(List<SearchModel> searchList) {
        this.searchList = searchList;
    }
}
