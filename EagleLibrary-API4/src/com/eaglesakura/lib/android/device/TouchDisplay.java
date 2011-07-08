/**
 *
 * @author eagle.sakura
 * @version 2009/11/19 : 新規作成
 */
package com.eaglesakura.lib.android.device;

import com.eaglesakura.lib.util.EagleUtil;

import android.graphics.Point;
import android.view.MotionEvent;

/**
 * @author eagle.sakura
 * @version 2009/11/19 : 新規作成
 * @version 2010/07/20 : マルチタッチ対応
 */
public class TouchDisplay {
    protected TouchPoint[] touchPoints;

    /**
     * ディスプレイマネージャ。<BR>
     * 現時点ではシングルタッチのみ対応している。
     *
     * @author eagle.sakura
     * @version 2009/11/19 : 新規作成
     */
    public TouchDisplay() {
        this(1);
    }

    /**
     *
     * @author eagle.sakura
     * @param nums
     * @version 2010/07/20 : 新規作成
     */
    protected TouchDisplay(int nums) {
        touchPoints = new TouchPoint[nums];
        for (int i = 0; i < nums; ++i) {
            touchPoints[i] = new TouchPoint(i);
        }
    }

    /**
     * タッチ座を取得する。
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/07/20 : 新規作成
     */
    public TouchPoint getTouchPoint(int index) {
        return touchPoints[index];
    }

    /**
     * タッチ系のイベントが発生した。
     *
     * @author eagle.sakura
     * @param me
     * @version 2009/11/19 : 新規作成
     */
    public boolean onTouchEvent(MotionEvent me) {
        TouchPoint tp = touchPoints[0];

        switch (me.getAction()) {
        case MotionEvent.ACTION_DOWN:
            return tp.onActionDown(me.getX(), me.getY());
        case MotionEvent.ACTION_MOVE:
            return tp.onActionMove(me.getX(), me.getY());
        case MotionEvent.ACTION_UP:
            return tp.onActionUp(me.getX(), me.getY());
        case MotionEvent.ACTION_OUTSIDE:
            return tp.onActionOutside(me.getX(), me.getY());
        }

        return true;
    }

    /**
     * ドラッグされた距離を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/19 : 新規作成
     */
    public int getDrugVectorX() {
        return touchPoints[0].getDrugVectorX();
    }

    /**
     * 画面に触れた位置を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/12/06 : 新規作成
     */
    public int getTouchPosX() {
        return touchPoints[0].getTouchPosX();
    }

    /**
     * 画面に触れた位置を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/12/06 : 新規作成
     */
    public int getTouchPosY() {
        return touchPoints[0].getTouchPosY();
    }

    /**
     * ドラッグされた距離を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/19 : 新規作成
     */
    public int getDrugVectorY() {
        return touchPoints[0].getDrugVectorY();
    }

    /**
     * タッチされているかを調べる。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/19 : 新規作成
     */
    public boolean isTouch() {
        return touchPoints[0].isTouch();
    }

    /**
     * ディスプレイから指が離れているか。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/21 : 新規作成
     */
    public boolean isRelease() {
        return touchPoints[0].isRelease();
    }

    /**
     * ディスプレイから指が離れた瞬間か。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/05/21 : 新規作成
     */
    public boolean isReleaseOnce() {
        return touchPoints[0].isReleaseOnce();
    }

    /**
     * タッチされているかを調べる。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/19 : 新規作成
     */
    public boolean isTouchOnce() {
        return touchPoints[0].isTouchOnce();
    }

    /**
     * 毎フレームの更新を行う。
     *
     * @author eagle.sakura
     * @version 2009/11/19 : 新規作成
     */
    public void update() {
        for (TouchPoint tp : touchPoints) {
            tp.update();
        }
    }

    /**
     * @author eagle.sakura
     * @version 2010/07/19 : 新規作成
     */
    public static class TouchPoint {
        /**
         * 属性情報。
         */
        private int attribute = 0x0;
        /**
         * 前フレームの属性情報。
         */
        private int attrOld = 0x0;
        /**
         * 現フレームの属性情報。
         */
        private int attrNow = 0x0;
        /**
         * タッチしていた時間。
         */
        @SuppressWarnings("all")
        private int touchTimeMs = 0;
        /**
         * タッチ開始した時間。
         */
        private long touchStartTime = 0;

        /**
         * タッチしていた時間（フレーム）
         */
        private int touchFrame = 0;
        /**
         * タッチした座標。
         */
        private Point touchPos = new Point();
        /**
         * 離した位置。
         */
        private Point releasePos = new Point();

        /**
         * ディスプレイに触れている。
         */
        private static final int eAttrTouch = 1 << 0;

        @SuppressWarnings("all")
        private int id = -1;

        /**
         * タッチ一箇所の値に対応している。
         *
         * @author eagle.sakura
         * @param id
         * @version 2010/07/19 : 新規作成
         */
        public TouchPoint(int id) {
            this.id = id;
        }

        /**
         * タッチされた。
         *
         * @author eagle.sakura
         * @param me
         * @version 2010/07/19 : 新規作成
         */
        protected boolean onActionDown(float x, float y) {
            touchPos.x = (int) x;
            touchPos.y = (int) y;
            releasePos.x = (int) x;
            releasePos.y = (int) y;

            touchStartTime = System.currentTimeMillis();
            attribute = EagleUtil.setFlag(attribute, eAttrTouch, true);
            return true;
        }

