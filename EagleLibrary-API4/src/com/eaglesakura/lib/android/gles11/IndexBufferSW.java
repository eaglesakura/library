/**
 *
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * @author eagle.sakura
 * @version 2010/07/08 : 新規作成
 */
public class IndexBufferSW implements IIndexBuffer {
    private ShortBuffer indexBuffer = null;
    private int bufferLength = 0;
    private GLManager glManager = null;

    /**
     * 空のインデックスバッファを作成する。
     *
     * @author eagle.sakura
     * @version 2009/11/15 : 新規作成
     */
    public IndexBufferSW(GLManager glManager) {
        this.glManager = glManager;
    }

    /**
     * 頂点バッファを作成する。
     *
     * @author eagle.sakura
     * @param index
     * @version 2009/11/15 : 新規作成
     */
    public void init(short[] index) {
        if (indexBuffer != null) {
            indexBuffer.put(index);
            indexBuffer.position(0);
        } else {
            indexBuffer = IGLResource.createBuffer(index);
        }
        bufferLength = index.length;
    }

    /**
     * 描画を行う。
     *
     * @author eagle.sakura
     * @param glMgr
     * @version 2009/11/16 : 新規作成
     */
    @Override
    public void drawElements() {
        GL11 gl = glManager.getGL();
        // ! インデックスバッファの描画を行う。
        gl.glDrawElements(GL10.GL_TRIANGLES, bufferLength, GL10.GL_UNSIGNED_SHORT, indexBuffer);
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
        indexBuffer = null;
    }

}
