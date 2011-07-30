/**
 *
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.eaglesakura.lib.util.EagleUtil;


/**
 * 頂点バッファを管理する。<BR>
 * VRAMに頂点情報を予め転送するため、 高速な描画を行える。
 *
 * @author eagle.sakura
 * @version 2010/07/25 : 新規作成
 */
public class VertexBufferHW implements IVertexBuffer {
    private GLManager glManager = null;
    private VRAMResource[] vrams = null;
    @SuppressWarnings("all")
    private int vertexNum = 0;
    private int attibutes = 0;

    /**
     * @author eagle.sakura
     * @param glManager
     * @version 2010/07/25 : 新規作成
     */
    public VertexBufferHW(GLManager glManager) {
        this.glManager = glManager;
        vrams = new VRAMResource[4];
    }

    private static int eVramIndexPositions = 0;
    private static int eVramIndexColors = 1;
    private static int eVramIndexUVs = 2;
    private static int eVramIndexNormals = 3;

    /**
     * 位置バッファへ転送する。
     *
     * @author eagle.sakura
     * @param positions
     * @version 2010/07/25 : 新規作成
     */
    public void initPosBuffer(float[] positions) {
        if (positions != null) {
            vertexNum = (positions.length / 3);
            vrams[eVramIndexPositions] = new VRAMResource(glManager);
            vrams[eVramIndexPositions].create(1);
            VRAMResource vram = vrams[eVramIndexPositions];

            IntBuffer buffer = GLManager.toGLFixed(positions, (ByteBuffer.allocateDirect(positions.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer()));
            buffer.position(0);
            vram.toGLBuffer(0, buffer, buffer.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);

            attibutes |= 0x1 << eVramIndexPositions;
        }
    }

    /**
     * UVバッファへ転送する。
     *
     * @author eagle.sakura
     * @param uv
     * @version 2010/07/25 : 新規作成
     */
    public void initUVBuffer(float[] uv) {
        if (uv != null) {
            vrams[eVramIndexUVs] = new VRAMResource(glManager);
            vrams[eVramIndexUVs].create(1);
            VRAMResource vram = vrams[eVramIndexUVs];

            IntBuffer buffer = GLManager.toGLFixed(uv, (ByteBuffer.allocateDirect(uv.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer()));
            buffer.position(0);
            vram.toGLBuffer(0, buffer, buffer.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);

            attibutes |= 0x1 << eVramIndexUVs;
        }
    }

    /**
     * 色バッファへ転送する。
     *
     * @author eagle.sakura
     * @param colors
     * @version 2010/07/25 : 新規作成
     */
    public void initColBuffer(byte[] colors) {
        if (colors != null) {
            vrams[eVramIndexColors] = new VRAMResource(glManager);
            vrams[eVramIndexColors].create(1);
            VRAMResource vram = vrams[eVramIndexColors];

            ByteBuffer buffer = IGLResource.createBuffer(colors);
            vram.toGLBuffer(0, buffer, buffer.capacity(), GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);

            attibutes |= 0x1 << eVramIndexColors;
        }
    }

    /**
     * 法線バッファへ転送する。
     *
     * @author eagle.sakura
     * @param normals
     * @version 2010/07/25 : 新規作成
     */
    public void initNormalBuffer(float[] normals) {
        if (normals != null) {
            vrams[eVramIndexNormals] = new VRAMResource(glManager);
            vrams[eVramIndexNormals].create(1);
            VRAMResource vram = vrams[eVramIndexNormals];

            IntBuffer buffer = GLManager.toGLFixed(normals, (ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer()));
            buffer.position(0);
            vram.toGLBuffer(0, buffer, buffer.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);

            attibutes |= 0x1 << eVramIndexNormals;
        }
    }

    /**
     * @author eagle.sakura
     * @version 2010/07/25 : 新規作成
     */
    @Override
    public void bind() {
        GL11 gl = glManager.getGL();
        // ! 位置
        if (EagleUtil.isFlagOn(attibutes, 0x1 << eVramIndexPositions)) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            vrams[eVramIndexPositions].bind(0, GL11.GL_ARRAY_BUFFER);
            gl.glVertexPointer(3, GL10.GL_FIXED, 0, 0);
        } else {
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }

        // ! 法線
        if (EagleUtil.isFlagOn(attibutes, 0x1 << eVramIndexNormals)) {
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            vrams[eVramIndexNormals].bind(0, GL11.GL_ARRAY_BUFFER);
            gl.glNormalPointer(GL10.GL_FIXED, 0, 0);
        } else {
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        }

        // ! 色
        if (EagleUtil.isFlagOn(attibutes, 0x1 << eVramIndexColors)) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vrams[eVramIndexColors].bind(0, GL11.GL_ARRAY_BUFFER);
            gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, 0);
        } else {
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }

        // ! UV
        if (EagleUtil.isFlagOn(attibutes, 0x1 << eVramIndexUVs)) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vrams[eVramIndexUVs].bind(0, GL11.GL_ARRAY_BUFFER);
            gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, 0);
        } else {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }

    /**
     * @author eagle.sakura
     * @version 2010/07/25 : 新規作成
     */
    @Override
    public void unbind() {
        GL11 gl = glManager.getGL();
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }

    /**
     * @author eagle.sakura
     * @version 2010/07/25 : 新規作成
     */
    @Override
    public void dispose() {
        for (VRAMResource vram : vrams) {
            if (vram != null) {
                vram.dispose();
            }
        }
    }

}
