/**
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
package com.eaglesakura.lib.android.fbx;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.android.gles11.IGLResource;

import eagle.android.gles11.GL11Extension;

/**
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
public class DeformBufferSW implements IDeformBuffer {
    /**
     * 頂点ウェイト配列。
     */
    private IntBuffer weights = null;

    /**
     * 頂点パレットの番号配列。
     */
    private ByteBuffer palettes = null;

    private GLManager glManager = null;

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/08 : 新規作成
     */
    public DeformBufferSW(GLManager glManager) {
        this.glManager = glManager;
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/12 : 新規作成
     */
    @Override
    public void dispose() {
        weights = null;
        palettes = null;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public Buffer getVertexWeight() {
        return weights;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public int getVertexWeightType() {
        return GL10.GL_FIXED;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public Buffer getPaletteIndexBuffer() {
        return palettes;
    }

    /**
     *
     * @author eagle.sakura
     * @return
     * @version 2010/07/10 : 新規作成
     */
    public int getPaletteIndexBufferType() {
        return GL10.GL_UNSIGNED_BYTE;
    }

    /**
     * 変形用バッファを作成する。
     *
     * @author eagle.sakura
     * @param weights
     * @param indices
     * @version 2010/07/08 : 新規作成
     */
    public void init(float[] weights, byte[] indices) {
        {
            ByteBuffer buffer = ByteBuffer.allocateDirect(weights.length * 4);
            buffer.order(ByteOrder.nativeOrder());
            this.weights = GLManager.toGLFixed(weights, buffer.asIntBuffer());
            this.weights.position(0);
        }

        {
            palettes = IGLResource.createBuffer(indices);
        }
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/26 : 新規作成
     */
    @Override
    public void bind() {
        GL11Extension gles = glManager.getGL11Extension();
        gles.glEnableClientState(GL11Ext.GL_MATRIX_INDEX_ARRAY_OES);
        gles.glEnableClientState(GL11Ext.GL_WEIGHT_ARRAY_OES);
        // ! パレット情報を転送
        gles.glWeightPointerOES(3, getVertexWeightType(), 0, getVertexWeight());
        gles.glMatrixIndexPointerOES(3, getPaletteIndexBufferType(), 0, getPaletteIndexBuffer());
    }

    /**
     *
     * @author eagle.sakura
     * @version 2010/07/27 : 新規作成
     */
    @Override
    public void unbind() {
        GL11 gl11 = glManager.getGL();
        gl11.glDisableClientState(GL11Ext.GL_MATRIX_INDEX_ARRAY_OES);
        gl11.glDisableClientState(GL11Ext.GL_WEIGHT_ARRAY_OES);

    }
}
