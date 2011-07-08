/**
 *
 */
package com.eaglesakura.lib.android.gles11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * @author SAKURA
 *
 */
public class QuadPolygon {
    private VRAMResource vertex = null;

    public QuadPolygon(GLManager gl, float sizeX, float sizeY) {

        vertex = new VRAMResource(gl);

        // ! pos,uv
        vertex.create(2);

        // ! 位置情報
        {
            float[] positions = { -0.5f * sizeX, 0.5f * sizeY, 0.0f, 0.5f * sizeX, 0.5f * sizeY, 0.0f, -0.5f * sizeX, -0.5f * sizeY, 0.0f, 0.5f * sizeX,
                    -0.5f * sizeY, 0.0f };
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);
            vertex.toGLBuffer(0, fb, fb.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);
        }

        // ! UV情報
        {
            float[] positions = { 0, 0, 1.0f, 0, 0, 1.0f, 1.0f, 1.0f, };
            ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
            bb.order(ByteOrder.nativeOrder());
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(positions);
            fb.position(0);
            vertex.toGLBuffer(1, fb, fb.capacity() * 4, GL11.GL_ARRAY_BUFFER, GL11.GL_STATIC_DRAW);
        }
    }

    private void bind(GL11 gl11) {
        // ! Posバインド
        {
            gl11.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            vertex.bind(0, GL11.GL_ARRAY_BUFFER);
            gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

        }

        // ! UVバインド
        {
            gl11.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vertex.bind(1, GL11.GL_ARRAY_BUFFER);
            gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
        }
    }

    private void unbind(GL11 gl11) {
        gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }

    /**
     * 現在の行列にしたがって四角形を描画する。
     */
    public void draw(GLManager gl) {

        GL11 gl11 = gl.getGL();
        bind(gl11);
        {
            gl11.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        }
        unbind(gl11);
    }

    public void dispose() {
        vertex.dispose();
    }
}
