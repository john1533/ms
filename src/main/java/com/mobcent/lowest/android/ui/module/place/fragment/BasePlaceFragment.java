package com.mobcent.lowest.android.ui.module.place.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mobcent.lowest.base.utils.MCResource;

public abstract class BasePlaceFragment extends Fragment {
    protected OnClickListener clickListener = new OnClickListener() {
        public void onClick(View v) {
            BasePlaceFragment.this.onViewClick(v);
        }
    };
    protected Context context;
    protected LayoutInflater inflater;
    protected MCResource resource;
    protected View rootView = null;

    protected abstract void initActions();

    protected abstract View initViews(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = initViews(inflater, container, savedInstanceState);
        initActions();
        return view;
    }

    protected void initData() {
        this.context = getActivity();
        this.resource = MCResource.getInstance(this.context);
    }

    protected View findViewByName(View container, String name) {
        return container.findViewById(this.resource.getViewId(name));
    }

    protected void onViewClick(View v) {
    }

    public void onDestroy() {
        super.onDestroy();
    }

    protected void warn(String name) {
        Toast.makeText(this.context, this.resource.getStringId(name), 1000).show();
    }
}