        /**
         * 移動された。
         *
         * @author eagle.sakura
         * @param me
         * @version 2010/07/19 : 新規作成
         */
        protected boolean onActionMove(float x, float y) {
            releasePos.x = (int) x;
            releasePos.y = (int) y;
            touchTimeMs = (int) (System.currentTimeMillis() - touchStartTime);
            attribute = EagleUtil.setFlag(attribute, eAttrTouch, true);
            return true;
        }

        /**
         * 指が離された。
         *
         * @author eagle.sakura
         * @param me
         * @return
         * @version 2010/07/19 : 新規作成
         */
        protected boolean onActionUp(float x, float y) {
            releasePos.x = (int) x;
            releasePos.y = (int) y;
            touchTimeMs = (int) (System.currentTimeMillis() - touchStartTime);
            attribute = EagleUtil.setFlag(attribute, eAttrTouch, false);
            return true;
        }

        /**
         * ディスプレイの外へ出た。
         *
         * @author eagle.sakura
         * @param me
         * @return
         * @version 2010/07/19 : 新規作成
         */
        protected boolean onActionOutside(float x, float y) {
            releasePos.x = (int) x;
            releasePos.y = (int) y;
            touchTimeMs = (int) (System.currentTimeMillis() - touchStartTime);
            attribute = EagleUtil.setFlag(attribute, eAttrTouch, false);
            return true;
        }

        /**
         * ドラッグされた距離を取得する。
         *
         * @author eagle.sakura
         * @return
         * @version 2009/11/19 : 新規作成
         */
        public int getDrugVectorX() {
            return releasePos.x - touchPos.x;
        }

        /**
         * 画面に触れた位置を取得する。
         *
         * @author eagle.sakura
         * @return
         * @version 2009/12/06 : 新規作成
         */
        public int getTouchPosX() {
            return touchPos.x;
        }

        /**
         * 画面に触れた位置を取得する。
         *
         * @author eagle.sakura
         * @return
         * @version 2009/12/06 : 新規作成
         */
        public int getTouchPosY() {
            return touchPos.y;
        }

        /**
         * 現在の指の位置、もしくは離した位置を取得する。
         *
         * @author eagle.sakura
         * @return
         * @version 2010/07/20 : 新規作成
         */
        public int getCurrentX() {
            return releasePos.x;
        }

        /**
         * 現在の指の位置、もしくは離した位置を取得する。
         *
         * @author eagle.sakura
         * @return
         * @version 2010/07/20 : 新規作成
         */
        public int getCurrentY() {
            return releasePos.y;
        }

        /**
         * 指定地点までの距離を取得する。
         *
         * @author eagle.sakura
         * @param x
         * @param y
         * @return
         * @version 2010/07/20 : 新規作成
         */
        public float getLength(int x, int y) {
            int lx = releasePos.x - x, ly = releasePos.y - y;
            return (float) Math.sqrt((double) (lx * lx + ly * ly));
        }

        /**
         * 指を引きずった長さを取得する。
         * @return
         */
        public float getDrugLength() {
            return (float) Math.sqrt(this.getDrugVectorX() * this.getDrugVectorX() + this.getDrugVectorY() * this.getDrugVectorY());
        }

        /**
         * ドラッグされた距離を取得する。
         *
         * @author eagle.sakura
         * @return
         * @version 2009/11/19 : 新規作成
         */
        public int getDrugVectorY() {
            return releasePos.y - touchPos.y;
        }

        /**
         * タッチされているかを調べる。
         *
         * @author eagle.sakura
         * @return
         * @version 2009/11/19 : 新規作成
         */
        public boolean isTouch() {
            return EagleUtil.isFlagOn(attrNow, eAttrTouch);
        }

        /**
         * ディスプレイから指が離れているか。
         *
         * @author eagle.sakura
         * @return
         * @version 2010/05/21 : 新規作成
         */
        public boolean isRelease() {
            return !EagleUtil.isFlagOn(attrNow, eAttrTouch);
        }

        /**
         * ディスプレイから指が離れた瞬間か。
         *
         * @author eagle.sakura
         * @return
         * @version 2010/05/21 : 新規作成
         */
        public boolean isReleaseOnce() {
            if (!EagleUtil.isFlagOn(attrNow, eAttrTouch) && EagleUtil.isFlagOn(attrOld, eAttrTouch)) {
                return true;
            }
            return false;
        }

        /**
         * タッチされているかを調べる。
         *
         * @author eagle.sakura
         * @return
         * @version 2009/11/19 : 新規作成
         */
        public boolean isTouchOnce() {
            if (EagleUtil.isFlagOn(attrNow, eAttrTouch) && !EagleUtil.isFlagOn(attrOld, eAttrTouch)) {
                return true;
            }
            return false;
        }

        /**
         * 毎フレームの更新を行う。
         *
         * @author eagle.sakura
         * @version 2009/11/19 : 新規作成
         */
        protected void update() {
            attrOld = attrNow;
            attrNow = attribute;

            if (isRelease() && !isReleaseOnce()) {
                touchFrame = 0;
            } else if (isTouch()) {
                ++touchFrame;
            }
        }

        /**
         * 何フレームタッチしたか。
         * @return
         */
        public int getTouchFrame() {
            return touchFrame;
        }
    }

}
