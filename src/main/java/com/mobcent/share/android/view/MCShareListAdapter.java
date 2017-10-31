package com.mobcent.share.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.util.ArrayList;
import java.util.List;

public class MCShareListAdapter extends BaseAdapter {
    private final String TAG = "MCShareListAdapter";
    private List<MCShareSiteModel> contentList;
    private Context context;
    private LayoutInflater inflater;
    private ShareItemClickListener listener;
    private int minCount = 1;
    private MCResource resource;

    public interface ShareItemClickListener {
        void itemClick(MCShareSiteModel mCShareSiteModel);
    }

    public MCShareListAdapter(Context context, List<MCShareSiteModel> contentList, ShareItemClickListener listener) {
        this.context = context;
        this.contentList = contentList;
        this.resource = MCResource.getInstance(context);
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public int getCount() {
        return getRealCount();
    }

    private int getRealCount() {
        int size = this.contentList.size();
        if (size / 4 < this.minCount) {
            return this.minCount;
        }
        return size % 4 == 0 ? size / 4 : (size / 4) + 1;
    }

    public Object getItem(int position) {
        return this.contentList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MCShareListHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mc_share_dialog_item"), null);
            holder = new MCShareListHolder();
            initViews(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (MCShareListHolder) convertView.getTag();
        }
        initViewsData(holder, getShareModel(position));
        convertView.setTag(holder);
        return convertView;
    }

    private void initViews(View convertView, MCShareListHolder holder) {
        holder.setContentBox1((RelativeLayout) convertView.findViewById(this.resource.getViewId("content_layout1")));
        holder.setImg1((ImageView) convertView.findViewById(this.resource.getViewId("share_gird_item_img1")));
        holder.setText1((TextView) convertView.findViewById(this.resource.getViewId("share_grid_item_text1")));
        holder.setContentBox2((RelativeLayout) convertView.findViewById(this.resource.getViewId("content_layout2")));
        holder.setImg2((ImageView) convertView.findViewById(this.resource.getViewId("share_gird_item_img2")));
        holder.setText2((TextView) convertView.findViewById(this.resource.getViewId("share_grid_item_text2")));
        holder.setContentBox3((RelativeLayout) convertView.findViewById(this.resource.getViewId("content_layout3")));
        holder.setImg3((ImageView) convertView.findViewById(this.resource.getViewId("share_gird_item_img3")));
        holder.setText3((TextView) convertView.findViewById(this.resource.getViewId("share_grid_item_text3")));
        holder.setContentBox4((RelativeLayout) convertView.findViewById(this.resource.getViewId("content_layout4")));
        holder.setImg4((ImageView) convertView.findViewById(this.resource.getViewId("share_gird_item_img4")));
        holder.setText4((TextView) convertView.findViewById(this.resource.getViewId("share_grid_item_text4")));
    }

    private void initViewsData(MCShareListHolder holder, List<MCShareSiteModel> models) {
        if (models.isEmpty()) {
            clearHolder(holder);
        } else {
            loadHolderData(holder, models);
        }
    }

    private List<MCShareSiteModel> getShareModel(int position) {
        int start = position * 4;
        int end = (position + 1) * 4;
        List<MCShareSiteModel> temp = new ArrayList();
        for (int i = start; i < end; i++) {
            try {
                temp.add((MCShareSiteModel) this.contentList.get(i));
            } catch (Exception e) {
            }
        }
        return temp;
    }

    private void clearHolder(MCShareListHolder holder) {
        if (holder != null) {
            holder.getImg1().setImageBitmap(null);
            holder.getImg2().setImageBitmap(null);
            holder.getImg3().setImageBitmap(null);
            holder.getImg4().setImageBitmap(null);
            holder.getImg1().setBackgroundDrawable(null);
            holder.getImg2().setBackgroundDrawable(null);
            holder.getImg3().setBackgroundDrawable(null);
            holder.getImg4().setBackgroundDrawable(null);
            holder.getText1().setText("");
            holder.getText2().setText("");
            holder.getText3().setText("");
            holder.getText4().setText("");
        }
    }

    private void loadHolderData(MCShareListHolder holder, List<MCShareSiteModel> models) {
        clearHolder(holder);
        for (int i = 0; i < models.size(); i++) {
            final MCShareSiteModel model = (MCShareSiteModel) models.get(i);
            if (i == 0) {
                holder.getText1().setText(model.getSiteName());
                loadImageByLocal(holder.getImg1(), model);
                holder.getContentBox1().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (MCShareListAdapter.this.listener != null) {
                            MCShareListAdapter.this.listener.itemClick(model);
                        }
                    }
                });
            } else if (i == 1) {
                holder.getText2().setText(model.getSiteName());
                loadImageByLocal(holder.getImg2(), model);
                holder.getContentBox2().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (MCShareListAdapter.this.listener != null) {
                            MCShareListAdapter.this.listener.itemClick(model);
                        }
                    }
                });
            } else if (i == 2) {
                holder.getText3().setText(model.getSiteName());
                loadImageByLocal(holder.getImg3(), model);
                holder.getContentBox3().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (MCShareListAdapter.this.listener != null) {
                            MCShareListAdapter.this.listener.itemClick(model);
                        }
                    }
                });
            } else if (i == 3) {
                holder.getText4().setText(model.getSiteName());
                loadImageByLocal(holder.getImg4(), model);
                holder.getContentBox4().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (MCShareListAdapter.this.listener != null) {
                            MCShareListAdapter.this.listener.itemClick(model);
                        }
                    }
                });
            }
        }
    }

    private void loadImageByLocal(ImageView imageView, MCShareSiteModel model) {
        if (MCStringUtil.isEmpty(model.getSiteImage())) {
            imageView.setImageDrawable(null);
        } else {
            imageView.setImageDrawable(this.resource.getDrawable(model.getSiteImage()));
        }
    }
}
