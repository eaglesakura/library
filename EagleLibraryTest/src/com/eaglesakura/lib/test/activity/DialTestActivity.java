package com.eaglesakura.lib.test.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.eaglesakura.lib.android.graphic.Graphics;
import com.eaglesakura.lib.android.view.CanvasView;
import com.eaglesakura.lib.math.Vector2;
import com.eaglesakura.lib.util.EagleUtil;

public class DialTestActivity extends BaseActivity {
    CanvasView view = null;

    GestureDetector detector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new CanvasView(this) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        };

        detector = new GestureDetector(new GestureDetector.OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                view.lock();
                refreshView();
                drawJog(new Vector2(e2.getRawX(), e2.getRawY()), new Vector2(distanceX, distanceY));
                view.unlock();
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                view.lock();
                refreshView();
                view.unlock();
                return true;
            }
        });

        setContentView(view);
    }

    void refreshView() {
        Graphics g = view.getGraphics();

        g.setColorARGB(255, 255, 255, 255);
        g.fillRect(0, 0, g.getWidth(), g.getHeight());

        g.setColorARGB(255, 255, 0, 0);
        final int eSize = 25;
        g.fillRect(g.getWidth() / 2 - eSize, g.getHeight() / 2 - eSize, eSize * 2, eSize * 2);

    }

    void drawPoint(Vector2 v, int color, float r) {
        Graphics g = view.getGraphics();
        g.setColorARGB(color);
        g.fillCircle((int) v.x, (int) v.y, r);
    }

    void drawJog(Vector2 current, Vector2 vector) {
        Graphics g = view.getGraphics();
        Vector2 centerPos = new Vector2(g.getWidth() / 2, g.getHeight() / 2);
        Vector2 before = new Vector2(current.x + vector.x, current.y + vector.y);
        int line = 0;
        g.setColorARGB(255, 0, 0, 0);
        g.drawString("current : " + current, 5, 5 + (line++) * 25, -1, -1, Graphics.eStringFlagTop | Graphics.eStringFlagLeft);
        g.drawString("before : " + before, 5, 5 + (line++) * 25, -1, -1, Graphics.eStringFlagTop | Graphics.eStringFlagLeft);

        drawPoint(current, Graphics.toColorARGB(255, 0, 255, 0), 20.0f);
        drawPoint(before, Graphics.toColorARGB(255, 0, 0, 255), 10.0f);

        float currentRotate = EagleUtil.getAngleDegree2D(centerPos, current);
        float beforeRotate = EagleUtil.getAngleDegree2D(centerPos, before);
        //        currentRotate = 350;
        //        beforeRotate = 360;

        float rotateDiff = EagleUtil.getAngleDiff(currentRotate, beforeRotate);
        g.drawString("currentRotate : " + currentRotate, 5, 5 + (line++) * 25, -1, -1, Graphics.eStringFlagTop | Graphics.eStringFlagLeft);
        g.drawString("beforeRotate : " + beforeRotate, 5, 5 + (line++) * 25, -1, -1, Graphics.eStringFlagTop | Graphics.eStringFlagLeft);
        g.drawString("diff : " + (int) rotateDiff, 5, 5 + (line++) * 25, -1, -1, Graphics.eStringFlagTop | Graphics.eStringFlagLeft);

    }

}
