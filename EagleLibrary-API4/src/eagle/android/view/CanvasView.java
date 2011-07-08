/**
 *
 * @author eagle.sakura
 * @version 2010/05/24 : 新規作成
 */
package eagle.android.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import eagle.android.graphic.SurfaceCanvas;

/**
 * Canvasによる描画が可能なビュー。
 *
 * @author eagle.sakura
 * @version 2010/05/24 : 新規作成
 */
public class CanvasView extends LooperSurfaceView {
    private SurfaceCanvas canvas = null;

    /**
     *
     * @author eagle.sakura
     * @param context
     * @param attr
     * @version 2010/05/24 : 新規作成
     */
    public CanvasView(Context context, AttributeSet attr) {
        super(context, attr);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        canvas = new SurfaceCanvas(null);
        canvas.setHolder(getHolder());

    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @version 2010/07/13 : 新規作成
     */
    public CanvasView(Context context) {
        super(context);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        canvas = new SurfaceCanvas(null);
        canvas.setHolder(getHolder());
    }

    /**
     * サーフェイスのロックを行う。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/25 : 新規作成
     */
    public boolean lock() {
        return canvas.lock();
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        //        canvas.setHolder(arg0);
        super.surfaceCreated(arg0);
    }

    /**
     * 描画用ラッパーを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    public SurfaceCanvas getGraphics() {
        return canvas;
    }

    /**
     * サーフェイスの描画終了と転送を行う。
     *
     * @author eagle.sakura
     * @version 2010/05/25 : 新規作成
     */
    public void unlock() {
        canvas.unlock();
    }

}
