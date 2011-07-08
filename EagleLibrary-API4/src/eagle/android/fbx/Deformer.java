/**
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
package eagle.android.fbx;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import eagle.android.gles11.GL11Extension;
import eagle.android.gles11.GLManager;
import eagle.android.math.Matrix4x4;

/**
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
public class Deformer {
    /**
     * 関連付けられたボーン。
     */
    private Bone[] bones = null;

    /**
     * 関連付けられたボーン名。
     */
    private String[] boneNames = null;

    /**
     * 変形用頂点情報バッファ。
     */
    private DeformBufferSW deform = null;

    /**
     *
     */
    private GLManager glManager = null;

    /**
     *
     */
    private Frame parent = null;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/12 : 新規作成
     */
    public void dispose() {
        deform.dispose();
    }

    /**
     *
     * @author eagle.sakura
     * @param paletteSize
     * @param gl
     * @version 2010/07/10 : 新規作成
     */
    public Deformer(Frame frame, int paletteSize, GLManager gl) {
        parent = frame;
        glManager = gl;
        bones = new Bone[paletteSize];
        boneNames = new String[paletteSize];
    }

    /**
     *
     * @author eagle.sakura
     * @param index
     * @param name
     * @version 2010/07/08 : 新規作成
     */
    public void setBoneName(int index, String name) {
        boneNames[index] = name;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public int getNumBones() {
        return boneNames.length;
    }

    /**
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/07/08 : 新規作成
     */
    public String getBoneName(int index) {
        return boneNames[index];
    }

    /**
     * ボーンを関連付ける。
     *
     * @author eagle.sakura
     * @param index
     * @param bone
     * @version 2010/07/08 : 新規作成
     */
    public void setBone(int index, Bone bone) {
        bones[index] = bone;
    }

    /**
     *
     * @author eagle.sakura
     * @param index
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public Bone getBone(int index) {
        return bones[index];
    }

    /**
     * 頂点変形用のバッファを与える。
     *
     * @author eagle.sakura
     * @param deform
     * @version 2010/07/08 : 新規作成
     */
    public void setDeform(DeformBufferSW deform) {
        this.deform = deform;
    }

    /**
     * GLに関連付ける。
     *
     * @author eagle.sakura
     * @version 2010/07/10 : 新規作成
     */
    public void bind() {
        // ! 行列パレットを有効化
        GL11 gl11 = glManager.getGL11();
        GL11Extension gles = glManager.getGL11Extension();
        {
            gles.glEnable(GL11Ext.GL_MATRIX_PALETTE_OES);
        }

        Matrix4x4 m = new Matrix4x4();
        // ! 行列を転送
        for (int i = 0; i < getNumBones(); ++i) {
            Bone bone = bones[i];
            // ! ボーン用の行列を作成する
            gl11.glMatrixMode(GL10.GL_MODELVIEW);

            /*
             * glManager.pushMatrixF( bone.getMatrix() ); glManager.pushMatrixF(
             * bone.getInvertMatrix() ); glManager.pushMatrixF(
             * parent.getMatrix() );
             */
            glManager.pushMatrixF(bone.getWorldMatrix(m));
            glManager.pushMatrixF(parent.getMatrix());

            // ! パレットへ転送する
            gl11.glMatrixMode(GL11Ext.GL_MATRIX_PALETTE_OES);
            gles.glCurrentPaletteMatrixOES(i);
            gles.glLoadPaletteFromModelViewMatrixOES();

            gl11.glMatrixMode(GL10.GL_MODELVIEW);
            // ! 行列を元に戻す
            /*
             * glManager.popMatrix(); glManager.popMatrix();
             */
            glManager.popMatrix();
            glManager.popMatrix();
        }

        deform.bind();
    }

    /**
     * GLへの関連付けを解除する。
     *
     * @author eagle.sakura
     * @version 2010/07/10 : 新規作成
     */
    public void unbind() {
        deform.unbind();

        GL11 gl11 = glManager.getGL11();
        gl11.glDisable(GL11Ext.GL_MATRIX_PALETTE_OES);
        gl11.glMatrixMode(GL10.GL_MODELVIEW);
    }
}

/*
 * glManager.pushMatrixF( bone.getMatrix() ); glManager.pushMatrixF(
 * bone.getInvertMatrix() ); glManager.pushMatrixF( getMatrix() );
 *
 *
 * gl11.glPushMatrix(); { gl11.glMatrixMode( GL11.GL_MODELVIEW );
 * gl11.glMultMatrixx( _matrix_0, 0 ); gl11.glMatrixMode(
 * GL11Ext.GL_MATRIX_PALETTE_OES ); gles.glCurrentPaletteMatrixOES( 0 );
 * gles.glLoadPaletteFromModelViewMatrixOES(); gl11.glMatrixMode(
 * GL11.GL_MODELVIEW ); } gl11.glPopMatrix();
 */
