package com.eaglesakura.lib.android.device;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.eaglesakura.lib.android.graphic.Graphics;
import com.eaglesakura.lib.math.Vector2;

public class TouchImageView extends View {
    Bitmap bitmap = null;
    Drawable image = null;
    ScaleGestureDetector scaleGesture = null;
    GestureDetector gesture = null;
    boolean scaling = false;
    float scaleMax = 3.0f;
    float nowScaling = 1.0f;
    OnLongClickListener onLongClickListener = null;
    OnClickListener onClickListener = null;

    public TouchImageView(Context context) {
        super(context);

        scaleGesture = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                scaling = false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                scaling = true;
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                pinch(new Vector2(detector.getFocusX(), detector.getFocusY()), detector.getScaleFactor());
                invalidate();
                return true;
            }
        });

        gesture = new GestureDetector(new GestureDetector.OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent motionevent) {
                if (onClickListener != null && !scaling) {
                    onClickListener.onClick(TouchImageView.this);
                    return true;
                }
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionevent) {
            }

            @Override
            public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float x, float y) {
                if (scaling) {
                    return true;
                }

                move(-x, -y);
                invalidate();

                return true;
            }

            @Override
            public void onLongPress(MotionEvent motionevent) {
            }

            @Override
            public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float x, float y) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent motionevent) {
                return false;
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        onLongClickListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGesture.onTouchEvent(event);
        gesture.onTouchEvent(event);
        return true;
    }

    final Rect windowFrame = new Rect();
    final Rect bounds = new Rect();

    public void setWindowFrame(Rect windowFrame) {
        this.windowFrame.set(windowFrame);
        fitToOrigin();
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        image = new BitmapDrawable(bitmap);
        bounds.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        fitToOrigin();
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        setWindowFrame(windowFrame);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setWindowFrame(new Rect(left, top, right, bottom));
    }

    @Override
    public void draw(Canvas canvas) {
        if (image == null) {
            return;
        }

        Graphics graphics = new Graphics();
        graphics.setCanvas(canvas);
        graphics.setColorARGB(255, 0, 0, 0);
        graphics.fillRect(0, 0, graphics.getWidth(), graphics.getHeight());
        image.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
        image.draw(canvas);
    }

    void fitToOrigin() {
        nowScaling = 1.0f;
        if (bitmap == null) {
            return;
        }
        bounds.set(0, 0, bitmap.getWidth(), bitmap.getHeight());

        if (bounds.width() != windowFrame.width()) {
            float widthMul = (float) bounds.width() / (float) windowFrame.width();
            bounds.right /= widthMul;
            bounds.bottom /= widthMul;
        }

        if (bounds.height() > windowFrame.height()) {
            float widthMul = (float) bounds.height() / (float) windowFrame.height();
            bounds.right /= widthMul;
            bounds.bottom /= widthMul;
        }

        int centerX = windowFrame.centerX();
        int centerY = windowFrame.centerY();

        bounds.offsetTo(centerX - (bounds.width() / 2), centerY - (bounds.height() / 2));
    }

    void fitImageBounds() {
        int centerX = windowFrame.centerX();
        int centerY = windowFrame.centerY();

        if (bounds.width() < windowFrame.width() && bounds.height() < windowFrame.height()) {
            fitToOrigin();
            return;
        }

        if (bounds.width() <= windowFrame.width()) {
            bounds.offsetTo(centerX - (bounds.width() / 2), bounds.top);
        } else if (bounds.left > windowFrame.left) {
            bounds.offsetTo(windowFrame.left, bounds.top);
        } else if (bounds.right < windowFrame.right) {
            bounds.offsetTo(windowFrame.right - bounds.width(), bounds.top);
        }

        if (bounds.height() <= windowFrame.height()) {
            bounds.offsetTo(bounds.left, centerY - (bounds.height() / 2));
        } else if (bounds.top > windowFrame.top) {
            bounds.offsetTo(bounds.left, windowFrame.top);
        } else if (bounds.bottom < windowFrame.bottom) {
            bounds.offsetTo(bounds.left, windowFrame.bottom - bounds.height());
        }
    }

    void pinch(Vector2 center, float scale) {
        if ((scale * nowScaling) > scaleMax) {
            return;
        }

        Matrix matrix = new Matrix();
        RectF rect = new RectF(bounds);
        matrix.postScale(scale, scale, center.x, center.y);
        matrix.mapRect(rect);

        nowScaling *= scale;
        bounds.set((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);
        fitImageBounds();
    }

    void move(float x, float y) {
        //! 規程以下のサイズなら移動させない
        if (bounds.width() <= windowFrame.width()) {
            x = 0;
        }
        if (bounds.height() <= windowFrame.height()) {
            y = 0;
        }
        bounds.offset((int) x, (int) y);
        fitImageBounds();
    }

}
