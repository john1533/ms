package com.mobcent.discuz.module.topic.detail.fragment.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.PostsModel;
import com.mobcent.discuz.android.model.ReplyModel;
import com.mobcent.discuz.android.model.TopicContentModel;
import com.mobcent.discuz.android.model.UserInfoModel;
import com.mobcent.discuz.android.service.UserService;
import com.mobcent.discuz.android.service.impl.UserServiceImpl;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseMiddleHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BasePostsGroupHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BasePostsLastHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseReplyGroupHolder;
import com.mobcent.discuz.module.topic.detail.fragment.adapter.holder.BaseReplyLastHolder;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.base.utils.MCResource;
import java.util.ArrayList;
import java.util.List;

public abstract class TopicDetailBaseAdapter extends BaseExpandableListAdapter {
    protected String TAG;
    protected MCAsyncTaskLoaderImage asyncTaskLoaderImage;
    protected List<UserInfoModel> atUserList;
    protected long boardId;
    protected String boardName;
    protected Context context;
    protected long currentUserId;
    protected List<Object> detailList;
    protected LayoutInflater inflater;
    protected ClickListener listener;
    protected Handler mHandler = new Handler();
    protected PostsModel postsModel;
    protected ArrayList<RichImageModel> replyRichList;
    protected MCResource resource;
    protected SharedPreferencesDB sDb;
    protected ArrayList<RichImageModel> topicRichList;
    protected UserService userService;

    public interface ClickListener {
        void authorClick(boolean z, long j);
    }

    protected abstract String currStyle();

    public abstract ArrayList<RichImageModel> getAllImageUrl(List<Object> list, boolean z);

    public abstract String getContentModelLayoutName();

    public abstract String getPostsGroupLayoutName();

    public abstract String getPostsLastLayoutName();

    public abstract String getReplyGroupLayoutName();

    public abstract String getReplyLastLayoutName();

    public abstract void initContentModelView(View view, BaseMiddleHolder baseMiddleHolder);

    public abstract void initPostsGroupView(View view, BasePostsGroupHolder basePostsGroupHolder);

    public abstract void initPostsLastView(View view, BasePostsLastHolder basePostsLastHolder);

    public abstract void initReplyGroupView(View view, BaseReplyGroupHolder baseReplyGroupHolder);

    public abstract void initReplyLastView(View view, BaseReplyLastHolder baseReplyLastHolder);

    public abstract void onDestroy();

    public abstract void share(int i);

    public abstract void updateContentModelView(BaseMiddleHolder baseMiddleHolder, TopicContentModel topicContentModel, boolean z);

    public abstract void updatePostsGroupView(BasePostsGroupHolder basePostsGroupHolder, PostsModel postsModel);

    public abstract void updatePostsLastView(BasePostsLastHolder basePostsLastHolder, PostsModel postsModel);

    public abstract void updateReplyGroupView(BaseReplyGroupHolder baseReplyGroupHolder, ReplyModel replyModel);

