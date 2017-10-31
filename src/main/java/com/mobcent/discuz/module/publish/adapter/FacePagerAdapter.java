package com.mobcent.discuz.module.publish.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.LinkedHashMap;
import java.util.List;

public class FacePagerAdapter extends PagerAdapter {
    private final String TAG = "FacePagerAdapter";
    private Context context;
    private List<LinkedHashMap> faceList;
    private LayoutInflater inflater;
    private OnFaceImageClickListener onFaceImageClickListener;
    private MCResource resource;

    public class FaceImageAdapter extends BaseAdapter {
        private Context context;
        private LinkedHashMap<String, Integer> hashMap;
        private LayoutInflater inflater;
        private String[] keyArray;
        private MCResource resource = MCResource.getInstance(this.context);

        public class FaceImageAdapterHolder {
            private ImageView imageView;

            public ImageView getImageView() {
                return this.imageView;
            }

            public void setImageView(ImageView imageView) {
                this.imageView = imageView;
            }
        }

        public FaceImageAdapter(Context context, LinkedHashMap<String, Integer> hashMap) {
            this.context = context;
            this.hashMap = hashMap;
            this.keyArray = (String[]) hashMap.keySet().toArray(new String[0]);
            this.inflater =  LayoutInflater.from(this.context);
        }

        public int getCount() {
            return this.hashMap.size();
        }

        public Integer getItem(int position) {
            return (Integer) this.hashMap.get(this.keyArray[position]);
        }

        public String getItemKey(int position) {
            return this.keyArray[position];
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            FaceImageAdapterHolder holder;
            if (convertView == null) {
                convertView = this.inflater.inflate(this.resource.getLayoutId("face_item"), null);
                holder = new FaceImageAdapterHolder();
                holder.setImageView((ImageView) convertView.findViewById(this.resource.getViewId("face_img")));
                convertView.setTag(holder);
            } else {
                holder = (FaceImageAdapterHolder) convertView.getTag();
            }
            final ImageView imageView = holder.getImageView();
            imageView.setImageResource(getItem(position).intValue());
            holder.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    String tag = FaceImageAdapter.this.getItemKey(position);
                    Drawable drawable = FaceImageAdapter.this.context.getResources().getDrawable(FaceImageAdapter.this.getItem(position).intValue());
                    drawable.setBounds(0, 0, (int) FaceImageAdapter.this.context.getResources().getDimension(FaceImageAdapter.this.resource.getDimenId("mc_forum_face_width")), (int) FaceImageAdapter.this.context.getResources().getDimension(FaceImageAdapter.this.resource.getDimenId("mc_forum_face_height")));
                    if (FacePagerAdapter.this.onFaceImageClickListener != null) {
                        FacePagerAdapter.this.onFaceImageClickListener.onFaceImageClick(tag, drawable);
                    }
                }
            });
            holder.getImageView().setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == 0) {
                        imageView.setBackgroundResource(FaceImageAdapter.this.resource.getDrawableId("mc_forum_expression_h"));
                    } else {
                        imageView.setBackgroundColor(0);
                    }
                    return false;
                }
            });
            return convertView;
        }
    }

    public interface OnFaceImageClickListener {
        void onFaceImageClick(String str, Drawable drawable);
    }

    public FacePagerAdapter(Context context, List<LinkedHashMap> faceList) {
        this.context = context.getApplicationContext();
        this.faceList = faceList;
        this.resource = MCResource.getInstance(this.context);
        this.inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.faceList.size();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = this.inflater.inflate(this.resource.getLayoutId("face_pager_item"), null);
        container.addView(convertView);
        ((GridView) convertView.findViewById(this.resource.getViewId("face_grid"))).setAdapter(new FaceImageAdapter(this.context, (LinkedHashMap) this.faceList.get(position)));
        return convertView;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public OnFaceImageClickListener getOnFaceImageClickListener() {
        return this.onFaceImageClickListener;
    }

    public void setOnFaceImageClickListener(OnFaceImageClickListener onFaceImageClickListener) {
        this.onFaceImageClickListener = onFaceImageClickListener;
    }
}
