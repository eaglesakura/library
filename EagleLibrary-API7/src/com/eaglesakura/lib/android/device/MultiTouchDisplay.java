/**
 *
 * @author eagle.sakura
 * @version 2010/07/20 : 新規作成
 */
package com.eaglesakura.lib.android.device;

import android.view.MotionEvent;

import com.eaglesakura.lib.math.Vector2;
import com.eaglesakura.lib.util.EagleUtil;

/**
 * マルチタッチ対応のディスプレイ管理。
 * @author eagle.sakura
 * @version 2010/07/20 : 新規作成
 */
public class MultiTouchDisplay extends TouchDisplay {
    /**
     * ピンチ認識時、最低限動かしている必要があるドラッグの長さ。
     */
    private float pinchLength = 40.0f;

    /**
     * 属性情報。
     */
    private int attrNow = 0;

    /**
     * 前フレームの属性情報。
     */
    private int attrBefore = 0;

    /**
     * ピンチインが行われている。
     */
    private static final int eAttrPichIn = 0x1 << 0;

    /**
     * ピンチアウトが行われている。
     */
    private static final int eAttrPinchOut = 0x1 << 1;

    /**
     * ２箇所のタッチ管理を行う。
     * @author eagle.sakura
     * @version 2010/07/20 : 新規作成
     */
    public MultiTouchDisplay() {
        super(2);
    }

    /**
     * ピンチ・インの判定を行う。
     * @author eagle.sakura
     * @return
     * @version 2010/07/20 : 新規作成
     */
    public boolean isPinchIn() {
        TouchPoint tp0 = getTouchPoint(0), tp1 = getTouchPoint(1);

        //!	どっちかが離されていたら認識しない
        if (tp0.isRelease() || tp1.isRelease()) {
            return false;
        }

        Vector2 v0 = new Vector2(tp0.getDrugVectorX(), tp0.getDrugVectorY()), v1 = new Vector2(tp1.getDrugVectorX(), tp1.getDrugVectorY());

        if (v0.length() < pinchLength || v1.length() < pinchLength) {
            return false;
        }

        float dot = v0.dot(v1);

        //!	同じ方向にベクトルが向いている。
        if (dot > 0) {
            return false;
        }

        v0.set(tp0.getTouchPosX(), tp0.getTouchPosY());
        v1.set(tp1.getTouchPosX(), tp1.getTouchPosY());
        float start = v0.length(v1);

        v0.set(tp0.getCurrentX(), tp0.getCurrentY());
        v1.set(tp1.getCurrentX(), tp1.getCurrentY());
        float end = v0.length(v1);

        //!	距離が狭まった
        return end < start;
    }

    /**
     * ピンチ・アウトの判定を行う。
     * @author eagle.sakura
     * @return
     * @version 2010/07/20 : 新規作成
     */
    public boolean isPinchOut() {
        TouchPoint tp0 = getTouchPoint(0), tp1 = getTouchPoint(1);

        //!	どっちかが離されていたら認識しない
        if (tp0.isRelease() || tp1.isRelease()) {
            return false;
        }

        Vector2 v0 = new Vector2(tp0.getDrugVectorX(), tp0.getDrugVectorY()), v1 = new Vector2(tp1.getDrugVectorX(), tp1.getDrugVectorY());

        if (v0.length() < pinchLength || v1.length() < pinchLength) {
            return false;
        }

        float dot = v0.dot(v1);

        //!	同じ方向にベクトルが向いている。
        if (dot > 0) {
            return false;
        }

        v0.set(tp0.getTouchPosX(), tp0.getTouchPosY());
        v1.set(tp1.getTouchPosX(), tp1.getTouchPosY());
        float start = v0.length(v1);

        v0.set(tp0.getCurrentX(), tp0.getCurrentY());
        v1.set(tp1.getCurrentX(), tp1.getCurrentY());
        float end = v0.length(v1);

        //!	距離が広まった
        return end > start;
    }

    /**
     * タッチイベントを管理する。
     * @author eagle.sakura
     * @param me
     * @return
     * @version 2010/07/20 : 新規作成
     */
    @Override
    public boolean onTouchEvent(MotionEvent me) {

        try {
            int index = (me.getAction() & 0xff00) >> 8;
            int id = (me.getPointerId(index));
            int action = (me.getAction() & 0xff);

            /*
            if( action != 2 )
            {
                EagleUtil.log( "ID : " +  me.getPointerId( index ) );
                EagleUtil.log( "index  : " +  index );
                EagleUtil.log( "action : " +  action );
            }
            */

            if (index >= 2) {
                return true;
            }

            TouchPoint tp0 = touchPoints[id];
            switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return tp0.onActionDown(me.getX(), me.getY());
            case MotionEvent.ACTION_UP:
                return tp0.onActionUp(me.getX(), me.getY());
            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < me.getPointerCount(); ++i) {
                    int _id = me.getPointerId(i);
                    TouchPoint point = getTouchPoint(_id);
                    point.onActionMove(me.getX(i), me.getY(i));
                }
            }
                return true;
            }

            TouchPoint tp = touchPoints[id];
            switch (action) {
            case MotionEvent.ACTION_POINTER_UP:
                return tp.onActionUp(me.getX(index), me.getY(index));
            case MotionEvent.ACTION_POINTER_DOWN:
                return tp.onActionDown(me.getX(index), me.getY(index));
            }

        } catch (Exception e) {
            EagleUtil.log(e);
        }
        return true;
    }

    /**
     * ピンチアウトが始まった瞬間にtrueを返す。
     * @return
     */
    public boolean isPinchOutStarting() {
        return (eAttrPinchOut & attrNow) != 0 && (eAttrPinchOut & attrBefore) == 0;
    }

    /**
     * ピンチインが始まった瞬間にtrueを返す。
     * @return
     */
    public boolean isPinchInStarting() {
        return (eAttrPichIn & attrNow) != 0 && (eAttrPichIn & attrBefore) == 0;
    }

    private Vector2 beforePosition = new Vector2();

    private Vector2 currentPosition = new Vector2();

    /**
     * 1フレームでのXの移動量。
     * @return
     */
    public float getFrameDrugX() {
        return currentPosition.x - beforePosition.x;
    }

    /**
     * 1フレームでのYの移動量。
     * @return
     */
    public float getFrameDrugY() {
        return currentPosition.y - beforePosition.y;
    }

    public int getEnableTouchPoints() {
        int result = 0;
        if (getTouchPoint(0).isTouch()) {
            result++;
        }
        if (getTouchPoint(1).isTouch()) {
            result++;
        }
        return result;
    }

    /**
     * 毎フレームの情報更新。
     */
    @Override
    public void update() {
        super.update();

        //! 前のフレームの属性情報
        attrBefore = attrNow;

        //! 新しい属性情報
        attrNow = 0;
        if (isPinchIn()) {
            attrNow |= eAttrPichIn;
        }
        if (isPinchOut()) {
            attrNow |= eAttrPinchOut;
        }

        //! 位置の保存
        beforePosition.set(currentPosition);
        currentPosition.set(getTouchPoint(0).getCurrentX(), getTouchPoint(0).getCurrentY());

    }
}
