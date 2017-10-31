package com.mobcent.lowest.android.ui.module.plaza.activity;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.IntentPlazaModel;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.PlazaSearchChannelModel;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.PlazaSearchKeyModel;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.android.ui.module.plaza.dialog.SearchTypeDialog;
import com.mobcent.lowest.android.ui.module.plaza.dialog.SearchTypeDialog.SearchDialogListener;
import com.mobcent.lowest.android.ui.module.plaza.fragment.PlazaSearchListFragment;
import com.mobcent.lowest.base.utils.MCErrorLogUtil;
import com.mobcent.lowest.base.utils.MCLogUtil;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PlazaSearchActivity extends BasePlazaFragmentActivity {
    private String TAG = "SearchActivity";
    private Button backBtn;
    private int baikeType;
    private RelativeLayout channelBox;
    private TextView channelText;
    private RelativeLayout containerBox;
    private Builder dialog;
    private IntentPlazaModel intentPlazaModel;
    private boolean isCreate = true;
    private String[] items;
    private EditText keyWordEdit;
    private PlazaSearchListFragment listFragment;
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (v == PlazaSearchActivity.this.channelBox) {
                PlazaSearchActivity.this.searchTypeDialog.show(v);
            } else if (v == PlazaSearchActivity.this.webSearchText) {
                PlazaSearchActivity.this.onWebSearchBtnClick(PlazaSearchActivity.this.keyWordEdit.getText().toString());
            } else if (v == PlazaSearchActivity.this.backBtn) {
                PlazaSearchActivity.this.onBackPressed();
            }
        }
    };
    private List<PlazaSearchChannelModel> searchBoards;
    private SearchDialogListener searchDialogListener = new SearchDialogListener() {
        public void onDialogDismiss(int position, SearchTypeDialog dialog) {
            dialog.dismiss();
            PlazaSearchActivity.this.baikeType = ((PlazaSearchChannelModel) PlazaSearchActivity.this.searchBoards.get(position)).getBaikeType();
            PlazaSearchActivity.this.setChannelText(((PlazaSearchChannelModel) PlazaSearchActivity.this.searchBoards.get(position)).getTypeName());
            PlazaSearchActivity.this.onSearchBtnClick();
        }
    };
    private String searchHeadText = null;
    private SearchTypeDialog searchTypeDialog = null;
    private String[] typeNames;
    private int[] types;
    private TextView webSearchText;

    protected void initData() {
        int i;
        this.searchBoards = new ArrayList();
        this.searchHeadText = this.adResource.getString("mc_plaza_search_search_head_text");
        this.typeNames = getResources().getStringArray(this.adResource.getArrayId("mc_plaza_search_channel"));
        this.types = getResources().getIntArray(this.adResource.getArrayId("mc_plaza_search_types_order"));
        for (i = 0; i < this.typeNames.length; i++) {
            PlazaSearchChannelModel searchBoard = new PlazaSearchChannelModel();
            searchBoard.setBaikeType(this.types[i]);
            searchBoard.setTypeName(this.typeNames[i]);
            searchBoard.setShow(false);
            this.searchBoards.add(searchBoard);
        }
        this.intentPlazaModel = (IntentPlazaModel) getIntent().getSerializableExtra(PlazaActivity.INTENT_PLAZA_MODEL);
        if (this.intentPlazaModel.getSearchTypes() == null || this.intentPlazaModel.getSearchTypes().length == 0) {
            finish();
            return;
        }
        dealSearchList(this.intentPlazaModel.getSearchTypes());
        this.items = new String[this.searchBoards.size()];
        for (i = 0; i < this.searchBoards.size(); i++) {
            this.items[i] = ((PlazaSearchChannelModel) this.searchBoards.get(i)).getTypeName();
        }
        if (this.searchTypeDialog == null) {
            this.searchTypeDialog = new SearchTypeDialog(this, this.adResource.getStyleId("mc_plaza_search_dialog_style"));
            this.searchTypeDialog.setSearchDialogListener(this.searchDialogListener);
            this.searchTypeDialog.setSearchListData(this.searchBoards);
        }
    }

    protected void initViews() {
        requestWindowFeature(1);
        setContentView(this.adResource.getLayoutId("mc_plaza_search_activity"));
        this.containerBox = (RelativeLayout) findViewById(this.adResource.getViewId("container_layout"));
        this.channelBox = (RelativeLayout) findViewById(this.adResource.getViewId("mc_plaza_channel_layout"));
        this.webSearchText = (TextView) findViewById(this.adResource.getViewId("web_search_btn"));
        this.channelText = (TextView) findViewById(this.adResource.getViewId("channel_text"));
        this.keyWordEdit = (EditText) findViewById(this.adResource.getViewId("key_word_edit"));
        this.backBtn = (Button) findViewById(this.adResource.getViewId("back_btn"));
        this.dialog = new Builder(this);
        this.dialog.create();
        setChannelText(((PlazaSearchChannelModel) this.searchBoards.get(0)).getTypeName());
        this.listFragment = new PlazaSearchListFragment(this.mHandler);
        addFragment(this.listFragment);
        showFragment(this.listFragment);
        if (this.searchBoards.size() > 0) {
            this.baikeType = ((PlazaSearchChannelModel) this.searchBoards.get(0)).getBaikeType();
        }
        if (!this.intentPlazaModel.isShowWebSearch()) {
            this.webSearchText.setVisibility(8);
        }
    }

    protected void initWidgetActions() {
        this.channelBox.setOnClickListener(this.onClickListener);
        this.webSearchText.setOnClickListener(this.onClickListener);
        this.backBtn.setOnClickListener(this.onClickListener);
        this.keyWordEdit.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == 66) {
                    PlazaSearchActivity.this.keyWordEdit.setFocusable(false);
                    PlazaSearchActivity.this.keyWordEdit.setFocusableInTouchMode(true);
                    if (PlazaSearchActivity.this.intentPlazaModel.isShowWebSearch()) {
                        PlazaSearchActivity.this.onWebSearchBtnClick(PlazaSearchActivity.this.keyWordEdit.getText().toString());
                    } else {
                        PlazaSearchActivity.this.onSearchBtnClick();
                    }
                    PlazaSearchActivity.this.hideSoftKeyboard();
                }
                return false;
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(17432576, 17432577);
    }

    protected void onResume() {
        super.onResume();
        if (this.isCreate) {
            this.isCreate = false;
            this.keyWordEdit.requestFocus();
        }
    }

    protected void onPause() {
        super.onPause();
        hideSoftKeyboard();
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.add(this.containerBox.getId(), fragment);
        tran.addToBackStack(null);
        tran.commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.show(fragment);
        tran.addToBackStack(null);
        tran.commit();
    }

    private void dealSearchList(int[] showTypes) {
        int i;
        for (int i2 : showTypes) {
            for (int j = 0; j < this.searchBoards.size(); j++) {
                PlazaSearchChannelModel searchBoard = (PlazaSearchChannelModel) this.searchBoards.get(j);
                if (i2 == searchBoard.getBaikeType()) {
                    searchBoard.setShow(true);
                    break;
                }
            }
        }
        List<PlazaSearchChannelModel> searchBoardsTemp = new ArrayList();
        for (i = 0; i < this.searchBoards.size(); i++) {
            if (((PlazaSearchChannelModel) this.searchBoards.get(i)).isShow()) {
                searchBoardsTemp.add(this.searchBoards.get(i));
            }
        }
        this.searchBoards.clear();
        this.searchBoards.addAll(searchBoardsTemp);
    }

    private void onWebSearchBtnClick(String q) {
        try {
            q = URLEncoder.encode(q, "utf-8");
        } catch (Exception e) {
            MCLogUtil.e(this.TAG, MCErrorLogUtil.getErrorInfo(e));
        }
        String url = "http://i.easou.com/s.m?cid=bicn3516_49704_D_1&sty=1&q=" + q;
        Intent intent = new Intent(this, PlazaWebViewActivity.class);
        intent.putExtra(PlazaConstant.WEB_VIEW_URL, url);
        intent.putExtra(PlazaConstant.WEB_VIEW_TOP, true);
        startActivity(intent);
    }

    public void onSearchBtnClick() {
        PlazaSearchKeyModel searchKeyModel = new PlazaSearchKeyModel();
        searchKeyModel.setForumId(this.intentPlazaModel.getForumId());
        searchKeyModel.setForumKey(this.intentPlazaModel.getForumKey());
        searchKeyModel.setUserId(this.intentPlazaModel.getUserId());
        searchKeyModel.setBaikeType(this.baikeType);
        searchKeyModel.setSearchMode(1);
        searchKeyModel.setKeyWord(this.keyWordEdit.getText().toString());
        showFragment(this.listFragment);
        this.listFragment.requestData(searchKeyModel);
        hideSoftKeyboard();
    }

    private void setChannelText(String text) {
        this.channelText.setText(this.searchHeadText + text);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 17432577);
    }
}
