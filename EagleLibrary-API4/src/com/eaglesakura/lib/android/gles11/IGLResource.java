/**
 * GLリソースを利用した描画オブジェクトを定義する。
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
package com.eaglesakura.lib.android.gles11;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.eaglesakura.lib.util.Disposable;


/**
 * OpenGLで使用する資源を表す。
 *
 * @author eagle.sakura
 * @version 2009/11/14 : 新規作成
 */
public abstract class IGLResource implements Disposable {
    /**
     * リソースをデバイスに転送する。
     *
     * @author eagle.sakura
     * @param gl
     * @version 2009/11/14 : 新規作成
     */
    public abstract void bind(GLManager glMgr);

    /**
     * リソースをデバイスから切り離す。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    public abstract void unbind(GLManager glMgr);

    /**
     * リソースを解放する。
     *
     * @author eagle.sakura
     * @version 2009/11/14 : 新規作成
     */
    @Override
    public abstract void dispose();

    /**
     * バッファを作成する。
     *
     * @author eagle.sakura
     * @param buf
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final IntBuffer createBuffer(int[] buf) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buf.length * 4);
        bb.order(ByteOrder.nativeOrder()); // !< ネイティブのオーダーに合わせる。
        IntBuffer ret = bb.asIntBuffer();
        ret.put(buf);
        ret.position(0);

        return ret;
    }

    /**
     * バッファを作成する。
     *
     * @author eagle.sakura
     * @param buf
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final ShortBuffer createBuffer(short[] buf) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buf.length * 2);
        bb.order(ByteOrder.nativeOrder()); // !< ネイティブのオーダーに合わせる。
        ShortBuffer ret = bb.asShortBuffer();
        ret.put(buf);
        ret.position(0);

        return ret;
    }

    /**
     * バッファを作成する。
     *
     * @author eagle.sakura
     * @param buf
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final ByteBuffer createBuffer(byte[] buf) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buf.length);
        bb.order(ByteOrder.nativeOrder()); // !< ネイティブのオーダーに合わせる。
        bb.put(buf);
        bb.position(0);

        return bb;
    }

    /**
     * バッファを作成する。
     *
     * @author eagle.sakura
     * @param buf
     * @return
     * @version 2009/11/15 : 新規作成
     */
    public static final FloatBuffer createBuffer(float[] buf) {
        ByteBuffer bb = ByteBuffer.allocateDirect(buf.length * 4);
        bb.order(ByteOrder.nativeOrder()); // !< ネイティブのオーダーに合わせる。
        FloatBuffer ret = bb.asFloatBuffer();
        ret.put(buf);
        ret.position(0);
        return ret;
    }
}
