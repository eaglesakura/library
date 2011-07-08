/**
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
package eagle.util;

/**
 * フレームレートの制御を行う。
 *
 * @author eagle.sakura
 * @version 2009/11/29 : 新規作成
 */
public class FrameController {
    private int defFrameTime = 0;
    private float realFps = 0;
    private long beforeFpsTime = 0;

    /**
     * フレームレートを制御する。
     *
     * @author eagle.sakura
     * @param rate
     * @version 2009/11/29 : 新規作成
     */
    public FrameController(int rate) {
        setFrameRate(rate);
    }

    /**
     * フレームレートを指定する。
     *
     * @author eagle.sakura
     * @param rate
     * @version 2009/11/29 : 新規作成
     */
    public void setFrameRate(int rate) {
        defFrameTime = (1000 / rate);
    }

    /**
     * 実際のフレームレートを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public float getFrameRate() {
        return realFps;
    }

    /**
     * 毎フレームの更新を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/29 : 新規作成
     */
    public void update() {
        long now = System.currentTimeMillis();
        long offset = now - beforeFpsTime;
        beforeFpsTime = now;

        realFps = (1000.0f / (float) offset);

        if ((defFrameTime - offset) > 0) {
            try {
                Thread.sleep(defFrameTime - offset);
            } catch (Exception e) {

            }
        }
        beforeFpsTime = System.currentTimeMillis();
    }
}
