/**
 *
 */
package eagle.android.graphic;

import android.graphics.Bitmap;

/**
 * 簡単なアンチエイリアスを搭載したイメージフィルタ。
 */
public class AAImageFilter {
    private float scale = 1.0f;
    private int xOffset = 0;
    private int yOffset = 0;

    /**
     *
     */
    public AAImageFilter() {

    }

    /**
     * 拡大率を調整する。
     *
     * @param f
     */
    public void setScaling(float f) {
        scale = f;
    }

    public float getScaling() {
        return scale;
    }

    /**
     * XY移動量を指定する。
     *
     * @param x
     * @param y
     */
    public void setOffset(int x, int y) {
        xOffset = x;
        yOffset = y;
    }

    public void addOffset(int x, int y) {
        xOffset += x;
        yOffset += y;
    }

    public int getOffsetX() {
        return xOffset;
    }

    public void setOffsetX(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getOffsetY() {
        return yOffset;
    }

    public void setOffsetY(int yOffset) {
        this.yOffset = yOffset;
    }

    private Bitmap source = null;

    public void setSource(Bitmap bmp) {
        source = bmp;
    }

    public int getPixels(int y, int height, int[] result) {
        int src_width = source.getWidth();
        int src_height = source.getHeight();

        if ((y + height) > src_height) {
            y = src_height - height;
        }

        source.getPixels(result, 0, src_width, 0, y, src_width, height);

        return y;
    }

    /**
     *
     * @param src
     * @param dst
     * @return
     */
    public Bitmap scaling(Bitmap dst) {
        float scale = this.scale;

        int dst_width = dst.getWidth();
        int dst_height = dst.getHeight();
        int src_width = source.getWidth();

        int[] dstPixels = new int[dst_width * dst_height];

        final int lines = Math.min(source.getHeight(), 50);
        int[] srcLine = new int[src_width * lines];
        int header = 0;
        int range_start = 0;
        int range_end = 0;

        int srcLine_length = srcLine.length;
        int dstPixels_length = dstPixels.length;
        int baseLine = 0;
        int refLine = 0;
        int r = 0, g = 0, b = 0, color = 0;
        int baseX = 0, refX = 0;

        int yOff = (int) (yOffset / scale);
        int xOff = (int) (xOffset / scale);

        for (int y = yOff; y < dst_height; ++y) {
            baseLine = (int) (y * 256 / scale);
            refLine = ((int) (baseLine + 128));

            baseLine >>= 8;
            refLine >>= 8;

            baseLine -= yOff;
            refLine -= yOff;

            // ! 元ラインを得る
            {
                int line = (baseLine + (baseLine > refLine ? -1 : 1));
                if (line < range_start || line >= range_end) {
                    /*
                     * src.getPixels( srcLine, 0, src_width, 0, line, src_width,
                     * lines );
                     */
                    line = getPixels(line, lines, srcLine);
                    range_start = line;
                    range_end = line + lines;
                }
                header = line - range_start;
            }

            if (baseLine < refLine) {
                baseLine = 0;
                refLine = 1;
            } else {
                baseLine = 1;
                refLine = 0;
            }

            baseLine += header;
            refLine += header;

            for (int x = 0; x < dst_width; ++x) {
                // ! 参照する中心
                baseX = (int) (x * 256 / scale);
                refX = ((int) (baseX + 128));

                baseX >>= 8;
                refX >>= 8;

                baseX -= xOff;
                refX -= xOff;

                {
                    color = srcLine[((src_width * baseLine + baseX) + srcLine_length) % srcLine_length];
                    r = (((color) >> 16) & 0xff);
                    g = (((color) >> 8) & 0xff);
                    b = (((color) >> 0) & 0xff);
                }
                {
                    color = srcLine[((src_width * refLine + refX) + srcLine_length) % srcLine_length];
                    r += (((color) >> 16) & 0xff);
                    g += (((color) >> 8) & 0xff);
                    b += (((color) >> 0) & 0xff);
                }
                {
                    color = srcLine[((src_width * refLine + baseX) + srcLine_length) % srcLine_length];
                    r += (((color) >> 16) & 0xff);
                    g += (((color) >> 8) & 0xff);
                    b += (((color) >> 0) & 0xff);
                }
                {
                    color = srcLine[((src_width * baseLine + refX) + srcLine_length) % srcLine_length];
                    r += (((color) >> 16) & 0xff);
                    g += (((color) >> 8) & 0xff);
                    b += (((color) >> 0) & 0xff);
                }
                r >>= 2;
                g >>= 2;
                b >>= 2;

                dstPixels[(y * dst_width + x + dstPixels_length) % dstPixels_length] = 0xff000000 | (r << 16) | (g << 8) | (b << 0);
            }
        }
        dst.setPixels(dstPixels, 0, dst_width, 0, 0, dst_width, dst_height);
        return dst;
    }

    /**
     *
     * @param src
     * @param dst
     * @return
     */
    public Bitmap scaling2(Bitmap dst) {
        float scale = this.scale;

        int dst_width = dst.getWidth();
        int dst_height = dst.getHeight();
        int src_width = source.getWidth();
        int src_height = source.getHeight();

        int[] dstPixels = new int[dst_width * dst_height];

        int[] srcLine = new int[src_width * 2];

        int srcLine_length = srcLine.length;
        int dstPixels_length = dstPixels.length;
        int baseLine = 0;
        int refLine = 0;
        int r = 0, g = 0, b = 0, color = 0;
        int baseX = 0, refX = 0;

        int yOff = (int) (yOffset / scale);
        int xOff = (int) (xOffset / scale);

        for (int y = yOff; y < dst_height; ++y) {
            baseLine = (int) (y * 256 / scale);
            refLine = ((int) (baseLine + 128));

            baseLine >>= 8;
            refLine >>= 8;

            baseLine -= yOff;
            refLine -= yOff;

            // ! 元ラインを得る
            {
                int line = (baseLine + (baseLine > refLine ? -1 : 1));
                line = (line >= (src_height - 2) ? (src_height - 2) : line);
                source.getPixels(srcLine, 0, src_width, 0, line, src_width, 2);
            }

            if (baseLine < refLine) {
                baseLine = 0;
                refLine = 1;
            } else {
                baseLine = 1;
                refLine = 0;
            }

            for (int x = 0; x < dst_width; ++x) {
                // ! 参照する中心
                baseX = (int) (x * 256 / scale);
                refX = ((int) (baseX + 128));

                baseX >>= 8;
                refX >>= 8;

                baseX -= xOff;
                refX -= xOff;

                {
                    color = srcLine[((src_width * baseLine + baseX) + srcLine_length) % srcLine_length];
                    r = (((color) >> 16) & 0xff);
                    g = (((color) >> 8) & 0xff);
                    b = (((color) >> 0) & 0xff);
                }
                {
                    color = srcLine[((src_width * refLine + refX) + srcLine_length) % srcLine_length];
                    r += (((color) >> 16) & 0xff);
                    g += (((color) >> 8) & 0xff);
                    b += (((color) >> 0) & 0xff);
                }
                {
                    color = srcLine[((src_width * refLine + baseX) + srcLine_length) % srcLine_length];
                    r += (((color) >> 16) & 0xff);
                    g += (((color) >> 8) & 0xff);
                    b += (((color) >> 0) & 0xff);
                }
                {
                    color = srcLine[((src_width * baseLine + refX) + srcLine_length) % srcLine_length];
                    r += (((color) >> 16) & 0xff);
                    g += (((color) >> 8) & 0xff);
                    b += (((color) >> 0) & 0xff);
                }
                r >>= 2;
                g >>= 2;
                b >>= 2;

                dstPixels[(y * dst_width + x + dstPixels_length) % dstPixels_length] = 0xff000000 | (r << 16) | (g << 8) | (b << 0);
            }
        }
        dst.setPixels(dstPixels, 0, dst_width, 0, 0, dst_width, dst_height);
        return dst;
    }
}
