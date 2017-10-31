package com.mobcent.discuz.module.person.activity.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.List;

public class RegLoginListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> list;
    private MCResource resource;

    public class RegLoginListAdapterHolder {
        private TextView emailText;

        public TextView getEmailText() {
            return this.emailText;
        }

        public void setEmailText(TextView emailText) {
            this.emailText = emailText;
        }
    }

    public RegLoginListAdapter(Context context, List<String> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context.getApplicationContext());
        this.resource = MCResource.getInstance(context.getApplicationContext());
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return this.list;
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
        RegLoginListAdapterHolder holder;
        String emailSection = (String) this.list.get(position);
        if (convertView == null || position == 0) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("user_register_login_item"), null);
            holder = new RegLoginListAdapterHolder();
            initRegLoginListAdapter(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (RegLoginListAdapterHolder) convertView.getTag();
        }
        holder.getEmailText().setText(emailSection);
        return convertView;
    }

    private void initRegLoginListAdapter(View convertView, RegLoginListAdapterHolder holder) {
        holder.setEmailText((TextView) convertView.findViewById(this.resource.getViewId("user_email_text")));
    }
}
