/**
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
package com.eaglesakura.lib.android.fbx;

import com.eaglesakura.lib.android.math.Matrix4x4;
import com.eaglesakura.lib.math.Vector3;


/**
 * アニメーションのキーフレームを管理する。
 *
 * @author eagle.sakura
 * @version 2010/07/11 : 新規作成
 */
public class KeyFrame {
    private Vector3 translate = new Vector3(), rotate = new Vector3(),
            scale = new Vector3();
    private float frame = 0;

    /**
     * フレームを指定してキーを打つ。
     *
     * @author eagle.sakura
     * @param frame
     * @version 2010/07/11 : 新規作成
     */
    public KeyFrame(float frame) {
        this.frame = frame;
    }

    /**
     * 何フレーム目に打たれているかを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public float getFrame() {
        return frame;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public Vector3 getTranslate() {
        return translate;
    }

    /**
     *
     * @author eagle.sakura
     * @param origin
     * @version 2010/07/11 : 新規作成
     */
    public void set(KeyFrame origin) {
        frame = origin.frame;
        scale.set(origin.scale);
        rotate.set(origin.rotate);
        translate.set(origin.translate);
    }

    /**
     *
     * @author eagle.sakura
     * @param result
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public Matrix4x4 calcMatrix(Matrix4x4 result) {
        Matrix4x4.create(getScale(), getRotate(), getTranslate(), result);
        return result;
    }

    /**
     * 姿勢を設定する 。
     *
     * @author eagle.sakura
     * @param s
     * @param r
     * @param t
     * @version 2010/07/11 : 新規作成
     */
    public void set(Vector3 s, Vector3 r, Vector3 t) {
        scale.set(s);
        rotate.set(r);
        translate.set(t);
    }

    /**
     * 現在フレームを書き換える。
     *
     * @author eagle.sakura
     * @param set
     * @version 2010/07/11 : 新規作成
     */
    public void setFrame(float set) {
        frame = set;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public Vector3 getRotate() {
        return rotate;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/11 : 新規作成
     */
    public Vector3 getScale() {
        return scale;
    }

}
