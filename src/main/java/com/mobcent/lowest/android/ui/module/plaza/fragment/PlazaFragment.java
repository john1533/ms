package com.mobcent.lowest.android.ui.module.plaza.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.PlazaFragmentAdapter;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.ArrayList;
import java.util.List;

public class PlazaFragment extends Fragment {
    private String TAG = "PlazaFragment";
    private MCResource adResource;
    private GridView gird;
    private PlazaFragmentAdapter gridAdapter;
    private Handler handler = null;
    private PlazaFragmentListener listener;
    private OnItemClickListener onItemClickListener;
    private List<PlazaAppModel> plazaAppModels;

    public interface PlazaFragmentListener {
        void plazaAppImgClick(PlazaAppModel plazaAppModel);
    }

    public PlazaFragment(){
        new Fragment();
        String plaFrame = "PlazaFragment";
        TAG = "PlazaFragment";
        this.handler = null;
        onItemClickListener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MCLogUtil.e(PlazaFragment.this.TAG, "onItemClickListener  --- " + position);
                if (PlazaFragment.this.listener != null) {
                    PlazaFragment.this.listener.plazaAppImgClick((PlazaAppModel) PlazaFragment.this.plazaAppModels.get(position));
                }
            }
        };
    }

    public PlazaFragment(Handler handler, List<PlazaAppModel> plazaAppModels) {
        this.handler = handler;
        this.plazaAppModels = plazaAppModels;
    }

    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            this.listener = (PlazaFragmentListener) activity;
        } catch (Exception e) {
            MCLogUtil.e(this.TAG, e.toString());
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adResource = MCResource.getInstance(getActivity().getApplicationContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.adResource.getLayoutId("mc_plaza_fragment"), null);
        this.gird = (GridView) view.findViewById(this.adResource.getViewId("plaza_gird"));
        if (this.plazaAppModels == null) {
            this.plazaAppModels = new ArrayList();
        }
        this.gridAdapter = new PlazaFragmentAdapter(getActivity(), this.handler, this.plazaAppModels);
        this.gird.setAdapter(this.gridAdapter);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.gird.setOnItemClickListener(this.onItemClickListener);
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public List<PlazaAppModel> getPlazaAppModels() {
        return this.plazaAppModels;
    }

    public void setPlazaAppModels(List<PlazaAppModel> plazaAppModels) {
        this.plazaAppModels = plazaAppModels;
    }
}
