/**
 *
 * @author eagle.sakura
 * @version 2010/07/22 : 新規作成
 */
package eagle.android.thread;

import eagle.android.view.LooperSurfaceView;

/**
 * @author eagle.sakura
 * @version 2010/07/22 : 新規作成
 */
public interface ILoopManager {
    /**
     * サーフェイスを追加する。
     *
     * @author eagle.sakura
     * @param surface
     * @version 2010/05/25 : 新規作成
     */
    public void addSurface(LooperSurfaceView surface);

    /**
     * 全サーフェイスの作成が完了したらtrueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/30 : 新規作成
     */
    public boolean isSurfaceCreateComplete();

    /**
     * サーフェイスが一つでも壊れている場合にtrueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/09/23 : 新規作成
     */
    public boolean isSurfaceDestroyed();

    /**
     * ループ処理を開始する。
     *
     * @author eagle.sakura
     * @version 2010/07/22 : 新規作成
     */
    public void startLoop();

    /**
     * ループを終了し、クラスを破棄する。
     *
     * @author eagle.sakura
     * @version 2010/07/22 : 新規作成
     */
    public void dispose();

    /**
     * フレームレートを指定する。<BR>
     * 負の値を指定した場合の挙動は不定。
     *
     * @author eagle.sakura
     * @param rate
     * @version 2010/07/27 : 新規作成
     */
    public void setFrameRate(int rate);

    /**
     * ループを一時停止する。
     *
     * @author eagle.sakura
     * @version 2010/09/23 : 新規作成
     */
    public boolean loopPause();

    /**
     * 一時停止していたループを再開する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/09/23 : 新規作成
     */
    public boolean loopResume();
}
