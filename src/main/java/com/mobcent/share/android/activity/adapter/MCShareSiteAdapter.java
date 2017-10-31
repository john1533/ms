package com.mobcent.share.android.activity.adapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.android.model.MCShareSiteModel;
import com.mobcent.android.service.MCShareService;
import com.mobcent.android.service.impl.MCShareServiceImpl;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCResource;
import com.mobcent.share.android.activity.MCShareWebActivity;
import com.mobcent.share.android.activity.adapter.holder.MCShareSiteAdapterHolder;
import com.mobcent.share.android.constant.MCShareIntentConstant;
import com.mobcent.share.android.utils.MCShareToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.List;

public class MCShareSiteAdapter extends BaseAdapter {
    private static final String TAG = "MCShareSiteAdapter";
    private String appKey;
    private MCAsyncTaskLoaderImage asyncTaskLoaderImage;
    private Context context;
    private LayoutInflater inflater;
    private List<MCShareSiteModel> list;
    private UpdateDataListener listener;
    private Handler mHandler;
    private MCResource resource;
    private MCShareService shareService;

    public interface UpdateDataListener {
        void update(MCShareSiteModel mCShareSiteModel);
    }

    public List<MCShareSiteModel> getList() {
        return this.list;
    }

    public void setList(List<MCShareSiteModel> list) {
        this.list = list;
    }

