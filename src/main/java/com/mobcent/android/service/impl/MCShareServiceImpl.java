package com.mobcent.android.service.impl;

import android.content.Context;
import com.mobcent.android.api.MCShareRestfulApiRequester;
import com.mobcent.android.db.MCShareDBUtil;
import com.mobcent.android.db.MCShareSharedPreferencesDB;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.android.service.MCShareService;
import com.mobcent.android.service.impl.helper.MCShareServiceImplHelper;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.mobcent.lowest.module.ad.constant.AdApiConstant;
import java.util.ArrayList;
import java.util.List;

public class MCShareServiceImpl implements MCShareService {
    private Context context;

    public MCShareServiceImpl(Context context) {
        this.context = context;
    }

    public List<MCShareSiteModel> getAllSitesByLocal(String appKey, String domainUrl, String lan, String cty) {
        String jsonStr;
        try {
            jsonStr = MCShareDBUtil.getInstance(this.context).getLocalSite();
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = null;
        }
        if (MCStringUtil.isEmpty(jsonStr)) {
            return getAllSitesByNet(appKey, domainUrl, lan, cty);
        }
        return MCShareServiceImplHelper.formSiteModels(jsonStr);
    }

    public List<MCShareSiteModel> getAllSites(String appKey, String domainUrl, String lan, String cty) {
        List<MCShareSiteModel> list = getAllSitesByNet(appKey, domainUrl, lan, cty);
        if (list != null && list.size() > 0) {
            return list;
        }
        String jsonStr;
        try {
            jsonStr = MCShareDBUtil.getInstance(this.context).getLocalSite();
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = null;
        }
        if (MCStringUtil.isEmpty(jsonStr)) {
            return list;
        }
        return MCShareServiceImplHelper.formSiteModels(jsonStr);
    }

    public List<MCShareSiteModel> getAllSitesByNet(String appKey, String domainUrl, String lan, String cty) {
        String jsonStr = MCShareRestfulApiRequester.getAllSites(appKey, domainUrl, lan, cty, this.context);
        List<MCShareSiteModel> list = MCShareServiceImplHelper.formSiteModels(jsonStr);
        if (list == null || list.isEmpty()) {
            list = new ArrayList();
            String rs = MCShareServiceImplHelper.formJsonRS(jsonStr);
            MCShareSiteModel model = new MCShareSiteModel();
            model.setRsReason(rs);
            list.add(model);
            return list;
        }
        MCShareDBUtil.getInstance(this.context).addOrUpdateSite(lan, cty, jsonStr);
        String ids = MCShareSharedPreferencesDB.getInstance(this.context).getSelectedSiteIds();
        if (ids.equals("")) {
            return list;
        }
        String[] id = ids.split(AdApiConstant.RES_SPLIT_COMMA);
        if (id.length <= 0) {
            return list;
        }
        for (int i = 0; i < list.size(); i++) {
            if (((MCShareSiteModel) list.get(i)).getBindState() == 1) {
                for (String parseInt : id) {
                    if (((MCShareSiteModel) list.get(i)).getSiteId() == Integer.parseInt(parseInt)) {
                        ((MCShareSiteModel) list.get(i)).setBindState(1);
                        break;
                    }
                    ((MCShareSiteModel) list.get(i)).setBindState(2);
                }
            }
        }
        return list;
    }

    public boolean unbindSite(long userId, int siteId, String appKey, String domainUrl) {
        return MCShareServiceImplHelper.isUnbindSucc(MCShareRestfulApiRequester.unbindSite(userId, siteId, appKey, domainUrl, this.context));
    }

    public String uploadImage(long userId, String uploadFile, String appKey, String domainUrl) {
        return MCShareServiceImplHelper.formUploadImageJson(MCShareRestfulApiRequester.uploadShareImage(userId, uploadFile, appKey, domainUrl, this.context));
    }

    public String shareInfo(long userId, String content, String picPath, String ids, String shareUrl, String appKey, String domainUrl) {
        return MCShareServiceImplHelper.formJsonRS(MCShareRestfulApiRequester.shareInfo(userId, content, picPath, ids, shareUrl, appKey, domainUrl, this.context));
    }

    public void shareLog(String appKey, String openPlatType, String domainUrl, Context context) {
        MCShareRestfulApiRequester.shareLog(appKey, openPlatType, domainUrl, context);
    }
}
