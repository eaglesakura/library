/**
 *
 * @author eagle.sakura
 * @version 2010/05/31 : 新規作成
 */
package eagle.android.thread;

import eagle.android.device.TouchDisplay;

/**
 * @author eagle.sakura
 * @version 2010/05/31 : 新規作成
 */
public abstract class ILooper {
    private TouchDisplay touchDisplay = new TouchDisplay();

    /**
     * タッチディスプレイ管理を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/31 : 新規作成
     */
    public TouchDisplay getTouchDisplay() {
        return touchDisplay;
    }

    /**
     * タッチディスプレイ管理クラスを設定する。
     *
     * @author eagle.sakura
     * @param td
     * @version 2010/07/20 : 新規作成
     */
    public void setTouchDisplay(TouchDisplay td) {
        touchDisplay = td;
    }

    /**
     * ループの最初に呼ばれる。
     *
     * @author eagle.sakura
     * @version 2010/05/31 : 新規作成
     */
    public abstract void onInitialize();

    /**
     * 毎フレームの定期処理が呼ばれる。
     *
     * @author eagle.sakura
     * @version 2010/05/31 : 新規作成
     */
    public abstract void onLoop();

    /**
     * ループの終了時に呼ばれる。
     *
     * @author eagle.sakura
     * @version 2010/05/31 : 新規作成
     */
    public abstract void onFinalize();

}
