/**
 *
 * @author eagle.sakura
 * @version 2010/05/25 : 新規作成
 */
package eagle.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import eagle.android.device.TouchDisplay;
import eagle.util.EagleUtil;

/**
 * @author eagle.sakura
 * @version 2010/05/25 : 新規作成
 */
public class LooperSurfaceView extends SurfaceView implements SurfaceHolder.Callback, ILooperSurface {
    /**
     * サーフェイスの作成が完了したらtrue。
     */
    private boolean created = false;

    /**
     * ディスプレイ管理クラス。<BR>
     * nullの場合、関連処理を行わない。
     */
    private TouchDisplay touchDisplay = null;

    /**
     * ループを行うサーフェイスを扱う。
     *
     * @author eagle.sakura
     * @param context
     * @param attribute
     * @version 2010/05/25 : 新規作成
     */
    public LooperSurfaceView(Context context, AttributeSet attribute) {
        super(context, attribute);
        getHolder().addCallback(this);
    }

    /**
     *
     * @author eagle.sakura
     * @param display
     * @version 2010/05/31 : 新規作成
     */
    public void setTouchDisplay(TouchDisplay display) {
        touchDisplay = display;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/31 : 新規作成
     */
    public TouchDisplay getTouchDisplay() {
        return touchDisplay;
    }

    /**
     *
     * @author eagle.sakura
     * @param event
     * @return
     * @version 2010/05/31 : 新規作成
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchDisplay != null) {
            touchDisplay.onTouchEvent(event);
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     *
     * @author eagle.sakura
     * @param context
     * @version 2010/05/30 : 新規作成
     */
    public LooperSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    /**
     * サーフェイスが作成されている場合、trueを返す。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/25 : 新規作成
     */
    @Override
    public boolean isCreated() {
        return created;
    }

    /**
     * 終了処理を行う。
     *
     * @author eagle.sakura
     * @version 2010/05/25 : 新規作成
     */
    public void dispose() {

    }

    /**
     * サーフェイスが作成された。
     *
     * @author eagle.sakura
     * @param arg0
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        EagleUtil.log("surfaceCreated : " + this);
    }

    /**
     * サーフェイスが変更された。
     *
     * @author eagle.sakura
     * @param holder
     * @param format
     * @param width
     * @param height
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        EagleUtil.log("surfaceChanged : " + this);
        created = true;
    }

    /**
     * サーフェイスが破棄された。
     *
     * @author eagle.sakura
     * @param holder
     * @version 2010/05/24 : 新規作成
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        EagleUtil.log("surfaceDestroyed : " + this);
    }
}