    public abstract void updateReplyLastView(BaseReplyLastHolder baseReplyLastHolder, ReplyModel replyModel);

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public TopicDetailBaseAdapter(Context context, List<Object> detailList) {
        this.context = context;
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.inflater = LayoutInflater.from(context.getApplicationContext());
        this.detailList = detailList;
        this.asyncTaskLoaderImage = MCAsyncTaskLoaderImage.getInstance(context.getApplicationContext());
        this.sDb = SharedPreferencesDB.getInstance(context.getApplicationContext());
        this.currentUserId = this.sDb.getUserId();
        this.userService = new UserServiceImpl(context.getApplicationContext());
        this.TAG = "BaseTopicDetailFragmentAdapter";
        this.topicRichList = new ArrayList();
        this.replyRichList = new ArrayList();
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public void setDetailList(List<Object> detailList) {
        this.detailList = detailList;
    }

    public void setAtUserList(List<UserInfoModel> atUserList) {
        this.atUserList = atUserList;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (!dataValid(groupPosition, this.detailList)) {
            return null;
        }
        if (this.detailList.get(groupPosition) instanceof PostsModel) {
            PostsModel postsModel = (PostsModel) this.detailList.get(groupPosition);
            if (dataValid(childPosition, postsModel.getContent())) {
                return (TopicContentModel) postsModel.getContent().get(childPosition);
            }
            return null;
        } else if (!(this.detailList.get(groupPosition) instanceof ReplyModel)) {
            return null;
        } else {
            ReplyModel replyModel = (ReplyModel) this.detailList.get(groupPosition);
            if (dataValid(childPosition, replyModel.getReplyContent())) {
                return (TopicContentModel) replyModel.getReplyContent().get(childPosition);
            }
            return null;
        }
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Object model = getGroup(groupPosition);
        TopicContentModel contentModel = (TopicContentModel) getChild(groupPosition, childPosition);
        if (model == null) {
            return new View(this.context);
        }
        if (model instanceof PostsModel) {
            if (isLastChild) {
                convertView = getPostsLastView(convertView, (PostsModel) model);
            } else {
                convertView = getContentModelView(contentModel, convertView, true);
            }
        } else if (model instanceof ReplyModel) {
            if (isLastChild) {
                convertView = getReplyLastView(convertView, (ReplyModel) model);
            } else {
                convertView = getContentModelView(contentModel, convertView, false);
            }
        }
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        if (!dataValid(groupPosition, this.detailList)) {
            return 0;
        }
        if (this.detailList.get(groupPosition) instanceof PostsModel) {
            return ((PostsModel) this.detailList.get(groupPosition)).getContent().size() + 1;
        }
        if (this.detailList.get(groupPosition) instanceof ReplyModel) {
            return ((ReplyModel) this.detailList.get(groupPosition)).getReplyContent().size() + 1;
        }
        return 0;
    }
    @Override
    public Object getGroup(int groupPosition) {
        if (dataValid(groupPosition, this.detailList)) {
            return this.detailList.get(groupPosition);
        }
        return null;
    }

    private <T> boolean dataValid(int position, List<T> detailList) {
        if (MCListUtils.isEmpty((List) detailList) || position >= detailList.size() || detailList.get(position) == null) {
            return false;
        }
        return true;
    }

    @Override
    public int getGroupCount() {
        return this.detailList.size();
    }
    @Override
    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Object model = getGroup(groupPosition);
        if (model == null) {
            return new View(this.context);
        }
        if (model instanceof PostsModel) {
            this.postsModel = (PostsModel) model;
            convertView = getPostsGroupView(convertView, this.postsModel);
        } else if (model instanceof ReplyModel) {
            convertView = getReplyGroupView(convertView, (ReplyModel)model);
        }
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private View getPostsGroupView(View convertView, PostsModel postsModel) {
        BasePostsGroupHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof BasePostsGroupHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getPostsGroupLayoutName()), null);
            holder = new BasePostsGroupHolder();
            initPostsGroupView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (BasePostsGroupHolder) convertView.getTag();
        }
        updatePostsGroupView(holder, postsModel);
        return convertView;
    }

    private View getReplyGroupView(View convertView, ReplyModel replyModel) {
        BaseReplyGroupHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof BaseReplyGroupHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getReplyGroupLayoutName()), null);
            holder = new BaseReplyGroupHolder();
            initReplyGroupView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (BaseReplyGroupHolder) convertView.getTag();
        }
        updateReplyGroupView(holder, replyModel);
        return convertView;
    }

    private View getReplyLastView(View convertView, ReplyModel replyModel) {
        BaseReplyLastHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof BaseReplyLastHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getReplyLastLayoutName()), null);
            holder = new BaseReplyLastHolder();
            initReplyLastView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (BaseReplyLastHolder) convertView.getTag();
        }
        updateReplyLastView(holder, replyModel);
        return convertView;
    }

    private View getPostsLastView(View convertView, PostsModel postsModel) {
        BasePostsLastHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof BasePostsLastHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getPostsLastLayoutName()), null);
            holder = new BasePostsLastHolder();
            initPostsLastView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (BasePostsLastHolder) convertView.getTag();
        }
        updatePostsLastView(holder, postsModel);
        return convertView;
    }

    private View getContentModelView(TopicContentModel contentModel, View convertView, boolean isTopic) {
        BaseMiddleHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof BaseMiddleHolder)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId(getContentModelLayoutName()), null);
            holder = new BaseMiddleHolder();
            initContentModelView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (BaseMiddleHolder) convertView.getTag();
        }
        if (contentModel == null) {
            return new View(this.context);
        }
        updateContentModelView(holder, contentModel, isTopic);
        return convertView;
    }

    protected View findViewByName(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }
}
