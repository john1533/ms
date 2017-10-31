package com.mobcent.discuz.module.msg.fragment.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mobcent.android.model.MCShareModel;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.activity.utils.DZFaceUtil;
import com.mobcent.discuz.activity.utils.DZImageLoadUtils;
import com.mobcent.discuz.activity.view.MCHeadIcon;
import com.mobcent.discuz.android.db.MsgDBUtil.MsgDBModel;
import com.mobcent.discuz.android.db.SettingSharePreference;
import com.mobcent.discuz.android.db.SharedPreferencesDB;
import com.mobcent.discuz.android.model.MsgContentModel;
import com.mobcent.discuz.android.model.MsgUserListModel;
import com.mobcent.discuz.base.helper.MCForumLaunchShareHelper;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.ChatRoomFragmentAdapterHolder1;
import com.mobcent.discuz.module.msg.fragment.adapter.holder.ChatRoomFragmentAdapterHolder2;
import com.mobcent.discuz.module.msg.helper.ChatRoomHelper;
import com.mobcent.discuz.module.person.activity.UserHomeActivity;
import com.mobcent.lowest.android.ui.utils.MCAudioUtils;
import com.mobcent.lowest.android.ui.widget.MCProgressBar;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper;
import com.mobcent.lowest.android.ui.widget.scaleview.ImagePreviewHelper.ImageViewerListener;
import com.mobcent.lowest.android.ui.widget.scaleview.RichImageModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import com.mobcent.lowest.base.utils.MCPhoneUtil;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.lowest.base.utils.MCStringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomFragmentAdapter extends BaseAdapter {
    private final long INTERVAL = 180000;
    private String SEND_AGAIN;
    private final String TAG = "ChatRoomFragmentAdapter";
    private MCAudioUtils audioUtils;
    private Context context;
    private List<MsgDBModel> datas;
    private Dialog dialog;
    private LayoutInflater inflater;
    private String[] items;
    private int maxWidth;
    private MsgUserListModel msgUserListModel;
    private MCResource resource;
    private SharedPreferencesDB sDb;
    private long userId;

    public ChatRoomFragmentAdapter(Context context, List<MsgDBModel> datas, MsgUserListModel msgUserListModel) {
        this.context = context;
        this.inflater = LayoutInflater.from(context.getApplicationContext());
        this.datas = datas;
        this.resource = MCResource.getInstance(context.getApplicationContext());
        this.maxWidth = MCPhoneUtil.getDisplayWidth(context.getApplicationContext()) - MCPhoneUtil.getRawSize(context.getApplicationContext(), 1, 183.0f);
        this.SEND_AGAIN = this.resource.getString("mc_forum_message_send_agin");
        this.items = new String[]{this.SEND_AGAIN};
        this.msgUserListModel = msgUserListModel;
        this.audioUtils = MCAudioUtils.getInstance(context.getApplicationContext());
        this.sDb = SharedPreferencesDB.getInstance(context.getApplicationContext());
        this.userId = this.sDb.getUserId();
    }

    public void setDatas(List<MsgDBModel> datas) {
        this.datas = datas;
    }

    public int getCount() {
        return this.datas.size();
    }

    public Object getItem(int position) {
        return this.datas.get(position);
    }

    public long getItemId(int position) {
        return ((MsgDBModel) this.datas.get(position)).getPmid();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MsgDBModel msgDBModel = (MsgDBModel) this.datas.get(position);
        if (msgDBModel.getSource() == 1) {
            return getOtherView(position, msgDBModel, convertView);
        }
        if (msgDBModel.getSource() == 0) {
            return getMySelfView(position, msgDBModel, convertView);
        }
        return convertView;
    }

    private View getOtherView(int position, MsgDBModel msgDBModel, View convertView) {
        ChatRoomFragmentAdapterHolder1 holder;
        if (convertView == null || !(convertView.getTag() instanceof ChatRoomFragmentAdapterHolder1)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("chat_room_other_view"), null);
            holder = new ChatRoomFragmentAdapterHolder1();
            initOtherView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (ChatRoomFragmentAdapterHolder1) convertView.getTag();
        }
        if (MCStringUtil.isEmpty(msgDBModel.getIcon())) {
            holder.getIconImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            updateImage(true, msgDBModel.getIcon(), holder.getIconImg());
        }
        initIconListener(holder.getIconImg(), msgDBModel.getFromUid());
        initTimeText(position, msgDBModel, holder.getTimeText());
        dealOtherData(holder, msgDBModel);
        return convertView;
    }

    private View getMySelfView(int position, MsgDBModel msgDBModel, View convertView) {
        ChatRoomFragmentAdapterHolder2 holder;
        if (convertView == null || !(convertView.getTag() instanceof ChatRoomFragmentAdapterHolder2)) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("chat_room_my_view"), null);
            holder = new ChatRoomFragmentAdapterHolder2();
            initMySelfView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (ChatRoomFragmentAdapterHolder2) convertView.getTag();
        }
        if (MCStringUtil.isEmpty(this.sDb.getIcon())) {
            holder.getIconImg().setImageBitmap(MCHeadIcon.getHeadIconBitmap(this.context));
        } else {
            updateImage(true, this.sDb.getIcon(), holder.getIconImg());
        }
        initIconListener(holder.getIconImg(), this.userId);
        initTimeText(position, msgDBModel, holder.getTimeText());
        dealMyData(holder, msgDBModel);
        return convertView;
    }

    private void initTimeText(int position, MsgDBModel msgDBModel, TextView timeText) {
        timeText.setVisibility(0);
        timeText.setTag(Long.valueOf(msgDBModel.getTime()));
        if (position == 0) {
            timeText.setText(MCDateUtil.getFormatTimeByPM(msgDBModel.getTime()));
            return;
        }
        if (msgDBModel.getTime() - ((MsgDBModel) getItem(position - 1)).getTime() > 180000) {
            timeText.setText(MCDateUtil.getFormatTimeByPM(msgDBModel.getTime()));
        } else {
            timeText.setVisibility(8);
        }
    }

    private void click(final View view, final MsgDBModel msgDBModel) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if ("image".equals(msgDBModel.getType())) {
                    ArrayList<RichImageModel> sendList = new ArrayList();
                    for (int i = 0; i < ChatRoomFragmentAdapter.this.datas.size(); i++) {
                        if ("image".equals(((MsgDBModel) ChatRoomFragmentAdapter.this.datas.get(i)).getType())) {
                            RichImageModel model = new RichImageModel();
                            model.setImageUrl(MCAsyncTaskLoaderImage.formatUrl(((MsgDBModel) ChatRoomFragmentAdapter.this.datas.get(i)).getContent(), FinalConstant.RESOLUTION_BIG));
                            sendList.add(model);
                        }
                    }
                    ImagePreviewHelper.getInstance().startImagePreview((Activity) ChatRoomFragmentAdapter.this.context, sendList, MCAsyncTaskLoaderImage.formatUrl(msgDBModel.getContent(), FinalConstant.RESOLUTION_BIG), new ImageViewerListener() {
                        public void sharePic(Context context, RichImageModel imgModel, String localPath) {
                            MCShareModel shareModel = new MCShareModel();
                            shareModel.setPicUrl(imgModel.getImageUrl());
                            shareModel.setImageFilePath(localPath);
                            shareModel.setDownloadUrl(ChatRoomFragmentAdapter.this.resource.getString("mc_share_download_url"));
                            shareModel.setType(1);
                            MCForumLaunchShareHelper.share(context, shareModel);
                        }
                    });
                } else if (!"audio".equals(msgDBModel.getType())) {
                } else {
                    if (view.getTag() != null && view.getTag().equals(ChatRoomFragmentAdapter.this.audioUtils.getCurrentAudioUrl()) && ChatRoomFragmentAdapter.this.audioUtils.isPlaying()) {
                        ChatRoomFragmentAdapter.this.audioUtils.stopAudio();
                    } else {
                        ChatRoomFragmentAdapter.this.audioUtils.playAudio((String) view.getTag());
                    }
                }
            }
        });
    }

    public void dealOtherData(ChatRoomFragmentAdapterHolder1 holder, MsgDBModel msgDBModel) {
        if ("text".equals(msgDBModel.getType())) {
            holder.getContentText().setVisibility(0);
            holder.getContentImg().setVisibility(8);
            holder.getContentAudio().setVisibility(8);
            holder.getContentText().setText(msgDBModel.getContent());
            DZFaceUtil.setStrToFace(holder.getContentText(), msgDBModel.getContent(), this.context.getApplicationContext());
        } else if ("image".equals(msgDBModel.getType())) {
            holder.getContentImg().setVisibility(0);
            holder.getContentText().setVisibility(8);
            holder.getContentAudio().setVisibility(8);
            holder.getContentImg().setImageBitmap(null);
            updateImage(false, msgDBModel.getContent(), holder.getContentImg());
            click(holder.getContentImg(), msgDBModel);
        } else if ("audio".equals(msgDBModel.getType())) {
            holder.getContentAudio().setVisibility(0);
            holder.getContentText().setVisibility(8);
            holder.getContentImg().setVisibility(8);
            holder.getContentBox().setTag(msgDBModel.getContent());
            holder.getContentAudio().setImageResource(this.resource.getDrawableId("mc_forum_voice_chat_img0"));
            click(holder.getContentBox(), msgDBModel);
        }
    }

    public void dealMyData(ChatRoomFragmentAdapterHolder2 holder, MsgDBModel msgDBModel) {
        if ("text".equals(msgDBModel.getType())) {
            holder.getContentText().setVisibility(0);
            holder.getContentImg().setVisibility(8);
            holder.getContentAudio().setVisibility(8);
            holder.getContentText().setText(msgDBModel.getContent());
            DZFaceUtil.setStrToFace(holder.getContentText(), msgDBModel.getContent(), this.context.getApplicationContext());
        } else if ("image".equals(msgDBModel.getType())) {
            holder.getContentImg().setVisibility(0);
            holder.getContentText().setVisibility(8);
            holder.getContentAudio().setVisibility(8);
            holder.getContentImg().setImageBitmap(null);
            updateImage(false, msgDBModel.getContent(), holder.getContentImg());
            click(holder.getContentImg(), msgDBModel);
        } else if ("audio".equals(msgDBModel.getType())) {
            holder.getContentAudio().setVisibility(0);
            holder.getContentText().setVisibility(8);
            holder.getContentImg().setVisibility(8);
            holder.getContentBox().setTag(msgDBModel.getContent());
            holder.getContentAudio().setImageResource(this.resource.getDrawableId("mc_forum_voice_chat2_img0"));
            click(holder.getContentBox(), msgDBModel);
        }
        dealStatus(holder, msgDBModel);
        initRetry(holder, msgDBModel);
    }

    @SuppressLint({"NewApi"})
    private void dealStatus(ChatRoomFragmentAdapterHolder2 holder, MsgDBModel msDbModel) {
        if (msDbModel.getStatus() == 1) {
            if ("image".equals(msDbModel.getType()) && VERSION.SDK_INT >= 11) {
                holder.getContentImg().setAlpha(0.3f);
            }
            holder.getSendIngProgressBar().setVisibility(0);
            holder.getSendIngProgressBar().show();
            holder.getSendFailImg().setVisibility(8);
        } else if (msDbModel.getStatus() == 2) {
            if ("image".equals(msDbModel.getType()) && VERSION.SDK_INT >= 11) {
                holder.getContentImg().setAlpha(CustomConstant.RATIO_ONE_HEIGHT);
            }
            holder.getSendIngProgressBar().setVisibility(8);
            holder.getSendIngProgressBar().hide();
            holder.getSendFailImg().setVisibility(0);
        } else if (msDbModel.getStatus() == 0) {
            if ("image".equals(msDbModel.getType()) && VERSION.SDK_INT >= 11) {
                holder.getContentImg().setAlpha(CustomConstant.RATIO_ONE_HEIGHT);
            }
            holder.getSendIngProgressBar().setVisibility(8);
            holder.getSendIngProgressBar().hide();
            holder.getSendFailImg().setVisibility(8);
        }
    }

    private void initRetry(final ChatRoomFragmentAdapterHolder2 holder, final MsgDBModel msgDBModel) {
        holder.getSendFailImg().setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                if (msgDBModel.getStatus() == 2) {
                    ChatRoomFragmentAdapter.this.dialog = new Builder(ChatRoomFragmentAdapter.this.context).setItems(ChatRoomFragmentAdapter.this.items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (ChatRoomFragmentAdapter.this.items[which].equals(ChatRoomFragmentAdapter.this.SEND_AGAIN)) {
                                msgDBModel.setStatus(1);
                                ChatRoomFragmentAdapter.this.dealStatus(holder, msgDBModel);
                                MsgContentModel msgContentModel = new MsgContentModel();
                                msgContentModel.setContent(msgDBModel.getContent());
                                msgContentModel.setTime(msgDBModel.getTime());
                                msgContentModel.setType(msgDBModel.getType());
                                msgContentModel.setPlid(msgDBModel.getPlid());
                                msgContentModel.setPmid(msgDBModel.getPmid());
                                msgContentModel.setPlid(ChatRoomFragmentAdapter.this.msgUserListModel.getPlid());
                                msgContentModel.setStatus(1);
                                if (ChatRoomHelper.getInstance().getChatRoomDelegate() != null) {
                                    ChatRoomHelper.getInstance().getChatRoomDelegate().sendMsgAgain(msgContentModel, ChatRoomFragmentAdapter.this.userId, ChatRoomFragmentAdapter.this.msgUserListModel.getToUserId(), ChatRoomFragmentAdapter.this.msgUserListModel.getToUserName(), true);
                                }
                            }
                        }
                    }).show();
                }
            }
        });
        if ("text".equals(msgDBModel.getType())) {
            initLayoutRetry(holder.getContentBox(), holder, msgDBModel);
        } else if ("image".equals(msgDBModel.getType())) {
            initLayoutRetry(holder.getContentImg(), holder, msgDBModel);
        } else if ("audio".equals(msgDBModel.getType())) {
            initLayoutRetry(holder.getContentBox(), holder, msgDBModel);
        }
    }

    private void initLayoutRetry(View view, final ChatRoomFragmentAdapterHolder2 holder, final MsgDBModel msgDBModel) {
        view.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                if (msgDBModel.getStatus() == 2) {
                    holder.getSendFailImg().performClick();
                }
                return true;
            }
        });
    }

    private void initOtherView(View convertView, ChatRoomFragmentAdapterHolder1 holder) {
        holder.setLayout((RelativeLayout) findViewByName(convertView, "other_view_box"));
        holder.setTimeText((TextView) findViewByName(convertView, "other_time_text"));
        holder.setIconImg((MCHeadIcon) findViewByName(convertView, "other_user_icon_img"));
        holder.setContentBox((RelativeLayout) findViewByName(convertView, "other_content_box"));
        holder.setContentText((TextView) findViewByName(convertView, "other_content_text"));
        holder.setContentImg((ImageView) findViewByName(convertView, "other_content_img"));
        holder.setAudioBox((LinearLayout) findViewByName(convertView, "other_content_audio_box"));
        holder.setContentAudio((ImageView) findViewByName(convertView, "other_content_audio"));
        holder.setAudioTimeText((TextView) findViewByName(convertView, "other_content_audio_time_text"));
        holder.getContentText().setMaxWidth(this.maxWidth);
        holder.getContentImg().setMaxWidth(this.maxWidth);
        holder.getContentAudio().setMaxWidth(this.maxWidth);
    }

    private void initMySelfView(View convertView, ChatRoomFragmentAdapterHolder2 holder) {
        holder.setLayout((RelativeLayout) findViewByName(convertView, "my_view_box"));
        holder.setTimeText((TextView) findViewByName(convertView, "my_time_text"));
        holder.setIconImg((MCHeadIcon) findViewByName(convertView, "my_user_icon_img"));
        holder.setContentSignBox((RelativeLayout) findViewByName(convertView, "my_content_sign_box"));
        holder.setContentBox((RelativeLayout) findViewByName(convertView, "my_content_box"));
        holder.setContentText((TextView) findViewByName(convertView, "my_content_text"));
        holder.setContentImg((ImageView) findViewByName(convertView, "my_content_img"));
        holder.setAudioBox((LinearLayout) findViewByName(convertView, "my_content_audio_box"));
        holder.setContentAudio((ImageView) findViewByName(convertView, "my_content_audio"));
        holder.setAudioTimeText((TextView) findViewByName(convertView, "my_content_audio_time_text"));
        holder.setSendIngProgressBar((MCProgressBar) findViewByName(convertView, "my_send_ing"));
        holder.setSendFailImg((ImageView) findViewByName(convertView, "my_send_fail"));
        holder.getContentText().setMaxWidth(this.maxWidth);
        holder.getContentImg().setMaxWidth(this.maxWidth);
        holder.getContentAudio().setMaxWidth(this.maxWidth);
    }

    private View findViewByName(View parent, String name) {
        return parent.findViewById(this.resource.getViewId(name));
    }

    private void updateImage(final boolean isIcon, String imgUrl, final ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            if (imgUrl.startsWith("/")) {
                imageView.setTag(Uri.fromFile(new File(imgUrl)).toString());
            } else {
                imageView.setTag(MCAsyncTaskLoaderImage.formatUrl(imgUrl, FinalConstant.RESOLUTION_SMALL));
            }
            if (new SettingSharePreference(this.context).isPicAvailable()) {
                DisplayImageOptions headIconOptions;
                ImageLoader instance = ImageLoader.getInstance();
                String obj = imageView.getTag().toString();
                if (isIcon) {
                    headIconOptions = DZImageLoadUtils.getHeadIconOptions();
                } else {
                    headIconOptions = null;
                }
                instance.displayImage(obj, imageView, headIconOptions, new SimpleImageLoadingListener() {
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (loadedImage == null || loadedImage.isRecycled() || !imageView.getTag().equals(imageUri)) {
                            if (!isIcon) {
                                imageView.setBackgroundResource(ChatRoomFragmentAdapter.this.resource.getDrawableId("mc_forum_add_new_img"));
                            }
                        } else if (!isIcon) {
                            imageView.setImageBitmap(loadedImage);
                        }
                    }
                });
            }
        }
    }

    private void initIconListener(ImageView iconImg, final long userId) {
        iconImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomFragmentAdapter.this.context, UserHomeActivity.class);
                intent.putExtra("userId", userId);
                ChatRoomFragmentAdapter.this.context.startActivity(intent);
            }
        });
    }
}
