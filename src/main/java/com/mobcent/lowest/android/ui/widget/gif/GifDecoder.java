package com.mobcent.lowest.android.ui.widget.gif;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.mobcent.discuz.module.custom.widget.constant.CustomConstant;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GifDecoder extends Thread {
    private static final int MaxStackSize = 4096;
    public static final int STATUS_FINISH = -1;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OPEN_ERROR = 2;
    public static final int STATUS_PARSING = 0;
    private int[] act;
    private GifAction action;
    private int bgColor;
    private int bgIndex;
    private byte[] block;
    private int blockSize;
    private GifFrame currentFrame;
    private int delay;
    private int dispose;
    private int frameCount;
    private int[] gct;
    private boolean gctFlag;
    private int gctSize;
    private byte[] gifData;
    private GifFrame gifFrame;
    public int height;
    private int ih;
    private Bitmap image;
    private InputStream in;
    private boolean interlace;
    private boolean isShow;
    private int iw;
    private int ix;
    private int iy;
    private int lastBgColor;
    private int lastDispose;
    private Bitmap lastImage;
    private int[] lct;
    private boolean lctFlag;
    private int lctSize;
    private int loopCount;
    private int lrh;
    private int lrw;
    private int lrx;
    private int lry;
    private int pixelAspect;
    private byte[] pixelStack;
    private byte[] pixels;
    private short[] prefix;
    private int status;
    private byte[] suffix;
    private int transIndex;
    private boolean transparency;
    public int width;

    public GifDecoder(byte[] data, GifAction act) {
        this.loopCount = 1;
        this.currentFrame = null;
        this.isShow = false;
        this.block = new byte[256];
        this.blockSize = 0;
        this.dispose = 0;
        this.lastDispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.action = null;
        this.gifData = null;
        this.gifData = data;
        this.action = act;
    }

    public GifDecoder(InputStream is, GifAction act) {
        this.loopCount = 1;
        this.currentFrame = null;
        this.isShow = false;
        this.block = new byte[256];
        this.blockSize = 0;
        this.dispose = 0;
        this.lastDispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.action = null;
        this.gifData = null;
        this.in = is;
        this.action = act;
    }

    public void run() {
        if (this.in != null) {
            readStream();
        } else if (this.gifData != null) {
            readByte();
        }
    }

    public void free() {
        GifFrame fg = this.gifFrame;
        while (fg != null) {
            fg.image = null;
            this.gifFrame = this.gifFrame.nextFrame;
            fg = this.gifFrame;
        }
        if (this.in != null) {
            try {
                this.in.close();
            } catch (Exception e) {
            }
            this.in = null;
        }
        this.gifData = null;
    }

    public int getStatus() {
        return this.status;
    }

    public boolean parseOk() {
        return this.status == -1;
    }

    public int getDelay(int n) {
        this.delay = -1;
        if (n >= 0 && n < this.frameCount) {
            GifFrame f = getFrame(n);
            if (f != null) {
                this.delay = f.delay;
            }
        }
        return this.delay;
    }

    public int[] getDelays() {
        GifFrame f = this.gifFrame;
        int[] d = new int[this.frameCount];
        int i = 0;
        while (f != null && i < this.frameCount) {
            d[i] = f.delay;
            f = f.nextFrame;
            i++;
        }
        return d;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public Bitmap getImage() {
        return getFrameImage(0);
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    private void setPixels() {
        int c;
        int i;
        int k;
        int[] dest = new int[(this.width * this.height)];
        if (this.lastDispose > 0) {
            if (this.lastDispose == 3) {
                int n = this.frameCount - 2;
                if (n > 0) {
                    this.lastImage = getFrameImage(n - 1);
                } else {
                    this.lastImage = null;
                }
            }
            if (this.lastImage != null) {
                this.lastImage.getPixels(dest, 0, this.width, 0, 0, this.width, this.height);
                if (this.lastDispose == 2) {
                    c = 0;
                    if (!this.transparency) {
                        c = this.lastBgColor;
                    }
                    for (i = 0; i < this.lrh; i++) {
                        int n1 = ((this.lry + i) * this.width) + this.lrx;
                        int n2 = n1 + this.lrw;
                        for (k = n1; k < n2; k++) {
                            dest[k] = c;
                        }
                    }
                }
            }
        }
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (i = 0; i < this.ih; i++) {
            int line = i;
            if (this.interlace) {
                if (iline >= this.ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += this.iy;
            if (line < this.height) {
                k = line * this.width;
                int dx = k + this.ix;
                int dlim = dx + this.iw;
                if (this.width + k < dlim) {
                    dlim = k + this.width;
                }
                int sx = i * this.iw;
                while (dx < dlim) {
                    int sx2 = sx + 1;
                    c = this.act[this.pixels[sx] & MotionEventCompat.ACTION_MASK];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                    dx++;
                    sx = sx2;
                }
            }
        }
        this.image = Bitmap.createBitmap(dest, this.width, this.height, Config.ARGB_4444);
    }

    public Bitmap getFrameImage(int n) {
        GifFrame frame = getFrame(n);
        if (frame == null) {
            return null;
        }
        return frame.image;
    }

    public GifFrame getCurrentFrame() {
        return this.currentFrame;
    }

    public GifFrame getFrame(int n) {
        GifFrame frame = this.gifFrame;
        int i = 0;
        while (frame != null) {
            if (i == n) {
                return frame;
            }
            frame = frame.nextFrame;
            i++;
        }
        return null;
    }

    public void reset() {
        this.currentFrame = this.gifFrame;
    }

    public GifFrame next() {
        if (this.isShow) {
            if (this.status != 0) {
                this.currentFrame = this.currentFrame.nextFrame;
                if (this.currentFrame == null) {
                    this.currentFrame = this.gifFrame;
                }
            } else if (this.currentFrame.nextFrame != null) {
                this.currentFrame = this.currentFrame.nextFrame;
            }
            return this.currentFrame;
        }
        this.isShow = true;
        return this.gifFrame;
    }

    private int readByte() {
        this.in = new ByteArrayInputStream(this.gifData);
        this.gifData = null;
        return readStream();
    }

    private int readStream() {
        init();
        if (this.in != null) {
            readHeader();
            if (err()) {
                this.action.parseFail(true, true);
            } else {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                    this.action.parseOk(false, -1);
                } else {
                    this.status = -1;
                    this.action.parseOk(true, -1);
                }
            }
            try {
                this.in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.status = 2;
            this.action.parseOk(false, -1);
        }
        return this.status;
    }

    private void decodeImageData() {
//        int code;
//        int i;
//        int npix = this.iw * this.ih;
//        if (this.pixels == null || this.pixels.length < npix) {
//            this.pixels = new byte[npix];
//        }
//        if (this.prefix == null) {
//            this.prefix = new short[4096];
//        }
//        if (this.suffix == null) {
//            this.suffix = new byte[4096];
//        }
//        if (this.pixelStack == null) {
//            this.pixelStack = new byte[FragmentTransaction.TRANSIT_FRAGMENT_OPEN];
//        }
//        int data_size = read();
//        int clear = 1 << data_size;
//        int end_of_information = clear + 1;
//        int available = clear + 2;
//        int old_code = -1;
//        int code_size = data_size + 1;
//        int code_mask = (1 << code_size) - 1;
//        for (code = 0; code < clear; code++) {
//            try {
//                this.prefix[code] = (short) 0;
//                this.suffix[code] = (byte) code;
//            } catch (Exception e) {
//            }
//        }
//        int bi = 0;
//        int first = 0;
//        int count = 0;
//        int bits = 0;
//        int datum = 0;
//        int i2 = 0;
//        int pi = 0;
//        int top = 0;
//        while (i2 < npix) {
//            if (top == 0) {
//                if (bits >= code_size) {
//                    code = datum & code_mask;
//                    datum >>= code_size;
//                    bits -= code_size;
//                    if (code > available) {
//                        break;
//                    } else if (code == end_of_information) {
//                        i = top;
//                        break;
//                    } else if (code == clear) {
//                        code_size = data_size + 1;
//                        code_mask = (1 << code_size) - 1;
//                        available = clear + 2;
//                        old_code = -1;
//                    } else if (old_code == -1) {
//                        i = top + 1;
//                        this.pixelStack[top] = this.suffix[code];
//                        old_code = code;
//                        first = code;
//                        top = i;
//                    } else {
//                        int in_code = code;
//                        if (code == available) {
//                            i = top + 1;
//                            this.pixelStack[top] = (byte) first;
//                            code = old_code;
//                            top = i;
//                        }
//                        while (code > clear) {
//                            i = top + 1;
//                            this.pixelStack[top] = this.suffix[code];
//                            code = this.prefix[code];
//                            top = i;
//                        }
//                        first = this.suffix[code] & MotionEventCompat.ACTION_MASK;
//                        if (available >= 4096) {
//                            i = top;
//                            break;
//                        }
//                        i = top + 1;
//                        this.pixelStack[top] = (byte) first;
//                        this.prefix[available] = (short) old_code;
//                        this.suffix[available] = (byte) first;
//                        available++;
//                        if ((available & code_mask) == 0 && available < 4096) {
//                            code_size++;
//                            code_mask += available;
//                        }
//                        old_code = in_code;
//                    }
//                } else {
//                    if (count == 0) {
//                        count = readBlock();
//                        if (count <= 0) {
//                            i = top;
//                            break;
//                        }
//                        bi = 0;
//                    }
//                    datum += (this.block[bi] & MotionEventCompat.ACTION_MASK) << bits;
//                    bits += 8;
//                    bi++;
//                    count--;
//                }
//            } else {
//                i = top;
//            }
//            i--;
//            int pi2 = pi + 1;
//            this.pixels[pi] = this.pixelStack[i];
//            i2++;
//            pi = pi2;
//            top = i;
//        }
//        i = top;
//        for (i2 = pi; i2 < npix; i2++) {
//            this.pixels[i2] = (byte) 0;
//        }
    }

    private boolean err() {
        return this.status != 0;
    }

    private void init() {
        this.status = 0;
        this.frameCount = 0;
        this.gifFrame = null;
        this.gct = null;
        this.lct = null;
    }

    private int read() {
        int curByte = 0;
        try {
            curByte = this.in.read();
        } catch (Exception e) {
            this.status = 1;
        }
        return curByte;
    }

    private int readBlock() {
        this.blockSize = read();
        int n = 0;
        if (this.blockSize > 0) {
            while (n < this.blockSize) {
                try {
                    int count = this.in.read(this.block, n, this.blockSize - n);
                    if (count == -1) {
                        break;
                    }
                    n += count;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (n < this.blockSize) {
                this.status = 1;
            }
        }
        return n;
    }

    private int[] readColorTable(int ncolors) {
        int nbytes = ncolors * 3;
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;
        try {
            n = this.in.read(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (n < nbytes) {
            this.status = 1;
        } else {
            tab = new int[256];
            int j = 0;
            int i = 0;
            while (i < ncolors) {
                int j2 = j + 1;
                int r = c[j] & MotionEventCompat.ACTION_MASK;
                j = j2 + 1;
                int g = c[j2] & MotionEventCompat.ACTION_MASK;
                j2 = j + 1;
                int i2 = i + 1;
                tab[i] = ((ViewCompat.MEASURED_STATE_MASK | (r << 16)) | (g << 8)) | (c[j] & MotionEventCompat.ACTION_MASK);
                j = j2;
                i = i2;
            }
        }
        return tab;
    }

    private void readContents() {
        boolean done = false;
        while (!done && !err()) {
            switch (read()) {
                case 0:
                    break;
                case CustomConstant.HEADER_HEIGHT /*33*/:
                    switch (read()) {
                        case 249:
                            readGraphicControlExt();
                            break;
                        case MotionEventCompat.ACTION_MASK /*255*/:
                            readBlock();
                            String app = "";
                            for (int i = 0; i < 11; i++) {
                                app = app + ((char) this.block[i]);
                            }
                            if (!app.equals("NETSCAPE2.0")) {
                                skip();
                                break;
                            } else {
                                readNetscapeExt();
                                break;
                            }
                        default:
                            skip();
                            break;
                    }
                case 44:
                    readImage();
                    break;
                case 59:
                    done = true;
                    break;
                default:
                    this.status = 1;
                    break;
            }
        }
    }

    private void readGraphicControlExt() {
        boolean z = true;
        read();
        int packed = read();
        this.dispose = (packed & 28) >> 2;
        if (this.dispose == 0) {
            this.dispose = 1;
        }
        if ((packed & 1) == 0) {
            z = false;
        }
        this.transparency = z;
        int delay_ = readShort();
        this.delay = delay_ < 5 ? 100 : delay_ * 10;
        this.transIndex = read();
        read();
    }

    private void readHeader() {
        String id = "";
        for (int i = 0; i < 6; i++) {
            id = id + ((char) read());
        }
        if (id.startsWith("GIF")) {
            readLSD();
            if (this.gctFlag && !err()) {
                this.gct = readColorTable(this.gctSize);
                this.bgColor = this.gct[this.bgIndex];
                return;
            }
            return;
        }
        this.status = 1;
    }

    private void readImage() {
        boolean z;
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int packed = read();
        if ((packed & 128) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.lctFlag = z;
        if ((packed & 64) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.interlace = z;
        this.lctSize = 2 << (packed & 7);
        if (this.lctFlag) {
            this.lct = readColorTable(this.lctSize);
            this.act = this.lct;
        } else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }
        int save = 0;
        if (this.transparency) {
            save = this.act[this.transIndex];
            this.act[this.transIndex] = 0;
        }
        if (this.act == null) {
            this.status = 1;
        }
        if (!err()) {
            decodeImageData();
            skip();
            if (!err()) {
                this.frameCount++;
                this.image = Bitmap.createBitmap(this.width, this.height, Config.ARGB_4444);
                setPixels();
                if (this.gifFrame == null) {
                    this.gifFrame = new GifFrame(this.image, this.delay);
                    this.currentFrame = this.gifFrame;
                } else {
                    GifFrame f = this.gifFrame;
                    while (f.nextFrame != null) {
                        f = f.nextFrame;
                    }
                    f.nextFrame = new GifFrame(this.image, this.delay);
                }
                if (this.transparency) {
                    this.act[this.transIndex] = save;
                }
                resetFrame();
                this.action.parseOk(true, this.frameCount);
            }
        }
    }

    private void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int packed = read();
        this.gctFlag = (packed & 128) != 0;
        this.gctSize = 2 << (packed & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
    }

    private void readNetscapeExt() {
        do {
            readBlock();
            if (this.block[0] == (byte) 1) {
                this.loopCount = ((this.block[2] & MotionEventCompat.ACTION_MASK) << 8) | (this.block[1] & MotionEventCompat.ACTION_MASK);
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    private int readShort() {
        return read() | (read() << 8);
    }

    private void resetFrame() {
        this.lastDispose = this.dispose;
        this.lrx = this.ix;
        this.lry = this.iy;
        this.lrw = this.iw;
        this.lrh = this.ih;
        this.lastImage = this.image;
        this.lastBgColor = this.bgColor;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.lct = null;
    }

    private void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
