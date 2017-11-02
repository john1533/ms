package com.mobcent.discuz.base.dispatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.constant.StyleConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.fragment.CustomPageFragment;
import com.mobcent.discuz.base.fragment.ErrorFragment;
import com.mobcent.discuz.base.fragment.FullPageFragment;
import com.mobcent.discuz.base.fragment.ImgTextPageFragment;
import com.mobcent.discuz.base.fragment.SubPageFragment;
import com.mobcent.discuz.base.fragment.WebViewFragment;
import com.mobcent.discuz.base.helper.DZWebWhiteListHelper;
import com.mobcent.discuz.module.about.fragment.AboutFragment;
import com.mobcent.discuz.module.board.fragment.BoardListFragment1;
import com.mobcent.discuz.module.board.fragment.BoardListFragment2;
import com.mobcent.discuz.module.msg.fragment.SessionList1Fragment;
import com.mobcent.discuz.module.person.activity.fragment.SettingFragment;
import com.mobcent.discuz.module.person.activity.fragment.UserHomeSelfFragment1;
import com.mobcent.discuz.module.person.activity.fragment.UserList1Fragment;
import com.mobcent.discuz.module.plaza.fragment.PlazaFragmentComponent;
import com.mobcent.discuz.module.topic.list.fragment.PortalListFrament;
import com.mobcent.discuz.module.topic.list.fragment.PortalPhotoListFrament;
import com.mobcent.discuz.module.topic.list.fragment.TopicListFragment;
import com.mobcent.lowest.android.ui.module.plaza.activity.model.IntentPlazaNewModel;
import com.mobcent.lowest.android.ui.module.plaza.constant.PlazaConstant;
import com.mobcent.lowest.base.utils.MCListUtils;
import com.mobcent.lowest.module.plaza.model.PlazaAppModel;
import java.util.ArrayList;
import java.util.List;

public class FragmentDispatchHelper implements ConfigConstant, StyleConstant {
    public static Fragment disPatchFragment(ConfigModuleModel dataModel) {
        Fragment c;
        String type = dataModel.getType();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseIntentConstant.BUNDLE_MODULE_MODEL, dataModel);
        if (ConfigConstant.MODULE_TYPE_FULL.equals(type)) {
            c = new FullPageFragment();
        } else if (ConfigConstant.MODULE_TYPE_SUBNAV.equals(type)) {
            c = new SubPageFragment();
        } else if (ConfigConstant.MODULE_TYPE_CUSTOM.equals(type)) {
            c = new CustomPageFragment();
        } else if (MODULE_TYPE_IMG_TEXT.equals(type)) {
            c = new ImgTextPageFragment();
        } else {
            c = new ErrorFragment();
        }
        c.setArguments(bundle);
        return c;
    }

    public static Fragment disPatchFragment(ConfigComponentModel dataModel) {
        Fragment fragment = null;
        Bundle bundleData = new Bundle();
        if (dataModel == null) {
            return new ErrorFragment();
        }
        bundleData.putSerializable(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, dataModel);
        String type = dataModel.getType();
        String style = dataModel.getStyle();
        if (type.equals(ConfigConstant.COMPONENT_MODULEREF)) {
            ConfigModuleModel moduleModel = DiscuzApplication._instance.getModuleModel(dataModel.getModuleId());
            if (moduleModel != null) {
                moduleModel = moduleModel.cloneModule();
                moduleModel.setComponent(true);
                return disPatchFragment(moduleModel);
            }
        } else if (type.equals(ConfigConstant.COMPONENT_USERINFO)) {
            fragment = new UserHomeSelfFragment1();
            BaseResultModel<ConfigModel> config = DiscuzApplication._instance.getConfigModel();
            if (!(config == null || config.getData() == null)) {
                bundleData.putBoolean(BaseIntentConstant.BUNDLE_IS_SHOW_MESSAGELIST, ((ConfigModel) config.getData()).isShowMessageList());
            }
        } else if (type.equals(ConfigConstant.COMPONENT_NEWSLIST)) {
            fragment = (StyleConstant.STYLE_IMAGE2.equals(style) || "image".equals(style)) ? new PortalPhotoListFrament() : new PortalListFrament();
        } else if (type.equals(ConfigConstant.COMPONENT_FORUMLIST)) {
            fragment = "flat".equals(style) ? new BoardListFragment1() : new BoardListFragment2();
        } else if (type.equals(ConfigConstant.COMPONENT_TOPICLIST_SIMPLE)) {
            fragment = new TopicListFragment();
        } else if (type.equals(ConfigConstant.COMPONENT_TOPICLIST)) {
            fragment = new TopicListFragment();
        } else if (type.equals(ConfigConstant.COMPONENT_MESSAGELIST)) {
            fragment = new SessionList1Fragment();
        } else if (type.equals(ConfigConstant.COMPONENT_SURROUDING_POST_LIST)) {
            fragment = new TopicListFragment();
            bundleData.putString(PostsConstant.TOPIC_TYPE, PostsConstant.TOPIC_TYPE_SURROUND);
        } else if (type.equals(ConfigConstant.COMPONENT_USER_LIST)) {
            fragment = new UserList1Fragment();
            bundleData.putString("userType", dataModel.getFilter());
            bundleData.putString("orderBy", dataModel.getOrderby());
        } else if (type.equals(ConfigConstant.COMPONENT_WEBAPP)) {
            if (DiscuzApplication._instance.isPayed() || DZWebWhiteListHelper.isInWhiteList(dataModel.getRedirect())) {
                fragment = new WebViewFragment();
            } else {
                fragment = new ErrorFragment();
                bundleData.putString("error", "");
            }
        } else if (type.equals(ConfigConstant.COMPONENT_DISCOVER)) {
//            fragment = new PlazaFragmentComponent();
//            if (dataModel instanceof ConfigComponentModel) {
//                dealPlazaBundle(bundleData, dataModel, fragment);
//            }
            fragment = new com.mobcent.lowest.android.ui.module.plaza.fragment.SettingFragment();
        } else if (type.equals("setting")) {
            fragment = new SettingFragment();
        } else if (type.equals(ConfigConstant.COMPONENT_ABOUT)) {
            fragment = new AboutFragment();
        }
        if (fragment == null) {
            fragment = new ErrorFragment();
        }
        if ((fragment instanceof ErrorFragment) && !ConfigConstant.COMPONENT_WEBAPP.equals(type)) {
            bundleData.putString("error", dataModel.getTitle());
        }
        fragment.setArguments(bundleData);
        return fragment;
    }

}
