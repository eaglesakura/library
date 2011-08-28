/**
 *
 */
package com.eaglesakura.lib.android.mqo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.eaglesakura.lib.android.gles11.GLManager;
import com.eaglesakura.lib.mqo.MQOVertex;
import com.eaglesakura.lib.mqo.MQOVertexBuffer;

/**
 * MQO用の頂点バッファ。<BR>
 * 位置、UV、色を管理する。
 */
public class VertexBuffer extends GLObject {
    private int vertices = 0;

    /**
     * 1頂点のサイズ
     */
    public static int eVertexSize = (4 * 3 + 4 * 2 + 4 * 4);

    public static int eVertexOnceLength = eVertexSize / 4;

    float[] originBuffer = null;

    private boolean colorEnable = true;

    /**
     *
     * @param origin
     */
    public VertexBuffer(GLManager glManager, MQOVertexBuffer origin) {
        setGLManager(glManager);
        vertices = getGLManager().genVBO();
        //! データ作成
        GL11 gl = getGLManager().getGL();
        FloatBuffer fb = ByteBuffer.allocateDirect(eVertexSize * origin.getVertexCount()).order(ByteOrder.nativeOrder()).asFloatBuffer();

        originBuffer = new float[(3 + 2 + 4) * origin.getVertexCount()];
        int current = 0;
        for (int i = 0; i < origin.getVertexCount(); ++i) {
            MQOVertex v = origin.getVertex(i);
            //! 位置
            {
                originBuffer[current] = v.position.x;
                ++current;
                originBuffer[current] = v.position.y;
                ++current;
                originBuffer[current] = v.position.z;
                ++current;
            }

            //! UV
            {
                originBuffer[current] = v.uv.x;
                ++current;
                originBuffer[current] = v.uv.y;
                ++current;
            }
            //! 色
            /*
            */
            {
                originBuffer[current] = v.color.r;
                ++current;
                originBuffer[current] = v.color.g;
                ++current;
                originBuffer[current] = v.color.b;
                ++current;
                originBuffer[current] = v.color.a;
                ++current;
            }
        }
        fb.put(originBuffer);
        fb.position(0);

        //! bind -> update -> unbind
        {
            gl.glDisable(GL10.GL_CULL_FACE);
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertices);
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, fb.capacity() * 4, fb, GL11.GL_STATIC_DRAW);
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        }

        //        rebind(gl, GL11.GL_DYNAMIC_DRAW);
    }

    /**
     *
     * @return
     */
    public float[] getOriginBuffer() {
        return originBuffer;
    }

    /**
     * オリジナルのバッファを再度転送する。
     * @param gl
     */
    public void rebind(GL11 gl, int bufferType) {
        FloatBuffer fb = ByteBuffer.allocateDirect(originBuffer.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

        fb.put(originBuffer);
        fb.position(0);

        {
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertices);
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, fb.capacity() * 4, fb, bufferType);
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        }
    }

    /**
     * 頂点カラーの有効・無効を切り替える。
     * @param colorEnable
     */
    public void setColorEnable(boolean colorEnable) {
        this.colorEnable = colorEnable;
    }

    /**
     * 頂点カラーが有効な場合はtrueを返す。
     * @return
     */
    public boolean isColorEnable() {
        return colorEnable;
    }

    @Override
    public void bind() {
        GL11 gl = getGLManager().getGL();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertices);

        gl.glVertexPointer(3, GL10.GL_FLOAT, eVertexSize, 0);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, eVertexSize, (4 * 3));

        if (isColorEnable()) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glColorPointer(4, GL10.GL_FLOAT, eVertexSize, (4 * 3) + (4 * 2));
        } else {
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
    }

    @Override
    public void unbind() {
        GL11 gl = getGLManager().getGL();

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void dispose() {
        if (vertices != 0) {
            getGLManager().delVBO(vertices);
        }
    }
}
