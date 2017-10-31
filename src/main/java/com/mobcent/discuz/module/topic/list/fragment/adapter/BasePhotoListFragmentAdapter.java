package com.mobcent.discuz.module.topic.list.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.TopicModel;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.discuz.module.topic.list.fragment.adapter.holder.TopicListFragmentAdapterHolder;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCResource;

public class BasePhotoListFragmentAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected MCResource resource;

    public BasePhotoListFragmentAdapter(Context context, ConfigComponentModel componentModel) {
        this.inflater = LayoutInflater.from(context.getApplicationContext());
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.context = context;
    }

    public View createView(BaseFallWallModel tag, TopicModel model) {
        return null;
    }

    public void setData(BaseFallWallModel flowTag, View view, boolean isVisibile, TopicModel model) {
    }

    protected void setOnClickListener(View convertView, final TopicModel topicModel) {
        TopicListFragmentAdapterHolder holder = null;
        if (convertView != null) {
            holder = (TopicListFragmentAdapterHolder) convertView.getTag();
        }
        if (holder != null) {
            holder.getHeadImageView().setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    if (LoginHelper.doInterceptor(BasePhotoListFragmentAdapter.this.context.getApplicationContext(), UserHomeActivity.class, null)) {
                        Intent intent = new Intent(BasePhotoListFragmentAdapter.this.context, UserHomeActivity.class);
                        intent.putExtra("userId", topicModel.getUserId());
                        BasePhotoListFragmentAdapter.this.context.startActivity(intent);
                    }
                }
            });
        }
    }

    protected void setTextViewData(TextView textView, long num) {
        if (num > 99) {
            textView.setText("99+");
        } else {
            textView.setText(num + "");
        }
    }
}
