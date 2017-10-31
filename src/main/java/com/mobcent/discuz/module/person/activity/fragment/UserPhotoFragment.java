package com.mobcent.discuz.module.person.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobcent.discuz.activity.constant.FinalConstant;
import com.mobcent.discuz.android.model.BaseResultModel;
import com.mobcent.discuz.android.model.PhotoModel;
import com.mobcent.discuz.android.service.impl.PostsServiceImpl;
import com.mobcent.discuz.module.person.activity.UserPhotosSubActivity;
import com.mobcent.lowest.android.ui.widget.PullToRefreshWaterFallNew.OnLoadItemListener;
import com.mobcent.lowest.base.model.BaseFallWallModel;
import com.mobcent.lowest.base.utils.MCAsyncTaskLoaderImage;
import java.util.List;

public class UserPhotoFragment extends BaseUserPhotosFragment {
    private int aid;

    class Holder {
        TextView name;
        ImageView photo;

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
                PhotoModel model = (PhotoModel) UserPhotoFragment.this.photoList.get(flowTag.getPosition());
                Intent intent = new Intent(UserPhotoFragment.this.activity, UserPhotosSubActivity.class);
                intent.putExtra("userId", UserPhotoFragment.this.userId);
                intent.putExtra("aid", model.getAlbumId());
                UserPhotoFragment.this.startActivity(intent);
            }

            public View createItemView(BaseFallWallModel tag) {
                View rootView = UserPhotoFragment.this.inflater.inflate(UserPhotoFragment.this.resource.getLayoutId("user_photos_item"), null);
                Holder holder = new Holder();
                holder.photo = (ImageView) UserPhotoFragment.this.findViewByName(rootView, "mc_forum_photot_img");
                holder.name = (TextView) UserPhotoFragment.this.findViewByName(rootView, "mc_forum_title");
                rootView.setTag(holder);
                return rootView;
            }

            public void setItemData(BaseFallWallModel flowTag, View view, boolean isVisibile) {
                if (isVisibile && view.getTag() != null && (view.getTag() instanceof Holder)) {
                    Holder holder = (Holder) view.getTag();
                    PhotoModel model = (PhotoModel) UserPhotoFragment.this.photoList.get(flowTag.getPosition());
                    String imgUrl = MCAsyncTaskLoaderImage.formatUrl(model.getThumbnailUrl(), FinalConstant.RESOLUTION_SMALL);
                    holder.name.setText(model.getTitle());
                    if (holder.photo.getDrawable() == null) {
                        UserPhotoFragment.this.loadImage(holder.photo, imgUrl);
                    }
                }
            }
        });
        this.pullToRefreshWaterFall.onRefresh();
    }

    public BaseResultModel<List<PhotoModel>> getPhotoList() {
        return new PostsServiceImpl(this.activity).getPhotoList(this.userId, "list", this.aid, this.page, 30);
    }

    public void onDestroy() {
        super.onDestroy();
        this.pullToRefreshWaterFall.onDestroyView();
    }
}
