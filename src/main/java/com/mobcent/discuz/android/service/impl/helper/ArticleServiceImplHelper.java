package com.mobcent.discuz.android.service.impl.helper;

import android.content.Context;
import com.mobcent.discuz.android.constant.ArticleConstant;
import com.mobcent.discuz.android.constant.BaseApiConstant;
import com.mobcent.discuz.android.model.ArticleModel;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.CommentModel;
import com.mobcent.discuz.android.model.ContentModel;
import com.mobcent.discuz.android.model.ManagePanelModel;
import com.mobcent.discuz.android.model.SoundModel;
import com.mobcent.lowest.base.utils.MCResource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArticleServiceImplHelper implements ArticleConstant {
    public static String createArticJson(long aid, int page) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("aid", aid);
            jsonObject.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static BaseResultModel<ArticleModel> getArticleDetail(Context context, String jsonStr) {
        BaseResultModel<ArticleModel> baseResultModel = new BaseResultModel();
        ArticleModel articleModel = new ArticleModel();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            BaseResultModel<Object> baseModel = BaseJsonHelper.formJsonRs(jsonStr, context);
            baseResultModel.setErrorInfo(baseModel.getErrorInfo());
            baseResultModel.setErrorCode(baseModel.getErrorCode());
            if (baseModel.getRs() != 1) {
                return baseResultModel;
            }
            JSONObject articleObject = jsonObject.optJSONObject("body").optJSONObject(ArticleConstant.NEWS_INFO);
            articleModel.setCatName(articleObject.optString(ArticleConstant.CAT_NEMA));
            articleModel.setTitle(articleObject.optString("title"));
            articleModel.setDateline(articleObject.optString("dateline"));
            articleModel.setAuthor(articleObject.optString("author"));
            articleModel.setViewNum(articleObject.optInt(ArticleConstant.VIEW_NUM));
            articleModel.setCommentNum(articleObject.optInt(ArticleConstant.COMMENT_NUM));
            articleModel.setAllowComment(articleObject.optInt("allowComment"));
            articleModel.setSummary(articleObject.optString("summary"));
            articleModel.setPageCount(articleObject.optInt(ArticleConstant.PAGE_COUNT));
            articleModel.setFrom(articleObject.optString(ArticleConstant.FROM));
            articleModel.setAriticleUrl(articleObject.optString(ArticleConstant.ARTICLE_URL));
            articleModel.setContentList(getArticleContent(articleObject.optString("content"), MCResource.getInstance(context).getString("mc_discuz_base_request_url"), articleModel));
            baseResultModel.setData(articleModel);
            return baseResultModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ContentModel> getArticleContent(String jsonStr, String baseURL, ArticleModel articelModel) {
        List<ContentModel> list = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            int j = jsonArray.length();
            for (int i = 0; i < j; i++) {
                JSONObject jobj = jsonArray.optJSONObject(i);
                ContentModel aeticleContent = new ContentModel();
                aeticleContent.setType(jobj.optString("type"));
                aeticleContent.setContent(jobj.optString("content").trim());
                if (aeticleContent.getType().equals("image")) {
                    JSONObject object = jobj.optJSONObject(ArticleConstant.EXTRAINFO);
                    if (object.optString("source").indexOf("http") > -1) {
                        aeticleContent.setSource(object.optString("source"));
                    } else {
                        aeticleContent.setSource(new StringBuilder(String.valueOf(baseURL)).append(object.optString("source")).toString());
                    }
                } else if (aeticleContent.getType().equals("audio")) {
                    SoundModel soundModel = new SoundModel();
                    soundModel.setSoundPath(jobj.optString("content").trim());
                    aeticleContent.setSoundModel(soundModel);
                }
                list.add(aeticleContent);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createCommentJson(String action, long id, String idType, String content, String splitChar, String tagImg, long quoteCommentId){
        JSONObject jsonObject = new JSONObject();


        try{
            jsonObject.put("action", action);
            jsonObject.put("id", id);
            jsonObject.put("idType", idType);
            JSONArray jsonArray = new JSONArray();
            String[] s = content.split(splitChar);
            for (int i = 0; i < s.length; i++) {
                JSONObject jsonObj = new JSONObject();
                if (s[i].indexOf(tagImg) > -1) {
                    jsonObj.put("type", 1);
                    jsonObj.put("infor", s[i].substring(1, s[i].length()));
                } else {
                    jsonObj.put("type", 0);
                    jsonObj.put("infor", URLEncoder.encode(s[i].toString(), "utf-8").replaceAll("\\+", "%20"));

                }
                jsonArray.put(jsonObj);
            }
            jsonObject.put("content", jsonArray);
            jsonObject.put(ArticleConstant.QUOTE_COMMENT_ID, quoteCommentId);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static String createCommentListJson(long id, String idType, int page, int pageSize) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("idType", idType);
            jsonObject.put("page", page);
            jsonObject.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static BaseResultModel<List<CommentModel>> getCommentList(String json) {
        JSONException e;
        BaseResultModel<List<CommentModel>> baseResultList = new BaseResultModel();
        try {
            JSONObject jsonObject = new JSONObject(json);
            List<CommentModel> commentList = new ArrayList();
            try {
                int rs = jsonObject.optInt("rs");
                String errorCode = jsonObject.optString("errcode");
                String errorInfo = jsonObject.optString("errInfo");
                JSONObject bodyObject = jsonObject.optJSONObject("body");
                JSONArray jsonArray = bodyObject.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CommentModel commentModel = new CommentModel();
                        JSONObject replyObject = jsonArray.getJSONObject(i);
                        commentModel.setUserNickName(replyObject.optString("username"));
                        commentModel.setCommentUserId(replyObject.optInt("uid"));
                        commentModel.setCommentPostsId(replyObject.optInt("id"));
                        commentModel.setIcon(replyObject.optString("avatar"));
                        commentModel.setTime(replyObject.optString("time"));
                        JSONArray contentArray = replyObject.getJSONArray("content");
                        List<ContentModel> list = new ArrayList();
                        for (int j = 0; j < contentArray.length(); j++) {
                            JSONObject jobj = contentArray.optJSONObject(j);
                            ContentModel topicContent = new ContentModel();
                            topicContent.setIdType(jobj.optInt("type"));
                            topicContent.setContent(jobj.optString("infor").trim());
                            list.add(topicContent);
                        }
                        commentModel.setContentList(list);
                        commentList.add(commentModel);
                        commentModel.setManagePanelModelList(getManagePanelModelList(replyObject.optJSONArray("managePanel")));
                    }
                }
                baseResultList.setData(commentList);
                baseResultList.setHasNext(bodyObject.optInt(BaseApiConstant.HAS_NEXT));
                baseResultList.setErrorCode(errorCode);
                baseResultList.setErrorInfo(errorInfo);
                baseResultList.setRs(rs);
//                List<CommentModel> list2 = commentList;
            } catch (JSONException e2) {
                e = e2;
//                list2 = commentList;
                e.printStackTrace();
                return baseResultList;
            }
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return baseResultList;
        }
        return baseResultList;
    }

    public static List<ManagePanelModel> getManagePanelModelList(JSONArray optJSONArray) {
        List<ManagePanelModel> managePanelModels = new ArrayList();
        int j = optJSONArray.length();
        for (int i = 0; i < j; i++) {
            JSONObject jsonObject = optJSONArray.optJSONObject(i);
            ManagePanelModel managePanelModel = new ManagePanelModel();
            managePanelModel.setAction(jsonObject.optString("action").trim());
            managePanelModel.setTitle(jsonObject.optString("title"));
            managePanelModel.setType(jsonObject.optString("type"));
            managePanelModels.add(managePanelModel);
        }
        return managePanelModels;
    }
}
