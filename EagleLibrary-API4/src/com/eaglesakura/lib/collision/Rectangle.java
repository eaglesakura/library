/**
 *
 * @author eagle.sakura
 * @version 2010/10/10 : 新規作成
 */
package com.eaglesakura.lib.collision;

import com.eaglesakura.lib.math.Vector3;

/**
 * @author eagle.sakura
 * @version 2010/10/10 : 新規作成
 */
public class Rectangle {
    public float x, y, w, h;

    /**
     *
     * @author eagle.sakura
     * @version 2010/10/10 : 新規作成
     */
    public Rectangle() {

    }

    /**
     * 四角形を形成する
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param w
     * @param h
     * @version 2010/10/10 : 新規作成
     */
    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public float getLeft() {
        return x;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public float getTop() {
        return y;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public float getRight() {
        return x + w;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public float getBottom() {
        return y + h;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public float getWidth() {
        return w;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public float getHeight() {
        return h;
    }

    /**
     * 特定点を内包しているかを判断する。
     *
     * @author eagle.sakura
     * @param p
     * @return
     * @version 2010/10/10 : 新規作成
     */
    public boolean isIntersect(Vector3 p) {
        return getLeft() <= p.x && getTop() <= p.y && getRight() >= p.x && getBottom() >= p.y;
    }
}
