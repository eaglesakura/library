/**
 *
 * @author eagle.sakura
 * @version 2009/11/15 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.eaglesakura.lib.android.math.Matrix4x4;
import com.eaglesakura.lib.math.Vector3;


/**
 * @author eagle.sakura
 * @version 2009/11/15 : 新規作成
 */
public class Camera extends IGLResource {
    /**
     * カメラ位置。
     */
    private Vector3 pos = new Vector3();
    /**
     * カメラ参照。
     */
    private Vector3 look = new Vector3();
    /**
     * カメラ上面。
     */
    private Vector3 upper = new Vector3(0.0f, 1.0f, 0.0f);

    public float getFovY() {
        return fovY;
    }

    public void setFovY(float fovY) {
        this.fovY = fovY;
    }

    public float getNearClip() {
        return nearClip;
    }

    public void setNearClip(float nearClip) {
        this.nearClip = nearClip;
    }

    public float getFarClip() {
        return farClip;
    }

    public void setFarClip(float farClip) {
        this.farClip = farClip;
    }

    /**
     * Y方向画角。
     */
    private float fovY = 1.0f;
    /**
     * アスペクト比。
     */
    private float aspect = 1.0f;
    /**
     * ニアクリップ。
     */
    private float nearClip = 0.1f;
    /**
     * ファークリップ。
     */
    private float farClip = 100.0f;

    /**
     * パース情報。
     */
    public static final int eCameraTypePerse = 0;

    /**
     * 現在設定されているカメラの種類。
     */
    @SuppressWarnings("all")
    private int eCameraType = 0;

    /**
     * GLで管理するカメラ。
     *
     * @author eagle.sakura
     * @version 2009/11/15 : 新規作成
     */
    public Camera() {

    }

    /**
     * パース情報を指定する。
     *
     * @author eagle.sakura
     * @param fovY
     * @param displayW
     * @param displayH
     * @param near
     * @param far
     * @version 2009/11/15 : 新規作成
     */
    public void setPerseData(float fovY, int displayW, int displayH, float near, float far) {
        nearClip = near;
        farClip = far;
        aspect = ((float) displayW) / ((float) displayH);
        this.fovY = fovY;
        eCameraType = eCameraTypePerse;
    }

    /**
     * 位置を設定する。
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param z
     * @version 2009/11/15 : 新規作成
     */
    public void setPos(float x, float y, float z) {
        pos.set(x, y, z);
    }

    /**
     * 位置ベクトルの参照を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/19 : 新規作成
     */
    public Vector3 getPos() {
        return pos;
    }

    /**
     * 参照点を取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public Vector3 getLook() {
        return look;
    }

    /**
     * 上方向ベクトルを取得する。
     *
     * @author eagle.sakura
     * @return
     * @version 2009/11/29 : 新規作成
     */
    public Vector3 getUpper() {
        return upper;
    }

    /**
     * 参照を設定する。
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param z
     * @version 2009/11/15 : 新規作成
     */
    public void setLook(float x, float y, float z) {
        look.set(x, y, z);
    }

    /**
     * 上を指定する。
     *
     * @author eagle.sakura
     * @param x
     * @param y
     * @param z
     * @version 2009/11/15 : 新規作成
     */
    public void setUpper(float x, float y, float z) {
        upper.set(x, y, z);
    }

    /**
     * 視線変換行列を作成する。
     *
     * @author eagle.sakura
     * @param result
     * @return
     * @version 2010/10/03 : 新規作成
     */
    public Matrix4x4 getLookMatrix(Matrix4x4 result) {
        result.lookAt(getPos(), getLook(), getUpper());
        return result;
    }

    /**
     * 射影行列を作成する。
     *
     * @author eagle.sakura
     * @param result
     * @return
     * @version 2010/10/03 : 新規作成
     */
    public Matrix4x4 getProjectionMatrix(Matrix4x4 result) {
        result.projection(nearClip, farClip, fovY, aspect);
        return result;
    }

    /**
     *
     * @author eagle.sakura
     * @param origin
     * @param result
     * @return
     * @version 2010/10/03 : 新規作成
     */
    public Vector3 lookTransform(Vector3 origin, Vector3 result) {
        getLookMatrix(new Matrix4x4()).transVector(origin, result);
        return result;
    }

    /**
     *
     * @author eagle.sakura
     * @param origin
     * @param aspect
     * @param origin
     * @return
     * @version 2010/10/03 : 新規作成
     */
    public Vector3 projectionTransform(Vector3 origin, Vector3 result) {
        // ! 視線変換
        Matrix4x4 m = getLookMatrix(new Matrix4x4());
        m.transVector(origin, result);

        // ! 画面座標変換
        getProjectionMatrix(m);
        m.transVector(result, result);
        return result;
    }

    public Matrix4x4 getCameraMatrix(Matrix4x4 result) {
        getLookMatrix(result).multiply(getProjectionMatrix(new Matrix4x4()));

        return result;
    }

    /**
     * リソースをデバイスに転送する。
     *
     * @author eagle.sakura
     * @param gl
     * @version 2009/11/14 : 新規作成
     */
    @Override
    public void bind(GLManager glMgr) {
        GL11 gl = glMgr.getGL();
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // gl.glPopMatrix();
        gl.glLoadIdentity();

        Matrix4x4 matrix = new Matrix4x4();
        gl.glLoadMatrixf(getCameraMatrix(matrix).m, 0);
        {
            // gl.glLoadMatrixf( getProjectionMatrix( matrix ).m, 0 );
            // matrix.projection( nearClip, farClip, fovY,
            // glMgr.getDisplayAspect() );
            // gl.glLoadMatrixf( matrix.m, 0 );
        }

        {
            // matrix.lookAt( getPos(), getLook(), getUpper() );
            // gl.glPushMatrix();
            // gl.glMultMatrixf( matrix.m, 0 );
        }
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        /*
         * gl.glMatrixMode( GL11.GL_MODELVIEW ); gl.glLoadIdentity();
         * GLU.gluLookAt( gl, pos.x, pos.y, pos.z, look.x, look.y, look.z,
         * upper.x, upper.y, upper.z );
         */
    }

    /**
     * リソースをデバイスから切り離す。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    @Override
    public void unbind(GLManager glMgr) {
    }

    /**
     * リソースを解放する。
     *
     * @param gl
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    @Override
    public void dispose() {

    }

}
