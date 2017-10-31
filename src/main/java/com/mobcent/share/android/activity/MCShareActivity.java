package com.mobcent.share.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.mobcent.android.constant.MCShareReturnConstant;
import com.mobcent.android.db.MCShareSharedPreferencesDB;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.android.service.MCShareService;
import com.mobcent.android.service.impl.MCShareServiceImpl;
import com.mobcent.lowest.android.ui.utils.MCStringBundleUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.share.android.activity.adapter.MCShareSiteAdapter;
import com.mobcent.share.android.activity.adapter.MCShareSiteAdapter.UpdateDataListener;
import com.mobcent.share.android.constant.MCShareIntentConstant;
import com.mobcent.share.android.utils.MCShareImageHelper;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MCShareActivity extends Activity implements UpdateDataListener {
    public static final int BIND_REQUEST_CODE = 100;
    public static final int BIND_RESULT_CODE = 200;
    private MCShareSiteAdapter adapter;
    private ImageView atBtn;
    private Bitmap bitmap;
    private Button cancelBtn;
    private EditText contentEditText;
    private GetShareSiteTask getShareSiteTask;
    private List<MCShareSiteModel> list;
    private Handler mHandler;
    private MCResource resource;
    private List<MCShareSiteModel> selectedSites;
    private Button shareBtn;
    private String shareContent;
    private int shareContentLength = 0;
    private ImageView shareImageView;
    private ShareInfoTask shareInfoTask;
    private MCShareModel shareModel;
    private String sharePicUrl;
    private MCShareService shareService;
    private ListView shareSiteList;
    private String shareUrl;
    private TextView shareWordNum;
    private MCShareSharedPreferencesDB sharedPreferencesDB;
    private ImageView topicBtn;
    private int upperLimit = 140;
    private long userId = -1;

    private class GetShareSiteTask extends AsyncTask<Void, Void, List<MCShareSiteModel>> {
        private GetShareSiteTask() {
        }

        protected List<MCShareSiteModel> doInBackground(Void... params) {
            return MCShareActivity.this.shareService.getAllSitesByLocal(MCShareActivity.this.shareModel.getAppKey(), MCResource.getInstance(MCShareActivity.this).getString("mc_share_domain_url"), MCShareActivity.this.getResources().getConfiguration().locale.getLanguage(), MCShareActivity.this.getResources().getConfiguration().locale.getCountry());
        }

        protected void onPostExecute(List<MCShareSiteModel> result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                MCShareActivity.this.userId = ((MCShareSiteModel) result.get(0)).getUserId();
                MCShareActivity.this.list.clear();
                MCShareActivity.this.list.addAll(result);
                MCShareActivity.this.adapter.setList(MCShareActivity.this.list);
                MCShareActivity.this.adapter.notifyDataSetChanged();
            }
        }
    }

    private class ShareInfoTask extends AsyncTask<Void, Void, String> {
        private ShareInfoTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            MCShareToastUtil.toast(MCShareActivity.this, MCShareActivity.this.resource.getString("mc_share_sharing"));
        }

        protected String doInBackground(Void... params) {
            MCShareActivity.this.sharePicUrl = "";
            if (!MCStringUtil.isEmpty(MCShareActivity.this.shareModel.getPicUrl())) {
                MCShareActivity.this.sharePicUrl = MCShareActivity.this.shareModel.getPicUrl();
            } else if (!(MCStringUtil.isEmpty(MCShareActivity.this.shareModel.getImageFilePath()) || MCShareActivity.this.userId == -1)) {
                String picTempUrl = MCShareActivity.this.shareService.uploadImage(MCShareActivity.this.userId, MCShareActivity.this.shareModel.getImageFilePath(), MCShareActivity.this.shareModel.getAppKey(), MCShareActivity.this.resource.getString("mc_share_domain_image_url"));
                if (!MCStringUtil.isEmpty(picTempUrl)) {
                    MCShareActivity.this.sharePicUrl = picTempUrl;
                }
            }
            String ids = AdApiConstant.RES_SPLIT_COMMA;
            int j = MCShareActivity.this.selectedSites.size();
            for (int i = 0; i < j; i++) {
                ids = ids + ((MCShareSiteModel) MCShareActivity.this.selectedSites.get(i)).getSiteId() + AdApiConstant.RES_SPLIT_COMMA;
            }
            MCShareActivity.this.shareUrl = "";
            if (!MCStringUtil.isEmpty(MCShareActivity.this.shareModel.getLinkUrl())) {
                MCShareActivity.this.shareUrl = MCShareActivity.this.shareModel.getLinkUrl() + " ";
            }
            if (!MCStringUtil.isEmpty(MCShareActivity.this.sharedPreferencesDB.getShareUrl())) {
                MCShareActivity.this.shareUrl = MCShareActivity.this.shareUrl + MCShareActivity.this.sharedPreferencesDB.getShareUrl() + " ";
            }
            return MCShareActivity.this.shareService.shareInfo(MCShareActivity.this.userId, MCShareActivity.this.shareContent, MCShareActivity.this.sharePicUrl, ids, MCShareActivity.this.shareUrl.trim(), MCShareActivity.this.shareModel.getAppKey(), MCShareActivity.this.resource.getString("mc_share_domain_url"));
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (MCShareReturnConstant.RS_SUCC.equals(result)) {
                String ids = "";
                for (int i = 0; i < MCShareActivity.this.selectedSites.size(); i++) {
                    if (ids.equals("")) {
                        ids = ((MCShareSiteModel) MCShareActivity.this.selectedSites.get(i)).getSiteId() + "";
                    } else {
                        ids = ids + AdApiConstant.RES_SPLIT_COMMA + ((MCShareSiteModel) MCShareActivity.this.selectedSites.get(i)).getSiteId();
                    }
                }
                MCShareSharedPreferencesDB.getInstance(MCShareActivity.this).setSelectedSiteIds(ids);
                MCShareToastUtil.toast(MCShareActivity.this, MCShareActivity.this.resource.getString("mc_share_share_succ"));
            } else {
                MCShareToastUtil.toast(MCShareActivity.this, MCShareActivity.this.resource.getString("mc_share_share_fail"));
            }
            MCShareActivity.this.finish();
            MCShareActivity.this.hideSoftKeyboard();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resource = MCResource.getInstance(this);
        requestWindowFeature(1);
        initData();
        initViews();
        initWidgetActions();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            this.shareModel = (MCShareModel) intent.getSerializableExtra(MCShareIntentConstant.MC_SHARE_MODEL);
        }
        this.list = new ArrayList();
        this.shareService = new MCShareServiceImpl(this);
        this.getShareSiteTask = new GetShareSiteTask();
        this.getShareSiteTask.execute(new Void[0]);
        this.mHandler = new Handler();
        this.sharedPreferencesDB = MCShareSharedPreferencesDB.getInstance(this);
    }

    private void initViews() {
        setContentView(this.resource.getLayoutId("mc_share_activity"));
        this.cancelBtn = (Button) findViewById(this.resource.getViewId("mc_share_cancel"));
        this.shareBtn = (Button) findViewById(this.resource.getViewId("mc_share_sure"));
        this.contentEditText = (EditText) findViewById(this.resource.getViewId("mc_share_edit_text"));
        this.shareImageView = (ImageView) findViewById(this.resource.getViewId("mc_share_image"));
        this.atBtn = (ImageView) findViewById(this.resource.getViewId("mc_share_at_btn"));
        this.topicBtn = (ImageView) findViewById(this.resource.getViewId("mc_share_topic_btn"));
        this.shareWordNum = (TextView) findViewById(this.resource.getViewId("mc_share_word_num"));
        this.shareSiteList = (ListView) findViewById(this.resource.getViewId("mc_share_item_list"));
        this.adapter = new MCShareSiteAdapter(this, this.list, null, this.mHandler, this.shareModel.getAppKey(), this);
        this.shareSiteList.setAdapter(this.adapter);
        if (!MCStringUtil.isEmpty(this.shareModel.getPicUrl())) {
            this.shareImageView.setVisibility(0);
            String imgUrl = this.shareModel.getPicUrl();
            if (!TextUtils.isEmpty(imgUrl) && imgUrl.startsWith("/")) {
                imgUrl = Uri.fromFile(new File(imgUrl)).toString();
            }
            ImageLoader.getInstance().displayImage(imgUrl, this.shareImageView);
        } else if (!MCStringUtil.isEmpty(this.shareModel.getImageFilePath())) {
            this.bitmap = MCShareImageHelper.compressBitmap(this.shareModel.getImageFilePath(), 75.0f, this);
            if (!(this.bitmap == null || this.bitmap.isRecycled())) {
                this.shareImageView.setVisibility(0);
                this.shareImageView.setImageBitmap(this.bitmap);
            }
        }
        if (!MCStringUtil.isEmpty(this.shareModel.getContent())) {
            if (this.shareModel.getType() == 6) {
                this.contentEditText.setText(MCShareSharedPreferencesDB.getInstance(this).getShareAppContent());
                this.shareContentLength = MCShareSharedPreferencesDB.getInstance(this).getShareAppContent().length();
            } else {
                this.contentEditText.setText(this.shareModel.getContent().trim());
                this.shareContentLength = this.shareModel.getContent().trim().length();
            }
            this.contentEditText.setSelection(this.shareContentLength);
        }
        calculateTextSize();
    }

    private void initWidgetActions() {
        this.cancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) MCShareActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(MCShareActivity.this.contentEditText.getWindowToken(), 0);
                MCShareActivity.this.finish();
            }
        });
        this.atBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCShareActivity.this.showSoftKeyboard(MCShareActivity.this.contentEditText);
                MCShareActivity.this.contentEditText.getText().delete(MCShareActivity.this.contentEditText.getSelectionStart(), MCShareActivity.this.contentEditText.getSelectionEnd());
                String defaultString = " @" + MCShareActivity.this.resource.getString("mc_share_input_some_one");
                int selectionStart = MCShareActivity.this.contentEditText.getSelectionEnd();
                int start = selectionStart + 2;
                int end = selectionStart + defaultString.length();
                MCShareActivity.this.contentEditText.getText().insert(selectionStart, defaultString);
                MCShareActivity.this.contentEditText.setSelection(start, end);
            }
        });
        this.topicBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCShareActivity.this.showSoftKeyboard(MCShareActivity.this.contentEditText);
                MCShareActivity.this.contentEditText.getText().delete(MCShareActivity.this.contentEditText.getSelectionStart(), MCShareActivity.this.contentEditText.getSelectionEnd());
                String defaultString = " #" + MCShareActivity.this.resource.getString("mc_share_input_topic") + "#";
                int selectionStart = MCShareActivity.this.contentEditText.getSelectionEnd();
                int start = selectionStart + 2;
                int end = (defaultString.length() + selectionStart) - 1;
                String s = MCShareActivity.this.contentEditText.getText().toString();
                if (selectionStart >= s.length()) {
                    MCShareActivity.this.contentEditText.getText().insert(selectionStart, defaultString);
                } else if (s.substring(selectionStart, selectionStart + 1).equals("#")) {
                    try {
                        MCShareActivity.this.contentEditText.getText().insert(selectionStart + 1, defaultString);
                        start++;
                        end++;
                    } catch (Exception e) {
                        MCShareActivity.this.contentEditText.getText().insert(selectionStart, defaultString);
                    }
                } else {
                    MCShareActivity.this.contentEditText.getText().insert(selectionStart, defaultString);
                }
                MCShareActivity.this.contentEditText.setSelection(start, end);
            }
        });
        this.contentEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                MCShareActivity.this.mHandler.post(new Runnable() {
                    public void run() {
                        MCShareActivity.this.calculateTextSize();
                    }
                });
            }
        });
        this.shareBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MCShareActivity.this.shareToWeb();
            }
        });
    }

    private void calculateTextSize() {
        this.shareContentLength = this.contentEditText.getText().length();
        int wordsLeft = this.upperLimit - this.shareContentLength;
        if (wordsLeft < 0) {
            this.shareWordNum.setTextColor(SupportMenu.CATEGORY_MASK);
        } else {
            this.shareWordNum.setTextColor(this.resource.getColorId("mc_share_text_normal_color3"));
        }
        this.shareWordNum.setText(MCStringBundleUtil.resolveString(this.resource.getStringId("mc_share_word_num"), String.valueOf(Math.abs(wordsLeft)), (Context) this));
    }

    private void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService("input_method");
        view.requestFocus();
        imm.showSoftInput(view, 1);
    }

    private void shareToWeb() {
        this.shareContent = this.contentEditText.getText().toString();
        if (MCStringUtil.isEmpty(this.shareContent)) {
            MCShareToastUtil.toast(this, this.resource.getString("mc_share_content_tip"));
        } else if (this.upperLimit - this.shareContent.length() < 0) {
            MCShareToastUtil.toast(this, MCStringBundleUtil.resolveString(this.resource.getStringId("mc_share_publish_words_tip"), String.valueOf(this.upperLimit), (Context) this));
        } else {
            this.shareBtn.setEnabled(false);
            this.selectedSites = getSelectSites();
            if (this.selectedSites.isEmpty()) {
                MCShareToastUtil.toast(this, this.resource.getString("mc_share_select_at_least_one"));
                this.shareBtn.setEnabled(true);
                return;
            }
            this.shareInfoTask = new ShareInfoTask();
            this.shareInfoTask.execute(new Void[0]);
        }
    }

    private List<MCShareSiteModel> getSelectSites() {
        List<MCShareSiteModel> selectSite = new ArrayList();
        for (MCShareSiteModel model : this.list) {
            if (model.getBindState() == 1) {
                selectSite.add(model);
            }
        }
        return selectSite;
    }

    private void updateUI(MCShareSiteModel model) {
        for (int i = 0; i < this.list.size(); i++) {
            if (((MCShareSiteModel) this.list.get(i)).getSiteId() == model.getSiteId()) {
                this.list.set(i, model);
                break;
            }
        }
        this.adapter.setList(this.list);
        this.mHandler.post(new Runnable() {
            public void run() {
                MCShareActivity.this.adapter.notifyDataSetChanged();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1) {
            updateUI((MCShareSiteModel) data.getSerializableExtra(MCShareIntentConstant.MC_SHARE_SITE_MODEL));
        }
    }

    public void update(MCShareSiteModel siteModel) {
        updateUI(siteModel);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.getShareSiteTask != null) {
            this.getShareSiteTask.cancel(true);
        }
        if (this.shareInfoTask != null) {
            this.shareInfoTask.cancel(true);
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
        }
    }
}
