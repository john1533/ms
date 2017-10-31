package com.mobcent.discuz.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListFragment extends BaseFragment {
    private ArrayAdapter<String> arrayAdapter;
    private String[] data = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "b", "c", "d", "e", "f", "g", "h", "i", "b", "c", "d", "e", "f", "g", "h", "i"};
    private ListView listView;

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }

    protected String getRootLayoutName() {
        return "base_my_fragment";
    }

    protected void initViews(View rootView) {
        this.listView = (ListView) findViewByName(rootView, "list_layout");
        this.arrayAdapter = new ArrayAdapter(getActivity(), 17367040, 16908308, this.data);
        this.listView.setAdapter(this.arrayAdapter);
    }

    protected void initActions(View rootView) {
    }
}
