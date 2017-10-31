package com.mobcent.discuz.module.person.activity.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlatformLoginAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private PlatformLoginListener listener;
    private List<String> platformIcons = new ArrayList();
    private Map<Long, String> platformInfos;
    private List<Long> platforumIds = new ArrayList();
    private MCResource resource;

    public class PlatformLoginAdapterHolder {
        private ImageView platformImg;

        public ImageView getPlatformImg() {
            return this.platformImg;
        }

        public void setPlatformImg(ImageView platformImg) {
            this.platformImg = platformImg;
        }
    }

    public interface PlatformLoginListener {
        void onPlatformLoginClick(long j);
    }

    public PlatformLoginAdapter(Context context, Map<Long, String> platformInfos, PlatformLoginListener listener) {
        this.context = context;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context);
        this.platformInfos = platformInfos;
        getKeyandValues();
    }

    private void getKeyandValues() {
        if (this.platformInfos != null && !this.platformInfos.isEmpty()) {
            for (Long key : this.platformInfos.keySet()) {
                this.platforumIds.add(key);
                this.platformIcons.add((String) this.platformInfos.get(key));
            }
        }
    }

    public void setPlatformInfos(Map<Long, String> platformInfos) {
        this.platformInfos = platformInfos;
    }

    public int getCount() {
        return this.platformInfos.size();
    }

    public Object getItem(int position) {
        return this.platformInfos.get(Integer.valueOf(position));
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PlatformLoginAdapterHolder platformHolder;
        final long platformId = ((Long) this.platforumIds.get(position)).longValue();
        String platformIcon = (String) this.platformIcons.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("user_login_grid_item"), null);
            platformHolder = new PlatformLoginAdapterHolder();
            platformHolder.setPlatformImg((ImageView) convertView.findViewById(this.resource.getViewId("login_platform_img")));
            convertView.setTag(platformHolder);
        } else {
            platformHolder = (PlatformLoginAdapterHolder) convertView.getTag();
        }
        platformHolder.getPlatformImg().setBackgroundResource(this.resource.getDrawableId(platformIcon));
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlatformLoginAdapter.this.listener != null) {
                    PlatformLoginAdapter.this.listener.onPlatformLoginClick(platformId);
                }
            }
        });
        return convertView;
    }
}
