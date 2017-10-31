package com.mobcent.discuz.activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.mobcent.lowest.base.utils.MCResource;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer.RoundedDrawable;

public class MCHeadIcon extends ImageView {
    private static Bitmap headIconBitmap = null;
    private int cirRatio = 8;

    public MCHeadIcon(Context context) {
        super(context);
    }

    public MCHeadIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            setBackgroundDrawable(null);
            setImageDrawable(new RoundedDrawable(bm, getMeasuredWidth() / this.cirRatio, 0));
            return;
        }
        bm = getHeadIconBitmap(getContext());
        setBackgroundDrawable(null);
        setImageDrawable(new RoundedDrawable(bm, getMeasuredWidth() / this.cirRatio, 0));
    }

    public static synchronized Bitmap getHeadIconBitmap(Context context) {
        Bitmap bitmap;
        synchronized (MCHeadIcon.class) {
            if (headIconBitmap == null) {
                headIconBitmap = BitmapFactory.decodeResource(context.getResources(), MCResource.getInstance(context).getDrawableId("mc_forum_head"));
            }
            bitmap = headIconBitmap;
        }
        return bitmap;
    }

    public static synchronized Drawable getHeadIconDrawable(Context context) {
        Drawable roundedDrawable;
        synchronized (MCHeadIcon.class) {
            Bitmap iconBitmap = getHeadIconBitmap(context);
            roundedDrawable = new RoundedDrawable(iconBitmap, iconBitmap.getWidth() / 8, 0);
        }
        return roundedDrawable;
    }

    public int getCirRatio() {
        return this.cirRatio;
    }

    public void setCirRatio(int cirRatio) {
        this.cirRatio = cirRatio;
    }
}
