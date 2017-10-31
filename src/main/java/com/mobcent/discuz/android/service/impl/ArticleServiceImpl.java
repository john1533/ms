package com.mobcent.discuz.android.service.impl;

import android.content.Context;
import com.mobcent.discuz.android.api.ArticleRestfulApiRequester;
import com.mobcent.discuz.android.model.ArticleModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentModel;
import com.mobcent.discuz.android.service.ArticleService;
import com.mobcent.discuz.android.service.impl.helper.ArticleServiceImplHelper;
import com.mobcent.discuz.android.service.impl.helper.BaseJsonHelper;
import java.util.List;

public class ArticleServiceImpl implements ArticleService {
    private Context context;

    public ArticleServiceImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    public BaseResultModel<ArticleModel> getArticleDetail(long aid, int page) {
        return ArticleServiceImplHelper.getArticleDetail(this.context, ArticleRestfulApiRequester.getArticleDetail(this.context, ArticleServiceImplHelper.createArticJson(aid, page)));
    }

    public BaseResultModel<Object> commentArticle(String json) {
        return BaseJsonHelper.formJsonRs(ArticleRestfulApiRequester.commentArticle(this.context, json), this.context);
    }

    public String createCommentJson(String action, long aid, String idType, String content, String splitChar, String tagImg, int quoteCommentId) {
        return ArticleServiceImplHelper.createCommentJson(action, aid, idType, content, splitChar, tagImg, (long) quoteCommentId);
    }

    public String creteCommentListJson(long id, String idType, int page, int pageSize) {
        return ArticleServiceImplHelper.createCommentListJson(id, idType, page, pageSize);
    }

    public BaseResultModel<List<CommentModel>> commentList(String json) {
        return ArticleServiceImplHelper.getCommentList(ArticleRestfulApiRequester.getArtileCommentList(this.context, json));
    }
}