    public MCShareSiteAdapter(Context context, List<MCShareSiteModel> list, MCAsyncTaskLoaderImage asyncTaskLoaderImage, Handler mHandler, String appKey, UpdateDataListener listener) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.resource = MCResource.getInstance(context);
        this.asyncTaskLoaderImage = asyncTaskLoaderImage;
        this.mHandler = mHandler;
        this.appKey = appKey;
        this.shareService = new MCShareServiceImpl(context);
        this.listener = listener;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    public long getItemId(int position) {
        return (long) ((MCShareSiteModel) this.list.get(position)).getSiteId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MCShareSiteAdapterHolder holder;
        if (convertView == null) {
            convertView = this.inflater.inflate(this.resource.getLayoutId("mc_share_site_list_item"), null);
            holder = new MCShareSiteAdapterHolder();
            initViews(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (MCShareSiteAdapterHolder) convertView.getTag();
        }
        initData(holder, (MCShareSiteModel) this.list.get(position));
        final ImageView itemImg = holder.getItemImg();
        convertView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                itemImg.performClick();
            }
        });
        convertView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                itemImg.performLongClick();
                return true;
            }
        });
        return convertView;
    }

    private void initViews(View convertView, MCShareSiteAdapterHolder holder) {
        holder.setItemImg((ImageView) convertView.findViewById(this.resource.getViewId("mc_share_item_img")));
        holder.setItemName((TextView) convertView.findViewById(this.resource.getViewId("mc_share_item_name")));
        holder.setItemNickName((TextView) convertView.findViewById(this.resource.getViewId("mc_share_user_name")));
        holder.setSetBtn((Button) convertView.findViewById(this.resource.getViewId("mc_share_set_btn")));
    }

    private void initData(final MCShareSiteAdapterHolder holder, final MCShareSiteModel siteModel) {
        holder.getItemName().setText(siteModel.getSiteName());
        updateImageView(holder, siteModel);
        holder.getSetBtn().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (siteModel.getBindState() == 1) {
                    siteModel.setBindState(2);
                    MCShareSiteAdapter.this.updateSiteStateUI(holder, siteModel);
                } else if (siteModel.getBindState() == 2) {
                    siteModel.setBindState(1);
                    MCShareSiteAdapter.this.updateSiteStateUI(holder, siteModel);
                } else if (siteModel.getBindState() == 3) {
                    MCShareSiteAdapter.this.goBindActivity(siteModel);
                }
            }
        });
        holder.getItemImg().setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (siteModel.getBindState() == 3) {
                    MCShareSiteAdapter.this.goBindActivity(siteModel);
                }
            }
        });
        holder.getItemImg().setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (siteModel.getBindState() == 1 || siteModel.getBindState() == 2) {
                    Builder builder = new Builder(MCShareSiteAdapter.this.context).setTitle(MCShareSiteAdapter.this.resource.getString("mc_share_tips")).setMessage(MCShareSiteAdapter.this.resource.getString("mc_share_unbind_tips"));
                    builder.setPositiveButton(MCShareSiteAdapter.this.resource.getString("mc_share_confirm"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            new Thread() {
                                public void run() {
                                    final boolean isSucc = MCShareSiteAdapter.this.shareService.unbindSite(siteModel.getUserId(), siteModel.getSiteId(), MCShareSiteAdapter.this.appKey, MCShareSiteAdapter.this.resource.getString("mc_share_domain_url"));
                                    MCShareSiteAdapter.this.mHandler.post(new Runnable() {
                                        public void run() {
                                            if (isSucc) {
                                                MCShareToastUtil.toast((Activity) MCShareSiteAdapter.this.context, MCShareSiteAdapter.this.resource.getString("mc_share_unbind_succ"));
                                                siteModel.setBindState(3);
                                                if (MCShareSiteAdapter.this.listener != null) {
                                                    MCShareSiteAdapter.this.listener.update(siteModel);
                                                }
                                                MCShareSiteAdapter.this.updateSiteStateUI(holder, siteModel);
                                                return;
                                            }
                                            MCShareToastUtil.toast((Activity) MCShareSiteAdapter.this.context, MCShareSiteAdapter.this.resource.getString("mc_share_unbind_fail"));
                                        }
                                    });
                                }
                            }.start();
                        }
                    });
                    builder.setNegativeButton(MCShareSiteAdapter.this.resource.getString("mc_share_cancel"), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else if (siteModel.getBindState() == 3) {
                    MCShareSiteAdapter.this.goBindActivity(siteModel);
                }
                return false;
            }
        });
    }

    private void updateImageView(final MCShareSiteAdapterHolder holder, final MCShareSiteModel siteModel) {
        ImageLoader.getInstance().displayImage(MCAsyncTaskLoaderImage.formatUrl(siteModel.getSiteImage(), MCShareIntentConstant.RESOLUTION_320X480), holder.getItemImg(), new SimpleImageLoadingListener() {
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                MCShareSiteAdapter.this.updateSiteStateUI(holder, siteModel);
            }

            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                MCShareSiteAdapter.this.updateSiteStateUI(holder, siteModel);
            }

            public void onLoadingCancelled(String imageUri, View view) {
                MCShareSiteAdapter.this.updateSiteStateUI(holder, siteModel);
            }
        });
    }

    public void updateSiteStateUI(MCShareSiteAdapterHolder holder, MCShareSiteModel siteModel) {
        Drawable bg;
        if (siteModel.getBindState() == 1) {
            holder.getSetBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_select3_2"));
            bg = holder.getItemImg().getBackground();
            if (bg != null) {
                bg.setAlpha(MotionEventCompat.ACTION_MASK);
            }
            holder.getItemImg().setBackgroundDrawable(bg);
            holder.getItemNickName().setVisibility(8);
        } else if (siteModel.getBindState() == 2) {
            holder.getSetBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_select3_1"));
            bg = holder.getItemImg().getBackground();
            if (bg != null) {
                bg.setAlpha(MotionEventCompat.ACTION_MASK);
            }
            holder.getItemImg().setBackgroundDrawable(bg);
            holder.getItemNickName().setVisibility(8);
        } else if (siteModel.getBindState() == 3) {
            holder.getSetBtn().setBackgroundResource(this.resource.getDrawableId("mc_forum_select3_1"));
            bg = holder.getItemImg().getBackground();
            if (bg != null) {
                bg.setAlpha(72);
            }
            holder.getItemImg().setBackgroundDrawable(bg);
            holder.getItemNickName().setVisibility(0);
        }
    }

    private void goBindActivity(MCShareSiteModel siteModel) {
        Intent intent = new Intent(this.context, MCShareWebActivity.class);
        intent.putExtra(MCShareIntentConstant.MC_SHARE_APP_KEY, this.appKey);
        intent.putExtra(MCShareIntentConstant.MC_SHARE_SITE_MODEL, siteModel);
        ((Activity) this.context).startActivityForResult(intent, 100);
    }
}
