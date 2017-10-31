package com.mobcent.lowest.android.ui.module.game.fragment.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseGameListFragmentAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected Handler mHandler = new Handler();
    protected MCResource mcResource;
    protected Resources resources;

    public BaseGameListFragmentAdapter(Context context) {
        this.mcResource = MCResource.getInstance(context);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resources = context.getResources();
    }

    public void loadImageByUrl(ImageView img) {
        ImageLoader.getInstance().displayImage(img.getTag() == null ? "" : img.getTag().toString(), img);
    }

    protected void showToast(Context context, String content) {
        Toast.makeText(context, content, 0).show();
    }
}
