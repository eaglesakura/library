/**
 * サーフェース内のCanvasを管理する。
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
package eagle.android.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;

/**
 * プリミティブの描画を管理する。<BR>
 * dojaでの同名クラスに近い動作を行う。
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
public class Graphics {
    private Canvas canvas = null;
    private Paint paint = new Paint();

    /**
     * @author eagle.sakura
     * @param target
     * @version 2009/11/29 : 新規作成
     */
    public Graphics() {
        setFontSize(24);
    }

    /**
     * 描画用のキャンバスを指定する。
     *
     * @author eagle.sakura
     * @param canvas
     * @version 2009/12/11 : 新規作成
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * 背景を特定色でクリアする。
     *
     * @author eagle.sakura
     * @param r
     * @param g
     * @param b
     * @param a
     * @version 2009/11/29 : 新規作成
     */
    public void clearRGBA(int r, int g, int b, int a) {
        canvas.drawColor(toColorARGB(a, r, g, b), Mode.CLEAR);
    }

    /**
     * 幅を取得する。
     *
     * @author eagle.sakura
     * @version 2009/11/30 : 新規作成
     */
    public int getWidth() {
        return canvas.getWidth();
    }

    /**
     * 高さを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/30 : 新規作成
     */
    public int getHeight() {
        return canvas.getHeight();
    }

    /**
     * ロックされたキャンバスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * ペイントクラスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * 描画色を指定する。
     *
     * @author eagle.sakura
     * @param a
     * @param r
     * @param g
     * @param b
     * @version 2009/11/14 : 新規作成
     */
    public void setColorARGB(int a, int r, int g, int b) {
        paint.setColor(toColorARGB(a, r, g, b));
    }

    /**
     * 描画色を指定する。
     *
     * @author eagle.sakura
     * @param argb
     * @version 2009/12/02 : 新規作成
     */
    public void setColorARGB(int argb) {
        paint.setColor(argb);
    }

    /**
     * アンチエイリアスの有効・無効の指定を行う。<BR>
     * デフォルトは無効である。
     *
     * @author eagle.sakura
     * @param set
     * @version 2010/05/23 : 新規作成
     */
    public void setAntiAlias(boolean set) {
        paint.setAntiAlias(set);
    }

    /**
     * 描画する太さを指定する。
     * @param size
     */
    public void setStrokeSize(int size) {
        paint.setStrokeWidth(size);
    }

    /**
     * イメージの描画を行う。
     *
     * @author eagle.sakura
     * @param bmp
     * @param src
     * @param dst
     * @version 2009/11/30 : 新規作成
     */
    public void drawBitmap(Bitmap bmp, Rect src, Rect dst) {
        canvas.drawBitmap(bmp, src, dst, paint);
    }

    /**
     * イメージ描画を行う。
     *
     * @author eagle.sakura
     * @param bmp
     * @param x
     * @param y
     * @version 2009/11/30 : 新規作成
     */
    public void drawBitmap(Bitmap bmp, int x, int y) {
        canvas.drawBitmap(bmp, (float) x, (float) y, paint);
    }

    /**
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param r
     * @version 2010/05/26 : 新規作成
     */
    public void drawCircle(int x, int y, float r) {
        paint.setStyle(Style.STROKE);
        canvas.drawCircle((float) x, (float) y, r, paint);
    }

    /**
     * 四角形の塗りつぶしを行う。
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param w
     * @param h
     * @version 2009/11/29 : 新規作成
     */
    public void fillRect(int x, int y, int w, int h) {
        paint.setStyle(Style.FILL);
        canvas.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), paint);
    }

    /**
     * 四角形の外枠を描画する。
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param w
     * @param h
     * @version 2009/11/29 : 新規作成
     */
    public void drawRect(int x, int y, int w, int h) {
        paint.setStyle(Style.STROKE);
        canvas.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), paint);
    }

    /**
     * 左下を起点に文字列を描画する。
     *
     * @author eagle.sakura
     * @param str
     * @param x
     * @param y
     * @param start
     * @param end
     * @version 2009/11/14 : 新規作成
     */
    public void drawStringLB(String str, int x, int y, int start, int end) {
        if (start < 0 || end < 0) {
            start = 0;
            end = str.length();
        }
        paint.setStyle(Style.STROKE);
        canvas.drawText(str, start, end, (float) x, (float) y, paint);
    }

    /**
     * 左上を起点に文字列を描画する。
     *
     * @author eagle.sakura
     * @param str
     * @param x
     * @param y
     * @param start
     * @param end
     * @version 2009/11/14 : 新規作成
     */
    public void drawStringLT(String str, int x, int y, int start, int end) {
        if (start < 0 || end < 0) {
            start = 0;
            end = str.length();
        }
        paint.setStyle(Style.STROKE);
        canvas.drawText(str, start, end, (float) x, (float) (y + getStringHeight(str)), paint);
    }

    /**
     * テキストを左揃えにする。
     */
    public static final int eStringFlagLeft = 0;
    /**
     * テキストを下揃えにする。
     */
    public static final int eStringFlagBottom = 0;
    /**
     * テキストを右揃えにする。
     */
    public static final int eStringFlagRight = 1 << 0;

    /**
     * テキストを上揃えにする。
     */
    public static final int eStringFlagTop = 1 << 1;
    /**
     * 中央揃えにする。
     */
    public static final int eStringFlagXCenter = 1 << 2;

    /**
     * 中央揃えにする。
     */
    public static final int eStringFlagYCenter = 1 << 3;

    public void setFillMode(boolean isFill) {
        paint.setStyle(isFill ? Style.FILL : Style.STROKE);
    }

    /**
     * 文字列を描画する。
     *
     * @author eagle.sakura
     * @param str
     * @param x
     * @param y
     * @param start
     * @param end
     * @param flags
     * @version 2009/12/13 : 新規作成
     */
    public void drawString(String str, int x, int y, int start, int end, int flags) {
        if (start < 0 || end < 0) {
            start = 0;
            end = str.length();
        }

        // y -= paint.getFontMetrics().descent;
        int width = getStringWidth(str, start, end);
        int height = getStringHeight(str);

        if ((flags & eStringFlagRight) != 0) {
            x -= width;
        } else if ((flags & eStringFlagXCenter) != 0) {
            x -= (width >> 1);
        }

        if ((flags & eStringFlagTop) != 0) {
            y += height;
        } else if ((flags & eStringFlagYCenter) != 0) {
            y += (height >> 1);
        }
        canvas.drawText(str, start, end, (float) x, (float) y, paint);
    }

    /**
     * 文字列の高さを取得する。
     *
     * @author eagle.sakura
     * @param str
     * @return
     * @version 2009/12/13 : 新規作成
     */
    public int getStringHeight(String str) {
        Rect r = srcRect;
        paint.getTextBounds(str, 0, str.length(), r);
        return r.height();
    }

    /**
     * 文字列の幅を取得する。
     *
     * @author eagle.sakura
     * @param str
     * @param start
     * @param end
     * @return
     * @version 2009/12/13 : 新規作成
     */
    public int getStringWidth(String str, int start, int end) {
        Rect r = srcRect;
        paint.getTextBounds(str, start, end, r);
        return r.width();
    }

    /**
     * 文字列の幅を取得する。
     *
     * @author eagle.sakura
     * @param str
     * @return
     * @version 2009/12/13 : 新規作成
     */
    public int getStringWidth(String str) {
        Rect r = srcRect;
        paint.getTextBounds(str, 0, str.length(), r);
        return r.width();
    }

    /**
     * 線を描画する。
     *
     * @author eagle.sakura
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @version 2009/12/02 : 新規作成
     */
    public void drawLine(int startX, int startY, int endX, int endY) {
        paint.setStyle(Style.STROKE);
        canvas.drawLine((float) startX, (float) startY, (float) endX, (float) endY, paint);
    }

    /**
     * 描画テキストのサイズを指定する。
     *
     * @author eagle.sakura
     * @param size
     * @version 2009/11/14 : 新規作成
     */
    public void setFontSize(int size) {
        paint.setTextSize((float) size);
    }

    /**
     * ARGB色に変換する。
     *
     * @author eagle.sakura
     * @param a
     * @param r
     * @param g
     * @param b
     * @return
     * @version 2009/11/14 : 新規作成
     */
    public static int toColorARGB(int a, int r, int g, int b) {
        return Color.argb(a, r, g, b);
    }

    private static Rect srcRect = new Rect();
}
