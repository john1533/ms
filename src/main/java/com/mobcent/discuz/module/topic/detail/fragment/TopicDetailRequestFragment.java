package com.mobcent.discuz.module.topic.detail.fragment;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.utils.DZToastAlertUtils;
import com.mobcent.discuz.activity.view.MCMultiBottomView;
import com.mobcent.discuz.activity.view.MCMultiBottomView.RecordModel;
import com.mobcent.discuz.activity.view.MCMultiBottomView.SendModel;
import com.mobcent.discuz.activity.view.MCPopupListView.PopupModel;
import com.mobcent.discuz.android.model.BaseModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.PermissionModel;
import com.mobcent.discuz.android.model.PictureModel;
import com.mobcent.discuz.android.model.PostsModel;
import com.mobcent.discuz.android.model.ReplyModel;
import com.mobcent.discuz.android.model.TopicContentModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.model.UploadPictureModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.DraftService;
import com.mobcent.discuz.android.service.PostsService;
import com.mobcent.discuz.android.service.impl.DraftServiceImpl;
import com.mobcent.discuz.base.delegate.SlideDelegate;
import com.mobcent.discuz.base.fragment.BaseFragment;
import com.mobcent.discuz.base.model.TopSettingModel;
import com.mobcent.discuz.module.publish.fragment.activity.ReplyTopicActivity;
import com.mobcent.discuz.module.publish.fragment.activity.ReplyTopicActivity.ReplyRetrunDelegate;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.TopicDetail2FragmentAdapter;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.TopicDetail3FragmentAdapter;
import com.mobcent.lowest.android.ui.module.ad.widget.AdView;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.widget.PullToRefreshExpandableListView;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.base.utils.MCToastUtils;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import com.mobcent.lowest.module.ad.manager.AdManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TopicDetailRequestFragment extends BaseFragment implements FinalConstant, IntentConstant, ReplyRetrunDelegate, SlideDelegate {
    protected int MORE = 2;
    protected int SHARE = 1;
    protected String TAG;
    protected AdView adView;
    protected TopicDetail2FragmentAdapter adapter2;
    protected TopicDetail3FragmentAdapter adapter3;
    protected String ascCheckStr;
    protected List<UserInfoModel> atUserList;
    protected long authorId;
    protected long boardId;
    protected String boardName;
    protected MCMultiBottomView bottomView;
    protected Builder builder;
    protected String cancelFavorStr;
    protected ConfigComponentModel componentModel;
    protected String copyUrlStr;
    protected int currentPage = 1;
    protected String descCheckStr;
    protected List<Object> detailList;
    protected TopicDraftModel draftModel;
    protected DraftService draftService;
    protected String favorTopicStr;
    protected FavoriteAsyncTask favoriteAsyncTask;
    protected String higherReplyStr;
    protected PermissionModel isCheckedPermissionModel;
    protected boolean isRequestEdit;
    protected PullToRefreshExpandableListView listView;
    protected LoadDataAsyncTask loadDataAsyncTask;
    protected String openBrowserStr;
    protected int pageCount = 0;
    protected int pageSize = 10;
    protected List<PopupModel> popupList;
    protected PostsModel postsModel;
    protected PostsService postsService;
    protected int showState;
    protected String skipPageStr;
    protected TopSettingModel topSettingModel;
    protected long topicId;
    public boolean topicIsExist = false;
    protected int totalNum;
    protected String nightStr;
    protected String dayStr;


    protected class FavoriteAsyncTask extends AsyncTask<Void, Void, BaseResultModel<Object>> {
        private String type;

        public FavoriteAsyncTask(String type) {
            this.type = type;
        }

        protected BaseResultModel<Object> doInBackground(Void... params) {
            return TopicDetailRequestFragment.this.postsService.favoriteTopic(TopicDetailRequestFragment.this.topicId, "tid", this.type);
        }

        protected void onPostExecute(BaseResultModel<Object> result) {
            DZToastAlertUtils.toast(TopicDetailRequestFragment.this.activity.getApplicationContext(), result);
            if (result.getRs() == 0) {
                if (MCStringUtil.isEmpty(result.getErrorInfo())) {
                    String toastStr = "";
                    if (FinalConstant.DEL_FAVORITE_ACTION.equals(this.type)) {
                        toastStr = TopicDetailRequestFragment.this.resource.getString("mc_forum_cancel_favor_fail");
                    } else if ("favorite".equals(this.type)) {
                        toastStr = TopicDetailRequestFragment.this.resource.getString("mc_forum_favor_fail");
                    }
                    if (!MCStringUtil.isEmpty(toastStr)) {
                        MCToastUtils.toast(TopicDetailRequestFragment.this.activity, toastStr);
                        return;
                    }
                    return;
                }
                MCToastUtils.toast(TopicDetailRequestFragment.this.activity, result.getErrorInfo());
            } else if (FinalConstant.DEL_FAVORITE_ACTION.equals(this.type)) {
                if (TopicDetailRequestFragment.this.postsModel != null) {
                    TopicDetailRequestFragment.this.postsModel.setIsFavor(0);
                }
            } else if ("favorite".equals(this.type) && TopicDetailRequestFragment.this.postsModel != null) {
                TopicDetailRequestFragment.this.postsModel.setIsFavor(1);
            }
        }
    }

    protected class LoadDataAsyncTask extends AsyncTask<Void, Void, BaseResultModel<List<Object>>> {
        private int requestId;

        public LoadDataAsyncTask(int requestId) {
            this.requestId = requestId;
        }

        protected void onPreExecute() {
            TopicDetailRequestFragment.this.topicIsExist = false;
            switch (this.requestId) {
                case 1:
                    TopicDetailRequestFragment.this.currentPage = 1;
                    AdManager.getInstance().recyclAdContainerByTag(TopicDetailRequestFragment.this.TAG);
                    AdManager.getInstance().recyclAdByTag(TopicDetailRequestFragment.this.TAG);
                    if (TopicDetailRequestFragment.this.adView != null) {
                        TopicDetailRequestFragment.this.adView.showAd(Integer.valueOf(TopicDetailRequestFragment.this.resource.getString("mc_forum_posts_detail_top_position")).intValue());
                        return;
                    }
                    return;
                case 2:
                    TopicDetailRequestFragment topicDetailRequestFragment = TopicDetailRequestFragment.this;
                    topicDetailRequestFragment.currentPage++;
                    return;
                case 3:
                    return;
                default:
                    TopicDetailRequestFragment.this.currentPage = 1;
                    return;
            }
        }

        protected BaseResultModel<List<Object>> doInBackground(Void... arg0) {
            return TopicDetailRequestFragment.this.loadData();
        }

        protected void onPostExecute(BaseResultModel<List<Object>> result) {
            if (this.requestId == 1) {
                TopicDetailRequestFragment.this.listView.onRefreshComplete();
            }
            TopicDetailRequestFragment.this.dealData(result);
            if (result.getData() != null) {
                TopicDetailRequestFragment.this.topicIsExist = true;
                TopicDetailRequestFragment.this.bottomView.setSendPerm(TopicDetailRequestFragment.this.topicIsExist);
                if (this.requestId == 3) {
                    TopicDetailRequestFragment.this.detailList.clear();
                    TopicDetailRequestFragment.this.detailList.addAll((Collection) result.getData());
                } else if (this.requestId == 1) {
                    if (!((List) result.getData()).isEmpty() && (((List) result.getData()).get(0) instanceof PostsModel)) {
                        TopicDetailRequestFragment.this.postsModel = (PostsModel) ((List) result.getData()).get(0);
                        if (!MCListUtils.isEmpty(TopicDetailRequestFragment.this.postsModel.getContent())) {
                            TopicContentModel adContent = new TopicContentModel();
                            adContent.setType(-1);
                            TopicDetailRequestFragment.this.postsModel.getContent().add(adContent);
                        }
                        if (!MCStringUtil.isEmpty(TopicDetailRequestFragment.this.postsModel.getBoardName())) {
                            TopicDetailRequestFragment.this.boardName = TopicDetailRequestFragment.this.postsModel.getBoardName();
                            try {
                                TopicDetailRequestFragment.this.topSettingModel.title = TopicDetailRequestFragment.this.boardName;
                                TopicDetailRequestFragment.this.getTopBarHelper().dealTopBar(TopicDetailRequestFragment.this.topSettingModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    TopicDetailRequestFragment.this.detailList.clear();
                    TopicDetailRequestFragment.this.detailList.addAll((Collection) result.getData());
                } else if (this.requestId == 2) {
                    TopicDetailRequestFragment.this.detailList.addAll((Collection) result.getData());
                }
                TopicDetailRequestFragment.this.nofityAdapter();
                TopicDetailRequestFragment.this.expandAllView();
                TopicDetailRequestFragment.this.atUserList = TopicDetailRequestFragment.this.getAtUserList(TopicDetailRequestFragment.this.detailList);
                TopicDetailRequestFragment.this.setAtUserList(TopicDetailRequestFragment.this.atUserList);
                TopicDetailRequestFragment.this.totalNum = result.getTotalNum();
                if (((List) result.getData()).size() + ((TopicDetailRequestFragment.this.currentPage - 1) * TopicDetailRequestFragment.this.pageSize) < TopicDetailRequestFragment.this.totalNum) {
                    TopicDetailRequestFragment.this.listView.onBottomRefreshComplete(0);
                } else {
                    TopicDetailRequestFragment.this.listView.onBottomRefreshComplete(3, TopicDetailRequestFragment.this.resource.getString("mc_forum_detail_load_finish"));
                }
                if (this.requestId == 1) {
                    TopicDetailRequestFragment.this.listView.setSelection(0);
                }
            }
        }
    }

    protected abstract void expandAllView();

    protected abstract void nofityAdapter();

    protected abstract void setAtUserList(List<UserInfoModel> list);

    public List<UserInfoModel> getAtUserList() {
        return this.atUserList;
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.topicId = getBundle().getLong("topicId", 0);
        this.draftService = new DraftServiceImpl(this.activity.getApplicationContext());
        this.draftModel = (TopicDraftModel) getBundle().getSerializable(IntentConstant.INTENT_TOPIC_DRAFMODEL);
        if (this.draftModel == null) {
            this.draftModel = new TopicDraftModel();
            this.draftModel.setCommonId(this.topicId);
            this.draftModel.setType(2);
            List draftList = this.draftService.queryDraftModel(this.draftModel);
            if (MCListUtils.isEmpty(draftList)) {
                this.draftModel = null;
            } else {
                this.draftModel = (TopicDraftModel) draftList.get(0);
            }
        }
    }

    private void dealData(BaseResultModel<List<Object>> result) {
        this.listView.onBottomRefreshComplete(3);
        DZToastAlertUtils.toast(this.activity.getApplicationContext(), result);
    }

    private List<UserInfoModel> getAtUserList(List<Object> detailList) {
        List<UserInfoModel> userlist = new ArrayList();
        if (!MCListUtils.isEmpty((List) detailList)) {
            for (Object object :  detailList) {
                UserInfoModel user = new UserInfoModel();
                if (object instanceof PostsModel) {
                    PostsModel postsModel = (PostsModel) object;
                    user.setNickname(postsModel.getUserNickName());
                    user.setUserId(postsModel.getUserId());
                } else if (object instanceof ReplyModel) {
                    ReplyModel reply = (ReplyModel) object;
                    user.setNickname(reply.getReplyName());
                    user.setUserId(reply.getReplyUserId());
                }
                if (!isContainUser(userlist, user)) {
                    userlist.add(user);
                }
            }
        }
        return userlist;
    }

    private boolean isContainUser(List<UserInfoModel> userlist, UserInfoModel user) {
        for (UserInfoModel userInfo : userlist) {
            if (userInfo.getUserId() == user.getUserId()) {
                return true;
            }
        }
        return false;
    }

    private BaseResultModel<List<Object>> loadData() {
        return this.postsService.getTopicDetail(this.boardId, this.topicId, this.authorId, this.currentPage, this.pageSize, this.showState);
    }

    protected BaseResultModel<Object> reply(BaseModel baseModel) {
        BaseResultModel<Object> baseResultModel = new BaseResultModel();
        baseResultModel.setRs(0);
        if (baseModel == null) {
            return baseResultModel;
        }
        if (baseModel instanceof SendModel) {
            SendModel sendModel = (SendModel) baseModel;
            if (sendModel == null) {
                return baseResultModel;
            }
            String aid = "";
            List<UploadPictureModel> uploadList = new ArrayList();
            if (!(sendModel.getPicModel() == null || sendModel.getPicModel().getSelectMap() == null || sendModel.getPicModel().getSelectMap().isEmpty())) {
                String[] files = new String[sendModel.getPicModel().getSelectMap().size()];
                int i = 0;
                for (String filePath : sendModel.getPicModel().getSelectMap().keySet()) {
                    files[i] = new String(((PictureModel) sendModel.getPicModel().getSelectMap().get(filePath)).getAbsolutePath());
                    i++;
                }
                BaseResultModel<List<UploadPictureModel>> result = this.postsService.upload(files, "forum", "image", this.boardId, 0, 0);
                if (result.getRs() == 0 || MCListUtils.isEmpty((List) result.getData())) {
                    baseResultModel.setErrorInfo(result.getErrorInfo());
                    saveDraftModel(baseModel);
                    return baseResultModel;
                }
                uploadList.addAll((Collection) result.getData());
                for (int j = 0; j < ((List) result.getData()).size(); j++) {
                    if (j == 0) {
                        aid = ((UploadPictureModel) ((List) result.getData()).get(j)).getAid() + "";
                    } else {
                        aid = aid + AdApiConstant.RES_SPLIT_COMMA + ((UploadPictureModel) ((List) result.getData()).get(j)).getAid();
                    }
                }
            }
            String content = "";
            if (sendModel.getWordModel() != null) {
                content = sendModel.getWordModel().getContent();
            }
            String contentStr = this.postsService.createPublishTopicJson(content, "ß", "á", "", uploadList);
            double longitude = 0.0d;
            double latitude = 0.0d;
            String location = "";
            int requireLocation = 0;
            if (sendModel.getPoiModel() != null) {
                longitude = sendModel.getPoiModel().getLongitude();
                latitude = sendModel.getPoiModel().getLatitude();
                location = sendModel.getPoiModel().getLocationStr();
                if (sendModel.getPoiModel().isOpen()) {
                    requireLocation = 1;
                }
            }
            baseResultModel = this.postsService.publishTopic(this.postsService.createReplyJson(this.boardId, this.topicId, contentStr, 0, false, longitude, latitude, location, requireLocation, aid, this.isCheckedPermissionModel), "reply");
        } else if (baseModel instanceof RecordModel) {
            RecordModel recordModel = (RecordModel) baseModel;
            if (recordModel == null) {
                return baseResultModel;
            }
            BaseResultModel<List<UploadPictureModel>> resultModel = this.postsService.upload(new String[]{recordModel.getAudioPath()}, "pm", "audio", 0, 0, 0);
            if (resultModel == null || resultModel.getRs() != 1 || resultModel.getData() == null || ((List) resultModel.getData()).isEmpty() || ((List) resultModel.getData()).get(0) == null) {
                baseResultModel.setErrorInfo(resultModel.getErrorInfo());
                saveDraftModel(baseModel);
                return baseResultModel;
            }
            recordModel.setAudioPath(((UploadPictureModel) ((List) resultModel.getData()).get(0)).getPicPath());
            baseResultModel = this.postsService.publishTopic(this.postsService.createReplyJson(this.boardId, this.topicId, this.postsService.createPublishTopicJson("", "ß", "á", recordModel.getAudioPath(), new ArrayList()), 0, false, 0.0d, 0.0d, "", 0, "", this.isCheckedPermissionModel), "reply");
        }
        return baseResultModel;
    }

    protected void createReplyView(BaseModel baseModel) {
        if (baseModel != null && this.topicIsExist) {
            ReplyModel replyModel = new ReplyModel();
            replyModel.setReplyName(this.sharedPreferencesDB.getNickName());
            replyModel.setIcon(this.sharedPreferencesDB.getIcon());
            replyModel.setReplyUserId(this.sharedPreferencesDB.getUserId());
            replyModel.setPostsDate(System.currentTimeMillis());
            replyModel.setPosition(-1);
            String locationStr = "";
            List<TopicContentModel> contentList = new ArrayList();
            TopicContentModel contentModel;
            if (baseModel instanceof SendModel) {
                SendModel sendModel = (SendModel) baseModel;
                locationStr = sendModel.getPoiModel() != null ? sendModel.getPoiModel().getLocationStr() : "";
                if (sendModel.getWordModel() != null) {
                    contentModel = new TopicContentModel();
                    contentModel.setType(0);
                    contentModel.setInfor(sendModel.getWordModel().getContent());
                    contentList.add(contentModel);
                }
                if (!(sendModel.getPicModel() == null || sendModel.getPicModel().getSelectMap() == null || sendModel.getPicModel().getSelectMap().isEmpty())) {
                    for (String path : sendModel.getPicModel().getSelectMap().keySet()) {
                        PictureModel pictureModel = (PictureModel) sendModel.getPicModel().getSelectMap().get(path);
                        if (pictureModel != null) {
                            contentModel = new TopicContentModel();
                            contentModel.setType(1);
                            contentModel.setInfor(pictureModel.getAbsolutePath());
                            contentModel.setOriginalInfo(pictureModel.getAbsolutePath());
                            contentList.add(contentModel);
                        }
                    }
                }
            } else if (baseModel instanceof RecordModel) {
                RecordModel recordModel = (RecordModel) baseModel;
                contentModel = new TopicContentModel();
                contentModel.setType(3);
                contentModel.setInfor(recordModel.getAudioPath());
                contentList.add(contentModel);
            }
            replyModel.setLocation(locationStr);
            replyModel.setReplyContent(contentList);
            this.detailList.add(this.detailList.size(), replyModel);
            this.mHandler.post(new Runnable() {
                public void run() {
                    TopicDetailRequestFragment.this.nofityAdapter();
                    TopicDetailRequestFragment.this.expandAllView();
                }
            });
        }
    }

    public void replyReturn() {
        if (this.listView != null) {
            this.listView.onRefresh(false);
        }
    }

    public boolean isSlideFullScreen() {
        if (this.bottomView.getFacePager() == null || this.bottomView.getFacePager().getVisibility() != 0) {
            return true;
        }
        return false;
    }

    protected String getRootLayoutName() {
        return null;
    }

    protected void initViews(View rootView) {
    }

    protected void initActions(View rootView) {
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
        MCAudioUtils.getInstance(this.activity.getApplicationContext()).release();
        saveDraftModel(null);
    }

    protected void restoreView(TopicDraftModel draft) {
        DZFaceUtil.setStrToFace((EditText) findViewByName(this.bottomView, "reply_edit"), this.draftModel.getContent(), this.activity.getApplicationContext());
        Map<String, PictureModel> picMap = new HashMap();
        if (this.bottomView == null || this.bottomView.getPicMap().isEmpty()) {
            if (draft.getPicPath() != null && draft.getPicPath().length > 0) {
                for (int i = 0; i < draft.getPicPath().length; i++) {
                    PictureModel model = new PictureModel();
                    model.setAbsolutePath(draft.getPicPath()[i]);
                    picMap.put(draft.getPicPath()[i], model);
                }
            }
            this.bottomView.setPicMap(picMap);
        } else {
            picMap = this.bottomView.getPicMap();
            String[] tempPicPath = new String[picMap.size()];
            int position = 0;
            for (String path : picMap.keySet()) {
                tempPicPath[position] = path;
                position++;
            }
            this.draftModel.setPicPath(tempPicPath);
        }
        this.bottomView.updateImageUI(picMap);
        this.bottomView.resetEdit();
    }

    public void saveDraftModel(BaseModel baseModel) {
        String content = "";
        String recordPath = "";
        String[] files = null;
        if (baseModel == null) {
            content = ((EditText) findViewByName(this.bottomView, "reply_edit")).getText().toString();
            files = getPicPath(this.bottomView.getPicMap());
        } else if (baseModel instanceof SendModel) {
            SendModel sendModel = (SendModel) baseModel;
            content = sendModel.getWordModel().getContent();
            if (sendModel.getPicModel() != null) {
                files = getPicPath(sendModel.getPicModel().getSelectMap());
            }
        } else if (baseModel instanceof RecordModel) {
            recordPath = ((RecordModel) baseModel).getAudioPath();
        }
        if (!MCStringUtil.isEmpty(content) || ((files != null && files.length > 0) || !MCStringUtil.isEmpty(recordPath))) {
            if (this.draftModel == null) {
                this.draftModel = new TopicDraftModel();
            }
            this.draftModel.setCommonId(this.topicId);
            this.draftModel.setType(2);
            this.draftModel.setTitle(content);
            this.draftModel.setBoardId(this.boardId);
            this.draftModel.setContent(content);
            if (files != null) {
                this.draftModel.setPicPath(files);
            }
            this.draftModel.setVoicePath(recordPath);
            this.draftService.saveDraftModel(this.draftModel);
        }
    }

    protected String[] getPicPath(Map<String, PictureModel> picMap) {
        String[] files = new String[picMap.size()];
        int i = 0;
        for (String key : picMap.keySet()) {
            files[i] = new String(((PictureModel) picMap.get(key)).getAbsolutePath());
            i++;
        }
        return files;
    }

    protected void onRelpyClick(TopicDraftModel model) {
        Intent intent = new Intent(this.activity, ReplyTopicActivity.class);
        intent.putExtra("boardId", this.boardId);
        intent.putExtra("boardName", this.boardName);
        intent.putExtra("topicId", this.topicId);
        intent.putExtra(IntentConstant.INTENT_IS_QUITE, false);
        intent.putExtra(FinalConstant.POSTS_USER_LIST, (ArrayList) this.atUserList);
        intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, model);
        this.activity.startActivity(intent);
    }
}
