package com.eaglesakura.lib.android.device;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.eaglesakura.lib.math.Vector2;

/**
 * ジェスチャ管理を行う。
 * @author SAKURA
 *
 */
public class GestureManager {

    /**
     * スケーリング用のジェスチャ
     */
    ScaleGestureDetector scaleGestureDetector = null;

    /**
     * 通常の操作ジェスチャ
     */
    GestureDetector gestureDetector = null;

    /**
     * スケーリング操作中だったらtrue
     */
    boolean scaling = false;

    /**
     * 操作用ハンドラ。
     */
    Handler handler = new Handler();

    protected Vector2 trans(MotionEvent e) {
        return new Vector2(e.getRawX(), e.getRawY());
    }

    protected float trans(float distance) {
        return distance;
    }

    public GestureManager(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        scaling = false;
                    }
                }, 100);
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                scaling = true;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return false;
            }
        });

        gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (scaling) {
                    return false;
                }

                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                if (scaling) {
                    return;
                }

            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (scaling) {
                    return false;
                }

                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (scaling) {
                    return;
                }

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (scaling) {
                    return false;
                }

                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                if (scaling) {
                    return false;
                }

                return false;
            }
        });
    }

    public void onTouchEvent(MotionEvent me) {
        gestureDetector.onTouchEvent(me);
        scaleGestureDetector.onTouchEvent(me);
    }

    /**
     * ジェスチャ操作用リスナ
     * @author SAKURA
     *
     */
    public interface GestureListener {

        /**
         * スケーリングが終了した。
         */
        public void onScaleEnd(GestureManager gestureManager, Vector2 focus);

        /**
         * スケーリングを開始した。
         * @param gestureManager
         * @return
         */
        public boolean onScaleBegin(GestureManager gestureManager, Vector2 focus);

        /**
         * スケーリング中
         * @param gestureManager
         * @return
         */
        public boolean onScale(GestureManager gestureManager, Vector2 focus, float scaleFactor);
    }
}
