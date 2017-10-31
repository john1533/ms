package com.mobcent.discuz.base.dispatch;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.TextUtils;
import com.mobcent.discuz.activity.PopComponentActivity;
import com.mobcent.discuz.activity.PopModuleActivity;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.constant.IntentConstant;
import com.mobcent.discuz.android.constant.ConfigConstant;
import com.mobcent.discuz.android.constant.PostsConstant;
import com.mobcent.discuz.android.model.ConfigComponentModel;
import com.mobcent.discuz.android.model.ConfigModuleModel;
import com.mobcent.discuz.android.model.SettingModel;
import com.mobcent.discuz.android.model.TopicDraftModel;
import com.mobcent.discuz.android.user.helper.UserManageHelper;
import com.mobcent.discuz.application.DiscuzApplication;
import com.mobcent.discuz.base.constant.BaseIntentConstant;
import com.mobcent.discuz.base.helper.ConfigOptHelper;
import com.mobcent.discuz.base.helper.DZDialogHelper;
import com.mobcent.discuz.base.helper.DraftHelper;
import com.mobcent.discuz.base.helper.DraftHelper.DraftDelegate;
import com.mobcent.discuz.base.helper.LoginHelper;
import com.mobcent.discuz.module.article.fragment.activity.ArticleDetailActivity;
import com.mobcent.discuz.module.msg.fragment.activity.SessionListActivity;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.discuz.module.publish.fragment.activity.ClassifyTopicActivity;
import com.mobcent.discuz.module.sign.SignInAsyncTask;
import com.mobcent.discuz.module.topic.detail.fragment.activity.TopicDetailActivity;
import com.mobcent.discuz.module.topic.list.fragment.activity.UserTopicListActivity;
import com.mobcent.lowest.android.ui.module.weather.activity.WeatherActivity;
import com.mobcent.lowest.base.utils.MCResource;

