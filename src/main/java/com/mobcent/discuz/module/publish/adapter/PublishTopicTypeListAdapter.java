package com.mobcent.discuz.module.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ClassifyTypeModel;
import com.mobcent.discuz.module.publish.adapter.holder.MentionFriendAdapterHolder;
import com.mobcent.discuz.module.publish.fragment.activity.PublishTopicActivity;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.List;

public class PublishTopicTypeListAdapter extends BaseAdapter {
    private Context context;
    private MentionFriendAdapterHolder holder;
    private LayoutInflater inflater;
    private MCResource resource;
    private List<ClassifyTypeModel> typeList;

    public List<ClassifyTypeModel> getTypeList() {
        return this.typeList;
    }

    public void setTypeList(List<ClassifyTypeModel> typeList) {
        this.typeList = typeList;
    }

    public PublishTopicTypeListAdapter(Context context, List<ClassifyTypeModel> typeList) {
        this.context = context;
        this.typeList = typeList;
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context);
    }

    public int getCount() {
        return this.typeList.size();
    }

    public Object getItem(int position) {
        return this.typeList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ClassifyTypeModel model = (ClassifyTypeModel) this.typeList.get(position);
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("publish_board_item"), null);
            this.holder = new MentionFriendAdapterHolder();
            this.holder.setNameText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_board_name_text")));
            convertView.setTag(this.holder);
        } else {
            this.holder = (MentionFriendAdapterHolder) convertView.getTag();
        }
        if (this.holder == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("publish_board_item"), null);
            this.holder = new MentionFriendAdapterHolder();
            this.holder.setNameText((TextView) convertView.findViewById(this.resource.getViewId("mc_forum_board_name_text")));
            convertView.setTag(this.holder);
        }
        ImageView selectImg = (ImageView) convertView.findViewById(this.resource.getViewId("mc_forum_select_board_image"));
        this.holder.getNameText().setText(model.getName());
        if (model.isSelect()) {
            selectImg.setVisibility(0);
        } else {
            selectImg.setVisibility(8);
        }
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int i = 0;
                while (i < PublishTopicTypeListAdapter.this.typeList.size()) {
                    if (((ClassifyTypeModel) PublishTopicTypeListAdapter.this.typeList.get(i)).getId() != model.getId() || ((ClassifyTypeModel) PublishTopicTypeListAdapter.this.typeList.get(i)).isSelect()) {
                        ((ClassifyTypeModel) PublishTopicTypeListAdapter.this.typeList.get(i)).setSelect(false);
                    } else {
                        model.setSelect(true);
                    }
                    i++;
                }
                PublishTopicTypeListAdapter.this.notifyDataSetInvalidated();
                PublishTopicTypeListAdapter.this.notifyDataSetChanged();
                ((PublishTopicActivity) PublishTopicTypeListAdapter.this.context).updateTypeView(model);
            }
        });
        return convertView;
    }
}
