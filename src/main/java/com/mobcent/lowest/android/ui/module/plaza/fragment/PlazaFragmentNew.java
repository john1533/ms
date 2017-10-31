package com.mobcent.lowest.android.ui.module.plaza.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import com.mobcent.lowest.android.ui.module.ad.activity.AdWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.PlazaWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.IntentPlazaNewModel;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.module.plaza.fragment.adapter.PlazaFragmentNewAdapter;
import com.mobcent.lowest.android.ui.module.plaza.helper.PlazaLocalAppListHelper;
import com.mobcent.lowest.android.ui.widget.switchpager.HeaderPagerAdapter.HeaderPagerClickListener;
import com.mobcent.lowest.android.ui.widget.switchpager.HeaderPagerHelper;
import com.mobcent.lowest.android.ui.widget.switchpager.SwitchModel;
import com.mobcent.lowest.base.config.LowestConfig;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.model.LowestResultModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.module.ad.model.AdIntentModel;
import com.mobcent.lowest.module.plaza.api.constant.PlazaApiConstant;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.mobcent.lowest.module.plaza.service.PlazaService;
import com.mobcent.lowest.module.plaza.service.impl.PlazaServiceImpl;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import java.util.ArrayList;
import java.util.List;

public class PlazaFragmentNew extends BaseFragment {
    private PlazaFragmentNewAdapter adapter;
    private LowestConfig config;
    private List<PlazaAppModel> dbAppList = null;
    private ExpandableListView expandableListView;
    private IntentPlazaNewModel intentModel;
    private boolean isCreate = true;
    private PlazaLocalAppListHelper localDataHelper;
    private List<PlazaAppModel> nativeAppList = null;
    private List<PlazaAppModel> netAppList = null;
    private HeaderPagerHelper pagerHelper;
    private List<List<PlazaAppModel>> plazaMoudelList = null;
    private PlazaService plazaService;
    private List<PlazaAppModel> switchAppList = null;

