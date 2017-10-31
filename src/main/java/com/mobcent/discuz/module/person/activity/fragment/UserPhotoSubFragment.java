package com.mobcent.discuz.module.person.activity.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFallNew.OnLoadItemListener;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import com.mobcent.lowest.base.utils.MCDateUtil;
import java.util.List;

public class UserPhotoSubFragment extends BaseUserPhotosFragment {

    class Holder {
        ImageView photo;
        TextView readCount;
        TextView replyCount;
        TextView time;
        TextView title;

        Holder() {
        }
    }

    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        this.aid = getBundle().getInt("aid");
    }

    protected void initActions(View rootView) {
        super.initActions(rootView);
        this.pullToRefreshWaterFall.setOnLoadItemListener(new OnLoadItemListener() {
            public void onItemRecycle(BaseFallWallModel flowTag, View view) {
                ((Holder) view.getTag()).photo.setImageBitmap(null);
            }

            public void onItemClick(BaseFallWallModel flowTag, View view) {
                UserPhotoSubFragment.this.previewBigImg(flowTag.getPosition());
            }

            public View createItemView(BaseFallWallModel tag) {
                View rootView = UserPhotoSubFragment.this.inflater.inflate(UserPhotoSubFragment.this.resource.getLayoutId("user_photo_list_item"), null);
                Holder holder = new Holder();
                holder.photo = (ImageView) UserPhotoSubFragment.this.findViewByName(rootView, "mc_forum_photo_img");
                holder.title = (TextView) UserPhotoSubFragment.this.findViewByName(rootView, "mc_forum_title");
                holder.time = (TextView) UserPhotoSubFragment.this.findViewByName(rootView, "mc_forum_time_text");
                holder.replyCount = (TextView) UserPhotoSubFragment.this.findViewByName(rootView, "mc_forum_reply_count");
                holder.readCount = (TextView) UserPhotoSubFragment.this.findViewByName(rootView, "mc_forum_read_count");
                rootView.setTag(holder);
                return rootView;
            }

            public void setItemData(BaseFallWallModel flowTag, View view, boolean isVisibile) {
                if (isVisibile && view.getTag() != null && (view.getTag() instanceof Holder)) {
                    Holder holder = (Holder) view.getTag();
                    PhotoModel model = (PhotoModel) UserPhotoSubFragment.this.photoList.get(flowTag.getPosition());
                    String imgUrl = MCAsyncTaskLoaderImage.formatUrl(model.getThumbnailUrl(), FinalConstant.RESOLUTION_SMALL);
                    holder.title.setText(model.getTitle());
                    holder.time.setText(MCDateUtil.getFormatTimeByYear(model.getCreateDate()));
                    holder.replyCount.setText(model.getReplieCount() + "");
                    holder.readCount.setText(model.getHitCount() + "");
                    UserPhotoSubFragment.this.loadImage(holder.photo, imgUrl);
                }
            }
        });
        this.pullToRefreshWaterFall.onRefresh();
    }

    public BaseResultModel<List<PhotoModel>> getPhotoList() {
        return new PostsServiceImpl(this.activity).getPhotoList(this.userId, "img", this.aid, this.page, 30);
    }
}
