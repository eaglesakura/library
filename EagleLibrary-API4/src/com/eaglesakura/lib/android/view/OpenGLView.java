/**
 *
 * @author eagle.sakura
 * @version 2010/05/30 : 新規作成
 */
package com.eaglesakura.lib.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.util.UtilActivity;
import com.eaglesakura.lib.math.Vector2;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/05/30 : 新規作成
 */
public class OpenGLView extends LooperSurfaceView {
    /**
     * OGL管理。
     */
    private GLManager glManager = new GLManager();

    private Context context = null;

    /**
     *
     * @author eagle.sakura
     * @param context
     * @param attr
     * @version 2010/05/24 : 新規作成
     */
    public OpenGLView(Context context, AttributeSet attr) {
        super(context, attr);
        // getHolder().setFormat( PixelFormat.RGBA_8888 );
        // getHolder().setFormat( PixelFormat.RGB_888 );
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        glManager.setSurfaceHolder(getHolder());
        this.context = context;
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @version 2010/05/30 : 新規作成
     */
    public OpenGLView(Context context) {
        super(context);
        // getHolder().setFormat( PixelFormat.RGB_888 );
        //        getHolder().setFormat(PixelFormat.RGB_565);
        // getHolder().setFormat( PixelFormat.TRANSPARENT );

        getHolder().setType(SurfaceHolder.SURFACE_TYPE_GPU);
        glManager.setSurfaceHolder(getHolder());
        this.context = context;
    }

    /**
     * サーフェイスが作成された。
     *
     * @author eagle.sakura
     * @param holder
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (glManager == null) {
            glManager = new GLManager();
        }

        EagleUtil.log("OpenGLView#surfaceCreated");
        EagleUtil.log("arg0 : " + holder);
        EagleUtil.log("cur holder : " + glManager.getSurfaceHolder());
        // glManager.setSurfaceHolder( arg0 );
        // glManager.initGL();
        // glManager.clearTest( );

        Vector2 v = UtilActivity.getDisplaySize(context, new Vector2());
        glManager.setDeviceSize((int) v.x, (int) v.y);
        if (holder.getSurfaceFrame().width() != 0 && holder.getSurfaceFrame().height() != 0) {
            v.set(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
        }
        EagleUtil.log("DisplaySize : " + v);
        glManager.setSurfaceSize((int) v.x, (int) v.y);

        super.surfaceCreated(holder);
    }

    /**
     * GL管理クラスを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    public GLManager getGLManager() {
        return glManager;
    }

    int pixelFormat = 0;

    public int getPixelFormat() {
        return pixelFormat;
    }

    /**
     *
     * @author eagle.sakura
     * @param holder
     * @param format
     * @param width
     * @param height
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.pixelFormat = format;
        if (glManager == null) {
            glManager = new GLManager();
            glManager.autoConfigSpec(format, true);
        }

        super.surfaceChanged(holder, format, width, height);
        Vector2 v = UtilActivity.getDisplaySize(context, new Vector2());
        glManager.setDeviceSize((int) v.x, (int) v.y);
        v.set(width, height);
        EagleUtil.log("DisplaySize : " + v);
        glManager.setSurfaceHolder(holder);
        glManager.setSurfaceSize((int) v.x, (int) v.y);
        if (destroyed) {
            glManager.onResume();
            destroyed = false;
        }
    }

    boolean destroyed = false;

    /**
     * サーフェイスが破棄された。
     *
     * @author eagle.sakura
     * @param holder
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);

        glManager.onPause();
        destroyed = true;
        /*
         * if( glManager != null ) { glManager.dispose(); }
        glManager = null;
         */
    }
}