    protected void initData() {
        this.config = LowestManager.getInstance().getConfig();
        this.plazaMoudelList = new ArrayList();
        this.localDataHelper = new PlazaLocalAppListHelper();
        this.plazaService = new PlazaServiceImpl(this.activity.getApplicationContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.intentModel = (IntentPlazaNewModel) bundle.getSerializable(PlazaConstant.INTENT_PLAZA_MODEL);
            if (this.intentModel != null) {
                this.switchAppList = this.intentModel.getSwitchAppList();
                this.nativeAppList = this.intentModel.getNativeAppList();
            }
        }
        this.pagerHelper = new HeaderPagerHelper(this.activity);
    }

    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.mcResource.getLayoutId("mc_plaza_fragment_new"), null);
        this.expandableListView = (ExpandableListView) view.findViewById(this.mcResource.getViewId("mc_plaza_expandlist"));
        setAdapter();
        if (this.isCreate) {
            initFirst();
            this.isCreate = false;
        }
        return view;
    }

    protected void initWidgetActions() {
        this.expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.expandGroup(groupPosition);
                return true;
            }
        });
        this.expandableListView.setOnChildClickListener(new OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                PlazaFragmentNew.this.onChildItemClick(PlazaFragmentNew.this.adapter.getChild(groupPosition, childPosition));
                return false;
            }
        });
        this.expandableListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
    }

    private void onChildItemClick(PlazaAppModel appModel) {
        if (appModel != null && !includeInterrupt(appModel)) {
            this.config.onAppItemClick(this.activity, appModel);
        }
    }

    public boolean includeInterrupt(PlazaAppModel paAppModel) {
        String url = this.plazaService.getPlazaLinkUrl(this.config.getForumKey(), paAppModel.getModelId(), this.config.getUserId());
        if (paAppModel.getModelAction() == 5) {
            return false;
        }
        Intent intent;
        if (paAppModel.getModelAction() == 3) {
            intent = new Intent(this.activity, AdWebViewActivity.class);
            AdIntentModel adIntentModel = new AdIntentModel();
            adIntentModel.setAid(paAppModel.getModelId());
            adIntentModel.setPo(PlazaApiConstant.PLAZA_POSITION);
            adIntentModel.setUrl(url);
            intent.putExtra(AdWebViewActivity.AD_INTENT_MODEL, adIntentModel);
            startActivity(intent);
            return true;
        } else if (paAppModel.getModelAction() == 0) {
            return false;
        } else {
            intent = new Intent(this.activity, PlazaWebViewActivity.class);
            intent.putExtra(PlazaConstant.WEB_VIEW_URL, url);
            if (paAppModel.getModelAction() == 2) {
                intent.putExtra(PlazaConstant.WEB_VIEW_TOP, false);
            } else {
                intent.putExtra(PlazaConstant.WEB_VIEW_TOP, true);
            }
            intent.putExtra("title", paAppModel.getModelName());
            startActivity(intent);
            return true;
        }
    }

    private void initFirst() {
        new Thread() {
            public void run() {
                PlazaFragmentNew.this.dbAppList = PlazaFragmentNew.this.getDbModuleData();
                if (PlazaFragmentNew.this.intentModel != null && PlazaFragmentNew.this.intentModel.getDbIds() != null) {//走这个流程
                    int[] ids = PlazaFragmentNew.this.intentModel.getDbIds();
                    for (int j = PlazaFragmentNew.this.dbAppList.size() - 1; j >= 0; j--) {
                        if (!PlazaFragmentNew.this.isInArray(ids, ((PlazaAppModel) PlazaFragmentNew.this.dbAppList.get(j)).getNativeCat())) {
                            PlazaFragmentNew.this.dbAppList.remove(j);
                        }
                    }
                } else if (PlazaFragmentNew.this.intentModel != null && PlazaFragmentNew.this.intentModel.getDbIds() == null) {
                    PlazaFragmentNew.this.dbAppList.clear();
                }
                LowestResultModel<List<PlazaAppModel>> result = PlazaFragmentNew.this.plazaService.getPlazaAppModelListByLocal();//从plaza.db数据库读取
                if (!MCListUtils.isEmpty((List) result.getData())) {
                    PlazaFragmentNew.this.netAppList = (List) result.getData();
                }
                PlazaFragmentNew.this.assembleData();
//                PlazaFragmentNew.this.initNet();
            }
        }.start();
    }

    private boolean isInArray(int[] ids, int id) {
        for (int i : ids) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

    private void initNet() {
        new Thread() {
            public void run() {
                LowestResultModel<List<PlazaAppModel>> result = PlazaFragmentNew.this.plazaService.getPlazaAppModelListByNet(PlazaFragmentNew.this.config.getForumKey(), PlazaFragmentNew.this.config.getUserId());
                if (result.getRs() == 1 && !MCListUtils.isEmpty((List) result.getData())) {
                    PlazaFragmentNew.this.netAppList = (List) result.getData();
                    PlazaFragmentNew.this.assembleData();
                }
            }
        }.start();
    }

    private void assembleData() {
        this.plazaMoudelList.clear();
        if (!MCListUtils.isEmpty(this.dbAppList)) {
            this.plazaMoudelList.add(this.dbAppList);
        }
        if (!MCListUtils.isEmpty(this.netAppList)) {
            this.plazaMoudelList.add(this.netAppList);
        }
        if (!MCListUtils.isEmpty(this.nativeAppList)) {
            this.plazaMoudelList.add(this.nativeAppList);
        }
        notifyList();
    }

    private void setAdapter() {
        if (!MCListUtils.isEmpty(this.switchAppList)) {
            this.expandableListView.addHeaderView(this.pagerHelper.initView(this.activity, parseSwitchList(), new HeaderPagerClickListener() {
                public void onHeaderClick(SwitchModel switchModel) {
                    if (switchModel.getData() instanceof PlazaAppModel) {
                        PlazaFragmentNew.this.onChildItemClick((PlazaAppModel) switchModel.getData());
                    }
                }
            }));
            this.pagerHelper.startAutoScroll();
        }
        if (this.adapter == null) {
            this.adapter = new PlazaFragmentNewAdapter(this.activity, this.plazaMoudelList);
            if (MCListUtils.isEmpty(this.switchAppList)) {
                this.adapter.setHasHeaderView(false);
            } else {
                this.adapter.setHasHeaderView(true);
            }
        }
        this.expandableListView.setAdapter(this.adapter);
        expandList();
    }

    private List<SwitchModel> parseSwitchList() {
        List<SwitchModel> switchList = new ArrayList();
        int count = this.switchAppList.size();
        for (int i = 0; i < count; i++) {
            PlazaAppModel appModel = (PlazaAppModel) this.switchAppList.get(i);
            SwitchModel switchModel = new SwitchModel();
            switchModel.setData(appModel);
            switchModel.setDesc(appModel.getModelName());
            switchModel.setImgUrl(appModel.getModelDrawable());
            switchList.add(switchModel);
        }
        return switchList;
    }

    private void expandList() {
        int count = this.plazaMoudelList.size();
        for (int i = 0; i < count; i++) {
            this.expandableListView.expandGroup(i);
        }
    }

    private void notifyList() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (PlazaFragmentNew.this.adapter != null) {
                    PlazaFragmentNew.this.adapter.notifyDataSetChanged();
                }
                PlazaFragmentNew.this.expandList();
            }
        });
    }

    public void notifyDataChange() {
        notifyList();
    }

    public List<List<PlazaAppModel>> getModuleData() {
        return this.plazaMoudelList;
    }

    public List<PlazaAppModel> getDbModuleData() {
        return this.localDataHelper.getLocalPlazaAppList(this.activity);
    }
}
