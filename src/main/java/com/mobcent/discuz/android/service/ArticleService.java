package com.mobcent.discuz.android.service;

import com.mobcent.discuz.android.model.ArticleModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentModel;
import java.util.List;

public interface ArticleService {
    BaseResultModel<List<CommentModel>> commentList(String str);

    String createCommentJson(String str, long j, String str2, String str3, String str4, String str5, int i);

    String creteCommentListJson(long j, String str, int i, int i2);

    BaseResultModel<ArticleModel> getArticleDetail(long j, int i);
}
