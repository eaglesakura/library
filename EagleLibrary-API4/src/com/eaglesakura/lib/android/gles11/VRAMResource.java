/**
 *
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

import java.nio.Buffer;

import javax.microedition.khronos.opengles.GL11;

/**
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
public class VRAMResource {
    private int[] buffers = null;
    private GLManager glMgr = null;

    /**
     *
     * @author eagle.sakura
     * @param mgr
     * @version 2009/11/17 : 新規作成
     */
    public VRAMResource(GLManager mgr) {
        glMgr = mgr;
    }

    /**
     * GL内にバッファを確保する。
     *
     * @author eagle.sakura
     * @param bufferNum
     * @version 2009/11/17 : 新規作成
     * @see GL11#GL_ARRAY_BUFFER
     * @see GL11#GL_ARRAY_BUFFER_BINDING
     */
    public void create(int bufferNum) {
        GL11 gl = glMgr.getGL();
        buffers = new int[bufferNum];
        gl.glGenBuffers(bufferNum, buffers, 0);
    }

    /**
     * バッファをGLに関連付ける。
     *
     * @author eagle.sakura
     * @param index
     * @param bufferType
     * @version 2010/07/25 : 新規作成
     */
    public void bind(int index, int bufferType) {
        GL11 gl = glMgr.getGL();
        gl.glBindBuffer(bufferType, buffers[index]);
    }

    /**
     * バッファの関連付けを解除する。
     *
     * @author eagle.sakura
     * @param bufferType
     * @version 2010/10/11 : 新規作成
     */
    public void unbind(int bufferType) {
        glMgr.getGL().glBindBuffer(bufferType, 0);
    }

    /**
     * バッファをバインドし、転送する。
     *
     * @author eagle.sakura
     * @param index
     *            バッファ番号
     * @param buffer
     *            実バッファ
     * @param ussage
     *            メモリの使用方法
     * @version 2009/11/17 : 新規作成
     * @see GL11#GL_STATIC_DRAW
     */
    public void toGLBuffer(int index, Buffer buffer, int bufferSize, int bufferType, int ussage) {
        GL11 gl = glMgr.getGL();
        gl.glBindBuffer(bufferType, buffers[index]);
        gl.glBufferData(bufferType, bufferSize, buffer, ussage);
        gl.glBindBuffer(bufferType, 0);
    }

    /**
     * メモリを解放する。
     *
     * @author eagle.sakura
     * @version 2009/11/17 : 新規作成
     */
    public void dispose() {
        GL11 gl = glMgr.getGL();
        gl.glDeleteBuffers(buffers.length, buffers, 0);
    }
}
