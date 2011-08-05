/**
 * OpenGL管理を行う。<BR>
 * 将来的にはデバイス非依存とする。
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;
import javax.microedition.khronos.opengles.GL11ExtensionPack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.view.SurfaceHolder;

import com.eaglesakura.lib.android.math.Matrix4x4;
import com.eaglesakura.lib.math.Vector3;
import com.eaglesakura.lib.util.EagleUtil;

import eagle.android.gles11.GL11Extension;

/**
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
public class GLManager {
    /**
     * 管理しているサーフェイス。
     */
    private SurfaceHolder holder = null;
    /**
     * GL10本体。
     */
    private EGL10 egl = null;
    /**
     * GL本体。
     */
    private GL10 gl10 = null;

    /**
     * GL本体。
     */
    private GL11 gl11 = null;
    private GL11Ext gl11Ext = null;
    private GL11ExtensionPack gl11ExtPack = null;
    private GL11Extension gl11Extension = null;

    /**
     * GLコンテキスト。
     */
    private EGLContext glContext = null;
    /**
     * ディスプレイ。
     */
    private EGLDisplay glDisplay = null;
    /**
     * サーフェイス。
     */
    private EGLSurface glSurface = null;

    /**
     * コンフィグ情報。
     */
    private EGLConfig glConfig = null;

    /**
     * 描画対象の幅。
     */
    private int surfaceWidth = -1;

    /**
     * 描画対象の高さ。
     */
    private int surfaceHeight = -1;

    /**
     * GL11エクステンション機能を有効にするか。
     */
    private static boolean isGL11ExtentionInitialize = true;

    private static GLManager instance;

    public static GLManager getInstance() {
        return instance;
    }

    /**
     * GL描画用スレッドを作成する。
     *
     * @author eagle.sakura
     * @param holder
     * @version 2009/11/14 : 新規作成
     */
    public GLManager() {
        instance = this;
    }

    /**
     * GL11エクステンションの有効・無効を指定する。<BR>
     * デフォルトは有効。
     *
     * @author eagle.sakura
     * @param set
     * @version 2010/09/12 : 新規作成
     */
    public static void setGL11ExtentionEnable(boolean set) {
        isGL11ExtentionInitialize = set;
    }

    /**
     * GLSurfaceViewを使用する。
     *
     * @author eagle.sakura
     * @param holder
     * @param gl
     * @param config
     * @version 2009/11/19 : 新規作成
     */
    public GLManager(SurfaceHolder holder, GL10 gl, EGLConfig config) {
        instance = this;
        this.holder = holder;
        gl10 = gl;
        gl11 = (GL11) gl;
        gl11Ext = (GL11Ext) gl;
        gl11ExtPack = (GL11ExtensionPack) gl;
        glConfig = config;

        _setDefGLStatus();
    }

    /**
     * レンダリングターゲットの大きさを設定する。
     *
     * @author eagle.sakura
     * @param width
     * @param height
     * @version 2010/06/02 : 新規作成
     */
    public void setSurfaceSize(int width, int height) {
        surfaceWidth = width;
        surfaceHeight = height;
    }

    /**
     * GLを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/14 : 新規作成
     */
    public GL10 getGL10() {
        return gl10;
        // return
    }

    /**
     * GLを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/16 : 新規作成
     */
    public GL11 getGL11() {
        return gl11;
    }

    /**
     * GLを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/16 : 新規作成
     */
    public GL11Ext getGL11Ext() {
        return gl11Ext;
    }

    /**
     * GL11用ブリッジを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public GL11Extension getGL11Extension() {
        return gl11Extension;
    }

    /**
     * GLを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/16 : 新規作成
     */
    public GL11ExtensionPack getGL11ExtPack() {
        return gl11ExtPack;
    }

    /**
     * メインで使用するGLインターフェースを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/16 : 新規作成
     */
    public GL11 getGL() {
        return gl11;
    }

    /**
     * EGLを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/14 : 新規作成
     */
    public EGL10 getEGL() {
        return egl;
    }

    /**
     * 通常合成
     */
    public static final int eAlphaBlendNormal = 0;

    /**
     * アルファ合成
     */
    public static final int eAlphaBlendAdd = 1;

    /**
     * 減算合成
     */
    public static final int eAlphaBlendSub = 2;

    /**
     * 乗算合成
     */
    public static final int eAlphaBlendMul = 3;

    /**
     *
     * @param mode
     */
    public void setAlphaBlendMode(int mode) {
        switch (mode) {
        case eAlphaBlendNormal:
            gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            break;
        case eAlphaBlendAdd:
            gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
            break;
        case eAlphaBlendSub:
            break;
        case eAlphaBlendMul:
            gl10.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ZERO);
            break;
        default:
            break;
        }
    }

    /**
     * バッファの消去を行う。
     *
     * @author eagle.sakura
     * @param mask
     * @version 2009/11/29 : 新規作成
     */
    public void clear(int mask) {
        gl11.glClear(mask);
    }

    /**
     * バッファの消去を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/29 : 新規作成
     */
    public void clear() {
        gl11.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * サーフェイスチェック用のテスト描画を行う。
     *
     * @author eagle.sakura
     * @version 2010/06/02 : 新規作成
     */
    public void clearTest() {
        clearColorRGBA(255, 0, 0, 255);
        clear();
        swapBuffers();
    }

    final float[] _lastColors = { 0, 0, 0, 0 };

    /**
     * 描画色を指定する。
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void setColor(float r, float g, float b, float a) {
        gl10.glColor4f(r, g, b, a);
        _lastColors[0] = r;
        _lastColors[1] = g;
        _lastColors[2] = b;
        _lastColors[3] = a;
    }

    /**
     * GLとの同期を行う。
     */
    public void syncGL() {
        setColor(_lastColors[0], _lastColors[1], _lastColors[2], _lastColors[3]);
    }

    /**
     * 最後にsetColor()したRGBAを取得する。
     * @return
     */
    public float[] getLastColors() {
        return _lastColors;
    }

    /**
     * 消去色を設定する。
     *
     * @author eagle.sakura
     * @param r
     * @param g
     * @param b
     * @param a
     * @version 2009/11/29 : 新規作成
     */
    public void clearColorRGBA(float r, float g, float b, float a) {
        gl11.glClearColor(r, g, b, a);
    }

    /**
     * 消去色を指定する。 値は0～255で指定。
     *
     * @author eagle.sakura
     * @param r
     * @param g
     * @param b
     * @param a
     * @version 2009/11/29 : 新規作成
     */
    public void clearColorRGBA(int r, int g, int b, int a) {
        gl11.glClearColorx((r & 0xff) << 8, (g & 0xff) << 8, (b & 0xff) << 8, (a & 0xff) << 8);
    }

    /**
     * 指定した４ｘ４行列をプッシュする。
     *
     * @author eagle.sakura
     * @param trans
     * @version 2009/11/15 : 新規作成
     */
    public void pushMatrixF(Matrix4x4 trans) {
        gl11.glPushMatrix();
        gl11.glMultMatrixf(trans.m, 0);
    }

    /**
     * 行列を取り出す。
     *
     * @author eagle.sakura
     * @version 2009/11/15 : 新規作成
     */
    public void popMatrix() {
        gl11.glPopMatrix();
    }

    public void onResume() {
        try {
            egl.eglDestroySurface(glDisplay, glSurface);
            egl.eglMakeCurrent(glDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, glContext);
            glSurface = egl.eglCreateWindowSurface(glDisplay, glConfig, holder, null);
            egl.eglMakeCurrent(glDisplay, glSurface, glSurface, glContext);
        } catch (Exception e) {
            EagleUtil.log(e);
        }
    }

    /**
     * バックバッファをフロントバッファに送る。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    public void swapBuffers() {
        // 画面に出力するバッファの切り替え

        if (!egl.eglSwapBuffers(glDisplay, glSurface)) {
            EagleUtil.log("swaperror : " + egl.eglGetError());
            try {
                egl.eglDestroySurface(glDisplay, glSurface);
                egl.eglMakeCurrent(glDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, glContext);
                glSurface = egl.eglCreateWindowSurface(glDisplay, glConfig, holder, null);
                egl.eglMakeCurrent(glDisplay, glSurface, glSurface, glContext);
                egl.eglSwapBuffers(glDisplay, glSurface);
            } catch (Exception e) {
                EagleUtil.log(e);
            }
        }

        /*
         * egl.eglSwapBuffers( glDisplay, glSurface ); if( egl.eglGetError() !=
         * EGL10.EGL_SUCCESS ) { try { EagleUtil.log( "swaperror : " +
         * egl.eglGetError() ); egl.eglDestroySurface( glDisplay, glSurface );
         * egl.eglMakeCurrent( glDisplay, EGL10.EGL_NO_SURFACE,
         * EGL10.EGL_NO_SURFACE, glContext ); glSurface =
         * egl.eglCreateWindowSurface( glDisplay, glConfig, holder, null );
         * egl.eglMakeCurrent( glDisplay, glSurface, glSurface, glContext );
         * egl.eglSwapBuffers( glDisplay, glSurface ); } catch( Exception e ) {
         * EagleUtil.log( e ); } }
         */
    }

    /**
     *
     * @author eagle.sakura
     * @param holder
     * @version 2010/05/31 : 新規作成
     */
    public void setSurfaceHolder(SurfaceHolder holder) {
        EagleUtil.log("" + holder);
        EagleUtil.log("" + this.holder);
        this.holder = holder;
    }

    /**
     * サーフェイスを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/23 : 新規作成
     */
    public SurfaceHolder getSurfaceHolder() {
        return holder;
    }

    /**
     * 初期化時のコンフィグスペック。
     */
    private int[] configSpec = {
    /**
     * 2008/12/1 修正 以下の設定が実機では使えないようなのでカット。
     * この部分をはずすと、サポートされている設定が使われる(明示的に設定しないと機種依存で変わる可能性あり?)。
     *
     * EGL10.EGL_RED_SIZE, 5, //! 赤要素：8ビット EGL10.EGL_GREEN_SIZE, 6, //! 緑要素：8ビット
     * EGL10.EGL_BLUE_SIZE, 5, //! 青要素：8ビット // EGL10.EGL_ALPHA_SIZE, 8, //!
     * アルファチャンネル：8ビット EGL10.EGL_DEPTH_SIZE, 16, //! 深度バッファ：16ビット
     */
    EGL10.EGL_NONE // ! 終端にはEGL_NONEを入れる
    };

    /**
     * 初期化設定を入れ替える。
     *
     * @author eagle.sakura
     * @param specs
     * @version 2010/07/27 : 新規作成
     */
    public void setConfigSpec(int[] specs) {
        configSpec = specs;
    }

    /**
     * GL系を初期化する。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    public void initGL() {
        if (egl != null) {
            return;
        }

        // GL ES操作モジュール取得
        egl = (EGL10) EGLContext.getEGL();
        EagleUtil.log("egl : " + egl);
        // EagleUtil.log( "eglGetCurrentContext : " + egl.eglGetCurrentContext()
        // );
        // EagleUtil.log( "eglGetCurrentDisplay : " + egl.eglGetCurrentDisplay()
        // );
        {
            // ディスプレイコネクション作成
            glDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            EagleUtil.log("glDisplay : " + glDisplay);
            EagleUtil.log("ERROR : " + egl.eglGetError());
            if (glDisplay == EGL10.EGL_NO_DISPLAY) {
                return;
            }

            // ディスプレイコネクション初期化
            int[] version = { -1, -1 };
            if (!egl.eglInitialize(glDisplay, version)) {
                EagleUtil.log("eglInitialize error");
                // Log.e(mName, "ディスプレイコネクション初期化失敗");
                return;
            }
            // OpenGLバージョン出力
            EagleUtil.log("OpenGL ES Version[" + version[0] + "." + version[1] + "]");
        }

        {
            // コンフィグ設定
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            if (!egl.eglChooseConfig(glDisplay, configSpec, configs, 1, numConfigs)) {
                // Log.e(mName, "コンフィグ設定失敗");
                return;
            }
            glConfig = configs[0];
            EagleUtil.log("glConfig : " + glConfig);
            EagleUtil.log("ERROR : " + egl.eglGetError());

            if (glConfig != null) {
                int[] value = new int[1];
                EagleUtil.log("LogStart...");
                EagleUtil.log("Message");
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_RED_SIZE, value);
                EagleUtil.log("EGL_RED_SIZE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_GREEN_SIZE, value);
                EagleUtil.log("EGL_GREEN_SIZE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_BLUE_SIZE, value);
                EagleUtil.log("EGL_BLUE_SIZE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_ALPHA_SIZE, value);
                EagleUtil.log("EGL_ALPHA_SIZE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_ALPHA_FORMAT, value);
                EagleUtil.log("EGL_ALPHA_FORMAT : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_PIXEL_ASPECT_RATIO, value);
                EagleUtil.log("EGL_PIXEL_ASPECT_RATIO : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_ALPHA_MASK_SIZE, value);
                EagleUtil.log("EGL_ALPHA_MASK_SIZE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_BUFFER_SIZE, value);
                EagleUtil.log("EGL_BUFFER_SIZE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_COLOR_BUFFER_TYPE, value);
                EagleUtil.log("EGL_COLOR_BUFFER_TYPE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_RENDERABLE_TYPE, value);

                EagleUtil.log("EGL_RENDERABLE_TYPE : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_HEIGHT, value);
                EagleUtil.log("EGL_HEIGHT : " + value[0]);
                egl.eglGetConfigAttrib(glDisplay, configs[0], EGL10.EGL_WIDTH, value);
                EagleUtil.log("EGL_WIDTH : " + value[0]);
                EagleUtil.log("LogEnd...");
            }

        }

        {
            // レンダリングコンテキスト作成
            glContext = egl.eglCreateContext(glDisplay, glConfig, EGL10.EGL_NO_CONTEXT, null);
            EagleUtil.log("glContext : " + glContext);
            EagleUtil.log("ERROR : " + egl.eglGetError());
            if (glContext == EGL10.EGL_NO_CONTEXT) {
                EagleUtil.log("Create Context Error");
                // Log.e(mName, "レンダリングコンテキスト作成失敗");
                return;
            }

            gl10 = (GL10) glContext.getGL();
            gl11 = (GL11) glContext.getGL();
            gl11Ext = (GL11Ext) glContext.getGL();
            gl11ExtPack = (GL11ExtensionPack) glContext.getGL();

            try {
                // ! Extentionの生成を試す。
                EagleUtil.log("SDK Version : " + EagleUtil.getBridge().getPlatformVersion());
                if (isGL11ExtentionInitialize) {
                    gl11Extension = new GL11Extension();
                }
            } catch (Exception e) {
                EagleUtil.log(e);
            }
        }

        {
            // サーフェイス作成(あとで分けるので別メソッド)
            if (!createSurface()) {
                EagleUtil.log("Surface create error...");
                return;
            }
        }

        {
            // ! サーフェイスの大きさを自動指定
            if (surfaceWidth < 0) {
                surfaceWidth = holder.getSurfaceFrame().width();
            }

            if (surfaceHeight < 0) {
                surfaceHeight = holder.getSurfaceFrame().height();
            }
        }

        EagleUtil.log("set default begin");
        _setDefGLStatus();
        EagleUtil.log("set default out");
    }

    private void _setDefGLStatus() {
        GL11 gl = getGL();
        // ! 深度テスト有効
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        /*
        */
        {
            int[] buf = { -1 };
            gl.glGetIntegerv(GL11Ext.GL_MAX_PALETTE_MATRICES_OES, buf, 0);
            EagleUtil.log("GL_MAX_PALETTE_MATRICES_OES : " + buf[0]);

            gl.glGetIntegerv(GL11Ext.GL_MAX_VERTEX_UNITS_OES, buf, 0);
            EagleUtil.log("GL_MAX_VERTEX_UNITS_OES : " + buf[0]);

            gl.glGetIntegerv(GL10.GL_MAX_MODELVIEW_STACK_DEPTH, buf, 0);
            EagleUtil.log("GL_MAX_MODELVIEW_STACK_DEPTH : " + buf[0]);

            gl.glGetIntegerv(GL10.GL_MAX_PROJECTION_STACK_DEPTH, buf, 0);
            EagleUtil.log("GL_MAX_PROJECTION_STACK_DEPTH : " + buf[0]);
        }

        // ! カリング無効
        gl.glDisable(GL10.GL_CULL_FACE);

        // ! 行列無効化
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // !
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glViewport(0, 0, getDisplayWidth(), getDisplayHeight());
        // gl.glViewport( 0, 0, 320, 480 );
        // gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );
        // gl.glHint( GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST );
        // gl.glDepthFunc( GL10.GL_LEQUAL );
        // gl.glShadeModel( GL10.GL_SMOOTH );
        // gl.glDisableClientState( GL10.GL_TEXTURE );
        // gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );

    }

    /**
     * 画面幅を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public int getDisplayWidth() {
        return surfaceWidth;
    }

    /**
     * 画面高さを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public int getDisplayHeight() {
        return surfaceHeight;
    }

    /**
     * ディスプレイアスペクト比（W / H)を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/09/18 : 新規作成
     */
    public float getDisplayAspect() {
        return ((float) surfaceWidth) / ((float) surfaceHeight);
    }

    /**
     * サーフェイス作成。<BR>
     * サーフェイスを作成して、レンダリングコンテキストと結びつける
     *
     * @return 正常終了なら真、エラーなら偽
     */
    private boolean createSurface() {
        if (egl == null) {
            return false;
        }

        {
            EagleUtil.log("eglCreateWindowSurface");
            // if( egl.eglMakeCurrent( glDisplay, EGL10.EGL_NO_SURFACE,
            // EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT ) )
            {
                // EagleUtil.log( "eglMakeCurrent boot ok" );
            }

            // サーフェイス作成
            glSurface = egl.eglCreateWindowSurface(glDisplay, glConfig, holder, null);
            EagleUtil.log("glSurface : " + glSurface);
            if (glSurface == EGL10.EGL_NO_SURFACE) {
                EagleUtil.log("ERROR : " + egl.eglGetError());
                EagleUtil.log("Error eglCreateWindowSurface");
                // Log.e(mName, "サーフェイス作成失敗");
                return false;
            }
        }

        {
            EagleUtil.log("eglMakeCurrent");
            // サーフェイスとレンダリングコンテキスト結びつけ
            if (!egl.eglMakeCurrent(glDisplay, glSurface, glSurface, glContext)) {
                EagleUtil.log("ERROR : " + egl.eglGetError());
                EagleUtil.log("Error eglMakeCurrent");
                // Log.e(mName, "レンダリングコンテキストとの結びつけ失敗");
                return false;
            }
        }

        EagleUtil.log("createSurface exit");
        return true;
    }

    /**
     *
     * @author eagle.sakura
     * @param holder
     * @version 2010/06/10 : 新規作成
     */
    public void onSurfaceChange(SurfaceHolder holder, int width, int height) {
        if (surfaceWidth == width && surfaceHeight == height) {
            return;
        }

        this.holder = holder;
        if (holder.getSurfaceFrame().width() > 0) {
            surfaceWidth = width;
            surfaceHeight = height;
        }

        EagleUtil.log("Surface : " + surfaceWidth + "x" + surfaceHeight);

        // レンダリングコンテキスト破棄
        if (glContext != null) {
            egl.eglDestroyContext(glDisplay, glContext);
            glContext = null;
        }
        // サーフェイス破棄
        if (glSurface != null) {
            // レンダリングコンテキストとの結びつけは解除
            egl.eglMakeCurrent(glDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);

            egl.eglDestroySurface(glDisplay, glSurface);
            glSurface = null;
        }

        createSurface();
    }

    /**
     * GLの終了処理を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    public void dispose() {
        // サーフェイス破棄
        if (glSurface != null) {
            // レンダリングコンテキストとの結びつけは解除
            egl.eglMakeCurrent(glDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);

            egl.eglDestroySurface(glDisplay, glSurface);
            glSurface = null;
        }

        // レンダリングコンテキスト破棄
        if (glContext != null) {
            egl.eglDestroyContext(glDisplay, glContext);
            glContext = null;
        }

        // ディスプレイコネクション破棄
        if (glDisplay != null) {
            egl.eglTerminate(glDisplay);
            glDisplay = null;
        }
    }

    /**
     * GL固定小数点配列に変換する。
     *
     * @author eagle.sakura
     * @param array
     * @param result
     * @return
     * @version 2010/06/24 : 新規作成
     */
    public static int[] toGLFixed(float[] array, int[] result) {
        for (int i = 0; i < array.length; ++i) {
            result[i] = (int) (array[i] * EagleUtil.eGLFixed1_0);
        }
        return result;
    }

    /**
     * GL固定小数点配列に変換する。
     *
     * @author eagle.sakura
     * @param array
     * @param result
     * @return
     * @version 2010/06/24 : 新規作成
     */
    public static IntBuffer toGLFixed(float[] array, IntBuffer result) {
        for (float f : array) {
            result.put((int) (f * EagleUtil.eGLFixed1_0));
        }
        return result;
    }

    /**
     * 単純なキャストをサポートする。
     *
     * @author eagle.sakura
     * @param array
     * @param result
     * @return
     * @version 2010/06/24 : 新規作成
     */
    public static ShortBuffer toShortBuffer(int[] array, ShortBuffer result) {
        for (int n : array) {
            result.put((short) n);
        }
        return result;
    }

    /**
     * カメラ関連の行列をリセットし、単位行列化する。
     *
     * @author eagle.sakura
     * @version 2010/09/18 : 新規作成
     */
    public void resetCamera() {
        gl11.glMatrixMode(GL10.GL_PROJECTION);
        // gl11.glPopMatrix();
        gl11.glLoadIdentity();
        gl11.glMatrixMode(GL10.GL_MODELVIEW);
    }

    /**
     *
     * @author eagle.sakura
     * @param array
     * @param result
     * @return
     * @version 2010/06/27 : 新規作成
     */
    public static short[] toShortBuffer(int[] array, short[] result) {
        for (int i = 0; i < array.length; ++i) {
            result[i] = (short) array[i];
        }
        return result;
    }

    /**
     * スプライト用オブジェクト。
     */
    private VRAMResource sprites = null;

    /**
     * 最後にバインドしたテクスチャ。
     */
    private ITexture spriteTexture = null;

    /**
     * スプライト描画を開始する。
     *
     * @author eagle.sakura
     * @version 2010/10/10 : 新規作成
     */
    public void beginSprite() {
        if (sprites == null) {
            sprites = new VRAMResource(this);

            // ! pos,uv
            sprites.create(2);

            // ! 位置情報
            {
                float[] positions = { -0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, };
                ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer fb = bb.asFloatBuffer();
                fb.put(positions);
                fb.position(0);
                sprites.toGLBuffer(0, fb, fb.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);
            }

            // ! UV情報
            // if( false )
            {
                float[] positions = { 0, 0, 1.0f, 0, 0, 1.0f, 1.0f, 1.0f, };
                ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
                bb.order(ByteOrder.nativeOrder());
                FloatBuffer fb = bb.asFloatBuffer();
                fb.put(positions);
                fb.position(0);
                sprites.toGLBuffer(1, fb, fb.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);
            }

        }

        // ! Posバインド
        {
            gl11.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            sprites.bind(0, GL11.GL_ARRAY_BUFFER);
            gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);

        }

        // ! UVバインド
        {
            gl11.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            sprites.bind(1, GL11.GL_ARRAY_BUFFER);
            gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
        }

        // ! カメラを初期化
        resetCamera();

        // ! ライト無効化
        gl10.glDisable(GL10.GL_LIGHTING);

        gl10.glDisable(GL10.GL_DEPTH_TEST);
    }

    /**
     * 任意の色で四角形を塗りつぶす。
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param w
     * @param h
     * @param colorRGBA
     * @version 2010/10/11 : 新規作成
     */
    public void fillRect(int x, int y, int w, int h, float rotateDeg, int colorRGBA) {
        GL11 gl = gl11;

        // ! 描画色指定
        gl.glColor4x(((colorRGBA >> 24) & 0xff) * 0x10000 / 255, ((colorRGBA >> 16) & 0xff) * 0x10000 / 255, ((colorRGBA >> 8) & 0xff) * 0x10000 / 255,
                ((colorRGBA & 0xff)) * 0x10000 / 255);

        // ! 描画位置を行列で操作する
        {
            float sizeX = (float) w / (float) getDisplayWidth() * 2, sizeY = (float) h / (float) getDisplayHeight() * 2, sx = (float) x
                    / (float) getDisplayWidth() * 2, sy = (float) y / (float) getDisplayHeight() * 2;

            gl.glLoadIdentity();
            gl.glTranslatef(-1.0f + sizeX / 2.0f + sx, 1.0f - sizeY / 2.0f - sy, 0.0f);
            gl.glScalef(sizeX, sizeY, 1.0f);
            gl.glRotatef(rotateDeg, 0.0f, 0.0f, 1.0f);
        }

        bindSpriteTexture(null);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }

    /**
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param w
     * @param h
     * @param rotateDeg
     * @param colorRGBA
     * @param tex
     * @param tx
     * @param ty
     * @param tw
     * @param th
     * @version 2010/10/11 : 新規作成
     */
    public void drawImage(int x, int y, int w, int h, float rotateDeg, int colorRGBA, ITexture tex, int tx, int ty, int tw, int th) {
        GL11 gl = gl11;

        // ! 描画色指定
        gl.glColor4x(((colorRGBA >> 24) & 0xff) * 0x10000 / 255, ((colorRGBA >> 16) & 0xff) * 0x10000 / 255, ((colorRGBA >> 8) & 0xff) * 0x10000 / 255,
                ((colorRGBA & 0xff)) * 0x10000 / 255);

        bindSpriteTexture(tex);
        // ! UV操作
        {
            gl.glMatrixMode(GL10.GL_TEXTURE);
            float sizeX = (float) tw / (float) tex.getWidth(), sizeY = (float) th / (float) tex.getHeight(), sx = (float) tx / (float) tex.getWidth(), sy = (float) tx
                    / (float) tex.getHeight();

            gl.glLoadIdentity();
            gl.glTranslatef(sx, sy, 0.0f);
            gl.glScalef(sizeX, sizeY, 1.0f);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
        }

        // ! 描画位置を行列で操作する
        {
            float sizeX = (float) w / (float) getDisplayWidth() * 2, sizeY = (float) h / (float) getDisplayHeight() * 2, sx = (float) x
                    / (float) getDisplayWidth() * 2, sy = (float) y / (float) getDisplayHeight() * 2;

            gl.glLoadIdentity();
            gl.glTranslatef(-1.0f + sizeX / 2.0f + sx, 1.0f - sizeY / 2.0f - sy, 0.0f);
            gl.glScalef(sizeX, sizeY, 1.0f);
            gl.glRotatef(rotateDeg, 0.0f, 0.0f, 1.0f);
        }

        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
    }

    /**
     *
     * @author eagle.sakura
     * @param tex
     * @version 2010/10/11 : 新規作成
     */
    private void bindSpriteTexture(ITexture tex) {
        if (tex != null) {
            if (tex != spriteTexture) {
                tex.bind();
                spriteTexture = tex;
            }
        } else {
            if (spriteTexture != null) {
                spriteTexture.unbind();
                spriteTexture = null;
            }
        }
    }

    /**
     * バックバッファの内容を撮影し、バッファに収める
     * @return
     */
    public Bitmap captureSurfaceRGB888() {
        final int eDisplayWidth = getDisplayWidth();
        final int eDisplayHeight = getDisplayHeight();
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * eDisplayWidth * eDisplayHeight);
        gl10.glReadPixels(0, 0, eDisplayWidth, eDisplayHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buffer);
        buffer.position(0);

        byte[] source = new byte[4 * eDisplayWidth * eDisplayHeight];
        buffer.get(source);
        buffer = null;
        System.gc();

        Bitmap bmp = Bitmap.createBitmap(getDisplayWidth(), getDisplayHeight(), Config.ARGB_8888);

        for (int y = 0; y < eDisplayHeight; ++y) {
            for (int x = 0; x < eDisplayWidth; ++x) {
                int head = (bmp.getWidth() * y + x) * 4;
                bmp.setPixel(x, eDisplayHeight - y - 1, ((((int) source[head + 3]) & 0xff) << 24) | ((((int) source[head + 0]) & 0xff) << 16)
                        | ((((int) source[head + 1]) & 0xff) << 8) | (((int) source[head + 2]) & 0xff));
            }
        }
        return bmp;
    }

    /**
     * スプライト描画を終了する。
     *
     * @author eagle.sakura
     * @version 2010/10/10 : 新規作成
     */
    public void endSprite() {
        bindSpriteTexture(null);
        GL11 gl = gl11;

        {
            gl.glMatrixMode(GL10.GL_TEXTURE);
            gl.glLoadIdentity();
        }
        {
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }

    /**
     * スクリーン正規化座標系からピクセル座標を取得する。
     *
     * @author eagle.sakura
     * @param screen
     * @param result
     * @return
     * @version 2010/10/04 : 新規作成
     */
    public Vector3 getPixelPosition(Vector3 screen, Vector3 result) {
        result.set((screen.x * getDisplayWidth() + getDisplayWidth()) / 2, getDisplayHeight() - (screen.y * getDisplayHeight() + getDisplayHeight()) / 2,
                screen.z);

        return result;
    }

    /**
     * ピクセル座標から正規化座標を取得する。<BR>
     * ただし、Zはそのまま出力される。
     * @param pixel
     * @param result
     * @return
     */
    public Vector3 getScreenPosition(Vector3 pixel, Vector3 result) {
        result.set(pixel.x / (getDisplayWidth() / 2) - 1.0f, pixel.y / (getDisplayHeight() / 2) - 1.0f, pixel.z);
        result.y = -result.y;

        return result;
    }

    /**
     * アセットからテクスチャを生成する。
     * @param assetFileName
     * @return
     * @throws IOException
     */
    public ITexture createTextureFromAsset(Context context, String assetFileName) throws IOException {
        InputStream is = context.getAssets().open(assetFileName);
        Bitmap bmp = BitmapFactory.decodeStream(is);
        ITexture result = new BmpTexture(bmp, this);
        bmp.recycle();
        is.close();

        return result;
    }

    /**
     * VBOのバッファをひとつ作成する。
     * @return
     */
    public int genVBO() {
        int[] buf = new int[1];
        gl11.glGenBuffers(1, buf, 0);
        return buf[0];
    }

    /**
     * VBOのバッファをひとつ削除する。
     * @param vbo
     */
    public void delVBO(int vbo) {
        gl11.glDeleteBuffers(1, new int[] { vbo }, 0);
    }

    /**
     * テクスチャバッファをひとつ作成する。
     * @return
     */
    public int genTex() {
        int[] buf = new int[1];
        gl11.glGenTextures(1, buf, 0);
        return buf[0];
    }

    /**
     * テクスチャバッファを削除する。
     * @param tex
     */
    public void delTex(int tex) {
        gl11.glDeleteTextures(0, new int[] { tex }, 0);
    }

}
