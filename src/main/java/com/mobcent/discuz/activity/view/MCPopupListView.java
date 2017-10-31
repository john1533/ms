package com.mobcent.discuz.activity.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MCPopupListView extends RelativeLayout {
    private PopupListAdapter adapter;
    private String backgroundResName = "mc_forum_personal_publish_bg";
    private Context context;
    private int drawableHeight = 0;
    private int drawableWidth = 0;
    private LayoutInflater inflater;
    private int itemHeight = 60;
    private ListView listView;
    private PopupClickListener popupClickListener;
    private List<PopupModel> popupList = new ArrayList();
    private MCResource resource;
    private View view;

    public interface PopupClickListener {
        void click(TextView textView, ImageView imageView, PopupModel popupModel, int i);

        void hideView();

        void initTextView(TextView textView);
    }

    private class PopupListAdapter extends BaseAdapter {
        private List<PopupModel> list;

        public void setList(List<PopupModel> list) {
            this.list = list;
        }

        public PopupListAdapter(Context context, int drawableHeight, int drawableWidth, int itemHeight, List<PopupModel> list) {
            this.list = list;
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int position) {
            return this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            PopupListAdapterHolder holder;
            final PopupModel popupModel = (PopupModel) this.list.get(position);
            if (convertView == null) {
                convertView = MCPopupListView.this.inflater.inflate(MCPopupListView.this.resource.getLayoutId("popup_list_item"), null);
                holder = new PopupListAdapterHolder();
                init(convertView, holder);
                convertView.setTag(holder);
            } else {
                holder = (PopupListAdapterHolder) convertView.getTag();
            }
            final TextView textView = holder.getTextView();
            final ImageView imageView = holder.getImageView();
            LayoutParams params2;
            if (MCStringUtil.isEmpty(popupModel.getDrawableId()) || MCPopupListView.this.drawableHeight == 0 || MCPopupListView.this.drawableWidth == 0) {
                holder.getImageView().setImageDrawable(null);
                holder.getImageView().setVisibility(8);
                params2 = new LayoutParams(-1, MCPopupListView.this.dip2px(MCPopupListView.this.itemHeight));
                params2.addRule(15);
                params2.addRule(11);
                textView.setGravity(17);
                textView.setLayoutParams(params2);
            } else {
                LayoutParams params1 = new LayoutParams(MCPopupListView.this.dip2px(MCPopupListView.this.drawableHeight), MCPopupListView.this.dip2px(MCPopupListView.this.drawableWidth));
                params1.addRule(9);
                params1.addRule(15);
                imageView.setLayoutParams(params1);
                imageView.setImageDrawable(MCPopupListView.this.resource.getDrawable(popupModel.getDrawableId()));
                params2 = new LayoutParams(-2, MCPopupListView.this.dip2px(MCPopupListView.this.itemHeight));
                params2.addRule(15);
                params2.addRule(11);
                textView.setLayoutParams(params2);
            }
            if (popupModel.getName().length() > 6) {
                textView.setText(popupModel.getName().substring(0, 5));
            } else {
                textView.setText(popupModel.getName());
            }
            MCPopupListView.this.popupClickListener.initTextView(textView);
            final RelativeLayout layout = holder.getLayout();
            final int i = position;
            layout.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (MCStringUtil.isEmpty(popupModel.getDrawableId())) {
                        MCPopupListView.this.popupClickListener.click(textView, null, popupModel, i);
                    } else {
                        MCPopupListView.this.popupClickListener.click(textView, imageView, popupModel, i);
                    }
                }
            });
            holder.getImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    layout.performClick();
                }
            });
            holder.getTextView().setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    layout.performClick();
                }
            });
            return convertView;
        }

        private void init(View convertView, PopupListAdapterHolder holder) {
            holder.setLayout((RelativeLayout) convertView.findViewById(MCPopupListView.this.resource.getViewId("popup_layout")));
            holder.setImageView((ImageView) convertView.findViewById(MCPopupListView.this.resource.getViewId("popup_img")));
            holder.setTextView((TextView) convertView.findViewById(MCPopupListView.this.resource.getViewId("popup_text")));
        }
    }

    private class PopupListAdapterHolder {
        private ImageView imageView;
        private RelativeLayout layout;
        private TextView textView;

        private PopupListAdapterHolder() {
        }

        public RelativeLayout getLayout() {
            return this.layout;
        }

        public void setLayout(RelativeLayout layout) {
            this.layout = layout;
        }

        public ImageView getImageView() {
            return this.imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTextView() {
            return this.textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    public static class PopupModel implements Serializable {
        private static final long serialVersionUID = -2768340503827092725L;
        private String action;
        private String drawableResName;
        private int id;
        private String name;
        private int type;

        public String getDrawableId() {
            return this.drawableResName;
        }

        public void setDrawableId(String drawableResName) {
            this.drawableResName = drawableResName;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAction() {
            return this.action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

    public void setResource(String backgroundResName, int itemHeight) {
        this.backgroundResName = backgroundResName;
        this.itemHeight = itemHeight;
    }

    public void setResource(String backgroundResName, int drawableHeight, int drawableWidth, int itemHeight) {
        this.backgroundResName = backgroundResName;
        this.drawableHeight = drawableHeight;
        this.drawableWidth = drawableHeight;
        this.itemHeight = itemHeight;
    }

    public void setPopupListViewLayoutParams(LayoutParams params) {
        if (params != null) {
            this.listView.setLayoutParams(params);
        }
    }

    public MCPopupListView(Context context) {
        super(context);
        initData(context);
        initViews();
        initWidgetActions();
    }

    public MCPopupListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
        initViews();
        initWidgetActions();
    }

    private void initData(Context context) {
        this.context = context;
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.inflater = LayoutInflater.from(context.getApplicationContext());
    }

    private void initViews() {
        this.view = this.inflater.inflate(this.resource.getLayoutId("popup_list_view"), this, true);
        this.listView = (ListView) this.view.findViewById(this.resource.getViewId("popup_list_view"));
    }

    private void initWidgetActions() {
        this.view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (MCPopupListView.this.view != null && MCPopupListView.this.view.getVisibility() == 0) {
                    MCPopupListView.this.view.setVisibility(8);
                    if (MCPopupListView.this.popupClickListener != null) {
                        MCPopupListView.this.popupClickListener.hideView();
                    }
                }
                return false;
            }
        });
        this.adapter = new PopupListAdapter(this.context, this.drawableHeight, this.drawableWidth, this.itemHeight, this.popupList);
        this.listView.setAdapter(this.adapter);
        this.listView.setBackgroundResource(this.resource.getDrawableId(this.backgroundResName));
    }

    public void init(List<PopupModel> list, PopupClickListener popupClickListener) {
        this.popupClickListener = popupClickListener;
        if (MCListUtils.isEmpty((List) list)) {
            this.view.setVisibility(8);
        } else {
            setPopupList(list);
        }
        if (!MCStringUtil.isEmpty(this.backgroundResName)) {
            this.listView.setBackgroundResource(this.resource.getDrawableId(this.backgroundResName));
        }
    }

    public void setPopupList(List<PopupModel> popupList) {
        this.popupList = popupList;
        if (this.adapter != null) {
            this.adapter.setList(popupList);
            this.adapter.notifyDataSetChanged();
        }
        if (popupList == null || popupList.isEmpty()) {
            this.view.setVisibility(8);
        }
    }

    private int dip2px(int dip) {
        return getRawSize(this.context, 1, (float) dip);
    }

    private int getRawSize(Context context, int unit, float size) {
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return (int) TypedValue.applyDimension(unit, size, resources.getDisplayMetrics());
    }

    public boolean onKeyDown() {
        if (this.view == null || this.view.getVisibility() != 0) {
            return true;
        }
        this.view.setVisibility(8);
        return false;
    }
}
