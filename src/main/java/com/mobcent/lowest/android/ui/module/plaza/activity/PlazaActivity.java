package com.mobcent.lowest.android.ui.module.plaza.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.ad.activity.AdWebViewActivity;
import com.mobcent.lowest.android.ui.module.plaza.activity.adapter.PlazaListAdapter;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.IntentPlazaModel;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.module.plaza.fragment.PlazaFragment.PlazaFragmentListener;
import com.mobcent.lowest.base.manager.LowestManager;
import com.mobcent.lowest.base.model.LowestResultModel;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCLogUtil;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.model.AdIntentModel;
import com.mobcent.lowest.module.plaza.api.constant.PlazaApiConstant;
import com.mobcent.lowest.module.plaza.config.PlazaConfig;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import com.mobcent.lowest.module.plaza.service.PlazaService;
import com.mobcent.lowest.module.plaza.service.impl.PlazaServiceImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlazaActivity extends BasePlazaFragmentActivity implements PlazaFragmentListener, OnDismissListener, PlazaApiConstant {
    public static final String APP_KEY = "appKey";
    public static final String INTENT_PLAZA_MODEL = "plazaIntentModel";
    public static final String SEARCH_TYPES = "searchTypes";
    public static final String USER_ID = "userId";
    private String TAG = "PlazaActivity";
    private RelativeLayout aboutBox;
    private Button aboutBtn;
    private RelativeLayout baseSearchBox;
    private View beforeView;
    private ListView contentList;
    private PlazaListAdapter contentListAdapter;
    private AsyncTask<Void, Void, LowestResultModel<List<PlazaAppModel>>> currentTask;
    private boolean hideSearchBox = false;
    private IntentPlazaModel intentPlazaModel;
    private RelativeLayout loadingBox;
    private OnTouchListener onTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (v == PlazaActivity.this.personalBox) {
                if (event.getAction() == 1) {
                    PlazaActivity.this.personalBtn.setPressed(false);
                } else {
                    PlazaActivity.this.personalBtn.setPressed(true);
                }
            } else if (v == PlazaActivity.this.setBox) {
                if (event.getAction() == 1) {
                    PlazaActivity.this.setBtn.setPressed(false);
                } else {
                    PlazaActivity.this.setBtn.setPressed(true);
                }
            } else if (v == PlazaActivity.this.aboutBox) {
                if (event.getAction() == 1) {
                    PlazaActivity.this.aboutBtn.setPressed(false);
                } else {
                    PlazaActivity.this.aboutBtn.setPressed(true);
                }
            }
            return false;
        }
    };
    private RelativeLayout personalBox;
    private Button personalBtn;
    private List<PlazaAppModel> plazaAppModels;
    private PlazaService plazaService;
    private RelativeLayout setBox;
    private Button setBtn;
    private TextView titleText;
    private RelativeLayout topBarBox;
    private LinearLayout topBtnBox;

    class GetDataTask extends AsyncTask<Void, Void, LowestResultModel<List<PlazaAppModel>>> {
        GetDataTask() {
        }

        protected void onPreExecute() {
            PlazaActivity.this.loadingBox.setVisibility(0);
            PlazaActivity.this.setProgressBarVisibility(true);
        }

        protected LowestResultModel<List<PlazaAppModel>> doInBackground(Void... params) {
            return PlazaActivity.this.plazaService.getPlazaAppModelListByLocal();
        }

        protected void onPostExecute(LowestResultModel<List<PlazaAppModel>> result) {
            PlazaActivity.this.loadingBox.setVisibility(8);
            PlazaActivity.this.setProgressBarVisibility(false);
            PlazaActivity.this.plazaAppModels.clear();
            if (PlazaActivity.this.intentPlazaModel.getPlazaAppModels() != null) {
                PlazaActivity.this.plazaAppModels.addAll(PlazaActivity.this.intentPlazaModel.getPlazaAppModels());
            }
            if (result.getRs() == 1 && !MCListUtils.isEmpty((List) result.getData())) {
                PlazaActivity.this.plazaAppModels.addAll((Collection) result.getData());
                ((List) result.getData()).clear();
            }
            PlazaActivity.this.contentListAdapter.notifyDataSetChanged();
        }
    }

    class GetNetDataUpdateLocal extends AsyncTask<Void, Void, LowestResultModel<List<PlazaAppModel>>> {
        GetNetDataUpdateLocal() {
        }

        protected LowestResultModel<List<PlazaAppModel>> doInBackground(Void... params) {
            return PlazaActivity.this.plazaService.getPlazaAppModelListByNet(PlazaActivity.this.intentPlazaModel.getForumKey(), PlazaActivity.this.intentPlazaModel.getUserId());
        }

        protected void onPostExecute(LowestResultModel<List<PlazaAppModel>> result) {
            int rs = result.getRs();
            if (rs != 0 && rs != 2 && !MCListUtils.isEmpty((List) result.getData())) {
                PlazaActivity.this.plazaAppModels.clear();
                if (PlazaActivity.this.intentPlazaModel.getPlazaAppModels() != null) {
                    PlazaActivity.this.plazaAppModels.addAll(PlazaActivity.this.intentPlazaModel.getPlazaAppModels());
                }
                PlazaActivity.this.plazaAppModels.addAll((Collection) result.getData());
                ((List) result.getData()).clear();
                PlazaActivity.this.contentListAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void initData() {
        this.plazaAppModels = new ArrayList();
        this.plazaService = new PlazaServiceImpl(this);
        this.intentPlazaModel = (IntentPlazaModel) getIntent().getSerializableExtra(INTENT_PLAZA_MODEL);
    }

    protected void initViews() {
        requestWindowFeature(1);
        setContentView(this.adResource.getLayoutId("mc_plaza_activity"));
        this.contentList = (ListView) findViewById(this.adResource.getViewId("mc_plaza_content_list"));
        ((EditText) findViewById(this.adResource.getViewId("key_word_edit"))).setFocusable(false);
        this.titleText = (TextView) findViewById(this.adResource.getViewId("title_text"));
        this.beforeView = findViewById(this.adResource.getViewId("mc_plaza_before_view"));
        this.topBarBox = (RelativeLayout) findViewById(this.adResource.getViewId("mc_plaza_top_bar_layout"));
        this.topBtnBox = (LinearLayout) findViewById(this.adResource.getViewId("mc_plaza_top_layout"));
        this.loadingBox = (RelativeLayout) findViewById(this.adResource.getViewId("loading_layout"));
        this.baseSearchBox = (RelativeLayout) findViewById(this.adResource.getViewId("base_search_layout"));
        this.personalBox = (RelativeLayout) findViewById(this.adResource.getViewId("personal_layout"));
        this.setBox = (RelativeLayout) findViewById(this.adResource.getViewId("set_layout"));
        this.aboutBox = (RelativeLayout) findViewById(this.adResource.getViewId("about_layout"));
        this.personalBtn = (Button) findViewById(this.adResource.getViewId("personal_btn"));
        this.setBtn = (Button) findViewById(this.adResource.getViewId("set_btn"));
        this.aboutBtn = (Button) findViewById(this.adResource.getViewId("about_btn"));
        if (this.intentPlazaModel != null) {
            if (this.intentPlazaModel.getSearchTypes() == null || this.intentPlazaModel.getSearchTypes().length == 0) {
                this.hideSearchBox = true;
                this.baseSearchBox.setVisibility(8);
            } else if (this.intentPlazaModel.getSearchTypes().length == 1) {
            }
            if (this.intentPlazaModel.isShowTopBar()) {
                this.topBarBox.setVisibility(0);
                if (!MCStringUtil.isEmpty(this.intentPlazaModel.getTitle())) {
                    this.titleText.setText(this.intentPlazaModel.getTitle());
                }
            } else {
                this.topBarBox.setVisibility(8);
                LayoutParams lps = (LayoutParams) this.topBtnBox.getLayoutParams();
                lps.setMargins(lps.leftMargin, 0, lps.rightMargin, lps.bottomMargin);
                this.topBtnBox.setLayoutParams(lps);
            }
        } else {
            this.baseSearchBox.setVisibility(8);
            this.hideSearchBox = true;
        }
        this.contentList.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                PlazaActivity.this.contentList.getViewTreeObserver().removeOnPreDrawListener(this);
                PlazaActivity.this.contentListAdapter = new PlazaListAdapter(PlazaActivity.this, PlazaActivity.this.plazaAppModels, PlazaActivity.this, PlazaActivity.this.contentList.getHeight(), PlazaActivity.this.hideSearchBox);
                PlazaActivity.this.contentList.setAdapter(PlazaActivity.this.contentListAdapter);
                PlazaActivity.this.getDataTask();
                return false;
            }
        });
    }

    protected void initWidgetActions() {
        this.beforeView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                    PlazaActivity.this.intentPlazaModel.setUserId(LowestManager.getInstance().getConfig().getUserId());
                    Intent intent = new Intent(PlazaActivity.this, PlazaSearchActivity.class);
                    intent.putExtra(PlazaActivity.INTENT_PLAZA_MODEL, PlazaActivity.this.intentPlazaModel);
                    PlazaActivity.this.startActivity(intent);
                }
            }
        });
        this.loadingBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PlazaActivity.this.loadingBox.setVisibility(8);
                PlazaActivity.this.setProgressBarVisibility(false);
            }
        });
        this.personalBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                    PlazaConfig.getInstance().getPlazaDelegate().onPersonalClick(PlazaActivity.this);
                }
            }
        });
        this.setBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                    PlazaConfig.getInstance().getPlazaDelegate().onSetClick(PlazaActivity.this);
                }
            }
        });
        this.aboutBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                    PlazaConfig.getInstance().getPlazaDelegate().onAboutClick(PlazaActivity.this);
                }
            }
        });
        this.personalBox.setOnTouchListener(this.onTouchListener);
        this.setBox.setOnTouchListener(this.onTouchListener);
        this.aboutBox.setOnTouchListener(this.onTouchListener);
    }

    protected void onResume() {
        super.onResume();
    }

    private void getDataTask() {
        this.currentTask = new GetDataTask().execute(new Void[0]);
    }

    public void onDismiss(DialogInterface dialog) {
        cancelTask();
    }

    private void cancelTask() {
        if (this.currentTask != null && !this.currentTask.isCancelled()) {
            this.currentTask.cancel(true);
        }
    }

    public void onBackPressed() {
        if (this.loadingBox.getVisibility() == 0) {
            this.loadingBox.setVisibility(8);
            setProgressBarVisibility(false);
        } else if (PlazaConfig.getInstance().getPlazaDelegate() == null) {
            super.onBackPressed();
        } else if (!PlazaConfig.getInstance().getPlazaDelegate().onPlazaBackPressed(this)) {
            super.onBackPressed();
        }
    }

    public void plazaAppImgClick(PlazaAppModel paAppModel) {
        String url = this.plazaService.getPlazaLinkUrl(this.intentPlazaModel.getForumKey(), paAppModel.getModelId(), this.intentPlazaModel.getUserId());
        Intent intent;
        if (paAppModel.getModelAction() == 5) {
            if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
                PlazaConfig.getInstance().getPlazaDelegate().onAppItemClick(this, paAppModel);
            }
        } else if (paAppModel.getModelAction() == 3) {
            intent = new Intent(this, AdWebViewActivity.class);
            AdIntentModel adIntentModel = new AdIntentModel();
            adIntentModel.setAid(paAppModel.getModelId());
            adIntentModel.setPo(PlazaApiConstant.PLAZA_POSITION);
            adIntentModel.setUrl(url);
            intent.putExtra(AdWebViewActivity.AD_INTENT_MODEL, adIntentModel);
            startActivity(intent);
        } else if (paAppModel.getModelAction() != 5) {
            MCLogUtil.e(this.TAG, "request url -- " + url);
            if (paAppModel.getModelAction() == 4) {
                intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
            } else {
                intent = new Intent(this, PlazaWebViewActivity.class);
                intent.putExtra(PlazaConstant.WEB_VIEW_URL, url);
                if (paAppModel.getModelAction() == 1) {
                    intent.putExtra(PlazaConstant.WEB_VIEW_TOP, true);
                } else if (paAppModel.getModelAction() == 2) {
                    intent.putExtra(PlazaConstant.WEB_VIEW_TOP, false);
                }
            }
            startActivity(intent);
        } else if (PlazaConfig.getInstance().getPlazaDelegate() != null) {
            PlazaConfig.getInstance().getPlazaDelegate().onAppItemClick(this, paAppModel);
        }
    }
}