public class ActivityDispatchHelper implements ConfigConstant, FinalConstant {
    public static void dispatchActivity(Context context, ConfigModuleModel moduleModel) {
        if (moduleModel != null) {
            Intent intent = new Intent(context, PopModuleActivity.class);
            intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, moduleModel);
            startActivity(context, intent);
        }
    }

    public static void dispatchActivity(Context context, ConfigComponentModel componentModel) {
        if (ConfigOptHelper.isNeedLogin(componentModel) && !LoginHelper.doInterceptor(context, null, null)) {
            return;
        }
        if (isPublish(componentModel.getType())) {
            dispatchPublish(context, componentModel);
        } else if (ConfigConstant.COMPONENT_MODULEREF.equals(componentModel.getType())) {
            ConfigModuleModel moduleModel = DiscuzApplication._instance.getModuleModel(componentModel.getModuleId());
            if (!ConfigOptHelper.isNeedLogin(moduleModel)) {
                dispatchActivity(context, moduleModel);
            } else if (LoginHelper.doInterceptor(context, null, null)) {
                dispatchActivity(context, moduleModel);
            }
        } else {
            Intent intent;
            if (COMPONENT_WEATHER.equals(componentModel.getType())) {
                intent = new Intent(context, WeatherActivity.class);
                SettingModel settingModel = UserManageHelper.getInstance(context).getSettingModel();
                int qs = 0;
                if (settingModel != null) {
                    qs = settingModel.getAllowCityQueryWeather();
                }
                intent.putExtra("qs", qs);
            } else if (ConfigConstant.COMPONENT_SIGN.equals(componentModel.getType())) {
                new SignInAsyncTask(context, null).execute(new Void[0]);
                return;
            } else if (ConfigConstant.COMPONENT_USERINFO.equals(componentModel.getType())) {
                intent = new Intent(context, UserHomeActivity.class);
            } else if ("search".equals(componentModel.getType())) {
                intent = new Intent(context, UserTopicListActivity.class);
                intent.putExtra(PostsConstant.TOPIC_TYPE, "search");
            } else if (ConfigConstant.COMPONENT_MESSAGELIST.equals(componentModel.getType())) {
                intent = new Intent(context, SessionListActivity.class);
            } else if (ConfigConstant.COMPONENT_POST_LIST.equals(componentModel.getType())) {
                intent = new Intent(context, TopicDetailActivity.class);
            } else if (ConfigConstant.COMPONENT_NEWSVIEW.equals(componentModel.getType())) {
                intent = new Intent(context, ArticleDetailActivity.class);
            } else {
                intent = new Intent(context, PopComponentActivity.class);
            }
            if (intent != null) {
                intent.putExtra(BaseIntentConstant.BUNDLE_COMPONENT_MODEL, componentModel);
            }
            startActivity(context, intent);
        }
    }

    public static void dispatchPublish(final Context context, final ConfigComponentModel componentModel) {
        if (!DraftHelper.isNeedAlertDialog(context, 1, new DraftDelegate() {
            public void onDraftAlertBack(TopicDraftModel model) {
                if (model == null) {
                    ActivityDispatchHelper.dispatchPublish(context, componentModel);
                    return;
                }
                Intent intent = ActivityDispatchHelper.getPublishIntent(context, componentModel);
                if (intent != null) {
                    intent.putExtra(IntentConstant.INTENT_TOPIC_DRAFMODEL, model);
                }
                ActivityDispatchHelper.startActivity(context, intent);
            }
        }) && !alertPicSelectDialog(context, componentModel)) {
            startActivity(context, getPublishIntent(context, componentModel));
        }
    }

    public static boolean alertPicSelectDialog(final Context context, final ConfigComponentModel componentModel) {
        if (!isPublishImgComponent(componentModel.getType())) {
            return false;
        }
        MCResource resource = MCResource.getInstance(context);
        Context context2;
        String[] names = new String[]{resource.getString("mc_forum_take_photo"), MCResource.getInstance(context).getString("mc_forum_gallery_local_pic")};
        Context topActivity = DiscuzApplication._instance.getTopActivity();
        if (topActivity == null) {
            context2 = context;
        } else {
            context2 = topActivity;
        }
        AlertDialog dialog = new Builder(context2).setTitle(resource.getString("mc_forum_publish_choose")).setItems(names, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    componentModel.setType(ConfigConstant.COMPONENT_FASTCAMERA);
                } else {
                    componentModel.setType(ConfigConstant.COMPONENT_FASTIMAGE);
                }
                ActivityDispatchHelper.startActivity(context, ActivityDispatchHelper.getPublishIntent(context, componentModel));
            }
        }).create();
        if (topActivity != null) {
            try {
                dialog.show();
            } catch (Exception e) {
                DZDialogHelper.setDialogSystemAlert(dialog);
            }
        } else {
            DZDialogHelper.setDialogSystemAlert(dialog);
        }
        return true;
    }

    public static boolean isPublish(String type) {
        if (ConfigConstant.COMPONENT_FASTPOST.equals(type) || ConfigConstant.COMPONENT_FASTIMAGE.equals(type) || ConfigConstant.COMPONENT_FASTCAMERA.equals(type) || ConfigConstant.COMPONENT_FASTAUDIO.equals(type)) {
            return true;
        }
        return false;
    }

    public static Intent getPublishIntent(Context context, ConfigComponentModel componentModel) {
        Intent intent = new Intent(context, ClassifyTopicActivity.class);
        intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
        if (!componentModel.getType().equals(ConfigConstant.COMPONENT_FASTPOST)) {
            if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTIMAGE)) {
                intent.putExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 1);
            } else if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTCAMERA)) {
                intent.putExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 2);
                intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
            } else if (componentModel.getType().equals(ConfigConstant.COMPONENT_FASTAUDIO)) {
                intent.putExtra(IntentConstant.INTENT_RAPID_PUBLISH_TYPE, 3);
                intent.putExtra(BaseIntentConstant.BUNDLE_MODULE_MODEL, componentModel);
            }
        }
        return intent;
    }

    public static void startActivity(Context context, Intent intent) {
        if (intent != null) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                intent.setFlags(268435456);
                context.startActivity(intent);
            }
        }
    }

    public static boolean isModuleModel(String type) {
        if (ConfigConstant.MODULE_TYPE_SUBNAV.equals(type) || ConfigConstant.MODULE_TYPE_FULL.equals(type) || ConfigConstant.MODULE_TYPE_SUBNAV.equals(type)) {
            return true;
        }
        return false;
    }

    public static boolean isPublishImgComponent(String type) {
        if (TextUtils.isEmpty(type)) {
            return false;
        }
        if (ConfigConstant.COMPONENT_FASTIMAGE.equals(type) || ConfigConstant.COMPONENT_FASTCAMERA.equals(type)) {
            return true;
        }
        return false;
    }
}
