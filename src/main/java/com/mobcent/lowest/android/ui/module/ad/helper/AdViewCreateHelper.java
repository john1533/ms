package com.mobcent.lowest.android.ui.module.ad.helper;

import android.content.Context;
import android.view.View;
import com.mobcent.lowest.android.ui.module.ad.widget.BigPicTypeView;
import com.mobcent.lowest.android.ui.module.ad.widget.LinkTextView;
import com.mobcent.lowest.android.ui.module.ad.widget.MultiTypeView;
import com.mobcent.lowest.android.ui.module.ad.widget.PicAndTextView;
import com.mobcent.lowest.android.ui.module.ad.widget.SearchPicAndTextView;
import com.mobcent.lowest.android.ui.module.ad.widget.TextTypeView;
import com.mobcent.lowest.module.ad.constant.AdConstant;
import com.mobcent.lowest.module.ad.model.AdContainerModel;

public class AdViewCreateHelper implements AdConstant {
    public static View createAdView(Context context, AdContainerModel adContainerModel) {
        View view = null;
        switch (adContainerModel.getDtType()) {
            case 1:
                view = createMulityView(context, adContainerModel);
                break;
            case 2:
                view = createTwoShortTextView(context, adContainerModel);
                break;
            case 3:
                view = createBigImgView(context, adContainerModel);
                break;
            case 4:
                view = createLinkView(context, adContainerModel);
                break;
            case 5:
                view = createPicAndTextView(context, adContainerModel);
                break;
            case 6:
                view = createSearchPicAndTextView(context, adContainerModel);
                break;
            case 7:
                if (adContainerModel.getType() != 11) {
                    if (adContainerModel.getType() == 12) {
                        view = createPicAndTextView(context, adContainerModel);
                        break;
                    }
                }
                view = createLinkView(context, adContainerModel);
                break;
        }
        if (view != null) {
            AdViewHelper.getInstance(context).exposureAd(adContainerModel);
        }
        return view;
    }

    private static View createMulityView(Context context, AdContainerModel adContainerModel) {
        if (adContainerModel == null) {
            return null;
        }
        MultiTypeView miltiView = new MultiTypeView(context);
        miltiView.setAdContainerModel(adContainerModel);
        return miltiView;
    }

    private static View createBigImgView(Context context, AdContainerModel adContainerModel) {
        if (adContainerModel == null) {
            return null;
        }
        BigPicTypeView bigImg = new BigPicTypeView(context);
        bigImg.setAdContainerModel(adContainerModel);
        return bigImg;
    }

    private static View createTwoShortTextView(Context context, AdContainerModel adContainerModel) {
        if (adContainerModel == null) {
            return null;
        }
        TextTypeView twoShortText = new TextTypeView(context);
        twoShortText.setAdContainerModel(adContainerModel);
        return twoShortText;
    }

    private static View createLinkView(Context context, AdContainerModel adContainerModel) {
        if (adContainerModel == null) {
            return null;
        }
        LinkTextView linkContent = new LinkTextView(context);
        linkContent.setAdContainerModel(adContainerModel);
        return linkContent;
    }

    private static View createPicAndTextView(Context context, AdContainerModel adContainerModel) {
        if (adContainerModel == null) {
            return null;
        }
        PicAndTextView picTextView = new PicAndTextView(context);
        picTextView.setAdContainerModel(adContainerModel);
        return picTextView;
    }

    private static View createSearchPicAndTextView(Context context, AdContainerModel adContainerModel) {
        if (adContainerModel == null) {
            return null;
        }
        SearchPicAndTextView searchItem = new SearchPicAndTextView(context);
        searchItem.setAdContainerModel(adContainerModel);
        return searchItem;
    }
}
