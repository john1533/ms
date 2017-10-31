package com.mobcent.lowest.android.ui.module.plaza.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.lowest.android.ui.module.plaza.fragment.PlazaFragment.PlazaFragmentListener;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;

public class PlazaListAdapter extends BaseAdapter {
    public String TAG = "PlazaListAdapter";
    private Context context;
    private boolean hideSearchBox = false;
    private LayoutInflater inflater;
    private PlazaFragmentListener listener;
    private MCResource mcResource;
    private int minCount;
    private List<PlazaAppModel> plazaAppModels;

    class Holder {
        RelativeLayout contentBox1;
        RelativeLayout contentBox2;
        RelativeLayout contentBox3;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        TextView text1;
        TextView text2;
        TextView text3;
        View topLineView;

        Holder() {
        }

        public ImageView getImg1() {
            return this.img1;
        }

        public void setImg1(ImageView img1) {
            this.img1 = img1;
        }

        public ImageView getImg2() {
            return this.img2;
        }

        public void setImg2(ImageView img2) {
            this.img2 = img2;
        }

        public ImageView getImg3() {
            return this.img3;
        }

        public void setImg3(ImageView img3) {
            this.img3 = img3;
        }

        public TextView getText1() {
            return this.text1;
        }

        public void setText1(TextView text1) {
            this.text1 = text1;
        }

        public TextView getText2() {
            return this.text2;
        }

        public void setText2(TextView text2) {
            this.text2 = text2;
        }

        public TextView getText3() {
            return this.text3;
        }

        public void setText3(TextView text3) {
            this.text3 = text3;
        }

        public View getTopLineView() {
            return this.topLineView;
        }

        public void setTopLineView(View topLineView) {
            this.topLineView = topLineView;
        }

        public RelativeLayout getContentBox1() {
            return this.contentBox1;
        }

        public void setContentBox1(RelativeLayout contentBox) {
            this.contentBox1 = contentBox;
        }

        public RelativeLayout getContentBox2() {
            return this.contentBox2;
        }

        public void setContentBox2(RelativeLayout contentBox2) {
            this.contentBox2 = contentBox2;
        }

        public RelativeLayout getContentBox3() {
            return this.contentBox3;
        }

        public void setContentBox3(RelativeLayout contextBox3) {
            this.contentBox3 = contextBox3;
        }
    }

    public PlazaListAdapter(Context context, List<PlazaAppModel> plazaAppModels, PlazaFragmentListener listener, int contentHeight, boolean hideSearchBox) {
        this.context = context;
        this.plazaAppModels = plazaAppModels;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        this.mcResource = MCResource.getInstance(context);
        this.minCount = contentHeight / ((int) (context.getResources().getDimension(this.mcResource.getDimenId("mc_plaza_content_height")) + ((float) MCPhoneUtil.dip2px(context, CustomConstant.RATIO_ONE_HEIGHT))));
        this.hideSearchBox = hideSearchBox;
    }

    public int getCount() {
        return getRealCount();
    }

