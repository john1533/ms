package com.mobcent.lowest.module.plaza.service.impl;

import android.content.Context;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.service.impl.helper.BaseJsonHelper;
import com.mobcent.lowest.module.plaza.api.SearchRestfulApiRequester;
import com.mobcent.lowest.module.plaza.model.SearchModel;
import com.mobcent.lowest.module.plaza.service.SearchService;
import com.mobcent.lowest.module.plaza.service.impl.helper.SearchServiceImplHelper;
import java.util.ArrayList;
import java.util.List;

public class SearchServiceImpl implements SearchService {
    private String TAG = "SearchServiceImpl";
    private Context context;

    public SearchServiceImpl(Context context) {
        this.context = context;
    }

    public List<SearchModel> getSearchList(int forumId, String forumKey, long userId, int baikeType, int searchMode, String keyWord, int page, int pageSize) {
        String jsonStr = SearchRestfulApiRequester.getSearchList(this.context, forumId, forumKey, userId, baikeType, searchMode, keyWord, page, pageSize);
        List<SearchModel> searchList = SearchServiceImplHelper.parseSearchList(jsonStr);
        if (searchList == null) {
            searchList = new ArrayList();
            String errorCode = BaseJsonHelper.formJsonRS(jsonStr);
            if (!MCStringUtil.isEmpty(errorCode)) {
                SearchModel searchModel = new SearchModel();
                searchModel.setErrorCode(errorCode);
                searchList.add(searchModel);
            }
        }
        return searchList;
    }
}
