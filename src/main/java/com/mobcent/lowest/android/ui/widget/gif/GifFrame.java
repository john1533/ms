package com.mobcent.lowest.android.ui.widget.gif;

import android.graphics.Bitmap;

public class GifFrame {
    public int delay;
    public Bitmap image;
    public GifFrame nextFrame = null;

    public GifFrame(Bitmap im, int del) {
        this.image = im;
        this.delay = del;
    }
}
