package com.eaglesakura.lib.android.appcore;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.view.CanvasView;
import com.eaglesakura.lib.android.view.OpenGLView;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * ゲームループとそれに付随するViewを管理する。
 * ゲームループはUIスレッドとは別スレッドを利用していることに注意すること。
 */
public abstract class GameLoopManager {

    /**
     * フレームレートをFPS単位で指定する。
     */
    int frameRate = 30;

    /**
     * 呼び出し元
     */
    Context context = null;

    /**
     * OpenGL用
     */
    OpenGLView glView = null;

    /**
     * Canvas用
     */
    CanvasView canvasView = null;

    /**
     * 各Layoutを格納する。
     */
    FrameLayout rootLayout = null;

    /**
     * 毎フレームのランナー
     */
    Runnable frameRunner = new Runnable() {

        @Override
        public void run() {
            final long frameTime = 1000 / frameRate;
            final long start = System.currentTimeMillis();
            {
                onGameFrame();
            }
            final long end = System.currentTimeMillis();
            final long nextTime = Math.max(1, frameTime - (end - start));
            if (isNextFrameEnable()) {
                gameHandle.postDelayed(this, nextTime);
            }
        }
    };

    /**
     * 親クラスが実装している。
     */
    ILoopParent loopParent = null;

    /**
     * UIスレッド用ハンドル。
     */
    Handler uiHandle = null;

    /**
     * ゲームループ用ハンドラ。
     */
    Handler gameHandle = null;

    /**
     * メインランニング用スレッド
     */
    Thread thread = new Thread() {
        public void run() {
            //! Viewの生成が完了するまで待つ
            waitViewCreated();

            Looper.prepare();
            gameHandle = new Handler();
            onGameInitialize();
            gameHandle.post(frameRunner);

            Looper.loop();
        };
    };

    /**
     *
     * @param context
     * @param loopParent
     */
    public GameLoopManager(Context context, ILoopParent loopParent) {
        this.context = context;
        this.loopParent = loopParent;
        this.uiHandle = new Handler();

        createViews();
    }

    /**
     * フレームレートをFPS単位で指定する。
     * @param frameRate
     */
    public void setFrameRateParSec(int frameRate) {
        this.frameRate = frameRate;
    }

    void createViews() {
        rootLayout = new FrameLayout(context);
        {
            glView = new OpenGLView(context);
        }
        {
            canvasView = new CanvasView(context);
            canvasView.setZOrderOnTop(true);
        }
        rootLayout.addView(glView);
        rootLayout.addView(canvasView);
    }

    /**
     * すべてのViewが作成し終わるまで待つ。
     */
    void waitViewCreated() {
        while (!glView.isCreated() || !canvasView.isCreated()) {
            EagleUtil.sleep(10);
        }
    }

    /**
     * GL管理クラスを取得する。
     * @return
     */
    public GLManager getGLManager() {
        return glView.getGLManager();
    }

    /**
     *
     * @return
     */
    public OpenGLView getGLView() {
        return glView;
    }

    /**
     *
     * @return
     */
    public CanvasView getCanvasView() {
        return canvasView;
    }

    /**
     * 描画用のViewを取得する。
     * @return
     */
    public ViewGroup getRootView() {
        return rootLayout;
    }

    /**
     * 呼び出し元が実装する必要があるinterface
     */
    public interface ILoopParent {
        public boolean isFinished();
    }

    /**
     * ゲーム実行用のハンドルを取得する。
     * @return
     */
    public Handler getGameHandle() {
        return gameHandle;
    }

    /**
     * 次のフレームの動作を許す場合true
     * @return
     */
    boolean isNextFrameEnable() {
        return !loopParent.isFinished() && !gamePaused && !activityPaused;
    }

    boolean activityPaused = false;
    boolean gamePaused = false;

    /**
     * ゲームの初期化を行うフェイズ。
     */
    public abstract void onGameInitialize();

    /**
     * ゲームの終了処理を行うフェイズ。
     */
    public abstract void onGameFinalize();

    /**
     * 毎フレームの処理を行う。
     */
    public abstract void onGameFrame();

    /**
     * ゲームループを開始する。
     */
    public void startGameLoop() {
        thread.start();
    }

    /**
     * ゲームループを終了する。
     */
    public void pauseGameLoop() {
        gamePaused = true;
        gameHandle.removeCallbacks(frameRunner);
    }

    /**
     * ゲームループを再開する。
     */
    public void resumeGameLoop() {
        gamePaused = false;
        gameHandle.post(frameRunner);
    }

    /**
     * Activity#onPause
     */
    public void onPause() {
        gameHandle.removeCallbacks(frameRunner);
        activityPaused = true;
    }

    /**
     * Activity#onResume
     */
    public void onResume() {
        if (activityPaused) {
            gameHandle.post(frameRunner);
            activityPaused = false;
        }
    }
}
