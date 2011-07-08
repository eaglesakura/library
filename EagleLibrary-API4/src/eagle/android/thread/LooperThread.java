/**
 *
 * @author eagle.sakura
 * @version 2010/05/25 : 新規作成
 */
package eagle.android.thread;

import java.util.ArrayList;
import java.util.List;

import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import eagle.android.view.ILooperSurface;
import eagle.android.view.LooperSurfaceView;
import eagle.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/05/25 : 新規作成
 */
public class LooperThread extends Thread implements ILoopManager, Callback {
    /**
     * 終了待ちフラグ。
     */
    private boolean done = false;

    /**
     * スレッドのループを一時停止する。
     */
    private boolean sleeping = false;

    /**
     * 管理するビュー。
     */
    private List<ILooperSurface> viewList = new ArrayList<ILooperSurface>();

    /**
     * メインループクラス。
     */
    private ILooper looper = null;

    /**
     * フレームレート
     */
    private int frameRate = 15;

    /**
     *
     * @author eagle.sakura
     * @param looper
     * @version 2010/05/31 : 新規作成
     */
    public LooperThread(ILooper looper) {
        setLooper(looper);
    }

    @Override
    public void setFrameRate(int rate) {
        frameRate = rate;
    }

    /**
     * サーフェイスを追加する。
     *
     * @author eagle.sakura
     * @param surface
     * @version 2010/05/25 : 新規作成
     */
    @Override
    public void addSurface(LooperSurfaceView surface) {
        if (surface == null) {
            return;
        }

        if (viewList.size() == 0) {
            surface.setTouchDisplay(looper.getTouchDisplay());
        }

        surface.getHolder().addCallback(this);
        viewList.add(surface); // !< サーフェイスを登録する。
    }

    /**
     * 全サーフェイスの作成が完了したらtrueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    @Override
    public boolean isSurfaceCreateComplete() {
        for (ILooperSurface view : viewList) {
            // ! 未作成のクラスがあった。
            if (!view.isCreated()) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @author eagle.sakura
     * @param looper
     * @version 2010/05/31 : 新規作成
     */
    public void setLooper(ILooper looper) {
        this.looper = looper;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/31 : 新規作成
     */
    public ILooper getLooper() {
        return looper;
    }

    /**
     * ループの開始時に呼ばれる。
     *
     * @author eagle.sakura
     * @version 2009/11/15 : 新規作成
     */
    protected void onLoopBegin() {
        if (looper != null) {
            looper.onInitialize();
        }
    }

    /**
     * スレッドの休止設定を行う。
     *
     * @author eagle.sakura
     * @param is
     * @version 2010/06/11 : 新規作成
     */
    public void setSleeping(boolean is) {
        sleeping = is;
    }

    /**
     * スレッドのループ休止命令が与えられている場合、trueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/06/11 : 新規作成
     */
    public boolean isSleeping() {
        return sleeping;
    }

    /**
     * 毎フレームのループを行う。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    protected void onLoop() {
        if (looper != null && !isSleeping()) {
            long sTime = System.currentTimeMillis();
            // ! フレーム処理
            {
                looper.onLoop();
            }
            long eTime = System.currentTimeMillis();

            // ! 1フレームにかけていい時間
            final int frameMilliSec = (1000 / frameRate);
            int sleepTime = frameMilliSec - (int) (eTime - sTime);
            sleepTime = Math.max(1, sleepTime);

            // ! スレッドを休眠させる
            try {
                sleep(sleepTime);
            } catch (Exception e) {
                EagleUtil.log(e);
            }
        } else {
            try {
                sleep(1000);
            } catch (Exception e) {

            }
            // done = true;
        }
    }

    /**
     * 終了処理を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    protected void onFinalize() {
        if (looper != null) {
            looper.onFinalize();
            viewList.clear();
            looper = null;
        }
    }

    /**
     * スレッド処理を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    @Override
    public void run() {
        onLoopBegin();
        // ! 終了要求まで繰り返す。
        while (!done) {
            onLoop();
        }
        onFinalize();
    }

    /**
     * ループを開始する。
     *
     * @author eagle.sakura
     * @version 2010/07/22 : 新規作成
     */
    @Override
    public void startLoop() {
        // ! スレッドを開始させる
        start();
    }

    /**
     * スレッド終了要求。<BR>
     * スレッドに終了要求を出して、停止するのを待つ。
     */
    @Override
    public void dispose() {
        synchronized (this) {
            // 終了要求を出す
            done = true;
        }

        try {
            // スレッド終了を待つ
            join();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (isSurfaceCreateComplete()) {
            EagleUtil.log("Surface create complete thread");
            startLoop();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public boolean loopPause() {
        setSleeping(true);
        return true;
    }

    @Override
    public boolean loopResume() {
        setSleeping(false);
        return true;
    }

    @Override
    public boolean isSurfaceDestroyed() {
        return false;
    }
}