    public Object getItem(int position) {
        return this.plazaAppModels.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(this.mcResource.getLayoutId("mc_plaza_activity_item"), null);
            holder = new Holder();
            initViews(convertView, holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        initViewsData(holder, getPlazaAppModels(position));
        convertView.setTag(holder);
        return convertView;
    }

    private void initViews(View convertView, Holder holder) {
        holder.setImg1((ImageView) convertView.findViewById(this.mcResource.getViewId("plaza_gird_item_img1")));
        holder.setImg2((ImageView) convertView.findViewById(this.mcResource.getViewId("plaza_gird_item_img2")));
        holder.setImg3((ImageView) convertView.findViewById(this.mcResource.getViewId("plaza_gird_item_img3")));
        holder.setText1((TextView) convertView.findViewById(this.mcResource.getViewId("plaza_grid_item_text1")));
        holder.setText2((TextView) convertView.findViewById(this.mcResource.getViewId("plaza_grid_item_text2")));
        holder.setText3((TextView) convertView.findViewById(this.mcResource.getViewId("plaza_grid_item_text3")));
        holder.setTopLineView(convertView.findViewById(this.mcResource.getViewId("top_line_view")));
        holder.setContentBox1((RelativeLayout) convertView.findViewById(this.mcResource.getViewId("content_layout1")));
        holder.setContentBox2((RelativeLayout) convertView.findViewById(this.mcResource.getViewId("content_layout2")));
        holder.setContentBox3((RelativeLayout) convertView.findViewById(this.mcResource.getViewId("content_layout3")));
    }

    private void initViewsData(Holder holder, List<PlazaAppModel> plazaAppModels) {
        if (plazaAppModels.isEmpty()) {
            clearHolder(holder);
        } else {
            loadHolder(holder, plazaAppModels);
        }
    }

    private void loadHolder(final Holder holder, List<PlazaAppModel> plazaAppModels) {
        clearHolder(holder);
        int backgroundResource = this.mcResource.getDrawableId("mc_forum_squre_bg1");
        for (int i = 0; i < plazaAppModels.size(); i++) {
            final PlazaAppModel plazaAppModel = (PlazaAppModel) plazaAppModels.get(i);
            if (i == 0) {
                holder.getContentBox1().setBackgroundResource(backgroundResource);
                holder.getText1().setText(plazaAppModel.getModelName());
                holder.getContentBox1().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (PlazaListAdapter.this.listener != null) {
                            PlazaListAdapter.this.listener.plazaAppImgClick(plazaAppModel);
                        }
                    }
                });
                holder.getContentBox1().setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == 0) {
                            holder.getImg1().setSelected(true);
                        } else if (event.getAction() == 1 || event.getAction() == 3) {
                            holder.getImg1().setSelected(false);
                        }
                        return false;
                    }
                });
                loadImgByUrl(holder.getImg1(), plazaAppModel);
            } else if (i == 1) {
                holder.getContentBox2().setBackgroundResource(backgroundResource);
                holder.getText2().setText(plazaAppModel.getModelName());
                loadImgByUrl(holder.getImg2(), plazaAppModel);
                holder.getContentBox2().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (PlazaListAdapter.this.listener != null) {
                            PlazaListAdapter.this.listener.plazaAppImgClick(plazaAppModel);
                        }
                    }
                });
                holder.getContentBox2().setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == 0) {
                            holder.getImg2().setSelected(true);
                        } else if (event.getAction() == 1 || event.getAction() == 3) {
                            holder.getImg2().setSelected(false);
                        }
                        return false;
                    }
                });
            } else if (i == 2) {
                holder.getContentBox3().setBackgroundResource(backgroundResource);
                holder.getText3().setText(plazaAppModel.getModelName());
                loadImgByUrl(holder.getImg3(), plazaAppModel);
                holder.getContentBox3().setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (PlazaListAdapter.this.listener != null) {
                            PlazaListAdapter.this.listener.plazaAppImgClick(plazaAppModel);
                        }
                    }
                });
                holder.getContentBox3().setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == 0) {
                            holder.getImg3().setSelected(true);
                        } else if (event.getAction() == 1 || event.getAction() == 3) {
                            holder.getImg3().setSelected(false);
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void loadImgByUrl(ImageView img, PlazaAppModel plazaAppModel) {
        img.setImageBitmap(null);
        img.setBackgroundResource(this.mcResource.getDrawableId("mc_forum_squre_substitute"));
        if (plazaAppModel.getModelAction() == 5) {
            img.setImageResource(this.mcResource.getDrawableId(plazaAppModel.getModelDrawable()));
            img.setBackgroundDrawable(null);
        } else if (!MCStringUtil.isEmpty(plazaAppModel.getModelDrawable())) {
            img.setTag(plazaAppModel.getModelDrawable());
            ImageLoader.getInstance().displayImage(plazaAppModel.getModelDrawable(), img);
        }
    }

    private void clearHolder(Holder holder) {
        if (holder != null) {
            holder.getContentBox1().setBackgroundDrawable(null);
            holder.getContentBox2().setBackgroundDrawable(null);
            holder.getContentBox3().setBackgroundDrawable(null);
            holder.getImg1().setImageBitmap(null);
            holder.getImg2().setImageBitmap(null);
            holder.getImg3().setImageBitmap(null);
            holder.getImg1().setBackgroundDrawable(null);
            holder.getImg2().setBackgroundDrawable(null);
            holder.getImg3().setBackgroundDrawable(null);
            holder.getText1().setText("");
            holder.getText2().setText("");
            holder.getText3().setText("");
        }
    }

    private int getRealCount() {
        int size = this.plazaAppModels.size();
        if (size / 3 < this.minCount) {
            return this.minCount;
        }
        return size % 3 == 0 ? size / 3 : (size / 3) + 1;
    }

    private List<PlazaAppModel> getPlazaAppModels(int position) {
        int start = position * 3;
        int end = (position + 1) * 3;
        List<PlazaAppModel> plazaAppModelsTemp = new ArrayList();
        for (int i = start; i < end; i++) {
            try {
                plazaAppModelsTemp.add((PlazaAppModel) this.plazaAppModels.get(i));
            } catch (Exception e) {
            }
        }
        return plazaAppModelsTemp;
    }

    public boolean isScroll() {
        if (this.plazaAppModels.size() <= (this.minCount - 1) * 3) {
            return false;
        }
        return true;
    }
}
